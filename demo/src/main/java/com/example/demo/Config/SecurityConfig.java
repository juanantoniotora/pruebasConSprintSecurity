package com.example.demo.Config;

import java.nio.file.DirectoryStream.Filter;

import javax.servlet.FilterChain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration      // indica que es una clase de configuración
@EnableMethodSecurity
public class SecurityConfig {

    // ejemplo 1
    // @Bean
    /**public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
        // csrf().disable().       // deshabilita la protección CSRF (Cross-Site Request Forgery) cuando no se use formularios web
        authorizeHttpRequests().antMatchers("/v1/index2").permitAll()
        .anyRequest().authenticated().
        and().formLogin().permitAll().
        and().build();
    }
    **/


    // ejemplo 2: configuración avanzada de la sesión 
    // @Bean
    /** public SecurityFilterChain securityFilterChain2(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
        authorizeRequests(auth -> {
            auth.antMatchers("/v1/index2").permitAll();
            auth.anyRequest().authenticated();
        }).
        formLogin().
            successHandler(myAuthenticationSuccessHandler()).  // URL donde redirigir tras login exitoso
            permitAll().
        and().sessionManagement().
            sessionCreationPolicy(SessionCreationPolicy.ALWAYS). 
                        // ALWAYAS: crear sesión siempre que se solicite
                        // IF_REQUIRED: crear sesión solo si es necesario
                        // NEVER: nunca crear sesión, pero usarla si ya existe  
                        // STATELESS: no usar sesión (para APIs RESTful), pero tampoco guarda datos de sesión
            invalidSessionUrl("/login").
            maximumSessions(1).  // número máximo de sesiones por usuario
            expiredUrl("/login").
            sessionRegistry(sessionRegistry()).  // definir un objeto que se encargará de administrar todos los registros de la sesión
        and().
            sessionFixation().migrateSession(). // evitar secuestro de sesión migrando la sesión al autenticar
                // MIGRATE_SESSION: crea una nueva sesión y transfiere los atributos de la sesión antigua a la nueva
                // NEW_SESSION: crea una nueva sesión sin transferir atributos
                // NONE: no realiza ninguna acción de fijación de sesión

        //and().httpBasic(). // enviar las credenciales en la cabecera HTTP Authorization para cuando la seguridad no es importante
        and().build();
    } 

    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        // Lógica personalizada después de un inicio de sesión exitoso
        return (Request, response, authentication) -> {
            response.sendRedirect("/v1/session");
            
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    } */


    // ejemplo 3: configuración avanzada de la sesión 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
        csrf(config -> config.disable())
        .authorizeRequests(auth -> {
            auth.antMatchers("/v1/index2").permitAll();
            auth.anyRequest().authenticated();
        }).
        sessionManagement(session ->{
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .httpBasic()
        .and()
        .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
         InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("juan")
        .password("1234")
        .roles("USER")
        .build());
        
        return manager;
    }
    

    // para cuando no se use un codificador de contraseñas
     @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    } 

    /**@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/


    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder)
        .and().build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
}
