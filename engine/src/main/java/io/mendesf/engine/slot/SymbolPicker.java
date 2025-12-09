package io.mendesf.engine.slot;

import java.util.List;

import io.mendesf.engine.rng.RNG;
import io.mendesf.engine.slot.config.ReelConfig;
import io.mendesf.engine.slot.config.SymbolConfig;

public final class SymbolPicker {

	public static SymbolConfig pickSymbol(ReelConfig reel, RNG rng) {

		List<Long> weights = reel.weights();
		List<SymbolConfig> symbols = reel.symbols();

		long total = 0;
		for (long w : weights) {
			total += w;
		}

		// valor no intervalo [0, total)
		long value = Long.remainderUnsigned(rng.nextLong(), total);

		long cumulative = 0;
		for (int i = 0; i < weights.size(); i++) {
			cumulative += weights.get(i);
			if (value < cumulative) {
				return symbols.get(i);
			}
		}

		// Isso nunca deveria acontecer
		throw new IllegalStateException("RNG mapping failed for reel: " + reel.name());
	}
}
