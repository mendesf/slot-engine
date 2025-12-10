package io.mendesf.engine.slot;

import io.mendesf.engine.core.Command;
import io.mendesf.engine.core.Context;
import io.mendesf.engine.core.Output;
import io.mendesf.engine.core.PipelineBuilder;
import io.mendesf.engine.core.Plugin;
import io.mendesf.engine.rng.PCG32;
import io.mendesf.engine.rng.RNG;
import io.mendesf.engine.rng.SeedGenerator;
import io.mendesf.engine.rng.StreamId;
import io.mendesf.engine.slot.config.EngineConfig;

public class SpinPlugin implements Plugin {
	private final EngineConfig config;
	private final SeedGenerator seedGenerator;

	public SpinPlugin(EngineConfig config, SeedGenerator seedGenerator) {
		this.config = config;
		this.seedGenerator = seedGenerator;
	}

	@Override
	public void registerSteps(Command command, PipelineBuilder builder) {
		for (var rule : CommonSteps.DEFAULT_ORDER) {
			builder.registerStep(command, rule.step(), rule.dependsOn(), rule.priority());
		}
	}

	@Override
	public void registerHandlers(Command command, PipelineBuilder builder) {
		builder.registerHandler(command, CommonSteps.VALIDATE_INPUT, this::validateBet)
				.registerHandler(command, CommonSteps.GENERATE_OUTCOME, this::generateOutcome)
				.registerHandler(command, CommonSteps.CALCULATE_PAYOUT, this::calculatePayout)
				.registerHandler(command, CommonSteps.BUILD_OUTPUT, this::buildOutput)
				.registerHandler(command, CommonSteps.COMPLETE, this::complete);
	}

	private Context<SpinInput, SpinOutput> validateBet(Context<SpinInput, SpinOutput> ctx) {
		System.out.println("  [Spin] Validating bet...");
		// Validate bet is within European roulette limits (0-36, red/black, etc.)
		return ctx;
	}

	private Context<SpinInput, SpinOutput> generateOutcome(Context<SpinInput, SpinOutput> ctx) {
		System.out.println("  [Spin] Generating outcome...");
		long seed = seedGenerator.nextSeed();
		long seq = StreamId.of("spin.reel");
		RNG rng = new PCG32(seed, seq);

		var display = config.displays().stream().filter(d -> d.name().equals("BaseGame")).findFirst().orElseThrow();

		var grid = DisplayGenerator.generate(display, rng);
		var output = new Output<>(new SpinOutput(grid.symbols()));
		// aqui depois você coloca cluster finder, avalanche, etc.
		return ctx.withOutput(output);
	}

	private Context<SpinInput, SpinOutput> calculatePayout(Context<SpinInput, SpinOutput> ctx) {
		System.out.println("  [Spin] Calculating payout...");

		return ctx;
	}

	private Context<SpinInput, SpinOutput> buildOutput(Context<SpinInput, SpinOutput> ctx) {
		System.out.println("  [Spin] Building output...");
		// Output is already built, just pass through
		return ctx;
	}

	private Context<SpinInput, SpinOutput> complete(Context<SpinInput, SpinOutput> ctx) {
		System.out.println("  [Spin] Complete!");
		return ctx;
	}
}
