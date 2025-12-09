package io.mendesf.engine.slot.config;

import java.util.List;
import java.util.Map;

import io.mendesf.engine.core.SlotConfigReferenceException;

public record StageConfig(
    String name,
    List<DisplayConfig> displays,
    String selectionMode,
    PayTableConfig payTable
) {

    public StageConfig(
        StageConfigData data,
        Map<String, DisplayConfig> displaysByName,
        Map<String, PayTableConfig> payTablesByName
    ) {
        this(
            data.name(),
            resolveDisplays(data, displaysByName),
            data.selectionMode(),
            resolvePayTable(data, payTablesByName)
        );
    }

    private static List<DisplayConfig> resolveDisplays(
        StageConfigData data,
        Map<String, DisplayConfig> displaysByName
    ) {
        return data.displays().stream()
            .map(displayName -> {
                DisplayConfig display = displaysByName.get(displayName);
                if (display == null) {
                    throw new SlotConfigReferenceException(
                        "Display '" + displayName + "' not found for stage '" + data.name() + "'."
                    );
                }
                return display;
            })
            .toList();
    }

    private static PayTableConfig resolvePayTable(
        StageConfigData data,
        Map<String, PayTableConfig> payTablesByName
    ) {
        PayTableConfig payTable = payTablesByName.get(data.payTable());
        if (payTable == null) {
            throw new SlotConfigReferenceException(
                "Pay table '" + data.payTable() + "' not found for stage '" + data.name() + "'."
            );
        }
        return payTable;
    }

    public String debug() {
        return "%s [mode=%s, displays=%s, payTable=%s]"
            .formatted(
                name,
                selectionMode,
                displays.stream().map(DisplayConfig::name).toList(),
                payTable.name()
            );
    }
    
}
