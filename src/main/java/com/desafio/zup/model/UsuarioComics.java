package com.desafio.zup.model;
import java.util.List;

public class UsuarioComics {
    private Usuario usuario;
    private List<Comic> comics;

    public UsuarioComics() {
    }

    public UsuarioComics(Usuario usuario, List<Comic> comics) {
        this.usuario = usuario;
        this.comics = comics;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }
}
