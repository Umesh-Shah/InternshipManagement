package ca.uwindsor.ims.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record AppProperties(
        int tokenExpiryHours,
        long slowRequestThresholdMs
) {}
