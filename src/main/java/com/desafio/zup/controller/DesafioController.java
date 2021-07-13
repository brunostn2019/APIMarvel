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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
public class DesafioController {

    private final DesafioService desafioService;
    private final ComicFeignInterface comicFeignInterface;

    @Autowired
    public DesafioController(DesafioService desafioService, Decoder decoder, Encoder encoder) {
        this.desafioService = desafioService;
        comicFeignInterface = Feign.builder().encoder(encoder).decoder(decoder)
                .target(Target.EmptyTarget.create(ComicFeignInterface.class));
    }

    @GetMapping(path = "/comics/{cpf}")
    public UsuarioComics getComicUsuario(@PathVariable("cpf") String cpfUsuario) throws Exception {
        return desafioService.getComicUsuario(cpfUsuario);
    }

    @PostMapping(path = "/comics")
    public ResponseEntity<String> addComic(@RequestBody Comic comic) {

        try {
            desafioService.addComic(comic);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso", HttpStatus.CREATED);

    }

    @PostMapping(path = "/usuarios")
    public ResponseEntity<String> addUsuario(@RequestBody Usuario usuario) {
        try {
            desafioService.addUsuario(usuario);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro ao salvar o usuário: "+usuario.toString()+" ERRO:" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso", HttpStatus.CREATED);
    }

}
