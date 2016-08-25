package controller;

import entidade.Usuario;
import helper.LoginHelper;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
        if(usuarioLogado.isAdminstrador()) return "/restrito/administrador/home?faces-redirect=true";
        if(usuarioLogado.isProfessor()) return "/restrito/professor/home?faces-redirect=true";
        if(usuarioLogado.isAluno()) return "/restrito/aluno/home?faces-redirect=true";
        
        return "/restrito/cadastro/home?faces-redirect=true";
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

}
