/**
 * Date:	01 февр. 2015 г.
 * File:	TestSettings.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.base.old;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.unit7.study.diplom.base.generator.Generator;

/**
 * @author unit7
 *
 */
public class BookStackTestSettings<T extends Generator<?>> {
    public BookStackTestSettings(T generator) {
        this.generator = generator;
    }

    public int getR() {
        return r;
    }

    /**
     * @param r количество множеств (r > 1)
     */
    public void setR(int r) {
        if (r > 1)
            this.r = r;
    }

    public long[] getK() {
        return k;
    }

    /**
     * Установить для каждого множества конечный номер.
     * Количество элементов должно быть равно установленному r.
     * Каждый элемент должен быть больше предыдущего.
     * Первый элемент должен быть больше либо равен единице,
     * а последний должен быть равен мощности множества генератора.
     */
    public void setK(long[] k) {
        if (k.length == r) {
            for (int i = 0; i < r; ++i) {
                if (i != 0 && k[i] <= k[i - 1])
                    return;
            }
            
            if (k[0] < 1 || k[r - 1] != generator.power())
                return;
            
            this.k = k;
            this.p = new double[r];
            for (int i = 0; i < r; ++i) {
                p[i] = (double) (k[i] - (i > 0 ? k[i - 1] : 0)) / generator.power();
            }
        }
    }

    /**
     * @return вероятности для всех множеств
     */
    public double[] getP() {
        return p;
    }

    /**
     * @return генератор выборки
     */
    public T getGenerator() {
        return generator;
    }
    
    /**
     * @return конечный номер i-го множества
     */
    public long getKi(int i) {
        return k[i];
    }
    
    /**
     * @return вероятность появления i-го номера в выборке
     */
    public double getPi(int i) {
        return p[i];
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("generator", generator).
                add("r", r).
                add("k", Arrays.toString(k)).
                add("p", Arrays.toString(p)).
                toString();
    }
    
    private T generator;
    private int r;
    private long[] k;
    private double[] p;
}
