/**
 * Date:	10 мая 2015 г.
 * File:	TestAlgorithmFactory.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.util.BitSet;

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.base.test.TestAlgorithm;
import com.unit7.study.diplom.base.test.TestAlgorithmType;

/**
 * Фабрика тестовых алгоритмов
 * 
 * @author unit7
 *
 */
public class TestAlgorithmFactory {
    @SuppressWarnings("unchecked")
    public static <T> TestAlgorithm<T> createAlgorithm(TestAlgorithmType type, T[] sequence, short bitCount) {
        Preconditions.checkNotNull(type, "algorithm type required");

        switch (type) {
            case ACS:
                return (TestAlgorithm<T>) new ACSTest((BitSet[]) (Object[]) sequence, bitCount);
            case BOOK_STACK:
                return (TestAlgorithm<T>) new BookStackTest((BitSet[]) sequence, bitCount);
            default:
                throw new IllegalArgumentException("The type " + type + " is unknown for this factory");
        }
    }
}
