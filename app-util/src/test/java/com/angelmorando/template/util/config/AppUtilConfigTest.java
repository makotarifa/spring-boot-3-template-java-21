package com.angelmorando.template.util.config;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

class AppUtilConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);

    @Test
    void beansArePresent() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(Clock.class);
            assertThat(context).hasSingleBean(ObjectMapper.class);
            assertThat(context).hasSingleBean(MessageSource.class);
            assertThat(context).hasSingleBean(LocalValidatorFactoryBean.class);
            assertThat(context).hasSingleBean(MethodValidationPostProcessor.class);
            // verify the primary ObjectMapper has expected config
            ObjectMapper mapper = context.getBean(ObjectMapper.class);
            assertThat(mapper.isEnabled(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
            assertThat(mapper.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();
            assertThat(mapper.getPropertyNamingStrategy()).isEqualTo(com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE);
            // Missing field should be deserialized as null for reference types
            String json = "{\"existingField\":\"value\"}";
            MissingTestDto dto = mapper.readValue(json, MissingTestDto.class);
            assertThat(dto.existingField).isEqualTo("value");
            assertThat(dto.missingField).isNull();
        });
    }

    private static class MissingTestDto {
        public String existingField;
        public String missingField;
    }

}
