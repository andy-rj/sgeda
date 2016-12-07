package controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.sun.mail.util.BEncoderStream;
import entidade.Aluno;
import entidade.Curso;
import entidade.Disciplina;
import entidade.Funcionario;
import entidade.Objetiva;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Questao;
import entidade.Resposta;
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
import helper.QuestaoHelper;
import helper.SimuladoHelper;
import helper.TurmaHelper;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@SessionScoped
public class RelatorioController {

    private String agrupamento;

    private final AlunoHelper alunoHelper;
    private List<Aluno> alunosInscritos;
    private List<Aluno> alunosRanking;
    private Integer anoFim;
    private Integer anoInicio;
    private final CursoHelper cursoHelper;
    private String cursoSelecionadoRanking;
    private List<Curso> cursos;
    private List<Curso> cursosCadastrados;
    private List<Curso> cursosParaRelatorio;
    private List<String> cursosSelecionados;
    private Date dataFim;
    private Date dataInicio;
    private final DisciplinaHelper disciplinaHelper;
    private List<Disciplina> disciplinasCadastradas;
    private Integer fim;
    private final FuncionarioHelper funcionarioHelper;
    private List<Funcionario> funcionariosCadastrados;
    private BarChartModel horizontalBarModel;
    private Integer inicio;
    private final LoginHelper loginHelper;
    private PieChartModel pieModelDesistentes;
    private final ProfessorHelper professorHelper;
    private List<Professor> professores;
    private List<String> professoresAtividadeSelecionados;
    private List<Professor> professoresCadastrados;
    private List<String> professoresSelecionados;
    private final SimuladoHelper simuladoHelper;
    private String simuladoSelecionado;
    private List<TurmaSimulado> simulados;
    private List<TurmaAluno> turmaAlunosRelatorio;
    private final TurmaHelper turmaHelper;
    private TurmaSimulado turmaSimuladoRelatorio;
    private List<Turma> turmasCadastradas;
    private final QuestaoHelper questaoHelper;

    public RelatorioController() {
        cursoHelper = new CursoHelper();
        alunoHelper = new AlunoHelper();
        loginHelper = new LoginHelper();
        turmaHelper = new TurmaHelper();
        professorHelper = new ProfessorHelper();
        disciplinaHelper = new DisciplinaHelper();
        funcionarioHelper = new FuncionarioHelper();
        simuladoHelper = new SimuladoHelper();
        questaoHelper = new QuestaoHelper();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    private LineChartModel lineModel;
    private Integer quantidadeAcertos;

    public Integer getQuantidadeAcertos() {
        return quantidadeAcertos;
    }

    public void itemSelect(ItemSelectEvent event) {
        int questao = event.getSeriesIndex();
        int i = 0;
        for (Map.Entry<Questao, BigDecimal> entry : ordenado.entrySet()) {
            if (i < questao) {
                i++;
                continue;
            }
            if (i > questao) {
                break;
            }

            questaoSelecionada = entry.getKey();
            break;
        }

        quantidadeAcertos = 0;
        for (Resposta resposta : respostas) {
            if (resposta.getQuestao().getIdQuestao().equals(questaoSelecionada.getIdQuestao())) {
                if (resposta.isCorreta()) {
                    quantidadeAcertos++;
                }
            }
        }

    }

    public void itemSelectErrada(ItemSelectEvent event) {
        int questao = event.getSeriesIndex();
        int i = 0;
        for (Map.Entry<Questao, BigDecimal> entry : ordenadoErradas.entrySet()) {
            if (i < questao) {
                i++;
                continue;
            }
            if (i > questao) {
                break;
            }

            questaoSelecionada = entry.getKey();
            break;
        }

        quantidadeAcertos = 0;
        for (Resposta resposta : respostas) {
            if (resposta.getQuestao().getIdQuestao().equals(questaoSelecionada.getIdQuestao())) {
                if (!resposta.isCorreta()) {
                    quantidadeAcertos++;
                }
            }
        }

    }

    LinkedHashMap<Questao, BigDecimal> ordenado;
    LinkedHashMap<Questao, BigDecimal> ordenadoErradas;

    public Questao getQuestaoSelecionada() {
        return questaoSelecionada;
    }

    public void setQuestaoSelecionada(Questao questaoSelecionada) {
        this.questaoSelecionada = questaoSelecionada;
    }

    private BarChartModel horizonBarChartModel2;

    public BarChartModel getHorizonBarChartModel2() {
        return horizonBarChartModel2;
    }

    public void setHorizonBarChartModel2(BarChartModel horizonBarChartModel2) {
        this.horizonBarChartModel2 = horizonBarChartModel2;
    }

    private void createTop10Model(List<Resposta> respostas, Date inicio, Date fim) {

        horizontalBarModel = new BarChartModel();
        horizontalBarModel.setTitle("Top 10 questões mais acertadas");
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("ne");

        horizonBarChartModel2 = new BarChartModel();
        horizonBarChartModel2.setTitle("Top 10 questões mais erradas");
        horizonBarChartModel2.setAnimate(true);
        horizonBarChartModel2.setLegendPosition("ne");

        Map<Questao, BigDecimal> myMap = new HashMap<>();
        Map<Questao, BigDecimal> myMapErradas = new HashMap<>();

        List<Questao> questoes = new ArrayList<>();
        for (Resposta resposta : respostas) {
            if (!questoes.contains(resposta.getQuestao())) {
                questoes.add(resposta.getQuestao());
            }
        }

        for (Questao questao : questoes) {
            int totalRespostas = 0;
            int totalRespostasCorretas = 0;
            for (Resposta resposta : respostas) {
                if (resposta.getQuestao().getIdQuestao().equals(questao.getIdQuestao())) {
                    totalRespostas++;
                    if (resposta.isCorreta()) {
                        totalRespostasCorretas++;
                    }
                }
            }

            if (totalRespostas != 0) {
                BigDecimal corretas = new BigDecimal(totalRespostasCorretas);
                BigDecimal porcentagem = corretas.divide(new BigDecimal(totalRespostas), 2, RoundingMode.HALF_DOWN);
                myMap.put(questao, porcentagem.multiply(new BigDecimal(100)));

                BigDecimal erradas = new BigDecimal(totalRespostas - totalRespostasCorretas);
                porcentagem = erradas.divide(new BigDecimal(totalRespostas), 2, RoundingMode.HALF_DOWN);
                myMapErradas.put(questao, porcentagem.multiply(new BigDecimal(100)));
            }
        }

        //criar serie
//        for (Resposta resposta : respostas) {
//            if (resposta.isCorreta()) {
//                int count = myMap.containsKey(resposta.getQuestao()) ? myMap.get(resposta.getQuestao()) : 0;
//                myMap.put(resposta.getQuestao(), count + 1);
//            }
//        }
        //procurar maior e colocar na serie
        ordenado = sortHashMapByValues(myMap);
        ordenadoErradas = sortHashMapByValues(myMapErradas);

        if (ordenado.isEmpty()) {
            ChartSeries serie = new BarChartSeries();
            serie.setLabel("Questão");
            serie.set(1, 0);
            horizontalBarModel.addSeries(serie);
        }

        if (ordenadoErradas.isEmpty()) {
            ChartSeries serie = new BarChartSeries();
            serie.setLabel("Questão");
            serie.set(1, 0);
            horizonBarChartModel2.addSeries(serie);
        }

        int i = 0;

        for (Map.Entry<Questao, BigDecimal> entry : ordenado.entrySet()) {
            if (i > 10) {
                break;
            }
            i++;
            Questao key = entry.getKey();
            BigDecimal value = entry.getValue();
            ChartSeries serie = new BarChartSeries();
            serie.setLabel("Questão " + key.getIdQuestao().toString());
            serie.set(i, value);
            horizontalBarModel.addSeries(serie);
        }

        i = 0;

        for (Map.Entry<Questao, BigDecimal> entry : ordenadoErradas.entrySet()) {
            if (i > 10) {
                break;
            }
            i++;
            Questao key = entry.getKey();
            BigDecimal value = entry.getValue();
            ChartSeries serie = new BarChartSeries();
            serie.setLabel("Questão " + key.getIdQuestao().toString());
            serie.set(i, value);
            horizonBarChartModel2.addSeries(serie);
        }

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Questão");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("%Acertos");
        yAxis.setMax(100);
        yAxis.setTickInterval("1");
        yAxis.setTickFormat("%d");

        xAxis = horizonBarChartModel2.getAxis(AxisType.X);
        xAxis.setLabel("Questão");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        yAxis = horizonBarChartModel2.getAxis(AxisType.Y);
        yAxis.setLabel("%Erros");
        yAxis.setMax(100);
        yAxis.setTickInterval("1");
        yAxis.setTickFormat("%d");
    }

    public LinkedHashMap<Questao, BigDecimal> sortHashMapByValues(
            Map<Questao, BigDecimal> passedMap) {
        List<Questao> mapKeys = new ArrayList<>(passedMap.keySet());
        List<BigDecimal> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.reverse(mapValues);

        LinkedHashMap<Questao, BigDecimal> sortedMap = new LinkedHashMap<>();

        Iterator<BigDecimal> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            BigDecimal val = valueIt.next();
            Iterator<Questao> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Questao key = keyIt.next();
                BigDecimal comp1 = passedMap.get(key);
                BigDecimal comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    private void createDesempenhoProfessoresModel(List<Professor> professorRelatorioGerencial, Integer inicio, Integer fim) {
        lineModel = new LineChartModel();

        Integer menorAno = 2100;
        Integer maiorAno = -1;

        for (Professor professor : professorRelatorioGerencial) {
            if (professor.getTurmas() != null) {
                for (Turma turma : professor.getTurmas()) {
                    if (turma.getDataFim().getYear() + 1900 <= this.fim && turma.getDataFim().getYear() + 1900 < menorAno) {
                        menorAno = turma.getDataFim().getYear() + 1900;
                    }
                    if (turma.getDataFim().getYear() + 1900 >= this.inicio && turma.getDataFim().getYear() + 1900 > maiorAno) {
                        maiorAno = turma.getDataFim().getYear() + 1900;
                    }
                }
            }
        }

        BigDecimal desempenho;
        int quantidadeTurmas = 0;

        for (Professor professor : professorRelatorioGerencial) {
            ChartSeries chartSeries = new ChartSeries();
            chartSeries.setLabel(professor.getPessoa().getNome());
            desempenho = new BigDecimal(BigInteger.ZERO);
            for (int ano = menorAno; ano <= maiorAno; ano++) {
                desempenho = new BigDecimal(BigInteger.ZERO);
                quantidadeTurmas = 0;
                if (professor.getTurmas() != null) {
                    for (Turma turma : professor.getTurmas()) {
                        if (turma.getDataFim().getYear() + 1900 == ano) {
                            quantidadeTurmas++;
                            desempenho = desempenho.add(turma.getDesempenho());
                        }
                    }
                }
                if (quantidadeTurmas != 0) {
                    desempenho = desempenho.divide(new BigDecimal(quantidadeTurmas), 2, RoundingMode.HALF_DOWN);
                }
                chartSeries.set(ano, desempenho);
            }
            lineModel.addSeries(chartSeries);
        }

        lineModel.setTitle("Desempenho");
        lineModel.setAnimate(true);
        lineModel.setLegendPosition("ne");

        Axis xAxis = lineModel.getAxis(AxisType.X);
        xAxis.setLabel("Professor");
        xAxis.setMin(menorAno - 1);
        xAxis.setMax(maiorAno + 1);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Média das turmas");
        yAxis.setMin(0);
        yAxis.setMax(10);
    }

    private void createHorizontalBarModel(List<Aluno> alunos, Integer inicio, Integer fim) {

        horizontalBarModel = new BarChartModel();
        pieModelDesistentes = new PieChartModel();
        pieModelDesistentes.setTitle("Total de Desistentes");
        pieModelDesistentes.setLegendPosition("s");

        List<String> cursosStr = new ArrayList<>();

        for (Aluno aluno : alunos) {
            if (!cursosStr.contains(aluno.getCurso().getNome())) {
                cursosStr.add(aluno.getCurso().getNome());
            }
        }
        
        int countDeTotal = 0;

        for (String nomeCurso : cursosStr) {
          
                int anofim = alunos.get(alunos.size() - 1).getDataInscricao().getYear() + 1900;
                for (int ano = alunos.get(0).getDataInscricao().getYear() + 1900; ano <= anofim; ano++) {
                    for (Aluno aluno : alunos) {
                        if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso) && aluno.getDataInscricao().getYear() + 1900 == ano) {
                            if (aluno.getDesistente()) {
                                countDeTotal++;
                            } 
                        }
                    }
                }
        }

        for (String nomeCurso : cursosStr) {
            ChartSeries chartSeriesAtivo = new ChartSeries();
            chartSeriesAtivo.setLabel(nomeCurso);

            int countAt = 0;
            int countDe = 0;

            int countAt1S = 0;
            int countDe1S = 0;
            int countAt2S = 0;
            int countDe2S = 0;

            if (agrupamento.equalsIgnoreCase("ano")) {
                int anofim = alunos.get(alunos.size() - 1).getDataInscricao().getYear() + 1900;
                for (int ano = alunos.get(0).getDataInscricao().getYear() + 1900; ano <= anofim; ano++) {
                    countAt = 0;
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
                }
                pieModelDesistentes.set(nomeCurso, new BigDecimal(countDe).divide((new BigDecimal(countDeTotal)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(100)));
            } else if (agrupamento.equalsIgnoreCase("semestre")) {
                int anofim = alunos.get(alunos.size() - 1).getDataInscricao().getYear() + 1900;
                for (int ano = alunos.get(0).getDataInscricao().getYear() + 1900; ano <= anofim; ano++) {
                    countAt1S = 0;
                    countAt2S = 0;
                    for (Aluno aluno : alunos) {
                        if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso) && aluno.getDataInscricao().getYear() + 1900 == ano) {
                            if (aluno.getDesistente()) {
                                if ((aluno.getDataInscricao().getMonth() / 6) == 0) {
                                    countDe1S++;
                                } else {
                                    countDe2S++;
                                }
                            } else {
                                if ((aluno.getDataInscricao().getMonth() / 6) == 0) {
                                    countAt1S++;
                                } else {
                                    countAt2S++;
                                }
                            }
                        }
                    }
                    chartSeriesAtivo.set(ano + "/1º Sem", countAt1S);
                    chartSeriesAtivo.set(ano + "/2º Sem", countAt2S);
                }
                pieModelDesistentes.set(nomeCurso, new BigDecimal(countDe1S + countDe2S).divide((new BigDecimal(countDeTotal)), 2, RoundingMode.HALF_DOWN)
                    .multiply(new BigDecimal(100)));
            }
            

            horizontalBarModel.addSeries(chartSeriesAtivo);
        }

        horizontalBarModel.setTitle("Matrículas Ativas");
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("ne");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Curso");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Qt. Matriculas");
        pieModelDesistentes.setShowDataLabels(true);

    }

    private void createModelAprovados(List<Aluno> alunos, Integer inicio, Integer fim) {

        horizontalBarModel = new BarChartModel();
        pieModelDesistentes = new PieChartModel();
        pieModelDesistentes.setTitle("Percentual de Aprovados");
        pieModelDesistentes.setLegendPosition("s");

        List<String> cursosStr = new ArrayList<>();

        for (Aluno aluno : alunos) {
            if (!cursosStr.contains(aluno.getCurso().getNome())) {
                cursosStr.add(aluno.getCurso().getNome());
            }
        }

        for (String nomeCurso : cursosStr) {
            ChartSeries chartSeriesAtivo = new ChartSeries();
            chartSeriesAtivo.setLabel(nomeCurso);

            int countNAp = 0;
            int countAp = 0;
            int countApTotal = 0;

            int totalAlunosApRe = alunos.size();

            int anofim = alunos.get(alunos.size() - 1).getDataInscricao().getYear() + 1900;
            for (int ano = alunos.get(0).getDataInscricao().getYear() + 1900; ano <= anofim; ano++) {
                countAp = 0;
                for (Aluno aluno : alunos) {
                    if (aluno.getCurso().getNome().equalsIgnoreCase(nomeCurso) && aluno.getDataInscricao().getYear() + 1900 == ano) {
                        if (aluno.getAprovado() == 1) {
                            countAp++;
                            countApTotal++;
                        } else {
                            countNAp++;
                        }
                    }
                }
                chartSeriesAtivo.set(ano, countAp);
            }
            BigDecimal porcentagem = new BigDecimal(BigInteger.ZERO);
            if (totalAlunosApRe != 0) {
                porcentagem = ((new BigDecimal(100)).multiply(new BigDecimal(countApTotal))).divide(new BigDecimal(totalAlunosApRe), 2, RoundingMode.HALF_UP);
            }
            pieModelDesistentes.set(nomeCurso, porcentagem);
            horizontalBarModel.addSeries(chartSeriesAtivo);
        }

        horizontalBarModel.setTitle("Aprovados");
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("ne");

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Curso");
        xAxis.setMin(0);
        xAxis.setTickInterval("1");
        xAxis.setTickFormat("%d");

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade de Alunos");
        pieModelDesistentes.setShowDataLabels(true);

    }

    public void gerarRelatorioAprovados() {
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

        inicio = anoInicio == null ? 1950 : anoInicio;
        fim = anoFim == null ? 2030 : anoFim;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

        createModelAprovados(alunosRelatorioGerencial, inicio, fim);

    }

    public void gerarRelatorioFuncionariosCadastradosRoll() {
        if (professoresAtividadeSelecionados == null || professoresAtividadeSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um tipo de cadastro!");
            return;
        }

        funcionariosCadastrados = funcionarioHelper.getFuncionarios(professoresAtividadeSelecionados);

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

        inicio = anoInicio == null ? 1950 : anoInicio;
        fim = anoFim == null ? 2030 : anoFim;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

    public void gerarRelatorioTop10() {
        if (disciplinasSelecionadas == null || disciplinasSelecionadas.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos uma disciplina!");
            return;
        }

        if (anoFim != null && anoInicio != null) {
            if (anoFim < anoInicio) {
                addMessage(null, FacesMessage.SEVERITY_ERROR, "Ano Final inválido!");
                return;
            }
        }

        inicio = anoInicio == null ? 1950 : anoInicio;
        fim = anoFim == null ? 2030 : anoFim;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

        respostas = questaoHelper.getRespostasPorDisciplina(disciplinasSelecionadas, dataInicio, dataFim);

        createTop10Model(respostas, dataInicio, dataFim);

    }
    private List<Resposta> respostas;

    public void gerarRelatorioDesempenhoProfessores() {
        if (professoresSelecionados == null || professoresSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um professor!");
            return;
        }

        if (anoFim != null && anoInicio != null) {
            if (anoFim < anoInicio) {
                addMessage(null, FacesMessage.SEVERITY_ERROR, "Ano Final inválido!");
                return;
            }
        }

        inicio = anoInicio == null ? 1950 : anoInicio;
        fim = anoFim == null ? 2030 : anoFim;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

        List<Professor> professorRelatorioGerencial = professorHelper.getProfessoresByIdRelatorio(professoresSelecionados, dataInicio, dataFim);

        createDesempenhoProfessoresModel(professorRelatorioGerencial, inicio, fim);

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

    public void gerarRelatorioProfessoresCadastradosRoll() {
        if (professoresAtividadeSelecionados == null || professoresAtividadeSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um tipo de cadastro!");
            return;
        }

        professoresCadastrados = professorHelper.getProfessores(professoresAtividadeSelecionados);

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

    public void gerarRelatorioTurmasCadastradasRoll() {
        if (cursosSelecionados == null || cursosSelecionados.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Selecione pelo menos um curso!");
            return;
        }

        turmasCadastradas = turmaHelper.getTurmas(cursosSelecionados);

    }

    public String getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(String agrupamento) {
        this.agrupamento = agrupamento;
    }

    public List<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }

    public List<Aluno> getAlunosRanking() {
        return alunosRanking;
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

    public String getCursoSelecionadoRanking() {
        return cursoSelecionadoRanking;
    }

    public void setCursoSelecionadoRanking(String cursoSelecionadoRanking) {
        this.cursoSelecionadoRanking = cursoSelecionadoRanking;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Curso> getCursosCadastrados() {
        return cursosCadastrados;
    }

    public List<Curso> getCursosParaRelatorio() {
        return cursosParaRelatorio;
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

    public List<Disciplina> getDisciplinasCadastradas() {
        return disciplinasCadastradas;
    }

    public Integer getFim() {
        return fim;
    }

    public List<Funcionario> getFuncionariosCadastrados() {
        return funcionariosCadastrados;
    }

    public BarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public Integer getInicio() {
        return inicio;
    }

    public PieChartModel getPieModelDesistentes() {
        return this.pieModelDesistentes;
    }

    public List<Professor> getProfessores() {
        return professores;
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

    public List<String> getProfessoresSelecionados() {
        return professoresSelecionados;
    }

    public void setProfessoresSelecionados(List<String> professoresSelecionados) {
        this.professoresSelecionados = professoresSelecionados;
    }

    public String getSimuladoSelecionado() {
        return simuladoSelecionado;
    }

    public void setSimuladoSelecionado(String simuladoSelecionado) {
        this.simuladoSelecionado = simuladoSelecionado;
    }

    public List<TurmaSimulado> getSimulados() {
        return simulados;
    }

    public List<TurmaAluno> getTurmaAlunosRelatorio() {
        return turmaAlunosRelatorio;
    }

    public TurmaSimulado getTurmaSimuladoRelatorio() {
        return turmaSimuladoRelatorio;
    }

    public List<Turma> getTurmasCadastradas() {
        return turmasCadastradas;
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
        this.agrupamento = "Ano";
        this.horizontalBarModel = null;
        this.pieModelDesistentes = null;
        this.professores = null;
        this.lineModel = null;
        this.professoresSelecionados = null;
        this.ordenado = null;
        this.questaoSelecionada = null;
        this.disciplinasSelecionadas = null;
    }

    private Questao questaoSelecionada;

    public String novoRelatorioAprovados() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        return "/restrito/administrador/relatorio/aprovados?faces-redirect=true";
    }

    public String novoRelatorioCursosCadastradosRoll() {
        limparCampos();
        cursosCadastrados = cursoHelper.getCursos(null);
        return "/restrito/administrador/relatorio/roll/cursosCadastrados?faces-redirect=true";
    }

    public String novoRelatorioDesempenhoProfessores() {
        limparCampos();
        professores = professorHelper.getProfessoresComTurma("");
        return "/restrito/administrador/relatorio/desempenhoProfessores?faces-redirect=true";
    }

    private String disciplinasSelecionadas;

    public String getDisciplinasSelecionadas() {
        return disciplinasSelecionadas;
    }

    public void setDisciplinasSelecionadas(String disciplinasSelecionadas) {
        this.disciplinasSelecionadas = disciplinasSelecionadas;
    }

    public String novoRelatorioTop10() {
        limparCampos();
        disciplinasCadastradas = disciplinaHelper.getDisciplinasDisponiveisQuestaoEager();
        List<Disciplina> excluir = new ArrayList<>();
        for (Disciplina disciplina : disciplinasCadastradas) {
            if (disciplina.getQuestaos() == null || disciplina.getQuestaos().isEmpty()) {
                excluir.add(disciplina);
            }
        }
        if (!excluir.isEmpty()) {
            disciplinasCadastradas.removeAll(excluir);
        }

        return "/restrito/administrador/relatorio/topQuestoes?faces-redirect=true";
    }

    public String novoRelatorioDisciplinasCadastradasRoll() {
        limparCampos();
        disciplinasCadastradas = disciplinaHelper.getDisciplinas(null);
        return "/restrito/administrador/relatorio/roll/disciplinasCadastradas?faces-redirect=true";
    }

    public String novoRelatorioFuncionariosCadastradosRoll() {
        limparCampos();
        return "/restrito/administrador/relatorio/roll/funcionariosCadastrados?faces-redirect=true";
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

    public String novoRelatorioRankingNotasRoll() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        simulados = null;
        return "/restrito/administrador/relatorio/roll/rankingNotas?faces-redirect=true";
    }

    public String novoRelatorioTurmasCadastradasRoll() {
        limparCampos();
        cursos = cursoHelper.getCursosDisponiveisEager();
        return "/restrito/administrador/relatorio/roll/turmasCadastradas?faces-redirect=true";
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
        String logo = externalContext.getRealPath("") + File.separator + "resource" + File.separator + "imagens" + File.separator + "logoFundoCinza.png";

        pdf.add(Image.getInstance(logo));
    }

    private int totalAlunosAprovadosReprovados(List<Aluno> alunos) {
        int count = 0;
        for (Aluno aluno : alunos) {
            if (aluno.getAprovado() == 0 || aluno.getAprovado() == 1) {
                count++;
            }
        }
        return count;
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

    public void updateCurso() {
        Curso curso = cursoHelper.getCursoByIdEagerTurmaSimulado(Integer.parseInt(cursoSelecionadoRanking));
        simulados = curso.getTurmasSimulados();
    }

}
