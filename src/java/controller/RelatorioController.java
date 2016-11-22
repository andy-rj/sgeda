package controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import entidade.Aluno;
import entidade.Curso;
import entidade.Disciplina;
import entidade.Funcionario;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Simulado;
import entidade.Telefone;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.TurmaSimulado;
import entidade.TurmaSimuladoId;
import helper.AlunoHelper;
import helper.CursoHelper;
import helper.DisciplinaHelper;
import helper.FuncionarioHelper;
import helper.LoginHelper;
import helper.ProfessorHelper;
import helper.SimuladoHelper;
import helper.TurmaHelper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

@ManagedBean
@SessionScoped
public class RelatorioController {
    private BarChartModel BarModelDesistentes;

    private final AlunoHelper alunoHelper;
    private List<Aluno> alunosInscritos;
    private Integer anoFim;
    private Integer anoInicio;
    private final CursoHelper cursoHelper;
    private List<Curso> cursos;
    private List<Curso> cursosParaRelatorio;
    private List<String> cursosSelecionados;
    private Date dataFim;
    private String agrupamento;
    private Date dataInicio;
    private Integer fim;
    private BarChartModel horizontalBarModel;
    private Integer inicio;
    private final LoginHelper loginHelper;
    private ProfessorHelper professorHelper;
    private List<String> professoresAtividadeSelecionados;
    private List<Professor> professoresCadastrados;
    private List<Disciplina> disciplinasCadastradas;
    private final DisciplinaHelper disciplinaHelper;
    private final TurmaHelper turmaHelper;
    private final FuncionarioHelper funcionarioHelper;
    private List<Funcionario> funcionariosCadastrados;
    private final SimuladoHelper simuladoHelper;

    public List<Funcionario> getFuncionariosCadastrados() {
        return funcionariosCadastrados;
    }

    public RelatorioController() {
        cursoHelper = new CursoHelper();
        alunoHelper = new AlunoHelper();
        loginHelper = new LoginHelper();
        turmaHelper = new TurmaHelper();
        professorHelper = new ProfessorHelper();
        disciplinaHelper = new DisciplinaHelper();
        funcionarioHelper = new FuncionarioHelper();
        simuladoHelper = new SimuladoHelper();
    }

    public void updateCurso() {
        Curso curso = cursoHelper.getCursoByIdEagerTurmaSimulado(Integer.parseInt(cursoSelecionadoRanking));
        simulados = curso.getTurmasSimulados();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public BarChartModel getBarModelDesistentes() {
        return BarModelDesistentes;
    }

    private void createHorizontalBarModel(List<Aluno> alunos, Integer inicio, Integer fim) {

        horizontalBarModel = new BarChartModel();
        BarModelDesistentes = new BarChartModel();


        List<String> cursosStr = new ArrayList<>();

        for (Aluno aluno : alunos) {
            if (!cursosStr.contains(aluno.getCurso().getNome())) {
                cursosStr.add(aluno.getCurso().getNome());
            }
        }

        for (String nomeCurso : cursosStr) {
            ChartSeries chartSeriesAtivo = new ChartSeries();
            ChartSeries chartSeriesDesistente = new ChartSeries();
            chartSeriesAtivo.setLabel(nomeCurso + "Matriculados");
            chartSeriesDesistente.setLabel(nomeCurso + "Desistentes");
            int countAt = 0;
            int countDe = 0;

            if (agrupamento.equalsIgnoreCase("ano")) {
                int anofim = alunos.get(alunos.size()-1).getDataInscricao().getYear() + 1900;
                for (int ano = alunos.get(0).getDataInscricao().getYear() + 1900;ano<=anofim;ano++) {
                    for (Aluno aluno : alunos) {
                        if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso) && aluno.getDataInscricao().getYear() + 1900 == ano) {
                            if (aluno.getDesistente()) {
                                countDe++;
                            } else {
                                countAt++;
                            }
                        }
                    }
                    chartSeriesAtivo.set(ano, countAt);
                    chartSeriesDesistente.set(ano, countDe);
                }
            } else if (agrupamento.equalsIgnoreCase("semestre")) {
                for (Aluno aluno : alunos) {
                    if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso)) {
                    }
                }
            } else if (agrupamento.equalsIgnoreCase("bimestre")) {

                for (Aluno aluno : alunos) {
                    if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso)) {
                    }
                }
            }

            horizontalBarModel.addSeries(chartSeriesAtivo);
            BarModelDesistentes.addSeries(chartSeriesDesistente);
        }

        horizontalBarModel.setTitle("Matriculas Ativas");
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("e");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Curso");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Qt. Matriculas");
        
        BarModelDesistentes.setTitle("Desistentes");
        BarModelDesistentes.setAnimate(true);
        BarModelDesistentes.setLegendPosition("e");

        xAxis = BarModelDesistentes.getAxis(AxisType.X);
        xAxis.setLabel("Curso");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        yAxis = BarModelDesistentes.getAxis(AxisType.Y);
        yAxis.setLabel("Qt. Matriculas");
    }

    public void gerarRelatorioMatricula() {
        if (cursosSelecionados == null || cursosSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um curso!");
            return;
        }

        if (anoFim != null && anoInicio != null) {
            if (anoFim < anoInicio) {
                addMessage(null, FacesMessage.SEVERITY_ERROR, "Ano Final inválido!");
                return;
            }
        }

        inicio = anoInicio == null ? 0 : anoInicio;
        fim = anoFim == null ? 4000 : anoFim;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
        String data;

        if (inicio == 0) {
            dataInicio = null;
        } else {
            data = "01-01-" + inicio;
            try {
                dataInicio = formatter.parse(data);
            } catch (ParseException e) {

            }
        }

        if (fim == 0) {
            dataFim = null;
        } else {
            data = "31-12-" + fim;
            try {
                dataFim = formatter.parse(data);
            } catch (ParseException e) {

            }
        }

        List<Aluno> alunosRelatorioGerencial = alunoHelper.getAlunosByIdCurso(cursosSelecionados, dataInicio, dataFim);

        createHorizontalBarModel(alunosRelatorioGerencial, inicio, fim);

    }

    public void gerarRelatorioMatriculaRoll() {
        if (cursosSelecionados == null || cursosSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um curso!");
            return;
        }

        if (dataFim != null && dataInicio != null) {
            if (dataFim.before(dataInicio)) {
                addMessage(null, FacesMessage.SEVERITY_ERROR, "Data fim inválida!");
                return;
            }
        }

        alunosInscritos = alunoHelper.getAlunosByIdCurso(cursosSelecionados, dataInicio, dataFim);

    }

    private List<Aluno> alunosRanking;

    public List<Aluno> getAlunosRanking() {
        return alunosRanking;
    }

    private List<TurmaAluno> turmaAlunosRelatorio;

    public List<TurmaAluno> getTurmaAlunosRelatorio() {
        return turmaAlunosRelatorio;
    }

    private TurmaSimulado turmaSimuladoRelatorio;

    public TurmaSimulado getTurmaSimuladoRelatorio() {
        return turmaSimuladoRelatorio;
    }

    public void gerarRelatorioRankingNotasRoll() {

        String turmaId;
        String simuladoId;

        if (simuladoSelecionado != null && !simuladoSelecionado.isEmpty()) {
            StringTokenizer st = new StringTokenizer(simuladoSelecionado, ";");
            turmaId = st.nextToken();
            simuladoId = st.nextToken();
        } else {
            return;
        }

        turmaSimuladoRelatorio = simuladoHelper.getTurmaSimuladoByIdTurmaAlunoEager(new TurmaSimuladoId(Integer.parseInt(simuladoId), Integer.parseInt(turmaId)));
        turmaAlunosRelatorio = new ArrayList<>(turmaSimuladoRelatorio.getTurma().getTurmaAlunos());

    }

    private List<Turma> turmasCadastradas;

    public List<Turma> getTurmasCadastradas() {
        return turmasCadastradas;
    }

    public void gerarRelatorioTurmasCadastradasRoll() {
        if (cursosSelecionados == null || cursosSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um curso!");
            return;
        }

        turmasCadastradas = turmaHelper.getTurmas(cursosSelecionados);

    }

    public void gerarRelatorioProfessoresCadastradosRoll() {
        if (professoresAtividadeSelecionados == null || professoresAtividadeSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um tipo de cadastro!");
            return;
        }

        professoresCadastrados = professorHelper.getProfessores(professoresAtividadeSelecionados);

    }

    public void gerarRelatorioFuncionariosCadastradosRoll() {
        if (professoresAtividadeSelecionados == null || professoresAtividadeSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um tipo de cadastro!");
            return;
        }

        funcionariosCadastrados = funcionarioHelper.getFuncionarios(professoresAtividadeSelecionados);

    }

    public List<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }

    public Integer getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(Integer anoFim) {
        this.anoFim = anoFim;
    }

    public Integer getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(Integer anoInicio) {
        this.anoInicio = anoInicio;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Curso> getCursosParaRelatorio() {
        return cursosParaRelatorio;
    }

    private String simuladoSelecionado;

    private String cursoSelecionadoRanking;

    public String getCursoSelecionadoRanking() {
        return cursoSelecionadoRanking;
    }

    public void setCursoSelecionadoRanking(String cursoSelecionadoRanking) {
        this.cursoSelecionadoRanking = cursoSelecionadoRanking;
    }

    public String getSimuladoSelecionado() {
        return simuladoSelecionado;
    }

    public void setSimuladoSelecionado(String simuladoSelecionado) {
        this.simuladoSelecionado = simuladoSelecionado;
    }

    public List<String> getCursosSelecionados() {
        return cursosSelecionados;
    }

    public void setCursosSelecionados(List<String> cursosSelecionados) {
        this.cursosSelecionados = cursosSelecionados;
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

    public Integer getFim() {
        return fim;
    }

    public BarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public Integer getInicio() {
        return inicio;
    }

    public List<String> getProfessoresAtividadeSelecionados() {
        return professoresAtividadeSelecionados;
    }

    public void setProfessoresAtividadeSelecionados(List<String> professoresAtividadeSelecionados) {
        this.professoresAtividadeSelecionados = professoresAtividadeSelecionados;
    }

    public List<Professor> getProfessoresCadastrados() {
        return professoresCadastrados;
    }

    private void limparCampos() {
        this.anoFim = null;
        this.anoInicio = null;
        this.cursosSelecionados = null;
        this.dataInicio = null;
        this.dataFim = null;
        this.cursos = null;
        this.cursosParaRelatorio = null;
        this.professoresAtividadeSelecionados = null;
        this.cursoSelecionadoRanking = null;
        this.simuladoSelecionado = null;
        this.alunosRanking = null;
        this.alunosInscritos = null;
        this.disciplinasCadastradas = null;
        this.turmaAlunosRelatorio = null;
        this.agrupamento = null;
    }

    public String getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(String agrupamento) {
        this.agrupamento = agrupamento;
    }

    public String novoRelatorioMatricula() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        horizontalBarModel = null;
        return "/restrito/administrador/relatorio/matricula?faces-redirect=true";
    }

    public String novoRelatorioMatriculaRoll() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        return "/restrito/administrador/relatorio/roll/alunosInscritos?faces-redirect=true";
    }

    public String novoRelatorioProfessoresCadastradosRoll() {
        limparCampos();
        return "/restrito/administrador/relatorio/roll/professoresCadastrados?faces-redirect=true";
    }

    public String novoRelatorioFuncionariosCadastradosRoll() {
        limparCampos();
        return "/restrito/administrador/relatorio/roll/funcionariosCadastrados?faces-redirect=true";
    }

    public String novoRelatorioTurmasCadastradasRoll() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        return "/restrito/administrador/relatorio/roll/turmasCadastradas?faces-redirect=true";
    }

    private List<TurmaSimulado> simulados;

    public List<TurmaSimulado> getSimulados() {
        return simulados;
    }

    public String novoRelatorioRankingNotasRoll() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        simulados = null;
        return "/restrito/administrador/relatorio/roll/rankingNotas?faces-redirect=true";
    }

    public List<Disciplina> getDisciplinasCadastradas() {
        return disciplinasCadastradas;
    }

    public String novoRelatorioDisciplinasCadastradasRoll() {
        limparCampos();
        disciplinasCadastradas = disciplinaHelper.getDisciplinas(null);
        return "/restrito/administrador/relatorio/roll/disciplinasCadastradas?faces-redirect=true";
    }

    private List<Curso> cursosCadastrados;

    public List<Curso> getCursosCadastrados() {
        return cursosCadastrados;
    }

    public String novoRelatorioCursosCadastradosRoll() {
        limparCampos();
        cursosCadastrados = cursoHelper.getCursos(null);
        return "/restrito/administrador/relatorio/roll/cursosCadastrados?faces-redirect=true";
    }

    public String now() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String logo = externalContext.getRealPath("") + File.separator + "resource" + File.separator + "imagens" + File.separator + "idealizarRelatorio.png";

        pdf.add(Image.getInstance(logo));
    }

    public Integer totalAtivoIntegral() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosAtivosPorAnoIntegral(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalAtivoManha() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosAtivosPorAnoManha(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalAtivoNoite() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosAtivosPorAnoNoite(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalAtivoTarde() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosAtivosPorAnoTarde(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalDesistenteIntegral() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosDesistentesPorAnoIntegral(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalDesistenteManha() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosDesistentesPorAnoManha(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalDesistenteNoite() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosDesistentesPorAnoNoite(inicio, fim).size();
        }
        return soma;
    }

    public Integer totalDesistenteTarde() {
        Integer soma = 0;
        for (Curso curso : cursosParaRelatorio) {
            soma += curso.getAlunosDesistentesPorAnoTarde(inicio, fim).size();
        }
        return soma;
    }

}
