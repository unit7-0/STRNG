/**
 * Date:	09 мая 2015 г.
 * File:	LCGTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.generator;

import java.util.BitSet;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.generator.impl.LCG;

/**
 * @author unit7
 *
 */
public class LCGTest {

    private static final Logger logger = LoggerFactory.getLogger(LCGTest.class);
    
    @org.junit.Test
    public void generate10() {
        final LCG g = new LCG((short) 10);
        
        BitSet generated = g.next();
        logger.info("generated_10: {}", bitSetToString(generated));
        
        Assert.assertNotNull(generated);
        Assert.assertEquals(10, generated.length());
        
        generated = g.next();
        logger.info("next generated_10: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_10: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_10: {}", bitSetToString(generated));
    }
    
    @org.junit.Test
    public void generate100() {
        final LCG g = new LCG((short) 100);
        
        BitSet generated = g.next();
        logger.info("generated_100: {}", bitSetToString(generated));
        
        Assert.assertNotNull(generated);
        Assert.assertEquals(100, generated.length());
        
        generated = g.next();
        logger.info("next generated_100: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_100: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_100: {}", bitSetToString(generated));
    }
    
    @org.junit.Test
    public void generate1000() {
        final LCG g = new LCG((short) 1000);
        
        BitSet generated = g.next();
        logger.info("generated_1000: {}", bitSetToString(generated));
        
        Assert.assertNotNull(generated);
        Assert.assertEquals(1000, generated.length());
        
        generated = g.next();
        logger.info("next generated_1000: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_1000: {}", bitSetToString(generated));
        
        generated = g.next();
        logger.info("next generated_1000: {}", bitSetToString(generated));
    }
    
    private String bitSetToString(BitSet bitSet) {
        final StringBuilder seq = new StringBuilder();
        final long[] words = bitSet.toLongArray();
        for (long word : words) {
            seq.insert(0, Long.toBinaryString(word));
        }
        
        return seq.toString();
    }
}
