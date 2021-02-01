package curso.api.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Telefone> telefones = new HashSet<>();

    /** vai pegar esses dados do banco **/
    /** vai ser criado uma tabela que conterá os dados do usuário **/
    /** e nela terá o código do usuário e o código do acesso **/
    /** JoinColumns() ele irá fazer a união dessas colunas no banco de dados **/

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "role_id"}, name = "unique_role_user")}
    ,joinColumns = {@JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", foreignKey = @ForeignKey (name = "usuario_fk", value = ConstraintMode.CONSTRAINT))}
    ,inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", updatable = false, foreignKey = @ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT))})
    private Collection<Role> roles;

    public Usuario (){

    }

    public Usuario(Long id, String login, String nome, String senha){
        this.id = id;
        this.login = login;
        this.nome = nome;
        this.senha = senha;
    }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /** ESSA LISTA CONTÉM AS AUTORIZAÇÕES, OU SEJA, OS ACESSOS DO USUÁRIO, EXEMPLO: ROLE_SECRETARIO**/
    @Override
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
}
