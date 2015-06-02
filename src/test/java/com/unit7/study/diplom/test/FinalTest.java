/**
 * Date:	31 мая 2015 г.
 * File:	FinalTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.generator.impl.GenericLCG;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;
import com.unit7.study.diplom.base.test.impl.TestWorkflow;

/**
 * @author unit7
 *
 */
public class FinalTest {
    public static final short[] SELECTIONS_BITS = new short[] { 48 };
    public static final int START_SELECTION_COUNT = 500;
    public static final long MAX_SELECTION_COUNT = 34359738368L;
    public static final short ITERATIONS_COUNT = 1;
    
    private PrintWriter writer;
    
    @org.junit.Before
    public void setUp() {
        try {
            writer = new PrintWriter(new FileOutputStream("/home/unit7/diplom/result.txt"), true);
        } catch (FileNotFoundException e) {
            Assert.fail("Cannot init file output stream: " + e.getMessage());
        }
    }
    
    @org.junit.Test
    public void testAll() {
        try {
            writer.println("Генератор\t\t\tмощность\tразмер выборки\tразмер выборки(бит)\tалгоритм\tзавалил тест");
            for (int i = 0; i < SELECTIONS_BITS.length; ++i) {
                for (TestingAlgorithmType algType : TestingAlgorithmType.values()) {
                    int counter = 0;
                    for (GenericLCG generator : GenericLCG.LCG_LIST) {
                        ++counter;
                        if (counter != 3)
                            continue;
                        
                        int selectionCount = START_SELECTION_COUNT;
                        int success = 0;
                        do {
                            generator.setBitCount(SELECTIONS_BITS[i]);
                            
                            final TestWorkflow<BitSet> workflow = new TestWorkflow<>();
                            
                            workflow.setAlgorithmType(algType);
                            workflow.setIterations(ITERATIONS_COUNT);
                            workflow.setGenerator(generator);
                            
                            workflow.setSelectionCount(selectionCount);
                            workflow.start();
                        
                            success = 0;
                            final List<Boolean> result = workflow.getTestResult();
                            
                            for (Boolean r : result) {
                                if (!r) {
                                    success += 1;
                                }
                            }
                            
                            writer.print(generator + "\t");
                            writer.print(SELECTIONS_BITS[i] + "\t");
                            writer.print(selectionCount + "\t");
                            writer.print(selectionCount * SELECTIONS_BITS[i] + "\t");
                            writer.print(algType.getCaption() + "\t");
                            writer.println(success + "/" + result.size());
                            
                            selectionCount = (int) (selectionCount * 1.4);
                        } while (selectionCount <= MAX_SELECTION_COUNT && success == 0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error when testing", e);
            Assert.fail(e.getMessage());
        }
    }
    
    @org.junit.After
    public void tearDown() {
        writer.close();
    }
    
    private static final Logger logger = LoggerFactory.getLogger(FinalTest.class);
}
