package curso.api.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String login;

    private String nome;

    private String senha;

    private String cpf;

    private String token = "";

    private String cep;

    private String logradouro;

    private String complemento;

    private String localidade;

    private String uf;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Telefone> telefones = new ArrayList<>();

    /** vai pegar esses dados do banco **/
    /** vai ser criado uma tabela que conterá os dados do usuário **/
    /** e nela terá o código do usuário e o código do acesso **/
    /** JoinColumns() ele irá fazer a união dessas colunas no banco de dados **/

    /** @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "role_id"}, name = "unique_role_user")}
    ,joinColumns = {@JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", foreignKey = @ForeignKey (name = "usuario_fk", value = ConstraintMode.CONSTRAINT))}
    ,inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", updatable = false, foreignKey = @ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT))})**/


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_role", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "role_id"}, name = "unique_role_user"),
    joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)),

    inverseJoinColumns = @JoinColumn (name = "role_id", referencedColumnName = "id", table = "role", foreignKey = @ForeignKey (name = "role_fk", value = ConstraintMode.CONSTRAINT)))
    private List<Role> roles;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /** https://www.concretepage.com/jackson-api/jackson-jsonproperty-and-jsonalias-example **/

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return senha.equals(usuario.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senha);
    }

    @Override
    @JsonProperty
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /** ESSA LISTA CONTÉM AS AUTORIZAÇÕES, OU SEJA, OS ACESSOS DO USUÁRIO, EXEMPLO: ROLE_SECRETARIO**/

}
