/**
 * Date:	05 апр. 2015 г.
 * File:	BSSet.java
 *
 * Author:	Zajcev V.
 */

package com.unit7.study.diplom.bookstack.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Objects;

/**
 * Множество, основанное на хэш-таблицах и алгоритме стопка книг.
 * При доступе к элементу из ячейки, если там связанный список из-за
 * совпадения хэш-значений, то он будет перемещен в начало такого списка.
 * Таким образов к наиболее часто запрашиваемым элементам доступ будет
 * быстрее.
 * 
 * @author unit7
 *
 */
public class BSSet<T> implements Set<T> {
    private int size;
    private LinkedList<T>[] table;
    
    private static final Object NULL_OBJECT = new Object();
    
    @SuppressWarnings("unchecked")
    public BSSet(int size) {
        if (size > (1 << 16))
            size = (1 << 16);
        
        this.table = new LinkedList[size];
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Object val = o;
        if (val == null)
            val = NULL_OBJECT;
        
        @SuppressWarnings("unchecked")
        final int hashIndex = hashIndex((T) o);
        final LinkedList<T> values = table[hashIndex];
        
        if (values == null)
            return false;
        
        int index = 0;
        for (T valueInTable : values) {
            if (valueInTable.equals(val)) {
                // переместить в начало списка
                values.remove(index);
                values.add(0, valueInTable);
                return true;
            }
            index += 1;
        }
        
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index;
            Iterator<T> listIterator;
            
            @Override
            public boolean hasNext() {
                return selectNext();
            }

            private boolean selectNext() {
                if (listIterator == null || !listIterator.hasNext()) {                    
                    while (index < size && table[index] == null)
                        ++index;
                    
                    if (index >= size)
                        return false;
                    
                    listIterator = table[index++].iterator();
                }
                
                return listIterator.hasNext();
            }
            
            @Override
            public T next() {
                if (!selectNext())
                    throw new NoSuchElementException();
                
                return listIterator.next();
            }
            
        };
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("This operation not supported yet");
    }

    @Override
    public <E> E[] toArray(E[] a) {
        throw new UnsupportedOperationException("This operation not supported yet");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(T e) {
        final int hashIndex = hashIndex(e);
        Object insertingValue = e;
        if (e == null)
            insertingValue = NULL_OBJECT;
        
        LinkedList<Object> valueList;
        if (table[hashIndex] == null) {
            valueList = new LinkedList<>();
            table[hashIndex] = (LinkedList<T>) valueList;
        } else {
            valueList = (LinkedList<Object>) table[hashIndex];
        }
        
        // проверим есть ли значение уже в списке
        for (Object val : valueList) {
            // TODO переставлять ли найденное значение в начало списка?
            if (val.equals(insertingValue))
                return false;
        }
        
        // если нет, добавим в начало
        valueList.add(0, insertingValue);
        size += 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("This operation not supported yet");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null || c.size() == 0)
            return false;
        
        boolean result = false;
        for (Iterator<?> it = c.iterator(); it.hasNext(); ) {
            Object e = it.next();
            result &= contains(e);
        }
        
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null || c.size() == 0)
            return false;
        
        boolean result = true;
        for (T e : c) {
            result &= add(e);
        }
        
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This operation not supported yet");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This operation not supported yet");
    }

    @Override
    public void clear() {
        size = 0;
        for (int i = 0; i < table.length; ++i) {
            table[i] = null;
        }
    }
    
    @Override
    public String toString() {
        return Objects
                .toStringHelper(this)
                .add("table", Arrays.toString(table))
                .add("size", size)
                .toString();
    }
    
    /**
     * Вычислить хэш для объекта
     */
    protected int hash(T val) {
        return val == null ? 0 : val.hashCode();
    }
    
    /**
     * Вычислить номер ячейки в таблице по хэш-коду
     */
    protected int hashIndex(T val) {
        int h = Math.abs(hash(val));
        if (h < 0)
            h = (h + 1) * -1;
        
        return h % table.length;
    }
}
