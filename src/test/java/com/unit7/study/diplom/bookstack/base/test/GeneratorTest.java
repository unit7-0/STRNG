/**
 * Date:	01 февр. 2015 г.
 * File:	GeneratorTezt.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.base.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.bookstack.base.Generator;
import com.unit7.study.diplom.bookstack.base.SEGenerator;

/**
 * @author unit7
 *
 */
public class GeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(GeneratorTest.class);
    
    public GeneratorTest() {
    }

    @Test
    public void testGenerateBytes() {
        Generator<Byte> g = new SEGenerator();
        
        log.debug("generator set power: {}", g.power());
        log.debug("generator set: {}", g.set());
        
        Assert.assertEquals(g.power(), g.set().size());
        for (Iterator<Byte> it = g.set().iterator(); it.hasNext(); ) {
            byte val = it.next();
            Assert.assertTrue(val < 128 && val > -129);
        }
        
        log.debug("test g.next()");
        for (int i = 0; i < 10; ++i) {
           byte n = g.next();
           log.debug("next {}", n);
        }
    }
}
