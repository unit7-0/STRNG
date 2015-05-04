/**
 * Date:	01 февр. 2015 г.
 * File:	BookStackTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.generator.Generator;
import com.unit7.study.diplom.base.generator.impl.IntSEGenerator;
import com.unit7.study.diplom.bookstack.base.old.BookStackTestSettings;

/**
 * @author unit7
 *
 */
public class BookStackTest {

    private static final Logger log = LoggerFactory.getLogger(BookStackTest.class);
    
    @Before
    public void init() {
        settings = new BookStackTestSettings<Generator<Integer>>(new IntSEGenerator(true));
    }
    
    @Test
    public void testSEGeneratorNew() {
        Generator<Integer> g = settings.getGenerator();
        int r = 2;
        settings.setR(r);
        
        // разбиваем на два множества
        // алфавит n=2^32. Критерий применим только если n*Pi > 10 следовательно
        // 100 элементов будет достаточно
        final  int n = 100;
        // номера первого множества
        int[] k = new int[n];
        for (int i = 0; i < n; ++i) {
            k[i] = Integer.MIN_VALUE + i;
        }
        
        final double s = Math.pow(2, 32);
        
        double p0 = n / s;
        double p1 = (s - n) / s;
        
        int n0 = 0;
        final int selectionCount = 1000000;
        for (int i = 0; i < selectionCount; ++i) {
            long generated = g.next();
            // определяем номер множества
            for (int j = 0; j < n; ++j) {
                if (k[j] == generated) {
                    n0 += 1;
                    break;
                }
            }
        }
        
        final double np0 = selectionCount * p0;
        final double np1 = selectionCount * p1;
        
        final double x2 = sqr(n0 - np0) / (np0) + sqr((selectionCount - n0) - np1) / (np1);
        final double hi2 = getHi2(r - 1, 1 - 1.0 / g.power());
        
        log.debug("x^2 = {}, hi^2 = {}", x2, hi2);
        
        log.debug("getHi2(1, 0.99) = {}", getHi2(1, 0.99));
        log.debug("getHi2(2, 0.99) = {}", getHi2(2, 0.99));
        log.debug("getHi2(2, 0.01) = {}", getHi2(2, 0.01));
        log.debug("getHi2(3, 0.75) = {}", getHi2(3, 0.75));
        
        Assert.assertTrue(hi2 >= x2);
    }
    
    public double sqr(double a) {
        return a * a;
    }
    
//    @Test
    public void testSEGenerator() {
        Generator<Integer> g = settings.getGenerator();
        int r = 2;
        settings.setR(r);
        calcSettings();
        
        log.debug("test settings: {}", settings);
        
        final int selectionCount = 1000000;
        // счетчик номеров для каждого множества
        Map<Long, Integer> setCounter = new HashMap<>();
        
        final long start = System.currentTimeMillis();
        
        // счита  ем количество выпавших номеров для каждого множества для достаточно большой выборки
        for (int i = 0; i < selectionCount; ++i) {
            // так как все элементы упорядочены и пронумерованы от 1 до 4294967296, то получить номер просто:
            long n = g.next() + 2147483649L;
            long setNumber = getSetNumberByElNumber(n);
            if (setCounter.containsKey(setNumber))
                setCounter.put(setNumber, setCounter.get(setNumber) + 1);
            else
                setCounter.put(setNumber, 1);
        }
        
        log.debug("setCounter: {}", setCounter);
        
        // посчитаем x^2
        double x2 = 0;
        for (int i = 0; i < r; ++i) {
            double expected = selectionCount * settings.getPi(i);
            Integer setCounterNext = setCounter.get(i);
            if (setCounterNext == null)
                setCounterNext = 0;
            
            double actual = (setCounterNext - expected);
            x2 += actual * actual / expected;
        }
        
        double hi2 = getHi2(r - 1, 1 - 1.0 / g.power());
        log.debug("x^2 = {}, hi^2 = {}", x2, hi2);
        log.debug(String.format("Set counter: %d, selection count: %d, time elapsed: %.3f sec",
                r, selectionCount, (System.currentTimeMillis() - start) / 1000.0));
        
        Assert.assertTrue(hi2 >= x2);
    }
    
    
    /**
     * Получить хи квадрат
     */
    private double getHi2(double degrees, double p) {
        /*ChiSquaredDistribution chi = new ChiSquaredDistribution(degrees);
        chi.
        final double gamma = Gamma.gamma(degrees / 2);
        return (1.0 / (Math.pow(2, degrees / 2) * gamma)) * 
                (Math.pow(p, degrees / 2 - 1) * (Math.pow(Math.E, -p / 2)));*/
        return 0.00004;
    }
    
    /**
     * Получить номер i множества A<sub>i</sub> по номеру элемента в множестве
     * @param n номер элемента
     */
    private int getSetNumberByElNumber(long n) {
        for (int i = 0; i < settings.getK().length; ++i) {
            if (n <= settings.getKi(i))
                return i;
        }
        
        log.error("for number {} set was not found");
        return -1;
    }
    
    public void calcSettings() {
        Generator<Integer> g = settings.getGenerator();
        long power = g.power();
        int r = settings.getR();
        long count = power / r;
        long k[] = new long[r];
        for (int i = 0; i < r; ++i)
            k[i] = (i > 0 ? k[i - 1] : 0) + count;
        
        k[r - 1] += power % r;
        settings.setK(k);
    }
    
    private BookStackTestSettings<Generator<Integer>> settings;
}
