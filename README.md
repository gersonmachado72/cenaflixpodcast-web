# 🎙️ Cenaflix Podcast - Sistema de Gerenciamento

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6.1.1-green)
![Java](https://img.shields.io/badge/Java-17-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1.2-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.1-purple)

Sistema completo de gerenciamento de podcasts com autenticação, upload de arquivos, gerenciamento de usuários e player integrado.

## 📋 Funcionalidades

### 🎯 **Módulo de Podcasts**
- ✅ Upload de arquivos de áudio (MP3)
- ✅ Categorização por gênero
- ✅ Player integrado com HTML5
- ✅ Controle de play/pause/progresso
- ✅ Listagem com filtros e paginação
- ✅ Edição e exclusão de podcasts
- ✅ Sistema de likes/favoritos

### 👥 **Módulo de Usuários**
- ✅ Cadastro com validação
- ✅ Perfis de acesso (Admin, Editor, Visualizador)
- ✅ Senhas criptografadas com BCrypt
- ✅ Gerenciamento completo (CRUD)
- ✅ Ativação/desativação de contas
- ✅ Sistema de login seguro

### 🔐 **Segurança**
- ✅ Autenticação com Spring Security
- ✅ Autorização baseada em roles
- ✅ Proteção CSRF
- ✅ Configuração de rotas protegidas
- ✅ Logout seguro
- ✅ Sessões gerenciadas

### 🎨 **Interface**
- ✅ Design responsivo com Bootstrap 5
- ✅ Templates Thymeleaf
- ✅ Ícones FontAwesome
- ✅ Player de áudio personalizado
- ✅ Dashboard administrativo
- ✅ Formulários validados

## 🚀 Tecnologias

- **Backend:** Spring Boot 3, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap 5, JavaScript Vanilla
- **Banco de Dados:** MySQL 8.0
- **Autenticação:** Spring Security + BCrypt
- **Upload de Arquivos:** Spring Multipart
- **Build:** Maven 3.8+

## 📁 Estrutura do Projeto
src/main/java/com/cenaflixpodcast/
├── controller/ # Controladores MVC
│ ├── PodcastController.java
│ ├── UsuarioController.java
│ └── web/UsuarioController.java
├── model/ # Entidades JPA
│ ├── Podcast.java
│ ├── Usuario.java
│ └── Categoria.java
├── repository/ # Repositórios Spring Data
│ ├── PodcastRepository.java
│ └── UsuarioRepository.java
├── service/ # Lógica de negócio
│ ├── PodcastService.java
│ └── UsuarioService.java
├── security/ # Configuração de segurança
│ ├── SecurityConfig.java
│ └── CustomUserDetailsService.java
└── CenaflixpodcastWebApplication.java

src/main/resources/
├── templates/ # Views Thymeleaf
│ ├── podcasts/
│ │ ├── listar.html
│ │ ├── novo.html
│ │ └── editar.html
│ ├── usuarios/
│ │ ├── listar.html
│ │ ├── novo.html
│ │ └── editar.html
│ ├── dashboard.html
│ ├── login.html
│ └── error/
├── static/ # Assets estáticos
│ ├── css/
│ ├── js/
│ └── images/
└── application.properties

text

## 🛠️ Instalação e Configuração

### **Pré-requisitos**
- Java 17 ou superior
- MySQL 8.0+
- Maven 3.8+
- Git

### **1. Clone o repositório**
```bash
git clone https://github.com/gersonmachado72/cenaflixpodcast-web.git
cd cenaflixpodcast-web
2. Configure o banco de dados
sql
CREATE DATABASE cenaflixpodcast;
CREATE USER 'cenaflix_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON cenaflixpodcast.* TO 'cenaflix_user'@'localhost';
FLUSH PRIVILEGES;
3. Configure o application.properties
properties
# src/main/resources/application.properties
spring.application.name=cenaflixpodcast-web

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/cenaflixpodcast
spring.datasource.username=cenaflix_user
spring.datasource.password=senha_segura
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Server
server.port=8081
server.servlet.context-path=/

# Logging
logging.level.com.cenaflixpodcast=DEBUG
logging.file.name=app.log

# Spring Security
spring.security.user.name=admin
spring.security.user.password=admin123
4. Compile e execute
bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Ou executar o JAR
mvn clean package
java -jar target/cenaflixpodcast-web-0.0.1-SNAPSHOT.jar
5. Acesse a aplicação
🌐 URL: http://localhost:8081

👤 Login: admin / admin123

🔐 Página de login: http://localhost:8081/login

👥 Configuração Inicial de Usuários
Usuários padrão:

Administrador: admin / admin123 (acesso total)

Editor: editor / editor123 (pode gerenciar podcasts)

Visualizador: user / user123 (apenas visualização)

Para criar novos usuários:

Acesse /usuarios/novo

Preencha o formulário

Selecione o perfil adequado

🎙️ Configuração de Podcasts
Formatos suportados:

Áudio: MP3 (recomendado), WAV, M4A

Tamanho máximo: 50MB por arquivo

Estrutura de pastas:

text
uploads/
└── podcasts/
    ├── 2024/
    │   ├── 01/  # Janeiro
    │   └── 12/  # Dezembro
    └── temp/    # Arquivos temporários
🔐 Configuração de Segurança
Rotas públicas:

/ - Página inicial

/login - Autenticação

/usuarios/novo - Cadastro

/static/** - Arquivos estáticos

Rotas protegidas por perfil:

java
// Exemplos de configuração
@PreAuthorize("hasRole('ADMIN')")      // Apenas administradores
@PreAuthorize("hasRole('EDITOR')")     // Editores ou acima
@PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')") // Múltiplos perfis
Criptografia:

Senhas: BCrypt com força 10

Sessões: HTTP-only cookies

CSRF: Proteção ativada

🚀 Scripts Úteis
Iniciar aplicação com todas funcionalidades:

bash
./iniciar_com_todas_funcionalidades.sh
Verificar logs:

bash
tail -f app.log
Limpar arquivos temporários:

bash
mvn clean
rm -rf target/
rm -rf uploads/temp/
🧪 Testes
Testar endpoints:

bash
# Login
curl -X POST http://localhost:8081/login \
  -d "username=admin&password=admin123"

# Listar podcasts
curl -H "Cookie: JSESSIONID=..." http://localhost:8081/podcasts/listar

# Criar usuário
curl -X POST http://localhost:8081/usuarios/salvar \
  -d "nome=NovoUsuario&email=novo@email.com&senha=123456&perfil=VISUALIZADOR"
Testar upload:

bash
curl -X POST -F "arquivo=@podcast.mp3" \
  -F "titulo=Meu Podcast" \
  -F "descricao=Descrição do podcast" \
  -F "genero=TECNOLOGIA" \
  http://localhost:8081/podcasts/upload
📊 Banco de Dados
Tabela podcast:

sql
CREATE TABLE podcast (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    arquivo VARCHAR(500) NOT NULL,
    data_publicacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    genero VARCHAR(50),
    duracao INT,
    likes INT DEFAULT 0,
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);
Tabela usuario:

sql
CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL DEFAULT 'VISUALIZADOR',
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
🐛 Solução de Problemas
Problema: Upload não funciona

properties
# Aumentar limite de upload
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
Problema: Erro de conexão MySQL

properties
# Verificar credenciais
spring.datasource.url=jdbc:mysql://localhost:3306/cenaflixpodcast?useSSL=false&serverTimezone=UTC
Problema: Template não encontrado

bash
# Limpar cache do Thymeleaf
mvn clean compile
rm -rf target/
Problema: Login não funciona

Verificar se senhas estão criptografadas no banco

Verificar se usuário está ativo (ativo = 1)

Verificar logs: tail -f app.log

🔄 Fluxo de Trabalho
Para desenvolvedores:

Clone o repositório

Crie branch: git checkout -b feature/nova-funcionalidade

Faça alterações e commits

Push: git push origin feature/nova-funcionalidade

Crie Pull Request

Para deploy:

bash
# Build
mvn clean package -DskipTests

# Copiar para servidor
scp target/cenaflixpodcast-web-*.jar usuario@servidor:/app/

# Executar no servidor
java -jar cenaflixpodcast-web-*.jar --spring.profiles.active=prod
📈 Monitoramento
Logs importantes:

app.log - Logs da aplicação

access.log - Acessos HTTP (se configurado)

Erros: grep ERROR app.log

Métricas:

Acesse: /actuator/health (se Spring Actuator configurado)

Monitorar: Uso de memória, threads ativas, sessões

🤝 Contribuição
Fork o projeto

Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)

Commit suas mudanças (git commit -m 'Add some AmazingFeature')

Push para a branch (git push origin feature/AmazingFeature)

Abra um Pull Request

📝 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

✨ Agradecimentos
Spring Boot Team

Bootstrap Team

MySQL Community

Todos os contribuidores

📞 Suporte
Issues: GitHub Issues
