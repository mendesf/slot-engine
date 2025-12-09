package io.mendesf.engine.core;

public class CommandNotRegisteredException extends PipelineConfigurationException {

	private final Command command;

	public CommandNotRegisteredException(Command command) {
		super("Command not registered: " + command);
		this.command = command;
	}

	public Command command() {
		return command;
	}
}
