package io.mendesf.engine.slot.config;

import java.util.List;

public record PayTableConfigData(String name, String type, List<PayTableEntryConfigData> entries) {
}
