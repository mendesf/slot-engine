package io.mendesf.engine.rng;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SeedGenerator determinístico, útil para testes e simulações.
 *
 * Começa em um valor inicial e vai incrementando a cada nextSeed().
 */
public final class SequentialSeedGenerator implements SeedGenerator {

	private final AtomicLong counter;

	public SequentialSeedGenerator(long initialSeed) {
		this.counter = new AtomicLong(initialSeed);
	}

	@Override
	public long nextSeed() {
		return counter.getAndIncrement();
	}
}
