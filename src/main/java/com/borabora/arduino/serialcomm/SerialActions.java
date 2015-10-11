package com.borabora.arduino.serialcomm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by mrbueno on 11/10/15.
 */
public class SerialActions {

    Map<Integer, Function<String, String>> map = new HashMap<>();

    public Function<String, String> get(Integer index) {
        Function<String, String> action = map.get(index);
        return action;
    }

    public void put(Integer command, Function<String, String> action) {
        map.put(command, action);
    }

    public boolean containsKey(Integer command) {
        return map.containsKey(command);
    }
}
