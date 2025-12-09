package io.mendesf.engine.slot.config;

import java.util.List;

record EngineConfigData(BetConfigData bet, List<SymbolConfigData> symbols, List<DisplayConfigData> displays,
		List<ReelConfigData> reels, List<PayTableConfigData> payTables, List<StageConfigData> stages) {
}
