package com.desafio.zup.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.DayOfWeek;


@Entity(name = "Comic")
@Table(name = "comic")
public class Comic {

    @Id
    @Column(
            name = "comic_id",
            updatable = false
    )
    private Long comicId;
    @Column(
            name = "title",
            updatable = false
    )
    private String title;
    @Column(
            name = "price_impresso",
            updatable = false
    )
    private String priceImpresso;
    @Column(
            name = "price_digital",
            updatable = false
    )
    private String priceDigital;
    @Column(
            name = "creators",
            updatable = false
    )
    private String creators;
    @Column(
            name = "description",
            updatable = false
    )
    @Lob
    private String description;
    @Column(
            name = "isbn",
            updatable = false
    )
    private String isbn;
    @ManyToOne
    @JsonIgnore
    private Usuario usuario;
    @Transient
    private boolean descontoAtivo;
    @Column(
            name = "dia_desconto"
    )
    private DayOfWeek diaDesconto;


    public Comic(Long comicId, Usuario usuario) {
        this.comicId = comicId;
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "comicId='" + comicId + '\'' +
                ", title='" + title + '\'' +
                ", priceImpresso='" + priceImpresso + '\'' +
                ", priceDigital='" + priceDigital + '\'' +
                ", creators='" + creators + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", usuario='" + usuario + '\'' +
                ", diaDesconto='" + diaDesconto + '\'' +
                '}';
    }

    public Comic() {
    }

    public Comic(Long comicId) {
        this.comicId = comicId;
    }

    @JsonIgnore
    public Usuario getUsuario() {
        return usuario;
    }

    @JsonProperty
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean getDescontoAtivo() {
        return descontoAtivo;
    }

    public void setDescontoAtivo(boolean descontoAtivo) {
        this.descontoAtivo = descontoAtivo;
    }

    public Long getComicId() {
        return comicId;
    }

    public void setComicId(Long comicId) {
        this.comicId = comicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceImpresso() {

        if (this.descontoAtivo) {
            Double precoImpressoD = Double.valueOf(priceImpresso);
            precoImpressoD = precoImpressoD * 0.9;
            DecimalFormat df = new DecimalFormat("0.00");
            priceImpresso = df.format(precoImpressoD);
        }
        return priceImpresso;
    }

    public void setPriceImpresso(String priceImpresso) {
        this.priceImpresso = priceImpresso;
    }


    public String getPriceDigital() {
        if (this.descontoAtivo) {
            Double priceDigitalD = Double.valueOf(priceDigital);
            priceDigitalD = priceDigitalD * 0.9;
            DecimalFormat df = new DecimalFormat("0.00");
            priceDigital = df.format(priceDigitalD);
        }
        return priceDigital;
    }

    public void setPriceDigital(String priceDigital) {
        this.priceDigital = priceDigital;
    }

    public String getCreators() {
        return creators;
    }

    public void setCreators(String creators) {
        this.creators = creators;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public DayOfWeek getDiaDesconto() {
        return diaDesconto;
    }

    public void setDiaDesconto(DayOfWeek diaDesconto) {
        this.diaDesconto = diaDesconto;
    }


}
