package io.mendesf.engine.core;

@FunctionalInterface
public interface Handler<I, O> {
	Context<I, O> handle(Context<I, O> context) throws Exception;
}