/**
 * Date:	10 мая 2015 г.
 * File:	TestWorkflow.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.test.TestingAlgorithm;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;
import com.unit7.study.diplom.base.test.Workflow;

/**
 * Окружение теста
 * 
 * @author unit7
 *
 */
public class TestWorkflow<T> implements Workflow {
    
    private static final Logger logger = LoggerFactory.getLogger(TestWorkflow.class);

    public TestWorkflow() {
    }

    @Override
    public void start() {
        Preconditions.checkNotNull(generator, "Generator is required");
        Preconditions.checkNotNull(algorithmType, "Algorithm type is required");
        
        logger.debug("Test iterations: {}", iterations);
        logger.debug("Test selection count: {}", selectionCount);
        logger.debug("Test algorithm type: {}", algorithmType);
        logger.debug("Test generator: {}", generator);
        
        short failedTestCounter = 0;
        
        @SuppressWarnings("unchecked")
        final T[] sequence = (T[]) Array.newInstance(BitSet.class, selectionCount);
        
        result = new ArrayList<>(iterations);
        
        for (short i = 0; i < iterations; ++i) {
            for (int j = 0; j < selectionCount; ++j) {
                sequence[j] = generator.next();
            }
            
            final TestingAlgorithm<T> alg = TestingAlgorithmFactory.createAlgorithm(algorithmType, sequence, generator.bitCount());
            alg.setHi2(0.00393); // уровень значимости 0.95
            final boolean testResult = alg.test();
            
            logger.debug("Iteration {}, testResult: {}", i, testResult);
            
            result.add(testResult);
            if (!testResult) {
                failedTestCounter += 1;
            }
        }
        
        logger.info("Failed test counter: {}", failedTestCounter);
    }
    
    public short getIterations() {
        return iterations;
    }

    public void setIterations(short iterations) {
        this.iterations = iterations;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(int selectionCount) {
        this.selectionCount = selectionCount;
    }

    public Generator<T> getGenerator() {
        return generator;
    }

    public void setGenerator(Generator<T> generator) {
        this.generator = generator;
    }

    public TestingAlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(TestingAlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
    
    public List<Boolean> getTestResult() {
        return result;
    }

    private short iterations;                   // количество итераций теста
    private int selectionCount;                 // количество выборок
    private Generator<T> generator;             // генератор выборки
    private TestingAlgorithmType algorithmType;    // тип алгоритма тестирования
    
    private List<Boolean> result;
}
