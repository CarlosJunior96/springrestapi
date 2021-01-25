package curso.api.spring.rest.repository;

import curso.api.spring.rest.model.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
