package io.mendesf.engine.core;

import java.util.*;

public final class StepOrderResolver {

	private StepOrderResolver() {
	}

	public static List<Step> resolve(List<StepRule> rules) {
		if (rules == null || rules.isEmpty()) {
			return List.of();
		}

		StepGraph graph = new StepGraph();
		for (StepRule rule : rules) {
			graph.addRule(rule);
		}

		return topologicalSort(graph, rules);
	}

	private static List<Step> topologicalSort(StepGraph graph, List<StepRule> rules) {
		PriorityQueue<Step> available = new PriorityQueue<>(Comparator.comparingInt(graph::priorityOf));

		for (Step step : graph.steps()) {
			if (graph.indegreeOf(step) == 0) {
				available.add(step);
			}
		}

		List<Step> result = new ArrayList<>();

		while (!available.isEmpty()) {
			Step current = available.poll();
			result.add(current);

			for (Step next : graph.dependentsOf(current)) {
				int indegree = graph.decrementIndegree(next);
				if (indegree == 0) {
					available.add(next);
				}
			}
		}

		if (result.size() != graph.size()) {
			throw new StepOrderException("Cycle detected or inconsistent step rules", rules);
		}

		return List.copyOf(result);
	}
}
