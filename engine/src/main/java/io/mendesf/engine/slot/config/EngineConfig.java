package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.mendesf.engine.core.SlotConfigReferenceException;

public record EngineConfig(
    BetConfig bet,
    List<SymbolConfig> symbols,
    List<ReelConfig> reels,
    List<DisplayConfig> displays,
    List<PayTableConfig> payTables,
    List<StageConfig> stages
) {

    public static EngineConfig from(EngineConfigData data) {
        // 1) Bet
        if (data.bet() == null) {
            throw new SlotConfigReferenceException("Missing 'bet' configuration.");
        }
        BetConfig bet = new BetConfig(data.bet());

        // 2) Symbols
        List<SymbolConfig> symbols = data.symbols().stream()
                .map(SymbolConfig::new)
                .toList();

        Map<String, SymbolConfig> symbolByName = symbols.stream()
                .collect(Collectors.toMap(SymbolConfig::name, Function.identity()));

        // 3) Reels
        List<ReelConfig> reels = data.reels().stream()
                .map(r -> new ReelConfig(r, symbolByName))
                .toList();

        Map<String, ReelConfig> reelByName = reels.stream()
                .collect(Collectors.toMap(ReelConfig::name, Function.identity()));

        // 4) Displays
        List<DisplayConfig> displays = data.displays().stream()
                .map(d -> new DisplayConfig(d, reelByName))
                .toList();

        Map<String, DisplayConfig> displayByName = displays.stream()
                .collect(Collectors.toMap(DisplayConfig::name, Function.identity()));

        // 5) PayTables
        List<PayTableConfig> payTables = data.payTables().stream()
                .map(pt -> new PayTableConfig(pt, symbolByName))
                .toList();

        Map<String, PayTableConfig> payTableByName = payTables.stream()
                .collect(Collectors.toMap(PayTableConfig::name, Function.identity()));

        // 6) Stages
        List<StageConfig> stages = data.stages().stream()
                .map(s -> new StageConfig(s, displayByName, payTableByName))
                .toList();

        return new EngineConfig(
                bet,
                List.copyOf(symbols),
                List.copyOf(reels),
                List.copyOf(displays),
                List.copyOf(payTables),
                List.copyOf(stages)
        );
    }

	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("=== ENGINE CONFIG ===\n");
	
		sb.append("Bet:\n");
		sb.append("  baseBet = ").append(bet.baseBet()).append("\n\n");
	
		sb.append("Symbols (").append(symbols.size()).append("):\n");
		symbols.forEach(s -> sb.append("  • ").append(s.debug()).append("\n"));
		sb.append("\n");
	
		sb.append("Reels (").append(reels.size()).append("):\n");
		reels.forEach(r -> sb.append("  • ").append(r.debug()).append("\n"));
		sb.append("\n");
	
		sb.append("Displays (").append(displays.size()).append("):\n");
		displays.forEach(d -> sb.append("  • ").append(d.debug()).append("\n"));
		sb.append("\n");
	
		sb.append("PayTables (").append(payTables.size()).append("):\n");
		payTables.forEach(pt -> sb.append("  • ").append(pt.debug()).append("\n"));
		sb.append("\n");
	
		sb.append("Stages (").append(stages.size()).append("):\n");
		stages.forEach(st -> sb.append("  • ").append(st.debug()).append("\n"));
	
		sb.append("=====================\n");
	
		return sb.toString();
	}
	
}
