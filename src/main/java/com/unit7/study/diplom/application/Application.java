/**
 * Date:	26 мая 2015 г.
 * File:	Application.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.application;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unit7.study.diplom.base.generator.impl.GenericLCG;
import com.unit7.study.diplom.base.test.TestingAlgorithmType;
import com.unit7.study.diplom.base.test.impl.TestWorkflow;

/**
 * @author unit7
 *
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) {
        try (final Scanner scanner = new Scanner(System.in)) {
            
            final PrintStream stream = System.out;
            
            stream.println("Задайте параметры тестирования");
            stream.print("Выберите тип теста: 1 - Новый адаптивный хи-квадрат, 2 - Стопка книг:");
            
            final int testType = scanner.nextInt();
            if (testType < 1 || testType > 2) {
                stream.println("Неверный тип теста, возможные значения: [1, 2]");
                return;
            }
            
            scanner.nextLine();
        
            stream.print("Введите длину последовательности, количество последовательностей[>3] и количество итераций теста через пробел:");
            
            final short blockSize = scanner.nextShort();
            final int blockCount = scanner.nextInt();
            final short iterationCount = scanner.nextShort();
            
            scanner.nextLine();          
            
            stream.print("Выбрать генератор из списка или задать параметры? y/n:");
            String ans = scanner.nextLine();
            
            final GenericLCG generator;
            
            if ("y".equals(ans.toLowerCase())) {
                stream.println("Выберети генератор из списка ниже");
                for (int i = 0; i < LCG_LIST.length; ++i) {
                    stream.println((i + 1) + " - " + LCG_LIST[i]);
                }
                
                final int g = scanner.nextInt() - 1;
                if (g < 0 || g >= LCG_LIST.length) {
                    stream.println("Неверный вариант");
                    return;
                }
                
                generator = LCG_LIST[g];
            } else { 
                
                stream.print("Задайте параметры генератора через пробел [a, b, m]:");
                String input = scanner.nextLine();
                
                String[] params = input.split("\\s");
                if (params.length != 3) {
                    stream.println("Неверное число параметров");
                    return;
                }
                
                final long a = Long.parseLong(params[0]);
                final long b = Long.parseLong(params[1]);
                final long m = Long.parseLong(params[2]);
                
                generator = new GenericLCG(a, b, m, 1L, blockSize);
            }
            
            generator.setBitCount(blockSize);
        
            final TestingAlgorithmType testingAlgorithmType = TestingAlgorithmType.values()[testType - 1];
            
            final TestWorkflow<BitSet> workflow = new TestWorkflow<>();
            
            workflow.setAlgorithmType(testingAlgorithmType);
            workflow.setIterations(iterationCount);
            workflow.setSelectionCount(blockCount);
            workflow.setGenerator(generator);
            
            workflow.start();
            
        } catch (Exception e) {
            logger.error("Произошла ошибка", e);
        }
    }
    
    private static final GenericLCG[] LCG_LIST = new GenericLCG[] {
        new GenericLCG(69069L, 1L, (((long) Integer.MAX_VALUE) + 1) * 2, 1L, (short) 1),
        new GenericLCG(1099087573L, 0L, (((long) Integer.MAX_VALUE) + 1) * 2, 1L, (short) 1),
        new GenericLCG(1220703125L, 0L, (long) Math.pow(2, 46), 1L, (short) 1),
        new GenericLCG(25214903917L, 11L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(33952834046453L, 0L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(44485709377909L, 0L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG((long) Math.pow(13, 13), 0L, (long) Math.pow(2, 59), 1L, (short) 1),
        new GenericLCG((long) Math.pow(5, 19), 1L, (long) Math.pow(2, 63), 1L, (short) 1),
        new GenericLCG((long) Math.pow(5, 19), 1L, (long) Math.pow(2, 48), 1L, (short) 1),
        new GenericLCG(9219741426499971445L, 1L, (long) Math.pow(2, 63), 1L, (short) 1)
    };
}
