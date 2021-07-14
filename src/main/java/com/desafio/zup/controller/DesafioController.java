package com.desafio.zup.controller;
import com.desafio.zup.feign.ComicFeignInterface;
import com.desafio.zup.service.DesafioService;
import com.desafio.zup.model.Comic;
import com.desafio.zup.model.Usuario;
import com.desafio.zup.model.UsuarioComics;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("api/v1")
@Api("API Desafio Zup")
@CrossOrigin(origins = "*")
public class DesafioController {

    private final DesafioService desafioService;
    private final ComicFeignInterface comicFeignInterface;

    @Autowired
    public DesafioController(DesafioService desafioService, Decoder decoder, Encoder encoder) {
        this.desafioService = desafioService;
        comicFeignInterface = Feign.builder().encoder(encoder).decoder(decoder)
                .target(Target.EmptyTarget.create(ComicFeignInterface.class));
    }

    @PostMapping(path = "/usuarios")
    @ApiOperation(value = "Salva um usuário - CPF, nome, email e dataNascimento são obrigatórios.")
    public ResponseEntity<String> addUsuario(@RequestBody Usuario usuario) {
        if(usuario.getCpf()==null
                || usuario.getCpf().trim().isEmpty()
                || usuario.getEmail()==null
                || usuario.getEmail().trim().isEmpty()
                || usuario.getNome()==null
                || usuario.getNome().trim().isEmpty()
                || usuario.getDataNascimento()==null
        )
        {
            return new ResponseEntity<>("CPF, nome, email e dataNascimento são obrigatórios!", HttpStatus.BAD_REQUEST);
        }
        try {
            desafioService.addUsuario(usuario);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro ao salvar o usuário: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso: usuário cadastrado CPF: "+usuario.getCpf(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/comics/{cpf}")
    @ApiOperation(value = "Retorna um usuário com seus comics cadastrados")
    public UsuarioComics getComicUsuario(@PathVariable("cpf") String cpfUsuario) throws Exception {
        return desafioService.getComicUsuario(cpfUsuario);
    }

    @PostMapping(path = "/comics")
    @ApiOperation(value = "Salva um comic para um usuário - comicId e CPF do usuário são obrigatórios.")
    public ResponseEntity<String> addComic(@RequestBody Comic comic) {
        if(comic.getComicId()==null
                || comic.getComicId()==0
                || comic.getUsuario().getCpf() == null
                || comic.getUsuario().getCpf().trim().isEmpty()
        )
        {
            return new ResponseEntity<>("comicId e CPF do usuário são obrigatórios!", HttpStatus.BAD_REQUEST);
        }

        try {
            desafioService.addComic(comic);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso: comic: "+ comic.getComicId() + " cadastrado para o usuário CPF: "+comic.getUsuario().getCpf(), HttpStatus.CREATED);

    }



    @GetMapping(path="/usuarios")
    @ApiOperation(value = "Retorna todos os usuários cadastrados", hidden = true)
    public List<Usuario> getUsuarios(){
        return desafioService.getUsuarios();
    }

}
