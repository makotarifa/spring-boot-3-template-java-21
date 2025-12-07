package com.angelmorando.template.util.config;

import java.time.Clock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

public class AppUtilConfigTest {

<<<<<<< HEAD
        private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);
=======
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(AppUtilConfig.class, ValidationConfig.class, MetricsConfig.class);
>>>>>>> 71bbab4 (feat(app-util): add validation & metrics config, expose dependencies, and wire module into app-runner and app-service)

    @Test
    void beansArePresent() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(Clock.class);
            assertThat(context).hasSingleBean(Jackson2ObjectMapperBuilderCustomizer.class);
            assertThat(context).hasSingleBean(MessageSource.class);
            assertThat(context).hasSingleBean(LocalValidatorFactoryBean.class);
            assertThat(context).hasSingleBean(MethodValidationPostProcessor.class);
        });
    }

}
