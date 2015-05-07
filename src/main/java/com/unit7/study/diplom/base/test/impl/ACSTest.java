/**
 * Date:	04 мая 2015 г.
 * File:	ACSTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.unit7.study.diplom.base.test.TestAlgorithm;

/**
 * Алгоритм тестирования новый адаптивный хи-квадрат
 * 
 * @author unit7
 *
 */
public class ACSTest implements TestAlgorithm {

    public ACSTest(BitSet[] sequence, short bitCount) {
        if (sequence == null || sequence.length < 3)
            throw new IllegalArgumentException("sequence should not be null and length should be >= 3");
        
        this.bitCount = bitCount;
        this.sequence = sequence;
    }

    private void calcDimensions() {
        // TODO
    }
    
    private void executeStageOne() {
        for (int i = 0; i < m; ++i) {
            sortedPart.add(sequence[i]);
        }
    }
    
    private void executeStageTwo() {
        // TODO
    }
    
    private void executeStageThree() {
        // TODO
    }
    
    @Override
    public boolean test() {
        
        calcDimensions();
        
        executeStageOne();
        executeStageTwo();
        executeStageThree();        
        
        return false;
    }

    private int n;
    private int m;
    private int k;
    
    private SortedSet<BitSet> sortedPart = new TreeSet<>(BIT_SET_COMPARATOR);
    private Set<Double> distanceSet = new HashSet<>();
    
    private short bitCount;
    private BitSet[] sequence;
    
    private static final Comparator<BitSet> BIT_SET_COMPARATOR = new Comparator<BitSet>() {
        @Override
        public int compare(BitSet left, BitSet right) {
            if (left.length() != right.length())
                return left.length() - right.length();
            
            for (int i = left.length() - 1; i >= 0; --i) {
                if (left.get(i) ^ right.get(i)) {
                    if (left.get(i))
                        return 1;
                    else
                        return -1;
                }
            }
            
            return 0;
        }
    };
}
