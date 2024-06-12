package br.unipar;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @BeforeEach
    void setUp() {
        Main.criarTabelaUsuario();
    }

    @Test
    void testInserirUsuario() {
        Main.inserirUsuario("testUser", "testPass", "Test Name", "1990-01-01");
        try (Connection conn = Main.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE username = 'testUser'")) {
            assertTrue(rs.next());
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testAlterarUsuario() {
        Main.inserirUsuario("testUser", "testPass", "Test Name", "1990-01-01");
        Main.alterarUsuario(1, "updatedUser", "updatedPass", "Updated Name", "1991-01-01");
        try (Connection conn = Main.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE username = 'updatedUser'")) {
            assertTrue(rs.next());
            assertEquals("updatedPass", rs.getString("password"));
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testListarTodosUsuarios() {
        Main.inserirUsuario("testUser", "testPass", "Test Name", "1990-01-01");
        assertDoesNotThrow(Main::listarTodosUsuarios);
    }

    @Test
    void testExcluirUsuario() {
        Main.inserirUsuario("testUser", "testPass", "Test Name", "1990-01-01");
        Main.excluirUsuario(1);
        try (Connection conn = Main.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE CODIGO = 1")) {
            assertFalse(rs.next());
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }
}
