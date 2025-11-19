package exercise_security.game_api.config;

import exercise_security.game_api.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired      // alt use constructor, now as autowired it can't be final
    private CustomUserDetailService customUserDetailService;

    // 1.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/public/**").permitAll()
                        .requestMatchers("/games/**").authenticated()
                        //.requestMatchers(HttpMethod.GET,"/games/**").authenticated()  // alt
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                )
                .userDetailsService(customUserDetailService)
                //.userDetailsService(userDetailsService())
                .httpBasic(Customizer.withDefaults());

        return http.build();

//        httpSecurity.csrf(c -> c.disable())
//                .authorizeHttpRequests( auth -> auth
//                        .requestMatchers("/games/**").authenticated()
//                        .requestMatchers("/public/**").permitAll()
//                        ).httpBasic(Customizer.withDefaults());
//
//        return httpSecurity.build();
    }


    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

//    @Bean
//    public UserDetailsService userDetails(){    // in memory user details
//        UserDetails user = User
//                .withUsername("user")
//                .password( passwordEncoder().encode("user123"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
