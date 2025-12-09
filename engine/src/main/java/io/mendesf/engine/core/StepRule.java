package io.mendesf.engine.core;

import org.jetbrains.annotations.Nullable;

public record StepRule(Step step, @Nullable Step dependsOn, int priority) {
}