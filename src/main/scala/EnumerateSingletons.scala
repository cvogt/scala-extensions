package org.cvogt.scala
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object EnumerateSingletons {
  /** singleton objects transitively extending the given class or trait */
  def apply[A]: Set[A] = macro EnumerateSingletonsMacros.enumerateSingletonsMacros[A]
}

class EnumerateSingletonsMacros( val c: Context ) {
  import c.universe._
  def enumerateSingletonsMacros[T: c.WeakTypeTag]: Tree = {
    val T = weakTypeOf[T].typeSymbol.asClass
    val ( subs, verifiers ) = knownTransitiveSubclassesAndVerifiers( T )
    val (singletons, classes) = subs.partition( _.isModuleClass )
    val nonClosed = classes.filterNot(_.isSealed).filterNot(_.isFinal)
    if(nonClosed.nonEmpty){
      c.error(
        c.enclosingPosition,
        "EnumerateSingleton requires all transitive subclasses to be sealed or final. These are not: " ++ nonClosed.mkString(", ")
      )
    }
    val trees = singletons.map( _.module ).map( m => q"$m" )
    val tree = q"""{
      ..$verifiers
      _root_.scala.collection.immutable.Set[$T](..$trees)
    }"""
    tree
  }

  /** Generates a list of all singleton objects extending the given class directly or transitively. */
  private def knownTransitiveSubclassesAndVerifiers( sym: ClassSymbol ): ( Set[ClassSymbol], List[Tree] ) = {
    val direct = knownDirectSubclassesAndVerifier( sym )
    direct._1.map( knownTransitiveSubclassesAndVerifiers ).fold(
      direct
    )(
      ( l, r ) => ( l._1 ++ r._1, l._2 ++ r._2 )
    )
  }

  private def knownDirectSubclassesAndVerifier( T: ClassSymbol ): ( Set[ClassSymbol], List[Tree] ) = {
    val subs = T.knownDirectSubclasses

    // hack to detect breakage of knownDirectSubclasses as suggested in 
    // https://gitter.im/scala/scala/archives/2015/05/05 and
    // https://gist.github.com/retronym/639080041e3fecf58ba9
    val global = c.universe.asInstanceOf[scala.tools.nsc.Global]
    def checkSubsPostTyper = if ( subs != T.knownDirectSubclasses )
      c.error(
        c.macroApplication.pos,
        s"""No child classes found for $T. If there clearly are child classes,
Try moving the call lower in the file, into a separate file, a sibbling package, a separate sbt sub project or else.
This is caused by https://issues.scala-lang.org/browse/SI-7046 and can only be avoided by manually moving the call.
It is triggered when a macro call happend in a place, where typechecking of $T hasn't been completed yet.
Completion is required in order to find subclasses.
"""
      )

    val checkSubsPostTyperTypTree =
      new global.TypeTreeWithDeferredRefCheck()( () => { checkSubsPostTyper; global.TypeTree( global.NoType ) } ).asInstanceOf[TypTree]

    val name = TypeName( c.freshName( "VerifyKnownDirectSubclassesPostTyper" ) )

    (
      subs.map( _.asClass ).toSet,
      List( q"type ${name} = $checkSubsPostTyperTypTree" )
    )
  }
}