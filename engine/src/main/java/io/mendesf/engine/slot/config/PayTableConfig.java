package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;

public record PayTableConfig(
    String name,
    String type,
    List<PayTableEntryConfig> entries
) {
    public PayTableConfig(
        PayTableConfigData data,
        Map<String, SymbolConfig> symbolsByName
    ) {
        this(
            data.name(),
            data.type(),
            data.entries().stream()
                .map(e -> new PayTableEntryConfig(e, symbolsByName))
                .toList()
        );
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
          .append(" (type=")
          .append(type)
          .append(")\n");
    
        entries.forEach(e -> sb.append("      ").append(e.debug()).append("\n"));
        return sb.toString();
    }
    
}
