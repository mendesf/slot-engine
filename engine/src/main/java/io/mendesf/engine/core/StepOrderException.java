package io.mendesf.engine.core;

import java.util.List;

public class StepOrderException extends EngineConfigurationException {

	private final List<StepRule> rules;

	public StepOrderException(String message, List<StepRule> rules) {
		super(message);
		this.rules = List.copyOf(rules);
	}

	public List<StepRule> rules() {
		return rules;
	}
}
