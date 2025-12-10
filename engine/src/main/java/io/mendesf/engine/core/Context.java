package io.mendesf.engine.core;

public record Context<I, O>(Input<I> input, Output<O> output) {
	public Context<I, O> withOutput(Output<O> newOutput) {
		return new Context<>(this.input, newOutput);
	}
}