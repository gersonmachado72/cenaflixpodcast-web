package com.cenaflixpodcast.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PodcastDTO {
    private Integer id;
    private Integer numeroEpisodio;
    private String duracao;
    private String nomeEpisodio;
    private String produtor;
    private String urlRepositorio;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Getters e Setters
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
