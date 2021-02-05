package curso.api.spring.rest.security;

import curso.api.spring.rest.ApplicationContextLoad;
import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
@Component
public class JWTTokenAutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * tempo de validade do token
     **/
    private static final long EXPIRATION_TIME = 172800000;

    /**
     * uma senha única para compor a autenticação
     **/
    private static final String SECRET = "SenhaExtremamenteSecreta";

    /**
     * prefixo padrão de autenticação de TOKEN
     **/
    private static final String TOKEN_PREFIX = "Bearer";

    /**
     * identificação do cabeçalho na resposta do TOKEN
     **/
    private static final String HEADER_STRING = "Authorization";

    /** INICIO PARTE DA GERAÇÃO **/

    /** GERANDO TOKEN DE AUTENTICAÇÃO E ADICIONANDO O CABEÇALHO E RESPOSTA HTTP **/
    /** addAuthetication() - recebe a instância HTTP e o Username **/
    public void addAuthetication(HttpServletResponse response, String username) throws IOException {

        /** MONTAGEM DO TOKEN **/
        String JWT = Jwts.builder()
                .setSubject(username) /** colocando o usuário para ser gerado o token **/
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /** tempo de duração do token concatenado com data atual **/
                .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /** pegando senha e compactando ela com algoritmo HS512**/

        /** JUNTA PREFIXO COM TOKEN **/
        String token = TOKEN_PREFIX + " " + JWT; /** Bearer token-token-token-token **/

        /** ADICIONA NO CABEÇALHO HTTP **/
        response.addHeader(HEADER_STRING, token); /** Authorization: Bearer token-token-token-token **/

        /** liberando resposta para portas diferentes que usam api, no caso clientes WEB **/
        liberacaoCors(response);


        /** ESCREVE TOKEN COMO RESPOSTA NO CORPO DO HTTP **/
        /** \" -> é usado para da uma resposta em formato JSON **/
        response.getWriter().write("{\""+HEADER_STRING+"\": \""+token+"\" }");
    }
    /** FIM PARTE DA GERAÇÃO **/


    /** INICIO VALIDAÇÃO DO USUÁRIO **/
    /** RETORNA O USUARIO VALIDADO COM TOKEN OU CASO NÃO SEJA VÁLIDO RETORNA NULL **/
    public Authentication getAuthentication(HttpServletRequest requisicao, HttpServletResponse resposta) {

        String token = requisicao.getHeader(HEADER_STRING);

        if (token != null) {

            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
            /** faz a validação do token do usuário  na requisição **/
            String user = Jwts.parser().setSigningKey(SECRET) /** Bearer token-token-token-token **/
                    .parseClaimsJws(tokenLimpo) /** token-token-token-token **/
                    .getBody().getSubject(); /** aqui retorna somente o usuario **/

            if (user != null) {
                Usuario usuario = ApplicationContextLoad.getApplicationContext()
                        .getBean(UsuarioRepository.class) /** são todas classes e serviços carregados no projeto **/
                        .findUserByLogin(user);

                if (usuario != null) {
                    /** VALIDA SE O TOKEN RETORNADO TEM NO BANCO DE DADOS CASO TENHA PERMITE O ACESSO **/
                    if (tokenLimpo.equalsIgnoreCase(usuario.getToken())){
                        /** retorna um usuário pronto para validação de token**/
                        return new UsernamePasswordAuthenticationToken(
                                usuario.getLogin(),
                                usuario.getSenha(),
                                usuario.getAuthorities());

                    }
                }
            }
        }

        /** configurando o cors // crossorigin para acesso */
        liberacaoCors(resposta);

        return null; /** usuario não autorizado **/
    }

    private void liberacaoCors(HttpServletResponse resposta) {

        /** liberando para que o usuário possa ter acesso e requisição dessa api **/
        if (resposta.getHeader("Access-Control-Allow-Origin") == null){
            resposta.addHeader("Access-Control-Allow-Origin", "*");
        }

        if (resposta.getHeader("Access-Control-Allow-Headers") == null){
            resposta.addHeader("Access-Control-Allow-Headers", "*");
        }

        if (resposta.getHeader("Access-Control-Request-Headers") == null){
            resposta.addHeader("Access-Control-Request-Headers", "*");
        }

        if (resposta.getHeader("Access-Control-Methods-Headers") == null){
            resposta.addHeader("Access-Control-Methods-Headers", "*");
        }
    }
    /** FIM VALIDAÇÃO DO USUÁRIO **/
}
