package controller;

import entidade.Disciplina;
import entidade.Discursiva;
import entidade.Figura;
import entidade.Objetiva;
import entidade.Opcao;
import entidade.Professor;
import entidade.Questao;
import entidade.Redacao;
import entidade.Assunto;
import helper.DisciplinaHelper;
import helper.ProfessorHelper;
import helper.QuestaoHelper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.ImagemUtil;

@ManagedBean
@SessionScoped
public class QuestaoController {

    private static final int IMG_HEIGHT = 400;
    private static final int IMG_WIDTH = 400;

    private String ano;
    private List<String> assuntosSelecionados;
    private DisciplinaHelper disciplinaHelper;
    private String disciplinaSelecionada;
    private List<Disciplina> disciplinasDisponiveis;
    private String stringConsulta;
    private String enunciado;
    private Figura figura;
    private List<Figura> figuras;
    private int height;
    private Integer idProfessorAtual;
    BufferedImage imagemOriginal;
    private String instituicao;
    private boolean opcaoCorreta;
    private List<Opcao> opcoes;
    private ProfessorHelper professorHelper;
    private Questao questaoDetalhe;
    private QuestaoHelper questaoHelper;
    private String resposta;
    private List<Questao> resultadoConsulta;
    private String tipo;
    boolean uploaded;
    private int width;
    private String nivel;
    private Integer dificuldade;

    public Integer getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Integer dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    
    

    public QuestaoController() {
        professorHelper = new ProfessorHelper();
        questaoHelper = new QuestaoHelper();
        disciplinaHelper = new DisciplinaHelper();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void cadastrarObjetiva() {

        boolean opCorreta = false;

        if (opcoes == null || opcoes.isEmpty() || opcoes.size() == 1) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Número de opções de resposta inválido!");
            return;
        }

        for (Opcao op : opcoes) {
            if (op.getCorreta()) {
                opCorreta = true;
                break;
            }
        }

        if (!opCorreta) {
            addMessage(null, FacesMessage.SEVERITY_ERROR, "Cadastre uma opção correta nas respostas!");
            return;
        }

        List<Assunto> assuntos = new ArrayList<>();

        if (assuntosSelecionados != null) {
            Assunto assunto;
            for (String strAssunto : assuntosSelecionados) {
                assunto = disciplinaHelper.getSubdisciplina(strAssunto, new Integer(disciplinaSelecionada));
                if (assunto != null) {
                    assuntos.add(assunto);
                } else {
                    Assunto novoAssunto = new Assunto();
                    novoAssunto.setNome(strAssunto.toLowerCase());
                    assuntos.add(novoAssunto);
                    for (Disciplina disciplina : disciplinasDisponiveis) {
                        if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                            novoAssunto.setDisciplina(disciplina);
                        }
                    }
                }
            }
        }

        if (figuras != null) {
            Pattern p = Pattern.compile("\\?id=(.*?)\"");
            Matcher m = p.matcher(enunciado);
            List<String> figurasNome = new ArrayList<>();

            boolean excluir;

            List<Figura> figurasExcluir = new ArrayList<>();

            while (m.find()) {
                figurasNome.add(m.group(1));
            }

            for (Figura figura : figuras) {

                excluir = true;

                for (String str : figurasNome) {
                    if (figura.getNome().equals(str)) {
                        excluir = false;
                        break;
                    }
                }

                if (excluir) {
                    figurasExcluir.add(figura);
                }
            }

            if (!figurasExcluir.isEmpty()) {
                figuras.removeAll(figurasExcluir);
            }
        }

        Questao questao = new Questao();

        questao.setAno(ano);
        questao.setEnunciado(enunciado);
        questao.setGrupo(null);
        questao.setInstituicao(instituicao);
        questao.setAssuntos(new HashSet<>(assuntos));

        if (figuras != null) {
            questao.setFiguras(new HashSet<>(figuras));
        }
        Professor professor = professorHelper.getById(idProfessorAtual);
        questao.setProfessor(professor);
        for (Disciplina disciplina : disciplinasDisponiveis) {
            if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                questao.setDisciplina(disciplina);
            }
        }

        questao.setTipo("Objetiva");
        questao.setNivel(nivel);
        questao.setDificuldade(dificuldade);
        Objetiva questaoObjetiva = new Objetiva();
        questaoObjetiva.setQuestao(questao);
        if (opcoes != null) {
            questaoObjetiva.setOpcaos(new HashSet<>(opcoes));
        }

        if (!questaoHelper.cadastrarObjetiva(questaoObjetiva)) {
            addMessage(null, "Cadastro não pode ser concluido, tente novamente!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Cadastro Concluido Com Sucesso!");

        limparCampos();

    }

    public void cadastrarDiscursiva() {

        List<Assunto> assuntos = new ArrayList<>();

        if (assuntosSelecionados != null) {
            Assunto assunto;
            for (String strAssunto : assuntosSelecionados) {
                assunto = disciplinaHelper.getSubdisciplina(strAssunto, new Integer(disciplinaSelecionada));
                if (assunto != null) {
                    assuntos.add(assunto);
                } else {
                    Assunto novoAssunto = new Assunto();
                    novoAssunto.setNome(strAssunto.toLowerCase());
                    assuntos.add(novoAssunto);
                    for (Disciplina disciplina : disciplinasDisponiveis) {
                        if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                            novoAssunto.setDisciplina(disciplina);
                        }
                    }
                }
            }
        }

        if (figuras != null) {
            Pattern p = Pattern.compile("\\?id=(.*?)\"");
            Matcher m = p.matcher(enunciado);
            List<String> figurasNome = new ArrayList<>();

            boolean excluir;

            List<Figura> figurasExcluir = new ArrayList<>();

            while (m.find()) {
                figurasNome.add(m.group(1));
            }

            for (Figura figura : figuras) {

                excluir = true;

                for (String str : figurasNome) {
                    if (figura.getNome().equals(str)) {
                        excluir = false;
                        break;
                    }
                }

                if (excluir) {
                    figurasExcluir.add(figura);
                }
            }

            if (!figurasExcluir.isEmpty()) {
                figuras.removeAll(figurasExcluir);
            }
        }

        Questao questao = new Questao();

        questao.setAno(ano);
        questao.setEnunciado(enunciado);
        questao.setGrupo(null);
        questao.setInstituicao(instituicao);
        questao.setAssuntos(new HashSet<Assunto>(assuntos));

        if (figuras != null) {
            questao.setFiguras(new HashSet<>(figuras));
        }
        Professor professor = professorHelper.getById(idProfessorAtual);
        questao.setProfessor(professor);
        for (Disciplina disciplina : disciplinasDisponiveis) {
            if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                questao.setDisciplina(disciplina);
            }
        }
        
        questao.setTipo("Discursiva");
        questao.setNivel(nivel);
        questao.setDificuldade(dificuldade);
        Discursiva questaoDiscursiva = new Discursiva();
        questaoDiscursiva.setQuestao(questao);
        questaoDiscursiva.setRespostaPadrao(resposta);

        if (!questaoHelper.cadastrarDiscursiva(questaoDiscursiva)) {
            addMessage(null, "Cadastro não pode ser concluido, tente novamente!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Cadastro Concluido Com Sucesso!");

        limparCampos();

    }

    public void cadastrarRedacao() {

        List<Assunto> assuntos = new ArrayList<>();

        if (assuntosSelecionados != null) {
            Assunto assunto;
            for (String strAssunto : assuntosSelecionados) {
                assunto = disciplinaHelper.getSubdisciplina(strAssunto, new Integer(disciplinaSelecionada));
                if (assunto != null) {
                    assuntos.add(assunto);
                } else {
                    Assunto novoAssunto = new Assunto();
                    novoAssunto.setNome(strAssunto.toLowerCase());
                    assuntos.add(novoAssunto);
                    for (Disciplina disciplina : disciplinasDisponiveis) {
                        if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                            novoAssunto.setDisciplina(disciplina);
                        }
                    }
                }
            }
        }

        if (figuras != null) {
            Pattern p = Pattern.compile("\\?id=(.*?)\"");
            Matcher m = p.matcher(enunciado);
            List<String> figurasNome = new ArrayList<>();

            boolean excluir;

            List<Figura> figurasExcluir = new ArrayList<>();

            while (m.find()) {
                figurasNome.add(m.group(1));
            }

            for (Figura figura : figuras) {

                excluir = true;

                for (String str : figurasNome) {
                    if (figura.getNome().equals(str)) {
                        excluir = false;
                        break;
                    }
                }

                if (excluir) {
                    figurasExcluir.add(figura);
                }
            }

            if (!figurasExcluir.isEmpty()) {
                figuras.removeAll(figurasExcluir);
            }
        }

        Questao questao = new Questao();

        questao.setAno(ano);
        questao.setEnunciado(enunciado);
        questao.setGrupo(null);
        questao.setInstituicao(instituicao);
        questao.setAssuntos(new HashSet<>(assuntos));

        if (figuras != null) {
            questao.setFiguras(new HashSet<>(figuras));
        }
        Professor professor = professorHelper.getById(idProfessorAtual);
        questao.setProfessor(professor);
        for (Disciplina disciplina : disciplinasDisponiveis) {
            if (disciplina.getIdDisciplina().equals(Integer.parseInt(disciplinaSelecionada))) {
                questao.setDisciplina(disciplina);
            }
        }

        questao.setTipo("Redação");
        questao.setNivel(nivel);
        questao.setDificuldade(dificuldade);
        Redacao redacao = new Redacao();
        redacao.setQuestao(questao);

        if (!questaoHelper.cadastrarRedacao(redacao)) {
            addMessage(null, "Cadastro não pode ser concluido, tente novamente!");
            return;
        }

        addMessage(null, FacesMessage.SEVERITY_INFO, "Cadastro Concluido Com Sucesso!");

        limparCampos();

    }

    public List<String> completarAssuntos(String query) {
        List<String> results = new ArrayList<>();
        List<Assunto> assuntos = disciplinaHelper.getSubdisciplinas(new Integer(disciplinaSelecionada));
        boolean encontrado = false;
        if (assuntos != null) {
            encontrado = false;
            for (Assunto assunto : assuntos) {
                if (assunto.getNome().toLowerCase().equals(query.toLowerCase())) {
                    encontrado = true;
                    break;
                }
            }
        }
        if (!encontrado) {
            results.add(query);
        }
        if (assuntos != null) {
            for (Assunto assunto : assuntos) {
                if (assunto.getNome().toLowerCase().startsWith(query)) {
                    results.add(assunto.getNome());
                }
            }

        }

        return results;
    }

    public void consultar() {
        resultadoConsulta = questaoHelper.getQuestoes(stringConsulta);
        if (resultadoConsulta == null || resultadoConsulta.isEmpty()) {
            addMessage(null, FacesMessage.SEVERITY_INFO, "Nenhum resultado encontrado!");
        }
    }

    public void excluirResposta(Opcao opcao) {
        opcoes.remove(opcao);
    }

    public void exibirDetalhes(Questao questao) {
        questaoDetalhe = questaoHelper.getQuestaoByIdEager(questao.getIdQuestao());
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List<Assunto> getAssuntosDetalhe() {
        if(questaoDetalhe == null) return new ArrayList<>();
        if (questaoDetalhe.getAssuntos()!= null) {
            return new ArrayList<>(questaoDetalhe.getAssuntos());
        }
        return new ArrayList<>();
    }

    public List<String> getAssuntosSelecionados() {
        return assuntosSelecionados;
    }

    public void setAssuntosSelecionados(List<String> assuntosSelecionados) {
        this.assuntosSelecionados = assuntosSelecionados;
    }

    public String getDisciplinaSelecionada() {
        return disciplinaSelecionada;
    }

    public void setDisciplinaSelecionada(String disciplinaSelecionada) {
        this.disciplinaSelecionada = disciplinaSelecionada;
    }

    public List<Disciplina> getDisciplinasDisponiveis() {

        return disciplinasDisponiveis;
    }

    public String getStringConsulta() {
        return stringConsulta;
    }

    public void setStringConsulta(String entradaConsulta) {
        this.stringConsulta = entradaConsulta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public List<Figura> getFiguras() {
        return this.figuras;
    }

    public int getHeight() {
        return height;
    }

    public int getHeightFigura() {
        if (figura == null) {
            return IMG_HEIGHT;
        } else {
            return height;
        }
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE || figura == null) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            return new DefaultStreamedContent(new ByteArrayInputStream(figura.getImagem()));
        }
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public List<Opcao> getOpcoes() {
        return this.opcoes;
    }

    public Questao getQuestaoDetalhe() {
        return questaoDetalhe;
    }

    public String getResposta() {
        return this.resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public List<Questao> getResultadoConsulta() {
        return resultadoConsulta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getWidth() {
        return width;
    }

    public int getWidthFigura() {
        if (figura == null) {
            return IMG_WIDTH;
        } else {
            return width;
        }
    }

    public void incluirFigura() {
        if (figuras == null) {
            figuras = new ArrayList<>();
        }
        UUID uuid = UUID.randomUUID();
        String nomeFigura = uuid.toString();
        figura.setNome(nomeFigura);
        figuras.add(figura);
        if (enunciado == null) {
            enunciado = "";
        }
        enunciado = enunciado + "<img _moz_dirty=\"\" src=\"/sgeda/imagem/?id=" + nomeFigura + "\" >";
        figura = null;
        width = 0;
        height = 0;
        uploaded = false;
    }

    public void incluirResposta() {
        if (opcoes == null) {
            opcoes = new ArrayList<>();
        }

        if (opcaoCorreta) {
            for (Opcao op : opcoes) {
                if (op.getCorreta()) {
                    addMessage("cadastro:opcao", "Opção correta já selecionada!");
                    return;
                }
            }
        }

        if (opcoes.size() >= 5) {
            addMessage("cadastro:incluirRespostaButton", "Número máximo de opções de resposta alcaçado!");
            return;
        }

        Opcao opcao = new Opcao();
        opcao.setDescricao(resposta);
        opcao.setCorreta(opcaoCorreta);

        opcoes.add(opcao);

        resposta = null;
        opcaoCorreta = false;

    }

    public boolean isOpcaoCorreta() {
        return opcaoCorreta;
    }

    public void setOpcaoCorreta(boolean opcaoCorreta) {
        this.opcaoCorreta = opcaoCorreta;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void limparCampos() {
        instituicao = null;
        ano = null;
        disciplinaSelecionada = null;
        enunciado = null;
        figuras = null;
        figura = null;
        height = 0;
        width = 0;
        opcaoCorreta = false;
        resposta = null;
        opcoes = null;
        assuntosSelecionados = new ArrayList<>();
        nivel = null;
        dificuldade = null;
    }

    public String limparFormularioCadastro() {
        limparCampos();
        return "";
    }

    public String limparFormularioCadastroObjetiva() {
        return novoCadastroObjetiva(idProfessorAtual);
    }

    public void limparFormularioConsulta() {
        stringConsulta = null;
        resultadoConsulta = null;
    }

    public String novaConsulta() {
        limparCampos();
        consultar();
        return "/restrito/professor/consulta/questao?faces-redirect=true";
    }

    public String novoCadastroDiscursiva(Integer idProfessor) {
        limparCampos();
        Professor professor = professorHelper.getByIdEagerDisciplinas(idProfessor);
        idProfessorAtual = idProfessor;
        if (professor != null) {
            disciplinasDisponiveis = new ArrayList<>(professor.getDisciplinas());
        }
        return "/restrito/professor/cadastro/questaoDiscursiva?faces-redirect=true";
    }

    public String novoCadastroGrupo() {
        limparCampos();
        return "/restrito/professor/cadastro/questaoGrupo?faces-redirect=true";
    }

    public String novoCadastroObjetiva(Integer idProfessor) {
        limparCampos();
        Professor professor = professorHelper.getByIdEagerDisciplinas(idProfessor);
        idProfessorAtual = idProfessor;
        if (professor != null) {
            disciplinasDisponiveis = new ArrayList<>(professor.getDisciplinas());
        }
        return "/restrito/professor/cadastro/questaoObjetiva?faces-redirect=true";
    }

    public String novoCadastroRedacao(Integer idProfessor) {
        limparCampos();
        Professor professor = professorHelper.getByIdEagerDisciplinas(idProfessor);
        idProfessorAtual = idProfessor;
        if (professor != null) {
            disciplinasDisponiveis = new ArrayList<>(professor.getDisciplinas());
        }
        return "/restrito/professor/cadastro/questaoRedacao?faces-redirect=true";
    }

    public void upload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        try {
            imagemOriginal = ImageIO.read(uploadedFile.getInputstream());
            height = imagemOriginal.getHeight();
            width = imagemOriginal.getWidth();
            int type = imagemOriginal.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

            Double razao;
            Double dHeight = (double) imagemOriginal.getHeight();
            Double dWidth = (double) imagemOriginal.getWidth();

            if (imagemOriginal.getWidth() >= imagemOriginal.getHeight()) {
                width = IMG_WIDTH;
            } else {
                razao = dHeight / dWidth;
                width = (new Double(((double) IMG_HEIGHT) / razao)).intValue();
            }

            if (imagemOriginal.getHeight() >= imagemOriginal.getWidth()) {
                height = IMG_HEIGHT;
            } else {
                razao = dWidth / dHeight;
                height = (new Double(((double) IMG_WIDTH) / razao)).intValue();
            }

            imagemOriginal = ImagemUtil.resizeImage(width, height, imagemOriginal, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(imagemOriginal, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            figura = new Figura();
            figura.setTipo("jpg");
            figura.setImagem(imageInByte);
            figura.setTamanho(Integer.toString(baos.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        uploaded = true;

    }

}
