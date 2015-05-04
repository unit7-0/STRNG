/**
 * Date:	01 февр. 2015 г.
 * File:	Generator.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.generator;

import java.util.Set;

/**
 * Базовый интерфейс всех тестируемых генераторов
 * 
 * @author unit7
 *
 */
public interface Generator<T> {
    /**
     * @return Следующий элемент последовательности
     */
    T next();
    
    /**
     * @return мощность множества, порождаемого генератором
     * Method has been deprecated. See com.unit7.study.diplom.base.Generator.bitCount
     */
    @Deprecated
    long power();
    
    /**
     * Количество бит, выделяемое для генерации одной последовательности
     */
    short bitCount();
    
    /**
     * @return множество, порождаемое генератором
     */
    Set<T> set();
}
