package controller;

import entidade.Disciplina;
import helper.DisciplinaHelper;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class DisciplinaController {
    private String codigo;
    
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
    

   
    
    public void salvarAlteracao(){
        Disciplina disciplinaAlterar = disciplinaDetalhe;
        
        if(disciplinaAlterar.getCodigo().equals(codigoAlterar)&&
                disciplinaAlterar.getDescricao().equals(descricaoAlterar)&&
                disciplinaAlterar.getNome().equals(nomeAlterar)){
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }
        
        disciplinaAlterar.setCodigo(codigoAlterar);
        disciplinaAlterar.setDescricao(descricaoAlterar);
        disciplinaAlterar.setNome(nomeAlterar);
        
        if(!disciplinaHelper.salvarAlteracaoDisciplina(disciplinaAlterar)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }
        
        addMessage(null,FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");
        
        consultar();
        disciplinaDetalhe = disciplinaAlterar;
        nomeAlterar = disciplinaDetalhe.getNome();
        codigoAlterar = disciplinaDetalhe.getCodigo();
        descricaoAlterar = disciplinaDetalhe.getDescricao();
    }
    private String codigoAlterar;

    public String getCodigoAlterar() {
        return codigoAlterar;
    }

    public void setCodigoAlterar(String codigoAlterar) {
        this.codigoAlterar = codigoAlterar;
    }

    public String getNomeAlterar() {
        return nomeAlterar;
    }

    public void setNomeAlterar(String nomeAlterar) {
        this.nomeAlterar = nomeAlterar;
    }

    public String getDescricaoAlterar() {
        return descricaoAlterar;
    }

    public void setDescricaoAlterar(String descricaoAlterar) {
        this.descricaoAlterar = descricaoAlterar;
    }
    private String nomeAlterar;
    private String descricaoAlterar;

    public void cadastrar() {
        
        Disciplina disciplina = new Disciplina();
        disciplina.setDescricao(descricao);
        disciplina.setNome(nome);
        disciplina.setCodigo(codigo);
        
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
    public void exibirDetalhes(Disciplina disciplina){
        disciplinaDetalhe = disciplina;
        nomeAlterar = disciplinaDetalhe.getNome();
        codigoAlterar = disciplinaDetalhe.getCodigo();
        descricaoAlterar = disciplinaDetalhe.getDescricao();
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
    
    public String getCodigo() {
        return codigo;
    }
     
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
     
    public String inativar() {
        return null;
     }

    public void limparCampos() {
        
        nome = null;
        descricao = null;
        codigo = null;
     
    }
     
    public String limparFormularioCadastro(){
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = null;
    }

    public String novaConsulta() {
        limparFormularioConsulta();
        stringConsulta = "";
        consultar();
        return "/restrito/cadastro/consulta/disciplina?faces-redirect=true";
    }

    public String novoCadastro() {
        limparCampos();
        return "/restrito/administrador/cadastro/disciplina?faces-redirect=true";
    }

}
