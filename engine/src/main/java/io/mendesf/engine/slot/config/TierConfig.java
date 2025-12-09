package io.mendesf.engine.slot.config;

public record TierConfig(int min, int max, int payout) {
	public TierConfig(PayTableTierConfigData data) {
		this(data.min() != null ? data.min() : 0, data.max() != null ? data.max() : Integer.MAX_VALUE, data.payout());
	}

	public boolean matches(int count) {
		return count >= min && count <= max;
	}

	public String debug() {
		if (max == Integer.MAX_VALUE) {
			return "min=" + min + " → payout=" + payout;
		}
		return "min=" + min + ", max=" + max + " → payout=" + payout;
	}

}
