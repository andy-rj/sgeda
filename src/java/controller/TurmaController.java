package controller;

import entidade.Aluno;
import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Simulado;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.TurmaSimulado;
import entidade.TurmaSimuladoId;
import helper.AlunoHelper;
import helper.CursoHelper;
import helper.DisciplinaHelper;
import helper.ProfessorHelper;
import helper.SimuladoHelper;
import helper.TurmaHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

@ManagedBean
@SessionScoped
public class TurmaController {
    private AlunoHelper alunoHelper;
    private Aluno alunoOnline;

    private Curso curso;
    private CursoHelper cursoHelper;
    private Integer cursoSelecionado;
    private boolean cursoSelecionadoFlag;
    private List<Curso> cursosCadastrados;
    private Date dataAberturaSimulado;
    private Date dataEncerramentoSimulado;
    @NotNull(message = "Data de término é obrigatória!")
    @Future(message = "Data de término inválida!")
    private Date dataFim;
    @NotNull(message = "Data de início é obrigatória!")
    @Future(message = "Data de início inválida!")
    private Date dataInicio;
    private String descricao;
    private DisciplinaHelper disciplinaHelper;
    private List<Disciplina> disciplinasDisponiveis;
    private Date duracaoSimulado;
    private String nome;
    private ProfessorHelper professorHelper;
    private List<String> professores;
    private List<Turma> resultadoConsulta;
    private SimuladoHelper simuladoHelper;
    private Simulado simuladoSelecionado;
    List<Simulado> simuladosCadastrados;
    private List<Simulado> simuladosFiltrados;
    private String stringConsulta;
    public Turma turmaDetalhe;
    private TurmaHelper turmaHelper;
    private Turma turmaProfessorDetalhe;
    private List<Turma> turmas;
    List<Turma> turmasProfessor;
    private String turno;

    public TurmaController() {
        simuladoHelper = new SimuladoHelper();
        cursoHelper = new CursoHelper();
        turmaHelper = new TurmaHelper();
        professorHelper = new ProfessorHelper();
        disciplinaHelper = new DisciplinaHelper();
        alunoHelper = new AlunoHelper();

    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void adicionarSimuladoSelecionado() {
        TurmaSimulado turmaSimulado = new TurmaSimulado();
        turmaSimulado.setDataAbertura(dataAberturaSimulado);
        turmaSimulado.setDataEncerramento(dataEncerramentoSimulado);
        turmaSimulado.setDuracao(duracaoSimulado);
        turmaSimulado.setSimulado(simuladoSelecionado);
        turmaSimulado.setTurma(turmaProfessorDetalhe);
        if(turmaProfessorDetalhe.getTurmaSimulados()==null) turmaProfessorDetalhe.setTurmaSimulados(new HashSet<TurmaSimulado>());
        turmaProfessorDetalhe.getTurmaSimulados().add(turmaSimulado);
        turmaSimulado.setId(new TurmaSimuladoId(simuladoSelecionado.getIdSimulado(), turmaProfessorDetalhe.getIdTurma()));
        if(!turmaHelper.cadastrarSimulado(turmaSimulado)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao incluir simulado!");
            return;
        }
        addMessage(null,FacesMessage.SEVERITY_INFO, "Simulado incluido com sucesso!");
    }

    public String alterarTurma() {
        return null;
    }

    
    public void cadastrar() {

        if (turmas == null || turmas.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Entre com pelo menos uma turma para cadastrar!");
            return;
        }

        if (!turmaHelper.cadastrar(turmas)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Turmas, Tente novamente!");
            return;

        }
        if (turmas.size() == 1) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Turma Cadastrada com Sucesso!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Turmas Cadastradas com Sucesso!");
        }

        limparCampos();
        turmas = null;
        cursoSelecionadoFlag = false;
        curso = null;

    }

    public void consultar() {
        resultadoConsulta = turmaHelper.getTurmas(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
    }

    public void detalheTurmaProfessor(Turma turma) {
        turmaProfessorDetalhe = turma;
    }
    
    public void detalheTurmaAluno(TurmaAluno turmaAluno) {
        turmaAlunoDetalhe = turmaAluno;
    }
    private TurmaAluno turmaAlunoDetalhe;

    public TurmaAluno getTurmaAlunoDetalhe() {
        return turmaAlunoDetalhe;
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
    
    public Date getDataAberturaSimulado() {
        return dataAberturaSimulado;
    }
    
    public void setDataAberturaSimulado(Date dataAberturaSimulado) {
        this.dataAberturaSimulado = dataAberturaSimulado;
    }
    
    public Date getDataEncerramentoSimulado() {
        return dataEncerramentoSimulado;
    }
    
    public void setDataEncerramentoSimulado(Date dataEncerramentoSimulado) {
        this.dataEncerramentoSimulado = dataEncerramentoSimulado;
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

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Disciplina> getDisciplinasDisponiveis() {
        return disciplinasDisponiveis;
    }

    public Date getDuracaoSimulado() {
        return duracaoSimulado;
    }
    
    public void setDuracaoSimulado(Date duracaoSimulado) {
        this.duracaoSimulado = duracaoSimulado;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getProfessores() {
        return professores;
    }

    public List<Turma> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public Simulado getSimuladoSelecionado() {
        return simuladoSelecionado;
    }
    
    public void setSimuladoSelecionado(Simulado simuladoSelecionado) {
        this.simuladoSelecionado = simuladoSelecionado;
    }

    public List<Simulado> getSimuladosCadastrados() {
        return simuladosCadastrados;
    }

    public List<Simulado> getSimuladosFiltrados() {
        return simuladosFiltrados;
    }
    
    public void setSimuladosFiltrados(List<Simulado> simuladosFiltrados) {
        this.simuladosFiltrados = simuladosFiltrados;
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

    public Turma getTurmaProfessorDetalhe() {
        return turmaProfessorDetalhe;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public List<Turma> getTurmasProfessor() {
        return turmasProfessor;
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

    public String novaConsulta() {
        limparFormularioConsulta();
        return "/restrito/cadastro/consulta/turma?faces-redirect=true";
    }

    public String novaConsultaAluno(Integer idAluno) {
        alunoOnline = alunoHelper.getById(idAluno);
        return "/restrito/aluno/consulta/turma?faces-redirect=true";
    }
    
    public String novaConsultaProfessor(Integer idProfessor) {
        turmasProfessor = turmaHelper.getTurmasByIdProfessor(idProfessor);
        simuladosCadastrados = simuladoHelper.getSimulados();
        professores = new ArrayList<>();
        for (Simulado simulado : simuladosCadastrados) {
            if (!professores.contains(simulado.getProfessor().getPessoa().getNome())) {
                professores.add(simulado.getProfessor().getPessoa().getNome());
            }
        }
        
        return "/restrito/professor/consulta/turma?faces-redirect=true";
    }
    
    public String novoCadastro() {
        cursosCadastrados = cursoHelper.getCursosDisponiveis();
        limparCampos();
        cursoSelecionadoFlag = false;
        turmas = null;
        curso = null;
        return "/restrito/administrador/cadastro/turma?faces-redirect=true";
    }
    
    
    public void selecionarCurso(){
        if (dataFim.before(dataInicio)) {
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

        for (Disciplina disciplina : disciplinasDisponiveis) {
            Turma turma = new Turma();
            turma.setCurso(curso);
            turma.setDataFim(dataFim);
            turma.setDataInicio(dataInicio);
            turma.setDisciplina(disciplina);
            turma.setTurno(turno);
            turmas.add(turma);
        }
    }

    public Aluno getAlunoOnline() {
        return alunoOnline;
    }

}
