/**
 * Date:	19 апр. 2015 г.
 * File:	ACSTest.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.acs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.unit7.study.diplom.base.Generator;
import com.unit7.study.diplom.base.IntSEGenerator;
import com.unit7.study.diplom.base.SequenceIntGenerator;

/**
 * @author unit7
 *
 */
public class ACSTest {
    private static final Logger logger = LoggerFactory.getLogger(ACSTest.class);
    
    @Test
    public void testMethod() {
        final long n = (long) Math.cbrt(g.power());     // размер выборки
        final long m = (long) (n * 0.4);                // размер первой обучающей выборки
        final long k = (long) (m * 0.7);                // размер второй обучающей выборки
        final long rem = n - m - k;                     // размер тестовой выборки
        
        logger.info("Selection count: {}, m: {}, k: {}, testing selection: {}", n, m, k, rem);
        
        // множество встреченных символов
        final SortedSet<Long> firstSet = new TreeSet<>();
        
        for (int i = 0; i < m; ++i) {
            final long val = g.next().longValue() - (long) Integer.MIN_VALUE;
            // make all numbers positive
            firstSet.add(val);
        }
        
        logger.debug("First set after selection: {}", firstSet);
        logger.info("First set size: {}", firstSet.size());
        
        // среднее растояние между символами
        double r = findDistance(firstSet);
        
        logger.info("Firstly finded distance: {}", r);
        logger.debug("Process first set...");
        
        // should b1 > r
        
        while (firstSet.first() <= r) {
            logger.debug("Remove first element: {}", firstSet.first());
            firstSet.remove(firstSet.first());
        }
        
        logger.debug("After first condition preparing: {}", firstSet);
        logger.info("After first condition preparing set size: {}", firstSet.size());
        
        // should b[last] < S - R
        while (firstSet.last() >= (g.power() - r)) {
            logger.debug("Remove last element: {}", firstSet.last());
            firstSet.remove(firstSet.last());
        }
        
        logger.debug("After third condition preparing: {}", firstSet);
        logger.info("After third condition preparing set size: {}", firstSet.size());
        
        // прорежаем выборки и находим новое среднее растояние между символами
        final double newDist = normalizeSet(firstSet, r);
        
        logger.info("Newly finded distance: {}", newDist);
        logger.debug("After full first set normalization: {}", firstSet);
        logger.info("After full first set normalization size: {}", firstSet.size());
        
        // stage 2
        
        // множество растояний до ближайшего символа в первой выборке из второй обучающей
        final Set<Long> distances = new HashSet<>();
        
        for (int i = 0; i < k; ++i) {
            final long next = g.next().longValue() - (long) Integer.MIN_VALUE;
            final long dist = findMinDist(firstSet, next);
            if (dist != -1) {
                if (dist < r)
                    distances.add(dist);
            }
        }
        
        logger.debug("Finded distances: {}", distances);
        logger.info("Distances size: {}", distances.size());
        
        if (firstSet.isEmpty() || distances.isEmpty())
            throw new OperationImpossibleException("One of sets is empty: ");
        
        // stage 3
        
        long freq = 0;
        
        // прогон тестовой выборки
        // если растояние до ближайшего символа в первом множестве находится во втором множестве
        // увеличиваем частоту встреченных символов
        
        // (n - k - m) = 5 * s / (2 |B| |C|)
        final long remain = (5 * g.power() / (2 * firstSet.size() * distances.size())) - k - m;
        
        logger.info("Real testing selection size: {}", remain);
        
        for (int i = 0; i < remain; ++i) {
            final long next = g.next().longValue() - (long) Integer.MIN_VALUE;
            final long minDist = findMinDist(firstSet, next);
            
            if (minDist != -1) {
                if (distances.contains(minDist)) {
                    freq += 1;
                }
            }
        }
        
        logger.info("Result frequency: {}", freq);
        
        // вероятность выпадания символа из первых двух выборок
        
        final double p = 2.0 * firstSet.size() * distances.size() / g.power();
        
        logger.info("Probablity: {}", p);
        
        final double np = rem * p;
        final double np2 = rem * (1 - p);
        
        // проверяем по критерию хи квадрат
        
        final double x2 = (freq - np) * (freq - np) / np + (rem - freq - np2) * (rem - freq - np2) / np2;
        
        logger.info("x2: {}", x2);
        
        final double hi2 = 0.00004; // 0.995
        
        Assert.assertTrue("", hi2 >= (x2 - 0.000001));
    }
    
    private long findMinDist(SortedSet<Long> set, long val) {
        long result = Long.MAX_VALUE;
        
        // TODO make binary search
        
        for (Long cur : set) {
            final long dist = Math.abs(cur - val);
            if (dist < result) {
                result = dist;
            } else {
                // дистанция только стала больше, т.к. числа упорядочены, дальше идти смысла нет
                break;
            }
        }
        
        if (result == Long.MAX_VALUE)
            return -1;
        
        return result;
    }
    
    /**
     * Проредить выборку для выполнения условий
     */
    private double normalizeSet(SortedSet<Long> set, double r) {
        double r2 = 2 * r;
        double newDist = r;
        while (true) {
            boolean finished = true;
            final Iterator<Long> it = set.iterator();
            long prev = it.next();
            while (it.hasNext() && finished) {
                long cur = it.next();
                int dist = (int) (cur - prev);
                if (dist >= r2) {
                    newDist = removeWithMinDist(set, newDist, dist);
                    r2 = newDist * 2;
                    logger.debug("During normalizing newly distance finded: {}", newDist);
                    finished = false;
                    break;
                }
                
                prev = cur;
                
                // TODO после удаления символа можно продолжить считать с того же места либо откатиться назад, но на
                // меньшее растояние чем сейчас - нужно знать позицию удаленного символа
            }
            
            if (finished) {
                break;
            }
        }

        return newDist;
    }
    
    /**
     * Удалить из множества элемент с минимальной дистанцией до другого
     * TODO добавить возможность удаления только последнего элемента наряду с первым
     */
    private double removeWithMinDist(SortedSet<Long> set, double current, int required) {
        logger.debug("Remove element from set, current dist: {}, required 2R - 1: {}", current, required);
        
        Preconditions.checkArgument(!set.isEmpty(), "set should be not empty");
        
        // можно попробовать удалять только первый элемент пока не достигнем предела
        // должно быть b1 > R
        double newDistance = current;//tryRemoveFirst(set, current, required);
//        double doubledDistance = newDistance * 2;
        
//        if (required < doubledDistance)
//            return newDistance;
        
        if (set.size() < 3)
            throw new OperationImpossibleException("set size should be >= 3. current: " + set.size());
        
        do {
            final Iterator<Long> it = set.iterator();
            long pprev = it.next();
            long prev = it.next();
            
            double calced = (double) distanceSum / (set.size() - 2);
            double doubled = calced * 2;
            
            boolean removed = false;
            
            while (it.hasNext()) {
                long cur = it.next();
                long dist = cur - pprev;
                
                // условие выполняется после удаления среднего элемента
                if (dist < doubled) {
                    set.remove(prev);
                    removed = true;
                    break;
                }
                
                pprev = prev;
                prev = cur;
            }
            
            logger.debug("Remove element: {}, newDistance: {}", prev, calced);
            
            if (required < doubled) {
                newDistance = calced;
                break;
            }
            
            if (!removed) {
                throw new OperationImpossibleException("Operation impossible with this params");
            }
            
        } while(true);
        
        return newDistance;
    }
    
    /**
     * Попробовать удалить первый элемент множества, не нарушив условия множества: первый элемент должен быть больше R
     * @param set множество
     * @param current текущее среднее растояние
     * @param requiredMin требуемое минимальное растояние (required < 2R)
     * @return новое среднее растояние после удаления элемента
     * 
     * Процедура некорректна
     */
    @Deprecated
    private double tryRemoveFirst(SortedSet<Long> set, double current, int requiredMin) {
        double calced = current;
        
        do {
            if (set.size() < 1)
                throw new OperationImpossibleException("Set size should be >= 2. current: " + set.size());
            
            final Iterator<Long> it = set.iterator();
            final long e = it.next();
            final long sec = it.next();
            
            double newDist = distanceSum / (set.size() - 2);
            double doubledDist = newDist * 2;
            
            if (sec <= newDist) {
                break;
            }
            
            calced = newDist;
            set.remove(e);
            
            logger.debug("Condition success. Removed element: {}, newDistance: {}, newNext: {}", e, newDist, sec);
            
            if (requiredMin < doubledDist) {
                break;
            }
        } while(true);
        
        logger.debug("Newly calculated distance: {}", calced);
        
        return calced;
    }
    
    /**
     * Найти среднее растояние между символами в множестве
     */
    private double findDistance(SortedSet<Long> set) {
        long result = 0;
        
        final Iterator<Long> it = set.iterator();
        long prev = it.next();
        while (it.hasNext()) {
            long cur = it.next();
            int dist = (int) (cur - prev);
            result += dist;            
            prev = cur;
        }
        
        distanceSum = result;
        
        return (double) result / (set.size() - 1);
    }
    
    @After
    public void destroy() {
        final long endTime = System.currentTimeMillis() - startTime;
        logger.info("Execution time: {} sec.", ((double) endTime) / 1000);
    }
    
    @Before
    public void init() {
        g = new SequenceIntGenerator();
        
        startTime = System.currentTimeMillis();
    }
    
    private long startTime;
    
    private Generator<Integer> g;
    
    private long distanceSum;
}

class OperationImpossibleException extends RuntimeException {

    private static final long serialVersionUID = 2978838653696279982L;

    public OperationImpossibleException() {
        super();
    }

    public OperationImpossibleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OperationImpossibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationImpossibleException(String message) {
        super(message);
    }

    public OperationImpossibleException(Throwable cause) {
        super(cause);
    }
    
}