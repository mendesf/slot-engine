package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;

import io.mendesf.engine.core.SlotConfigReferenceException;

public record PayTableEntryConfig(SymbolConfig symbol, List<TierConfig> tiers) {
	public PayTableEntryConfig(PayTableEntryConfigData data, Map<String, SymbolConfig> symbolsByName) {
		this(resolveSymbol(data, symbolsByName), data.tiers().stream().map(TierConfig::new).toList());
	}

	private static SymbolConfig resolveSymbol(PayTableEntryConfigData data, Map<String, SymbolConfig> symbolsByName) {
		SymbolConfig symbol = symbolsByName.get(data.symbol());
		if (symbol == null) {
			throw new SlotConfigReferenceException("Symbol '" + data.symbol() + "' not found for paytable entry.");
		}
		return symbol;
	}

	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append(symbol.name()).append(": ");

		tiers.forEach(t -> sb.append("[").append(t.debug()).append("] "));

		return sb.toString().trim();
	}
}
