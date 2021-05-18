package cz.cvut.fel.dom.acoustic_event_detector.calculation

import com.lemmingapex.trilateration.TrilaterationFunction
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.util.Pair
import kotlin.math.pow
import kotlin.math.sqrt


class MultilaterationFunction(
    positions: Array<DoubleArray>,
    distances: DoubleArray,
    private val firstPoint: DoubleArray
) :
    TrilaterationFunction(positions, distances) {
    // * Calculate value of partial derivations of function
    // * f'(x_i) = [df/dx_i ; df/dy_i]
    // *         ----------                                                                 ----------
    // *         |                                                                                   |
    // *   df    |             (x - P_r_x)                               (x - P_i_x)                 |
    // *   __  = | _____________________________________   -   _____________________________________ |
    // *   dx    | [(x - P_r_x)^2 + (y - P_r_y)^2)]^1/2        [(P_i_x - x)^2 + (P_i_y - y)^2)]^1/2  |
    // *         |                                                                                   |
    // *         ----------                                                                 ----------
    // *         ----------                                                                 ----------
    // *         |                                                                                   |
    // *   df    |             (y - P_r_y)                               (y - P_i_y)                 |
    // *   __  = | _____________________________________   -   _____________________________________ |
    // *   dy    | [(x - P_r_x)^2 + (y - P_r_y)^2)]^1/2        [(P_i_x - x)^2 + (P_i_y - y)^2)]^1/2  |
    // *         |                                                                                   |
    // *         ----------                                                                 ----------
    override fun jacobian(point: RealVector): RealMatrix {
        val referenceArray = point.toArray()
        val jacobian = Array(distances.size) { DoubleArray(referenceArray.size) }
        for (i in jacobian.indices) {
            val den1 = sqrt(
                (referenceArray[0] - firstPoint[0]).pow(2.0) + (referenceArray[1] - firstPoint[1]).pow(2.0)
            )
            val den2 = sqrt(
                (positions[i][0] - referenceArray[0]).pow(2.0) + (positions[i][1] - referenceArray[1]).pow(2.0)
            )
            val xPart1 = (referenceArray[0] - firstPoint[0]) / den1
            val xPart2 = (referenceArray[0] - positions[i][0]) / den2
            val yPart1 = (referenceArray[1] - firstPoint[1]) / den1
            val yPart2 = (referenceArray[1] - positions[i][1]) / den2
            jacobian[i][0] = xPart1 - xPart2
            jacobian[i][1] = yPart1 - yPart2
        }
        return Array2DRowRealMatrix(jacobian)
    }

    // Calculate value of function
    // f(x_i) = [(x - P_r_x)^2 + (y - P_r_y)^2]^1/2 + [(x - P_i_x)^2 + (y - P_i_y)^2]^1/2 + distances_i
    // distances_i = (t_i - t_r) * speed_of_sound
    override fun value(point: RealVector): Pair<RealVector, RealMatrix> {
        val referenceArray = point.toArray()
        val resultPoint = DoubleArray(distances.size)
        for (i in resultPoint.indices) {
            resultPoint[i] = 0.0
            val firstPart = sqrt(
                (referenceArray[0] - firstPoint[0]).pow(2.0) + (referenceArray[1] - firstPoint[1]).pow(2.0)
            )
            val secondPart = sqrt(
                (referenceArray[0] - positions[i][0]).pow(2.0) + (referenceArray[1] - positions[i][1]).pow(2.0)
            )
            resultPoint[i] = firstPart + secondPart + distances[i]
        }
        val jacobian = jacobian(point)
        return Pair(ArrayRealVector(resultPoint), jacobian)
    }
}