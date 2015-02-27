package org.cvogt.scala.constraint.boolean
import scala.annotation.implicitNotFound

/** type-level boolean algebra */
object `package`

/** Type representing Boolean True for type-level boolean algebra */
@implicitNotFound(msg = """Cannot prove True. This should never happen. Maybe you have ambiguous implicits.""")
final class True
object True{
  implicit def succeed = new True
}
/** Type representing Boolean False for type-level boolean algebra */
@implicitNotFound(msg = """Cannot prove False""")
final class False

/** Proves that Constraint does NOT hold */
@implicitNotFound(msg = """Cannot prove ![${Constraint}]""")
final class ![Constraint]
object !{
  implicit def prove[Constraint](implicit ev: InvertDeferredForBetterError[Constraint]) = new ![Constraint]
}

/* Better error message than ambiguous implicit for ! constraint */
final class InvertDeferredForBetterError[Constraint]
object InvertDeferredForBetterError{
  /** Implicit that always succeeds */
  implicit def succeed[Constraint] = new InvertDeferredForBetterError[Constraint]
  /** Creates an ambigious implicit if Constraint holds. */
  implicit def prove[Constraint](implicit ev: Constraint) = new InvertDeferredForBetterError[Constraint]
}

/** Proves both Left and Right */
@implicitNotFound(msg = "Cannot prove ${Left} && ${Right}")
final class &&[Left, Right]
object &&{
  implicit def prove[Left,Right](implicit ev1: Left, ev2: Right) = new &&[Left,Right]
}

/** Proves Left Implies Right */
@implicitNotFound(msg = "Cannot prove ${Left} Implies ${Right}")
final class Implies[Left, Right]
object Implies{
  implicit def prove[Left,Right](implicit ev: ![Left] || Right) = new Implies[Left,Right]
}

/** Proves either Left or Right but not both */
@implicitNotFound(msg = "Cannot prove ${Left} Implies ${Right}")
final class Xor[Left, Right]
object Xor{
  implicit def prove[Left,Right](implicit ev: (Left || Right) && ![Left && Right]) = new Xor[Left,Right]
}

/** Proves or disproves both Left and Right (aka Left ≡ Right, Left <=> Right) */
@implicitNotFound(msg = "Cannot prove ${Left} IdenticalTo ${Right}")
final class ==[Left, Right]
object =={
  implicit def prove[Left,Right](implicit ev: ![Left Xor Right]) = new ==[Left,Right]
}

/** Proves either Left or Right or both */
@implicitNotFound(msg = "Cannot prove ${Left} || ${Right}")
final class ||[Left, Right]
object ||{
  implicit def proveLeft[Left,Right](implicit ev: Left) = new ||[Left,Right]
  implicit def proveRight[Left,Right](implicit ev: Right && ![Left]) = new ||[Left,Right]
}
