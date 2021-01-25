package curso.api.spring.rest.config;

import curso.api.spring.rest.model.Telefone;
import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.repository.TelefoneRepository;
import curso.api.spring.rest.repository.UsuarioRepository;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("dev")
public class ConfigTest implements CommandLineRunner {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        /**
        Usuario usuario1 = new Usuario(null, "primeiro@gmail.com", "Primeiro", "1234");
        Usuario usuario2 = new Usuario(null, "segundo@gmail.com", "Segundo", "5678");
        Usuario usuario3 = new Usuario(null, "terceiro@gmail.com", "Terceiro", "9102");
        Usuario usuario4 = new Usuario(null, "quarto@gmail.com", "Quarto", "5454");
        Usuario usuario5 = new Usuario(null, "quinto@gmail.com", "Quinto", "3333");
        usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2, usuario3, usuario4, usuario5));

        Telefone telefone1 = new Telefone(null, "32242526", usuario1);
        Telefone telefone2 = new Telefone(null, "32243030", usuario1);
        Telefone telefone3 = new Telefone(null, "32244250", usuario2);
        Telefone telefone4 = new Telefone(null, "32243535", usuario2);
        Telefone telefone5 = new Telefone(null, "32242020", usuario3);
        Telefone telefone6 = new Telefone(null, "32248969", usuario3);
        Telefone telefone7 = new Telefone(null, "32241414", usuario4);
        Telefone telefone8 = new Telefone(null, "32247788", usuario4);
        Telefone telefone9 = new Telefone(null, "32248585", usuario5);
        Telefone telefone10 = new Telefone(null,"32241133", usuario5);
        telefoneRepository.saveAll(Arrays.asList(telefone1, telefone2, telefone3, telefone4, telefone5, telefone6, telefone7, telefone8, telefone9, telefone10));
         **/
    }
}
