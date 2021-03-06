package com.desafio.zup.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity(name = "Usuario")
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "usuario_unique", columnNames = {"email"})
        }
)
public class Usuario {

    @Id
    @Column(
            name = "cpf",
            updatable = false
    )
    private String cpf;
    @Column(
            name = "nome",
            nullable = false
    )
    private String nome;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;
    @Column(
            name = "data_nascimento",
            nullable = false
    )
    private LocalDate dataNascimento;
    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    @ApiModelProperty(hidden = true)
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
    @ApiModelProperty(hidden = true)
    public List<Comic> getComics() {
        return comics;
    }

    @JsonProperty
    @ApiModelProperty(hidden = true)
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

    public Usuario(String cpf) {
        this.cpf = cpf;
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
