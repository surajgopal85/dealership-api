package com.dealership.config;

// import beans and configuration
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// cors
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import webmvc
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; 


@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply to all API endpoints
            .allowedOrigins(
                "http://localhost:3000", // React port
                "http://localhost:3001", // Alt react port
                "http://127.0.0.1:3000" // localhost port
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600); // Cache preflight ~ 1 hour
    }

    // Alternative bean based configuration (flexible for production)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // for production, more restrictions and update allowed origins
        // local development
        config.addAllowedOrigin("http://localhost:3000");
        // production:
        // config.addAllowedOrigin("https://prod-domain.com");

        config.addAllowedMethod("*"); // all HTTP methods
        config.addAllowedHeader("*"); // all headers
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
   /*
    * CORS Configuration Explained:
    * 
    * - allowedOrigins: Which domains can call your API
    * - allowedMethods: Which HTTP methods are permitted
    * - allowedHeaders: Which request headers are allowed
    * - allowCredentials: Allow cookies/auth headers
    * - maxAge: How long browsers cache CORS preflight responses
    * 
    * Production Notes:
    * - Replace localhost with your actual frontend domain
    * - Consider using environment variables for origins
    * - Be specific about allowed methods in production
    */

}
