/**
 * Date:	01 февр. 2015 г.
 * File:	Generator.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base;

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
     */
    long power();
    
    /**
     * @return множество, порождаемое генератором
     */
    Set<T> set();
}
