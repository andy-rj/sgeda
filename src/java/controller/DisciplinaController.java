package controller;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Subdisciplina;
import helper.CursoHelper;
import helper.DisciplinaHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class DisciplinaController {
    
    private String descricao;
    private DisciplinaHelper disciplinaHelper;
    private String nome;
    private List<Disciplina> resultadoConsulta;
    
    public DisciplinaController() {
        disciplinaHelper = new DisciplinaHelper();
    }
    
    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public String alterarDisciplina() {
        return null;
     }

    public void cadastrar() {
        
        Disciplina disciplina = new Disciplina();
        disciplina.setDescricao(descricao);
        disciplina.setNome(nome);
        
        if(disciplinaHelper.getByNome(nome) != null){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Disciplina com nome " + nome + " ja está cadastrada no sistema!");
            return;
        }
        
        if(!disciplinaHelper.cadastrar(disciplina)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Disciplina, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Disciplina Cadastrado com Sucesso!");

        limparCampos();
        
    }

   private String stringConsulta;

    public List<Disciplina> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public void setResultadoConsulta(List<Disciplina> resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String nomeConsulta) {
        this.stringConsulta = nomeConsulta;
    }
    
    public String consultar(){
        resultadoConsulta = disciplinaHelper.getDisciplinas(stringConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
        }
        return "";
     }
    
    private Disciplina disciplinaDetalhe;

    public Disciplina getDisciplinaDetalhe() {
        return disciplinaDetalhe;
    }

    public void setDisciplinaDetalhe(Disciplina disciplinaDetalhe) {
        this.disciplinaDetalhe = disciplinaDetalhe;
    }
    public String exibirDetalhes(Disciplina disciplina){
        disciplinaDetalhe = disciplina;
        return "/restrito/cadastro/detalhe/disciplina?faces-redirect=true";
     }
    
    public String getDescricao(){
        return descricao;
    }
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public String getNome() {
        return nome;
    }
     
    public void setNome(String nome){
        this.nome = nome;
    }
     
    public String inativar() {
        return null;
     }

    public void limparCampos() {
        
        nome = null;
        descricao = null;
     
    }
     
    public String limparFormularioCadastro(){
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = null;
    }

    public String novaConsulta() {
        limparCampos();
        return "/restrito/cadastro/consulta/disciplina?faces-redirect=true";
    }

    public String novoCadastro() {
        limparCampos();
        return "/restrito/administrador/cadastro/disciplina?faces-redirect=true";
    }

}
