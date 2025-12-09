package io.mendesf.engine.slot;

import io.mendesf.engine.slot.config.DisplayConfig;
import io.mendesf.engine.slot.config.SymbolConfig;

public record DisplayGrid(DisplayConfig config, SymbolConfig[][] symbols) {
}
