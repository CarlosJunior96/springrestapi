package curso.api.spring.rest.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


/** CLASSE RESPONSÁVEL DE REPRESENTAR OS PAPEIS DOS USUÁRIOS **/

@Entity
@Table(name = "role")
/** VALORES GERADOS DE FORMA AUTOMATICA NO BANCO, POR EXEMPLO: CPF **/
/** allocationSize() vai incrementar de 1 e 1, initialValue() começa a partir de 1 **/
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1, initialValue = 1)
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    /** aqui o id é gerado de acordo com a sequencia especificada pelo @SequenceGenerator(name="seq_role")**/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    private Long id;

    /** papel no sistema, exemplo ROLE_SECRETARIO, ROLE_GERENTE ... **/
    private String nomeRole;

    @Override
    /** retorna o nome do papel, acesso ou autorização exemplo: ROLE_SECRETARIO **/
    /** para retornar o atributo role que quero é necessário sobrescrever o método **/
    public String getAuthority() {
        return this.nomeRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeRole() {
        return nomeRole;
    }

    public void setNomeRole(String nomeRole) {
        this.nomeRole = nomeRole;
    }
}
