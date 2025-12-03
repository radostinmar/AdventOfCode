package util

import com.microsoft.z3.ArithExpr
import com.microsoft.z3.ArithSort
import com.microsoft.z3.BoolSort
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.Model
import com.microsoft.z3.RatNum
import com.microsoft.z3.RealSort
import com.microsoft.z3.Sort
import java.math.BigDecimal

context(context: Context)
operator fun <R : ArithSort> ArithExpr<R>.times(other: Expr<out R>): ArithExpr<R> =
    context.mkMul(this, other)

context(context: Context)
operator fun <R : ArithSort> ArithExpr<R>.plus(other: Expr<out R>): ArithExpr<R> =
    context.mkAdd(this, other)

context(context: Context)
infix fun <R : Sort> Expr<R>.eq(other: Expr<R>): Expr<BoolSort> =
    context.mkEq(this, other)

context(context: Context)
val BigDecimal.real: RatNum
    get() = context.mkReal(this.toPlainString())

operator fun Model.get(x: Expr<RealSort>): BigDecimal = (getConstInterp(x) as RatNum).let {
    it.bigIntNumerator.toBigDecimal() / it.bigIntDenominator.toBigDecimal()
}