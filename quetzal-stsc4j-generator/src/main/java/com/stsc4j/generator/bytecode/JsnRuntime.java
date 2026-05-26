package com.stsc4j.generator.bytecode;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsnRuntime {

    // Serializa un LinkedHashMap a un formato JSON compacto
    public static String texto(LinkedHashMap<?, ?> map) {
        if (map == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean primero = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!primero) sb.append(",");
            primero = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            Object val = entry.getValue();
            if (val instanceof String) {
                sb.append("\"").append(val).append("\"");
            } else {
                sb.append(val);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Deserializa una cadena JSON simple y limpia a un LinkedHashMap de Quetzal
    public static LinkedHashMap<String, Object> jsn(String json) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (json == null) return map;
        String limpio = json.trim();
        if (limpio.startsWith("{") && limpio.endsWith("}")) {
            limpio = limpio.substring(1, limpio.length() - 1);
            if (limpio.isEmpty()) return map;
            String[] parejas = limpio.split(",");
            for (String pareja : parejas) {
                String[] kv = pareja.split(":");
                if (kv.length == 2) {
                    String k = kv[0].trim().replace("\"", "");
                    String v = kv[1].trim().replace("\"", "");
                    if (v.equals("verdadero") || v.equals("true")) map.put(k, true);
                    else if (v.equals("falso") || v.equals("false")) map.put(k, false);
                    else {
                        try { map.put(k, Integer.parseInt(v)); }
                        catch (Exception e) {
                            try { map.put(k, Float.parseFloat(v)); }
                            catch (Exception e2) { map.put(k, v); }
                        }
                    }
                }
            }
        }
        return map;
    }
}