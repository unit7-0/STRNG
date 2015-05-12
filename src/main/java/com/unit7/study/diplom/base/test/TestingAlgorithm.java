/**
 * Date:	04 мая 2015 г.
 * File:	TestAlgorithm.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test;


/**
 * Интерфейс алгоритма тестирования последовательностей на определение их случайности.
 * 
 * @author unit7
 *
 */
public abstract class TestingAlgorithm<T> {
    public TestingAlgorithm(T[] sequence, short bitCount) {
        this.sequence = sequence;
        this.bitCount = bitCount;
    }
    
    public abstract boolean test();
    
    protected short bitCount;                     // степень двойки мощности словаря
    protected T[] sequence;                  // выборка данных
}
