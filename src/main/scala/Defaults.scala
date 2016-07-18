package org.cvogt.scala

import scala.reflect.macros.whitebox.Context
import scala.language.dynamics
import scala.language.experimental.macros
import macrocompat.bundle

object Defaults {
  /** returns the given type's default values in an anonymous class instance */
  def apply[T]: Any = macro DefaultsMacros.apply[T]
}

@bundle
class DefaultsMacros( val c: Context ) {
  import c.universe._
  def apply[T: c.WeakTypeTag] = {
    val T = weakTypeOf[T]
    if ( !isCaseClass( T ) )
      c.error( c.enclosingPosition, s"not a case class: $T" )
    val defs = caseClassFieldsTypes( T ).collect {
      case ( name, tpe, Some( default ) ) => q"def ${TermName( name )}: $tpe = $default"
    }
    q"new{ ..$defs }"
  }

  private def isCaseClass( tpe: Type ) = tpe.typeSymbol.isClass && tpe.typeSymbol.asClass.isCaseClass

  private def caseClassFieldsTypes( tpe: Type ): List[( String, Type, Option[Tree] )] = {
    tpe.companion.member( TermName( "apply" ) ).asTerm.alternatives.find( _.isSynthetic ).get.asMethod.paramLists.flatten.zipWithIndex.map {
      case ( field, i ) =>
        (
          field.name.toTermName.decodedName.toString,
          field.infoIn( tpe ),
          {
            val method = TermName( s"apply$$default$$${i + 1}" )
            tpe.companion.member( method ) match {
              case NoSymbol => None
              case _        => Some( q"${tpe.typeSymbol.companion}.$method" )
            }
          }
        )
    }.toList
  }
}
