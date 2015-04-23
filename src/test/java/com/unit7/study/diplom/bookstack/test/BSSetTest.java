/**
 * Date:	05 апр. 2015 г.
 * File:	BSSetTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.test;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.unit7.study.diplom.bookstack.base.BSSet;

/**
 * @author unit7
 *
 */
public class BSSetTest {

    @Test
    public void testSet() {
        Set<Integer> set = new BSSet<>(20);
        
        boolean result = set.add(1);
        Assert.assertTrue(result);
        
        result = set.add(2);
        Assert.assertTrue(result);
        
        result = set.add(0);
        Assert.assertTrue(result);
        
        result = set.add(null);
        Assert.assertTrue(result);
        
        result = set.add(20);
        Assert.assertTrue(result);

        result = set.contains(null);
        Assert.assertTrue(result);
        
        result = set.add(3);
        Assert.assertTrue(result);
        
        result = set.add(2);
        Assert.assertFalse(result);
        
        result = set.contains(1);
        Assert.assertTrue(result);
        
        result = set.contains(2);
        Assert.assertTrue(result);
        
        result = set.contains(6);
        Assert.assertFalse(result);

        result = set.add(33);
        Assert.assertTrue(result);
        
        result = set.add(22);
        Assert.assertTrue(result);
        
        result = set.add(32);
        Assert.assertTrue(result);
        
        result = set.contains(2);
        Assert.assertTrue(result);
        
        result = set.add(42);
        Assert.assertTrue(result);
        
        result = set.contains(2);
        Assert.assertTrue(result);
        
        System.out.println(set);
    }
}
