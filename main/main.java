package br.univali.es2.prettyflights.main;

import br.univali.es2.prettyflights.main.test.AllocationIntegrationTest;
import br.univali.es2.prettyflights.main.test.AllocationSystemTest;
import br.univali.es2.prettyflights.main.test.GateTest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Classe principal do PrettyFlights
 * Contém um menu interativo que executa os testes da pasta test
 */
@SpringBootApplication
public class PrettyFlightsApplication {

    private static ConfigurableApplicationContext context;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   PRETTY FLIGHTS - SISTEMA DE ALOÇÃO   ");
        System.out.println("=========================================");
        
        // Iniciar o Spring Boot
        context = SpringApplication.run(PrettyFlightsApplication.class, args);
        
        // Exibir menu
        exibirMenu();
        
        // Fechar o contexto ao sair
        context.close();
        scanner.close();
    }

    private static void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=========================================");
            System.out.println("              MENU PRINCIPAL             ");
            System.out.println("=========================================");
            System.out.println(" 1 - Teste de Unidade (Base do V)");
            System.out.println(" 2 - Teste de Integração (Meio do V)");
            System.out.println(" 3 - Teste de Sistema (Topo do V)");
            System.out.println(" 4 - Executar TODOS os testes");
            System.out.println(" 0 - Sair");
            System.out.println("=========================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        executarTesteUnidade();
                        break;
                    case 2:
                        executarTesteIntegracao();
                        break;
                    case 3:
                        executarTesteSistema();
                        break;
                    case 4:
                        executarTodosTestes();
                        break;
                    case 0:
                        System.out.println("\nSaindo do sistema... Até logo!");
                        break;
                    default:
                        System.out.println("\n❌ Opção inválida! Escolha 1, 2, 3, 4 ou 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n❌ Digite um número válido!");
            }
        }
    }

    // =============================================
    // EXECUTAR TESTE DE UNIDADE
    // =============================================
    private static void executarTesteUnidade() {
        System.out.println("\n=========================================");
        System.out.println("  EXECUTANDO TESTE DE UNIDADE");
        System.out.println("=========================================");
        System.out.println("Foco: Testar métodos isolados de lógica");
        System.out.println("de alocação e validação de compatibilidade");
        System.out.println("=========================================\n");

        // Redirecionar a saída do JUnit para capturar os resultados
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Criar o launcher do JUnit
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(GateTest.class))
                    .build();

            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.execute(request, listener);

            // Restaurar a saída padrão
            System.setOut(originalOut);

            // Exibir resultados
            TestExecutionSummary summary = listener.getSummary();
            
            System.out.println("📋 RESULTADO DOS TESTES:");
            System.out.println("   - Total de testes: " + summary.getTestsFoundCount());
            System.out.println("   - Executados: " + summary.getTestsStartedCount());
            System.out.println("   - Sucessos: " + summary.getTestsSucceededCount());
            System.out.println("   - Falhas: " + summary.getTestsFailedCount());
            
            if (summary.getTestsFailedCount() == 0) {
                System.out.println("\n✅ TESTE DE UNIDADE: TODOS OS TESTES PASSARAM!");
            } else {
                System.out.println("\n❌ TESTE DE UNIDADE: ALGUNS TESTES FALHARAM!");
                System.out.println("   Detalhes das falhas:");
                summary.getFailures().forEach(failure -> 
                    System.out.println("   - " + failure.getTestIdentifier().getDisplayName() + 
                                     ": " + failure.getException().getMessage())
                );
            }

            // Exibir a saída dos testes
            System.out.println("\n📝 SAÍDA DOS TESTES:");
            System.out.println(outContent.toString());

        } catch (Exception e) {
            System.setOut(originalOut);
            System.out.println("\n❌ ERRO ao executar testes: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    // =============================================
    // EXECUTAR TESTE DE INTEGRAÇÃO
    // =============================================
    private static void executarTesteIntegracao() {
        System.out.println("\n=========================================");
        System.out.println("  EXECUTANDO TESTE DE INTEGRAÇÃO");
        System.out.println("=========================================");
        System.out.println("Foco: Testar comunicação entre módulo de");
        System.out.println("Alocação e módulo de Cadastro de Portões");
        System.out.println("=========================================\n");

        // Redirecionar a saída do JUnit para capturar os resultados
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Criar o launcher do JUnit
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(AllocationIntegrationTest.class))
                    .build();

            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.execute(request, listener);

            // Restaurar a saída padrão
            System.setOut(originalOut);

            // Exibir resultados
            TestExecutionSummary summary = listener.getSummary();
            
            System.out.println("📋 RESULTADO DOS TESTES:");
            System.out.println("   - Total de testes: " + summary.getTestsFoundCount());
            System.out.println("   - Executados: " + summary.getTestsStartedCount());
            System.out.println("   - Sucessos: " + summary.getTestsSucceededCount());
            System.out.println("   - Falhas: " + summary.getTestsFailedCount());
            
            if (summary.getTestsFailedCount() == 0) {
                System.out.println("\n✅ TESTE DE INTEGRAÇÃO: TODOS OS TESTES PASSARAM!");
            } else {
                System.out.println("\n❌ TESTE DE INTEGRAÇÃO: ALGUNS TESTES FALHARAM!");
                System.out.println("   Detalhes das falhas:");
                summary.getFailures().forEach(failure -> 
                    System.out.println("   - " + failure.getTestIdentifier().getDisplayName() + 
                                     ": " + failure.getException().getMessage())
                );
            }

            // Exibir a saída dos testes
            System.out.println("\n📝 SAÍDA DOS TESTES:");
            System.out.println(outContent.toString());

        } catch (Exception e) {
            System.setOut(originalOut);
            System.out.println("\n❌ ERRO ao executar testes: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    // =============================================
    // EXECUTAR TESTE DE SISTEMA
    // =============================================
    private static void executarTesteSistema() {
        System.out.println("\n=========================================");
        System.out.println("  EXECUTANDO TESTE DE SISTEMA");
        System.out.println("=========================================");
        System.out.println("Foco: Validar fluxo completo de alocação,");
        System.out.println("conflito e realocação manual");
        System.out.println("=========================================\n");

        // Redirecionar a saída do JUnit para capturar os resultados
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Criar o launcher do JUnit
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(AllocationSystemTest.class))
                    .build();

            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.execute(request, listener);

            // Restaurar a saída padrão
            System.setOut(originalOut);

            // Exibir resultados
            TestExecutionSummary summary = listener.getSummary();
            
            System.out.println("📋 RESULTADO DOS TESTES:");
            System.out.println("   - Total de testes: " + summary.getTestsFoundCount());
            System.out.println("   - Executados: " + summary.getTestsStartedCount());
            System.out.println("   - Sucessos: " + summary.getTestsSucceededCount());
            System.out.println("   - Falhas: " + summary.getTestsFailedCount());
            
            if (summary.getTestsFailedCount() == 0) {
                System.out.println("\n✅ TESTE DE SISTEMA: TODOS OS TESTES PASSARAM!");
            } else {
                System.out.println("\n❌ TESTE DE SISTEMA: ALGUNS TESTES FALHARAM!");
                System.out.println("   Detalhes das falhas:");
                summary.getFailures().forEach(failure -> 
                    System.out.println("   - " + failure.getTestIdentifier().getDisplayName() + 
                                     ": " + failure.getException().getMessage())
                );
            }

            // Exibir a saída dos testes
            System.out.println("\n📝 SAÍDA DOS TESTES:");
            System.out.println(outContent.toString());

        } catch (Exception e) {
            System.setOut(originalOut);
            System.out.println("\n❌ ERRO ao executar testes: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    // =============================================
    // EXECUTAR TODOS OS TESTES
    // =============================================
    private static void executarTodosTestes() {
        System.out.println("\n=========================================");
        System.out.println("  EXECUTANDO TODOS OS TESTES");
        System.out.println("=========================================\n");

        int totalTests = 0;
        int totalPassed = 0;
        int totalFailed = 0;

        // Executar teste de unidade
        System.out.println("▶️ Executando Teste de Unidade...");
        TestExecutionSummary summary1 = executarTesteClasse(GateTest.class);
        if (summary1 != null) {
            totalTests += summary1.getTestsFoundCount();
            totalPassed += summary1.getTestsSucceededCount();
            totalFailed += summary1.getTestsFailedCount();
        }

        // Executar teste de integração
        System.out.println("\n▶️ Executando Teste de Integração...");
        TestExecutionSummary summary2 = executarTesteClasse(AllocationIntegrationTest.class);
        if (summary2 != null) {
            totalTests += summary2.getTestsFoundCount();
            totalPassed += summary2.getTestsSucceededCount();
            totalFailed += summary2.getTestsFailedCount();
        }

        // Executar teste de sistema
        System.out.println("\n▶️ Executando Teste de Sistema...");
        TestExecutionSummary summary3 = executarTesteClasse(AllocationSystemTest.class);
        if (summary3 != null) {
            totalTests += summary3.getTestsFoundCount();
            totalPassed += summary3.getTestsSucceededCount();
            totalFailed += summary3.getTestsFailedCount();
        }

        // Resumo final
        System.out.println("\n=========================================");
        System.out.println("       RESUMO FINAL DOS TESTES          ");
        System.out.println("=========================================");
        System.out.println("   Total de testes: " + totalTests);
        System.out.println("   ✅ Sucessos: " + totalPassed);
        System.out.println("   ❌ Falhas: " + totalFailed);
        System.out.println("=========================================");
        
        if (totalFailed == 0) {
            System.out.println("✅ TODOS OS TESTES PASSARAM COM SUCESSO!");
        } else {
            System.out.println("❌ ALGUNS TESTES FALHARAM!");
        }
        System.out.println("=========================================");

        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    // =============================================
    // MÉTODO AUXILIAR PARA EXECUTAR UMA CLASSE DE TESTE
    // =============================================
    private static TestExecutionSummary executarTesteClasse(Class<?> testClass) {
        // Redirecionar a saída do JUnit para capturar os resultados
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(testClass))
                    .build();

            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.execute(request, listener);

            // Restaurar a saída padrão
            System.setOut(originalOut);

            // Mostrar saída do teste
            System.out.println(outContent.toString());

            return listener.getSummary();

        } catch (Exception e) {
            System.setOut(originalOut);
            System.out.println("❌ ERRO ao executar teste " + testClass.getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}