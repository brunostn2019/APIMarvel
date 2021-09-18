package com.desafio.zup.dto;
import com.desafio.zup.model.Usuario;

import java.time.LocalDate;
import java.util.Objects;

public class UsuarioRespostaDTO {


    private String cpf;

    private String nome;

    private String email;

    private LocalDate dataNascimento;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public UsuarioRespostaDTO() {    }

    public UsuarioRespostaDTO(String cpf, String nome, String email, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public UsuarioRespostaDTO(String cpf) {
        this.cpf = cpf;
    }

    public UsuarioRespostaDTO transformaEmResposta(Usuario usuario)
    {
        return new UsuarioRespostaDTO(usuario.getCpf(), usuario.getNome(), usuario.getEmail(), usuario.getDataNascimento());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cpf=" + cpf +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioRespostaDTO)) return false;
        UsuarioRespostaDTO that = (UsuarioRespostaDTO) o;
        return Objects.equals(getCpf(), that.getCpf()) && Objects.equals(getNome(), that.getNome()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getDataNascimento(), that.getDataNascimento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCpf(), getNome(), getEmail(), getDataNascimento());
    }
}
