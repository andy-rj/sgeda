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
    private final AlunoHelper alunoHelper;

    private String bairro;
    private String cep;
    private String cidade;
    private String complemento;
    @CPF(message = "CPF inválido!")
    private String cpf;
    private String cpfResponsavel;
    private final CursoHelper cursoHelper;
    private Integer cursoSelecionado;
    private List<Curso> cursosCadastrados;
    private List<String> dataInicioDisponiveis;
    private String dataInicioSelecionada;
    @NotNull(message = "Data de nascimento é obrigatória!")
    @Past(message = "Data de nascimento inválida!")
    private Date dataNascimento;
    private String ddd;
    private String descricaoTelefone;
    private String email;
    private final String emailHTML = "<div style=\"background-color: #f4f4f4; margin:0px;\"><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"border:20px #f4f4f4\" ><tbody><tr><td><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; \"><tbody><tr><td style=\"line-height: 0;\"><center><img src=\"http://cursoidealizar.acessotemporario.net/sgeda/resource/imagens/idealizar.png\" width=\"300\" height=\"80\" border=\"0\" style=\"display: block; margin-bottom: 10px; margin-top: 10px;\"></center></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family:Arial, SansSerif, Myriad Pro;font-size:14px; padding:30px 40px 0px 40px; background:#ffffff;color:#373737;\" ><tbody><tr style=\"border-bottom:4px solid #f4f4f4; display:block; padding:20px 0;\"><td><p><strong>Bem vindo ao Curso Idealizar,</strong></p><p></p><p>Para acessar o sistema utilize os dados abaixo:</p>----------------------------------------------------------------------<div style=\"margin-left: 20px;\"><p>Matrícula: <strong>{1}</strong></p><p>Senha: <strong>{2}</strong></p></div>----------------------------------------------------------------------<p>Clique <a href=\"http://cursoidealizar.acessotemporario.net/sgeda/\" target=\"_blank\">aqui</a> para login. <p><p></p><p></p><p>Atenciosamente,</p><p>Grupo Idealizar</p></td></tr></tbody></table><table width=\"642\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"segundo-rodape\" style=\"font-family:SansSerif, Myriad Pro;font-size:14px; padding:35px 40px; background:#ffffff; color:#373737; text-align:center; \"><tbody><tr><td><p><center>Esta senha é gerada automaticamente.<br>Para alterar, acesse o sistema em \"Configuração\" >> \"Senha\".</center></p></td></tr></tbody></table></td></tr><tbody></table></div>";
    private EmailSender emailSender;
    private boolean enderecoEncontrado;
    private String escolaridade;
    private String estado;
    private Foto foto;
    private StreamedContent image;
    private final LoginHelper loginHelper;
    private String logradouro;
    private String nome;
    private String nomeResponsavel;
    private String numero;
    private String numeroTelefone;
    private Pessoa pessoa;
    private String sexo;
    private String telefoneResponsavel;
    private Set<Telefone> telefones;
    private TurmaHelper turmaHelper;
    private List<Turma> turmasDisponiveis;
    private String turnoSelecionado;
    private List<String> turnosDisponiveis;
    
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
        
        boolean erro=false;
        
        if (telefones == null || telefones.isEmpty()) {
            addMessage("cadastro:telefone", "Cadastre pelo menos um telefone!");
            erro = true;
        }

        if (alunoHelper.getByCpf(cpf) != null) {
            addMessage("cadastro:cpf", "CPF já cadastrado!");
            erro = true;
        }
        
        if(nomeResponsavel != null || !nomeResponsavel.isEmpty()){
            if(telefoneResponsavel == null || telefoneResponsavel.isEmpty()){
                addMessage("cadastro:telefoneResponsavel", "Telefone do responsável não preenchido!");
                erro = true;
            }
            if(cpfResponsavel == null || cpfResponsavel.isEmpty()){
                addMessage("cadastro:cpfResponsavel", "CPF do responsável não preenchido!");
                erro = true;
            }
        } 
        else if(cpfResponsavel != null || !cpfResponsavel.isEmpty()){
            if(telefoneResponsavel == null || telefoneResponsavel.isEmpty()){
                addMessage("cadastro:telefoneResponsavel", "Telefone do responsável não preenchido!");
                erro = true;
            }
            if(nomeResponsavel == null || nomeResponsavel.isEmpty()){
                addMessage("cadastro:responsavel", "Nome do responsável não preenchido!");
                erro = true;
            }
        }else if(telefoneResponsavel != null || !telefoneResponsavel.isEmpty()){
            if(nomeResponsavel == null || nomeResponsavel.isEmpty()){
                addMessage("cadastro:responsavel", "Nome do responsável não preenchido!");
                erro = true;
            }
            if(cpfResponsavel == null || cpfResponsavel.isEmpty()){
                addMessage("cadastro:cpfResponsavel", "CPF do responsável não preenchido!");
                erro = true;
            }
        }
        
        if(erro) return;
        
        Endereco endereco = new Endereco(null, logradouro, numero, complemento, cep, bairro, cidade, estado);
        Pessoa pessoa = new Pessoa(cpf, "0", nome, dataNascimento, sexo, new Timestamp(new Date().getTime()), email, telefones, endereco);
        endereco.setPessoa(pessoa);
        Aluno aluno = new Aluno(pessoa, nomeResponsavel, escolaridade, cpfResponsavel, telefoneResponsavel, null, null);

        pessoa.setFoto(foto);

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
        
        for(Turma turma: turmas){
            TurmaAluno turmaAluno = new TurmaAluno(null, aluno, turma, new Timestamp(new Date().getTime()), null);
            aluno.getTurmaAlunos().add(turmaAluno);
        }
        
        if (!alunoHelper.cadastrar(pessoa, endereco, aluno, usuario)) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar Aluno, Tente novamente!");
            return;

        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Aluno Cadastrado com Sucesso!");

        String corpoMessagem = emailHTML.replace("{1}", pessoa.getMatricula()).replace("{2}", senha);

        if (emailSender.sendTo(email, "Idealizar", corpoMessagem)) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Email enviado com informações de login!");
        } else {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Email com informações de login não pode ser enviado! Verifique sua conexão e tente reeviar através da página de detalhes");
        }

        limparCampos();
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
    
    public void setComplemento(String complemento){
        this.complemento = complemento;
    }
    
    public String getCpf(){
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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
        limparCampos();
        return "";
    }

    public String novaConsulta() {
        limparCampos();
        return "/restrito/cadastro/consulta/aluno?faces-redirect=true";
    }

    public String novoCadastro() {
        cursosCadastrados = cursoHelper.getCursosDisponiveis();
        limparCampos();
        return "/restrito/cadastro/aluno?faces-redirect=true";
    }
    
    public void onChangeCurso() {
        if(cursoSelecionado !=null && !cursoSelecionado.equals(0)){
            turmasDisponiveis = turmaHelper.getTurmasByCurso(cursoSelecionado);
            turnosDisponiveis = new ArrayList<>();
            for(Turma turma : turmasDisponiveis){
                boolean encontrado = false;
                for(String str: turnosDisponiveis){
                    if(str.equalsIgnoreCase(turma.getTurno())){
                        encontrado = true;
                        break;
                    }
                }
                if(!encontrado){
                    turnosDisponiveis.add(turma.getTurno());
                }
            }
            dataInicioDisponiveis = new ArrayList<>();
        }else
            turmasDisponiveis = new ArrayList<>();
    }
    
    private String stringConsulta;

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String stringConsulta) {
        this.stringConsulta = stringConsulta;
    }
    
    private List<Aluno> resultadoConsulta;

    public List<Aluno> getResultadoConsulta() {
        return resultadoConsulta;
    }
    
    public String consultar(){
        resultadoConsulta = alunoHelper.getAlunos(stringConsulta);
        if(resultadoConsulta == null || resultadoConsulta.isEmpty()){
            addMessage(null, FacesMessage.SEVERITY_INFO ,"Nenhum resultado encontrado!");
        }
        return "";
     }
    
    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = new ArrayList<>();
    }
    
    private Aluno alunoDetalhe;

    public Aluno getAlunoDetalhe() {
        return alunoDetalhe;
    }
    
    public String exibirDetalhes(Aluno aluno) {
        alunoDetalhe = aluno;
        return "/restrito/cadastro/detalhe/aluno?faces-redirect=true";
    }
    
    public void onChangeTurno() {
        SimpleDateFormat format;
        format = new SimpleDateFormat("dd/MM/yyyy");
        if(turnoSelecionado !=null && !turnoSelecionado.equals("0")){
            dataInicioDisponiveis = new ArrayList<>();
            for(Turma turma : turmasDisponiveis){
                boolean encontrado = false;
                for(String date: dataInicioDisponiveis){
                    if(date.equalsIgnoreCase(format.format(turma.getDataInicio()))){
                        encontrado = true;
                        break;
                    }
                }
                if(!encontrado){
                    if(turma.getTurno().equalsIgnoreCase(turnoSelecionado))
                        dataInicioDisponiveis.add(format.format(turma.getDataInicio()));
                }
            }
        }else
            dataInicioDisponiveis = new ArrayList<>();
    }
    
    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String contentType = uploadedFile.getContentType();
        foto = new Foto();
        foto.setImagem(uploadedFile.getContents());
        image = new DefaultStreamedContent(new ByteArrayInputStream(foto.getImagem()), contentType);
    }

}
