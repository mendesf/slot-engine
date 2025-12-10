package io.mendesf.games.clusterdemo;

import io.mendesf.engine.core.PipelineBuilder;

public class ClusterDemo {
	public static void main(String[] args) {
		var game = new ClusterGame();
		game.configure(new PipelineBuilder());
	}
}
