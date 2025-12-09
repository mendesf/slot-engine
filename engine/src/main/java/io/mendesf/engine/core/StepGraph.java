package io.mendesf.engine.core;

import java.util.*;

final class StepGraph {

	private final Map<Step, Integer> indegrees = new HashMap<>();
	private final Map<Step, List<Step>> adjacency = new HashMap<>();
	private final Map<Step, Integer> priorities = new HashMap<>();

	void addRule(StepRule rule) {
		Step step = rule.step();
		Step dependsOn = rule.dependsOn();
		int priority = rule.priority();

		indegrees.putIfAbsent(step, 0);
		priorities.put(step, priority);

		if (dependsOn != null) {
			indegrees.putIfAbsent(dependsOn, 0);

			adjacency.computeIfAbsent(dependsOn, k -> new ArrayList<>()).add(step);

			indegrees.put(step, indegrees.get(step) + 1);
		}
	}

	Set<Step> steps() {
		return indegrees.keySet();
	}

	int indegreeOf(Step step) {
		return indegrees.getOrDefault(step, 0);
	}

	int decrementIndegree(Step step) {
		int current = indegrees.getOrDefault(step, 0);
		int updated = current - 1;
		indegrees.put(step, updated);
		return updated;
	}

	List<Step> dependentsOf(Step step) {
		return adjacency.getOrDefault(step, List.of());
	}

	int priorityOf(Step step) {
		return priorities.getOrDefault(step, 0);
	}

	int size() {
		return indegrees.size();
	}
}
