package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


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
import javax.persistence.Table;

/**
 * Curso generated by hbm2java
 */
@Entity
@Table(name="curso"
    ,catalog="sgeda"
)
public class Curso  implements java.io.Serializable {


     private Integer idCurso;
     private String nome;
     private String descricao;
     private Set<Disciplina> disciplinas = new HashSet(0);
     private Set<AlunoHasTurma> alunoHasTurmas = new HashSet(0);

    public Curso() {
    }

    public Curso(String nome, String descricao, Set disciplinas, Set alunoHasTurmas) {
       this.nome = nome;
       this.descricao = descricao;
       this.disciplinas = disciplinas;
       this.alunoHasTurmas = alunoHasTurmas;
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

    
    @Column(name="descricao")
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="curso_has_disciplina", catalog="sgeda", joinColumns = { 
        @JoinColumn(name="curso_idCurso", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="disciplina_idDisciplina", nullable=false, updatable=false) })
    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }
    
    public void setDisciplinas(Set disciplinas) {
        this.disciplinas = disciplinas;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="aluno_has_turma_has_curso", catalog="sgeda", joinColumns = { 
        @JoinColumn(name="curso_idCurso", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="aluno_has_turma_aluno_idAluno", nullable=false, updatable=false), 
        @JoinColumn(name="aluno_has_turma_aluno_idTurma", nullable=false, updatable=false),
        @JoinColumn(name="aluno_has_turma_aluno_idDisciplina", nullable=false, updatable=false)})
    public Set<AlunoHasTurma> getAlunoHasTurmas() {
        return this.alunoHasTurmas;
    }
    
    public void setAlunoHasTurmas(Set alunoHasTurmas) {
        this.alunoHasTurmas = alunoHasTurmas;
    }




}


