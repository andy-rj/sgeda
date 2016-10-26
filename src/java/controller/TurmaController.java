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
import java.util.Set;
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
    private String codigoAlterar;

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
    @NotNull(message = "Data de início é obrigatória!")
    @Future(message = "Data de início inválida!")
    private Date dataInicioAlterar;
    @NotNull(message = "Data de término é obrigatória!")
    @Future(message = "Data de término inválida!")
    private Date dataTerminoAlterar;
    private String descricao;
    private String descricaoAlterar;
    private Disciplina disciplinaAlterar;
    private DisciplinaHelper disciplinaHelper;
    private List<Disciplina> disciplinasDisponiveis;
    private Date duracaoSimulado;
    private String nome;
    private ProfessorHelper professorHelper;
    private Integer professorSelecionadoAlterar;
    private List<String> professores;
    private Set<Professor> professoresAtivosAlterar;
    private List<Turma> resultadoConsulta;
    private SimuladoHelper simuladoHelper;
    private Simulado simuladoSelecionado;
    List<Simulado> simuladosCadastrados;
    private List<Simulado> simuladosFiltrados;
    private String stringConsulta;
    private TurmaAluno turmaAlunoDetalhe;
    public Turma turmaDetalhe;
    private TurmaHelper turmaHelper;
    private Turma turmaProfessorDetalhe;
    private List<Turma> turmas;
    List<Turma> turmasProfessor;
    private String turno;
    private String turnoAlterar;

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
        if (turmaProfessorDetalhe.getTurmaSimulados() == null) {
            turmaProfessorDetalhe.setTurmaSimulados(new HashSet<TurmaSimulado>());
        }
        turmaProfessorDetalhe.getTurmaSimulados().add(turmaSimulado);
        turmaSimulado.setId(new TurmaSimuladoId(simuladoSelecionado.getIdSimulado(), turmaProfessorDetalhe.getIdTurma()));
        if (!turmaHelper.cadastrarSimulado(turmaSimulado)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao incluir simulado!");
            return;
        }
        addMessage(null, FacesMessage.SEVERITY_INFO, "Simulado incluido com sucesso!");
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
    
    public void salvarAlteracao(){
        Turma turmaAlterar = turmaDetalhe;
        
        if(turmaAlterar.getNome().equals(codigoAlterar)&&
                turmaAlterar.getDescricao().equals(descricaoAlterar)&&
                turmaAlterar.getDataInicio().getDay()==dataInicioAlterar.getDay()&&
                turmaAlterar.getDataInicio().getMonth()==dataInicioAlterar.getMonth()&&
                turmaAlterar.getDataInicio().getYear()==dataInicioAlterar.getYear()&&
                turmaAlterar.getDataFim().getDay()==dataTerminoAlterar.getDay()&&
                turmaAlterar.getDataFim().getMonth()==dataTerminoAlterar.getMonth()&&
                turmaAlterar.getDataFim().getYear()==dataTerminoAlterar.getYear()&&
                turmaAlterar.getTurno().equals(turnoAlterar)&&
                turmaAlterar.getProfessor().getIdProfessor() == professorSelecionadoAlterar){
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }
        
        if(dataTerminoAlterar.before(dataInicioAlterar)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Data de término anterior a data de início!");
            return;
        }
        
        turmaAlterar.setNome(codigoAlterar);
        turmaAlterar.setDescricao(descricaoAlterar);
        turmaAlterar.setDataInicio(dataInicioAlterar);
        turmaAlterar.setDataFim(dataTerminoAlterar);
        turmaAlterar.setTurno(turnoAlterar);
        turmaAlterar.setProfessor(professorHelper.getById(professorSelecionadoAlterar));
        
        if(!turmaHelper.salvarAlteracaoTurma(turmaAlterar)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }
        
        addMessage(null,FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");
        
        consultar();
        turmaDetalhe = turmaAlterar;
    }

    public void carregarAlterar() {
        descricaoAlterar = turmaDetalhe.getDescricao();
        codigoAlterar = turmaDetalhe.getNome();
        dataInicioAlterar = turmaDetalhe.getDataInicio();
        dataTerminoAlterar = turmaDetalhe.getDataFim();
        if (turmaDetalhe.getProfessor().getPessoa().getAtivo()) {
            professorSelecionadoAlterar = turmaDetalhe.getProfessor().getIdProfessor();
        } else {
            professorSelecionadoAlterar = 0;
        }
        professoresAtivosAlterar = new HashSet<>(turmaDetalhe.getDisciplina().getProfessoresAtivos());
        turnoAlterar = turmaDetalhe.getTurno();
    }
    
    public void consultar() {
        resultadoConsulta = turmaHelper.getTurmas(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
    }

    public void detalheTurmaAluno(TurmaAluno turmaAluno) {
        turmaAlunoDetalhe = turmaAluno;
    }

    public void detalheTurmaProfessor(Turma turma) {
        turmaProfessorDetalhe = turma;
    }

    public void excluirTurma(Turma turma) {
        turmas.remove(turma);
    }

    public void exibirDetalhes(Turma turma) {
        turmaDetalhe = turma;
    }

    public Aluno getAlunoOnline() {
        return alunoOnline;
    }
    
    public String getCodigoAlterar() {
        return codigoAlterar;
    }
    
    public void setCodigoAlterar(String codigoAlterar) {
        this.codigoAlterar = codigoAlterar;
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

    public Date getDataInicioAlterar() {
        return dataInicioAlterar;
    }
    
    public void setDataInicioAlterar(Date dataInicioAlterar) {
        this.dataInicioAlterar = dataInicioAlterar;
    }

    public Date getDataTerminoAlterar() {
        return dataTerminoAlterar;
    }
    
    public void setDataTerminoAlterar(Date dataTerminoAlterar) {
        this.dataTerminoAlterar = dataTerminoAlterar;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoAlterar() {
        return descricaoAlterar;
    }
    
    public void setDescricaoAlterar(String descricaoAlterar) {
        this.descricaoAlterar = descricaoAlterar;
    }

    public Disciplina getDisciplinaAlterar() {
        return disciplinaAlterar;
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

    public Integer getProfessorSelecionadoAlterar() {
        return professorSelecionadoAlterar;
    }
    
    public void setProfessorSelecionadoAlterar(Integer professorSelecionadoAlterar) {
        this.professorSelecionadoAlterar = professorSelecionadoAlterar;
    }

    public List<String> getProfessores() {
        return professores;
    }

    public Set<Professor> getProfessoresAtivosAlterar() {
        return professoresAtivosAlterar;
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

    public TurmaAluno getTurmaAlunoDetalhe() {
        return turmaAlunoDetalhe;
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

    public String getTurnoAlterar() {
        return turnoAlterar;
    }
    
    public void setTurnoAlterar(String turnoAlterar) {
        this.turnoAlterar = turnoAlterar;
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
        consultar();
        professoresAtivosAlterar = new HashSet<>();
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

    public void selecionarCurso() {
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

}
