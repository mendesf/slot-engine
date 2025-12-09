package io.mendesf.engine.core;

import java.util.*;

public class PipelineBuilder {

	private final Set<Command> commands = new HashSet<>();
	private final Map<Command, List<StepRule>> stepRules = new HashMap<>();
	private final Map<Command, Map<Step, Handler<?, ?>>> handlers = new HashMap<>();

	public PipelineBuilder registerGame(Game game) {
		game.configure(this);
		return this;
	}

	public PipelineBuilder registerPlugin(Command command, Plugin plugin) {
		plugin.registerSteps(command, this);
		plugin.registerHandlers(command, this);
		return this;
	}

	public PipelineBuilder registerCommand(Command command) {
		commands.add(command);
		return this;
	}

	public PipelineBuilder registerStep(Command command, Step step) {
		return this.registerStep(command, step, null, 0);
	}

	public PipelineBuilder registerStep(Command command, Step step, Step dependsOn, int priority) {
		if (!commands.contains(command)) {
			throw new CommandNotRegisteredException(command);
		}

		var rules = stepRules.get(command);
		if (rules != null) {
			for (var existingRule : rules) {
				if (Objects.equals(existingRule.dependsOn(), dependsOn) && existingRule.priority() == priority) {

					throw new AmbiguousStepOrderingException(command, existingRule.step(), step, dependsOn, priority);
				}
			}
		}

		stepRules.computeIfAbsent(command, k -> new ArrayList<>()).add(new StepRule(step, dependsOn, priority));
		return this;
	}

	public PipelineBuilder registerHandler(Command command, Step step, Handler<?, ?> handler) {
		if (!commands.contains(command)) {
			throw new CommandNotRegisteredException(command);
		}
		if (!stepRules.containsKey(command)) {
			throw new StepsNotRegisteredForCommandException(command);
		}

		handlers.computeIfAbsent(command, k -> new HashMap<>()).put(step, handler);

		return this;
	}

	public Pipeline build() {
		Map<Command, List<Step>> orderedSteps = new HashMap<>();
		for (Command command : stepRules.keySet()) {
			var steps = StepOrderResolver.resolve(stepRules.get(command));

			var handlersForCommand = handlers.getOrDefault(command, Map.of());
			for (Step step : steps) {
				if (!handlersForCommand.containsKey(step)) {
					throw new HandlerNotRegisteredForStepException(command, step);
				}
			}

			orderedSteps.put(command, steps);
		}

		return new Pipeline(orderedSteps, handlers);
	}
}
