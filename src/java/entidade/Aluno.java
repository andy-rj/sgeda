package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Aluno generated by hbm2java
 */
@Entity
@Table(name="aluno"
    ,catalog="kemdixip_sgedanovo"
)
public class Aluno  implements java.io.Serializable {


     private int idAluno;
     private Pessoa pessoa;
     private String nomeResponsavel;
     private String escolaridade;
     private String cpfResponsavel;
     private String telefoneResponsavel;
     private Set<TurmaAluno> turmaAlunos = new HashSet(0);
     private Set<AlunoSimulado> alunoSimulados = new HashSet(0);
     private Boolean desistente;

    public Aluno() {
    }

	
    public Aluno(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    public Aluno(Pessoa pessoa, String nomeResponsavel, String escolaridade, String cpfResponsavel, String telefoneResponsavel, Set<TurmaAluno> turmaAlunos, Set<AlunoSimulado> alunoSimulados) {
       this.pessoa = pessoa;
       this.nomeResponsavel = nomeResponsavel;
       this.escolaridade = escolaridade;
       this.cpfResponsavel = cpfResponsavel;
       this.telefoneResponsavel = telefoneResponsavel;
       this.turmaAlunos = turmaAlunos;
       this.alunoSimulados = alunoSimulados;
    }
   
    @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="pessoa"))
    @Id @GeneratedValue(generator="generator")
    @Column(name="idAluno", unique=true, nullable=false)
    public int getIdAluno() {
        return this.idAluno;
    }
    
    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    @OneToOne(fetch=FetchType.EAGER)@PrimaryKeyJoinColumn
    public Pessoa getPessoa() {
        return this.pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    
    @Column(name="nomeResponsavel")
    public String getNomeResponsavel() {
        return this.nomeResponsavel;
    }
    
    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    
    @Column(name="escolaridade", length=50)
    public String getEscolaridade() {
        return this.escolaridade;
    }
    
    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    
    @Column(name="cpfResponsavel", length=11)
    public String getCpfResponsavel() {
        return this.cpfResponsavel;
    }
    
    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    
    @Column(name="telefoneResponsavel", length=11)
    public String getTelefoneResponsavel() {
        return this.telefoneResponsavel;
    }
    
    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    @OneToMany(fetch=FetchType.EAGER, mappedBy="aluno")
    public Set<TurmaAluno> getTurmaAlunos() {
        return this.turmaAlunos;
    }
    
    public void setTurmaAlunos(Set<TurmaAluno> turmaAlunos) {
        this.turmaAlunos = turmaAlunos;
    }

    @OneToMany(fetch=FetchType.EAGER, mappedBy="aluno")
    public Set<AlunoSimulado> getAlunoSimulados() {
        return this.alunoSimulados;
    }
    
    public void setAlunoSimulados(Set<AlunoSimulado> alunoSimulados) {
        this.alunoSimulados = alunoSimulados;
    }

    @Transient
    public Curso getCurso(){
        if(turmaAlunos != null && !turmaAlunos.isEmpty()){
            for(TurmaAluno turma : turmaAlunos){
                return turma.getTurma().getCurso();
            }
        }
        return null;
    }
    
    @Transient
    public Date getDataInicio(){
        if(turmaAlunos != null && !turmaAlunos.isEmpty()){
            for(TurmaAluno turma : turmaAlunos){
                return turma.getTurma().getDataInicio();
            }
        }
        return null;
    }
    
    @Transient
    public Date getDataFim(){
        if(turmaAlunos != null && !turmaAlunos.isEmpty()){
            for(TurmaAluno turma : turmaAlunos){
                return turma.getTurma().getDataFim();
            }
        }
        return null;
    } 
    
    @Transient
    public String getTurno(){
        if(turmaAlunos != null && !turmaAlunos.isEmpty()){
            for(TurmaAluno turma : turmaAlunos){
                return turma.getTurma().getTurno();
            }
        }
        return null;
    }
    
    @Transient
    public Date getDataInscricao(){
        if(turmaAlunos != null && !turmaAlunos.isEmpty()){
            for(TurmaAluno turma : turmaAlunos){
                return turma.getDataIncricao();
            }
        }
        return null;
    }
    
    @Transient
    public List<TurmaAluno> getTurmaAlunosOrd(){
        List<TurmaAluno> turmas = new ArrayList<>(this.turmaAlunos);
        Collections.sort( turmas, new Comparator<TurmaAluno>() {
            public int compare(TurmaAluno obj1, TurmaAluno obj2) {
                return obj1.getTurma().getIdTurma().compareTo(obj2.getTurma().getIdTurma());
            }
        });
        return turmas;
    }
    
    public boolean simuladoRealizado(Simulado simulado){
        if(alunoSimulados == null) return false;
        for(AlunoSimulado alunoSimulado: alunoSimulados){
            if(alunoSimulado.getTurmaSimulado().getSimulado().getIdSimulado().equals(simulado.getIdSimulado())){
                return true;
            }
        }
        return false;
    }

    @Column(name="desistente")
    public Boolean getDesistente() {
        return this.desistente;
    }
    
    public void setDesistente(Boolean desistente) {
        this.desistente = desistente;
    }

}


