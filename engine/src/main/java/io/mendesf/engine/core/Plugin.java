package io.mendesf.engine.core;

public interface Plugin {
	void registerSteps(Command command, PipelineBuilder builder);

	void registerHandlers(Command command, PipelineBuilder builder);
}
