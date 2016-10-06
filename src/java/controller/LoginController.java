package controller;

import entidade.Foto;
import entidade.Usuario;
import helper.LoginHelper;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.ImagemUtil;

@ManagedBean
@SessionScoped
public class LoginController {
    
    private Foto foto;

    public Foto getFoto() {
        return foto;
    }
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

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE || usuarioLogado.getPessoa().getFoto() == null) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            return new DefaultStreamedContent(new ByteArrayInputStream(usuarioLogado.getPessoa().getFoto().getImagem()));
        }
    }
     
     public String alterarSenha(){
         return "/restrito/alterarSenha.xhtml?faces-redirect=true";
     }
     
     public String alterarFoto(){
         return "";
     }

    public void salvarImagem() {
        
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String top = requestParamMap.get("top");
        top = top.replaceAll("px", "");
        String left = requestParamMap.get("left");
        left = left.replace("px", "");
        
        imagemOriginal = cropImage(imagemOriginal, imagemOriginal.getWidth()-Integer.parseInt(left)-IMG_WIDTH, imagemOriginal.getHeight()-Integer.parseInt(top) - IMG_HEIGHT, IMG_WIDTH, IMG_HEIGHT);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            ImageIO.write(imagemOriginal, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            usuarioLogado.getPessoa().getFoto().setImagem(imageInByte);
            height = imagemOriginal.getHeight();
            width = imagemOriginal.getWidth();
            helper.salvarFotoPerfil(usuarioLogado.getPessoa());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        uploaded=false;

    }

    private BufferedImage cropImage(BufferedImage src, int x, int y, int w, int h) {
        BufferedImage dest = src.getSubimage(x, y, w, h);
        return dest;
   }
     
     private int width;

    
    private static final int IMG_WIDTH = 200;
    private static final int IMG_HEIGHT = 200;
    
    public int getWidth() {
        if (usuarioLogado.getPessoa().getFoto() != null) {
            InputStream in = new ByteArrayInputStream(usuarioLogado.getPessoa().getFoto().getImagem());
            BufferedImage bImageFromConvert = null;
            try {
                bImageFromConvert = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            width = bImageFromConvert.getWidth();
        }else{
            width =0;
        }
        return width;
    }

    public int getHeight() {
       if (usuarioLogado.getPessoa().getFoto() != null) {
            InputStream in = new ByteArrayInputStream(usuarioLogado.getPessoa().getFoto().getImagem());
            BufferedImage bImageFromConvert = null;
            try {
                bImageFromConvert = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            height = bImageFromConvert.getHeight();
        }else{
            height =0;
        }
        return height;
    }
    
    BufferedImage imagemOriginal;
    private int height;
    boolean uploaded;

    public boolean isUploaded() {
        return uploaded;
    }
     
    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        try{
            imagemOriginal = ImageIO.read(uploadedFile.getInputstream());
            height = imagemOriginal.getHeight();
            width = imagemOriginal.getWidth();
            int type = imagemOriginal.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            
            Double razao;
            Double dHeight = (double) imagemOriginal.getHeight();
            Double dWidth = (double) imagemOriginal.getWidth();
            
            if(imagemOriginal.getWidth()<=imagemOriginal.getHeight()){
                width = IMG_WIDTH;
            } else {
                razao = dHeight/IMG_HEIGHT;
                width = (new Double(((double)dWidth)/razao)).intValue();
            }
            
            if(imagemOriginal.getHeight()<=imagemOriginal.getWidth()){
                height = IMG_HEIGHT;
            } else {
                razao = dWidth/IMG_WIDTH;
                height = (new Double(((double)dHeight)/razao)).intValue();
            }
            
            imagemOriginal = ImagemUtil.resizeImage(width, height, imagemOriginal, type);
        } catch (IOException e){
            e.printStackTrace();
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            ImageIO.write(imagemOriginal, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            if(usuarioLogado.getPessoa().getFoto()==null) usuarioLogado.getPessoa().setFoto(new Foto());
            usuarioLogado.getPessoa().getFoto().setTipo("jpg");
            usuarioLogado.getPessoa().getFoto().setImagem(imageInByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        uploaded = true;
        
    }

}
