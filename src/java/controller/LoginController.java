package controller;

import entidade.Foto;
import entidade.Usuario;
import helper.LoginHelper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
    private String senhaAtual;

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }
    private String novaSenha;
    private String confirmaSenha;

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
    
    public void confirmarAlterarSenha(){
        
        try{
        
        if(!LoginHelper.md5(senhaAtual).equals(usuarioLogado.getSenha())){
            addMessage("cadastro:atual", FacesMessage.SEVERITY_ERROR, "Senha não confere!");
            return;
        }
            }catch(NoSuchAlgorithmException e){
                
            }
        if(!confirmaSenha.equals(novaSenha)){
            addMessage("cadastro:confirma", FacesMessage.SEVERITY_ERROR, "Senha não confere!");
            return;
        }
        
        if(!helper.alterarSenha(novaSenha,usuarioLogado)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "A senha não foi alterada! Tente novamente!");
        }
        addMessage(null, FacesMessage.SEVERITY_INFO, "Senha alterada com sucesso!");
    }
    
    public String cancelarAlterarSenha(){
        return  "/home?faces-redirect=true";
    }
    
     public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
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
     
     public String alterarSenha(){
         return "/restrito/alterarSenha.xhtml?faces-redirect=true";
     }
     
     public String alterarFoto(){
         return "";
     }

}
