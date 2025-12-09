package io.mendesf.engine.slot;

import java.util.List;

import io.mendesf.engine.rng.RNG;
import io.mendesf.engine.slot.config.DisplayConfig;
import io.mendesf.engine.slot.config.ReelConfig;
import io.mendesf.engine.slot.config.SymbolConfig;

// runtime
public final class DisplayGenerator {

	private DisplayGenerator() {
	}

	public static DisplayGrid generate(DisplayConfig display, RNG rng) {
		int rows = display.rows();
		int cols = display.columns();

		SymbolConfig[][] grid = new SymbolConfig[rows][cols];

		List<ReelConfig> reels = display.reels();

		for (int col = 0; col < cols; col++) {
			ReelConfig reel = reels.get(col);
			for (int row = 0; row < rows; row++) {
				grid[row][col] = SymbolPicker.pickSymbol(reel, rng);
			}
		}

		return new DisplayGrid(display, grid);
	}
}
