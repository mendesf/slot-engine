package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;

import io.mendesf.engine.core.SlotConfigReferenceException;

public record DisplayConfig(String name, int columns, int rows, List<ReelConfig> reels) {

	public DisplayConfig(DisplayConfigData data, Map<String, ReelConfig> reelsByName) {
		this(data.name(), data.columns(), data.rows(), mapReels(data, reelsByName));
	}

	private static List<ReelConfig> mapReels(DisplayConfigData data, Map<String, ReelConfig> reelsByName) {
		return data.reels().stream().map(reelName -> {
			ReelConfig reel = reelsByName.get(reelName);
			if (reel == null) {
				throw new SlotConfigReferenceException(
						"Reel '" + reelName + "' not found for display '" + data.name() + "'.");
			}
			return reel;
		}).toList();
	}

	public String debug() {
		return "%s (%dx%d, reels=%d)".formatted(name, columns, rows, reels.size());
	}

}
