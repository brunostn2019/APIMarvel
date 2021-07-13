package com.desafio.zup.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table
public class Usuario {

    @Id
    @Column(unique = true)
    private String cpf;
    private String nome;
    @Column(unique = true)
    private String email;
    private LocalDate dataNascimento;
    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Comic> comics;

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

    @JsonIgnore
    public List<Comic> getComics() {
        return comics;
    }

    @JsonProperty
    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }

    public Usuario() {
    }

    public Usuario(String cpf, String nome, String email, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;

    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cpf=" + cpf +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", comics=" + comics +
                '}';
    }
}
