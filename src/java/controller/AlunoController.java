package controller;

import entidade.Aluno;
import entidade.Curso;
import entidade.Endereco;
import entidade.Foto;
import entidade.Papel;
import entidade.Pessoa;
import entidade.Telefone;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.Usuario;
import helper.AlunoHelper;
import helper.CursoHelper;
import helper.LoginHelper;
import helper.TurmaHelper;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class AlunoController {

    private Aluno alunoDetalhe;
    private final AlunoHelper alunoHelper;
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
    private String cpfResponsavel;
    private String cpfResponsavelAlterar;
    private final CursoHelper cursoHelper;
    private Integer cursoSelecionado;
    private List<Curso> cursosCadastrados;
    private List<String> dataInicioDisponiveis;
    private String dataInicioSelecionada;
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
    private final String emailHTML = "<div style=\"background-color: #f4f4f4; margin:0px;\"><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"border:20px #f4f4f4\" ><tbody><tr><td><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; \"><tbody><tr><td style=\"line-height: 0;\"><center><img src=\"http://cursoidealizar.acessotemporario.net/sgeda/resource/imagens/idealizar.png\" width=\"300\" height=\"80\" border=\"0\" style=\"display: block; margin-bottom: 10px; margin-top: 10px;\"></center></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; padding:30px 40px 0px 40px; background:#ffffff;color:#373737;\" ><tbody><tr style=\"border-bottom:4px solid #f4f4f4; display:block; padding:20px 0;\"><td><p><strong>Bem vindo ao Curso Idealizar,</strong></p><p></p><p>Para acessar o sistema utilize os dados abaixo:</p>----------------------------------------------------------------------<div style=\"margin-left: 20px;\"><p>Curso: <strong>{0}</strong></p><p>Matrícula: <strong>{1}</strong></p><p>Senha: <strong>{2}</strong></p></div>----------------------------------------------------------------------<p>Clique <a href=\"http://cursoidealizar.acessotemporario.net/sgeda/\" target=\"_blank\">aqui</a> para login. <p><p></p><p></p><p>Atenciosamente,</p><p>Grupo Idealizar</p></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"segundo-rodape\" style=\"font-family:SansSerif, Myriad Pro;font-size:14px; padding:35px 40px; background:#ffffff; color:#373737; text-align:center; \"><tbody><tr><td><p><center>Esta senha é gerada automaticamente.<br>Para alterar, acesse o sistema em \"Configuração\" >> \"Senha\".</center></p></td></tr></tbody></table></td></tr><tbody></table></div>";
    private EmailSender emailSender;
    private boolean enderecoEncontrado;
    private String escolaridade;
    private String estado;
    private Foto foto;
    private StreamedContent image;
    private final LoginHelper loginHelper;
    private String logradouro;
    private String logradouroAlterar;
    private String nome;
    private String nomeAlterar;
    private String nomeResponsavel;
    private String nomeResponsavelAlterar;
    private String numero;
    private String numeroAlterar;
    private String numeroTelefone;
    private Pessoa pessoa;
    private List<Aluno> resultadoConsulta;
    private String sexo;
    private String sexoAlterar;
    private String stringConsulta;
    private String telefoneNumeroAlterar;
    private String telefoneResponsavel;
    private String telefoneResponsavelAlterar;
    private Set<Telefone> telefones;
    private HashSet<Telefone> telefonesAlterar;
    private TurmaHelper turmaHelper;
    private List<Turma> turmasDisponiveis;
    private String turnoSelecionado;
    private List<String> turnosDisponiveis;
    private String ufAlterar;
    private String motivoDesistencia;
    private Boolean maiorIdade;

    public Boolean getMaiorIdade() {
        return maiorIdade;
    }

    private int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year   
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3)
                || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                && (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }

    public void updateDataNascimento() {
        int idade = getAge(dataNascimento);
        maiorIdade = idade >= 18;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void aprovadoCheckBoxChange() {
        if (!aprovado) {
            posicao = null;
            nota = null;
            descricaoAprovado = null;
        }
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDescricaoAprovado() {
        return descricaoAprovado;
    }

    public void setDescricaoAprovado(String descricaoAprovado) {
        this.descricaoAprovado = descricaoAprovado;
    }
    private Boolean aprovado;
    private Integer posicao;
    private String nota;
    private String descricaoAprovado;

    public String getMotivoDesistencia() {
        return motivoDesistencia;
    }

    public void setMotivoDesistencia(String motivoDesistencia) {
        this.motivoDesistencia = motivoDesistencia;
    }

    public AlunoController() {
        enderecoEncontrado = true;
        cursoHelper = new CursoHelper();
        alunoHelper = new AlunoHelper();
        loginHelper = new LoginHelper();
        turmaHelper = new TurmaHelper();
        emailSender = new EmailSender();
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

    public void cadastrar() {

        if (telefones == null || telefones.isEmpty()) {
            addMessage("cadastro:telefone", "Cadastre pelo menos um telefone!");
            return;
        }

        if (!maiorIdade) {
            if (telefoneResponsavel == null||telefoneResponsavel.isEmpty()) {
                addMessage("cadastro:telefoneResponsavel", "Telefone do responsavel é obrigatório!");
            }
            if (nomeResponsavel == null||nomeResponsavel.isEmpty()) {
                addMessage("cadastro:responsavel", "Nome do responsavel é obrigatório!");
            }
            if (cpfResponsavel == null||cpfResponsavel.isEmpty()) {
                addMessage("cadastro:cpfResponsavel", "CPF do responsável é obrigatório!");
            }
            return;
        }

        Endereco endereco = new Endereco(null, logradouro, numero, complemento, cep, bairro, cidade, estado);
        Pessoa pessoa = new Pessoa(cpf, "0", nome, dataNascimento, sexo, new Timestamp(new Date().getTime()), email, telefones, endereco);
        endereco.setPessoa(pessoa);
        Aluno aluno = new Aluno(pessoa, nomeResponsavel, escolaridade, cpfResponsavel, telefoneResponsavel, null, null);
        aluno.setDesistente(false);

        pessoa.setFoto(foto);
        pessoa.setAtivo(true);

        for (Telefone telefone : telefones) {
            telefone.setPessoa(pessoa);
        }

        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if (senha.length() > 6) {
            senha = senha.substring(0, 6);
        }

        Papel papelAluno = loginHelper.getPapelAluno();
        Set<Papel> papeis = new HashSet<>();
        papeis.add(papelAluno);

        Usuario usuario = new Usuario(pessoa, "", senha, true, null, papeis);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInicioSelecionada;
        try {
            dataInicioSelecionada = df.parse(this.dataInicioSelecionada);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        List<Turma> turmas = turmaHelper.getTurmasByCurso(cursoSelecionado, dataInicioSelecionada, turnoSelecionado);

        aluno.setTurmaAlunos(new HashSet<TurmaAluno>());

        for (Turma turma : turmas) {
            TurmaAluno turmaAluno = new TurmaAluno(null, aluno, turma, new Timestamp(new Date().getTime()), null);
            aluno.getTurmaAlunos().add(turmaAluno);
        }

        if (!alunoHelper.cadastrar(pessoa, endereco, aluno, usuario)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Aluno, Tente novamente!");
            return;

        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Aluno Cadastrado com Sucesso!");

        String corpoMessagem = emailHTML.replace("{0}", aluno.getCurso().getNome()).replace("{1}", pessoa.getMatricula()).replace("{2}", senha);

        if (emailSender.sendTo(email, "Idealizar", corpoMessagem)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Email enviado com informações de login!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }

        limparCampos();
    }

    public String consultar() {
        resultadoConsulta = alunoHelper.getAlunos(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
        return "";
    }

    public void alterarAprovado() {
        if (aprovado) {
            alunoDetalhe.setAprovado(1);
            alunoDetalhe.setNota(nota);
            alunoDetalhe.setPosicao(posicao);
            alunoDetalhe.setDescricaoAprovado(descricaoAprovado);
        } else {
            alunoDetalhe.setAprovado(0);
        }

        if (!alunoHelper.alterarAprovado(alunoDetalhe)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao salvar Aluno, Tente novamente!");
            return;
        }
        addMessage(null, FacesMessage.SEVERITY_INFO, "Aluno alterado com sucesso!");
    }

    public void excluirTelefone(Telefone telefone) {
        telefones.remove(telefone);
    }

    public void exibirDetalhes(Aluno aluno) {
        alunoDetalhe = alunoHelper.getByIdEager(aluno.getIdAluno());
    }

    public Boolean getAtivoAlterar() {
        return ativoAlterar;
    }

    public void setAtivoAlterar(Boolean ativoAlterar) {
        this.ativoAlterar = ativoAlterar;
    }

    public String getBairroAlterar() {
        return bairroAlterar;
    }

    public void setBairroAlterar(String bairroAlterar) {
        this.bairroAlterar = bairroAlterar;
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

    public String getCpfResponsavelAlterar() {
        return cpfResponsavelAlterar;
    }

    public void setCpfResponsavelAlterar(String cpfResponsavelAlterar) {
        this.cpfResponsavelAlterar = cpfResponsavelAlterar;
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

    public String getNomeResponsavelAlterar() {
        return nomeResponsavelAlterar;
    }

    public void setNomeResponsavelAlterar(String nomeResponsavelAlterar) {
        this.nomeResponsavelAlterar = nomeResponsavelAlterar;
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

    public String getTelefoneResponsavelAlterar() {
        return telefoneResponsavelAlterar;
    }

    public void setTelefoneResponsavelAlterar(String telefoneResponsavelAlterar) {
        this.telefoneResponsavelAlterar = telefoneResponsavelAlterar;
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

    public void alterarAtividade() {
        if (alunoDetalhe.getPessoa().getAtivo()) {
            alunoDetalhe.getPessoa().setAtivo(false);
            alunoDetalhe.getPessoa().getUsuario().setAtivo(false);
            alunoDetalhe.setDesistente(true);
            alunoDetalhe.setMotivoDesistencia(motivoDesistencia);
        } else {
            alunoDetalhe.getPessoa().setAtivo(true);
            alunoDetalhe.getPessoa().getUsuario().setAtivo(true);
            alunoDetalhe.setDesistente(false);
            alunoDetalhe.setMotivoDesistencia(null);
        }

        if (!alunoHelper.alterarAtividade(alunoDetalhe)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao salvar Aluno, Tente novamente!");
            return;

        }

        if (alunoDetalhe.getPessoa().getAtivo()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Aluno reativado com Sucesso!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Aluno inativado com Sucesso!");
        }

    }

    public void excluirTelefoneAlterar(Telefone telefone) {
        telefonesAlterar.remove(telefone);
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

    private boolean alteracaoDeDados(Aluno aluno) {

        boolean encontrado = false;

        if (telefonesAlterar.size() != aluno.getPessoa().getTelefones().size()) {
            return true;
        }

        for (Telefone tel : telefonesAlterar) {
            encontrado = false;
            for (Telefone telC : aluno.getPessoa().getTelefones()) {
                if (tel.getDdd().equals(telC.getDdd())
                        && tel.getNumero().equals(telC.getNumero())
                        && tel.getDescricao().equals(telC.getDescricao())) {
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                return true;
            }
        }

        return !(aluno.getPessoa().getNome().equals(nomeAlterar)
                && aluno.getPessoa().getSexo().equals(sexoAlterar)
                && aluno.getPessoa().getEmail().equals(emailAlterar)
                && aluno.getNomeResponsavel().equals(nomeResponsavelAlterar)
                && aluno.getCpfResponsavel().equals(cpfResponsavelAlterar)
                && aluno.getTelefoneResponsavel().equals(telefoneResponsavelAlterar)
                && aluno.getPessoa().getEnderecos().getBairro().equals(bairroAlterar)
                && aluno.getPessoa().getEnderecos().getCep().equals(cepAlterar)
                && aluno.getPessoa().getEnderecos().getCidade().equals(cidadeAlterar)
                && aluno.getPessoa().getEnderecos().getComplemento().equals(complementoAlterar)
                && aluno.getPessoa().getEnderecos().getEstado().equals(ufAlterar)
                && aluno.getPessoa().getEnderecos().getLogradouro().equals(logradouroAlterar)
                && aluno.getPessoa().getEnderecos().getNumero().equals(numeroAlterar)
                && aluno.getPessoa().getDataNascimento().getDay() == dataNascAlterar.getDay()
                && aluno.getPessoa().getDataNascimento().getMonth() == dataNascAlterar.getMonth()
                && aluno.getPessoa().getDataNascimento().getYear() == dataNascAlterar.getYear());

    }

    public void salvarAlteracao() {
        Set<Telefone> telefoneExcluir = new HashSet<>(alunoDetalhe.getPessoa().getTelefones());
        Aluno alunoAlterar = alunoDetalhe;

        if (!alteracaoDeDados(alunoAlterar)) {
            addMessage(null, FacesMessage.SEVERITY_WARN, "Não houve alteração nos dados!");
            return;
        }

        alunoAlterar.getPessoa().setNome(nomeAlterar);
        alunoAlterar.getPessoa().setSexo(sexoAlterar);
        alunoAlterar.getPessoa().setEmail(emailAlterar);
        alunoAlterar.setNomeResponsavel(nomeResponsavelAlterar);
        alunoAlterar.setCpfResponsavel(cpfResponsavelAlterar);
        alunoAlterar.setTelefoneResponsavel(telefoneResponsavelAlterar);
        alunoAlterar.getPessoa().setTelefones(telefonesAlterar);
        alunoAlterar.getPessoa().getEnderecos().setBairro(bairroAlterar);
        alunoAlterar.getPessoa().getEnderecos().setCep(cepAlterar);
        alunoAlterar.getPessoa().getEnderecos().setCidade(cidadeAlterar);
        alunoAlterar.getPessoa().getEnderecos().setComplemento(complementoAlterar);
        alunoAlterar.getPessoa().getEnderecos().setEstado(ufAlterar);
        alunoAlterar.getPessoa().getEnderecos().setLogradouro(logradouroAlterar);
        alunoAlterar.getPessoa().getEnderecos().setNumero(numeroAlterar);
        alunoAlterar.getPessoa().setDataNascimento(dataNascAlterar);

        if (!alunoHelper.salvarAlteracaoAluno(alunoAlterar, telefoneExcluir)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Não foi possível salvar alterações!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Alterações salvas com sucesso!");

        consultar();
        alunoDetalhe = alunoAlterar;
        carregarAlterar();
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

    public void carregarAlterar() {
        nomeAlterar = alunoDetalhe.getPessoa().getNome();
        dataNascAlterar = alunoDetalhe.getPessoa().getDataNascimento();
        sexoAlterar = alunoDetalhe.getPessoa().getSexo();
        emailAlterar = alunoDetalhe.getPessoa().getEmail();
        nomeResponsavelAlterar = alunoDetalhe.getNomeResponsavel();
        cpfResponsavelAlterar = alunoDetalhe.getCpfResponsavel();
        telefoneResponsavelAlterar = alunoDetalhe.getTelefoneResponsavel();
        cepAlterar = alunoDetalhe.getPessoa().getEnderecos().getCep();
        logradouroAlterar = alunoDetalhe.getPessoa().getEnderecos().getLogradouro();
        numeroAlterar = alunoDetalhe.getPessoa().getEnderecos().getNumero();
        complementoAlterar = alunoDetalhe.getPessoa().getEnderecos().getComplemento();
        ufAlterar = alunoDetalhe.getPessoa().getEnderecos().getEstado();
        cidadeAlterar = alunoDetalhe.getPessoa().getEnderecos().getCidade();
        bairroAlterar = alunoDetalhe.getPessoa().getEnderecos().getBairro();
        telefonesAlterar = new HashSet<>();
        for (Telefone telefone : alunoDetalhe.getPessoa().getTelefones()) {
            Telefone novoTelefone = new Telefone(alunoDetalhe.getPessoa(), telefone.getDdd(), telefone.getNumero(), telefone.getDescricao());
            telefone.setIdTelefone(telefone.getIdTelefone());
            telefonesAlterar.add(novoTelefone);
        }
        dddAlterar = "";
        telefoneNumeroAlterar = "";
        descricaoTelefoneAlterar = "";
        ativoAlterar = alunoDetalhe.getPessoa().getAtivo();
    }

    public void carregarAlterarAprovado() {
        if (alunoDetalhe.getAprovado().equals(1)) {
            aprovado = true;
        } else {
            aprovado = false;
        }

        nota = alunoDetalhe.getNota();
        posicao = alunoDetalhe.getPosicao();
        descricaoAprovado = alunoDetalhe.getDescricaoAprovado();
    }

    public Aluno getAlunoDetalhe() {
        return alunoDetalhe;
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

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public Integer getCursoSelecionado() {
        return cursoSelecionado;
    }

    public void setCursoSelecionado(Integer cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    public List<Curso> getCursosCadastrados() {
        return cursosCadastrados;
    }

    public List<String> getDataInicioDisponiveis() {
        return dataInicioDisponiveis;
    }

    public String getDataInicioSelecionada() {
        return dataInicioSelecionada;
    }

    public void setDataInicioSelecionada(String dataInicioSelecionada) {
        this.dataInicioSelecionada = dataInicioSelecionada;
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

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
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

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Aluno> getResultadoConsulta() {
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

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public String getTurnoSelecionado() {
        return turnoSelecionado;
    }

    public void setTurnoSelecionado(String turnoSelecionado) {
        this.turnoSelecionado = turnoSelecionado;
    }

    public List<String> getTurnosDisponiveis() {
        return turnosDisponiveis;
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

    public boolean isEnderecoEncontrado() {
        return enderecoEncontrado;
    }

    public void limparCampos() {
        bairro = null;
        cep = null;
        cidade = null;
        complemento = null;
        cpf = null;
        cpfResponsavel = null;
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
        foto = null;
        cursoSelecionado = null;
        turnoSelecionado = null;
        dataInicioSelecionada = null;
        turnosDisponiveis = new ArrayList<>();
        dataInicioDisponiveis = new ArrayList<>();
    }

    public String limparFormularioCadastro() {
        return novoCadastro();
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        consultar();
    }

    public String novaConsulta() {
        limparCampos();
        consultar();
        return "/restrito/cadastro/consulta/aluno?faces-redirect=true";
    }

    public String novoCadastro() {
        cursosCadastrados = cursoHelper.getCursosDisponiveis();
        limparCampos();
        maiorIdade = true;
        return "/restrito/cadastro/aluno?faces-redirect=true";
    }

    public void onChangeCurso() {
        if (cursoSelecionado != null && !cursoSelecionado.equals(0)) {
            turmasDisponiveis = turmaHelper.getTurmasByCurso(cursoSelecionado);
            turnosDisponiveis = new ArrayList<>();
            for (Turma turma : turmasDisponiveis) {
                boolean encontrado = false;
                for (String str : turnosDisponiveis) {
                    if (str.equalsIgnoreCase(turma.getTurno())) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    turnosDisponiveis.add(turma.getTurno());
                }
            }
            dataInicioDisponiveis = new ArrayList<>();
        } else {
            turmasDisponiveis = new ArrayList<>();
        }
    }

    public void onChangeTurno() {
        SimpleDateFormat format;
        format = new SimpleDateFormat("dd/MM/yyyy");
        if (turnoSelecionado != null && !turnoSelecionado.equals("0")) {
            dataInicioDisponiveis = new ArrayList<>();
            for (Turma turma : turmasDisponiveis) {
                boolean encontrado = false;
                for (String date : dataInicioDisponiveis) {
                    if (date.equalsIgnoreCase(format.format(turma.getDataInicio()))) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    if (turma.getTurno().equalsIgnoreCase(turnoSelecionado)) {
                        dataInicioDisponiveis.add(format.format(turma.getDataInicio()));
                    }
                }
            }
        } else {
            dataInicioDisponiveis = new ArrayList<>();
        }
    }

    public void reenviarEmail() {
        String email = alunoDetalhe.getPessoa().getEmail();
        String senha = Long.toHexString(Double.doubleToLongBits(Math.random()));
        if (senha.length() > 6) {
            senha = senha.substring(0, 6);
        }
        String corpoMessagem = emailHTML.replace("{0}", alunoDetalhe.getCurso().getNome()).replace("{1}", alunoDetalhe.getPessoa().getMatricula()).replace("{2}", senha);
        Usuario usuario = loginHelper.getByIdPessoa(alunoDetalhe.getIdAluno());
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

    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String contentType = uploadedFile.getContentType();
        foto = new Foto();
        foto.setImagem(uploadedFile.getContents());
        image = new DefaultStreamedContent(new ByteArrayInputStream(foto.getImagem()), contentType);
    }

}
