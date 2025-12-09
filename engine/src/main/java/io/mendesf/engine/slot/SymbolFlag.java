package io.mendesf.engine.slot;

public enum SymbolFlag {
	NONE(0), WILD(1 << 0), // 0001
	SUBSTITUTABLE(1 << 1), // 0010
	SCATTER(1 << 2); // 0100

	private final int mask;

	SymbolFlag(int mask) {
		this.mask = mask;
	}

	public int mask() {
		return mask;
	}
}
