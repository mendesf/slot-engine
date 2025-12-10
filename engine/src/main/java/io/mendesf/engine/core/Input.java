package io.mendesf.engine.core;

public record Input<T>(Command command, T payload) {
}
