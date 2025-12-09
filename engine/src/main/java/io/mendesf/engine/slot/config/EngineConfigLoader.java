package io.mendesf.engine.slot.config;

import io.mendesf.engine.util.JsonDeserializer;

public final class EngineConfigLoader {

	private EngineConfigLoader() {
	}

	public static EngineConfig fromJson(String filename) {
		EngineConfigData data = JsonDeserializer.deserialize(filename, EngineConfigData.class);
		return EngineConfig.from(data);
	}
}
