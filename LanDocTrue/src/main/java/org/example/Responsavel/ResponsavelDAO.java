package org.example.Responsavel;

import org.example.BancoDados.Banco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponsavelDAO {
    public void salvarResponsavel(Responsavel responsavel) throws SQLException {
        String sql = """
            INSERT INTO responsavel (
                nome, cpf, rg, sexo, data_nascimento, etnia, email, contato,
                contato2, parentesco, responsavel_legal, renda_bruta,
                auxilio_governo, emprego, local_trabalho
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, responsavel.getNome());
            stmt.setString(2, responsavel.getCpf());
            stmt.setString(3, responsavel.getRg());
            stmt.setString(4, responsavel.getSexo());
            stmt.setString(5, responsavel.getDataNascimento());
            stmt.setString(6, responsavel.getEtnia());
            stmt.setString(7, responsavel.getEmail());
            stmt.setString(8, responsavel.getContato());
            stmt.setString(9, responsavel.getContato2());
            stmt.setString(10, responsavel.getParentesco());
            stmt.setBoolean(11, responsavel.isResponsavelLegal());
            stmt.setString(12, responsavel.getRendaBruta());
            stmt.setString(13, responsavel.getAuxilioGoverno());
            stmt.setString(14, responsavel.getEmprego());
            stmt.setString(15, responsavel.getLocalTrabalho());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                responsavel.setIdResponsavel(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir responsável: " + e.getMessage());
        }

    }

    public static Responsavel buscarPorId(Long id) {
        String sql = "SELECT * FROM responsavel WHERE id_responsavel = ?";
        Responsavel responsavel = null;

        try (Connection conn = Banco.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                responsavel = new Responsavel();
                responsavel.setIdResponsavel(rs.getLong("id_responsavel"));
                responsavel.setNome(rs.getString("nome"));
                responsavel.setCpf(rs.getString("cpf"));
                responsavel.setRg(rs.getString("rg"));
                responsavel.setSexo(rs.getString("sexo"));
                responsavel.setDataNascimento(rs.getString("data_nascimento"));
                responsavel.setEtnia(rs.getString("etnia"));
                responsavel.setEmail(rs.getString("email"));
                responsavel.setContato(rs.getString("contato"));
                responsavel.setContato2(rs.getString("contato2"));
                responsavel.setParentesco(rs.getString("parentesco"));
                responsavel.setResponsavelLegal(rs.getBoolean("responsavel_legal"));
                responsavel.setRendaBruta(rs.getString("renda_bruta"));
                responsavel.setAuxilioGoverno(rs.getString("auxilio_governo"));
                responsavel.setEmprego(rs.getString("emprego"));
                responsavel.setLocalTrabalho(rs.getString("local_trabalho"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar responsável: " + e.getMessage());
        }

        return responsavel;
    }

    public static List<Responsavel> buscarTodos() {
        String sql = "SELECT * FROM responsavel";
        List<Responsavel> responsaveis = new ArrayList<>();

        try (Connection conn = Banco.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Responsavel responsavel = new Responsavel();
                responsavel.setIdResponsavel(rs.getLong("id_responsavel"));
                responsavel.setNome(rs.getString("nome"));
                responsavel.setCpf(rs.getString("cpf"));
                responsavel.setRg(rs.getString("rg"));
                responsavel.setSexo(rs.getString("sexo"));
                responsavel.setDataNascimento(rs.getString("data_nascimento"));
                responsavel.setEtnia(rs.getString("etnia"));
                responsavel.setEmail(rs.getString("email"));
                responsavel.setContato(rs.getString("contato"));
                responsavel.setContato2(rs.getString("contato2"));
                responsavel.setParentesco(rs.getString("parentesco"));
                responsavel.setResponsavelLegal(rs.getBoolean("responsavel_legal"));
                responsavel.setRendaBruta(rs.getString("renda_bruta"));
                responsavel.setAuxilioGoverno(rs.getString("auxilio_governo"));
                responsavel.setEmprego(rs.getString("emprego"));
                responsavel.setLocalTrabalho(rs.getString("local_trabalho"));

                responsaveis.add(responsavel);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar responsáveis: " + e.getMessage());
        }

        return responsaveis;
    }

    public void atualizarResponsavel(Responsavel responsavel) {
        String sql = """
            UPDATE responsavel SET 
                nome = ?, cpf = ?, rg = ?, sexo = ?, data_nascimento = ?, 
                etnia = ?, email = ?, contato = ?, contato2 = ?, parentesco = ?, 
                responsavel_legal = ?, renda_bruta = ?, auxilio_governo = ?, 
                emprego = ?, local_trabalho = ?
            WHERE id_responsavel = ?
            """;

        try (Connection conn = Banco.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, responsavel.getNome());
            pstmt.setString(2, responsavel.getCpf());
            pstmt.setString(3, responsavel.getRg());
            pstmt.setString(4, responsavel.getSexo());
            pstmt.setString(5, responsavel.getDataNascimento());
            pstmt.setString(6, responsavel.getEtnia());
            pstmt.setString(7, responsavel.getEmail());
            pstmt.setString(8, responsavel.getContato());
            pstmt.setString(9, responsavel.getContato2());
            pstmt.setString(10, responsavel.getParentesco());
            pstmt.setBoolean(11, responsavel.isResponsavelLegal());
            pstmt.setString(12, responsavel.getRendaBruta());
            pstmt.setString(13, responsavel.getAuxilioGoverno());
            pstmt.setString(14, responsavel.getEmprego());
            pstmt.setString(15, responsavel.getLocalTrabalho());
            pstmt.setLong(16, responsavel.getIdResponsavel());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar responsável: " + e.getMessage());
        }
    }

    public void deletarResponsavel(Long id) {
        String sql = "DELETE FROM responsavel WHERE id_responsavel = ?";

        try (Connection conn = Banco.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar responsável: " + e.getMessage());
        }
    }

}




