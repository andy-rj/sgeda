package controller;

import entidade.Aluno;
import entidade.AlunoSimulado;
import entidade.Disciplina;
import entidade.Endereco;
import entidade.Foto;
import entidade.Papel;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Simulado;
import entidade.Telefone;
import entidade.TurmaSimulado;
import entidade.Usuario;
import helper.DisciplinaHelper;
import helper.LoginHelper;
import helper.ProfessorHelper;
import helper.SimuladoHelper;
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
import org.primefaces.model.CroppedImage;
import org.primefaces.model.StreamedContent;
import util.EnvelopeEndereco;
import webservice.CepWebService;
import util.EmailSender;

@ManagedBean
@SessionScoped
public class ProfessorController {
    private AlunoSimulado alunoSimuladoCorrigir;
    private Boolean ativoAlterar;
    private String bairro;
    private String bairroAlterar;
    private String cep;
    private String cepAlterar;
    private String cidade;
    private String cidadeAlterar;
    private String complemento;
    private String complementoAlterar;
    @CPF(message = "CPF inválido!")
    private String cpf;
    private SimuladoHelper simuladoHelper;
    private CroppedImage croppedImage;
    @NotNull(message = "Data de nascimento é obrigatória!")
    @Past(message = "Data de nascimento inválida!")
    private Date dataNascAlterar;
    @NotNull(message = "Data de nascimento é obrigatória!")
    @Past(message = "Data de nascimento inválida!")
    private Date dataNascimento;
    private String ddd;
    private String dddAlterar;
    private String descricaoTelefone;
    private String descricaoTelefoneAlterar;
    private DisciplinaHelper disciplinaHelper;
    private Set<Disciplina> disciplinas;
    private HashSet<Disciplina> disciplinasAlterar;
    private List<Disciplina> disciplinasDisponiveis;
    private Set<String> disciplinasSelecionadas;
    private Set<String> disciplinasSelecionadasAlterar;
    private String email;
    private String emailAlterar;
    private final String emailHTML = "<div style=\"background-color: #f4f4f4; margin:0px;\"><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"border:20px #f4f4f4\" ><tbody><tr><td><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; \"><tbody><tr><td style=\"line-height: 0;\"><center><img src=\"http://cursoidealizar.acessotemporario.net/sgeda/resource/imagens/idealizar.png\" width=\"300\" height=\"80\" border=\"0\" style=\"display: block; margin-bottom: 10px; margin-top: 10px;\"></center></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; padding:30px 40px 0px 40px; background:#ffffff;color:#373737;\" ><tbody><tr style=\"border-bottom:4px solid #f4f4f4; display:block; padding:20px 0;\"><td><p><strong>Bem vindo ao Curso Idealizar,</strong></p><p></p><p>Para acessar o sistema utilize os dados abaixo:</p>----------------------------------------------------------------------<div style=\"margin-left: 20px;\"><p>Matrícula: <strong>{1}</strong></p><p>Senha: <strong>{2}</strong></p></div>----------------------------------------------------------------------<p>Clique <a href=\"http://cursoidealizar.acessotemporario.net/sgeda/\" target=\"_blank\">aqui</a> para login. <p><p></p><p></p><p>Atenciosamente,</p><p>Grupo Idealizar</p></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"segundo-rodape\" style=\"font-family:SansSerif, Myriad Pro;font-size:14px; padding:35px 40px; background:#ffffff; color:#373737; text-align:center; \"><tbody><tr><td><p><center>Esta senha é gerada automaticamente.<br>Para alterar, acesse o sistema em \"Configuração\" >> \"Senha\".</center></p></td></tr></tbody></table></td></tr><tbody></table></div>";
    private EmailSender emailSender;
    private boolean enderecoEncontrado;
    private String especializacaAlterar;
    private String especializacao;
    private String estado;
    private Foto foto;
    private StreamedContent image;
    private String instituicaoFormacao;
    private String instituicaoFormacaoAlterar;
    private LoginHelper loginHelper;
    private String logradouro;
    private String logradouroAlterar;
    private String nome;
    private String nomeAlterar;
    private String numero;
    private String numeroAlterar;
    private String numeroTelefone;
    private Professor professorDetalhe;
    private ProfessorHelper professorHelper;
    private List<Professor> resultadoConsulta;
    private String sexo;
    private String sexoAlterar;
    
    
    private String stringConsulta;
    private String telefoneNumeroAlterar;
    private Set<Telefone> telefones;
    private HashSet<Telefone> telefonesAlterar;
    private String ufAlterar;
    
    public ProfessorController() {
        telefones = new HashSet<>();
        enderecoEncontrado = true;
        disciplinaHelper = new DisciplinaHelper();
        professorHelper = new ProfessorHelper();
        loginHelper = new LoginHelper();
        emailSender = new EmailSender();
        simuladoHelper = new SimuladoHelper();
                
    }

    public AlunoSimulado getAlunoSimuladoCorrigir() {
        return alunoSimuladoCorrigir;
    }

    public void setAlunoSimuladoCorrigir(AlunoSimulado alunoSimuladoCorrigir) {
        this.alunoSimuladoCorrigir = alunoSimuladoCorrigir;
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary,  null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public void salvarNotaSimulado(){
        if(!professorHelper.salvarNotasSimulado(alunoSimuladoCorrigir)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao salvar notas, Tente novamente!");
            return;
        }
        addMessage(null, FacesMessage.SEVERITY_INFO, "Notas salvas com sucesso!");
    }

    public void alterarAtividade() {
        if(professorDetalhe.getPessoa().getAtivo()){
            professorDetalhe.getPessoa().setAtivo(false);
            professorDetalhe.getPessoa().getUsuario().setAtivo(false);
        } else {
            professorDetalhe.getPessoa().setAtivo(true);
            professorDetalhe.getPessoa().getUsuario().setAtivo(true);
        }
        
        if(!professorHelper.alterarAtividade(professorDetalhe)){
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao salvar Professor, Tente novamente!");
            return;
            
        }
        
        if(professorDetalhe.getPessoa().getAtivo()){
            addMessage(null, FacesMessage.SEVERITY_INFO, "Professor reativado com Sucesso!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Professor inativado com Sucesso!");
        }
        
    }

    public String alterarProfessor() {
        return "/restrito/cadastro/detalhe/professor?faces-redirect=true";
     }

    public void buscarCep() {
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

    public void buscarCepAlterar() {
        EnvelopeEndereco endereco = CepWebService.buscaEndereco(cepAlterar);
        if (endereco == null) {
            addMessage("cadastro:cepA", "Cep não encontrado! Verifique e tente novamente ou preencha manualmente!");
            cidadeAlterar = null;
            bairroAlterar = null;
            ufAlterar = null;
            logradouroAlterar = null;
            complementoAlterar = null;
            enderecoEncontrado = false;
            return;
        }
        cidadeAlterar = endereco.getLocalidade();
        bairroAlterar = endereco.getBairro();
        ufAlterar = endereco.getUf();
        logradouroAlterar = endereco.getLogradouro();
        complementoAlterar = endereco.getComplemento();
        enderecoEncontrado = true;

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
        
        String corpoMessagem  = emailHTML.replace("{1}", pessoa.getMatricula()).replace("{2}", senha);
        
        if(emailSender.sendTo(email, "Idealizar", corpoMessagem)){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Email enviado com informações de login!");
        }else{
            addMessage(null, FacesMessage.SEVERITY_ERROR ,"Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }

        limparCampos();
        
    }

    public void carregarAlterar() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        disciplinasSelecionadasAlterar = new HashSet<>();
        disciplinasAlterar = new HashSet<>();
        
        for(Disciplina disciplina:professorDetalhe.getDisciplinas()){
            disciplinasSelecionadasAlterar.add(disciplina.getIdDisciplina().toString());
            disciplinasAlterar.add(disciplina);
        }
        
        nomeAlterar = professorDetalhe.getPessoa().getNome();
        dataNascAlterar = professorDetalhe.getPessoa().getDataNascimento();
        sexoAlterar = professorDetalhe.getPessoa().getSexo();
        emailAlterar = professorDetalhe.getPessoa().getEmail();
        especializacaAlterar = professorDetalhe.getEspecializacao();
        instituicaoFormacaoAlterar = professorDetalhe.getIntituicaoFormacao();
        
        cepAlterar = professorDetalhe.getPessoa().getEnderecos().getCep();
        logradouroAlterar = professorDetalhe.getPessoa().getEnderecos().getLogradouro();
        numeroAlterar = professorDetalhe.getPessoa().getEnderecos().getNumero();
        complementoAlterar = professorDetalhe.getPessoa().getEnderecos().getComplemento();
        ufAlterar = professorDetalhe.getPessoa().getEnderecos().getEstado();
        cidadeAlterar = professorDetalhe.getPessoa().getEnderecos().getCidade();
        bairroAlterar = professorDetalhe.getPessoa().getEnderecos().getBairro();
        telefonesAlterar = new HashSet<>();
        for(Telefone telefone: professorDetalhe.getPessoa().getTelefones()){
            Telefone novoTelefone = new Telefone(professorDetalhe.getPessoa(), telefone.getDdd(), telefone.getNumero(), telefone.getDescricao());
            telefone.setIdTelefone(telefone.getIdTelefone());
            telefonesAlterar.add(novoTelefone);
        }
        dddAlterar = "";
        telefoneNumeroAlterar = "";
        descricaoTelefoneAlterar = "";
        ativoAlterar = professorDetalhe.getPessoa().getAtivo();
    }

    public String consultar() {
        resultadoConsulta = professorHelper.getProfessores(stringConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
        }
         return "";
     }

    public void editarTelefone(Telefone telefone) {
        
    }

    public void excluirTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }

    public void excluirTelefone() {
        
    }

    public void excluirTelefoneAlterar(Telefone telefone) {
        telefonesAlterar.remove(telefone);
    }

    public void exibirDetalhes(Professor professor) {
        professorDetalhe = professor;
     }

    public Boolean getAtivoAlterar() {
        return ativoAlterar;
    }

    public void setAtivoAlterar(Boolean ativoAlterar) {
        this.ativoAlterar = ativoAlterar;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getBairroAlterar() {
        return bairroAlterar;
    }

    public void setBairroAlterar(String bairroAlterar) {
        this.bairroAlterar = bairroAlterar;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCepAlterar() {
        return cepAlterar;
    }

    public void setCepAlterar(String cepAlterar) {
        this.cepAlterar = cepAlterar;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCidadeAlterar() {
        return cidadeAlterar;
    }

    public void setCidadeAlterar(String cidadeAlterar) {
        this.cidadeAlterar = cidadeAlterar;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getComplementoAlterar() {
        return complementoAlterar;
    }

    public void setComplementoAlterar(String complementoAlterar) {
        this.complementoAlterar = complementoAlterar;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public Date getDataNascAlterar() {
        return dataNascAlterar;
    }

    public void setDataNascAlterar(Date dataNascAlterar) {
        this.dataNascAlterar = dataNascAlterar;
    }
    
    public Date getDataNascimento(){
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

    public String getDddAlterar() {
        return dddAlterar;
    }
    
    public void setDddAlterar(String dddAlterar){
        this.dddAlterar = dddAlterar;
    }
    
    public String getDescricaoTelefone(){
        return descricaoTelefone;
    }
    
    public void setDescricaoTelefone(String descricaoTelefone){
        this.descricaoTelefone = descricaoTelefone;
    }
    
    public String getDescricaoTelefoneAlterar(){
        return descricaoTelefoneAlterar;
    }
    
    public void setDescricaoTelefoneAlterar(String descricaoTelefoneAlterar){
        this.descricaoTelefoneAlterar = descricaoTelefoneAlterar;
    }
    
    public Set<Disciplina> getDisciplinas(){
        return disciplinas;
    }

    public void setDisciplinas(Set<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    public List<Disciplina> getDisciplinasDisponiveis() {
        disciplinasDisponiveis = disciplinaHelper.getDisciplinasDisponiveis();
        return disciplinasDisponiveis;
    }
    
    public Set<String> getDisciplinasSelecionadas(){
        return disciplinasSelecionadas;
    }

    public HashSet<Disciplina> getDisciplinasAlterar() {
        return disciplinasAlterar;
    }

    public void setDisciplinasAlterar(HashSet<Disciplina> disciplinasAlterar) {
        this.disciplinasAlterar = disciplinasAlterar;
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
    
    public Set<String> getDisciplinasSelecionadasAlterar(){
        return disciplinasSelecionadasAlterar;
    }

    public void setDisciplinasSelecionadasAlterar(Set<String> disciplinasSelecionadasAlterar) {
        this.disciplinasSelecionadasAlterar = disciplinasSelecionadasAlterar;
        disciplinasAlterar = new HashSet<>();
        for(String id:disciplinasSelecionadasAlterar){
            Integer intId = new Integer(id);
            for(Disciplina disciplina: disciplinasDisponiveis){
                if(intId.equals(disciplina.getIdDisciplina())){
                    disciplinasAlterar.add(disciplina);
                    break;
                }
            }
        }
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    
    
    public String getEmailAlterar(){
        return emailAlterar;
    }
    
    public void setEmailAlterar(String emailAlterar) {
        this.emailAlterar = emailAlterar;
    }
     
    public String getEspecializacaAlterar(){
        return especializacaAlterar;
    }
     

    public void setEspecializacaAlterar(String especializacaAlterar) {
        this.especializacaAlterar = especializacaAlterar;
    }
     
    public String getEspecializacao(){
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
    
    public Foto getFoto() {
        return foto;
    }
    
    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public String getInstituicaoFormacao() {
        return instituicaoFormacao;
    }

    public void setInstituicaoFormacao(String intituicaoFormacao) {
        this.instituicaoFormacao = intituicaoFormacao;
    }

    public String getInstituicaoFormacaoAlterar() {
        return instituicaoFormacaoAlterar;
    }

    public void setInstituicaoFormacaoAlterar(String instituicaoFormacaoAlterar) {
        this.instituicaoFormacaoAlterar = instituicaoFormacaoAlterar;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getLogradouroAlterar() {
        return logradouroAlterar;
    }

    public void setLogradouroAlterar(String logradouroAlterar) {
        this.logradouroAlterar = logradouroAlterar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeAlterar() {
        return nomeAlterar;
    }

    public void setNomeAlterar(String nomeAlterar) {
        this.nomeAlterar = nomeAlterar;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroAlterar() {
        return numeroAlterar;
    }

    public void setNumeroAlterar(String numeroAlterar) {
        this.numeroAlterar = numeroAlterar;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String telefone) {
        this.numeroTelefone = telefone;
    }

    public Professor getProfessorDetalhe() {
        return professorDetalhe;
    }

    public List<Professor> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSexoAlterar() {
        return sexoAlterar;
    }

    public void setSexoAlterar(String sexoAlterar) {
        this.sexoAlterar = sexoAlterar;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }

    public String getTelefoneNumeroAlterar() {
        return telefoneNumeroAlterar;
    }

    public void setTelefoneNumeroAlterar(String telefoneNumeroAlterar) {
        this.telefoneNumeroAlterar = telefoneNumeroAlterar;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public HashSet<Telefone> getTelefonesAlterar() {
        return telefonesAlterar;
    }

    public void setTelefonesAlterar(HashSet<Telefone> telefonesAlterar) {
        this.telefonesAlterar = telefonesAlterar;
    }

    public String getUfAlterar() {
        return ufAlterar;
    }

    public void setUfAlterar(String ufAlterar) {
        this.ufAlterar = ufAlterar;
    }
    
    public String inativar() {
        return "/restrito/cadastro/detalhe/professor?faces-redirect=true";
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
    
    public void incluirTelefone(ActionEvent event){
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
    
    public void incluirTelefoneAlterar() {
        if (dddAlterar == null || dddAlterar.isEmpty()) {
            addMessage("cadastro:dddA", "DDD inválido!");
            return;
        }
        if (telefoneNumeroAlterar == null || telefoneNumeroAlterar.isEmpty()) {
            addMessage("cadastro:telefoneA", "Número do telefone inválido");
            return;
        }

        Telefone telefone = new Telefone();
        telefone.setDdd(dddAlterar);
        telefone.setNumero(telefoneNumeroAlterar);
        telefone.setDescricao(descricaoTelefoneAlterar);

        if (telefonesAlterar == null) {
            telefonesAlterar = new HashSet<>();
        }
        telefonesAlterar.add(telefone);

        dddAlterar = null;
        telefoneNumeroAlterar = null;
        descricaoTelefoneAlterar = null;

    }

     
    public boolean isEnderecoEncontrado(){
        return enderecoEncontrado;
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
        return novoCadastro();
    }
    
    public void limparFormularioConsulta(){
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
    }
    
    public String novaConsulta() {
        limparFormularioConsulta();
        consultar();
        return "/restrito/cadastro/consulta/professor?faces-redirect=true";
    }
    
    public String novoCadastro() {
        limparCampos();
        return "/restrito/administrador/cadastro/professor?faces-redirect=true";
    }
    
    public void reenviarEmail() {
        String email = professorDetalhe.getPessoa().getEmail();
        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if (senha.length() > 6) {
            senha = senha.substring(0, 6);
        }
        String corpoMessagem = emailHTML.replace("{1}", professorDetalhe.getPessoa().getMatricula()).replace("{2}", senha);
        Usuario usuario = loginHelper.getByIdPessoa(professorDetalhe.getIdProfessor());
        if(!loginHelper.alterarSenha(senha, usuario)){
            addMessage(null, FacesMessage.SEVERITY_INFO, "Não foi possivel recuperar senha!");
            return;
        }
        if (emailSender.sendTo(email, "Idealizar", corpoMessagem)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Email enviado com informações de login!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }
    }
    
    private boolean alteracaoDeDados(Professor professor) {
        
        boolean encontrado = false;
        
        if(telefonesAlterar.size()!=professor.getPessoa().getTelefones().size()){
            return true;
        }
        
        for(Telefone tel : telefonesAlterar){
            encontrado = false;
            for(Telefone telC : professor.getPessoa().getTelefones()){
                if(tel.getDdd().equals(telC.getDdd())&&
                        tel.getNumero().equals(telC.getNumero())&&
                        tel.getDescricao().equals(telC.getDescricao())){
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado){
                return true;
            }
        }
        
        if(disciplinasAlterar.size()!=professor.getDisciplinas().size()){
            return true;
        }
        
        for(Disciplina dis : disciplinasAlterar){
            encontrado = false;
            for(Disciplina disC : professor.getDisciplinas()){
                if(dis.getIdDisciplina().equals(disC.getIdDisciplina())){
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado){
                return true;
            }
        }
        
        return !(professor.getPessoa().getNome().equals(nomeAlterar)
                && professor.getPessoa().getSexo().equals(sexoAlterar)
                && professor.getPessoa().getEmail().equals(emailAlterar)
                && professor.getEspecializacao().equals(especializacaAlterar)
                && professor.getIntituicaoFormacao().equals(instituicaoFormacaoAlterar)
                && professor.getPessoa().getEnderecos().getBairro().equals(bairroAlterar)
                && professor.getPessoa().getEnderecos().getCep().equals(cepAlterar)
                && professor.getPessoa().getEnderecos().getCidade().equals(cidadeAlterar)
                && professor.getPessoa().getEnderecos().getComplemento().equals(complementoAlterar)
                && professor.getPessoa().getEnderecos().getEstado().equals(ufAlterar)
                && professor.getPessoa().getEnderecos().getLogradouro().equals(logradouroAlterar)
                && professor.getPessoa().getEnderecos().getNumero().equals(numeroAlterar)
                && professor.getPessoa().getDataNascimento().getDay() == dataNascAlterar.getDay()
                && professor.getPessoa().getDataNascimento().getMonth() == dataNascAlterar.getMonth()
                && professor.getPessoa().getDataNascimento().getYear() == dataNascAlterar.getYear());
                
    }
    
    public void salvarAlteracao() {
        Set<Telefone> telefoneExcluir = new HashSet<>(professorDetalhe.getPessoa().getTelefones());
        Professor professorAlterar = professorDetalhe;

        if (!alteracaoDeDados(professorAlterar)) {
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }

        professorAlterar.getPessoa().setNome(nomeAlterar);
        professorAlterar.getPessoa().setSexo(sexoAlterar);
        professorAlterar.getPessoa().setEmail(emailAlterar);
        professorAlterar.getPessoa().setTelefones(telefonesAlterar);
        professorAlterar.setEspecializacao(especializacaAlterar);
        professorAlterar.setIntituicaoFormacao(instituicaoFormacaoAlterar);
        professorAlterar.setDisciplinas(disciplinasAlterar);
        professorAlterar.getPessoa().getEnderecos().setBairro(bairroAlterar);
        professorAlterar.getPessoa().getEnderecos().setCep(cepAlterar);
        professorAlterar.getPessoa().getEnderecos().setCidade(cidadeAlterar);
        professorAlterar.getPessoa().getEnderecos().setComplemento(complementoAlterar);
        professorAlterar.getPessoa().getEnderecos().setEstado(ufAlterar);
        professorAlterar.getPessoa().getEnderecos().setLogradouro(logradouroAlterar);
        professorAlterar.getPessoa().getEnderecos().setNumero(numeroAlterar);
        professorAlterar.getPessoa().setDataNascimento(dataNascAlterar);
        

        if (!professorHelper.salvarAlteracaoProfessor(professorAlterar, telefoneExcluir)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");

        consultar();
        professorDetalhe = professorAlterar;
        carregarAlterar();
    }
    
    public void corrigirSimulado(Aluno aluno, TurmaSimulado simulado){
        for(AlunoSimulado alunoSimulado: aluno.getAlunoSimulados()){
            if(alunoSimulado.getTurmaSimulado().getId().equals(simulado.getId())){
                alunoSimuladoCorrigir = simuladoHelper.getAlunoSimuladoByIdEager(alunoSimulado.getId());
                break;
            }
        }
    }
 

}
