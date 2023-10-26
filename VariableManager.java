package org.example;

import java.util.HashMap;
import java.util.Map;

public class VariableManager {

    public void main(String[] args) {
        // yeah no1 cares about this
    }

    private final Map<String, Object> variables = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void set(String index, Object value) {
        String[] parts = index.split("::");

        Map<String, Object> currentMap = variables;
        for (int i = 0; i < parts.length - 1; i++) { // Loop till second last part
            currentMap.putIfAbsent(parts[i], new HashMap<String, Object>());
            if (!(currentMap.get(parts[i]) instanceof Map)) {
                throw new IllegalArgumentException("[JavaSkript, Variables] Cannot overwrite existing non-map value with a map at: " + parts[i]);
            }
            currentMap = (Map<String, Object>) currentMap.get(parts[i]);
        }

        if (currentMap.get(parts[parts.length - 1]) instanceof Map && value != null && !(value instanceof Map)) {
            throw new IllegalArgumentException("[JavaSkript, Variables] Cannot overwrite existing map with a non-map value at: " + parts[parts.length - 1]);
        }

        // For the final part of the index, set the actual value
        currentMap.put(parts[parts.length - 1], value);
        System.out.println(currentMap.toString());
    }

    // You might also want a method to get the value based on an index
    @SuppressWarnings("unchecked")
    public Object get(String index) {
        String[] parts = index.split("::");

        Map<String, Object> currentMap = variables;
        for (int i = 0; i < parts.length - 1; i++) {
            if (!(currentMap.get(parts[i]) instanceof Map)) {
                return null; // Return null if any intermediate key doesn't have a map
            }
            currentMap = (Map<String, Object>) currentMap.get(parts[i]);
        }

        return currentMap.get(parts[parts.length - 1]);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNestedMap(String index) {
        String[] parts = index.split("::");

        Map<String, Object> currentMap = variables;
        for (int i = 0; i < parts.length; i++) {
            if (!(currentMap.get(parts[i]) instanceof Map)) {
                return null; // Return null if any intermediate key doesn't have a map
            }
            currentMap = (Map<String, Object>) currentMap.get(parts[i]);
        }

        return currentMap;
    }

    public Object[] valueList(String index) {
        Map<String, Object> map = getNestedMap(index);
        if (map == null) return new Object[0];

        return map.values().stream()
                .filter(v -> !(v instanceof Map))  // Filter out any values that are Maps
                .toArray();
    }

    public String[] indexList(String index) {
        Map<String, Object> map = getNestedMap(index);
        if (map == null) return new String[0];

        return map.keySet().toArray(new String[0]);
    }

    public Object[] entryList(String index) {
        Map<String, Object> map = getNestedMap(index);
        if (map == null) return new Object[0];

        return map.entrySet().stream()
                .filter(entry -> !(entry.getValue() instanceof Map)) // Filter out any entries with Map values
                .toArray();
    }




}
