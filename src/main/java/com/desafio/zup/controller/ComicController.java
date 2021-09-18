package com.desafio.zup.controller;

import com.desafio.zup.model.UsuarioComics;
import com.desafio.zup.service.ComicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comic")
@Api("API Desafio Zup")
@CrossOrigin(origins = "*")
public class ComicController {

    private final ComicService comicService;

    @Autowired
    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

//    @GetMapping(path = "/{cpf}")
//    @ApiOperation(value = "Retorna um usuário com seus comics cadastrados")
//    public UsuarioComics getComicUsuario(@PathVariable("cpf") String cpfUsuario) {
//        return comicService.getComics()
//    }

}
