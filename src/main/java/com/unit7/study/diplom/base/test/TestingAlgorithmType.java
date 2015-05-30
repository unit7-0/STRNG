/**
 * Date:	10 мая 2015 г.
 * File:	TestAlgorithmType.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.base.test;

/**
 * Тип алгоритма тестирования
 * 
 * @author unit7
 *
 */
public enum TestingAlgorithmType {
    NAT("Новый адаптивный хи-квадрат"),
    BOOK_STACK("Стопка книг");
    
    private String caption;
    
    private TestingAlgorithmType(String caption) {
        this.caption = caption;
    }
    
    public String getCaption() {
        return caption;
    }
}
