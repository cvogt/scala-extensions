package org.cvogt.scala.constraint.set
import scala.annotation.implicitNotFound
import scala.language.higherKinds

import org.cvogt.scala.constraint.boolean._

/** type-level subset tests */
object `package`{
  /* abstract over operations, so we can so a strict and a subtyping subset check
  type SetOps[Op1[_,_],Op2[_,_]] = {
    type In2[Left,T1,T2] = (Left Op1 T1) Op2 (Left Op1 T2)
  }
  type Ops = SetOps[=:=,||]
  */
}

/** Proves that Left is a component of the tuple given as Right */
@implicitNotFound(msg = "Cannot prove ${Left} In ${Set}")
final class In[Left, Set]
object In{
  implicit def prove2[Left,T1,T2](
    implicit ev: (Left =:= T1) || (Left =:= T2)
  ) = new In[Left,(T1,T2)]

  implicit def prove3[Left,T1,T2,T3](
    implicit ev: (Left =:= T1) || (Left =:= T2) || (Left =:= T3)
  ) = new In[Left,(T1,T2,T3)]

  implicit def prove4[Left,T1,T2,T3,T4](
    implicit ev: (Left =:= T1) || (Left =:= T2) || (Left =:= T3) || (Left =:= T4)
  ) = new In[Left,(T1,T2,T3,T4)]

  implicit def prove5[Left,T1,T2,T3,T4,T5](
    implicit ev: (Left =:= T1) || (Left =:= T2) || (Left =:= T3) || (Left =:= T4) || (Left =:= T5)
  ) = new In[Left,(T1,T2,T3,T4,T5)]
}

/** Proves that the first type is NOT a component of the tuple given as the second type */
@implicitNotFound(msg = "Cannot prove ${Left} NotIn ${Set}")
final class NotIn[Left, Set]
object NotIn{
  implicit def prove[Left,Set](
    implicit ev: ![Left In Set]
  ) = new NotIn[Left,Set]
}
