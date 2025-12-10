package io.mendesf.engine.slot;

import java.util.List;

import io.mendesf.engine.core.Step;
import io.mendesf.engine.core.StepRule;

/**
 * Common processing steps for casino games. These steps represent a standard
 * flow that works for most games: slots, roulette, blackjack, poker, etc.
 * 
 * Games can use these as-is or add custom steps for specific mechanics.
 */
public final class CommonSteps {

	/**
	 * Validate the input (bet, wager, game parameters). Examples: check bet limits,
	 * validate player balance, verify game rules.
	 */
	public static final Step VALIDATE_INPUT = new Step("validate_input");

	/**
	 * Generate the game outcome (spin result, card deal, dice roll, etc.). This is
	 * where the core game logic happens.
	 */
	public static final Step GENERATE_OUTCOME = new Step("generate_outcome");

	/**
	 * Calculate the payout based on the outcome and bet. Determines wins, losses,
	 * multipliers, bonuses, etc.
	 */
	public static final Step CALCULATE_PAYOUT = new Step("calculate_payout");

	/**
	 * Build the final output/response. Formats the result for the client/API.
	 */
	public static final Step BUILD_OUTPUT = new Step("build_output");

	/**
	 * Complete the game round. Final cleanup, logging, statistics, etc.
	 */
	public static final Step COMPLETE = new Step("complete");

	/**
	 * Default execution order for most casino games. Games can use this directly or
	 * customize it.
	 */
	public static final List<StepRule> DEFAULT_ORDER = List.copyOf(List.of(new StepRule(VALIDATE_INPUT, null, 0),
			new StepRule(GENERATE_OUTCOME, VALIDATE_INPUT, 0), new StepRule(CALCULATE_PAYOUT, GENERATE_OUTCOME, 0),
			new StepRule(BUILD_OUTPUT, CALCULATE_PAYOUT, 0), new StepRule(COMPLETE, BUILD_OUTPUT, 0)));

	private CommonSteps() {
		// Utility class, no instances
	}
}
