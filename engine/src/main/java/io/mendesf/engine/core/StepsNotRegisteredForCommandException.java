package io.mendesf.engine.core;

public class StepsNotRegisteredForCommandException extends PipelineConfigurationException {

	private final Command command;

	public StepsNotRegisteredForCommandException(Command command) {
		super("No steps registered for command: " + command);
		this.command = command;
	}

	public Command command() {
		return command;
	}
}
