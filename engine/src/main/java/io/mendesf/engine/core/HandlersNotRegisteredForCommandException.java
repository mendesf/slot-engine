package io.mendesf.engine.core;

public class HandlersNotRegisteredForCommandException extends PipelineConfigurationException {

	private final Command command;

	public HandlersNotRegisteredForCommandException(Command command) {
		super("No handlers registered for command: " + command);
		this.command = command;
	}

	public Command command() {
		return command;
	}
}
