/**
 * Date:	10 мая 2015 г.
 * File:	TestAlgorithmFactory.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.util.BitSet;

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.base.test.TestingAlgorithm;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;

/**
 * Фабрика тестовых алгоритмов
 * 
 * @author unit7
 *
 */
public class TestingAlgorithmFactory {
    @SuppressWarnings("unchecked")
    public static <T> TestingAlgorithm<T> createAlgorithm(TestingAlgorithmType type, T[] sequence, short bitCount) {
        Preconditions.checkNotNull(type, "algorithm type required");

        switch (type) {
            case NAT:
                return (TestingAlgorithm<T>) new NATTest((BitSet[]) (Object[]) sequence, bitCount);
            case BOOK_STACK:
                return (TestingAlgorithm<T>) new BookStackTest((BitSet[]) (Object[]) sequence, bitCount);
            default:
                throw new IllegalArgumentException("The type " + type + " is unknown for this factory");
        }
    }
}
