package curso.api.spring.rest.repository;

import curso.api.spring.rest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** FAZENDO UMA QUERRY PARA CONSULTAR NO BANCO **/
    @Query("select u from Usuario  u where u.login = ?1") /** ?1 = é a posição do parâmetro dentro do findUserByLogin(String login)**/
    Usuario findUserByLogin(String login);
}
