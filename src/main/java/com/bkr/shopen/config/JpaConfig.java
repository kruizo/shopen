package com.bkr.shopen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
    // This class is used to enable JPA auditing features in the application.
    // It allows us to automatically populate fields like createdDate and lastModifiedDate
    // in our entity classes when they are persisted or updated.
    // No additional beans or methods are required here, as the @EnableJpaAuditing annotation
}
