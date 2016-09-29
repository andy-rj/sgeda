package controller;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Turma;
import helper.CursoHelper;
import helper.DisciplinaHelper;
import helper.ProfessorHelper;
import helper.TurmaHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class TurmaController {
    private Curso curso;
    private CursoHelper cursoHelper;
    private Integer cursoSelecionado;
    private boolean cursoSelecionadoFlag;
    private List<Curso> cursosCadastrados;
    private Date dataFim;
    private Date dataInicio;
    private String descricao;
    private DisciplinaHelper disciplinaHelper;
    private List<Disciplina> disciplinasDisponiveis;
    private String nome;
    private ProfessorHelper professorHelper;
    private List<Turma> resultadoConsulta;
    private String stringConsulta;
    public Turma turmaDetalhe;
    private TurmaHelper turmaHelper;
    private List<Turma> turmas;
    private String turno;

    public TurmaController() {
        
        cursoHelper = new CursoHelper();
        turmaHelper = new TurmaHelper();
        professorHelper = new ProfessorHelper();
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

    public String alterarTurma() {
        return null;
    }

    public void cadastrar() {
        
        if(turmas == null || turmas.isEmpty()) {
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Entre com pelo menos uma turma para cadastrar!");
            return;
        }
        
        if(!turmaHelper.cadastrar(turmas)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Turmas, Tente novamente!");
            return;

        }
        if(turmas.size()==1) addMessage(null, FacesMessage.SEVERITY_INFO ,"Turma Cadastrada com Sucesso!");
        else addMessage(null, FacesMessage.SEVERITY_INFO ,"Turmas Cadastradas com Sucesso!");

        limparCampos();
        turmas = null;
        cursoSelecionadoFlag = false;
        curso = null;
        
    }

    public void consultar() {
        resultadoConsulta = turmaHelper.getTurmas(stringConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
        }
     }
   
    public void excluirTurma(Turma turma) {
        turmas.remove(turma);
    }

    public String exibirDetalhes(Turma turma) {
        turmaDetalhe = turma;
        return "/restrito/cadastro/detalhe/turma?faces-redirect=true";
     }

    public Integer getCursoSelecionado() {
        return cursoSelecionado;
    }
    

    public void setCursoSelecionado(Integer cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    public List<Curso> getCursosCadastrados() {
        return cursosCadastrados;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }


    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
   
    public List<Disciplina> getDisciplinasDisponiveis(){
        return disciplinasDisponiveis;
    }

    public String getNome() {
        return nome;
    }
    
    
     
    public void setNome(String nome){
        this.nome = nome;
    }
     
    public void selecionarCurso() {
        if(dataFim.before(dataInicio)){
            addMessage("cadastro:dataFim", FacesMessage.SEVERITY_ERROR, "Data de término anterior a data de início!");
            return;
        }
        
        for (Curso curso : cursosCadastrados) {
            if (curso.getIdCurso().equals(cursoSelecionado)) {
                this.curso = curso;
                break;
            }
        }
        List<Disciplina> disciplinasDisponiveis = disciplinaHelper.getDisciplinasByCurso(cursoSelecionado);
        
        turmas = new ArrayList<>();
        
        for(Disciplina disciplina: disciplinasDisponiveis){
            Turma turma = new Turma();
            turma.setCurso(curso);
            turma.setDataFim(dataFim);
            turma.setDataInicio(dataInicio);
            turma.setDisciplina(disciplina);
            turma.setTurno(turno);
            turmas.add(turma);
        }
            
    }

    public List<Turma> getResultadoConsulta(){
        return resultadoConsulta;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }

    public Turma getTurmaDetalhe() {
        return turmaDetalhe;
    }

    
    public void setTurmaDetalhe(Turma turmaDetalhe) {
        this.turmaDetalhe = turmaDetalhe;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String inativar() {
        return null;
    }

    public boolean isCursoSelecionadoFlag() {
        return cursoSelecionadoFlag;
    }

    public void limparCampos() {
        nome = null;
        descricao = null;
        dataFim = null;
        dataInicio = null;
        turno = null;
        cursoSelecionado = 0;
    }

    public String limparFormularioCadastro() {
        return novoCadastro();
    }
    
    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = null;
    }
    
    
    public String novaConsulta(){
        limparFormularioConsulta();
        return "/restrito/cadastro/consulta/turma?faces-redirect=true";
    }
    
    
    public String novoCadastro(){
        cursosCadastrados = cursoHelper.getCursosDisponiveis();
        limparCampos();
        cursoSelecionadoFlag = false;
        turmas = null;
        curso = null;
        return "/restrito/administrador/cadastro/turma?faces-redirect=true";
    }
    
}
