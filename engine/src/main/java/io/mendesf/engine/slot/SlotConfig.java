package io.mendesf.engine.slot;

import java.util.List;

public class SlotConfig {
	private List<SymbolData> symbols;

	public SlotConfig(List<SymbolData> symbols) {
		this.symbols = symbols;
	}

	public List<SymbolData> getSymbols() {
		return symbols;
	}

}
