/**
 * Date:	31 мая 2015 г.
 * File:	HardnessTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

/**
 * @author unit7
 *
 */
public class HardnessTest {

    @org.junit.Test
    public void countOperations() {
        final int n = 50;
        
        int newLen = n;
        int counter = 0;
        while (true) {
            boolean finished = true;
            
            for (int i = 1; i < newLen; ++i) {
                counter += 1;
                for (int j = 2; j < newLen; ++j) {
                    counter += 1;
                    finished = false;
                    newLen -= 1;
                }
            }
            
            if (finished)
                break;
        }
        
        System.out.println("Length: " + n);
        System.out.println("final counter: " + counter);
    }
}
