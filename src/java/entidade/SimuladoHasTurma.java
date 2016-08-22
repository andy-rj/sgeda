package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SimuladoHasTurma generated by hbm2java
 */
@Entity
@Table(name="simulado_has_turma"
    ,catalog="sgeda"
)
public class SimuladoHasTurma  implements java.io.Serializable {


     private SimuladoHasTurmaId id;
     private Simulado simulado;
     private Turma turma;
     private Date dataAbertura;
     private Date dataEncerramento;
     private Date duracao;

    public SimuladoHasTurma() {
    }

	
    public SimuladoHasTurma(SimuladoHasTurmaId id, Simulado simulado, Turma turma) {
        this.id = id;
        this.simulado = simulado;
        this.turma = turma;
    }
    public SimuladoHasTurma(SimuladoHasTurmaId id, Simulado simulado, Turma turma, Date dataAbertura, Date dataEncerramento, Date duracao) {
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
        @AttributeOverride(name="turmaIdTurma", column=@Column(name="turma_idTurma", nullable=false) ), 
        @AttributeOverride(name="turmaIdDisciplina", column=@Column(name="turma_idDisciplina", nullable=false) ) } )
    public SimuladoHasTurmaId getId() {
        return this.id;
    }
    
    public void setId(SimuladoHasTurmaId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="simulado_idSimulado", nullable=false, insertable=false, updatable=false)
    public Simulado getSimulado() {
        return this.simulado;
    }
    
    public void setSimulado(Simulado simulado) {
        this.simulado = simulado;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns( { 
        @JoinColumn(name="turma_idTurma", referencedColumnName="idTurma", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="turma_idDisciplina", referencedColumnName="idDisciplina", nullable=false, insertable=false, updatable=false) } )
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




}


