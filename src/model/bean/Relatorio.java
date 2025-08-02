/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

/**
 *
 * @author Lucas B Tsuchiya
 */
public class Relatorio {
    private String nome_aluno;
    private String nome_evento;
    private String data_evento;
    private String responsavel;
    private String curso_aluno;
    private int matricula_aluno;

    public int getMatricula_aluno() {
        return matricula_aluno;
    }

    public void setMatricula_aluno(int matricula_aluno) {
        this.matricula_aluno = matricula_aluno;
    }

    public String getNome_aluno() {
        return nome_aluno;
    }

    public void setNome_aluno(String nome_aluno) {
        this.nome_aluno = nome_aluno;
    }

    public String getNome_evento() {
        return nome_evento;
    }

    public void setNome_evento(String nome_evento) {
        this.nome_evento = nome_evento;
    }

    public String getData_evento() {
        return data_evento;
    }

    public void setData_evento(String data_evento) {
        this.data_evento = data_evento;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getCurso_aluno() {
        return curso_aluno;
    }

    public void setCurso_aluno(String curso_aluno) {
        this.curso_aluno = curso_aluno;
    }
    
}
