package org.cvogt.test.scala.constraint

import org.scalatest.FunSuite
import org.scalactic.TypeCheckedTripleEquals._

import scala.language.higherKinds

import org.cvogt.scala.constraint.boolean._

class BooleanTest extends FunSuite{
  test("type-level boolean algebra"){
    // Basic operations
    prove[True]
    prove[![False]]
    prove[![![True]]]
    assertTypeError("prove[False]")
    assertTypeError("prove[![True]]")

    // commutative ==
    prove[True == True]
    prove[False == False]
    prove[![True == False]]
    prove[![False == True]]

    prove[(False && False) == False]
    prove[(True  && False) == False]
    prove[(False && True)  == False]
    prove[(True  && True)  == True]

    prove[(False || False) == False]
    prove[(True  || False) == True]
    prove[(False || True)  == True]
    prove[(True  || True)  == True]

    // Derived operations
    type ImpliesDefinition[X,Y] = (X Implies Y) == (![X] || Y)
    prove2[ImpliesDefinition]

    type XorDefinition[X,Y] = (X Xor Y) == ((X || Y) && ![X && Y])
    prove2[XorDefinition]

    type IdenticalDefinition[X,Y] = (X == Y) == ![X Xor Y]
    prove2[IdenticalDefinition]

    // Monotone laws
    // basic laws
    type Associative[X,Y,Z,Op[_,_]] = (X Op (Y Op Z)) == ((X Op Y) Op Z)
    type Cummutative[X,Y,Op[_,_]] = (X Op Y) == (Y Op X)
    type Distributivity[X,Y,Z,Op1[_,_],Op2[_,_]] = (X Op1 (Y Op2 Z)) == ((X Op1 Y) Op2 (X Op1 Z))

    type AssociativeOr[X,Y,Z] = Associative[X,Y,Z,||]
    prove3[AssociativeOr]

    type AssociativeAnd[X,Y,Z] = Associative[X,Y,Z,&&]
    prove3[AssociativeAnd]

    type CummutativeOr[X,Y] = Cummutative[X,Y,||]
    prove2[CummutativeOr]

    type CummutativeAnd[X,Y] = Cummutative[X,Y,&&]
    prove2[CummutativeAnd]

    type DistributivityAndOverOr[X,Y,Z] = Distributivity[X,Y,Z,&&,||]
    prove3[DistributivityAndOverOr]

    type OrIdentity[X] = (X || False) == X
    prove1[OrIdentity]

    type AndIdentity[X] = (X && True) == X
    prove1[AndIdentity]

    type AndAnnihilator[X] = (X && False) == False
    prove1[AndAnnihilator]
    
    // boolean laws
    type IdempotentOr[X] = (X || X) == X
    prove1[IdempotentOr]

    type IdempotentAnd[X] = (X && X) == X
    prove1[IdempotentAnd]

    type Absorption1[X,Y] = (X && (X || Y)) == X
    prove2[Absorption1]

    type Absorption2[X,Y] = (X || (X && Y)) == X
    prove2[Absorption2]

    type DistributivityOrOverAnd[X,Y,Z] = Distributivity[X,Y,Z,||,&&]
    prove3[DistributivityOrOverAnd]

    type OrAnnihilator[X] = (X || True) == True
    prove1[OrAnnihilator]

    // Nonmonotone laws
    type Complementation1[X] = (X && ![X]) == False
    prove1[Complementation1]

    type Complementation2[X] = (X || ![X]) == True
    prove1[Complementation2]

    type DeMorgan1[X,Y] = (![X] && ![Y]) == ![X || Y]
    prove2[DeMorgan1]

    type DeMorgan2[X,Y] = (![X] || ![Y]) == ![X && Y]
    prove2[DeMorgan2]
  }
}

object `package`{
  /** alias for implicitly[...] */
  def prove[Expr](
    implicit ev0: Expr
  ) = null

  def prove1[Law[_]](
    implicit ev0: Law[False],
             ev1: Law[True]
  ) = null

  def prove2[Law[_,_]](
    implicit ev00: Law[False,False],
             ev01: Law[False,True],
             ev10: Law[True,False],
             ev11: Law[True,True]
  ) = null

  def prove3[Law[_,_,_]](
    implicit ev000: Law[False,False,False],
             ev001: Law[False,False,True],
             ev010: Law[False,True,False],
             ev011: Law[False,True,True],
             ev100: Law[True,False,False],
             ev101: Law[True,False,True],
             ev110: Law[True,True,False],
             ev111: Law[True,True,True]
  ) = null
}
