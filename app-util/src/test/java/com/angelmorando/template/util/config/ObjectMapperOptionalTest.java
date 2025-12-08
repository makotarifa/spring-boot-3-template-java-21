package com.angelmorando.template.util.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperOptionalTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);

    @Test
    void optionalEmptyWhenMissingAndEmptyWhenNull() throws Exception {
        contextRunner.run(context -> {
            ObjectMapper mapper = context.getBean(ObjectMapper.class);
            String missingJson = "{\"name\":\"test\"}";
            String nullJson = "{\"name\":\"test\", \"opt\": null}";
            TestDto t1 = mapper.readValue(missingJson, TestDto.class);
            assertThat(t1.opt).isNotNull();
            assertThat(t1.opt.isPresent()).isFalse();
            TestDto t2 = mapper.readValue(nullJson, TestDto.class);
            assertThat(t2.opt).isNotNull();
            assertThat(t2.opt.isPresent()).isFalse();
        });
    }

    private static class TestDto {
        public String name;
        public Optional<String> opt = Optional.empty();
    }

}
