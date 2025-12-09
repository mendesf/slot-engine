package io.mendesf.engine.rng;

import java.security.SecureRandom;

/**
 * SeedGenerator baseado em SecureRandom.
 *
 * Ideal para produção quando você precisa de seeds imprevisíveis para cada spin
 * ou sessão.
 */
public final class SecureSeedGenerator implements SeedGenerator {

	private final SecureRandom random;

	public SecureSeedGenerator() {
		this(new SecureRandom());
	}

	public SecureSeedGenerator(SecureRandom random) {
		this.random = random;
	}

	@Override
	public long nextSeed() {
		return random.nextLong();
	}
}
