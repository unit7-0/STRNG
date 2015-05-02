/**
 * Date:	02 мая 2015 г.
 * File:	SequenceGenerator.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base;

import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

/**
 * Генератор последовательности целых чисел 2<sup>32</sup> бит
 * Начинает с нуля с шагом 1 и ходит по кругу
 * @author unit7
 *
 */
public class SequenceIntGenerator implements Generator<Integer> {

    public SequenceIntGenerator() {
    }

    @Override
    public Integer next() {
        return current++;
    }
    
    @Override
    public long power() {
        return 4294967296L;
    }
    
    @Override
    public Set<Integer> set() {
        if (set.isEmpty()) {
            synchronized (set) {
                if (set.isEmpty()) {
                    for (long i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; ++i) {
                        set.add((int) i);
                    }
                    
                    set = ImmutableSet.copyOf(set);
                }
            }
        }
        
        return set;
    }
    @Override
    public String toString() {
        return String.format("IntSEGenerator [set=%s]", set);
    }

    private int current = 0;
    
    private Set<Integer> set = new TreeSet<>();
}
