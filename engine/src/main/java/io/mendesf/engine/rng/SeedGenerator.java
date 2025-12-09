package io.mendesf.engine.rng;

/**
 * Fonte de seeds para a engine.
 *
 * Em produção, normalmente usa SecureRandom. Em testes/simulações, pode usar
 * seeds fixas ou sequenciais.
 */
public interface SeedGenerator {

	/**
	 * Gera o próximo seed de 64 bits para um spin.
	 */
	long nextSeed();
}
