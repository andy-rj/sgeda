package controller;

import entidade.Foto;
import entidade.Usuario;
import helper.LoginHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class LoginController {
    
    private Foto foto;
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
        carregarFotoPerfil();
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
    
    private void carregarFotoPerfil() {
        foto = helper.getFotoPerfil(usuarioLogado.getPessoa());        
    }
    
     public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            return new DefaultStreamedContent(new ByteArrayInputStream(foto.getImagem()));
        }
    }

}
