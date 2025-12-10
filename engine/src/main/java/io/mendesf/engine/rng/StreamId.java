package io.mendesf.engine.rng;

/**
 * Converte nomes de streams (Strings) em IDs de 64 bits estáveis para serem
 * usados como "sequence" no PCG32.
 *
 * Usa FNV-1a 64-bit, que é simples, rápido e portável.
 */
public final class StreamId {

	private static final long FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
	private static final long FNV_PRIME = 0x100000001b3L;

	private StreamId() {
	}

	public static long of(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Stream name cannot be null");
		}
		long hash = FNV_OFFSET_BASIS;
		for (int i = 0; i < name.length(); i++) {
			hash ^= name.charAt(i);
			hash *= FNV_PRIME;
		}
		return hash;
	}
}
