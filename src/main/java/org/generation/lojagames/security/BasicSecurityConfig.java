package org.generation.lojagames.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /*
         *  PROTECTED: a proteção permite que só a segunça tenha acesso ao usuario em memoria
         *  AUTH: apelido
         *  THROWS (excessão): indica que se eu quiser usar um usuário do meu db além desse memoria eu também consigo
         */

        auth.userDetailsService(userDetailsService);
        auth.inMemoryAuthentication()
                .withUser("root")
                .password(passwordEncoder().encode("root"))
                .authorities("ROLE_USER");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

        /*
         * inMemoryAuthentication: para entrar na aplicação precisa de usuario e senha
         * ROLE_USER: indica que acima foi feita a atribuição do usuário
         * passwordEncoder: usamos depois no bean para encriptar a senha!!!!!!!
         * BCryptPasswordEncoder: encripta de fato a senha!!!!!!!!!!
         */

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() //libera acesso as Requests
                .antMatchers("/usuarios/logar").permitAll()
                .antMatchers("/usuarios/cadastrar").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()

                // daqui pra baixo toda e qualquer requisição fora as acima precisam de autenticação
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().csrf().disable();


    }
}
