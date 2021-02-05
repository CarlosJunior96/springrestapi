package curso.api.spring.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioDTO implements Serializable {

    private String nome;
    private String login;
    private String cpf;


    public UsuarioDTO(Usuario usuario){
        setNome(usuario.getNome());
        setLogin(usuario.getLogin());
        setCpf(usuario.getCpf());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
