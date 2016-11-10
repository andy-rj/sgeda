package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Curso generated by hbm2java
 */
@Entity
@Table(name="curso"
    ,catalog="kemdixip_sgedanovo"
)
public class Curso  implements java.io.Serializable {


     private Integer idCurso;
     private String codigo;
     private String nome;
     private String descricao;
     private Set<Disciplina> disciplinas = new HashSet(0);
     private Set<Turma> turmas = new HashSet(0);
     
    public Curso() {
    }

    public Curso(String nome, String descricao, Set<Disciplina> disciplinas) {
       this.nome = nome;
       this.descricao = descricao;
       this.disciplinas = disciplinas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idCurso", unique=true, nullable=false)
    public Integer getIdCurso() {
        return this.idCurso;
    }
    
    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    
    @Column(name="nome")
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Column(name="codigo")
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    @Column(name="descricao")
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "curso")
    public Set<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="curso_disciplina", catalog="kemdixip_sgedanovo", joinColumns = { 
        @JoinColumn(name="curso_idCurso", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="disciplina_idDisciplina", nullable=false, updatable=false) })
    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }
    
    public void setDisciplinas(Set<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    @Transient
    public Set<TurmaAluno> getTurmaAlunos(){
        Set<TurmaAluno> setAlunos = new HashSet<>();
        for(Turma turma:this.getTurmas()){
            setAlunos.addAll(turma.getTurmaAlunos());
        }
        return setAlunos;
    }
    
    @Transient
    public Set<Aluno> getAlunosDesistentes(){
        Set<Aluno> setAlunos = new HashSet<>();
        for(TurmaAluno turmaAluno:this.getTurmaAlunos()){
            if(turmaAluno.getAluno().getDesistente()){
                setAlunos.add(turmaAluno.getAluno());
            }
        }
        return setAlunos;
    }
    
    @Transient
    public Set<Aluno> getAlunosAtivos(){
        Set<Aluno> setAlunos = new HashSet<>();
        for(TurmaAluno turmaAluno:this.getTurmaAlunos()){
            if(!turmaAluno.getAluno().getDesistente()){
                setAlunos.add(turmaAluno.getAluno());
            }
        }
        return setAlunos;
    }

}


