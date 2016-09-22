package controller;

import entidade.Disciplina;
import entidade.Endereco;
import entidade.Foto;
import entidade.Papel;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Telefone;
import entidade.Usuario;
import helper.DisciplinaHelper;
import helper.LoginHelper;
import helper.ProfessorHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.EnvelopeEndereco;
import webservice.CepWebService;
import util.EmailSender;

@ManagedBean
@SessionScoped
public class ProfessorController {
    
    private EmailSender emailSender;
    private String bairro;
    private String cep;
    private String cidade;
    private String complemento;
    @CPF(message = "CPF inválido!")
    private String cpf;
    @NotNull(message = "Data de nascimento é obrigatória!")
    @Past(message = "Data de nascimento inválida!")
    private Date dataNascimento;
    private String ddd;
    private String descricaoTelefone;
    private Set<Disciplina> disciplinas;
    private Set<String> disciplinasSelecionadas;
    private String email;
    private String especializacao;
    private String estado;
    private String instituicaoFormacao;
    private String logradouro;
    private String nome;
    private String numero;
    private String sexo;
    private String numeroTelefone;
    private Set<Telefone> telefones;
    private DisciplinaHelper disciplinaHelper;
    private ProfessorHelper professorHelper;
    private LoginHelper loginHelper;
    private Foto foto;
    private StreamedContent image;

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }
    
    private String stringConsulta;
    
    public ProfessorController() {
        telefones = new HashSet<>();
        enderecoEncontrado = true;
        disciplinaHelper = new DisciplinaHelper();
        professorHelper = new ProfessorHelper();
        loginHelper = new LoginHelper();
        emailSender = new EmailSender();
                
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }

    public void cadastrar() {
        
        if(telefones==null||telefones.isEmpty()){
            addMessage("cadastro:telefone", "Cadastre pelo menos um telefone!");
            return;
        }
        
        if(professorHelper.getByCpf(cpf)!= null){
            addMessage("cadastro:cpf", "CPF já cadastrado!");
            return;
        }
        
        Endereco endereco = new Endereco(null,logradouro, numero, complemento, cep, bairro, cidade, estado);
        Pessoa pessoa = new Pessoa(cpf, "0", nome, dataNascimento, sexo, new Timestamp(new Date().getTime()), email, telefones, endereco);
        endereco.setPessoa(pessoa);
        Professor professor = new Professor(pessoa, especializacao, instituicaoFormacao, disciplinas);
                
        pessoa.setFoto(foto);
        
        for(Telefone telefone:telefones){
            telefone.setPessoa(pessoa);
        }
        
        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if(senha.length()>5) senha = senha.substring(0, 5);
        
        Papel papelProfessor = loginHelper.getPapelProfessor();
        Set<Papel> papeis = new HashSet<>();
        papeis.add(papelProfessor);
        
        Usuario usuario = new Usuario(pessoa, "", senha,true, null, papeis);
        
        if(!professorHelper.cadastrar(pessoa, endereco, professor,usuario)){
            addMessage(null,FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Professor, Tente novamente!");
            return;

        }
        
        addMessage(null, FacesMessage.SEVERITY_INFO ,"Professor Cadastrado com Sucesso!");
        
        if(emailSender.sendTo(email, "Idealizar", "usuário: "+pessoa.getMatricula()+"\n Senha: "+senha)){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Email enviado com informações de login!");
        }else{
            addMessage(null, FacesMessage.SEVERITY_ERROR ,"Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }

        limparCampos();
        
    }

    public void editarTelefone(Telefone telefone) {
        
    }

    public void excluirTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getDescricaoTelefone() {
        return descricaoTelefone;
    }

    public void setDescricaoTelefone(String descricaoTelefone) {
        this.descricaoTelefone = descricaoTelefone;
    }

    public Set<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public Set<String> getDisciplinasSelecionadas() {
        return disciplinasSelecionadas;
    }

    public void setDisciplinasSelecionadas(Set<String> disciplinasSelecionadas) {
        this.disciplinasSelecionadas = disciplinasSelecionadas;
        disciplinas = new HashSet<>();
        for(String id:disciplinasSelecionadas){
            Integer intId = new Integer(id);
            for(Disciplina disciplina: disciplinasDisponiveis){
                if(intId.equals(disciplina.getIdDisciplina())){
                    disciplinas.add(disciplina);
                    break;
                }
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInstituicaoFormacao() {
        return instituicaoFormacao;
    }

    public void setInstituicaoFormacao(String intituicaoFormacao) {
        this.instituicaoFormacao = intituicaoFormacao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero){
        this.numero = numero;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String telefone) {
        this.numeroTelefone = telefone;
    }
    
    public Set<Telefone> getTelefones(){
        return telefones;
    }
    
    public void setTelefones(Set<Telefone> telefones){
        this.telefones = telefones;
    }
    
    public void limparCampos(){
      bairro = null;
      cep = null;
      cidade = null;
      complemento = null;
      cpf = null;
      dataNascimento = null;
      ddd = null;
      descricaoTelefone = null;
      disciplinas = null;
      disciplinasSelecionadas = null;
      email = null;
      especializacao = null;
      estado = null;
      instituicaoFormacao = null;
      logradouro = null;
      nome = null;
      numero = null;
      sexo = null;
      numeroTelefone = null;
      telefones = new HashSet<>();
      foto=null;
      resultadoConsulta = new ArrayList<>();
    }
    
    public String limparFormularioCadastro(){
        limparCampos();
        return "";
    }
    
    public String novoCadastro(){
        limparCampos();
        return "/restrito/administrador/cadastro/professor?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/cadastro/consulta/professor?faces-redirect=true";
    }
    
    public void incluirTelefone() {
        if(ddd==null || ddd.isEmpty()){
            addMessage("cadastro:ddd","DDD inválido!");
            return;
        }
        if(numeroTelefone == null || numeroTelefone.isEmpty()){
            addMessage("cadastro:telefone","Número do telefone inválido");
            return;
        }
        
        Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(numeroTelefone);
        telefone.setDescricao(descricaoTelefone);
        
        if(telefones == null) telefones = new HashSet<>();
        telefones.add(telefone);
        
        ddd = null;
        numeroTelefone = null;
        descricaoTelefone = null;
        
    }
    
    public void incluirTelefone(ActionEvent event) {
        if(ddd==null || ddd.isEmpty()){
            addMessage("cadastro:ddd","DDD inválido!");
            return;
        }
        if(numeroTelefone == null || numeroTelefone.isEmpty()){
            addMessage("cadastro:telefone","Número do telefone inválido");
            return;
        }
        
        Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(numeroTelefone);
        telefone.setDescricao(descricaoTelefone);
        
        telefones.add(telefone);
        
        ddd = null;
        numeroTelefone = null;
        descricaoTelefone = null;
        
    }
    
    public void excluirTelefone(){
        
    }
    
    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void buscarCep(){
        EnvelopeEndereco endereco = CepWebService.buscaEndereco(cep);
        if(endereco == null){
            addMessage("cadastro:cep", "Cep não encontrado! Verifique e tente novamente ou preencha manualmente!");
            cidade = null;
            bairro = null;
            estado = null;
            logradouro = null;
            complemento = null;
            enderecoEncontrado = false;
            return;
        }
        cidade = endereco.getLocalidade();
        bairro = endereco.getBairro();
        estado = endereco.getUf();
        logradouro = endereco.getLogradouro();
        complemento = endereco.getComplemento();
        enderecoEncontrado = true;
        
    }
    
    private boolean enderecoEncontrado;
    private List<Disciplina> disciplinasDisponiveis;
    
    
    public boolean isEnderecoEncontrado(){
        return enderecoEncontrado;
    }
    
     public List<Disciplina> getDisciplinasDisponiveis() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        return disciplinasDisponiveis;
    }
     
     public void limparFormularioConsulta(){
         stringConsulta = null;
         resultadoConsulta = new ArrayList<>();
     }
     
     private List<Professor> resultadoConsulta;

    public List<Professor> getResultadoConsulta() {
        return resultadoConsulta;
    }
     
     public String consultar(){
         resultadoConsulta = professorHelper.getProfessores(stringConsulta);
         if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
             addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
         }
         return "";
     }
     
     private Professor professorDetalhe;

    public Professor getProfessorDetalhe() {
        return professorDetalhe;
    }
     
     public String exibirDetalhes(Professor professor){
        professorDetalhe = professor;
        return "/restrito/cadastro/detalhe/professor?faces-redirect=true";
     }
     
     public String alterarProfessor(){
         return "/restrito/cadastro/detalhe/professor?faces-redirect=true";
     }
     
     public String inativar(){
         return "/restrito/cadastro/detalhe/professor?faces-redirect=true";
     }
     
    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String contentType = uploadedFile.getContentType();
        foto = new Foto();
        foto.setImagem(uploadedFile.getContents());
        image = new DefaultStreamedContent(new ByteArrayInputStream(foto.getImagem()), contentType);
    }
    
    public StreamedContent getImage(){
        return image;
    }
            
}
