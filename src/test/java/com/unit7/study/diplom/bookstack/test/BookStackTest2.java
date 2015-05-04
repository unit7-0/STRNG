/**
 * Date:	29 марта 2015 г.
 * File:	BookStackTest2.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.test;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.BSSet;
import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.generator.impl.SequenceIntGenerator;

/**
 * @author unit7
 *
 */
public class BookStackTest2 {
    private static final Logger log = LoggerFactory.getLogger(BookStackTest2.class);
    
    @Test
    public void bookStack() {
        
        final int firstSetSize = (int) Math.sqrt(generator.power());
        final long secondSetSize = generator.power() - firstSetSize;
        final Set<Integer> firstSet = new BSSet<>(1000);
        
        // инициализируем первое множество
        
        final int counter = Integer.MIN_VALUE + firstSetSize;
        for (int i = Integer.MIN_VALUE; i < counter; ++i) {
            firstSet.add(i);
        }
        
        log.info("selectionCount: {}, firstSetSize: {}, secondSetSize: {}", SELECTION_COUNT, firstSetSize, secondSetSize);
//        log.debug("firstSet: {}", firstSet);
        
        // количество элементов первого множества, встретившихся в выборке
        int n1 = 0;
        
        for (int i = 0; i < SELECTION_COUNT; ++i) {
            final int val = generator.next();
            
            // если множество содержит значение, то оно из первого множества, иначе из второго
            if (firstSet.contains(val))
                ++n1;
        }
        
        // количество элементов второго множества, встретившихся в выборке
        int n2 = SELECTION_COUNT - n1;
        
        // вероятности выпадения элемента из первого и второго множества соответственно
        double p1 = (double) firstSetSize / generator.power();
        double p2 = (double) secondSetSize / generator.power();
        
        double np1 = SELECTION_COUNT * p1;
        double np2 = SELECTION_COUNT * p2;
        
        final double x2 = sqr(n1 - np1) / (np1) + sqr(n2 - np2) / (np2);
        final double hi2 = getHi2(1, 1 - 1.0 / generator.power());
        
        log.info("n2={}, p1={}, p2={}, np1={}, np2={}", n2, p1, p2, np1, np2);
        log.info("x2={}, hi2={}", x2, hi2);
        
        Assert.assertTrue(hi2 >= x2);
    }
    
    public double sqr(double a) {
        return a * a;
    }
    
    /**
     * Получить хи квадрат
     */
    private double getHi2(double degrees, double p) {
        return 0.00004;
    }
    
    @After
    public void shutdown() {
        final long elapsed = System.currentTimeMillis() - start;
        
        System.gc();
        final double freeMemoryAtEnd = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
        final double memoryUsage = freeMemoryAtStart - freeMemoryAtEnd;
        
        log.info("time elapsed: {} ms, memoryAtStart: {} KB, memoryAtEnd: {} KB, memoryUsage: {} KB", elapsed, freeMemoryAtStart, freeMemoryAtEnd, memoryUsage);
    }
    
    @Before
    public void setup() {
        System.gc();
        
        start = System.currentTimeMillis();
        freeMemoryAtStart = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
        
        generator = new SequenceIntGenerator();
    }
    
    private long start;
    private double freeMemoryAtStart;
    
    // размер выборки
    private static final int SELECTION_COUNT = 1000000;
    
    private Generator<Integer> generator;
}
