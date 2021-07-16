package com.desafio.zup.controller;

import com.desafio.zup.service.DesafioService;
import com.desafio.zup.model.Comic;
import com.desafio.zup.model.Usuario;
import com.desafio.zup.model.UsuarioComics;
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

    @Autowired
    public DesafioController(DesafioService desafioService) {
        this.desafioService = desafioService;
    }

    @PostMapping(path = "/usuarios")
    @ApiOperation(value = "Salva um usuário - CPF, nome, email e dataNascimento são obrigatórios.")
    public ResponseEntity<String> addUsuario(@RequestBody Usuario usuario) {

        try {
            desafioService.addUsuario(usuario);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro ao salvar o usuário: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso: usuário cadastrado CPF: " + usuario.getCpf(), HttpStatus.CREATED);
    }

    @GetMapping(path = "/comics/{cpf}")
    @ApiOperation(value = "Retorna um usuário com seus comics cadastrados")
    public UsuarioComics getComicUsuario(@PathVariable("cpf") String cpfUsuario) throws Exception {
        return desafioService.getComicUsuario(cpfUsuario);
    }

    @PostMapping(path = "/comics/{comicId}/{cpf}")
    @ApiOperation(value = "Salva um comic para um usuário - comicId e CPF do usuário são obrigatórios.")
    public ResponseEntity<String> addComic(@PathVariable("comicId") Long comicId, @PathVariable("cpf") String cpfUsuario) {

        try {
            desafioService.addComic(comicId, cpfUsuario);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Sucesso: comic: " + comicId + " cadastrado para o usuário CPF: " + cpfUsuario, HttpStatus.CREATED);

    }


    @GetMapping(path = "/usuarios")
    @ApiOperation(value = "Retorna todos os usuários cadastrados", hidden = true)
    public List<Usuario> getUsuarios() {
        return desafioService.getUsuarios();
    }

}
