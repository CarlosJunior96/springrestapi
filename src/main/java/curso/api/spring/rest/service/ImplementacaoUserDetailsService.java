package curso.api.spring.rest.service;

import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** ESSA CLASSE IMPLEMENTADA JÁ TEM POR PADRÃO UM MÉTODO PARA BUSCAR O USUÁRIO NO BANCO DE DADOS **/
@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /** CONSULTAR NO BANCO DE DADOS O USUARIO**/
        Usuario usuario = usuarioRepository.findUserByLogin(username);

        /** FAZENDO VALIDACAO SE NÃO ENCONTRAR USUÁRIO ELE RETORNA NULL**/
        if (usuario == null){
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
        /** SE ELE ENCONTRAR RETORNA IMPLEMENTAÇÃO DO USER DETAILS **/
        /** o motivo de retornar o objeto User do SpringSecurity é que ele faz muito na parte de autenticação
         ** mas poderia também ser retornado o tipo Usuario **/
        return new User(usuario.getLogin(),
                usuario.getPassword(),
                usuario.getAuthorities());
    }
}
