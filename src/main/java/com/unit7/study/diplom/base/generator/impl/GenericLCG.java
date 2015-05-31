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

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.base.generator.Generator;

/**
 * @author unit7
 *
 */
public class GenericLCG implements Generator<BitSet> {

    private static final Logger logger = LoggerFactory.getLogger(GenericLCG.class);
    
    private static final byte BYTE_SIZE = 8;
    
    private final byte maxBitPosition;
    
    private short bitCount;
    
    private long a;
    private long b;
    private long mod;
    private long lastValue = 1;
    
    private byte bitCounter;
    
    public GenericLCG(short bitCount) {
        this(0L, 0L, 0L, 1L, bitCount);
    }
    
    public GenericLCG(long a, long b, long mod, long lastValue, short bitCount) {
        Preconditions.checkArgument(mod > 256, "mod should be > 256");
        
        this.a = a;
        this.b = b;
        this.mod = mod;
        this.bitCount = bitCount;
        this.lastValue = (a * lastValue + b) % mod;
        
        maxBitPosition = (byte) (Math.log(mod) / Math.log(2) - 1);
        bitCounter = maxBitPosition;
        
        logger.debug("mod: {}, maxBitPosition: {}", mod, maxBitPosition);
    }
    

    @Override
    public BitSet next() {
        final BitSet result = new BitSet();
        
        logger.debug("bitCount: {}, bitCounter: {}, lastValue: {}", bitCount, bitCounter, lastValue);
        
        for (int i = 0; i < bitCount; ++i) {
            if (bitCounter <= (maxBitPosition - BYTE_SIZE)) {
                bitCounter = maxBitPosition;
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

    public void setBitCount(short bitCount) {
        this.bitCount = bitCount;
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

    @Override
    public String toString() {
        return String.format("GenericLCG [a=%s, b=%s, mod=%s]", a, b, mod);
    }
    
    public static final GenericLCG[] LCG_LIST = new GenericLCG[] {
        new GenericLCG(69069L, 1L, (((long) Integer.MAX_VALUE) + 1) * 2, 1L, (short) 1),
        new GenericLCG(1099087573L, 0L, (((long) Integer.MAX_VALUE) + 1) * 2, 1L, (short) 1),
        new GenericLCG(1220703125L, 0L, (long) Math.pow(2, 46), 1L, (short) 1),
        new GenericLCG(25214903917L, 11L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(33952834046453L, 0L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(44485709377909L, 0L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG((long) Math.pow(13, 13), 0L, (long) Math.pow(2, 59), 1L, (short) 1),
        new GenericLCG((long) Math.pow(5, 19), 1L, (long) Math.pow(2, 63), 1L, (short) 1),
        new GenericLCG((long) Math.pow(5, 19), 1L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(9219741426499971445L, 1L, (long) Math.pow(2, 63), 1L, (short) 1)
    };
}
