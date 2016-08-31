package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SimuladoController {
    
    private String nome;
    private String descricao;
    private String questao;
    private String disciplina;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
     
    public SimuladoController() {
    }
    
        public String novoCadastro(){
        limparCampos();
        return "/restrito/professor/cadastro/simulado?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/professor/cadastro/consulta/simulado?faces-redirect=true";
    }
    
    public void limparCampos(){
        
    }
    
    public String limparFormularioCadastro(){
        limparCampos();
        return "";
    }
    
    public String cadastrar() {
        return "";
    }
    
}
