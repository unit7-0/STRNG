/**
 * Date:	19 апр. 2015 г.
 * File:	ACSTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.unit7.study.diplom.bookstack.base.Generator;
import com.unit7.study.diplom.bookstack.base.IntSEGenerator;
import com.unit7.study.diplom.bookstack.base.SEGenerator;

/**
 * @author unit7
 *
 */
public class ACSTest {
    private static final Logger logger = LoggerFactory.getLogger(ACSTest.class);
    
    @Test
    public void testMethod() {
        final long n = (long) Math.sqrt(g.power());     // размер выборки
        final long m = (long) (n * 0.2);                // размер обучающей выборки
        final long rem = n - m;                         // размер тестовой выборки
        
        logger.debug("Size of selection: {}", n);
        logger.debug("Size of study selection: {}", m);
        logger.debug("Size of test selection: {}", rem);
        
        final Map<Integer, Integer> freq = new HashMap<>();
        
        // обучающая выборка
        for (int i = 0; i < m; ++i) {
            final Integer next = g.next().intValue();
            if (freq.containsKey(next))
                freq.put(next, freq.get(next) + 1);
            else
                freq.put(next, 1);
        }
        
        final long otherSetSize = g.power() - freq.size();
        
        logger.debug("Generator power: {}", g.power());
        logger.debug("other set size: {}", otherSetSize);
        logger.debug("Frequences set: {}", freq);
        
        // разбиваем на подмножества
        final int defaultMinDist = (int) (m * 0.05);
        final List<Set<Integer>> sets = new ArrayList<>();
        
        logger.debug("Default minimum distance between elements: {}", defaultMinDist);
        
        ArrayList<Map.Entry<Integer, Integer>> freqList = Lists.newArrayList(freq.entrySet());
        
        // сортируем частоты
        Collections.sort(freqList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        
        logger.debug("Sorted frequencies: {}", freqList);
        
        int lastFreq = 0; // последняя встреченная частота
        
        // хранит количество элементов + сами элементы
        Set<Integer> curSet = new HashSet<>();
        for (int i = 0; i < freqList.size(); ++i) {
            final Map.Entry<Integer, Integer> entry = freqList.get(i);
            final int curFreq = entry.getValue();
            if (curFreq - lastFreq <= defaultMinDist) {
                lastFreq = curFreq;
                curSet.add(entry.getKey());                // добавляем очередной встреченный символ к множеству с похожей частотой встречаемости
            } else {
                if (!curSet.isEmpty()) {
                    sets.add(curSet);
                }
                
                curSet = new HashSet<>();
                curSet.add(entry.getKey());
                lastFreq = curFreq;
            }
        }
        
        if (!curSet.isEmpty())
            sets.add(curSet);
        
        logger.debug("Builded sets: {}", sets);
        
        // сортируем так, чтобы множества с большим количеством элементов находилось ближе к началу списка
        Collections.sort(sets, new Comparator<Set<Integer>>() {

            @Override
            public int compare(Set<Integer> o1, Set<Integer> o2) {
                return Integer.valueOf(o2.size()).compareTo(o2.size());
            }
        });
        
        logger.debug("Builded sets after sort: {}", sets);
        
        // прогоняем тестовую выборку
        
        Map<Integer, Integer> countMap = new TreeMap<>();
        
        int otherSet = 0;
        
        for (int i = 0; i < rem; ++i) {
            final Integer next = g.next().intValue();
            //logger.debug("next value: {}", next);
            // ищем в каком множестве наш символ
            boolean finded = false;
            for (int j = 0; j < sets.size(); ++j) {
                final Set<Integer> set = sets.get(j);
                if (set.contains(next)) {
                    finded = true;
                    if (countMap.containsKey(j)) {
                        countMap.put(j, countMap.get(j) + 1);
                    } else {
                        countMap.put(j, 1);
                    }
                    
                    break;
                }
            }
            
            if (!finded) {
                // в наших множествах нет, значит элемент из другого множества, добавляем ему статистику
                otherSet += 1;
            }
        }
        
        // сделать проверку по хи-квадрат
        
        for (int i = 0; i < sets.size(); ++i) {
            final Set<Integer> set = sets.get(i);
            logger.debug("size of [ {} ] set: {}, and probablity: {}", i, set.size(), (double) set.size() / g.power());
        }
        
        logger.debug("And otherSet probablity: {}", (double) otherSetSize / g.power());
        
        logger.debug("After counting. otherSet: {}, countMap: {}", otherSet, countMap);
        
        logger.debug("Real probablities:");
        for (Integer key : countMap.keySet()) {
            logger.debug("P for set [ {} ]: {}", key, (double) countMap.get(key) / rem);
        }
        
        logger.debug("For other sets: {}", (double) otherSet / rem);
    }
    
    @After
    public void destroy() {
        final long endTime = System.currentTimeMillis() - startTime;
        logger.debug("Execution time: {} sec.", ((double) endTime) / 1000);
    }
    
    @Before
    public void init() {
        g = new IntSEGenerator(true);
        
        startTime = System.currentTimeMillis();
    }
    
    private long startTime;
    
    private Generator<Integer> g;
}
