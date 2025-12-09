package io.mendesf.engine.core;

public class AmbiguousStepOrderingException extends PipelineConfigurationException {

	private final Command command;
	private final Step existingStep;
	private final Step conflictingStep;
	private final Step dependsOn;
	private final int priority;

	public AmbiguousStepOrderingException(Command command, Step existingStep, Step conflictingStep, Step dependsOn,
			int priority) {

		super("Ambiguous step ordering for command %s: steps %s and %s both depend on %s with priority %d"
				.formatted(command, existingStep, conflictingStep, dependsOn, priority));

		this.command = command;
		this.existingStep = existingStep;
		this.conflictingStep = conflictingStep;
		this.dependsOn = dependsOn;
		this.priority = priority;
	}

	public Command command() {
		return command;
	}

	public Step existingStep() {
		return existingStep;
	}

	public Step conflictingStep() {
		return conflictingStep;
	}

	public Step dependsOn() {
		return dependsOn;
	}

	public int priority() {
		return priority;
	}
}
