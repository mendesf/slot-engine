package io.mendesf.games.clusterdemo;

import io.mendesf.engine.slot.config.EngineConfig;
import io.mendesf.engine.slot.config.EngineConfigLoader;

public class ClusterDemo {
	public static void main(String[] args) {
		EngineConfig config = EngineConfigLoader.fromJson("engine.json");
		System.out.println(config.debug());
	}
}
