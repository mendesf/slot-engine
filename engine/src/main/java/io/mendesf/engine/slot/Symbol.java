package io.mendesf.engine.slot;

public final class Symbol {

	private final String name;
	private final int flags;

	public Symbol(String name, int flags) {
		this.name = name;
		this.flags = flags;
	}

	public String name() {
		return name;
	}

	public int flags() {
		return flags;
	}

	public boolean has(SymbolFlag f) {
		return (flags & f.mask()) != 0;
	}

	public boolean isWild() {
		return has(SymbolFlag.WILD);
	}

	public boolean isSubstitutable() {
		return has(SymbolFlag.SUBSTITUTABLE);
	}

	public boolean isScatter() {
		return has(SymbolFlag.SCATTER);
	}
}
