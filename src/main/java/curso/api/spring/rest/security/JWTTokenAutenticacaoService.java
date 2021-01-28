package curso.api.spring.rest.security;

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
    public void addAuthetication(HttpServletResponse response, String username) throws Exception {

        /** MONTAGEM DO TOKEN **/
        String JWT = Jwts.builder()
                .setSubject(username) /** colocando o usuário para ser gerado o token **/
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /** tempo de duração do token concatenado com data atual **/
                .signWith(SignatureAlgorithm.ES512, SECRET).compact(); /** pegando senha e compactando ela com algoritmo ES512**/

        /** JUNTA PREFIXO COM TOKEN **/
        String token = TOKEN_PREFIX + " " + JWT; /** Bearer token-token-token-token **/

        /** ADICIONA NO CABEÇALHO HTTP **/
        response.addHeader(HEADER_STRING, token); /** Authorization: Bearer token-token-token-token **/

        /** ESCREVE TOKEN COMO RESPOSTA NO CORPO DO HTTP **/
        /** \" -> é usado para da uma resposta em formato JSON **/
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }
    /** FIM PARTE DA GERAÇÃO **/


    /** INICIO VALIDAÇÃO DO USUÁRIO **/
    /** RETORNA O USUARIO VALIDADO COM TOKEN OU CASO NÃO SEJA VÁLIDO RETORNA NULL **/
    public Authentication getAuthentication(HttpServletRequest requisicao) {

        String token = requisicao.getHeader(HEADER_STRING);

        if (token != null) {

            /** faz a validação do token do usuário  na requisição **/
            String user = Jwts.parser().setSigningKey(SECRET) /** Bearer token-token-token-token **/
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")) /** token-token-token-token **/
                    .getBody().getSubject(); /** aqui retorna somente o usuario **/

            if (user != null) {
                Usuario usuario = usuarioRepository.findUserByLogin(user);

                if (usuario != null) {
                    /** retorna um usuário pronto para validação de token**/
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getSenha(),
                            usuario.getAuthorities());
                }
            }
        }
        return null; /** usuario não autorizado **/
    }
    /** FIM VALIDAÇÃO DO USUÁRIO **/
}
