# Firebase API

Aplicação web e API REST, desenvolvida com Spring Boot, para gerenciar envio de push notifications.

## Funcionalidades

A aplicação permite:

- Cadastrar, alterar, remover e listar notificações com título, descrição e uma imagem
- Enviar dados ou notificações com o FCM ([Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging))
- Identificar os dispositivos cadastrados na aplicação

Na API REST podemos:

- Cadastrar dispositivos
- Buscar todos dispositivos
- Envio de mensagem de dados do FCM

## Como executar

Após clonar o projeto, dentro do diretório raiz, execute a task `bootRun` do Gradle. A primeira execução vai realizar o download de todas dependências necessárias.

> O uso do Wrapper é recomendado (gradlew ou gradlew.bat) para garantir a compatibilidade.

Também é possível executar o projeto via arquivo jar gerado pela task `build` que é armazenado no diretório **build/libs/file-name.jar**. Para executar faça o seguinte:

```
java -jar file-name.jar
```

> A build do projeto foi feita com base no Java 8, portanto, é recomendado usar o Java 11 para que tudo funcione como esperado.

## Modificando propriedades de inicialização

Por padrão o Spring Boot vai rodar a aplicação na porta `8080`. Caso queria modificar a porta, edite a seguinte propriedade no arquivo **application-dev.yml**:

```yaml
server:
  port: ${port:8081}
```

Na amostra acima, a aplicação vai operar na porta `8081`.

## Modificando propriedade durante a execução

Também é possível modificar as propriedades via command line durante a execução.

### **Via task `bootRun` do Gradle**

Com o Gradle você pode alterar os valores das propriedades por meio do comando `args`:

```
./gradlew bootRun --args='--server.port=8081'
```

### **Via arquivo jar gerado**

Caso execute pelo arquivo jar:

```
java -jar file-name.jar --server.port=8081
```

## Configurando o FCM

A aplicação web oferece funcionalidades para enviar mensagens de notificação ou dados por meio do FCM. Para isso é necessário configurar o servidor adicionando a chave do Firebase.

Para obter a chave do Firebase, vá até o console:

- Acesse as configurações (**Settings**) contas de serviço (**Service Accounts**)
- Clique em gerar nova chave privata (**Generate New Private Key**) e confirme em gerar chave (**Generate Key**)

Ao fazer esse procedimento, um arquivo JSON será baixado, armazene-o no diretório **firebase** que é criado ao rodar a aplicação no mesmo nível.

Para conferir se a configuração funcionou como esperado, acesse a página de mensagens, se aparecer o formulário para enviar mensagem de dados ou notificação, a configuração foi feita com sucesso.

Caso contrário, algum problema ocorreu, confira do servidor e veja se existe alguma mensagem sobre o possível problema.

## Log de execução

A aplicação registra logs no terminal de acordo com algumas ações realizadas, como acesso aos controllers ou services, que indicam classes, métodos e argumentos executados. 

Caso tiver algum problema, utilize o log para tentar identificar o possível problema e resolvê-lo ou relatar via issues.

## Mapeamento de end-points

Para acessar as funcionalidades por meio da API REST, são disponibilizados os seguintes end-points:

> A URL base da API é `/api`, portanto, todas as requisições mantém esse prefixo.

### Dispositivos

- `/devices`
    - **GET**: busca de todos dispositivos

        ```json
        [
            {
                "modelo": "motorola one zoom",
                "marca": "motorola",
                "token": "cavqLZjrSpaUIuSwx8XfRF:APA91bF8bfU-j2kC3EdPHCGdJtzKxOubJRy7QKeirNkFEofrtnNEBzEb2s7X3_j9xGN2PD5XOIXLJamC0D2Gluuo3Li2VGg0xvDiZNikNfGl-nT7vE24eU0bUay32Warsh4BFj-ZNXCu"
            },
            {
                "modelo": "sdk_gphone_x86",
                "marca": "google",
                "token": "cbp-UkpFRnKIBkEu9e7qzA:APA91bErqfmoaYiSyjYhhB6oyX9UVH25DffpumRbTTe1PxgcZgfRXqVU9-jN6lTtpCclv9QRrLMMa8yU8oTsELMFAgzCJE5dJ2dbKesV0PrtqKVtoxjp46SukSAfh3qT-rukCBmpNWuo"
            }
        ]
        ```

    - **POST**: cadastro de dispositivo
        - Recebe o dispositivo no corpo da requisição no seguinte formato

        ```json
        {
           "marca":"google",
           "modelo":"sdk_gphone_x86",
           "token":"cbp-UkpFRnKIBkEu9e7qzA:APA91bErqfmoaYiSyjYhhB6oyX9UVH25DffpumRbTTe1PxgcZgfRXqVU9-jN6lTtpCclv9QRrLMMa8yU8oTsELMFAgzCJE5dJ2dbKesV0PrtqKVtoxjp46SukSAfh3qT-rukCBmpNWuo"
        }
        ```