package io.mendesf.games.clusterdemo;

import io.mendesf.engine.core.Command;
import io.mendesf.engine.core.Context;
import io.mendesf.engine.core.Game;
import io.mendesf.engine.core.Input;
import io.mendesf.engine.core.PipelineBuilder;
import io.mendesf.engine.core.Wager;
import io.mendesf.engine.rng.SecureSeedGenerator;
import io.mendesf.engine.rng.SeedGenerator;
import io.mendesf.engine.slot.SpinInput;
import io.mendesf.engine.slot.SpinOutput;
import io.mendesf.engine.slot.SpinPlugin;
import io.mendesf.engine.slot.config.EngineConfig;
import io.mendesf.engine.slot.config.EngineConfigLoader;

public class ClusterGame implements Game {

	@Override
	public void configure(PipelineBuilder builder) {
		EngineConfig config = EngineConfigLoader.fromJson("engine.json");
		SeedGenerator seedGenerator = new SecureSeedGenerator();
		SpinPlugin spinPlugin = new SpinPlugin(config, seedGenerator);
		var command = new Command("spin");
		var pipeline = builder.registerCommand(command).registerPlugin(command, spinPlugin).build();

		var input = new Input<SpinInput>(command, new SpinInput(new Wager(100)));

		var context = new Context<SpinInput, SpinOutput>(input, null);

		try {
			var output = pipeline.execute(context);
			for (var column : output.output().payload().symbols()) {
				for (var symbol : column) {
					System.out.print(symbol.name() + " ");
				}
				System.out.println();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
