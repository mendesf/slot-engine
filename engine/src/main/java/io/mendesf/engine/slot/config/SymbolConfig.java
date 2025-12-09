package io.mendesf.engine.slot.config;

public record SymbolConfig(
    String name,
    int flags
) {

    public SymbolConfig(SymbolConfigData data) {
        this(
            data.name(),
            computeFlags(data)
        );
    }

    private static int computeFlags(SymbolConfigData data) {
        int computed = 0;
        if (data.flags() != null) {
            for (String flagName : data.flags()) {
                SymbolFlag flag = SymbolFlag.fromName(flagName);
                if (flag != null) {
                    computed |= flag.mask();
                }
            }
        }
        return computed;
    }

    public boolean has(SymbolFlag flag) {
        return (flags & flag.mask()) != 0;
    }

    public boolean isWild() {
        return has(SymbolFlag.WILD);
    }

    public boolean isSubstitutable() {
        return has(SymbolFlag.SUBSTITUTABLE);
    }

    public boolean isBlocker() {
        return has(SymbolFlag.BLOCKER);
    }

	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" [flags: ");
	
		boolean first = true;
		for (SymbolFlag flag : SymbolFlag.values()) {
			if (has(flag) && flag != SymbolFlag.NONE) {
				if (!first) sb.append(", ");
				sb.append(flag.name());
				first = false;
			}
		}
	
		if (first) sb.append("NONE");
		sb.append("]");
	
		return sb.toString();
	}
}
