package io.mendesf.engine.rng;

/**
 * RNG (Pseudo-Random Number Generator)
 *
 * Interface mínima e estável para geração de números pseudoaleatórios. A engine
 * deve depender desta interface ― nunca das implementações concretas.
 *
 * Implementações típicas: - PCG32 (determinístico, rápido, certificado) -
 * Xoshiro256** (período maior, muito rápido) - FixedRNG (para testes) -
 * SecureRNG (se for necessário gerar seeds criptográficas)
 */
public interface RNG {

	/**
	 * Retorna um inteiro de 32 bits pseudoaleatório. Faixa completa: 0 <= x <=
	 * 2^32-1 (interpretado como signed em Java).
	 */
	int nextInt();

	/**
	 * Retorna um inteiro pseudoaleatório no intervalo [0, bound). Não deve produzir
	 * viés estatístico.
	 *
	 * @param bound
	 *            limite superior exclusivo
	 */
	int nextInt(int bound);

	/**
	 * Retorna um long de 64 bits pseudoaleatório.
	 */
	long nextLong();

	/**
	 * Retorna um double em [0.0, 1.0). Deve usar 53 bits de precisão (mantissa IEEE
	 * 754).
	 */
	double nextDouble();
}
