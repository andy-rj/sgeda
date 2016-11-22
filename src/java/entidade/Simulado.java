package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Simulado generated by hbm2java
 */
@Entity
@Table(name="simulado"
    ,catalog="kemdixip_sgedanovo"
)
public class Simulado  implements java.io.Serializable {


     private Integer idSimulado;
     private Professor professor;
     private String nome;
     private String descricao;
     private String status;
     private String tipo;
     private Date dataCadastro;
     private Set<SimuladoQuestao> simuladoQuestaos = new HashSet(0);
     private Set<TurmaSimulado> turmaSimulados = new HashSet(0);

    public Simulado() {
    }

	
    public Simulado(Professor professor) {
        this.professor = professor;
    }
    public Simulado(Professor professor, String nome, String descricao, String status, String tipo, Date dataCadastro, Set<SimuladoQuestao> simuladoQuestaos, Set<TurmaSimulado> turmaSimulados, Set<AlunoSimulado> alunoSimulados) {
       this.professor = professor;
       this.nome = nome;
       this.descricao = descricao;
       this.status = status;
       this.tipo = tipo;
       this.dataCadastro = dataCadastro;
       this.simuladoQuestaos = simuladoQuestaos;
       this.turmaSimulados = turmaSimulados;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idSimulado", unique=true, nullable=false)
    public Integer getIdSimulado() {
        return this.idSimulado;
    }
    
    public void setIdSimulado(Integer idSimulado) {
        this.idSimulado = idSimulado;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="idProfessor", nullable=false)
    public Professor getProfessor() {
        return this.professor;
    }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    
    @Column(name="nome", length=20)
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    
    @Column(name="descricao")
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    @Column(name="status", length=45)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    
    @Column(name="tipo", length=45)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dataCadastro", length=19)
    public Date getDataCadastro() {
        return this.dataCadastro;
    }
    
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="simulado")
    public Set<SimuladoQuestao> getSimuladoQuestaos() {
        return this.simuladoQuestaos;
    }
    
    @Transient
    public List<SimuladoQuestao> getSimuladoQuestaosList(){
        return new ArrayList<>(getSimuladoQuestaos());
    }
    
    public void setSimuladoQuestaos(Set<SimuladoQuestao> simuladoQuestaos) {
        this.simuladoQuestaos = simuladoQuestaos;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="simulado")
    public Set<TurmaSimulado> getTurmaSimulados() {
        return this.turmaSimulados;
    }
    
    public void setTurmaSimulados(Set<TurmaSimulado> turmaSimulados) {
        this.turmaSimulados = turmaSimulados;
    }






}


