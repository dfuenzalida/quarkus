package io.quarkus.micrometer.runtime.binder.vertx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.quarkus.micrometer.runtime.config.runtime.VertxConfig;

public class VertxHttpServerMetricsTest {

    @Test
    public void testHttpServerMetricsIgnorePatterns() {
        VertxConfig runtimeConfig = new VertxConfig();
        runtimeConfig.ignorePatterns = Optional.of(new ArrayList<>(Arrays.asList("/item/.*")));
        VertxHttpServerMetrics metrics = new VertxHttpServerMetrics(new SimpleMeterRegistry(), runtimeConfig);

        Assertions.assertFalse(metrics.ignorePatterns.isEmpty());
        Pattern p = metrics.ignorePatterns.get(0);
        Assertions.assertEquals("/item/.*", p.pattern());
        Assertions.assertTrue(p.matcher("/item/123").matches());
    }

    @Test
    public void testHttpServerMetricsMatchPatterns() {
        VertxConfig runtimeConfig = new VertxConfig();
        runtimeConfig.matchPatterns = Optional.of(new ArrayList<>(Arrays.asList("/item/\\d+=/item/{id}")));
        VertxHttpServerMetrics metrics = new VertxHttpServerMetrics(new SimpleMeterRegistry(), runtimeConfig);

        Assertions.assertFalse(metrics.matchPatterns.isEmpty());
        Map.Entry<Pattern, String> entry = metrics.matchPatterns.entrySet().iterator().next();
        Assertions.assertEquals("/item/\\d+", entry.getKey().pattern());
        Assertions.assertEquals("/item/{id}", entry.getValue());
        Assertions.assertTrue(entry.getKey().matcher("/item/123").matches());
    }
}
