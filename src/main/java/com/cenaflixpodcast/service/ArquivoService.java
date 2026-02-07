package com.cenaflixpodcast.service;

import com.cenaflixpodcast.model.Arquivo;
import com.cenaflixpodcast.repository.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivoService {

    @Autowired
    private ArquivoRepository arquivoRepository;

    // Diretório para upload (pode ser configurável)
    private final Path rootLocation = Paths.get("uploads");

    public List<Arquivo> listarTodos() {
        return arquivoRepository.findByOrderByDataUploadDesc();
    }

    public Arquivo buscarPorId(Integer id) {
        return arquivoRepository.findById(id).orElse(null);
    }

    public Arquivo salvarArquivo(MultipartFile file, String descricao, Integer podcastId) throws IOException {
        // Criar diretório se não existir
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        // Gerar nome único para o arquivo
        String nomeOriginal = file.getOriginalFilename();
        String extensao = "";
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }

        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Salvar arquivo no disco
        Path destinationFile = rootLocation.resolve(Paths.get(nomeArquivo))
                .normalize().toAbsolutePath();

        file.transferTo(destinationFile.toFile());

        // Criar entidade Arquivo
        Arquivo arquivo = new Arquivo();
        arquivo.setNome(nomeArquivo);
        arquivo.setNomeOriginal(nomeOriginal);
        arquivo.setTipo(file.getContentType());
        arquivo.setTamanho(file.getSize());
        arquivo.setDescricao(descricao);
        arquivo.setCaminho(destinationFile.toString());
        arquivo.setDataUpload(LocalDateTime.now());

        // TODO: Associar podcast se podcastId for fornecido
        return arquivoRepository.save(arquivo);
    }

    public void excluirArquivo(Integer id) throws IOException {
        Arquivo arquivo = buscarPorId(id);
        if (arquivo != null && arquivo.getCaminho() != null) {
            // Excluir arquivo do disco
            Files.deleteIfExists(Paths.get(arquivo.getCaminho()));
            // Excluir do banco
            arquivoRepository.deleteById(id);
        }
    }

    public long contarArquivos() {
        return arquivoRepository.count();
    }

    public List<Arquivo> buscarPorTipo(String tipo) {
        return arquivoRepository.findByTipoContainingIgnoreCase(tipo);
    }

    public List<Arquivo> buscarPorNome(String nome) {
        return arquivoRepository.findByNomeOriginalContainingIgnoreCase(nome);
    }
}
