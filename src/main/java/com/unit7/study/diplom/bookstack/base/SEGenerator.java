/**
 * Date:	01 февр. 2015 г.
 * File:	SEGenerator.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.base;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Objects;

/**
 * @author unit7
 *
 */
public class SEGenerator implements Generator<Byte> {
    public SEGenerator() {
        this(false);
    }
    
    public SEGenerator(boolean randomize) {
        if (randomize)
            generator = new Random(System.currentTimeMillis());
        else
            generator = new Random();
    }
    
    @Override
    public Byte next() {
        return (byte) (generator.nextInt(256) - 128);
    }

    @Override
    public long power() {
        return 256;
    }

    @Override
    public Set<Byte> set() {
        if (set.isEmpty()) {
            synchronized (set) {
                if (set.isEmpty()) {
                    for (int i = -128; i < 128; ++i) {
                        set.add((byte) i);
                    }
                    set = Collections.unmodifiableSet(set);
                }
            }
        }
        return set;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("generator", generator).
                add("set", set).
                toString();
    }
    
    private Random generator;
    private Set<Byte> set = new TreeSet<>();
}
