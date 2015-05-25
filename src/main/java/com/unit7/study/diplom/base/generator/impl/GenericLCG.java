/**
 * Date:	25 мая 2015 г.
 * File:	GenericLCG.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.generator.impl;

import java.util.BitSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.generator.Generator;

/**
 * @author unit7
 *
 */
public class GenericLCG implements Generator<BitSet> {

    private static final Logger logger = LoggerFactory.getLogger(GenericLCG.class);
    
    private static final byte MAX_BIT_POSITION = 30;
    private static final byte MIN_BIT_POSITION = 22;
    
    private short bitCount;
    
    private long a;
    private long b;
    private long mod;
    private long lastValue = 1;
    
    private byte bitCounter = MAX_BIT_POSITION;
    
    public GenericLCG(short bitCount) {
        this.bitCount = bitCount;
    }
    
    public GenericLCG(long a, long b, long mod, long lastValue, short bitCount) {
        this.a = a;
        this.b = b;
        this.mod = mod;
        this.bitCount = bitCount;
        this.lastValue = (a * lastValue + b) % mod;
    }

    @Override
    public BitSet next() {
        final BitSet result = new BitSet();
        
        logger.debug("bitCount: {}, bitCounter: {}, lastValue: {}", bitCount, bitCounter, lastValue);
        
        for (int i = 0; i < bitCount; ++i) {
            if (bitCounter <= MIN_BIT_POSITION) {
                bitCounter = MAX_BIT_POSITION;
                lastValue = (a * lastValue + b) % mod;
            }
            
            final boolean bit = ((lastValue >> bitCounter) & 1) > 0;
            if (bit) {
                result.set(i);
            }
            bitCounter -= 1;
        }
        
        logger.debug("Generated result: {}", result);
        
        return result;
    }

    @Deprecated
    @Override
    public long power() {
        throw new AbstractMethodError("This method has been deprecated in parent class and does not implements for new classes");
    }

    @Override
    public short bitCount() {
        return bitCount;
    }

    @Override
    public Set<BitSet> set() {
        throw new UnsupportedOperationException();
    }

    public short getBitCount() {
        return bitCount;
    }

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }

    public long getMod() {
        return mod;
    }

    public void setMod(long mod) {
        this.mod = mod;
    }

    public long getLastValue() {
        return lastValue;
    }

    public void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }
}
