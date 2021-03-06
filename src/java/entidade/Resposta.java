package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Resposta generated by hbm2java
 */
@Entity
@Table(name = "resposta", catalog = "kemdixip_sgedanovo"
)
public class Resposta implements java.io.Serializable {

    private Integer idResposta;
    private AlunoSimulado alunoSimulado;
    private Questao questao;
    private BigDecimal nota;
    private String resposta;

    public Resposta() {
    }

    public Resposta(AlunoSimulado alunoSimulado, Questao questao) {
        this.alunoSimulado = alunoSimulado;
        this.questao = questao;
    }

    public Resposta(AlunoSimulado alunoSimulado, Questao questao, BigDecimal nota, String resposta) {
        this.alunoSimulado = alunoSimulado;
        this.questao = questao;
        this.nota = nota;
        this.resposta = resposta;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "idResposta", unique = true, nullable = false)
    public Integer getIdResposta() {
        return this.idResposta;
    }

    public void setIdResposta(Integer idResposta) {
        this.idResposta = idResposta;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "idAluno", referencedColumnName = "aluno_idAluno", nullable = false),
        @JoinColumn(name = "idTurma", referencedColumnName = "turma_idTurma", nullable = false),
        @JoinColumn(name = "idSimulado", referencedColumnName = "simulado_idSimulado", nullable = false)})
    public AlunoSimulado getAlunoSimulado() {
        return this.alunoSimulado;
    }

    public void setAlunoSimulado(AlunoSimulado alunoSimulado) {
        this.alunoSimulado = alunoSimulado;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idQuestao", nullable = false)
    public Questao getQuestao() {
        return this.questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    @Column(name = "nota", precision = 5)
    public BigDecimal getNota() {
        return this.nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    @Column(name = "resposta", length = 3000)
    public String getResposta() {
        return this.resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    private boolean marcadaRevisao;

    @Transient
    public boolean isCorreta() {
        if (questao.getObjetiva() != null) {
            if (questao.getObjetiva().getOpcaoCorreta().equalsIgnoreCase(resposta)) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public boolean isMarcadaRevisao() {
        return marcadaRevisao;
    }

    public void setMarcadaRevisao(boolean marcadaRevisao) {
        this.marcadaRevisao = marcadaRevisao;
    }

    @Transient
    public BigDecimal getNotaMaximaQuestao() {
        if (questao.getSimuladoQuestaos() == null) {
            return null;
        }
        if (alunoSimulado == null) {
            return null;
        }
        for (SimuladoQuestao simuladoQuestao : questao.getSimuladoQuestaos()) {
            if (simuladoQuestao.getQuestao().getIdQuestao() == questao.getIdQuestao()
                    && simuladoQuestao.getSimulado().getIdSimulado() == alunoSimulado.getTurmaSimulado().getSimulado().getIdSimulado()) {
                return simuladoQuestao.getValorQuestao();
            }
        }
        return null;
    }

}
