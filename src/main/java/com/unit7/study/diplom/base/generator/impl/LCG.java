/**
 * Date:	04 мая 2015 г.
 * File:	LCG.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.generator.impl;

import java.util.BitSet;
import java.util.Set;

import com.unit7.study.diplom.base.generator.Generator;

/**
 * Линейный конгруэнтный генератор последовательностей
 * 
 * @author unit7
 *
 */
public class LCG implements Generator<BitSet> {

    public LCG(short length) {
        this.sequenceLength = length;
    }

    @Override
    public BitSet next() {
        // TODO
        return null;
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

    private short sequenceLength;
}
