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

/**
 * Disciplina generated by hbm2java
 */
@Entity
@Table(name="disciplina"
    ,catalog="kemdixip_sgedanovo"
)
public class Disciplina  implements java.io.Serializable {


     private Integer idDisciplina;
     private String codigo;
     private String nome;
     private String descricao;
     private Set<Subdisciplina> subdisciplinas = new HashSet(0);
     private Set<Curso> cursos = new HashSet(0);
     private Set<Turma> turmas = new HashSet(0);
     private Set<Professor> professors = new HashSet(0);
     private Set<Questao> questaos = new HashSet(0);

    public Disciplina() {
    }

    public Disciplina(String nome, String descricao, Set<Subdisciplina> subdisciplinas, Set<Curso> cursos, Set<Turma> turmas, Set<Professor> professors, Set<Questao> questaos) {
       this.nome = nome;
       this.descricao = descricao;
       this.subdisciplinas = subdisciplinas;
       this.cursos = cursos;
       this.turmas = turmas;
       this.professors = professors;
       this.questaos = questaos;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idDisciplina", unique=true, nullable=false)
    public Integer getIdDisciplina() {
        return this.idDisciplina;
    }
    
    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="disciplina")
    public Set<Subdisciplina> getSubdisciplinas() {
        return this.subdisciplinas;
    }
    
    public void setSubdisciplinas(Set<Subdisciplina> subdisciplinas) {
        this.subdisciplinas = subdisciplinas;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="curso_disciplina", catalog="kemdixip_sgedanovo", joinColumns = { 
        @JoinColumn(name="disciplina_idDisciplina", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="curso_idCurso", nullable=false, updatable=false) })
    public Set<Curso> getCursos() {
        return this.cursos;
    }
    
    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="disciplina")
    public Set<Turma> getTurmas() {
        return this.turmas;
    }
    
    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="professor_habilitado_disciplina", catalog="kemdixip_sgedanovo", joinColumns = { 
        @JoinColumn(name="disciplina_idDisciplina", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="professor_idProfessor", nullable=false, updatable=false) })
    public Set<Professor> getProfessors() {
        return this.professors;
    }
    
    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }
    
@OneToMany(fetch=FetchType.LAZY, mappedBy="disciplina")
    public Set<Questao> getQuestaos() {
        return this.questaos;
    }
    
    public void setQuestaos(Set<Questao> questaos) {
        this.questaos = questaos;
    }




}


