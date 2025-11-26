package org.example.Aluno;

import org.example.BancoDados.Banco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    public void salvarAluno(Aluno aluno) throws SQLException {
        String sql = """
            INSERT INTO aluno (
                nome, cpf, rg, sexo, data_nascimento, etnia, endereco, num_casa,
                ponto_referencia, bairro, uf, municipio, sus, mobilidade,
                problema_saude, restricao_alimentar, turma, periodo_cursando, id_responsavel
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;


        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getRg());
            stmt.setString(4, aluno.getSexo());
            stmt.setString(5, aluno.getDataNascimento());
            stmt.setString(6, aluno.getEtnia());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getNumCasa());
            stmt.setString(9, aluno.getPontoReferencia());
            stmt.setString(10, aluno.getBairro());
            stmt.setString(11, aluno.getUf());
            stmt.setString(12, aluno.getMunicipio());
            stmt.setString(13, aluno.getSus());
            stmt.setString(14, aluno.getMobilidade());
            stmt.setString(15, aluno.getProblemaSaude());
            stmt.setString(16, aluno.getRestricaoAlimentar());
            stmt.setString(17, aluno.getTurma());
            stmt.setString(18, aluno.getPeriodoCursando());

            if (aluno.getIdResponsavel() != null) {
                stmt.setLong(19, aluno.getIdResponsavel());
            } else {
                stmt.setNull(19, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                aluno.setIdAluno(generatedKeys.getLong(1));
            }

            if (aluno.getDeficiencias() != null && !aluno.getDeficiencias().isEmpty()) {
                inserirDeficiencias(aluno.getIdAluno(), aluno.getDeficiencias());
            }

            if (aluno.getAlergias() != null && !aluno.getAlergias().isEmpty()) {
                inserirAlergias(aluno.getIdAluno(), aluno.getAlergias());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluno: " + e.getMessage());
        }
    }

    private static void inserirDeficiencias(Long idAluno, List<String> deficiencias) {
        String sql = "INSERT INTO aluno_deficiencias (id_aluno, deficiencia) VALUES (?, ?)";

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String deficiencia : deficiencias) {
                stmt.setLong(1, idAluno);
                stmt.setString(2, deficiencia);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir deficiências: " + e.getMessage());
        }
    }

    private static void inserirAlergias(Long idAluno, List<String> alergias) {
        String sql = "INSERT INTO aluno_alergias (id_aluno, alergia) VALUES (?, ?)";

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String alergia : alergias) {
                stmt.setLong(1, idAluno);
                stmt.setString(2, alergia);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir alergias: " + e.getMessage());
        }
    }

    public static Aluno buscarPorNome(Long id) {
        if (id != null) {
            String sql = """
                    SELECT a.*, r.* 
                    FROM aluno a 
                    LEFT JOIN responsavel r ON a.id_responsavel = r.id_responsavel 
                    WHERE a.id_aluno = ?
                    """;
            Aluno aluno = null;

            try (Connection conn = Banco.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    aluno = mapearAluno(rs);

                    aluno.setDeficiencias(buscarDeficiencias(id));

                    aluno.setAlergias(buscarAlergias(id));
                }

            } catch (SQLException e) {
                System.out.println("Erro ao buscar aluno: " + e.getMessage());
            }

            return aluno;
        }

        return null;
    }

    private static Aluno mapearAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setIdAluno(rs.getLong("id_aluno"));
        aluno.setNome(rs.getString("nome"));
        aluno.setCpf(rs.getString("cpf"));
        aluno.setRg(rs.getString("rg"));
        aluno.setSexo(rs.getString("sexo"));
        aluno.setDataNascimento(rs.getString("data_nascimento"));
        aluno.setEtnia(rs.getString("etnia"));
        aluno.setEndereco(rs.getString("endereco"));
        aluno.setNumCasa(rs.getString("num_casa"));
        aluno.setPontoReferencia(rs.getString("ponto_referencia"));
        aluno.setBairro(rs.getString("bairro"));
        aluno.setUf(rs.getString("uf"));
        aluno.setMunicipio(rs.getString("municipio"));
        aluno.setSus(rs.getString("sus"));
        aluno.setMobilidade(rs.getString("mobilidade"));
        aluno.setProblemaSaude(rs.getString("problema_saude"));
        aluno.setRestricaoAlimentar(rs.getString("restricao_alimentar"));
        aluno.setTurma(rs.getString("turma"));
        aluno.setPeriodoCursando(rs.getString("periodo_cursando"));
        aluno.setIdResponsavel(rs.getLong("id_responsavel"));

        return aluno;
    }

    private static List<String> buscarDeficiencias(Long idAluno) {
        String sql = "SELECT deficiencia FROM aluno_deficiencias WHERE id_aluno = ?";
        List<String> deficiencias = new ArrayList<>();

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                deficiencias.add(rs.getString("deficiencia"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar deficiências: " + e.getMessage());
        }

        return deficiencias;
    }

    private static List<String> buscarAlergias(Long idAluno) {
        String sql = "SELECT alergia FROM aluno_alergias WHERE id_aluno = ?";
        List<String> alergias = new ArrayList<>();

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                alergias.add(rs.getString("alergia"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar alergias: " + e.getMessage());
        }

        return alergias;
    }

    public static List<Aluno> buscarTodos() {
        String sql = """
            SELECT a.*, r.* 
            FROM aluno a 
            LEFT JOIN responsavel r ON a.id_responsavel = r.id_responsavel
            """;
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = Banco.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno aluno = mapearAluno(rs);
                aluno.setDeficiencias(buscarDeficiencias(aluno.getIdAluno()));
                aluno.setAlergias(buscarAlergias(aluno.getIdAluno()));
                alunos.add(aluno);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar alunos: " + e.getMessage());
        }

        return alunos;
    }

    public static void atualizarAluno(Aluno aluno) {
        String sql = """
            UPDATE aluno SET 
                nome = ?, cpf = ?, rg = ?, sexo = ?, data_nascimento = ?, 
                etnia = ?, endereco = ?, num_casa = ?, ponto_referencia = ?, 
                bairro = ?, uf = ?, municipio = ?, sus = ?, mobilidade = ?, 
                problema_saude = ?, restricao_alimentar = ?, turma = ?, 
                periodo_cursando = ?, id_responsavel = ?
            WHERE id_aluno = ?
            """;

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getRg());
            stmt.setString(4, aluno.getSexo());
            stmt.setString(5, aluno.getDataNascimento());
            stmt.setString(6, aluno.getEtnia());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getNumCasa());
            stmt.setString(9, aluno.getPontoReferencia());
            stmt.setString(10, aluno.getBairro());
            stmt.setString(11, aluno.getUf());
            stmt.setString(12, aluno.getMunicipio());
            stmt.setString(13, aluno.getSus());
            stmt.setString(14, aluno.getMobilidade());
            stmt.setString(15, aluno.getProblemaSaude());
            stmt.setString(16, aluno.getRestricaoAlimentar());
            stmt.setString(17, aluno.getTurma());
            stmt.setString(18, aluno.getPeriodoCursando());

            if (aluno.getIdResponsavel() != null) {
                stmt.setLong(19, aluno.getIdResponsavel());
            } else {
                stmt.setNull(19, Types.INTEGER);
            }

            stmt.setLong(20, aluno.getIdAluno());
            stmt.executeUpdate();

            atualizarDeficiencias(aluno.getIdAluno(), aluno.getDeficiencias());
            atualizarAlergias(aluno.getIdAluno(), aluno.getAlergias());

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    private static void atualizarDeficiencias(Long idAluno, List<String> deficiencias) {
        String deleteSql = "DELETE FROM aluno_deficiencias WHERE id_aluno = ?";
        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setLong(1, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao remover deficiências: " + e.getMessage());
        }

        if (deficiencias != null && !deficiencias.isEmpty()) {
            inserirDeficiencias(idAluno, deficiencias);
        }
    }

    private static void atualizarAlergias(Long idAluno, List<String> alergias) {
        String deleteSql = "DELETE FROM aluno_alergias WHERE id_aluno = ?";
        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            stmt.setLong(1, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao remover alergias: " + e.getMessage());
        }

        if (alergias != null && !alergias.isEmpty()) {
            inserirAlergias(idAluno, alergias);
        }
    }

    public void deletarAluno(Long id) {
        String sql = "DELETE FROM aluno WHERE id_aluno = ?";

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar aluno: " + e.getMessage());
        }
    }

    public static Aluno buscarPorNome(String nome) {
        String sql = """
            SELECT a.*, r.* 
            FROM aluno a 
            LEFT JOIN responsavel r ON a.id_responsavel = r.id_responsavel 
            WHERE a.nome = ?
            """;
        Aluno aluno = null;

        try (Connection conn = Banco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = mapearAluno(rs);
                aluno.setDeficiencias(buscarDeficiencias(aluno.getIdAluno()));
                aluno.setAlergias(buscarAlergias(aluno.getIdAluno()));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno por nome: " + e.getMessage());
        }

        return aluno;
    }

}
