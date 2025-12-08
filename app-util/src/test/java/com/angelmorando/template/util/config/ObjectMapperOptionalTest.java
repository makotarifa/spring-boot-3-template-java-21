package com.angelmorando.template.util.config;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

class ObjectMapperOptionalTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);

    @Test
    void optionalEmptyWhenMissingAndEmptyWhenNull() {
        contextRunner.run(context -> {
            var mapper = context.getBean(ObjectMapper.class);
            String missingJson = "{\"name\":\"test\"}";
            String nullJson = "{\"name\":\"test\", \"opt\": null}";
            TestDto t1 = mapper.readValue(missingJson, TestDto.class);
            assertThat(t1.opt).isEmpty();
            TestDto t2 = mapper.readValue(nullJson, TestDto.class);
            assertThat(t2.opt).isEmpty();
        });
    }

    private static class TestDto {
        public Optional<String> opt = Optional.empty();
    }

}
