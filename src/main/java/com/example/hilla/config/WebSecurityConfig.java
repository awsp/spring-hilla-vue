package com.example.hilla.config;

import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.springframework.security.WebAuthnAuthenticationProvider;
import com.webauthn4j.springframework.security.config.configurers.WebAuthnLoginConfigurer;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final ApplicationContext applicationContext;
  private final static String EXPRESSION = "@webAuthnSecurityExpression.isWebAuthnAuthenticated(authentication)";

  @Bean
  public WebAuthnAuthenticationProvider webAuthnAuthenticationProvider(WebAuthnCredentialRecordManager credentialRecordManager,
    WebAuthnManager webAuthnManager) {
    return new WebAuthnAuthenticationProvider(credentialRecordManager, webAuthnManager);
  }

  @Bean
  public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
    return new ProviderManager(providers);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> {
      web.ignoring().requestMatchers(
        "/favicon.ico",
        "/js/**",
        "/css/**",
        "/VAADIN/**",
        "/webjars/**"
      );
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http.apply(WebAuthnLoginConfigurer.webAuthnLogin())
      .defaultSuccessUrl("/admin", true)
      .failureHandler((request, response, exception) -> {
        log.info("failureHandler");
        response.sendRedirect("/auth/login");
      })
      .loginPage("/auth/login")
      .attestationOptionsEndpoint()
      .rp()
      .name("WebAuthn4J Passkeys")
      .and()
      .pubKeyCredParams(
        new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
        new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256)
      )
      .attestation(AttestationConveyancePreference.DIRECT)
      .extensions()
      .uvm(true)
      .credProps(true)
      .extensionProviders()
      .and()
      .assertionOptionsEndpoint()
      .extensions()
      .extensionProviders();

    http.headers(headers -> {
      headers.permissionsPolicy(config -> config.policy("publickey-credentials-get *"));
      headers.frameOptions(Customizer.withDefaults()).disable();
    });

    http.authorizeHttpRequests(auth -> auth
      .requestMatchers(HttpMethod.GET, "/admin/test").permitAll()
      .requestMatchers(HttpMethod.GET, "/auth/login").permitAll()
      .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll() // TODO: temporary enable
      .anyRequest()
      .access(getWebExpressionAuthorizationManager())
    );

    http.exceptionHandling(eh -> eh.accessDeniedHandler((request, response, accessDeniedException) -> {
      log.info("accessDeniedHandler");
      response.sendRedirect("/auth/login");
    }));

    http.authenticationManager(authenticationManager);

    // As WebAuthn has its own CSRF protection mechanism (challenge), CSRF token is disabled here
    http.csrf(csrf -> {
      csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
      csrf.ignoringRequestMatchers("/webauthn/**");
    });

    return http.build();
  }

  private WebExpressionAuthorizationManager getWebExpressionAuthorizationManager() {
    DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
    expressionHandler.setApplicationContext(applicationContext);
    WebExpressionAuthorizationManager authorizationManager = new WebExpressionAuthorizationManager(EXPRESSION);
    authorizationManager.setExpressionHandler(expressionHandler);

    return authorizationManager;
  }
}
