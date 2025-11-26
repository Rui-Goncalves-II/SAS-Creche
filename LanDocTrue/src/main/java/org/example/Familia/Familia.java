package org.example.Familia;

import org.example.Aluno.Aluno;
import org.example.Responsavel.Responsavel;

import java.util.List;

public class Familia {

    List<Aluno> alunos;
    List<Responsavel> responsaveis;

    public Familia(List<Aluno> alunos, List<Responsavel> responsaveis) {
        this.alunos = alunos;
        this.responsaveis = responsaveis;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
}
