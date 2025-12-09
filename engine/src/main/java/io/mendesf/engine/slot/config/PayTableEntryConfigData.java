package io.mendesf.engine.slot.config;

import java.util.List;

public record PayTableEntryConfigData(
    String symbol,
    List<PayTableTierConfigData> tiers
) {}
