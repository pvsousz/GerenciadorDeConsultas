
# Gestão de Consultas

Sistema de gerenciamento de consultas médicas, desenvolvido inteiramente para a disciplina de POO.
Funções de CREATE(),UPDATE(),READ() E DELETE()




## 🚀 Começando
Copie e clone o repositório localmente:
```bash
https://github.com/pvsousz/GerenciadorDeConsultas.git
```


## 📋 Pré-Requisitos
- Java instalado [21]
- MySQL DB
- Framework Spring Boot
##  🛠️ Feito com

- [Java](https://www.oracle.com/java/technologies/downloads/) [21]
- Spring Boot [3.4.2]
    - Dependencias:
        - devtools
        - mysql-conector
        - lombok
        - security
        - jpa
- [MySQL](https://www.mysql.com/downloads/)
    - WorkBench
- [Maven](https://maven.apache.org/)


## ✒️ Autor
* **Paulo Vinicius** - *Trabalho Inicial* - [pvsousz](https://github.com/pvsousz)
* **Paulo Vinicius** - *Documentação* - [pvsousz](https://github.com/pvsousz)

## Funcionalidades

- Create ✅
- Read 👓
- Update 🚀
- Delete ❌


## ⚙️ Ajustes

Vá no diretório da aplicação:
```bash
\resources\aplication.properties
```

```bash
spring.application.name=GestaoConsultas
spring.datasource.url=jdbc:mysql://localhost:3306/gestao_consultas?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=290429
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

↘️ Substitua pelo seu endereço, username e password do seu banco.
```bash
    spring.datasource.url=
    spring.datasource.username=
    spring.datasource.password=
``` 

✅  Certifique-se de usar os acessos corretos
## 🚀 Começando
Copie e clone o repositório localmente:
```bash
https://github.com/pvsousz/GerenciadorDeConsultas.git
```


## 🔃 Rodar projeto

🔃 Rodar o projeto -> Execute a classe
```bash
\main\java\com\gestao\consultas\GestãoConsultasApplication
```



# 📋Documentação


## Classe de execução
    @SpringBootApplication
    public class GestaoConsultasApplication implements CommandLineRunner {

	@Autowired
	private TerminalInterface terminalInterface;

	public static void main(String[] args) {
		SpringApplication.run(GestaoConsultasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		terminalInterface.iniciar();
	}
Aqui podemos ver a classe de execução criada por padrão do spring SpringApplication, logo acima temos uma injeção de dependência com @Autowired para ser possível chamar o atributo do tipo TerminalInterface que por sua vez executa um comando de iniciar a interface.


> ***Observação***
Foram feitas algumas adaptações como não foi utilizado uma GUI e sim uma interface de Terminal CLI foi  preciso implementar ComanddLineRunner.

## Classes Models

Na pasta model, teremos três classes principais para o funcionamento de todo o sistema, Consulta, Medico e Paciente. Elas que irão dar vida ao banco de dados utilizando Spring Boot com a anotação @Entity. E também com todos os seus métodos acessores que serão bastante úteis no sistema como todo.

    @Entity
    public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String crmMedico;
    private LocalDate dataConsulta;
    private LocalTime horarioConsulta;
    private String cpfPaciente;
    private Boolean confirmada;
    private String identificadorUnico;
    }

    @Entity
    public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String crm;
    private String cpf;
    private String telefone;
    private String email;
    private String especialidade;
    }

    @Entity
    public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String cpf;
    private String dataNascimento;
    private String telefone;
    private String email;
    private String planoSaude;
    }

## 🚦 SecurityConfig
São configurações criadas para modelos de acessos como roles, o que foi expressamente pedido nos requisitos do projeto. Foram criadas três roles. Admin, Medico e paciente.



        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin@IFCE#2022"))
                .roles("ADMIN")
                .build();

        UserDetails medico = User.withUsername("medico")
                .password(passwordEncoder().encode("medico123"))
                .roles("MEDICO")
                .build();

        UserDetails paciente = User.withUsername("paciente")
                .password(passwordEncoder().encode("paciente123"))
                .roles("PACIENTE")
                .build();

Esses são os logins e senhas configuradas para acesso e permissões respectivas.

## 📄 Classes do pacote CLI

Foi criado uma classe TerminalInterface para centralizar as demandas de menu de interface ou seja cada classe consulta, medico e paciente tem seu menu proprio bem definido e importado.

***TerminalInterface***
    
    public void iniciar() {

        if (autenticaService.autenticarUsuario()) {
            exibirMenuPrincipal();
        } else {
            System.out.println("Autenticação falhou. Tente novamente.");
            autenticaService.autenticarUsuario();
      }
    }

Acima vemos a implementação do código iniciar que foi visto na classe de execução SpringApplication.



    private void exibirMenuPrincipal()
    private void menuPacientes()
    private void menuMedicos()
    private void menuConsultas()
    private void visualizarConsultas()
    private void visualizarConsultasAdmin()
    private void visualizarConsultasPaciente()
    private void visualizarConsultasMedico()
    private boolean temPermissao(String role)

Todos esses metodos chamam outros metodos para desenhar as opções de menus com funcionalidade e funcionar logicamente. Ponto importante o método ->
    
    private void visualizarConsultas()

Une:

    private void visualizarConsultasAdmin()
    private void visualizarConsultasPaciente()
    private void visualizarConsultasMedico()

Por último:

 
    private boolean temPermissao(String role)

Esse método faz a função de verificação de qual tipo de acesso está sendo logado e suas respectivas atribuições

## 🏦 Repository

No Spring Boot para o funcionamento do @Autowide deve-se ser criado o repository, que nada mais é a conexão com o banco e a criação da tabela conforme a definição da @Entity. Foram criados três repository.

***ConsultaRepository***

    @Repository
    public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByCrmMedicoAndDataConsulta(String crmMedico, LocalDate dataConsulta);
    List<Consulta> findByCpfPaciente(String cpfPaciente);
    List<Consulta> findByCrmMedico(String crmMedico);
    List<Consulta> findByCrmMedicoAndDataConsultaBetween(String crmMedico, LocalDate startDate, LocalDate endDate);
    List<Consulta> findByDataConsultaBetween(LocalDate start, LocalDate end);

}

***MedicoRepository***

    @Repository
    public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Medico findByCrm(String crm);
}

***PacienteRepository***

    @Repository
    public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByCpf(String cpf);
}


Essas são todas as consultas e tipos de consultas que eu precisei criar para fazer funcionar a conexão da interface com o banco de dados e ser entregue os dados respectivos. Vale salientar o extends em cada interface ***JpaRepository*** que foi a dependencia instalada no projeto para que fosse possível fazer essa conexão fácil. 

## ⚙️ Service

O service é baiscamente o pacote onde terão as classes que farão realmente a conexão com o banco de dados. Ou seja toda a lógica bruta. Foram criadas cinco classes services.

- AutenticaService
- ConsultaService
- MedicoService
- PacienteService


***AutenticaService***

Ela irá fazer a lógica da autenticação da classe que vimos TerminalInterface no metodo iniciar onde iremos fazer o primeiro contato, a autenticação.


    public void iniciar() {

        if (autenticaService.autenticarUsuario()) {
            exibirMenuPrincipal();
        } else {
            System.out.println("Autenticação falhou. Tente novamente.");
            autenticaService.autenticarUsuario();
      }
    }

    public boolean autenticarUsuario()


***ConsultaService***

Fará toda a implementação real da classe ConsultaCLI fazendo assim o funcionamento da classe por inteiro. Aqui teremos os metodos.


```bash
public Consulta cadastrarConsulta
public Consulta cadastrarConsultaCli
public void atualizarConsulta()
public void atualizarConsultaCli()
public void deletarConsulta
public Consulta buscarConsultaPorId()
public List<Consulta> listarConsultas()

```

Foi criado a ConsultaCli para refatorar código e melhorar a leitura do código assim criando outra classe. Essa classe assite a funcionaliade três do CRUD de gerenciar consulta.


***MedicoService***

    public Medico cadastrarMedico()
    public Medico atualizarMedico()
    public void deletarMedico()
    public Medico buscarMedicoPorCrm()
    public List<Medico> listarMedicos()

Assim como a clasase consulta, fará toda lógica com o banco, tornando viva a MedicoCli.



***PacienteService***

    public Paciente cadastrarPaciente()
    public Paciente atualizarPaciente()
    public void deletarPaciente()
    public Paciente buscarMPacientePorCpf()
    public List<Paciente> listarMPacientes()

Também efetua os processos e implementa os codigos de interface.


***VisualizarService***

A classe VisualizarService se responsabiliza pela quarta funcionalidade pedida nos requisitos do trabalho. Faz a visualização conforme a permissão que a role dispôe. E também toda a lógica pesada de criação, atualização, leitura e remoção. De todas as classes models

```bash
public void visualizarConsultaPacienteService()

public void visualizarConsultaAdminService()

public void visualizarConsultasMedicoService()

```

