package controller;

import entidade.Aluno;
import entidade.AlunoSimulado;
import entidade.AlunoSimuladoId;
import entidade.Resposta;
import entidade.SimuladoQuestao;
import entidade.TurmaSimulado;
import helper.AlunoHelper;
import helper.SimuladoHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class AlunoSimuladoController {
    private Date agora;
    
    private AlunoHelper alunoHelper;
    public boolean finalizado;
    private Integer indiceQuestaoAtual;
    
    
    private Date inicio;
    private Integer numeroQuestaoAtual;
    private Integer numeroQuestoes;
    private String opcaoAtualSelecionada;
    private Resposta questaoAtual;
    private String respostaAtual;
    private AlunoSimulado simulado;
    private TurmaSimulado simuladoAtual;
    private SimuladoHelper simuladoHelper;
    private Long tempoRestante;
    private Long tempoTotal;
    
    @PreDestroy
    public void sessionDestroyed(){
        if(simuladoAtual!=null){
            forcarFinalizarSimulado();
        }
    }

    public AlunoSimuladoController() {
        alunoHelper = new AlunoHelper();
        simuladoHelper = new SimuladoHelper();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    
    public void atualizaCronometro(){
        agora = new Date();
        tempoRestante = tempoTotal - (agora.getTime() - inicio.getTime());
        if(tempoRestante <= 0){
            forcarFinalizarSimulado();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('modalMensagens').show();");
            context.execute("poll.stop();");
        }
        
    }
    
    public String cadastrar() {
        return "";
    }
    
    public void desmarcarRevisao() {
        questaoAtual.setMarcadaRevisao(false);
    }

    public void escolherQuestao(int indice) {
        if(questaoAtual.getQuestao().getObjetiva()!=null){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada=null;
        }
        }
        else
        {
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        }
        
        indiceQuestaoAtual = indice;
        questaoAtual = respostas.get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }

    public void fecharConfirmacaoSimulado() {
        simuladoAtual = null;
    }

    public String fecharPaginaSimulado() {
        return "/restrito/aluno/home?faces-redirect=true";
    }

    public void finalizarSimulado() {
        finalizado = true;
        if(questaoAtual.getQuestao().getObjetiva() != null){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
        }
        }
        else
        {
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(respostaAtual);
        }
        }
        
        for(Resposta resposta:simulado.getRespostas()){
            if(resposta.isMarcadaRevisao()){
                addMessage(null, FacesMessage.SEVERITY_WARN, "Existem questões marcadas para revisão!");
                finalizado = false;
                return;
            }
            if(resposta.getResposta()==null||resposta.getResposta().isEmpty()){
                addMessage(null, FacesMessage.SEVERITY_WARN, "Existem questões não respondidas!");
                finalizado = false;
                return;
            }
        }
        
        forcarFinalizarSimulado();
        
    }

    public void forcarFinalizarSimulado() {
        if(questaoAtual.getQuestao().getObjetiva() != null){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
        }
        }
        else
        {
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(respostaAtual);
        }
        }
        
        for(Resposta resposta:respostas){
            if(resposta.getQuestao().getObjetiva()!=null){
                if(resposta.getResposta()!=null && resposta.getResposta().equalsIgnoreCase(resposta.getQuestao().getObjetiva().getOpcaoCorreta())){
                    resposta.setNota(resposta.getQuestao().getValorQuestao(simulado.getTurmaSimulado().getSimulado(),resposta.getQuestao()));
                }
                else{
                    resposta.setNota(new BigDecimal(BigInteger.ZERO));
                }
            }
        }
        
        simulado.setRespostas(new HashSet<>(respostas));
        
        if(simuladoHelper.cadastrarSimuladoAluno(simulado)){
            addMessage(null,FacesMessage.SEVERITY_INFO, "Simulado Finalizado com sucesso!");
            finalizado = true;
            return;
        }
        addMessage(null,FacesMessage.SEVERITY_ERROR, "Simulado não pode ser finalizado!");
        finalizado = false;
        simuladoAtual = null;
    }
    

    public String getHorasRestante() {
        Long hora = tempoRestante/3600000;
        if(hora < 0) return "0";
        return hora.toString();
    }

    
    public Integer getIndiceQuestaoAtual(){
        return indiceQuestaoAtual;
    }

    public String getMinutosRestante() {
        Long minutos = (tempoRestante/60000)%60;
        if(minutos < 0) return "00";
        if(minutos<10) return "0"+minutos.toString();
        return minutos.toString();
    }
    

    public Integer getNumeroQuestaoAtual() {
        return numeroQuestaoAtual;
    }
    

    public Integer getNumeroQuestoes() {
        return numeroQuestoes;
    }

    public String getOpcaoAtualSelecionada() {
        return opcaoAtualSelecionada;
    }

    public void setOpcaoAtualSelecionada(String opcaoAtualSelecionada) {
        this.opcaoAtualSelecionada = opcaoAtualSelecionada;
    }

    public Resposta getQuestaoAtual() {
        return questaoAtual;
    }

    public String getRespostaAtual() {
        return respostaAtual;
    }
    

    public void setRespostaAtual(String respostaAtual) {
        this.respostaAtual = respostaAtual;
    }

    public String getSegundosRestante() {
        Long segundos = (tempoRestante/1000)%60;
        if(segundos < 0) return "00";
        if(segundos<10) return "0"+segundos.toString();
        return segundos.toString();
    }
    
    public String getSeveridadeCronometro(){
        if(tempoRestante<tempoTotal/4){
            return "danger";
        }
        else if(tempoRestante<tempoTotal/2){
            return "warning";
        }
        return "success";
    }
    
    public AlunoSimulado getSimulado(){
        return simulado;
    }
    
    public TurmaSimulado getSimuladoAtual(){
        return simuladoAtual;
    }
    

    public Long getTempoRestante() {
        return tempoRestante;
    }
    private List<Resposta> respostas;

    public List<Resposta> getRespostas() {
        return respostas;
    }
    public String iniciarRealizacaoSimulado(Integer idAluno){
        inicio = agora = new Date();
        tempoTotal = new Long(simuladoAtual.getDuracao().getHours()*3600000+simuladoAtual.getDuracao().getMinutes()*60000+simuladoAtual.getDuracao().getSeconds()*1000);
        tempoRestante = tempoTotal;
        numeroQuestoes = simuladoAtual.getSimulado().getSimuladoQuestaos().size();
        
        simulado = new AlunoSimulado();
        Aluno aluno = alunoHelper.getById(idAluno);
        simulado.setAluno(aluno);
        simulado.setTurmaSimulado(simuladoAtual);
        simulado.setData(new Date());
        simulado.setId(new AlunoSimuladoId(simulado.getAluno().getIdAluno(),simulado.getTurmaSimulado().getSimulado().getIdSimulado(),simulado.getTurmaSimulado().getTurma().getIdTurma()));
        respostas = new ArrayList<>();
        for(SimuladoQuestao questao : simuladoAtual.getSimulado().getSimuladoQuestaos()){
            Resposta resposta = new Resposta();
            resposta.setQuestao(questao.getQuestao());
            resposta.setAlunoSimulado(simulado);
            respostas.add(resposta);
        }
        
        questaoAtual = respostas.get(0);
        indiceQuestaoAtual = 0;
        
        //toDo
        //salvar simulado no banco para que não seja possivel realizalo novamente ao fechar a pagina
        
        return "/restrito/aluno/simulado?faces-redirect=true";
    }
    
    public boolean isFinalizado(){
        return finalizado;
    }
    
    public void setFinalizado(boolean finalizado){
        this.finalizado = finalizado;
    }
    
    public void limparCampos(){
    }
    
    public String limparFormularioCadastro(){
        limparCampos();
        return "";
    }

    public void marcarRevisao() {
        questaoAtual.setMarcadaRevisao(true);
    }

    public String novaConsulta() {
        return "";
    }

    public void proximaQuestao() {
        if(questaoAtual.getQuestao().getObjetiva() != null){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada=null;
        }
        }
        else {
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        }
        
        indiceQuestaoAtual++;
        questaoAtual = respostas.get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }

    public void questaoAnterior() {
        if(questaoAtual.getQuestao().getObjetiva()!=null){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada = null;
        }
        }
        else
        {
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            respostas.get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        }
        
        indiceQuestaoAtual--;
        questaoAtual = respostas.get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }

    public void realizarSimulado(TurmaSimulado simulado) {
        simuladoAtual = simulado;
    }
    
    

}
