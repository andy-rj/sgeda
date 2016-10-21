package controller;

import entidade.Aluno;
import entidade.AlunoSimulado;
import entidade.AlunoSimuladoId;
import entidade.Resposta;
import entidade.SimuladoQuestao;
import entidade.TurmaSimulado;
import helper.AlunoHelper;
import helper.SimuladoHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class AlunoSimuladoController {
    private Date agora;
    
    private AlunoHelper alunoHelper;
    private SimuladoHelper simuladoHelper;
    
    
    private Date inicio;
    private Integer numeroQuestaoAtual;
    private Integer numeroQuestoes;
    private AlunoSimulado simulado;
    private TurmaSimulado simuladoAtual;
    private Long tempoRestante;
    private Long tempoTotal;

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
    }
    
    public String cadastrar() {
        return "";
    }
    
    public void fecharConfirmacaoSimulado() {
        simuladoAtual = null;
    }

    public String getHorasRestante() {
        Long hora = tempoRestante/3600000;
        if(hora < 0) return "0";
        return hora.toString();
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

    public String getSegundosRestante() {
        Long segundos = (tempoRestante/1000)%60;
        if(segundos < 0) return "00";
        if(segundos<10) return "0"+segundos.toString();
        return segundos.toString();
    }
    

    public String getSeveridadeCronometro() {
        if(tempoRestante<tempoTotal/4){
            return "danger";
        }
        else if(tempoRestante<tempoTotal/2){
            return "warning";
        }
        return "success";
    }

    
    public TurmaSimulado getSimuladoAtual(){
        return simuladoAtual;
    }

    public Long getTempoRestante() {
        return tempoRestante;
    }
    
    private Resposta questaoAtual;

    public Resposta getQuestaoAtual() {
        return questaoAtual;
    }
    
    private String opcaoAtualSelecionada;
    private String respostaAtual;

    public String getRespostaAtual() {
        return respostaAtual;
    }

    public void setRespostaAtual(String respostaAtual) {
        this.respostaAtual = respostaAtual;
    }

    public String getOpcaoAtualSelecionada() {
        return opcaoAtualSelecionada;
    }

    public void setOpcaoAtualSelecionada(String opcaoAtualSelecionada) {
        this.opcaoAtualSelecionada = opcaoAtualSelecionada;
    }

    public AlunoSimulado getSimulado() {
        return simulado;
    }
    
    public boolean finalizado;

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
    
    public void finalizarSimulado(){
        finalizado = true;
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
        }
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(respostaAtual);
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
    
    public String fecharPaginaSimulado(){
        return "/restrito/aluno/home?faces-redirect=true";
    }
    
    public void forcarFinalizarSimulado(){
        if(simuladoHelper.cadastrarSimuladoAluno(simulado)){
            addMessage(null,FacesMessage.SEVERITY_INFO, "Simulado Finalizado com sucesso!");
            return;
        }
        addMessage(null,FacesMessage.SEVERITY_ERROR, "Simulado não pode ser finalizado!");
        finalizado = false;
    }
    
    private Integer indiceQuestaoAtual;

    public Integer getIndiceQuestaoAtual() {
        return indiceQuestaoAtual;
    }
    
    public void proximaQuestao(){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada=null;
        }
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        
        indiceQuestaoAtual++;
        questaoAtual = simulado.getRespostas().get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }
    
    public void escolherQuestao(int indice){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada=null;
        }
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        
        indiceQuestaoAtual = indice;
        questaoAtual = simulado.getRespostas().get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }
    
    public void questaoAnterior(){
        if(opcaoAtualSelecionada != null && !opcaoAtualSelecionada.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(opcaoAtualSelecionada);
            opcaoAtualSelecionada = null;
        }
        
        if(respostaAtual != null && !respostaAtual.isEmpty()){
            simulado.getRespostas().get(indiceQuestaoAtual).setResposta(respostaAtual);
            respostaAtual = null;
        }
        
        indiceQuestaoAtual--;
        questaoAtual = simulado.getRespostas().get(indiceQuestaoAtual);
        
        if(questaoAtual.getQuestao().getObjetiva() != null)
            opcaoAtualSelecionada = questaoAtual.getResposta();
        else
            respostaAtual = questaoAtual.getResposta();
    }
    
    public void marcarRevisao(){
        questaoAtual.setMarcadaRevisao(true);
    }
    
    public void desmarcarRevisao(){
        questaoAtual.setMarcadaRevisao(false);
    }

    public String iniciarRealizacaoSimulado(Integer idAluno) {
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
        simulado.setRespostas(new ArrayList<Resposta>());
        for(SimuladoQuestao questao : simuladoAtual.getSimulado().getSimuladoQuestaos()){
            Resposta resposta = new Resposta();
            resposta.setQuestao(questao.getQuestao());
            resposta.setAlunoSimulado(simulado);
            simulado.getRespostas().add(resposta);
        }
        
        questaoAtual = simulado.getRespostas().get(0);
        indiceQuestaoAtual = 0;
        
        return "/restrito/aluno/simulado?faces-redirect=true";
    }

    public void limparCampos() {
    }

    public String limparFormularioCadastro() {
        limparCampos();
        return "";
    }

    public String novaConsulta() {
        return "";
    }

    public void realizarSimulado(TurmaSimulado simulado) {
        simuladoAtual = simulado;
    }

}
