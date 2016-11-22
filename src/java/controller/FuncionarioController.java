package controller;

import entidade.Endereco;
import entidade.Foto;
import entidade.Funcionario;
import entidade.Papel;
import entidade.Pessoa;
import entidade.Telefone;
import entidade.Usuario;
import helper.FuncionarioHelper;
import helper.LoginHelper;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.EmailSender;
import util.EnvelopeEndereco;
import webservice.CepWebService;

@ManagedBean
@SessionScoped
public class FuncionarioController {

    private boolean admAlterar;
    private Boolean ativoAlterar;

    private String bairro;
    private String bairroAlterar;
    private String cargo;
    private String cargoAlterar;
    private String cep;
    private String cepAlterar;
    private String cidade;
    private String cidadeAlterar;
    private String complemento;
    private String complementoAlterar;
    @CPF(message = "CPF inválido!")
    private String cpf;
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
    private String email;
    private String emailAlterar;
    private final String emailHTML = "<div style=\"background-color: #f4f4f4; margin:0px;\"><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"border:20px #f4f4f4\" ><tbody><tr><td><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; \"><tbody><tr><td style=\"line-height: 0;\"><center><img src=\"http://cursoidealizar.acessotemporario.net/sgeda/resource/imagens/idealizar.png\" width=\"300\" height=\"80\" border=\"0\" style=\"display: block; margin-bottom: 10px; margin-top: 10px;\"></center></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; padding:30px 40px 0px 40px; background:#ffffff;color:#373737;\" ><tbody><tr style=\"border-bottom:4px solid #f4f4f4; display:block; padding:20px 0;\"><td><p><strong>Bem vindo ao Curso Idealizar,</strong></p><p></p><p>Para acessar o sistema utilize os dados abaixo:</p>----------------------------------------------------------------------<div style=\"margin-left: 20px;\"><p>Matrícula: <strong>{1}</strong></p><p>Senha: <strong>{2}</strong></p></div>----------------------------------------------------------------------<p>Clique <a href=\"http://cursoidealizar.acessotemporario.net/sgeda/\" target=\"_blank\">aqui</a> para login. <p><p></p><p></p><p>Atenciosamente,</p><p>Grupo Idealizar</p></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"segundo-rodape\" style=\"font-family:SansSerif, Myriad Pro;font-size:14px; padding:35px 40px; background:#ffffff; color:#373737; text-align:center; \"><tbody><tr><td><p><center>Esta senha é gerada automaticamente.<br>Para alterar, acesse o sistema em \"Configuração\" >> \"Senha\".</center></p></td></tr></tbody></table></td></tr><tbody></table></div>";
    private final EmailSender emailSender;
    private boolean enderecoEncontrado;
    private String estado;
    private Foto foto;
    private Funcionario funcionarioDetalhe;
    private final FuncionarioHelper funcionarioHelper;
    private StreamedContent image;
    private final LoginHelper loginHelper;
    private String logradouro;
    private String logradouroAlterar;
    private String nome;
    private String nomeAlterar;
    private String numero;
    private String numeroAlterar;
    private String numeroTelefone;
    private boolean papelAdmistrador;
    private List<Funcionario> resultadoConsulta;
    private String sexo;
    private String sexoAlterar;
    private String stringConsulta;
    private String telefoneNumeroAlterar;

    private Set<Telefone> telefones;
    private Set<Telefone> telefonesAlterar;
    private String ufAlterar;

    public FuncionarioController() {
        telefones = new HashSet<>();
        enderecoEncontrado = true;
        funcionarioHelper = new FuncionarioHelper();
        loginHelper = new LoginHelper();
        emailSender = new EmailSender();
    }
    
    public void alterarAtividade(){
        if(funcionarioDetalhe.getPessoa().getAtivo()){
            funcionarioDetalhe.getPessoa().setAtivo(false);
            funcionarioDetalhe.getPessoa().getUsuario().setAtivo(false);
        } else {
            funcionarioDetalhe.getPessoa().setAtivo(true);
            funcionarioDetalhe.getPessoa().getUsuario().setAtivo(true);
        }
        
        if(!funcionarioHelper.alterarAtividade(funcionarioDetalhe)){
             addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao salvar Funcionário, Tente novamente!");
            return;

        }
        
        if(funcionarioDetalhe.getPessoa().getAtivo()){
            addMessage(null, FacesMessage.SEVERITY_INFO, "Funcionário reativado com Sucesso!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Funcionário inativado com Sucesso!");
        }

    }

    private boolean alteracaoDeDados(Funcionario funcionarioAlterar) {
        
        boolean encontrado = false;
        
        if(telefonesAlterar.size()!=funcionarioAlterar.getPessoa().getTelefones().size()){
            return true;
        }
        
        for(Telefone tel : telefonesAlterar){
            encontrado = false;
            for(Telefone telC : funcionarioAlterar.getPessoa().getTelefones()){
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
        
        return !(funcionarioAlterar.getPessoa().getNome().equals(nomeAlterar)
                && funcionarioAlterar.getPessoa().getSexo().equals(sexoAlterar)
                && funcionarioAlterar.getPessoa().getEmail().equals(emailAlterar)
                && funcionarioAlterar.getCargo().equals(cargoAlterar)
                && !(funcionarioAlterar.getPessoa().getUsuario().isAdministrador() ^ admAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getBairro().equals(bairroAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getCep().equals(cepAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getCidade().equals(cidadeAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getComplemento().equals(complementoAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getEstado().equals(ufAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getLogradouro().equals(logradouroAlterar)
                && funcionarioAlterar.getPessoa().getEnderecos().getNumero().equals(numeroAlterar)
                && funcionarioAlterar.getPessoa().getDataNascimento().getDay() == dataNascAlterar.getDay()
                && funcionarioAlterar.getPessoa().getDataNascimento().getMonth() == dataNascAlterar.getMonth()
                && funcionarioAlterar.getPessoa().getDataNascimento().getYear() == dataNascAlterar.getYear());
                
    }

    public void alterarFuncionario() {

    }

    public void inativar() {

    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void buscarCep() {
        EnvelopeEndereco endereco = CepWebService.buscaEndereco(cep);
        if (endereco == null) {
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
        if (telefones == null || telefones.isEmpty()) {
            addMessage("cadastro:telefone", "Cadastre pelo menos um telefone!");
            return;
        }

        if (funcionarioHelper.getByCpf(cpf) != null) {
            addMessage("cadastro:cpf", "CPF já cadastrado!");
            return;
        }

        Endereco endereco = new Endereco(null, logradouro, numero, complemento, cep, bairro, cidade, estado);
        Pessoa pessoa = new Pessoa(cpf, "0", nome, dataNascimento, sexo, new Timestamp(new Date().getTime()), email, telefones, endereco);
        endereco.setPessoa(pessoa);
        Funcionario funcionario = new Funcionario(pessoa, cargo);

        pessoa.setFoto(foto);

        for (Telefone telefone : telefones) {
            telefone.setPessoa(pessoa);
        }

        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if (senha.length() > 6) {
            senha = senha.substring(0, 6);
        }

        Papel papelAtendente = loginHelper.getPapelAtendente();
        Set<Papel> papeis = new HashSet<>();
        papeis.add(papelAtendente);

        if (papelAdmistrador) {
            Papel papelAdm = loginHelper.getPapelAdminstrador();
            papeis.add(papelAdm);
        }

        Usuario usuario = new Usuario(pessoa, "", senha, true, null, papeis);

        pessoa.setAtivo(true);

        if (!funcionarioHelper.cadastrar(pessoa, endereco, funcionario, usuario)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Funcionario, Tente novamente!");
            return;

        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Funcionario Cadastrado com Sucesso!");

        String corpoMessagem = emailHTML.replace("{1}", pessoa.getMatricula()).replace("{2}", senha);

        if (emailSender.sendTo(email, "Idealizar", corpoMessagem)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Email enviado com informações de login!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }

        limparCampos();
    }

    public void salvarAlteracao() {
        Set<Telefone> telefoneExcluir = new HashSet<>(funcionarioDetalhe.getPessoa().getTelefones());
        Funcionario funcionarioAlterar = funcionarioDetalhe;

        if (!alteracaoDeDados(funcionarioAlterar)) {
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }

        funcionarioAlterar.getPessoa().setNome(nomeAlterar);
        funcionarioAlterar.getPessoa().setSexo(sexoAlterar);
        funcionarioAlterar.getPessoa().setEmail(emailAlterar);
        funcionarioAlterar.setCargo(cargoAlterar);
        funcionarioAlterar.getPessoa().setTelefones(telefonesAlterar);
        
        
        if(!admAlterar){
            boolean encontrado = false;
            Papel papelAlterado = null;
            for(Papel papel : funcionarioAlterar.getPessoa().getUsuario().getPapels()){
                if(papel.getIdPapel()==1){
                    encontrado = true;
                    papelAlterado = papel;
                }
            }
            
            if(encontrado){
                funcionarioAlterar.getPessoa().getUsuario().getPapels().remove(papelAlterado);
            }
        }
        
        if(admAlterar){
            boolean encontrado = false;
            Papel papelAlterado = null;
            for(Papel papel : funcionarioAlterar.getPessoa().getUsuario().getPapels()){
                if(papel.getIdPapel()==1){
                    encontrado = true;
                    papelAlterado = papel;
                }
            }
            
            if(!encontrado){
                Papel papelAdm = loginHelper.getPapelAdminstrador();
                funcionarioAlterar.getPessoa().getUsuario().getPapels().add(papelAdm);
            }
        }
        
        funcionarioAlterar.getPessoa().getEnderecos().setBairro(bairroAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setCep(cepAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setCidade(cidadeAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setComplemento(complementoAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setEstado(ufAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setLogradouro(logradouroAlterar);
        funcionarioAlterar.getPessoa().getEnderecos().setNumero(numeroAlterar);
        funcionarioAlterar.getPessoa().setDataNascimento(dataNascAlterar);
        

        if (!funcionarioHelper.salvarAlteracaoFuncionario(funcionarioAlterar, telefoneExcluir)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");

        consultar();
        funcionarioDetalhe = funcionarioAlterar;
        carregarAlterar();
    }

    public String consultar() {
        resultadoConsulta = funcionarioHelper.getFuncionarios(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
        return "";
    }

    public void excluirTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }

    public void excluirTelefoneAlterar(Telefone telefone) {
        telefonesAlterar.remove(telefone);
    }

    public void exibirDetalhes(Funcionario funcionario) {
        funcionarioDetalhe = funcionarioHelper.getFuncionarioEager(funcionario.getIdFuncionario());
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    public Funcionario getFuncionarioDetalhe() {
        return funcionarioDetalhe;
    }

    public void setFuncionarioDetalhe(Funcionario funcionarioDetalhe) {
        this.funcionarioDetalhe = funcionarioDetalhe;
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

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String telefone) {
        this.numeroTelefone = telefone;
    }

    public List<Funcionario> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public void incluirTelefone() {
        if (ddd == null || ddd.isEmpty()) {
            addMessage("cadastro:ddd", "DDD inválido!");
            return;
        }
        if (numeroTelefone == null || numeroTelefone.isEmpty()) {
            addMessage("cadastro:telefone", "Número do telefone inválido");
            return;
        }

        Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(numeroTelefone);
        telefone.setDescricao(descricaoTelefone);

        if (telefones == null) {
            telefones = new HashSet<>();
        }
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

    public boolean isEnderecoEncontrado() {
        return enderecoEncontrado;
    }

    public boolean isPapelAdmistrador() {
        return papelAdmistrador;
    }

    public void setPapelAdmistrador(boolean papelAdmistrador) {
        this.papelAdmistrador = papelAdmistrador;
    }

    public void limparCampos() {
        bairro = null;
        cep = null;
        cidade = null;
        complemento = null;
        cpf = null;
        dataNascimento = null;
        ddd = null;
        descricaoTelefone = null;
        email = null;
        estado = null;
        logradouro = null;
        nome = null;
        numero = null;
        sexo = null;
        numeroTelefone = null;
        telefones = new HashSet<>();
        foto = null;
        cargo = null;
        papelAdmistrador = false;

    }

    public String limparFormularioCadastro() {
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
    }

    public String novaConsulta() {
        limparCampos();
        consultar();
        return "/restrito/cadastro/consulta/funcionario?faces-redirect=true";
    }

    public String novoCadastro() {
        limparCampos();
        return "/restrito/administrador/cadastro/funcionario?faces-redirect=true";
    }

    public void carregarAlterar() {
        nomeAlterar = funcionarioDetalhe.getPessoa().getNome();
        dataNascAlterar = funcionarioDetalhe.getPessoa().getDataNascimento();
        sexoAlterar = funcionarioDetalhe.getPessoa().getSexo();
        emailAlterar = funcionarioDetalhe.getPessoa().getEmail();
        cargoAlterar = funcionarioDetalhe.getCargo();
        admAlterar = funcionarioDetalhe.getPessoa().getUsuario().isAdministrador();
        cepAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getCep();
        logradouroAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getLogradouro();
        numeroAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getNumero();
        complementoAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getComplemento();
        ufAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getEstado();
        cidadeAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getCidade();
        bairroAlterar = funcionarioDetalhe.getPessoa().getEnderecos().getBairro();
        telefonesAlterar = new HashSet<>();
        for(Telefone telefone: funcionarioDetalhe.getPessoa().getTelefones()){
            Telefone novoTelefone = new Telefone(funcionarioDetalhe.getPessoa(), telefone.getDdd(), telefone.getNumero(), telefone.getDescricao());
            telefone.setIdTelefone(telefone.getIdTelefone());
            telefonesAlterar.add(novoTelefone);
        }
        dddAlterar = "";
        telefoneNumeroAlterar = "";
        descricaoTelefoneAlterar = "";
        ativoAlterar = funcionarioDetalhe.getPessoa().getAtivo();
    }

    public Boolean getAtivoAlterar() {
        return ativoAlterar;
    }

    public void setAtivoAlterar(Boolean ativoAlterar) {
        this.ativoAlterar = ativoAlterar;
    }

    public boolean isAdmAlterar() {
        return admAlterar;
    }

    public void setAdmAlterar(boolean admAlterar) {
        this.admAlterar = admAlterar;
    }

    public String getBairroAlterar() {
        return bairroAlterar;
    }

    public void setBairroAlterar(String bairroAlterar) {
        this.bairroAlterar = bairroAlterar;
    }

    public String getCargoAlterar() {
        return cargoAlterar;
    }

    public void setCargoAlterar(String cargoAlterar) {
        this.cargoAlterar = cargoAlterar;
    }

    public String getCepAlterar() {
        return cepAlterar;
    }

    public void setCepAlterar(String cepAlterar) {
        this.cepAlterar = cepAlterar;
    }

    public String getCidadeAlterar() {
        return cidadeAlterar;
    }

    public void setCidadeAlterar(String cidadeAlterar) {
        this.cidadeAlterar = cidadeAlterar;
    }

    public String getComplementoAlterar() {
        return complementoAlterar;
    }

    public void setComplementoAlterar(String complementoAlterar) {
        this.complementoAlterar = complementoAlterar;
    }

    public Date getDataNascAlterar() {
        return dataNascAlterar;
    }

    public void setDataNascAlterar(Date dataNascAlterar) {
        this.dataNascAlterar = dataNascAlterar;
    }

    public String getDddAlterar() {
        return dddAlterar;
    }

    public void setDddAlterar(String dddAlterar) {
        this.dddAlterar = dddAlterar;
    }

    public String getDescricaoTelefoneAlterar() {
        return descricaoTelefoneAlterar;
    }

    public void setDescricaoTelefoneAlterar(String descricaoTelefoneAlterar) {
        this.descricaoTelefoneAlterar = descricaoTelefoneAlterar;
    }

    public String getEmailAlterar() {
        return emailAlterar;
    }

    public void setEmailAlterar(String emailAlterar) {
        this.emailAlterar = emailAlterar;
    }

    public String getLogradouroAlterar() {
        return logradouroAlterar;
    }

    public void setLogradouroAlterar(String logradouroAlterar) {
        this.logradouroAlterar = logradouroAlterar;
    }

    public String getNomeAlterar() {
        return nomeAlterar;
    }

    public void setNomeAlterar(String nomeAlterar) {
        this.nomeAlterar = nomeAlterar;
    }

    public String getNumeroAlterar() {
        return numeroAlterar;
    }

    public void setNumeroAlterar(String numeroAlterar) {
        this.numeroAlterar = numeroAlterar;
    }

    public String getSexoAlterar() {
        return sexoAlterar;
    }

    public void setSexoAlterar(String sexoAlterar) {
        this.sexoAlterar = sexoAlterar;
    }

    public String getTelefoneNumeroAlterar() {
        return telefoneNumeroAlterar;
    }

    public void setTelefoneNumeroAlterar(String telefoneNumeroAlterar) {
        this.telefoneNumeroAlterar = telefoneNumeroAlterar;
    }

    public Set<Telefone> getTelefonesAlterar() {
        return telefonesAlterar;
    }

    public void setTelefonesAlterar(Set<Telefone> telefonesAlterar) {
        this.telefonesAlterar = telefonesAlterar;
    }

    public String getUfAlterar() {
        return ufAlterar;
    }

    public void setUfAlterar(String ufAlterar) {
        this.ufAlterar = ufAlterar;
    }

    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String contentType = uploadedFile.getContentType();
        foto = new Foto();
        foto.setImagem(uploadedFile.getContents());
        image = new DefaultStreamedContent(new ByteArrayInputStream(foto.getImagem()), contentType);
    }

    public void reenviarEmail() {
        String email = funcionarioDetalhe.getPessoa().getEmail();
        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if (senha.length() > 6) {
            senha = senha.substring(0, 6);
        }
        String corpoMessagem = emailHTML.replace("{1}", funcionarioDetalhe.getPessoa().getMatricula()).replace("{2}", senha);
        Usuario usuario = loginHelper.getByIdPessoa(funcionarioDetalhe.getIdFuncionario());
        if (!loginHelper.alterarSenha(senha, usuario)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Não foi possivel recuperar senha!");
            return;
        }
        if (emailSender.sendTo(email, "Idealizar", corpoMessagem)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Email enviado com informações de login!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }
    }

}
