package org.example.Responsavel;

import org.example.Aluno.Aluno;

import java.util.List;

public class Responsavel {
    private Long idResponsavel;
    private String nome;
    private String cpf;
    private String rg;
    private String sexo;
    private String dataNascimento;
    private String etnia;
    private String email;
    private String contato;
    private String contato2;
    private String parentesco;
    private boolean responsavelLegal;
    private String rendaBruta;
    private String auxilioGoverno;
    private String emprego;
    private String localTrabalho;
    private List<Aluno> alunos;

    public Responsavel(Long idResponsavel, String nome, String cpf, String rg, String sexo, String dataNascimento, String etnia, String email, String contato, String contato2, String parentesco, boolean responsavelLegal, String rendaBruta, String auxilioGoverno, String emprego, String localTrabalho, List<Aluno> alunos) {
        this.idResponsavel = idResponsavel;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.etnia = etnia;
        this.email = email;
        this.contato = contato;
        this.contato2 = contato2;
        this.parentesco = parentesco;
        this.responsavelLegal = responsavelLegal;
        this.rendaBruta = rendaBruta;
        this.auxilioGoverno = auxilioGoverno;
        this.emprego = emprego;
        this.localTrabalho = localTrabalho;
        this.alunos = alunos;
    }

    public Responsavel() {

    }

    public Long getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Long idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEtnia() {
        return etnia;
    }

    public void setEtnia(String etnia) {
        this.etnia = etnia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getContato2() {
        return contato2;
    }

    public void setContato2(String contato2) {
        this.contato2 = contato2;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean isResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(boolean responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public String getRendaBruta() {
        return rendaBruta;
    }

    public void setRendaBruta(String rendaBruta) {
        this.rendaBruta = rendaBruta;
    }

    public String getAuxilioGoverno() {
        return auxilioGoverno;
    }

    public void setAuxilioGoverno(String auxilioGoverno) {
        this.auxilioGoverno = auxilioGoverno;
    }

    public String getEmprego() {
        return emprego;
    }

    public void setEmprego(String emprego) {
        this.emprego = emprego;
    }

    public String getLocalTrabalho() {
        return localTrabalho;
    }

    public void setLocalTrabalho(String localTrabalho) {
        this.localTrabalho = localTrabalho;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}
