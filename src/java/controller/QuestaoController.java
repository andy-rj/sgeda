package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class QuestaoController {
    
     
     private String enunciado;
     private String ano;
     private String instituicao;
     private UploadedFile file;
     
     public void upload(){
         
     }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
     
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
     
     private String tipo;
   
     
    public QuestaoController() {
    }
    
        public String novoCadastro(){
        limparCampos();
        return "/restrito/professor/cadastro/questao?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/professor/cadastro/consulta/questao?faces-redirect=true";
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
