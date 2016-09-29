package controller;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
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
        curso.setDisciplinas(new HashSet<>(disciplinasCurso));
        
        if(!cursoHelper.cadastrar(curso)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Curso, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Curso Cadastrado com Sucesso!");

        limparCampos();
        
    }

   private List<Disciplina> disciplinasCurso;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void incluirDisciplina(){
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
   
    private String stringConsulta;

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }
    
    
    public void limparCampos(){
     
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
    public List<Disciplina> getDisciplinasCurso(){
        return disciplinasCurso;
    }
    
    public void excluirDisciplina(Disciplina disciplina){
        if(disciplina != null){
            disciplinasCurso.remove(disciplina);
            disciplinasDisponiveis.add(disciplina);
        }
        disciplinaSelecionada = null;
    }
    
    public String novoCadastro(){
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
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
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
     }
     
     private List<Curso> resultadoConsulta;

    public List<Curso> getResultadoConsulta() {
        return resultadoConsulta;
    }
     
     public void consultar(){
         resultadoConsulta = cursoHelper.getCursos(stringConsulta);
         if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
             addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
         }
     }
     
     private Curso cursoDetalhe;

    public Curso getCursoDetalhe() {
        return cursoDetalhe;
    }

    public void setCursoDetalhe(Curso cursoDetalhe) {
        this.cursoDetalhe = cursoDetalhe;
    }
     
     public String exibirDetalhes(Curso curso){
        cursoDetalhe = curso;
        return "/restrito/cadastro/detalhe/curso?faces-redirect=true";
     }
     
     public String alterarCurso(){
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
