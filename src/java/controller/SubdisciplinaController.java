package controller;

import entidade.Disciplina;
import entidade.Subdisciplina;
import helper.DisciplinaHelper;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SubdisciplinaController {
    
    private String descricao;
    private DisciplinaHelper disciplinaHelper;
    private String nome;
    private Integer disciplina;
    private List<Disciplina> disciplinasDisponiveis;
    private List<Subdisciplina> resultadoConsulta;
    
    public SubdisciplinaController() {
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
        
        Subdisciplina subdisciplina = new Subdisciplina();
        subdisciplina.setDescricao(descricao);
        subdisciplina.setNome(nome);
        if(disciplina.equals(new Integer("0"))) return;
        subdisciplina.setDisciplina(disciplinaHelper.getById(disciplina));
        
        if(!disciplinaHelper.cadastrarSubdisciplina(subdisciplina)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Subdisciplina, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Subdisciplina Cadastrado com Sucesso!");

        limparCampos();
        
    }

   private String nomeConsulta;

    public List<Subdisciplina> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public void setResultadoConsulta(List<Subdisciplina> resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    public Integer getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    public String getNomeConsulta() {
        return nomeConsulta;
    }

    public void setNomeConsulta(String nomeConsulta) {
        this.nomeConsulta = nomeConsulta;
    }
   private String descricaoConsulta;
    
    public String consultar(){
        resultadoConsulta = disciplinaHelper.getSubdisciplinas(nomeConsulta, descricaoConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
            return "";
        }
        return "/restrito/cadastro/consulta/resultadoSubdisciplina?faces-redirect=true";
     }

    public String getDescricaoConsulta() {
        return descricaoConsulta;
    }

    public void setDescricaoConsulta(String descricaoConsulta) {
        this.descricaoConsulta = descricaoConsulta;
    }
    
    private Disciplina disciplinaDetalhe;

    public List<Disciplina> getDisciplinasDisponiveis() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        return disciplinasDisponiveis;
    }
    
    public Disciplina getDisciplinaDetalhe() {
        return disciplinaDetalhe;
    }

    public void setDisciplinaDetalhe(Disciplina disciplinaDetalhe) {
        this.disciplinaDetalhe = disciplinaDetalhe;
    }
    public String exibirDetalhes(Disciplina disciplina){
        disciplinaDetalhe = disciplina;
        return "/restrito/cadastro/detalhe/subdisciplina?faces-redirect=true";
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
        disciplina = null;
     
    }
     
    public String limparFormularioCadastro(){
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
    }

    public String novaConsulta() {
        limparCampos();
        return "/restrito/cadastro/consulta/subdisciplina?faces-redirect=true";
    }

    public String novoCadastro() {
        limparCampos();
        return "/restrito/administrador/cadastro/subdisciplina?faces-redirect=true";
    }

}
