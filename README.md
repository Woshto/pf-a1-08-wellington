# PrettyFlights [ES2] - Projeto Modelo para a Atividade A1.08

## Contexto

Este repositório tem fins exclusivamente didáticos, servindo como ferramenta de apoio ao desenvolvimento de competências em Engenharia de Software II ao longo dos conteúdos do plano de ensino da disciplina.

| Item | Descrição |
| --- | --- |
| **Instituição** | Universidade do Vale do Itajaí (Univali) |
| **Curso** | Ciência da Computação |
| **Disciplina** | Engenharia de Software II |
| **Professor** | Roger Anderson Schmidt |
| **Alunos** | Ana Maria e Wellington Moura |


## Regras de Negócio do Projeto Modelo

O projeto modelo implementa a Política de Cancelamento de Voo e Cálculo de Reembolso, que envolve lógica condicional baseada no tempo restante para o voo.

No contexto da plataforma PrettyFlights, um importante cenário de integração é o processo de compra e reserva de passagem. Esse fluxo exige que o componente de negócio (ServicoReservaVoo) interaja com duas dependências simuladas na forma abaixo:

- Banco de Dados (Persistência): Para verificar se a reserva foi salva corretamente e se os assentos disponíveis no voo foram decrementados.

- Gateway de Pagamentos (API): Para simular a integração com um serviço externo de cobrança.

## Arquitetura (Stack de Componentes)

### [JUnit](https://junit.org/)

Framework de testes unitários.

### [Spring Boot Test](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html)

Framework de testes de integração.

### [H2 Database](http://h2database.com/html/main.html)

Banco de dados em memória (*in-memory database*), que dispensa a necessidade de instalação de um banco local como PostgreSQL ou MySQL, reduzindo a complexidade na configuração do ambiente de testes.

### [Maven](https://maven.apache.org/)

Ferramenta de empacotamento (build) do ecossistema Java.

## Comandos para Execução dos Testes

1) Inicializar o projeto:
   ```
   mvn clean install -DskipTests
   ```

2) Rodar **todos** os testes (Unitários e de Integração):
   ```
   mvn test
   ```

3) Rodar apenas **Testes Unitários** (sufixo `Test`):

   Execução mais performática, pois não inicia o contexto do Spring Boot (isolamento das dependências).

   ```
   mvn test -Dtest="*Test"
   ```
4) Rodar apenas **Testes de Integração** (sufixo `IT`):
   ```
   mvn test -Dtest="*IT"
   ```

## Incremento Semanal A1.08 (CADA DUPLA DEVE RESPONDER AOS 4 ITENS)

### 1) Identificação do Requisito

> *RF1 - Cadastro e configuração de portões*
> *RF2 - Alocação automática de portões*
> *RF3 - Realocação manual pelo operador*
> *RNF1 - Log de auditoria*
> *RNF2 - Algoritmo parametrizável*

### 2) Implementação do Requisito

> *PF-A1-08-WELLINGTON/
├── src/
│   ├── main/
    │    └── domain/         
    │    │   ├── AircraftCategory.java
    │    │   ├── AllocationLog.java
    │    │   ├── AllocationStatus.java
    │    │   ├── Gate.java
    │    │   ├── GateAllocation.java
    │    │   ├── GateStatus.java
    │    │   └── Voo.java
    │    ├── repository/
    │    │   ├── AllocationLogRepository.java
    │    │   ├── GateAllocationRepository.java
    │    │   ├── GateRepository.java
    │    │   └── VooRepository.java
    │    ├── service/
    │    │   └── AllocationService.java
    │    ├── controller/
    │    │   └── AllocationController.java
    │    └── test/
    │    │   ├── GateTest.java
    │    │   ├── AllocationIntegrationTest.java
    │    │   └── AllocationSystemTest.java
    │    └── main.java
    ├── pom.xml
    └── README.md*

### 3) Codificação dos Testes

> *Popular a seguinte tabela com todos os casos de teste implementados (unitários e de integração):*

| Tipo de Teste | Classe de Teste | Cenário de Teste (Método) | Técnicas Empregadas | Comentários da Dupla sobre a Implementação |
| --- | --- | --- | --- | --- |
| Unitário / Integração |  *Preencher* | *Preencher* | *Identificar __Particionamento de Equivalência__, __Análise de Valor Limite__ ou __Tabela de Transição de Estados__* | *Preencher* |

1. Testes de Unidade (Base do V)
Foco: Testar os métodos isolados de lógica de alocação e validação de compatibilidade do objeto Gate e Flight.

Classe de Teste: GateTest.java

Cenário de Teste	Descrição	Resultado Esperado
shouldReturnFalseWhenFlightCategoryDInGateCategoryB	Voo categoria D em portão categoria B	false (incompatível)
shouldDetectConflictWhenTwoFlightsOverlapInSameGate	Dois voos com janela de tempo sobreposta no mesmo portão	true (conflito detectado)

2. Testes de Integração (Meio do V)
Foco: Testar a comunicação entre o módulo de Alocação e o módulo de Cadastro de Portões.

Classe de Teste: AllocationIntegrationTest.java

Cenário de Teste	Descrição	Critério de Sucesso
shouldAllocateFlightCToGateCAndGenerateLog	Cadastrar portão C, criar voo C, acionar /allocation/run	Retornar ID do portão correto e gerar log com horário, userID e dados

3. Testes de Sistema / End-to-End (Topo do V)
Foco: Validar o fluxo completo de alocação, conflito e realocação manual.

Classe de Teste: AllocationSystemTest.java

Etapa	Descrição	Verificação
a	Cadastrar 10 portões com categorias distintas	10 portões salvos
b	Importar 20 voos com conflito (LA3041 e LA3042)	20 voos salvos
c	Executar alocação automática	Conflito resolvido em portões diferentes
d	Realocar LA3041 para G7 com motivo	Portão G7, motivo registrado
e	Consultar logs do LA3041	Log com estado anterior, posterior, horário e operador

### 4) Declaração de Uso da IA

Esta seção deve ser obrigatoriamente preenchida em todas as atividades práticas 
entregues na disciplina de Engenharia de Software II.

### Nível de Utilização

> Marque a opção que melhor descreve o uso da IA generativa neste trabalho: 

| Seleção | Nível | Descrição |
| -- | --- | --- |
|  | 0 | Não usei IA generativa |
|  | 1 (Assistência) | Usei apenas para correção gramatical, tradução ou formatação de referências. |
|  | 2 (Co-piloto) | Usei para gerar techos de código *boilerplate*, scripts de CI/CD ou sugestão de casos de teste. |
| X| 3 (Consultoria) | Usei para debater decisões arquiteturais ou entender padrões de projeto específicos. |
|  | 4 (Uso Específico) | Apresentar os casos de uso específico. |

### Registro de Prompts

> Cole aqui os principais prompts utilizados, registrando a devida ferramenta e 
versão correspondentes (Exemplos: Gemini, ChatGPT, Claude, GitHub Copilot):

Ferramenta	Versão	   Prompt
ChatGPT	   GPT-4	      "Preciso implementar um sistema de alocação de portões com Spring Boot. Como estruturar as classes de domínio usando JPA e Lombok?"
ChatGPT	   GPT-4	      "Como implementar testes de unidade para validar compatibilidade de categorias de aeronaves usando JUnit 5?"
ChatGPT	   GPT-4	      "Como criar um teste de integração que valide a comunicação entre o módulo de alocação e o repositório de portões?"
ChatGPT	   GPT-4	      "Preciso simular um fluxo completo de alocação com 10 portões e 20 voos. Como estruturar um teste de sistema?"
ChatGPT	   GPT-4	      "Como criar um menu interativo que execute os testes da pasta 'test' programaticamente usando JUnit Launcher?"
ChatGPT	   GPT-4	      "Meu package é br.univali.es2.prettyflights.main.domain. Como ajustar os imports dos repositórios corretamente?"

### Validação Humana

> Descreva  como  você  validou  a  saída  da  IA.  O  que  você  precisou  corrigir  ou 
adaptar para que o resultado fizesse sentido no contexto da atividade? 

A saída da IA foi validada em cada etapa do desenvolvimento:
Estrutura das classes: A IA sugeriu o uso de Lombok com @Data, @NoArgsConstructor e @AllArgsConstructor. Validamos que as anotações estavam corretas para JPA e que os métodos auxiliares (canAccommodate, hasTimeConflictWith) foram implementados manualmente para manter a lógica de negócio.
Testes de unidade: A IA sugeriu cenários de teste. Ajustamos os nomes dos campos para vooNum, chegada e saida para corresponder à nossa implementação, em vez de flightNumber e estimatedArrival.
Testes de integração: A IA gerou um teste que usava TestRestTemplate. Validamos que o endpoint /allocation/run estava configurado corretamente no Controller e que o log era gerado com os dados esperados.
Testes de sistema: A IA sugeriu 20 voos com dados fictícios. Validamos que os voos conflitantes (LA3041 e LA3042) estavam no mesmo horário e que a realocação manual registrava o motivo corretamente.
Menu interativo: A IA sugeriu o uso do Launcher do JUnit 5. Validamos que as classes de teste estavam no package correto e que o menu executava os testes sem reimplementar a lógica.
Principais adaptações realizadas:
Correção dos packages de com.prettyflights.gates.model para br.univali.es2.prettyflights.main.domain
Renomeação de FlightRepository para VooRepository e ajuste do método findByFlightNumber para findByVooNum
Ajuste dos campos estimatedArrival/estimatedDeparture para chegada/saida nas queries JPA
Correção da versão do Java de 25 para 17 no pom.xml
Implementação manual dos métodos de negócio em vez de gerar automaticamente com Lombok