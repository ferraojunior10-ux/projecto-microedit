package mz.co.jcf;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@Configuration
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
  @Bean UserDetailsService userDetailsService(@Value("${jcf.security.username}") String username, @Value("${jcf.security.password-hash}") String hash) {
    if (username.isBlank() || !hash.matches("^\\$2[aby]\\$\\d{2}\\$.*")) throw new IllegalArgumentException("Set a BCrypt JCF_ADMIN_PASSWORD_HASH and JCF_ADMIN_USER");
    return new InMemoryUserDetailsManager(User.withUsername(username).password(hash).roles("ADMIN").build());
  }
  @Bean CorsConfigurationSource corsConfigurationSource(@Value("${jcf.cors.allowed-origin}") String origin) {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(List.of(origin));
    c.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", c);
    return source;
  }
  @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a -> a
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
        .requestMatchers("/api/**").hasRole("ADMIN")
        .anyRequest().denyAll())
      .httpBasic(Customizer.withDefaults())
      .build();
  }
}
