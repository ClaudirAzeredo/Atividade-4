package br.unipar;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static final String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    public static final String user = "postgres";
    public static final String password = "admin123";

    public static void main(String[] args) {
        criarTabelaUsuario();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha uma operação: 1 - Inserir, 2 - Alterar, 3 - Listar, 4 - Excluir, 5 - Sair");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Data de Nascimento (YYYY-MM-DD): ");
                    String dataNascimento = scanner.nextLine();
                    inserirUsuario(username, password, nome, dataNascimento);
                    break;
                case 2:
                    System.out.print("Código do usuário para alterar: ");
                    int codigo = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Novo Username: ");
                    String novoUsername = scanner.nextLine();
                    System.out.print("Novo Password: ");
                    String novoPassword = scanner.nextLine();
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Nova Data de Nascimento (YYYY-MM-DD): ");
                    String novaDataNascimento = scanner.nextLine();
                    alterarUsuario(codigo, novoUsername, novoPassword, novoNome, novaDataNascimento);
                    break;
                case 3:
                    listarTodosUsuarios();
                    break;
                case 4:
                    System.out.print("Código do usuário para excluir: ");
                    int codigoExcluir = scanner.nextInt();
                    excluirUsuario(codigoExcluir);
                    break;
                case 5:
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void criarTabelaUsuario() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "CODIGO SERIAL PRIMARY KEY, "
                + "username VARCHAR(50) UNIQUE NOT NULL, "
                + "password VARCHAR(300) NOT NULL, "
                + "nome VARCHAR(50) NOT NULL, "
                + "nascimento DATE)";

        try (Connection conn = connection();
             Statement statement = conn.createStatement()) {

            statement.execute(sql);
            System.out.println("Tabela criada");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void inserirUsuario(String username, String password, String nome, String dataNascimento) {
        String sql = "INSERT INTO usuarios (username, password, nome, nascimento) VALUES (?,?,?,?)";

        try (Connection conn = connection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nome);
            preparedStatement.setDate(4, java.sql.Date.valueOf(dataNascimento));

            preparedStatement.executeUpdate();
            System.out.println("Usuario inserido");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void alterarUsuario(int codigo, String username, String password, String nome, String dataNascimento) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, nome = ?, nascimento = ? WHERE CODIGO = ?";

        try (Connection conn = connection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nome);
            preparedStatement.setDate(4, java.sql.Date.valueOf(dataNascimento));
            preparedStatement.setInt(5, codigo);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usuario atualizado");
            } else {
                System.out.println("Usuário não encontrado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarTodosUsuarios() {
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = connection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int codigo = resultSet.getInt("CODIGO");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String nome = resultSet.getString("nome");
                Date nascimento = resultSet.getDate("nascimento");

                System.out.printf("CODIGO: %d, Username: %s, Password: %s, Nome: %s, Nascimento: %s%n",
                        codigo, username, password, nome, nascimento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void excluirUsuario(int codigo) {
        String sql = "DELETE FROM usuarios WHERE CODIGO = ?";

        try (Connection conn = connection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, codigo);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Usuario excluído");
            } else {
                System.out.println("Usuário não encontrado");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

