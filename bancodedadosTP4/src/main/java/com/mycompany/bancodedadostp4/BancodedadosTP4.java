/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bancodedadostp4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Yanne
 */
public class BancodedadosTP4 {

    private static final String URL = "jdbc:sqlite:tp4bancodedados.db";

    public static void main(String[] args) {
        criarTabelas();
        adicionarAluno("Joao", 25, 1);
        atualizarNomeAluno(1, "Joao Atualizado");
        pesquisarAluno(1);

        Aluno[] alunos = {
                new Aluno("Maria", 22, 2),
                new Aluno("Carlos", 30, 1),
                new Aluno("Ana", 21, 3),
                new Aluno("Lucas", 28, 2),
                new Aluno("Beatriz", 25, 1)
        };

        adicionarListaAlunos(alunos);
        buscarPorSubstring("Luc");
    }

    private static void criarTabelas() {
        try (Connection conexao = DriverManager.getConnection(URL);
             PreparedStatement statement = conexao.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS Aluno (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(50), idade INT, curso_id INT)")) {
            statement.execute();
            System.out.println("Tabelas criadas com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }

    private static void adicionarAluno(String nome, int idade, int cursoId) {
        try (Connection conexao = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = conexao.prepareStatement(
                     "INSERT INTO Aluno (nome, idade, curso_id) VALUES (?, ?, ?)")) {
            conexao.setAutoCommit(false);

            preparedStatement.setString(1, nome);
            preparedStatement.setInt(2, idade);
            preparedStatement.setInt(3, cursoId);

            preparedStatement.executeUpdate();
            conexao.commit();

            System.out.println("Aluno adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }

    private static void adicionarListaAlunos(Aluno[] alunos) {
        try (Connection conexao = DriverManager.getConnection(URL)) {
            conexao.setAutoCommit(false);

            for (Aluno aluno : alunos) {
                adicionarAlunoConexao(conexao, aluno);
            }

            conexao.commit();
            System.out.println("Alunos adicionados com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }

    private static void adicionarAlunoConexao(Connection conexao, Aluno aluno) {
        try (PreparedStatement preparedStatement = conexao.prepareStatement(
                     "INSERT INTO Aluno (nome, idade, curso_id) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, aluno.getNome());
            preparedStatement.setInt(2, aluno.getIdade());
            preparedStatement.setInt(3, aluno.getCursoId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar o aluno: " + e.getMessage());
        }
    }

    private static void atualizarNomeAluno(int id, String novoNome) {
        try (Connection conexao = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = conexao.prepareStatement("UPDATE Aluno SET nome = ? WHERE id = ?")) {
            preparedStatement.setString(1, novoNome);
            preparedStatement.setInt(2, id);

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Nome do aluno atualizado com sucesso!");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }

    private static void pesquisarAluno(int id) {
        try (Connection conexao = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = conexao.prepareStatement("SELECT * FROM Aluno WHERE id = ?")) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Nome: " + resultSet.getString("nome"));
                    System.out.println("Idade: " + resultSet.getInt("idade"));
                    System.out.println("Curso ID: " + resultSet.getInt("curso_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }

    private static void buscarPorSubstring(String substring) {
        try (Connection conexao = DriverManager.getConnection(URL);
             PreparedStatement preparedStatement = conexao.prepareStatement("SELECT * FROM Aluno WHERE nome LIKE ?")) {
            preparedStatement.setString(1, "%" + substring + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Alunos que contêm a substring '" + substring + "':");
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Nome: " + resultSet.getString("nome"));
                    System.out.println("Idade: " + resultSet.getInt("idade"));
                    System.out.println("Curso ID: " + resultSet.getInt("curso_id"));
                    System.out.println("--------");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao processar o banco de dados: " + e.getMessage());
        }
    }
}

class Aluno {
    private String nome;
    private int idade;
    private int cursoId;

    public Aluno(String nome, int idade, int cursoId) {
        this.nome = nome;
        this.idade = idade;
        this.cursoId = cursoId;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public int getCursoId() {
        return cursoId;
    }
}
