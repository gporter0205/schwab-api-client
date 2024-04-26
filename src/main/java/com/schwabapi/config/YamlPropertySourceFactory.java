package com.schwabapi.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static org.springframework.beans.factory.config.YamlProcessor.MatchStatus.*;

@Slf4j
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public @NotNull PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
        String activeProfile = Optional.ofNullable(System.getenv("SPRING_PROFILES_ACTIVE"))
                .orElse(System.getProperty("spring.profiles.active"));

        assert activeProfile != null;

        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setDocumentMatchers(properties -> {
            String profileProperty = properties.getProperty("spring.profiles");

            if (profileProperty != null && !profileProperty.isEmpty()) {
                return ABSTAIN;
            }

            return profileProperty != null && profileProperty.contains(activeProfile) ? FOUND : NOT_FOUND;
        });
        yamlFactory.setResources(encodedResource.getResource());

        Properties properties = yamlFactory.getObject();

        assert properties != null;
        return new PropertiesPropertySource(Objects.requireNonNull(encodedResource.getResource().getFilename()), properties);
    }
}