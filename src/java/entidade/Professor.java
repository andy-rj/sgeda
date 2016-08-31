package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Professor generated by hbm2java
 */
@Entity
@Table(name="professor"
    ,catalog="sgeda"
)
public class Professor  implements java.io.Serializable {


     private int idProfessor;
     private Pessoa pessoa;
     private String especializacao;
     private String intituicaoFormacao;
     private Set<Disciplina> disciplinas;

    public Professor() {
    }

	
    public Professor(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    public Professor(Pessoa pessoa, String especializacao, String intituicaoFormacao, Set<Disciplina> disciplinas) {
       this.pessoa = pessoa;
       this.especializacao = especializacao;
       this.intituicaoFormacao = intituicaoFormacao;
       this.disciplinas = disciplinas;

    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="pessoa"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="idProfessor", unique=true, nullable=false)
    public int getIdProfessor() {
        return this.idProfessor;
    }
    
    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Pessoa getPessoa() {
        return this.pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    
    @Column(name="especializacao", length=100)
    public String getEspecializacao() {
        return this.especializacao;
    }
    
    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    
    @Column(name="intituicaoFormacao", length=100)
    public String getIntituicaoFormacao() {
        return this.intituicaoFormacao;
    }
    
    public void setIntituicaoFormacao(String intituicaoFormacao) {
        this.intituicaoFormacao = intituicaoFormacao;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="professor_habilitado_disciplina", catalog="sgeda", joinColumns = { 
        @JoinColumn(name="professor_idProfessor", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="disciplina_idDisciplina", nullable=false, updatable=false) })
    public Set<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }
    
    public void setDisciplinas(Set disciplinas) {
        this.disciplinas = disciplinas;
    }
}


