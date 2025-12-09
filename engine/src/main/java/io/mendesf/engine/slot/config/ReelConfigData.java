package io.mendesf.engine.slot.config;

import java.util.List;

record ReelConfigData(
    String name,
    ReelDistributionType distributionType,
    List<String> symbols,
    List<Long> weights
) {}
