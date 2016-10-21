package controller;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Assunto;
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

    private String codigo;
    private String codigoAlterar;

    private Curso cursoDetalhe;

    private CursoHelper cursoHelper;
    private String descricao;
    private String descricaoAlterar;
    private DisciplinaHelper disciplinaHelper;
    private Integer disciplinaSelecionada;
    private Integer disciplinaSelecionadaAlterar;
    private List<Disciplina> disciplinasCurso;
    private List<Disciplina> disciplinasCursoAlterar;
    private List<Disciplina> disciplinasDisponiveis;
    private List<Disciplina> disciplinasDisponiveisAlterar;
    private String nome;
    private String nomeAlterar;
    private List<Curso> resultadoConsulta;
    private String stringConsulta;
    private Integer subdisciplinaSelecionada;
    private List<Assunto> subdisciplinas;

    public List<Disciplina> getDisciplinasDisponiveisAlterar() {
        return disciplinasDisponiveisAlterar;
    }

    public Integer getDisciplinaSelecionadaAlterar() {
        return disciplinaSelecionadaAlterar;
    }

    public void setDisciplinaSelecionadaAlterar(Integer disciplinaSelecionadaAlterar) {
        this.disciplinaSelecionadaAlterar = disciplinaSelecionadaAlterar;
    }

    public CursoController() {

        cursoHelper = new CursoHelper();
        disciplinaHelper = new DisciplinaHelper();

    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public String alterarCurso() {
        return null;
    }

    public void cadastrar() {

        Curso curso = new Curso();
        curso.setDescricao(descricao);
        curso.setNome(nome);
        curso.setDisciplinas(new HashSet<>(disciplinasCurso));
        curso.setCodigo(codigo);

        if (!cursoHelper.cadastrar(curso)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Curso, Tente novamente!");
            return;

        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Curso Cadastrado com Sucesso!");

        limparCampos();

    }

    public void consultar() {
        resultadoConsulta = cursoHelper.getCursos(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
    }

    public void excluirDisciplina(Disciplina disciplina) {
        if (disciplina != null) {
            disciplinasCurso.remove(disciplina);
            disciplinasDisponiveis.add(disciplina);
        }
        disciplinaSelecionada = null;
    }
    
    public void excluirDisciplinaAlterar(Disciplina disciplina) {
        if (disciplina != null) {
            disciplinasCursoAlterar.remove(disciplina);
            disciplinasDisponiveisAlterar.add(disciplina);
        }
        disciplinaSelecionadaAlterar = null;
    }

    public void exibirDetalhes(Curso curso) {
        cursoDetalhe = curso;
        codigoAlterar = cursoDetalhe.getCodigo();
        nomeAlterar = cursoDetalhe.getNome();
        descricaoAlterar = cursoDetalhe.getDescricao();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoAlterar() {
        return codigoAlterar;
    }

    public void setCodigoAlterar(String codigoAlterar) {
        this.codigoAlterar = codigoAlterar;
    }

    public Curso getCursoDetalhe() {
        return cursoDetalhe;
    }

    public void setCursoDetalhe(Curso cursoDetalhe) {
        this.cursoDetalhe = cursoDetalhe;
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

    public Integer getDisciplinaSelecionada() {
        return disciplinaSelecionada;
    }

    public void setDisciplinaSelecionada(Integer disciplinaSelecionada) {
        this.disciplinaSelecionada = disciplinaSelecionada;
    }

    public List<Disciplina> getDisciplinasCurso() {
        return disciplinasCurso;
    }

    public List<Disciplina> getDisciplinasDisponiveis() {
        return disciplinasDisponiveis;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeAlterar() {
        return nomeAlterar;
    }

    public void setNomeAlterar(String nomeAlterar) {
        this.nomeAlterar = nomeAlterar;
    }

    public List<Curso> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }

    public Integer getSubdisciplinaSelecionada() {
        return subdisciplinaSelecionada;
    }

    public void setSubdisciplinaSelecionada(Integer subdisciplinaSelecionada) {
        this.subdisciplinaSelecionada = subdisciplinaSelecionada;
    }

    public List<Assunto> getSubdisciplinas() {
        return subdisciplinas;
    }

    public void setSubdisciplinas(List<Assunto> subdisciplinas) {
        this.subdisciplinas = subdisciplinas;
    }

    public String inativar() {
        return null;
    }

    public List<Disciplina> getDisciplinasCursoAlterar() {
        return disciplinasCursoAlterar;
    }

    public void incluirDisciplina() {
        if (disciplinaSelecionada != null) {
            if (disciplinasCurso == null) {
                disciplinasCurso = new ArrayList<>();
            }
            for (Disciplina disciplina : disciplinasDisponiveis) {
                if (disciplina.getIdDisciplina().equals(disciplinaSelecionada)) {
                    if (!disciplinasCurso.contains(disciplina)) {
                        disciplinasCurso.add(disciplina);
                        disciplinasDisponiveis.remove(disciplina);
                        break;
                    }
                }
            }
        }
    }
    
    public void incluirDisciplinaAlterar() {
        if (disciplinaSelecionadaAlterar != null) {
            if (disciplinasCursoAlterar == null) {
                disciplinasCursoAlterar = new ArrayList<>();
            }
            for (Disciplina disciplina : disciplinasDisponiveisAlterar) {
                if (disciplina.getIdDisciplina().equals(disciplinaSelecionadaAlterar)) {
                    if (!disciplinasCursoAlterar.contains(disciplina)) {
                        disciplinasCursoAlterar.add(disciplina);
                        disciplinasDisponiveisAlterar.remove(disciplina);
                        break;
                    }
                }
            }
        }
    }

    public void limparCampos() {

        nome = null;
        descricao = null;
        if (disciplinasCurso != null) {
            for (Disciplina disciplina : disciplinasCurso) {
                disciplinasDisponiveis.add(disciplina);
            }
        }
        disciplinasCurso = null;
        codigo = null;
        stringConsulta = null;

    }

    public String limparFormularioCadastro() {
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
    }

    public String novaConsulta() {
        limparCampos();
        consultar();
        return "/restrito/cadastro/consulta/curso?faces-redirect=true";
    }

    public String novoCadastro() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        limparCampos();
        return "/restrito/administrador/cadastro/curso?faces-redirect=true";
    }

    public void carregarDisciplinas() {

        disciplinasDisponiveisAlterar = disciplinaHelper.getDisciplinasDisponiveis();
        
        List<Disciplina> excluir = new ArrayList<>();
        disciplinasCursoAlterar = new ArrayList<>(cursoDetalhe.getDisciplinas());
        
        if (disciplinasDisponiveisAlterar != null) {
            for (Disciplina disciplina : disciplinasCursoAlterar) {
                for (Disciplina disciplinaDisponivel : disciplinasDisponiveisAlterar) {
                    if (disciplina.getIdDisciplina().equals(disciplinaDisponivel.getIdDisciplina())) {
                        excluir.add(disciplinaDisponivel);
                        break;
                    }
                }
            }
        }
        
        disciplinasDisponiveisAlterar.removeAll(excluir);
        disciplinaSelecionadaAlterar = null;
    }
    
    public void salvarAlteracao(){
        Curso cursoAlterar = cursoDetalhe;
        
        if(cursoAlterar.getCodigo().equals(codigoAlterar)&&
                cursoAlterar.getDescricao().equals(descricaoAlterar)&&
                cursoAlterar.getNome().equals(nomeAlterar)&&
                !isAlteracaoDisciplinas()){
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }
        
        cursoAlterar.setCodigo(codigoAlterar);
        cursoAlterar.setDescricao(descricaoAlterar);
        cursoAlterar.setNome(nomeAlterar);
        cursoAlterar.setDisciplinas(new HashSet<>(disciplinasCursoAlterar));
        
        if(!cursoHelper.salvarAlteracaoCurso(cursoAlterar)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }
        
        addMessage(null,FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");
        
        consultar();
        cursoDetalhe = cursoAlterar;
        nomeAlterar = cursoDetalhe.getNome();
        codigoAlterar = cursoDetalhe.getCodigo();
        descricaoAlterar = cursoDetalhe.getDescricao();
        disciplinasCursoAlterar = new ArrayList<>(cursoDetalhe.getDisciplinas());
    }

    public void onChangeDisciplina() {
        if (disciplinaSelecionada != null && !disciplinaSelecionada.equals(0)) {
            subdisciplinas = disciplinaHelper.getSubdisciplinas(disciplinaSelecionada);
        } else {
            subdisciplinas = new ArrayList<>();
        }
    }

    private boolean isAlteracaoDisciplinas() {
        
        if(disciplinasCursoAlterar.size()!=cursoDetalhe.getDisciplinas().size()){
            return true;
        }
        
        boolean encontrado;
        
        for(Disciplina disciplina: disciplinasCursoAlterar){
            encontrado =false;
            for(Disciplina disciplinaAntiga: cursoDetalhe.getDisciplinas()){
                if(disciplina.getIdDisciplina().equals(disciplinaAntiga.getIdDisciplina())){
                    encontrado = true;
                    break;
                }
                if(!encontrado){
                    return true;
                }
            }
        }
        
        return false;
    }
}
