package io.mendesf.engine.slot.config;

public record BetConfig(int baseBet) {
	public BetConfig(BetConfigData data) {
		this(data.baseBet());
	}
}
