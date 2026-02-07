package com.cenaflixpodcast.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "arquivo")
public class Arquivo {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_arquivo", nullable = false)
    private String nome;

    @Column(name = "nome_original", nullable = false)
    private String nomeOriginal;

    @Column(name = "tipo_conteudo", nullable = false)
    private String tipo;

    @Column(name = "tamanho")
    private Long tamanho;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "caminho", length = 500)
    private String caminho;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    @ManyToOne
    @JoinColumn(name = "podcast_id")
    private Podcast podcast;

    
    // Construtores
    public Arquivo() {
        this.dataUpload = LocalDateTime.now();
    }
    
    public Arquivo(String nome, String nomeOriginal, String tipo, Long tamanho) {
        this();
        this.nome = nome;
        this.nomeOriginal = nomeOriginal;
        this.tipo = tipo;
        this.tamanho = tamanho;
    }
    
    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getNomeOriginal() { return nomeOriginal; }
    public void setNomeOriginal(String nomeOriginal) { this.nomeOriginal = nomeOriginal; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Long getTamanho() { return tamanho; }
    public void setTamanho(Long tamanho) { this.tamanho = tamanho; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getCaminho() { return caminho; }
    public void setCaminho(String caminho) { this.caminho = caminho; }
    
    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }
    
    public Podcast getPodcast() { return podcast; }
    public void setPodcast(Podcast podcast) { this.podcast = podcast; }
    
    // Método auxiliar para formatar tamanho
    public String getTamanhoFormatado() {
        if (tamanho == null) return "N/A";
        
        if (tamanho < 1024) {
            return tamanho + " B";
        } else if (tamanho < 1024 * 1024) {
            return String.format("%.1f KB", tamanho / 1024.0);
        } else if (tamanho < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", tamanho / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", tamanho / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    // Método auxiliar para obter ícone baseado no tipo
    public String getIcone() {
        if (tipo == null) return "📄";
        
        if (tipo.startsWith("audio")) {
            return "🎵";
        } else if (tipo.startsWith("image")) {
            return "🖼️";
        } else if (tipo.contains("pdf")) {
            return "📕";
        } else if (tipo.contains("word") || tipo.contains("document")) {
            return "📝";
        } else {
            return "📄";
        }
    }
}
