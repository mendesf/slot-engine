package io.mendesf.games.clusterdemo;

import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mendesf.engine.slot.SlotConfigData;

public class ClusterDemo {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream in = ClusterDemo.class.getResourceAsStream("/slot.json");
        if (in == null) {
            throw new IllegalStateException("slot.json not found on classpath");
        }

        SlotConfigData cfg = mapper.readValue(in, SlotConfigData.class);
        System.out.println(cfg.symbols.get(0).name);
    }
}
