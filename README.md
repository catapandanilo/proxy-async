# proxy-async API

Este projeto fornece uma API que implementa um proxy para direcionamento HTTP. A aplicação é construída usando o Quarkus, o Supersonic Subatomic Java Framework, e utiliza a extensão vertx-web-client para efetuar chamadas HTTP de forma assíncrona.

## Classes Principais

- **ProxyResource**: Responsável por receber as requisições HTTP, construir a URL de destino e reencaminhar a requisição para o servidor de destino.
- **WebClientProducer**: Produz uma instância do WebClient que é injetada na classe `ProxyResource`.

## Configuração

As configurações do projeto estão no arquivo `application.properties`. Você pode alterar as seguintes propriedades:

- `HOST_PROXY`: O host do servidor de destino. (Padrão: `seguradora.com.br`)
- `HOST_PORT`: A porta do servidor de destino. (Padrão: `443`)
- `LOG_LEVEL`: Define o nível de log da aplicação. (Padrão: `INFO`)

## Dependências

As principais dependências do projeto incluem:

- quarkus-vertx
- quarkus-resteasy-reactive
- quarkus-reactive-routes
- quarkus-arc
- vertx-web-client (Versão 4.4.4)

## Como Executar

1. Clone o repositório.
2. Navegue até o diretório do projeto e execute: `./mvnw quarkus:dev`
3. A API estará disponível na porta padrão do Quarkus (8080).

## Contribuição

Sinta-se à vontade para contribuir com este projeto. Abra um pull request ou crie uma issue para discussão.
