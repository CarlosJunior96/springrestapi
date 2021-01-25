package curso.api.spring.rest.model;

import javax.persistence.*;


@Entity
@Table(name = "role")
/** VALORES GERADOS DE FORMA AUTOMATICA NO BANCO, POR EXEMPLO: CPF **/
/** allocationSize() vai incrementar de 1 e 1, initialValue() começa a partir de 1 **/
@SequenceGenerator(name = "seq_role", sequenceName = "seq_role", allocationSize = 1, initialValue = 1)
public class Role{

    @Id
    /** aqui o id é gerado de acordo com a sequencia especificada pelo @SequenceGenerator(name="seq_role")**/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    private Long id;

    /** papel no sistema, exemplo ROLE_SECRETARIO, ROLE_GERENTE ... **/
    private String nomeRole;
}
