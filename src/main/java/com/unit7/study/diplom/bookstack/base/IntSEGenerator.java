/**
 * Date:	23 февр. 2015 г.
 * File:	IntSEGenerator.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.base;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;

/**
 * @author unit7
 *
 */
public class IntSEGenerator implements Generator<Integer> {

    public IntSEGenerator() {
        this(false);
    }
    
    public IntSEGenerator(boolean randomize) {
        if (randomize)
            generator = new Random(System.currentTimeMillis());
        else
            generator = new Random();
    }

    @Override
    public Integer next() {
        return generator.nextInt();
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
        return String.format("IntSEGenerator [generator=%s, set=%s]", generator, set);
    }

    private Random generator;
    private Set<Integer> set = new TreeSet<>();
}
