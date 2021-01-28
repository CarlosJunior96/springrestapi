package curso.api.spring.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import curso.api.spring.rest.model.Usuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** ESTABELE O NOSSO GERENCIADOR DE TOKEN **/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    /** CONFIGURANDO O GERENCIADOR DE AUTENTICAÇÃO **/
    protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

        /** fazendo a chamada do construtor da classe principal e obriga autenticação da URL**/
        super(new AntPathRequestMatcher(url));

        /** Gerenciador de Autenticação **/
        setAuthenticationManager(authenticationManager);
    }

    /** MÉTODO QUE RETORNA O USUÁRIO APÓS PROCESSAR AUTENTICAÇÃO **/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        /** Está pegando o TOKEN para validar **/
        /** aqui ele pega o token e faz uma conversão para o tipo Usuario.class **/
        Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        /** Retorna o usuario login, senha e acessos **/
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha()));
    }

    /** AQUI NOTIFICA SUCESSO CASO SEJA FEITO A AUTENTICAÇÃO **/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        new JWTTokenAutenticacaoService().addAuthetication(response, authResult.getName());

    }
}
