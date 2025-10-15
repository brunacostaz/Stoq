# Stoq

📦 Sistema de Gestão de Estoque em Laboratórios  

## 📖 Visão Geral  
O **Stoq** é um sistema desenvolvido em **Java** com conexão a banco de dados **Oracle**, cujo objetivo é melhorar o **controle de estoque em laboratórios de diagnóstico**.  
Ele garante maior **visibilidade, rastreabilidade e automação** no consumo de insumos (materiais, reagentes e descartáveis), evitando perdas por falta ou excesso.  

O sistema foi desenhado para resolver problemas comuns em laboratórios:  
- Baixa visibilidade do consumo de materiais.  
- Controle manual suscetível a erros.  
- Dificuldade em prever reposição e validade de insumos.  
- Falta de automação em processos críticos como pedidos, retirada e validação de materiais.  

---

## 🏗️ Arquitetura  
O projeto segue uma arquitetura **em camadas**:  

- **`domain.model`** → Representa as entidades do sistema (Laboratório, Funcionário, Material, Pedido, Estoque etc).  
- **`infra.dao`** → Camada de acesso a dados (DAO), responsável por CRUD no Oracle.  
- **`domain.service`** → Camada de regras de negócio, onde estão os serviços que orquestram operações (ex.: geração de QR Code, monitoramento de estoque, pedidos automáticos).  
- **`infra.db`** → Classe utilitária `OracleConnectionFactory`, que gerencia as conexões com o banco.  
- **`App.java`** → Classe inicial de teste/execução para validar as operações.  

---

## ⚙️ Funcionalidades Principais  

### 🔐 Cadastro  
- **Funcionários** → Administrar usuários (Admin, Gestor, Enfermeiro).  
- **Presets** → Conjunto de materiais necessários para um exame.  
- **Consultas** → Agendamento de exames vinculados a presets.  

### 📦 Estoque  
- **Movimentações** → Entrada, saída e ajustes de materiais.  
- **Histórico diário** → Registro consolidado de estoques por dia.  
- **Alertas automáticos**:  
  - Materiais abaixo do estoque mínimo.  
  - Validade próxima do vencimento.  

### 🧾 Pedidos  
- **Geração automática de pedidos** quando materiais atingem o mínimo.  
- **Aprovação e envio ao fornecedor** pelo gestor.  
- **Recebimento e atualização automática no estoque**.  

### 📲 QR Code  
- **Geração de QR Code** pelo enfermeiro para retirada de insumos de uma consulta.  
- **Validação pelo admin**, que libera os insumos e atualiza o estoque.  

### 📊 Analytics  
Relatórios e métricas para o gestor:  
- Retirada e reposição ao longo do tempo.  
- Frequência de retiradas por enfermeiro.  
- Materiais mais usados na semana.  
- Dias/horários de maior movimento.  
- Taxa de materiais vencidos descartados.  
- Tempo médio de entrega de pedidos.  
- Comparação entre estoque mínimo e uso real.  

---

## 🛠️ Tecnologias Utilizadas  
- **Java 17+**  
- **JDBC (DriverManager)**  
- **Oracle Database**  
- **DAO Pattern**  
- **PlantUML** (para diagramas de classe e arquitetura)  

---

## ▶️ Como Rodar o Projeto  

### Pré-requisitos  
- **Java JDK 17+**  
- **Oracle Database** configurado (ou conexão disponível).  
- Variáveis de ambiente:  
  - `ORACLE_URL` → String de conexão (ex.: `jdbc:oracle:thin:@host:1521:orcl`).  
  - `ORACLE_USER` → Usuário do banco.  
  - `ORACLE_PASSWORD` → Senha do banco.  

### Passos  
1. Clone este repositório:  
   ```bash
   git clone https://github.com/seu-usuario/stoq.git
   cd stoq
