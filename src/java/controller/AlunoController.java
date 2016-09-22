package controller;

import entidade.Disciplina;
import entidade.Endereco;
import entidade.Pessoa;
import entidade.Telefone;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.EnvelopeEndereco;
import webservice.CepWebService;

@ManagedBean
@SessionScoped
public class AlunoController {
    
    private String bairro;
    private String cep;
    private String cidade;
    private String complemento;
    private String cpf;
    private Date dataNascimento;
    private String ddd;
    private String descricaoTelefone;
    private String email;
    private String estado;
    private String logradouro;
    private String nome;
    private String numero;
    private Pessoa pessoa;
    private String sexo;
    private String numeroTelefone;
    private Set<Telefone> telefones;
    private String nomeResponsavel;
    private String escolaridade;
    private String cpfResponsavel;
    private String telefoneResponsavel;
    private String curso;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void excluirTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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
        this.numero = telefone;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }
    
    public AlunoController() {
        enderecoEncontrado = true;
    }
    
        public String novoCadastro(){
        limparCampos();
        return "/restrito/cadastro/aluno?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/cadastro/consulta/aluno?faces-redirect=true";
    }
    
    public void limparCampos(){
        bairro = null;
        cep = null;
        cidade = null;
        complemento = null;
        cpf = null;
        cpfResponsavel = null;
        curso = null;
        dataNascimento = null;
        ddd = null;
        descricaoTelefone = null;
        email = null;
        enderecoEncontrado = true;
        escolaridade = null;
        estado = null;
        logradouro = null;
        nome = null;
        nomeResponsavel = null;
        numero = null;
        numeroTelefone = null;
        telefoneResponsavel = null;
        telefones = new HashSet<>();
        sexo = null;
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
    
    public String limparFormularioCadastro(){
        limparCampos();
        return "";
    }
    
    public String cadastrar() {
        return "";
    }
    
    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    private boolean enderecoEncontrado;

    public boolean isEnderecoEncontrado() {
        return enderecoEncontrado;
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
    
}
