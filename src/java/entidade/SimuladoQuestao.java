package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SimuladoQuestao generated by hbm2java
 */
@Entity
@Table(name="simulado_questao"
    ,catalog="kemdixip_sgedanovo"
)
public class SimuladoQuestao  implements java.io.Serializable {


     private SimuladoQuestaoId id;
     private Questao questao;
     private Simulado simulado;
     private BigDecimal valorQuestao;

    public SimuladoQuestao() {
    }

	
    public SimuladoQuestao(SimuladoQuestaoId id, Questao questao, Simulado simulado) {
        this.id = id;
        this.questao = questao;
        this.simulado = simulado;
    }
    public SimuladoQuestao(SimuladoQuestaoId id, Questao questao, Simulado simulado, BigDecimal valorQuestao) {
       this.id = id;
       this.questao = questao;
       this.simulado = simulado;
       this.valorQuestao = valorQuestao;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="simuladoIdSimulado", column=@Column(name="simulado_idSimulado", nullable=false) ), 
        @AttributeOverride(name="questaoIdQuestao", column=@Column(name="questao_idQuestao", nullable=false) ) } )
    public SimuladoQuestaoId getId() {
        return this.id;
    }
    
    public void setId(SimuladoQuestaoId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="questao_idQuestao", nullable=false, insertable=false, updatable=false)
    public Questao getQuestao() {
        return this.questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="simulado_idSimulado", nullable=false, insertable=false, updatable=false)
    public Simulado getSimulado() {
        return this.simulado;
    }
    
    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

    
    @Column(name="valorQuestao", precision=5)
    public BigDecimal getValorQuestao() {
        return this.valorQuestao;
    }
    
    public void setValorQuestao(BigDecimal valorQuestao) {
        this.valorQuestao = valorQuestao;
    }




}


