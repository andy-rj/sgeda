package controller;

import entidade.Professor;
import entidade.Questao;
import entidade.Simulado;
import entidade.SimuladoQuestao;
import entidade.Assunto;
import helper.ProfessorHelper;
import helper.QuestaoHelper;
import helper.SimuladoHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SimuladoController {

    private String descricao;
    private String disciplina;
    private String nome;
    private QuestaoHelper questaoHelper;
    private List<Questao> questoesCadastradas;
    private List<SimuladoQuestao> questoesSelecionadas;
    private ProfessorHelper professorHelper;
    private SimuladoHelper simuladoHelper;
    
    public List<SimuladoQuestao> getQuestoesSelecionadas() {
        return questoesSelecionadas;
    }
    private Questao questaoSelecionada;

    public Questao getQuestaoSelecionada() {
        return questaoSelecionada;
    }

    public void setQuestaoSelecionada(Questao questaoSelecionada) {
        this.questaoSelecionada = questaoSelecionada;
    }

    public void setQuestoesFiltradas(List<Questao> questoesFiltradas) {
        this.questoesFiltradas = questoesFiltradas;
    }
    private List<Questao> questoesFiltradas;

    public List<Questao> getQuestoesFiltradas() {
        return questoesFiltradas;
    }

    public SimuladoController() {
        questaoHelper = new QuestaoHelper();
        professorHelper = new ProfessorHelper();
        simuladoHelper = new SimuladoHelper();
    }
    
    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public String cadastrar() {
        if(questoesSelecionadas == null || questoesSelecionadas.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Adicione pelo menos uma questão!");
            return "";
        }
        
        if(new BigDecimal(getNotaTotal()).compareTo(BigDecimal.TEN) != 0){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Nota total do simulado diferente de 10");
            return "";
        }
        
        for(SimuladoQuestao questao: questoesSelecionadas){
            if(questao.getValorQuestao() == null){
                addMessage(null, FacesMessage.SEVERITY_ERROR, "Por favor cadastrar a nota para todas as questões!");
                return "";
            }
        }
        
        Simulado simulado = new Simulado();
        simulado.setDescricao(descricao);
        simulado.setNome(nome);
        simulado.setDataCadastro(new Timestamp(new Date().getTime()));
        simulado.setProfessor(professor);
        simulado.setSimuladoQuestaos(new HashSet<>(questoesSelecionadas));
        
        if(!simuladoHelper.cadastrar(simulado)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar simulado, tente novamente!");
            return "";
        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso!");
        
        limparCampos();
        
        return "";
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Questao> getQuestoesCadastradas() {
        return questoesCadastradas;
    }

    public void limparCampos() {
        nome = null;
        questaoSelecionada = null;
        descricao = null;
        questoesSelecionadas = new ArrayList<>();
    }

    public String limparFormularioCadastro() {
        limparCampos();
        return "";
    }

    public String novaConsulta() {
        limparCampos();
        return "/restrito/professor/consulta/simulado?faces-redirect=true";
    }

    private List<String> instituicoes;

    public List<String> getInstituicoes() {
        return instituicoes;
    }

    public List<String> getAnos() {
        return anos;
    }

    public List<String> getProfessores() {
        return professores;
    }

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public List<String> getAssuntos() {
        return assuntos;
    }
    private List<String> anos;
    private List<String> professores;
    private List<String> disciplinas;
    private List<String> assuntos;

    private Professor professor;
    public String novoCadastro(Integer idProfessorAtual) {
        this.professor = professorHelper.getById(idProfessorAtual);
        limparCampos();
        questoesCadastradas = questaoHelper.getAllQuestoes();
        if (questoesCadastradas != null) {
            anos = new ArrayList<>();
            assuntos = new ArrayList<>();
            disciplinas = new ArrayList<>();
            professores = new ArrayList<>();
            instituicoes = new ArrayList<>();
            for (Questao questao : questoesCadastradas) {
                if (!anos.contains(questao.getAno())) {
                    anos.add(questao.getAno());
                }
                if (!instituicoes.contains(questao.getInstituicao())) {
                    instituicoes.add(questao.getInstituicao());
                }
                if (!professores.contains(questao.getProfessor().getPessoa().getNome())) {
                    professores.add(questao.getProfessor().getPessoa().getNome());
                }
                if (!disciplinas.contains(questao.getDisciplina().getNome())) {
                    disciplinas.add(questao.getDisciplina().getNome());
                }
                if (questao.getAssuntos()!= null) {
                    for (Assunto assunto : questao.getAssuntos()) {
                        if (!assuntos.contains(assunto.getNome())) {
                            assuntos.add(assunto.getNome());
                        }
                    }
                }
            }
            Collections.sort(anos);
            Collections.sort(instituicoes);
            Collections.sort(assuntos);
            Collections.sort(disciplinas);
            Collections.sort(professores);
        }
        return "/restrito/professor/cadastro/simulado?faces-redirect=true";
    }

    public void adicionarQuestaoSelecionada() {
        if (questaoSelecionada != null) {
            if (questoesSelecionadas == null) {
                questoesSelecionadas = new ArrayList<>();
            }
            for(SimuladoQuestao q : questoesSelecionadas){
                if(q.getQuestao().getIdQuestao().equals(questaoSelecionada.getIdQuestao())) return;
            }
            SimuladoQuestao questao = new SimuladoQuestao();
            questao.setQuestao(questaoSelecionada);
            questoesSelecionadas.add(questao);
            questaoSelecionada = null;
        }
    }

    public void excluirQuestao(SimuladoQuestao questao) {
        questoesSelecionadas.remove(questao);
    }

    public String getNotaTotal() {
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for (SimuladoQuestao questao : questoesSelecionadas) {
            if (questao.getValorQuestao() != null) {
                total = total.add(questao.getValorQuestao());
            }
        }
        return total.toPlainString();
    }

    public boolean filtroPorAssunto(Object value, Object filter, Locale locale) {
        String[] filterText = (filter == null) ? null : (String[]) filter;
        if (filterText == null || filterText.length == 0) {
            return true;
        }

        List<Assunto> listaAssuntos = (value == null) ? null : (List<Assunto>) value;

        if (listaAssuntos == null || listaAssuntos.isEmpty()) {
            return false;
        }
        for (String filtro : filterText) {

            for (Assunto assunto : listaAssuntos) {
                if (assunto.getNome().equals(filtro)) {
                    return true;
                }
            }
        }
        return false;
    }

}
