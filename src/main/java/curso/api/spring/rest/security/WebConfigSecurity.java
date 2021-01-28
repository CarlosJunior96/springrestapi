package curso.api.spring.rest.security;

/** CLASSE RESPONSÁVEL DE MAPEAR URLS, ENDEREÇOS, AUTORIZA E BLOQUEIA ACESSOS URLS **/

import curso.api.spring.rest.service.ImplementacaoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/** @Configuration indica que é uma classe de configuração **/
/** @EnableWebSecurity habilita todos os recurso de segurança pra essa classe **/
/** @Autowired faz a injenção de dependencia **/
@Configuration
@EnableWebSecurity
@Profile("dev")
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    /**  configurações de solicitação/acesso HTTP // tudo de HTTP passará por aqui **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** ativando proteção contra usuários que não estão validados por token**/
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

        /** Ativando a permissão para acesso a página inicial do sistema. Exemplo: www.sistema.com.br/ **/
        .disable().authorizeRequests().antMatchers("/").permitAll()
        .antMatchers("/index").permitAll()

        /** Autenticação URL de logouts - Redireciona após o user deslogar do sistema nesse caso para o index **/
        .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

        /** Mapeia URL de logout e valida o usuário **/
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

        /** filtrar requisições de login para autenticação **/
        .and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)

        /** filtrar demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP **/
        .addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    /** aqui acontece a implementação que cuida de receber o serviço e por trás fazer essa validação de consulta no
     ** bando de dados **/
    /** AuthenticationManagerBuilder é o gerenciador de autenticação **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /** Service que irá consultar o usuário no banco de dados **/
        auth.userDetailsService(implementacaoUserDetailsService)
                /** Padrão de codificação de senha do usuário **/
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
