package com.borabora.arduino.serialcomm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by mrbueno on 11/10/15.
 */
public class SerialActions {

    Map<Integer, Function<byte[], String>> map = new HashMap<>();

    public Function<byte[], String> get(Integer index) {
        Function<byte[], String> action = map.get(index);
        return action;
    }

    public void put(Integer command, Function<byte[], String> action) {
        map.put(command, action);
    }

    public boolean containsKey(Integer command) {
        return map.containsKey(command);
    }
}
