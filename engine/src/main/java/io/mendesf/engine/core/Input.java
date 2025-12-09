package io.mendesf.engine.core;

public record Input<T>(Command command, Wager wager, T payload) {
}
