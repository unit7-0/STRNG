/**
 * Date:	10 мая 2015 г.
 * File:	BookStackTestV3.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.test;

import java.util.BitSet;

import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.generator.impl.LCG;
import com.unit7.study.diplom.base.test.TestAlgorithmType;
import com.unit7.study.diplom.base.test.impl.TestWorkflow;

/**
 * @author unit7
 *
 */
public class BookStackTestV3 {

    @org.junit.Test
    public void generate() {
        final short iterations = 10;
        final short bitCount = 10;
        final int selectionCount = 1000;
        final Generator<BitSet> g = new LCG(bitCount);
        
        final TestWorkflow<BitSet> workflow = new TestWorkflow<>();
        
        workflow.setAlgorithmType(TestAlgorithmType.BOOK_STACK);
        workflow.setGenerator(g);
        workflow.setIterations(iterations);
        workflow.setSelectionCount(selectionCount);
        
        workflow.start();
    }
}
