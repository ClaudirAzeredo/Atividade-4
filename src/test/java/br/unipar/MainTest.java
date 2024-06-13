package br.unipar;

import org.junit.jupiter.api.*;

import java.sql.*;

class MainTest {

    private static final String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    private static final String user = "postgres";
    private static final String password = "admin123";

    @BeforeAll
    static void setUp() {
        criarTabelaUsuario();
        criarTabelaCliente();
        criarTabelaProduto();
        criarTabelaVenda();
    }

    @AfterAll
    static void tearDown() {
        excluirTabelaVenda();
        excluirTabelaProduto();
        excluirTabelaCliente();
        excluirTabelaUsuario();
    }

    @Test
    void testInserirUsuario() {
        inserirUsuario("taffe", "12345", "fabio", "1890-01-01");
    }

    @Test
    void testAtualizarUsuario() {

    }

    @Test
    void testExcluirUsuario() {

    }

    @Test
    void testListarUsuarios() {

    }

    @Test
    void testInserirCliente() {
        inserirCliente("Cliente1", "Endereco1");
    }

    @Test
    void testAtualizarCliente() {

    }

    @Test
    void testExcluirCliente() {

    }

    @Test
    void testListarClientes() {

    }

    @Test
    void testInserirProduto() {
        inserirProduto("Produto1", 10.0);
    }

    @Test
    void testAtualizarProduto() {

    }

    @Test
    void testExcluirProduto() {

    }

    @Test
    void testListarProdutos() {

    }

    @Test
    void testInserirVenda() {
        inserirVenda(1, 1, 1);
    }

    @Test
    void testAtualizarVenda() {

    }

    @Test
    void testExcluirVenda() {

    }

    @Test
    void testListarVendas() {

    }

    private static void criarTabelaUsuario() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "codigo SERIAL PRIMARY KEY,"
                + "username VARCHAR(50) UNIQUE NOT NULL,"
                + "password VARCHAR(300) NOT NULL,"
                + "nome VARCHAR(300) NOT NULL,"
                + "nascimento DATE)";
        executeSQL(sql);
    }

    private static void excluirTabelaUsuario() {
        String sql = "DROP TABLE IF EXISTS usuarios";
        executeSQL(sql);
    }

    private void inserirUsuario(String username, String password, String nome, String nascimento) {
        String sql = "INSERT INTO usuarios (username, password, nome, nascimento) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, nome);
            pstmt.setDate(4, Date.valueOf(nascimento));
            pstmt.executeUpdate();
            System.out.println("USUÁRIO INSERIDO");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void criarTabelaCliente() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes ("
                + "id SERIAL PRIMARY KEY,"
                + "nome VARCHAR(100) NOT NULL,"
                + "endereco VARCHAR(300) NOT NULL)";
        executeSQL(sql);
    }

    private static void excluirTabelaCliente() {
        String sql = "DROP TABLE IF EXISTS clientes";
        executeSQL(sql);
    }

    private void inserirCliente(String nome, String endereco) {
        String sql = "INSERT INTO clientes (nome, endereco) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, endereco);
            pstmt.executeUpdate();
            System.out.println("CLIENTE INSERIDO");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void criarTabelaProduto() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos ("
                + "id SERIAL PRIMARY KEY,"
                + "nome VARCHAR(100) NOT NULL,"
                + "preco DECIMAL(10, 2) NOT NULL)";
        executeSQL(sql);
    }

    private static void excluirTabelaProduto() {
        String sql = "DROP TABLE IF EXISTS produtos";
        executeSQL(sql);
    }

    private void inserirProduto(String nome, double preco) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.executeUpdate();
            System.out.println("PRODUTO INSERIDO");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void criarTabelaVenda() {
        String sql = "CREATE TABLE IF NOT EXISTS vendas ("
                + "id SERIAL PRIMARY KEY,"
                + "id_cliente INTEGER NOT NULL REFERENCES clientes(id),"
                + "id_produto INTEGER NOT NULL REFERENCES produtos(id),"
                + "quantidade INTEGER NOT NULL)";
        executeSQL(sql);
    }

    private static void excluirTabelaVenda() {
        String sql = "DROP TABLE IF EXISTS vendas";
        executeSQL(sql);
    }

    private void inserirVenda(int idCliente, int idProduto, int quantidade) {
        String sql = "INSERT INTO vendas (id_cliente, id_produto, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idProduto);
            pstmt.setInt(3, quantidade);
            pstmt.executeUpdate();
            System.out.println("VENDA INSERIDA");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void executeSQL(String sql) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("EXECUTED: " + sql);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
