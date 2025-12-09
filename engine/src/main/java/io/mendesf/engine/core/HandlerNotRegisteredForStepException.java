package io.mendesf.engine.core;

public class HandlerNotRegisteredForStepException extends PipelineConfigurationException {

	private final Command command;
	private final Step step;

	public HandlerNotRegisteredForStepException(Command command, Step step) {
		super("No handler registered for command: " + command + ", step: " + step);
		this.command = command;
		this.step = step;
	}

	public Command command() {
		return command;
	}

	public Step step() {
		return step;
	}
}
