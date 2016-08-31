package controller;

import entidade.Usuario;
import helper.LoginHelper;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LoginController {

    private final LoginHelper helper;
    private String senha;
    private String usuario;
    private Usuario usuarioLogado;

    public LoginController() {
        helper = new LoginHelper();
    }

    public String entrar() {
        usuarioLogado = helper.getByLoginSenha(usuario, senha);
        if (usuarioLogado == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos!", "Usuário ou senha inválidos!");
            FacesContext.getCurrentInstance().addMessage("", message);
            return null;
        }
        //helper.atualizarDataAcesso(usuarioLogado);
        
        return "/home?faces-redirect=true";
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public String sair() {
        usuarioLogado = null;
        usuario = null;
        senha = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }
    
    public String redirecionarHome() throws IOException{
 
        if(usuarioLogado==null)
             return "/index?faces-redirect=true"; 
        if(usuarioLogado.isAdministrador())
             return "/restrito/administrador/home.xhtml?faces-redirect=true";
        if(usuarioLogado.isCadastro())
            return "/restrito/cadastro/home.xhtml?faces-redirect=true";
        if(usuarioLogado.isProfessor())
            return "/restrito/professor/home.xhtml?faces-redirect=true";
        if(usuarioLogado.isAluno())
            return "/restrito/aluno/home.xhtml?faces-redirect=true";
        
       return "/index?faces-redirect=true"; 

    }

}
