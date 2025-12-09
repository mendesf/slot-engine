package io.mendesf.engine.rng;

/**
 * RngStream
 *
 * Streams lógicos de aleatoriedade usados pela engine. Cada stream recebe um
 * "sequence id" fixo que é passado para o PCG32, gerando fluxos independentes
 * de números.
 *
 * Mantenha este enum pequeno e em nível de conceito: - REEL => tudo
 * relacionado a reels / símbolos / avalanches - GAMBLE => double-or-nothing,
 * coin flip, etc. - BONUS => triggers de bônus (se precisar futuramente) -
 * FEATURE => decisões internas de features (opcional futuro)
 */
public enum StreamId {

	REEL(1L), GAMBLE(2L);
	// Se quiser no futuro:
	// BONUS(3L),
	// FEATURE(4L),
	// UI(5L);

	private final long sequenceId;

	StreamId(long sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * Sequence id passado para o PCG32. Cada valor diferente gera um stream
	 * independente de RNG.
	 */
	public long sequenceId() {
		return sequenceId;
	}
}
