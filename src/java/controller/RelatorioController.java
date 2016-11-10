package controller;

import entidade.Aluno;
import entidade.Curso;
import entidade.Foto;
import entidade.Pessoa;
import entidade.Telefone;
import entidade.Turma;
import helper.AlunoHelper;
import helper.CursoHelper;
import helper.LoginHelper;
import helper.TurmaHelper;
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
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import util.EmailSender;

@ManagedBean
@SessionScoped
public class RelatorioController {
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
    
    public RelatorioController() {
        cursoHelper = new CursoHelper();
        alunoHelper = new AlunoHelper();
        loginHelper = new LoginHelper();
        turmaHelper = new TurmaHelper();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    public String novoRelatorioMatricula(){
        return "/restrito/administrador/relatorio/matricula?faces-redirect=true";
    }
    
    public void gerarRelatorioMatricula(){
        List<Curso> cursos = cursoHelper.getCursosDisponiveis();
        createHorizontalBarModel(cursos);
        
    }
    
    private HorizontalBarChartModel horizontalBarModel;
 
     
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }
     
    private void createHorizontalBarModel(List<Curso> cursos) {
        horizontalBarModel = new HorizontalBarChartModel();
 
        int maiorMatriculas = -1;
        ChartSeries desistentes = new ChartSeries();
        desistentes.setLabel("Desistentes");
        ChartSeries ativos = new ChartSeries();
        ativos.setLabel("Matriculados");
        for(Curso curso:cursos){
            ativos.set(curso.getNome(), curso.getAlunosAtivos().size());
            desistentes.set(curso.getNome(), curso.getAlunosDesistentes().size());
           if(curso.getAlunosAtivos().size()+curso.getAlunosDesistentes().size() > maiorMatriculas){
               maiorMatriculas = curso.getAlunosAtivos().size()+curso.getAlunosDesistentes().size();
            }
        }
        
        horizontalBarModel.addSeries(ativos);
        horizontalBarModel.addSeries(desistentes);
         
        horizontalBarModel.setTitle("Matriculados por Curso");
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Quantidade de Matrículas");
        xAxis.setTickFormat("%d");
        xAxis.setMin(0);
        xAxis.setMax(maiorMatriculas + 5);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Curso");        
    }

}
