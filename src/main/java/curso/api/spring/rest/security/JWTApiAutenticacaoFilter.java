package curso.api.spring.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** filtro onde todas as requisições serão capturadas para autenticar **/
/** toda vez que tentar autenticar essa classe pegará a solicitação **/
public class JWTApiAutenticacaoFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /** Estabelece a autenticação para a requisição **/
        Authentication authentication = new JWTTokenAutenticacaoService().getAuthentication((HttpServletRequest) request);

        /** Coloca o processo de autenticação no spring security **/
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /** Continua o processo **/
        chain.doFilter(request, response);
    }
}
