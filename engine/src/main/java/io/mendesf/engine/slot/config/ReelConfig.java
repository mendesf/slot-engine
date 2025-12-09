package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;

import io.mendesf.engine.core.SlotConfigReferenceException;

public record ReelConfig(String name, ReelDistributionType distributionType, List<SymbolConfig> symbols,
		List<Long> weights) {

	public ReelConfig(ReelConfigData data, Map<String, SymbolConfig> symbolsByName) {
		this(data.name(), data.distributionType(), mapSymbols(data, symbolsByName), List.copyOf(data.weights()));

		if (symbols.size() != weights.size()) {
			throw new SlotConfigReferenceException(
					"Reel '" + name + "' has " + symbols.size() + " symbols but " + weights.size() + " weights.");
		}
	}

	private static List<SymbolConfig> mapSymbols(ReelConfigData data, Map<String, SymbolConfig> symbolsByName) {
		return data.symbols().stream().map(symbolName -> {
			SymbolConfig symbol = symbolsByName.get(symbolName);
			if (symbol == null) {
				throw new SlotConfigReferenceException(
						"Symbol '" + symbolName + "' not found for reel '" + data.name() + "'.");
			}
			return symbol;
		}).toList();
	}

	public String debug() {
		return "%s [%d symbols, type=%s]".formatted(name, symbols.size(), distributionType);
	}

}
