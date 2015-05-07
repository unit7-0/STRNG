/**
 * Date:	06 мая 2015 г.
 * File:	Test.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.test;

import java.util.BitSet;

/**
 * @author unit7
 *
 */
public class Test {

    public Test() {
    }

    @org.junit.Test
    public void test() {
        BitSet a = new BitSet();
        BitSet b = new BitSet();
        
        a.set(0);
        a.set(2);
        b.set(2);
        b.set(1);
        
        System.out.println(compare(a, b));
        System.out.println(compare(b, a));
        System.out.println(compare(a, a));
    }
    
    private int compare(BitSet left, BitSet right) {
        if (left.equals(right))
            return 0;
        
        final BitSet xor = (BitSet) left.clone();
        xor.xor(right);
        
        System.out.println(Long.toBinaryString(left.toLongArray()[0]) + " " + Long.toBinaryString(right.toLongArray()[0]));
        System.out.println(Long.toBinaryString(xor.toLongArray()[0]));
        
        return xor.length() == right.length() ? 1 : -1;
    }
}
