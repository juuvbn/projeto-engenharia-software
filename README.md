# :handshake: Cuidar+: Sistema de Intermediação de Serviços Comunitários

Uma plataforma solidária projetada para conectar **clientes** que necessitam de auxílio a **prestadores de serviço** dispostos a oferecer seu trabalho de forma gratuita e voluntária.

> Projeto acadêmico desenvolvido para a disciplina de Laboratório de Engenharia de Software — Faculdade de Computação e Informática (FCI), Universidade Presbiteriana Mackenzie.

## :busts_in_silhouette: Equipe

| Nome | RA |
|---|---|
| Andrey Bezerra Virgínio dos Santos | 10420696 |
| Igor Silva Araujo | 10428505 |
| Julia Vitória Bomfim do Nascimento | 10425604 |
| William Saran dos Santos Junior | 10420128 |

**Orientador:** Prof. Me. Luiz Carlos Machi Lozano

---

## :dart: Sobre o Projeto

O **Cuidar+** é uma plataforma digital de intermediação voltada exclusivamente para serviços de cuidado e assistência comunitária. O objetivo principal é facilitar o acesso a serviços para pessoas sem condições financeiras, digitalizando e organizando uma rede de apoio que já existe de forma latente nas comunidades.

- **Clientes** podem utilizar a plataforma para buscar e filtrar prestadores adequados às suas necessidades, enviar propostas de serviço e acompanhar seu andamento.
- **Prestadores** podem cadastrar suas especialidades técnicas, visualizar solicitações, propor orçamentos (focados em materiais e transporte — mão de obra gratuita) e gerenciar seus serviços.

### Funcionalidades Implementadas

- **Cadastro e Autenticação:** registro e login separados para Clientes e Prestadores, com senhas criptografadas via BCrypt e sessões gerenciadas por tokens JWT.
- **Busca de Prestadores:** filtragem por especialidade (ex.: Eletricista, Babá, Cuidador de Idosos, Passeador de Pets) com paginação e exibição de dados de contato.
- **Fluxo de Negociação em duas etapas:**
  1. Cliente seleciona prestador e descreve o serviço → status `ACEITACAO_PRESTADOR_PENDENTE`
  2. Prestador aceita (definindo data e lista de materiais/custos) ou recusa → status `ACEITACAO_CLIENTE_PENDENTE`
  3. Cliente confirma, recusa ou propõe nova data → status final `ACEITO`, `NEGADO` ou retorno para negociação
  4. Após conclusão → status `FINALIZADO`
- **Gerenciamento de Perfil:** edição de dados cadastrais, senha e especialidades (para prestadores).
- **Controle de Acesso por Papel:** `ROLE_CLIENTE` e `ROLE_PRESTADOR` impedem ações cruzadas entre perfis.
- **Documentação interativa da API** via Swagger UI.

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| **Back-end** | Java, Spring Boot, Spring Security, Spring Data JPA |
| **Front-end** | React, Vite, Tailwind CSS, React Router |
| **Banco de Dados** | MySQL |
| **Modelagem** | PlantUML |

---

## :books: Documentação

### Diagramas

Os diagramas do sistema estão disponíveis em formato PlantUML no diretório [`/diagramas`](/diagramas/):

| Diagrama | Arquivo |
|---|---|
| Casos de Uso | [`casos_de_uso.plantuml`](/diagramas/casos_de_uso.plantuml) |
| Classes de Domínio | [`classes_de_dominio.plantuml`](/diagramas/classes_de_dominio.plantuml) |
| Sequência — Filtrar Prestadores | [`sequencia_filtrar_prestadores_por_tipo_de_servico.plantuml`](/diagramas/sequencia_filtrar_prestadores_por_tipo_de_servico.plantuml) |
| Sequência — Propor Orçamento | [`sequencia_propor_orcamento_ao_cliente.plantuml`](/diagramas/sequencia_propor_orcamento_ao_cliente.plantuml) |
| Sequência — Aceitar Proposta | [`sequencia_aceitar_propostas_de_trabalho.plantuml`](/diagramas/sequencia_aceitar_propostas_de_trabalho.plantuml) |
| Implementação | [`implementacao.plantuml`](/diagramas/implementacao.plantuml) |

Imagens geradas (SVG) ficam em `/out/diagramas/` após compilação com PlantUML.

### Diagrama de Casos de Uso

Visão geral das interações possíveis entre os usuários (atores) e o sistema:

![Diagrama de casos de uso](/out/diagramas/casos_de_uso/casos_de_uso.svg)

### Diagrama de Classes de Domínio

Estrutura conceitual das entidades de negócio e seus relacionamentos:

![Diagrama de classes de domínio](/out/diagramas/classes_de_dominio/classes_de_dominio.svg)

### API REST

A documentação interativa da API é gerada automaticamente pelo Swagger e acessível em:

```
http://localhost:8080/swagger-ui.html
```

---

## :rocket: Instalação

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) 24+
- [Docker Compose](https://docs.docker.com/compose/install/) v2+

```bash
git clone https://github.com/juuvbn/projeto-engenharia-software.git
cd projeto-engenharia-software
docker compose up --build
```

| Serviço | URL |
|---|---|
| Front-end | http://localhost:80 |
| Back-end (API) | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

---

## :blue_book: Como Usar

### Fluxo do Cliente

1. **Cadastro:** acesse a tela de cadastro e escolha o perfil **Cliente**. Informe nome, e-mail, telefone e senha.
2. **Login:** entre com e-mail e senha cadastrados.
3. **Buscar Prestadores:** navegue até "Buscar Serviço", selecione a(s) especialidade(s) desejada(s) e veja a lista de prestadores disponíveis.
4. **Solicitar Serviço:** clique em um prestador e descreva o que você precisa. A proposta é criada com status *Aguardando Prestador*.
5. **Acompanhar em "Meus Serviços":**
   - Quando o prestador aceitar e enviar orçamento, a proposta aparece como *Aguardando sua Confirmação*.
   - Você pode **Aceitar** (serviço agendado), **Recusar** ou **Propor Nova Data**.
6. **Conclusão:** após a realização do serviço, o prestador marca como finalizado.

### Fluxo do Prestador

1. **Cadastro:** acesse a tela de cadastro e escolha o perfil **Prestador**. Informe seus dados e selecione suas especialidades (ex.: Eletricista, Babá, Cuidador de Idosos).
2. **Login:** entre com e-mail e senha cadastrados.
3. **Visualizar Propostas:** em "Meus Serviços", veja as solicitações pendentes enviadas por clientes.
4. **Responder à Proposta:**
   - **Aceitar:** informe a data disponível e adicione os materiais necessários (com quantidade e valor unitário). O sistema calcula o total automaticamente. A mão de obra é sempre gratuita.
   - **Recusar:** a proposta é encerrada.
5. **Após Aceite do Cliente:** você terá acesso aos dados de contato do cliente (telefone, e-mail, endereço) para combinar os detalhes da execução.
6. **Finalizar:** marque o serviço como concluído após a realização.

### Perfil e Configurações

- Tanto clientes quanto prestadores podem editar seus dados cadastrais na tela de **Perfil**.
- Prestadores podem atualizar suas especialidades a qualquer momento.
- A alteração de senha exige confirmação da senha atual.

---

## 🚀 CI/CD Pipeline

Este projeto utiliza uma esteira de integração contínua (CI/CD) para automatizar o processo de build e testes.

### Fluxo da Pipeline

```
Push para GitHub → Webhook → Jenkins (EC2) → Maven build → Testes → Deploy back-end (EC2)
```

1. Um `push` para a branch principal dispara um webhook do GitHub.
2. O Jenkins (hospedado em AWS EC2) recebe o evento e inicia a pipeline.
3. O Maven compila o projeto e executa os testes unitários.
4. Em caso de sucesso, o artefato é implantado automaticamente no servidor de produção.

### Tecnologias utilizadas

- ☁️ **AWS (EC2)** — hospedagem do servidor Jenkins
- ⚙️ **Jenkins** — automação da pipeline
- 📦 **Maven** — build e gerenciamento de dependências
- 🐙 **GitHub** — controle de versão e gatilho via webhook

---

## :triangular_ruler: Especificação e Modelagem

Os casos de uso principais do sistema são:

- **Filtrar Prestadores por Tipo de Serviço** — o cliente seleciona especialidades e o sistema retorna prestadores compatíveis.
- **Propor Orçamento ao Cliente** — o prestador formaliza a proposta com data, materiais e custos de transporte.
- **Aceitar Proposta de Trabalho** — o cliente revisa os termos e confirma, recusa ou negocia nova data.
