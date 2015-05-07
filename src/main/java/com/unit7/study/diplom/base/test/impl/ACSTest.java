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

    /**
     * Рассчитать размеры выборок для каждой стадии
     */
    private void calcDimensions() {
        sequenceLength = sequence.length;
        stageOneSize = (int) (sequenceLength * 0.1);
        stageTwoSize = (int) (stageOneSize * 0.7);
        stageThreeSize = sequenceLength - stageOneSize - stageTwoSize;
    }
    
    private void executeStageOne() {
        for (int i = 0; i < stageOneSize; ++i) {
            sortedPart.add(sequence[i]);
        }
        
        // TODO рассчитать среднее расстояние между значениями и проредить выборку, если нужно
    }
    
    private void executeStageTwo() {
        for (int i = 0; i < stageTwoSize; ++i) {
            final int index = stageOneSize + i;
            final BitSet next = sequence[index];
            final BitSet distance = findMinDist(next);
            
            if (distance != null && BIT_SET_COMPARATOR.compare(currentDistance, distance) > 0)
                distanceSet.add(distance);
        }
    }
    
    private void executeStageThree() {
        for (int i = 0; i < stageThreeSize; ++i) {
            final int index = i + stageOneSize + stageTwoSize;
            final BitSet next = sequence[index];
            final BitSet distance = findMinDist(next);
            if (distanceSet.contains(distance)) {
                meetFrequency += 1;
            }
        }
    }
    
    /**
     * Рассчитать конечное значение x2
     */
    private double calcFinalValue() {
        // TODO
        return 0;
    }
    
    /**
     * Найти расстояние до ближайшего значения в словаре
     * @param value значение для сравнения
     * @return кратчайшее расстояние
     */
    private BitSet findMinDist(BitSet value) {
        // TODO
        return null;
    }
    
    /**
     * Рассчитать значение тестовой выборки
     * @return
     */
    private int calcStageThreeSize() {
        return (int) (5 * (long) Math.pow(2, bitCount) / (2 * sortedPart.size() * distanceSet.size())) + stageTwoSize + stageOneSize;
    }
    
    @Override
    public boolean test() {
        
        calcDimensions();
        
        executeStageOne();
        executeStageTwo();
        
        stageThreeSize = calcStageThreeSize();
        
        executeStageThree();
        
        final double x2 = calcFinalValue();
        
        return x2 <= 0.00016;
    }

    private int sequenceLength;                 // размер всей тестовой выборки
    
    private int stageOneSize;                   // размер выборки для определения первого множества(словаря)
    private int stageTwoSize;                   // размер выборки для определения множества расстояний
    private int stageThreeSize;                 // размер тестовой выборки для расчета итоговых значений
    
    private int meetFrequency;                  // частота встречаемости элементов первого множества
    
    private BitSet currentDistance;             // текущее среднее расстояние между значениями в словаре
    
    private SortedSet<BitSet> sortedPart = new TreeSet<>(BIT_SET_COMPARATOR);           // словарь
    private Set<BitSet> distanceSet = new HashSet<>();                                  // множество расстояний
    
    private short bitCount;                     // степень двойки мощности словаря
    private BitSet[] sequence;                  // выборка данных
    
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
