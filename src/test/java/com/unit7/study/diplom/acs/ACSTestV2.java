/**
 * Date:	10 мая 2015 г.
 * File:	ACSTestV2.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

import java.util.BitSet;

import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.generator.impl.LCG;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;
import com.unit7.study.diplom.base.test.impl.TestWorkflow;

/**
 * @author unit7
 *
 */
public class ACSTestV2 {

    @org.junit.Test
    public void generate23() {
        final int iterations = 10;
        final int bitCount = 30;
        final int selectionCount = 30625;
        
        final Generator<BitSet> g = new LCG((short) bitCount);
        
        final TestWorkflow<BitSet> workflow = new TestWorkflow<>();
        
        workflow.setAlgorithmType(TestingAlgorithmType.ACS);
        workflow.setGenerator(g);
        workflow.setIterations((short) iterations);
        workflow.setSelectionCount(selectionCount);
        
        workflow.start();
    }
}
