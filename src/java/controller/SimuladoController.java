package controller;

import entidade.Questao;
import entidade.SimuladoQuestao;
import entidade.Subdisciplina;
import helper.QuestaoHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SimuladoController {

    private String descricao;
    private String disciplina;
    private String nome;
    private QuestaoHelper questaoHelper;
    private List<Questao> questoesCadastradas;
    private List<SimuladoQuestao> questoesSelecionadas;

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
    }

    public String cadastrar() {
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

    public String novoCadastro() {
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
                if (questao.getSubdisciplinas() != null) {
                    for (Subdisciplina assunto : questao.getSubdisciplinas()) {
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
            SimuladoQuestao questao = new SimuladoQuestao();
            questao.setQuestao(questaoSelecionada);
            questoesSelecionadas.add(questao);
            questoesCadastradas.remove(questao.getQuestao());
            if (questoesFiltradas != null) {
                questoesFiltradas.remove(questao.getQuestao());
            }
            questaoSelecionada = null;
        }
    }

    public void excluirQuestao(SimuladoQuestao questao) {
        questoesSelecionadas.remove(questao);
        questoesCadastradas.add(questao.getQuestao());
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

        List<Subdisciplina> listaAssuntos = (value == null) ? null : (List<Subdisciplina>) value;

        if (listaAssuntos == null || listaAssuntos.isEmpty()) {
            return false;
        }
        for (String filtro : filterText) {

            for (Subdisciplina assunto : listaAssuntos) {
                if (assunto.getNome().equals(filtro)) {
                    return true;
                }
            }
        }
        return false;
    }

}
