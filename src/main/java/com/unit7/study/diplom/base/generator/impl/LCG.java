/**
 * Date:	04 мая 2015 г.
 * File:	LCG.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.generator.impl;

import java.util.BitSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.unit7.study.diplom.base.generator.Generator;

/**
 * Линейный конгруэнтный генератор последовательностей
 * 
 * @author unit7
 *
 */
public class LCG implements Generator<BitSet> {
    
    private static final Logger logger = LoggerFactory.getLogger(LCG.class);
    
    public LCG(short length) {
        this.sequenceLength = length;
    }

    private void lcgRand(double[] dunif) {
        double dz = seed;
        
        for (short i = 0; i < sequenceLength; ++i) {
            dz = Math.floor(dz);
            
            double dz1 = dz * DA_1;
            double dz2 = dz * DA_2;
            double dOver1 = Math.floor(dz1 / TWO_31);
            double dOver2 = Math.floor(dz2 / TWO_31);
            
            dz1 -= dOver1 * TWO_31;
            dz2 -= dOver2 * TWO_31;
            
            dz = dz1 + dz2 + dOver1 + dOver2;
            
            double dOver = Math.floor(dz / TWO_31_DECREMENT);
            dz -= dOver * TWO_31_DECREMENT;
            
            dunif[i] = dz / TWO_31_DECREMENT;
            seed = dz;
        }
    }
    
    @Override
    public BitSet next() {
        
        final BitSet result = new BitSet();
        final double[] dunif = new double[sequenceLength];
        
        short zeroCounter = 0;
        
        lcgRand(dunif);
        for (short i = 0; i < dunif.length; ++i) {
            if (dunif[i] < 0.5) {
                zeroCounter += 1;
            } else {
                result.set(i);
            }
        }
        
        logger.debug("zeroBits: {}, oneBits: {}", zeroCounter, sequenceLength - zeroCounter);
        
        return result;
    }

    @Override
    @Deprecated
    public long power() {
        throw new AbstractMethodError("This method has been deprecated in parent class and does not implements for new classes");
    }

    @Override
    public Set<BitSet> set() {
        throw new UnsupportedOperationException("For this generator method not allowed");
    }
    
    @Override
    public short bitCount() {
        return sequenceLength;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("sequenceLength", sequenceLength)
                      .add("seed", seed)
                      .toString();
    }

    private static final double TWO_31 = 2147483648.0;
    private static final double TWO_31_DECREMENT = TWO_31 - 1;
    private static final double DA_1 = 41160.0;
    private static final double DA_2 = 950665216.0;
    
    private double seed = 23482349.0;
    
    private short sequenceLength;
}
