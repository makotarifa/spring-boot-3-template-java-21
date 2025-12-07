package com.angelmorando.template.util.config;

import java.time.Clock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

public class AppUtilConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);

    @Test
    void beansArePresent() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(Clock.class);
            assertThat(context).hasSingleBean(Jackson2ObjectMapperBuilderCustomizer.class);
            assertThat(context).hasSingleBean(MessageSource.class);
            assertThat(context).hasSingleBean(LocalValidatorFactoryBean.class);
            assertThat(context).hasSingleBean(MethodValidationPostProcessor.class);
            // verify the customizer produces an ObjectMapper with expected config
            Jackson2ObjectMapperBuilderCustomizer customizer = context.getBean(Jackson2ObjectMapperBuilderCustomizer.class);
            var builder = new org.springframework.http.converter.json.Jackson2ObjectMapperBuilder();
            customizer.customize(builder);
            ObjectMapper mapper = builder.build();
            assertThat(mapper.isEnabled(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
            assertThat(mapper.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();
        });
    }

}
