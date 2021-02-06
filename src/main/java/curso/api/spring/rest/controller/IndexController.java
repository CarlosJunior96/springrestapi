package curso.api.spring.rest.controller;

import com.google.gson.Gson;
import curso.api.spring.rest.model.Usuario;
import curso.api.spring.rest.model.UsuarioDTO;
import curso.api.spring.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController /** ARQUITETURA REST **/
@RequestMapping(value = "/usuario")
public class IndexController {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping(value = "/", produces = "application/json")
    @CacheEvict(value = "cacheusuarios", allEntries = true) /** se não tiver que é usado é removido **/
    @CachePut("cacheusuarios") /** verifica se tem atualização e coloca no cache**/
    public ResponseEntity<List<Usuario>> findAll() throws InterruptedException {

        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok().body(usuarios);
    }

    /** classe DTO que retorna um usuário para visualização **/
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UsuarioDTO> init(@PathVariable (value = "id") Long id){

        Usuario usuario = usuarioRepository.getOne(id);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);

        return ResponseEntity.ok().body(usuarioDTO);
    }


    /**
    @GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
    public ResponseEntity<Usuario> relatorio(@PathVariable (value = "id") Long id,
                                             @PathVariable (value = "venda") Long venda){

        return new ResponseEntity<>(Usuario, HttpStatus.OK);
    }**/


    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {


        /** consumindo api publica de consulta CEP **/

        URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");

        /** abrindo a conexão **/
        URLConnection connection = url.openConnection();

        /** retorno da requisição a URL, ou seja, vem os dados da requisição a URL **/
        InputStream inputStream = connection.getInputStream();

        /** preparando a leitura do inputStream **/
        /** aqui é atribuido ao bufferedReader os dados do inputStream o UTF-8 indica para reconhecimento de caractere especial **/
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));


        /** criando variável para ler o bufferedReader**/
        String cep = "";
        StringBuilder jsonCep = new StringBuilder();

        /** dentro desse while acontece a leitura enquanto as linhas retornadas pelo bufferedReader forem diferentes de nulas **/
        while ((( cep = bufferedReader.readLine())!= null)){

            /** aqui acontece a junção da string contida no cep **/
            jsonCep.append(cep);
        }

        /** transformar string recebida para o tipo JSON **/
        Usuario usuarioAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);

        /** atribuindo os dados recebidos para o objeto usuario **/
        usuario.setLogradouro(usuarioAux.getLogradouro());
        usuario.setComplemento(usuarioAux.getComplemento());
        usuario.setLocalidade(usuarioAux.getLocalidade());
        usuario.setUf(usuarioAux.getUf());


        /** consumindo api publica de consulta CEP **/

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

        if ( usuario.getCep() != null){
            usuarioTemporario.setCep(usuario.getCep());
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
