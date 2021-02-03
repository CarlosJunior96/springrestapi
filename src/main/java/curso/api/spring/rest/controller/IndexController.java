package curso.api.spring.rest.controller;

import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController /** ARQUITETURA REST **/
@RequestMapping(value = "/usuario")
public class IndexController {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @CrossOrigin
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Usuario>> findAll(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok().body(usuarios);
    }

    /** SERVIÇO RESTFUL**/
    /**
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> init(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }
    **/

    @GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
    public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id,
                                             @PathVariable (value = "venda") Long venda){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }


    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){

        String senhaCript= new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaCript);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }

    @PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
    public ResponseEntity cadastrarVenda(@PathVariable(value = "iduser") Long iduser,
                                         @PathVariable(value = "idvenda") Long idvenda){

        /** CODIGO RELACIONADO A VENDA **/

        return new ResponseEntity("ID User: " + iduser + ", ID Venda: " + idvenda, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable(value = "id") Long id, @RequestBody Usuario usuario){


        Usuario usuarioTemporario = usuarioRepository.getOne(id);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (usuario.getSenha() != null){
            if (!passwordEncoder.matches(usuario.getSenha(), usuarioTemporario.getSenha())){
             String senhaCript= new BCryptPasswordEncoder().encode(usuario.getSenha());
             usuarioTemporario.setSenha(senhaCript);
             }
        }

        if( usuario.getNome() != null){
            usuarioTemporario.setNome(usuario.getNome());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuarioTemporario);
        return ResponseEntity.ok().body(usuarioSalvo);
    }

    @PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
    public ResponseEntity atualizarVenda(@PathVariable(value = "iduser") Long iduser,
                                         @PathVariable(value = "idvenda") Long idvenda){

        /** CODIGO RELACIONADO A ATUALIZAO DA VENDA **/

        return new ResponseEntity("Venda Atualizada", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** VERSIONAMENTO DE API POR MEIO DE URL **/
    /** INICIO **/
    @GetMapping(value = "/v1/{id}", produces = "application/json")
    public ResponseEntity<Usuario> exibirUsuarioV1(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        System.out.println("Versão 1");
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }


    @GetMapping(value = "/v2/{id}", produces = "application/json")
    public ResponseEntity<Usuario> exibirUsuarioV2(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        System.out.println("Versão 2");
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }
    /** FIM **/




    /** VERSIONAMENTO POR MEIO DE HEADER **/
    @GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
    public ResponseEntity<Usuario> exibirUsuarioIdV1(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        System.out.println("Versão 1 com HEADER");
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
    public ResponseEntity<Usuario> exibirUsuarioIdV2(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        System.out.println("Versão 2 com HEADER");
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }
}
