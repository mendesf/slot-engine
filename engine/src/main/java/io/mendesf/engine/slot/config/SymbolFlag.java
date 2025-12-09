package io.mendesf.engine.slot.config;

public enum SymbolFlag {
	NONE(0), // 0000
	WILD(1 << 0), // 0001
	SUBSTITUTABLE(1 << 1), // 0010
	BLOCKER(1 << 2), // 0100
	SCATTER(1 << 3); // 1000

	private final int mask;

	SymbolFlag(int mask) {
		this.mask = mask;
	}

	public int mask() {
		return mask;
	}

	public static SymbolFlag fromName(String name) {
		for (SymbolFlag flag : values()) {
			if (flag.name().equalsIgnoreCase(name)) {
				return flag;
			}
		}
		return null;
	}
}
