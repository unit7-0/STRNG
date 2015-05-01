/**
 * Date:	19 апр. 2015 г.
 * File:	ACSTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.bookstack.base.Generator;
import com.unit7.study.diplom.bookstack.base.IntSEGenerator;

/**
 * @author unit7
 *
 */
public class ACSTest {
    private static final Logger logger = LoggerFactory.getLogger(ACSTest.class);
    
    @Test
    public void testMethod() {
        final long n = (long) Math.sqrt(g.power());     // размер выборки
        final long m = (long) (n * 0.3);
        final long k = m;
        final long rem = n - m - k;
        
        logger.debug("Selection count: {}, m: {}, k: {}, testing selection: {}", n, m, k, rem);
        
        final SortedSet<Long> firstSet = new TreeSet<>();
        
        for (int i = 0; i < m; ++i) {
            final int val = g.next().intValue();
            // make all numbers positive
            firstSet.add((long) val - (long) Integer.MIN_VALUE);
        }
        
        logger.debug("First set after selection: {}", firstSet);
        logger.debug("Size: {}", firstSet.size());
        
        int r = findDistance(firstSet);
        
        logger.debug("Firstly finded distance: {}", r);
        logger.debug("Process first set...");
        
        // should b1 > r
        
        while (Math.abs(firstSet.first()) <= r) {
            logger.debug("Remove first element: {}", firstSet.first());
            firstSet.remove(firstSet.first());
        }
        
        logger.debug("After first condition preparing: {}", firstSet);
        
        final int newDist = normalizeSet(firstSet, r);
        
        logger.debug("Newly finded distance: {}", newDist);
        logger.debug("After full first set normalization: {}", firstSet);
        logger.debug("Size: {}", firstSet.size());
        
        // stage 2
        
        final Set<Long> distances = new HashSet<>();
        
        for (int i = 0; i < k; ++i) {
            final long next = g.next().intValue();
            final long dist = findMinDist(firstSet, next);
            if (dist != -1) {
                if (dist < r)
                    distances.add(dist);
            }
        }
        
        logger.debug("Finded distances: {}", distances);
        
        // stage 3
        
        long freq = 0;
        
        for (int i = 0; i < rem; ++i) {
            final long next = g.next();
            final long minDist = findMinDist(firstSet, next);
            
            if (minDist != -1) {
                if (distances.contains(minDist)) {
                    freq += 1;
                }
            }
        }
        
        logger.debug("Result frequency: {}", freq);
        
        final double p = 2.0 * firstSet.size() * distances.size() / g.power();
        
        logger.debug("Probablity: {}", p);
        
        final double np = rem * p;
        final double np2 = rem * (1 - p);
        
        final double x2 = (freq - np) * (freq - np) / np + (rem - freq - np2) * (rem - freq - np2) / np2;
        
        logger.debug("x2: {}", x2);
    }
    
    private long findMinDist(SortedSet<Long> set, long val) {
        long result = Long.MAX_VALUE;
        
        // TODO make binary search
        
        for (Long cur : set) {
            final long dist = Math.abs(cur - val);
            if (dist < result) {
                result = dist;
            } else {
                // дистанция только стала больше, т.к. числа упорядочены, дальше идти смысла нет
                break;
            }
        }
        
        if (result == Long.MAX_VALUE)
            return -1;
        
        return result;
    }
    
    private int normalizeSet(SortedSet<Long> set, int r) {
        int r2 = 2 * r;
        int newDist = r;
        while (true) {
            boolean finished = true;
            final Iterator<Long> it = set.iterator();
            long prev = it.next();
            while (it.hasNext() && finished) {
                long cur = it.next();
                int dist = (int) (cur - prev);
                if (dist >= r2) {
                    removeWithMinDist(set);
                    newDist = findDistance(set);
                    r2 = newDist * 2;
                    logger.debug("During normalizing newly distance finded: {}", newDist);
                    finished = false;
                }
            }
            
            if (finished) {
                break;
            }
        }
        
        return newDist;
    }
    
    private void removeWithMinDist(SortedSet<Long> set) {
        logger.debug("Remove element from set");
        
        Preconditions.checkArgument(!set.isEmpty(), "set should be not empty");
        
        int minDist = Integer.MAX_VALUE;
        final Iterator<Long> it = set.iterator();
        Long e = it.next();
        long prev = e;
        while (it.hasNext()) {
            long cur = it.next();
            int dist = (int) (cur - prev);
            if (dist < minDist) {
                minDist = dist;
                if (it.hasNext()) {
                    e = cur;
                } else {
                    e = prev;
                }
            }
        }
        
        logger.debug("Element to remove from set: {}", e);
        
        set.remove(e);
    }
    
    private int findDistance(SortedSet<Long> set) {
        long result = 0;
        
        final Iterator<Long> it = set.iterator();
        long prev = it.next();
        while (it.hasNext()) {
            long cur = it.next();
            int dist = (int) (cur - prev);
            result += dist;            
            prev = cur;
        }
        
        return (int) (result / (set.size() - 1));
    }
    
    @After
    public void destroy() {
        final long endTime = System.currentTimeMillis() - startTime;
        logger.debug("Execution time: {} sec.", ((double) endTime) / 1000);
    }
    
    @Before
    public void init() {
        g = new IntSEGenerator(true);
        
        startTime = System.currentTimeMillis();
    }
    
    private long startTime;
    
    private Generator<Integer> g;
}
