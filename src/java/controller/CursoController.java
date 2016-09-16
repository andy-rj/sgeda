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
public class CursoController {
    
   
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    private String nome;
    private CursoHelper cursoHelper;
    private DisciplinaHelper disciplinaHelper;
    private Integer disciplinaSelecionada;

    public Integer getDisciplinaSelecionada() {
        return disciplinaSelecionada;
    }

    public void setDisciplinaSelecionada(Integer disciplinaSelecionada) {
        this.disciplinaSelecionada = disciplinaSelecionada;
    }
    private List<Disciplina> disciplinasDisponiveis;
    
    public List<Disciplina> getDisciplinasDisponiveis() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        return disciplinasDisponiveis;
    }
    
    public CursoController() {

       cursoHelper = new CursoHelper();
       disciplinaHelper = new DisciplinaHelper();
         
    }

    public void cadastrar() {
        
        Curso curso = new Curso();
        curso.setDescricao(descricao);
        curso.setNome(nome);
        
        if(!cursoHelper.cadastrar(curso)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Curso, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Curso Cadastrado com Sucesso!");

        limparCampos();
        
    }

   

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

   
    
    public void limparCampos(){
     
      nome = null;
      descricao = null;
     
    }
    
    public String limparFormularioCadastro(){
        return novoCadastro();
    }
    
    public String novoCadastro(){
        limparCampos();
        return "/restrito/administrador/cadastro/curso?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/cadastro/consulta/curso?faces-redirect=true";
    }
    
    
    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    
     
     public void limparFormularioConsulta(){
        
     }
     
     private List<Professor> resultadoConsulta;

    public List<Professor> getResultadoConsulta() {
        return resultadoConsulta;
    }
     
     public String consultar(){
         //resultadoConsulta = professorHelper.getProfessores(nomeConsulta, cpfConsulta, matriculaConsulta);
         if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
             addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
             return "";
         }
         return "/restrito/cadastro/consulta/resultadoCurso?faces-redirect=true";
     }
     
     
     public String exibirDetalhes(Professor professor){
        //professorDetalhe = professor;
        return "/restrito/cadastro/detalhe/curso?faces-redirect=true";
     }
     
     public String alterarProfessor(){
         return null;
     }
     
     public String inativar(){
         return null;
     }
     private List<Subdisciplina> subdisciplinas;
     private Integer subdisciplinaSelecionada;

    public List<Subdisciplina> getSubdisciplinas() {
        return subdisciplinas;
    }

    public void setSubdisciplinas(List<Subdisciplina> subdisciplinas) {
        this.subdisciplinas = subdisciplinas;
    }

    public Integer getSubdisciplinaSelecionada() {
        return subdisciplinaSelecionada;
    }

    public void setSubdisciplinaSelecionada(Integer subdisciplinaSelecionada) {
        this.subdisciplinaSelecionada = subdisciplinaSelecionada;
    }
     
     public void onChangeDisciplina(){
          if(disciplinaSelecionada !=null && !disciplinaSelecionada.equals(0))
            subdisciplinas = disciplinaHelper.getSubdisciplinas(disciplinaSelecionada);
        else
            subdisciplinas = new ArrayList<>();
     }
}
