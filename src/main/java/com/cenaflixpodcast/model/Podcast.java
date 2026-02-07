package com.cenaflixpodcast.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "podcast")
public class Podcast {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "numeroEpisodio", nullable = false)
    private Integer numeroEpisodio;
    
    @Column(length = 255) // Ajustado para bater com seu varchar(255) do MySQL
    private String duracao;
    
    @Column(name = "nomeEpisodio", length = 255)
    private String nomeEpisodio;
    
    @Column(length = 255)
    private String produtor;
    
    @Column(name = "urlRepositorio", length = 255) // Ajustado para bater com seu varchar(255)
    private String urlRepositorio;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();
    
    public Podcast() {}
    
    public Podcast(Integer numeroEpisodio, String duracao, String nomeEpisodio, 
                   String produtor, String urlRepositorio) {
        this.numeroEpisodio = numeroEpisodio;
        this.duracao = duracao;
        this.nomeEpisodio = nomeEpisodio;
        this.produtor = produtor;
        this.urlRepositorio = urlRepositorio;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getNumeroEpisodio() { return numeroEpisodio; }
    public void setNumeroEpisodio(Integer numeroEpisodio) { this.numeroEpisodio = numeroEpisodio; }
    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public String getNomeEpisodio() { return nomeEpisodio; }
    public void setNomeEpisodio(String nomeEpisodio) { this.nomeEpisodio = nomeEpisodio; }
    public String getProdutor() { return produtor; }
    public void setProdutor(String produtor) { this.produtor = produtor; }
    public String getUrlRepositorio() { return urlRepositorio; }
    public void setUrlRepositorio(String urlRepositorio) { this.urlRepositorio = urlRepositorio; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}