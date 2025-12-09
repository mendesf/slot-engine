package io.mendesf.engine.rng;

/**
 * PCG32 – 64-bit state, 32-bit output (XSH-RR variant).
 *
 * - Baseado na família PCG, por Melissa O'Neill. - Usa um LCG de 64 bits
 * internamente, com um output function (permutation) para produzir inteiros de
 * 32 bits com alta qualidade.
 *
 * Recomendado para: - Engines de jogo - Simulações de slot - Qualquer uso que
 * precise de RNG rápido, determinístico e reprodutível.
 */
public final class PCG32 implements RNG {

	/**
	 * Estado interno de 64 bits do LCG.
	 */
	private long state;

	/**
	 * Incremento ímpar do LCG. Cada valor diferente produz um "stream" independente
	 * de números.
	 */
	private final long increment;

	/**
	 * Cria uma nova instância de PCG32.
	 *
	 * @param seed
	 *            semente inicial (define a sequência gerada)
	 * @param sequence
	 *            identifica o "stream" (cada sequence gera um stream independente)
	 */
	public PCG32(long seed, long sequence) {
		// O increment precisa ser ímpar para garantir bom período.
		// (sequence << 1) | 1 => sempre resulta em número ímpar.
		this.increment = (sequence << 1) | 1;

		// Inicializa o estado de forma recomendada pelo PCG:
		// 1) Começa em 0
		// 2) Avança uma vez
		// 3) Soma o seed
		// 4) Avança mais uma vez
		// Isso evita correlações ruins em seeds semelhantes.
		this.state = 0;
		advance();
		this.state += seed;
		advance();
	}

	/**
	 * Passo do LCG: avança o estado interno.
	 *
	 * state = state * a + c (mod 2^64)
	 *
	 * Onde: - a = 6364136223846793005L É um multiplicador conhecido, com boas
	 * propriedades para LCG 64-bit. - c = increment (ímpar)
	 */
	private void advance() {
		state = state * 6364136223846793005L + increment;
		// Overflow em long é desejado aqui; ele implementa o "mod 2^64".
	}

	/**
	 * Retorna um inteiro de 32 bits pseudo-aleatório.
	 */
	@Override
	public int nextInt() {
		// Guarda o estado atual para usar no output function.
		long oldState = state;

		// Avança o estado para a próxima chamada.
		advance();

		// XSH-RR output:
		// 1) XOR-shift: mistura bits altos e baixos.
		// ((oldState >>> 18) ^ oldState)
		// 2) Shift adicional de 27 bits à direita: seleciona 32 bits.
		int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);

		// Usa os 5 bits mais altos do estado como quantidade de rotação (0..31).
		int rot = (int) (oldState >>> 59);

		// Rotaciona à direita os 32 bits obtidos.
		// Isso é uma permutação não-linear que melhora a distribuição.
		return Integer.rotateRight(xorShifted, rot);
	}

	/**
	 * Retorna um inteiro pseudo-aleatório no intervalo [0, bound), sem viés, usando
	 * rejection sampling.
	 */
	@Override
	public int nextInt(int bound) {
		if (bound <= 0) {
			throw new IllegalArgumentException("bound must be positive");
		}

		// threshold é o menor valor tal que (2^32 - threshold) seja múltiplo de bound.
		// Valores abaixo de threshold são rejeitados para evitar viés.
		int threshold = Integer.remainderUnsigned(-bound, bound);

		int r;
		do {
			r = nextInt();
		} while (Integer.compareUnsigned(r, threshold) < 0);

		// Mapeia uniformemente o intervalo válido para [0, bound).
		return Integer.remainderUnsigned(r, bound);
	}

	/**
	 * Retorna um long pseudo-aleatório de 64 bits. Construído combinando dois
	 * nextInt() de 32 bits.
	 */
	@Override
	public long nextLong() {
		long high = ((long) nextInt()) << 32;
		long low = Integer.toUnsignedLong(nextInt());
		return high | low;
	}

	/**
	 * Retorna um double uniforme em [0.0, 1.0).
	 *
	 * Utiliza 53 bits de precisão, compatível com o formato IEEE 754.
	 */
	@Override
	public double nextDouble() {
		// Pega 53 bits dos mais altos de um long aleatório.
		long bits = nextLong() >>> 11; // 64 - 53 = 11

		// Divide por 2^53 para obter um double em [0, 1).
		return bits * (1.0 / (1L << 53));
	}
}
