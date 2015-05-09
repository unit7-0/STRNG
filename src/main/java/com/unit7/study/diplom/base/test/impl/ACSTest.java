/**
 * Date:	04 мая 2015 г.
 * File:	ACSTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.unit7.study.diplom.base.test.TestAlgorithm;

/**
 * Алгоритм тестирования новый адаптивный хи-квадрат
 * 
 * TODO использовать BigInteger?
 * 
 * @author unit7
 *
 */
public class ACSTest implements TestAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(ACSTest.class);    

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
        
        findAvgDistance();
        
        // b0 > r
        while (BIT_SET_COMPARATOR.compare(sortedPart.first(), currentDistance) <= 0) {
            sortedPart.remove(sortedPart.first());
        }
        
        final BitSet powerSubDistance = sub(calcSetPower(), currentDistance);
        
        // bm < s - r
        while (BIT_SET_COMPARATOR.compare(sortedPart.last(), powerSubDistance) > 0) {
            sortedPart.remove(sortedPart.last());
        }
        
        // bi < 2 * r
        normalizeSortedPart();
    }
    
    /**
     * Посчитать мощность алфаватиа
     * @return
     */
    private BitSet calcSetPower() {
        final BigInteger result = new BigInteger("2").pow(bitCount);
        
        final BitSet bitSet = new BitSet();
        for (int i = 0; i < result.bitLength(); ++i) {
            if (result.testBit(i)) {
                bitSet.set(i);
            }
        }
        
        return bitSet;
    }
    
    /**
     * Проредить выборку, сделав ее равномерной - чтобы элементы были равноудалены друг от друга
     */
    private void normalizeSortedPart() {
        final BitSet bitSetTwo = BitSet.valueOf(new long[] { 2 });
        BitSet distanceDoubled = mul(currentDistance, bitSetTwo);
        
        while (true) {
            boolean finished = true;
            final Iterator<BitSet> it = sortedPart.iterator();
            BitSet prev = it.next();
            while (it.hasNext()) {
                final BitSet cur = it.next();
                final BitSet dist = sub(cur, prev);
                
                if (BIT_SET_COMPARATOR.compare(dist, distanceDoubled) >= 0) {
                    // проредить
                    currentDistance = removeSequenceWithMinDist(dist);
                    distanceDoubled = mul(currentDistance, bitSetTwo);
                    
                    finished = false;
                    break;
                }
                
                prev = cur;
                
                if (!finished)
                    break;
            }
        }
    }
    
    /**
     * Удалить одну или несколько последовательностей, в результате чего удвоенное среднее расстояние будет больше требуемого
     * @param requiredDistance требуемое среднее расстояние
     * @return новое среднее расстояние между последовательностями
     */
    private BitSet removeSequenceWithMinDist(BitSet requiredDistance) {
        
        BitSet newDistance = currentDistance;
        
        do {
            final Iterator<BitSet> it = sortedPart.iterator();
            BitSet pprev = it.next();
            BitSet prev = it.next();
            
            BitSet newDistanceCalced = div(currentAmount, BitSet.valueOf(new long[] { sortedPart.size() - 2 }));
            BitSet newDistanceDoubled = mul(newDistanceCalced, BitSet.valueOf(new long[] { 2 }));
            
            boolean removed = false;
            
            while (it.hasNext()) {
                final BitSet current = it.next();
                final BitSet distance = sub(current, pprev);
                
                if (BIT_SET_COMPARATOR.compare(distance, newDistanceDoubled) < 0) {
                    sortedPart.remove(prev);
                    removed = true;
                    break;
                }
                
                if (removed)
                    break;
                
                pprev = prev;
                prev  = current;
            }
            
            if (BIT_SET_COMPARATOR.compare(requiredDistance, newDistanceDoubled) < 0) {
                newDistance = newDistanceCalced;
                break;
            }

            // TODO throw exception
            
        } while (true);
        
        return newDistance;
    }
    
    /**
     * Рассчитать среднее расстояние между элементами первого множества
     */
    private void findAvgDistance() {
        currentAmount = new BitSet();
        
        final Iterator<BitSet> it = sortedPart.iterator();
        BitSet prev = it.next();
        for (; it.hasNext(); ) {
            final BitSet cur = it.next();
            final BitSet distance = sub(cur, prev);
            
            currentAmount = add(currentAmount, distance);
            prev = cur;
        }
        
        currentDistance = div(currentAmount, BitSet.valueOf(new long[] { sortedPart.size() - 1 }));
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
     * Разделить битовые последовательности left / right
     * @param left
     * @param right
     * @return
     * 
     * TODO переписать
     */
    private BitSet div(BitSet left, BitSet right) {
        final BigInteger a = new BigInteger(bitSetToString(left), 2);
        final BigInteger b = new BigInteger(bitSetToString(right), 2);
        
        final BigInteger result = a.divide(b);
        
        final BitSet bitSet = new BitSet();
        for (int i = 0; i < result.bitLength(); ++i) {
            if (result.testBit(i)) {
                bitSet.set(i);
            }
        }
        
        return bitSet;
    }
    
    private String bitSetToString(BitSet bitSet) {
        final StringBuilder seq = new StringBuilder();
        final long[] words = bitSet.toLongArray();
        for (long word : words) {
            seq.insert(0, Strings.padStart(Long.toBinaryString(word), 63, '0'));
        }
        
        return seq.toString();
    }
    
    /**
     * Умножить первый на второй
     * @param left
     * @param right
     * @return
     */
    private BitSet mul(BitSet left, BitSet right) {
        final BigInteger a = new BigInteger(bitSetToString(left), 2);
        final BigInteger b = new BigInteger(bitSetToString(right), 2);
        
        final BigInteger result = a.multiply(b);
        final BitSet bitSet = new BitSet();
        
        for (int i = 0; i < result.bitLength(); ++i) {
            if (result.testBit(i)) {
                bitSet.set(i);
            }
        }
        
        return bitSet;
    }
    
    /**
     * Вычислить расстояние между последовательностями
     * Левая должна быть больше либо равна правой
     * @param left левая последовательность
     * @param right права последовательность
     * @return
     */
    private BitSet sub(BitSet left, BitSet right) {
        final BitSet result = BitSet.valueOf(left.toLongArray());
        
        for (int i = 0; i < right.length(); ++i) {
            if (right.get(i)) {
                if (result.get(i)) {
                    result.flip(i);
                } else {
                    tookBit(result, i); // занимаем бит
                }
            }
        }
        
        return result;
    }
    
    /**
     * Сложить две битовые последовательности
     * @param left
     * @param right
     * @return
     */
    private BitSet add(BitSet left, BitSet right) {
        final BitSet result = BitSet.valueOf(left.toLongArray());
        
        for (int i = 0; i < right.length(); ++i) {
            if (right.get(i)) {
                if (result.get(i)) {
                    addBit(result, i);
                } else {
                    result.flip(i);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Добавить бит к последовательности от текущей позиции
     * @param bitSet
     * @param pos
     */
    private void addBit(BitSet bitSet, int pos) {
        int zeroPos = -1;
        for (int i = pos + 1; i < bitSet.length(); ++i) {
            if (!bitSet.get(i)) {
                zeroPos = i;
                break;
            }
        }
        
        if (zeroPos == -1) {
            bitSet.set(bitSet.length());
            for (int i = bitSet.length() - 2; i >= pos; --i) {
                bitSet.flip(i);
            }
        } else {
            for (int i = zeroPos; i >= pos; --i) {
                bitSet.flip(i);
            }
        }
    }
    
    /**
     * Занимает бит в правой части, начиная от текущей позиции
     * Предполагается, что занять бит всегда возможно
     * @param bitSet
     * @param curPos
     */
    private void tookBit(BitSet bitSet, int curPos) {
        int onePos = -1;
        for (int i = curPos + 1; i < bitSet.length(); ++i) {
            if (bitSet.get(i)) {
                onePos = i;
                break;
            }
        }
        
        for (int i = onePos; i >= curPos; --i) {
            bitSet.flip(i);
        }
        
        // TODO если невозможно занять бит
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
        BitSet distance = null;
        
        for (BitSet bs : sortedPart) {
            BitSet left = bs;
            BitSet right = value;
            if (BIT_SET_COMPARATOR.compare(left, right) < 0) {
                left = value;
                right = bs;
            }
            
            final BitSet newDistance = sub(left, right);
            if (distance == null || BIT_SET_COMPARATOR.compare(distance, newDistance) > 0) {
                distance = newDistance;
            }
        }
        
        return distance;
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
    
    private BitSet currentAmount;               // текущая сумма расстояний
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
