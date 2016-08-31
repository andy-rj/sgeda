package controller;

import entidade.Disciplina;
import entidade.Endereco;
import entidade.Pessoa;
import entidade.Telefone;
import java.util.Date;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FuncionarioController {
    
    private String bairro;
    private String cep;
    private String cidade;
    private String complemento;
    private String cpf;
    private Date dataNascimento;
    private String ddd;
    private String descricaoTelefone;
    private String email;
    private Endereco endereco;
    private String estado;
    private String logradouro;
    private String nome;
    private String numero;
    private Pessoa pessoa;
    private String sexo;
    private String telefone;
    private Set<Telefone> telefones;
    private String cargo;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }
    
    public FuncionarioController() {
    }
    
        public String novoCadastro(){
        limparCampos();
        return "/restrito/cadastro/funcionario?faces-redirect=true";
    }
    
    public String novaConsulta(){
        limparCampos();
        return "/restrito/cadastro/consulta/funcionario?faces-redirect=true";
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
