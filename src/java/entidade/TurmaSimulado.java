package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * TurmaSimulado generated by hbm2java
 */
@Entity
@Table(name="turma_simulado"
    ,catalog="kemdixip_sgedanovo"
)
public class TurmaSimulado  implements java.io.Serializable {


     private TurmaSimuladoId id;
     private Simulado simulado;
     private Turma turma;
     private Date dataAbertura;
     private Date dataEncerramento;
     private Date duracao;

    public TurmaSimulado() {
    }

	
    public TurmaSimulado(TurmaSimuladoId id, Simulado simulado, Turma turma) {
        this.id = id;
        this.simulado = simulado;
        this.turma = turma;
    }
    public TurmaSimulado(TurmaSimuladoId id, Simulado simulado, Turma turma, Date dataAbertura, Date dataEncerramento, Date duracao) {
       this.id = id;
       this.simulado = simulado;
       this.turma = turma;
       this.dataAbertura = dataAbertura;
       this.dataEncerramento = dataEncerramento;
       this.duracao = duracao;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="simuladoIdSimulado", column=@Column(name="simulado_idSimulado", nullable=false) ), 
        @AttributeOverride(name="turmaIdTurma", column=@Column(name="turma_idTurma", nullable=false) ) } )
    public TurmaSimuladoId getId() {
        return this.id;
    }
    
    public void setId(TurmaSimuladoId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="simulado_idSimulado", nullable=false, insertable=false, updatable=false)
    public Simulado getSimulado() {
        return this.simulado;
    }
    
    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="turma_idTurma", nullable=false, insertable=false, updatable=false)
    public Turma getTurma() {
        return this.turma;
    }
    
    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dataAbertura", length=19)
    public Date getDataAbertura() {
        return this.dataAbertura;
    }
    
    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dataEncerramento", length=19)
    public Date getDataEncerramento() {
        return this.dataEncerramento;
    }
    
    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    @Temporal(TemporalType.TIME)
    @Column(name="duracao", length=8)
    public Date getDuracao() {
        return this.duracao;
    }
    
    public void setDuracao(Date duracao) {
        this.duracao = duracao;
    }

    @Transient
    public boolean isSimuladoAberto(){
        Date now = new Date();
        if(dataAbertura.before(now) && dataEncerramento.after(now)) return true;
        return false;
    }


}


