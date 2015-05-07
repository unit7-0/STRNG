/**
 * Date:	04 мая 2015 г.
 * File:	BookStackTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.math.BigDecimal;
import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.BSSet;
import com.unit7.study.diplom.base.test.TestAlgorithm;

/**
 * Алгоритм тестирования стопка книг
 * 
 * @author unit7
 *
 */
public class BookStackTest implements TestAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(BookStackTest.class);
    
    public BookStackTest(BitSet[] sequence, short bitCount) {
        if (sequence == null || sequence.length == 0) {
            throw new IllegalArgumentException("sequence length should be > 0");
        }
        
        this.bitCount = bitCount;
        this.sequence = sequence;
    }

    private void prepare() {
        final int preparedSetLength = (int) Math.sqrt(sequence.length);
        preparedSet = new BSSet<>(preparedSetLength);
        
        logger.debug("preparedSetLength: {}", preparedSetLength);
        
        for (int i = 0; i < preparedSetLength; ++i) {
            preparedSet.add(sequence[i]);
        }
        
        firstSequenceIndex = preparedSetLength;
    }
    
    private double calcDimension() {
        int n1 = 0;
        
        for (int i = firstSequenceIndex; i < sequence.length; ++i) {
            if (preparedSet.contains(sequence[i])) {
                n1 += 1;
            }
        }
        
        final int selectionCount = sequence.length - firstSequenceIndex;
        final int n2 = selectionCount - n1;
        
        final BigDecimal setPower = BigDecimal.valueOf(2).pow(bitCount);
        
        final double p1 = BigDecimal.valueOf(n1).divide(setPower).doubleValue();
        final double p2 = BigDecimal.valueOf(n2).divide(setPower).doubleValue();
        
        logger.debug("Calculated first set matching and probability: [ {}, {} ], second set matching and probability: [ {}, {} ]",
                n1, n2, p1, p2);
        
        final double np1 = selectionCount * p1;
        final double np2 = selectionCount * p2;
        
        final double x2 = sqr(n1 - np1) / (np1) + sqr(n2 - np2) / (np2);
        
        return x2;
    }
    
    private double sqr(double v) {
        return v * v;
    }
    
    @Override
    public boolean test() {
        prepare();
        
        final double x2 = calcDimension();
        
        logger.info("Calculated x2: {} against hi2: 0.00016", x2);
        
        return x2 <= 0.00016;
    }

    private BSSet<BitSet> preparedSet;
    private BitSet[] sequence;

    private short bitCount;
    
    private int firstSequenceIndex;
}
