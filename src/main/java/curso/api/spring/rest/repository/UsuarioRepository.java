package curso.api.spring.rest.repository;

import curso.api.spring.rest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** FAZENDO UMA QUERRY PARA CONSULTAR NO BANCO **/
    @Query("select u from Usuario  u where u.login = ?1") /** ?1 = é a posição do parâmetro dentro do findUserByLogin(String login)**/
    Usuario findUserByLogin(String login);

    /** Método criado para atualizar o token do usuário no banco de dados **/
    /** nativeQuery = isso habilita para que seja usado direto no banco de dados **/
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update usuario set token = ?1 where login = ?2")
    void atualizarTokenUser(String token, String login);
}
