/**
 * Date:	10 мая 2015 г.
 * File:	ACSTestV2.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

import java.util.BitSet;

import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.generator.impl.GenericLCG;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;
import com.unit7.study.diplom.base.test.impl.TestWorkflow;

/**
 * @author unit7
 *
 */
public class NATTestV2 {

    @org.junit.Test
    public void generate23() {
        final int iterations = 3;
        final short bitCount = 30;
        final int selectionCount = 36000;
        
        final Generator<BitSet> g = new GenericLCG(69069L, 1L, (((long) Integer.MAX_VALUE)+ 1) * 2, 1L, bitCount);//new LCG((short) bitCount);
        
        final TestWorkflow<BitSet> workflow = new TestWorkflow<>();
        
        workflow.setAlgorithmType(TestingAlgorithmType.NAT);
        workflow.setGenerator(g);
        workflow.setIterations((short) iterations);
        workflow.setSelectionCount(selectionCount);
        
        workflow.start();
    }
}
