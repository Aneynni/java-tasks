package ru.mail.polis.homework.collections;


import java.util.*;

/**
 * Написать структуру данных, реализующую интерфейс мапы + набор дополнительных методов.
 * 4 дополнительных метода должны возвращать самый популярный ключ и его популярность. (аналогично для самого популярного значения)
 * Популярность - это количество раз, который этот ключ/значение учавствовал/ло в других методах мапы, такие как
 * containsKey, get, put, remove (в качестве параметра и возвращаемого значения).
 * Считаем, что null я вам не передаю ни в качестве ключа, ни в качестве значения
 *
 * Так же надо сделать итератор (подробности ниже).
 *
 * Важный момент, вам не надо реализовывать мапу, вы должны использовать композицию.
 * Вы можете использовать любые коллекции, которые есть в java.
 *
 * Помните, что по мапе тоже можно итерироваться
 *
 *         for (Map.Entry<K, V> entry : map.entrySet()) {
 *             entry.getKey();
 *             entry.getValue();
 *         }
 *
 * Всего 10 тугриков (3 тугрика за общие методы, 2 тугрика за итератор, 5 тугриков за логику популярности)
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class PopularMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;

    private Map<K, Integer> keysPopularity;
    private Map<V, Integer> valuesPopularity;

    private K mostPopularKey;
    private V mostPopularValue;

    public PopularMap() {
        this.map = new HashMap<>();
        this.keysPopularity = new HashMap<>();
        this.valuesPopularity = new HashMap<>();
    }

    public PopularMap(Map<K, V> map) {
        this.map = map;
        this.keysPopularity = new HashMap<>();
        this.valuesPopularity = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        increaseKeyPopularity((K) key);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        increaseValuePopularity((V) value);
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        increaseKeyPopularity((K) key);
        V value = map.get(key);
        increaseValuePopularity(value);
        return value;
    }

    @Override
    public V put(K key, V value) {
        increaseKeyPopularity(key);
        increaseValuePopularity(value);
        if (map.containsKey(key)) {
            increaseValuePopularity(map.get(key));
        }
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        increaseKeyPopularity((K) key);
        increaseValuePopularity(map.get(key));
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    private void increaseKeyPopularity(K key) {
        if (!keysPopularity.containsKey(key)) {
            keysPopularity.put(key, 0);
        }
        int popularity = keysPopularity.get(key) + 1;
        keysPopularity.put(key, popularity);
        if (mostPopularKey == null) {
            mostPopularKey = key;
            return;
        }
        if (keysPopularity.get(mostPopularKey) < popularity) {
            mostPopularKey = key;
        }
    }
    private void increaseValuePopularity(V value) {
        if (!valuesPopularity.containsKey(value)) {
            valuesPopularity.put(value, 0);
        }
        int popularity = valuesPopularity.get(value) + 1;
        valuesPopularity.put(value, popularity);
        if (mostPopularValue == null) {
            mostPopularValue = value;
            return;
        }
        if (valuesPopularity.get(mostPopularValue) < popularity) {
            mostPopularValue = value;
        }
    }

    /**
     * Возвращает самый популярный, на данный момент, ключ
     */
    public K getPopularKey() {
        return mostPopularKey;
    }


    /**
     * Возвращает количество использование ключа
     */
    public int getKeyPopularity(K key) {
        return keysPopularity.getOrDefault(key, 0);
    }

    /**
     * Возвращает самое популярное, на данный момент, значение. Надо учесть что значени может быть более одного
     */
    public V getPopularValue() {
        return mostPopularValue;
    }

    /**
     * Возвращает количество использований значений в методах: containsValue, get, put (учитывается 2 раза, если
     * старое значение и новое - одно и тоже), remove (считаем по старому значению).
     */
    public int getValuePopularity(V value) {
        return valuesPopularity.getOrDefault(value, 0);
    }

    /**
     * Вернуть итератор, который итерируется по значениям (от самых НЕ популярных, к самым популярным)
     * 2 тугрика
     */
    public Iterator<V> popularIterator() {
        List<V> mapValues = new ArrayList<>();
        for (Map.Entry<V, Integer> entry : valuesPopularity.entrySet()) {
               if (entry.getKey() != null) {
                   mapValues.add(entry.getKey());
               }
        }
        Collections.sort(mapValues, (v1, v2) -> {
            if (getValuePopularity(v1) < getValuePopularity(v2)) {
                return -1;
            }
            if (getValuePopularity(v1) == getValuePopularity(v2)) {
                return 0;
            }
            return 1;
        });

        return mapValues.iterator();
    }
}
