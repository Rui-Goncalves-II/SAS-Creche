package org.example.Aluno;

import org.example.Responsavel.Responsavel;

import java.util.List;

public class Aluno {
    private Long idAluno;
    private String nome;
    private String cpf;
    private String rg;
    private String sexo;
    private String dataNascimento;
    private String etnia;
    private String endereco;
    private String numCasa;
    private String pontoReferencia;
    private String bairro;
    private String uf;
    private String municipio;
    private String sus;
    private List<String> deficiencias;
    private List<String> alergias;
    private String mobilidade;
    private String problemaSaude;
    private String restricaoAlimentar;
    private String turma;
    private String periodoCursando;
    private Long idResponsavel;
    private List<Responsavel> pessoasAutorizadas;

    public Aluno(Long idAluno, String nome, String cpf, String rg, String sexo, String dataNascimento, String etnia, String endereco, String numCasa, String pontoReferencia, String bairro, String uf, String municipio, String sus, List<String> deficiencias, List<String> alergias, String mobilidade, String problemaSaude, String restricaoAlimentar, String turma, String periodoCursando, Long idResponsavel, List<Responsavel> pessoasAutorizadas) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.etnia = etnia;
        this.endereco = endereco;
        this.numCasa = numCasa;
        this.pontoReferencia = pontoReferencia;
        this.bairro = bairro;
        this.uf = uf;
        this.municipio = municipio;
        this.sus = sus;
        this.deficiencias = deficiencias;
        this.alergias = alergias;
        this.mobilidade = mobilidade;
        this.problemaSaude = problemaSaude;
        this.restricaoAlimentar = restricaoAlimentar;
        this.turma = turma;
        this.periodoCursando = periodoCursando;
        this.idResponsavel = idResponsavel;
        this.pessoasAutorizadas = pessoasAutorizadas;
    }

    public Aluno() {

    }

    public Long getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Long idAluno) {
        this.idAluno = idAluno;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumCasa() {
        return numCasa;
    }

    public void setNumCasa(String numCasa) {
        this.numCasa = numCasa;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSus() {
        return sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public List<String> getDeficiencias() {
        return deficiencias;
    }

    public void setDeficiencias(List<String> deficiencias) {
        this.deficiencias = deficiencias;
    }

    public List<String> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<String> alergias) {
        this.alergias = alergias;
    }

    public String getMobilidade() {
        return mobilidade;
    }

    public void setMobilidade(String mobilidade) {
        this.mobilidade = mobilidade;
    }

    public String getProblemaSaude() {
        return problemaSaude;
    }

    public void setProblemaSaude(String problemaSaude) {
        this.problemaSaude = problemaSaude;
    }

    public String getRestricaoAlimentar() {
        return restricaoAlimentar;
    }

    public void setRestricaoAlimentar(String restricaoAlimentar) {
        this.restricaoAlimentar = restricaoAlimentar;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getPeriodoCursando() {
        return periodoCursando;
    }

    public void setPeriodoCursando(String periodoCursando) {
        this.periodoCursando = periodoCursando;
    }

    public Long getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Long idResponsavel) {
        this.idResponsavel = idResponsavel;
    }
}