//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.vorotov.simulationslab11.NormalDist;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

public interface RealDistribution {
    double probability(double var1);

    double density(double var1);

    double cumulativeProbability(double var1);

    /**
     * @deprecated
     */
    @Deprecated
    double cumulativeProbability(double var1, double var3) throws NumberIsTooLargeException;

    double inverseCumulativeProbability(double var1) throws OutOfRangeException;

    double getNumericalMean();

    double getNumericalVariance();

    double getSupportLowerBound();

    double getSupportUpperBound();

    /**
     * @deprecated
     */
    @Deprecated
    boolean isSupportLowerBoundInclusive();

    /**
     * @deprecated
     */
    @Deprecated
    boolean isSupportUpperBoundInclusive();

    boolean isSupportConnected();

    void reseedRandomGenerator(long var1);

    double sample();

    double[] sample(int var1);
}
