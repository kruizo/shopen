package com.bkr.shopen.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter  jwtAuthenticationFilter;

    /**
     * Constructor for SecurityConfiguration.
     *
     * @param authenticationProvider the authentication provider
     * @param jwtAuthenticationFilter the JWT authentication filter
     */
    public SecurityConfiguration(AuthenticationProvider authenticationProvider, 
                        JwtAuthenticationFilter jwtAuthenticationFilter
                       ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // CORS Preflight
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("actuator/health").permitAll()
                .requestMatchers("/home").permitAll()
                .requestMatchers("/users" ,"/users/**").permitAll()
                .requestMatchers("/user","/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
          )
          .sessionManagement(session -> 
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
          .build();
    //       .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling((handling) -> {
    //      handling.authenticationEntryPoint((request, response, authException) -> {
    //         response.setStatus(401);
    //         response.getWriter().write("인증이 필요합니다");
    //      }).accessDeniedHandler((request, response, accessDeniedException) -> {
    //         response.setStatus(403);
    //         response.getWriter().write("접근 권한이 없습니다");
    //      });
            // .exceptionHandling(ex -> ex
            //                 .authenticationEntryPoint(authenticationEntryPoint())
            //                 .accessDeniedHandler(accessDeniedHandler())
            //             )
    //   });
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://app-backend.com", "http://localhost:8080")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"code\":\"UNAUTHORIZED\",\"message\":\"인증이 필요합니다\"}"
            );
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"code\":\"ACCESS_DENIED\",\"message\":\"권한이 부족합니다\"}"
            );
        };
    }

    // @Bean 
    // public InMemoryUserDetailsManager userDetails() {
    //    UserDetails normalUser = User.builder()
    //         .username("user")
    //         .password(passwordEncoder().encode("password"))
    //         .roles("USER")
    //         .build();

    //     UserDetails adminUser = User.builder()
    //         .username("admin")
    //         .password(passwordEncoder().encode("password"))
    //         .roles("ADMIN")
    //         .build();
    //     return new InMemoryUserDetailsManager(normalUser, adminUser);
    // }


}
