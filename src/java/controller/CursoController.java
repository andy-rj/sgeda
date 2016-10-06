package controller;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Subdisciplina;
import helper.CursoHelper;
import helper.DisciplinaHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CursoController {
    
   
    private Curso cursoDetalhe;

    private CursoHelper cursoHelper;
    private String descricao;
    private DisciplinaHelper disciplinaHelper;
    private Integer disciplinaSelecionada;
    private List<Disciplina> disciplinasCurso;
    private List<Disciplina> disciplinasDisponiveis;
    private String nome;
    private List<Curso> resultadoConsulta;
    private String stringConsulta;
    private Integer subdisciplinaSelecionada;
    private List<Subdisciplina> subdisciplinas;

    public CursoController() {
        
        cursoHelper = new CursoHelper();
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


    public String alterarCurso() {
        return null;
     }

    public void cadastrar() {
        
        Curso curso = new Curso();
        curso.setDescricao(descricao);
        curso.setNome(nome);
        curso.setDisciplinas(new HashSet<>(disciplinasCurso));
        
        if(!cursoHelper.cadastrar(curso)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Curso, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Curso Cadastrado com Sucesso!");

        limparCampos();
        
    }

    public void consultar(){
        resultadoConsulta = cursoHelper.getCursos(stringConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
         }
     }
   

    public void excluirDisciplina(Disciplina disciplina) {
        if(disciplina != null){
            disciplinasCurso.remove(disciplina);
            disciplinasDisponiveis.add(disciplina);
        }
        disciplinaSelecionada = null;
    }

    public String exibirDetalhes(Curso curso) {
        cursoDetalhe = curso;
        return "/restrito/cadastro/detalhe/curso?faces-redirect=true";
     }
    
    
    public Curso getCursoDetalhe(){
        return cursoDetalhe;
    }
    
    public void setCursoDetalhe(Curso cursoDetalhe){
        this.cursoDetalhe = cursoDetalhe;
    }
    public String getDescricao(){
        return descricao;
    }
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public Integer getDisciplinaSelecionada(){
        return disciplinaSelecionada;
    }
    
    public void setDisciplinaSelecionada(Integer disciplinaSelecionada){
        this.disciplinaSelecionada = disciplinaSelecionada;
    }
    
    
    public List<Disciplina> getDisciplinasCurso() {
        return disciplinasCurso;
    }
    
    public List<Disciplina> getDisciplinasDisponiveis() {
        return disciplinasDisponiveis;
    }
    
    
     
    public String getNome(){
        return nome;
    }
     

    public void setNome(String nome) {
        this.nome = nome;
    }
     
    public List<Curso> getResultadoConsulta(){
        return resultadoConsulta;
    }
     

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }
     
    public Integer getSubdisciplinaSelecionada(){
        return subdisciplinaSelecionada;
    }
     
    public void setSubdisciplinaSelecionada(Integer subdisciplinaSelecionada){
        this.subdisciplinaSelecionada = subdisciplinaSelecionada;
    }
     
    public List<Subdisciplina> getSubdisciplinas(){
        return subdisciplinas;
    }

    public void setSubdisciplinas(List<Subdisciplina> subdisciplinas) {
        this.subdisciplinas = subdisciplinas;
    }

    public String inativar() {
        return null;
     }

    public void incluirDisciplina() {
        if(disciplinaSelecionada!=null){
            if(disciplinasCurso==null) disciplinasCurso = new ArrayList<>();
            for(Disciplina disciplina : disciplinasDisponiveis){
                if(disciplina.getIdDisciplina().equals(disciplinaSelecionada)){
                    if(!disciplinasCurso.contains(disciplina)){
                        disciplinasCurso.add(disciplina);
                        disciplinasDisponiveis.remove(disciplina);
                        break;
                    }
                }
            }
        }
    }

    public void limparCampos() {
        
        nome = null;
        descricao = null;
        if(disciplinasCurso != null){
            for(Disciplina disciplina : disciplinasCurso){
                disciplinasDisponiveis.add(disciplina);
            }
        }
        disciplinasCurso = null;

    }
     
    public String limparFormularioCadastro(){
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
    }

    public String novaConsulta() {
        limparCampos();
        return "/restrito/cadastro/consulta/curso?faces-redirect=true";
    }

    public String novoCadastro() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        limparCampos();
        return "/restrito/administrador/cadastro/curso?faces-redirect=true";
    }

    public void onChangeDisciplina() {
        if(disciplinaSelecionada !=null && !disciplinaSelecionada.equals(0))
            subdisciplinas = disciplinaHelper.getSubdisciplinas(disciplinaSelecionada);
        else
            subdisciplinas = new ArrayList<>();
    }
}
