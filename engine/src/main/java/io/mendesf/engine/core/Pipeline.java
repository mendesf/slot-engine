package io.mendesf.engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pipeline {

	private final Map<Command, List<Step>> steps;
	private final Map<Command, Map<Step, Handler<?, ?>>> handlers;

	public Pipeline(Map<Command, List<Step>> steps, Map<Command, Map<Step, Handler<?, ?>>> handlers) {

		Map<Command, List<Step>> stepsCopy = new HashMap<>();
		for (var entry : steps.entrySet()) {
			stepsCopy.put(entry.getKey(), List.copyOf(entry.getValue()));
		}
		this.steps = Map.copyOf(stepsCopy);

		Map<Command, Map<Step, Handler<?, ?>>> handlersCopy = new HashMap<>();
		for (var entry : handlers.entrySet()) {
			handlersCopy.put(entry.getKey(), Map.copyOf(entry.getValue()));
		}
		this.handlers = Map.copyOf(handlersCopy);
	}

	public <I, O> Context<I, O> execute(Context<I, O> context) throws Exception {
		Command command = context.input().command();
		List<Step> stepsForCommand = steps.get(command);
		if (stepsForCommand == null || stepsForCommand.isEmpty()) {
			throw new StepsNotRegisteredForCommandException(command);
		}

		for (Step step : stepsForCommand) {
			context = executeStep(context, command, step);
		}

		return context;
	}

	@SuppressWarnings("unchecked")
	private <I, O> Context<I, O> executeStep(Context<I, O> context, Command command, Step step) throws Exception {
		Map<Step, Handler<?, ?>> commandHandlers = handlers.get(command);
		if (commandHandlers == null || commandHandlers.isEmpty()) {
			throw new HandlersNotRegisteredForCommandException(command);
		}

		Handler<I, O> handler = (Handler<I, O>) commandHandlers.get(step);
		if (handler == null) {
			throw new HandlerNotRegisteredForStepException(command, step);
		}

		return handler.handle(context);
	}
}
