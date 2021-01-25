package curso.api.spring.rest.controller;

import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController /** ARQUITETURA REST **/
@RequestMapping(value = "/users")
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Usuario>> findAll(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok().body(usuarios);
    }

    /** SERVIÇO RESTFUL**/
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> init(@PathVariable (value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
    public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id,
                                             @PathVariable (value = "venda") Long venda){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }


    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }

    @PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
    public ResponseEntity cadastrarVenda(@PathVariable(value = "iduser") Long iduser,
                                         @PathVariable(value = "idvenda") Long idvenda){

        /** CODIGO RELACIONADO A VENDA **/

        return new ResponseEntity("ID User: " + iduser + ", ID Venda: " + idvenda, HttpStatus.OK);
    }

    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario){

        /** CODIGO RELACIONADO A VENDA **/

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioAtualizado, HttpStatus.OK);
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
        int soma = 1 + 1;
        return ResponseEntity.noContent().build();
    }

}
