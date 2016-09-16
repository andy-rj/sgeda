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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Subdisciplina generated by hbm2java
 */
@Entity
@Table(name="subdisciplina"
    ,catalog="sgeda"
)
public class Subdisciplina  implements java.io.Serializable {


     private Integer idSubdisciplina;
     private Disciplina disciplina;
     private String nome;
     private String descricao;
     private Set<Questao> questaos = new HashSet(0);

    public Subdisciplina() {
    }

	
    public Subdisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    public Subdisciplina(Disciplina disciplina, String nome, String descricao, Set<Questao> questaos) {
       this.disciplina = disciplina;
       this.nome = nome;
       this.descricao = descricao;
       this.questaos = questaos;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idSubdisciplina", unique=true, nullable=false)
    public Integer getIdSubdisciplina() {
        return this.idSubdisciplina;
    }
    
    public void setIdSubdisciplina(Integer idSubdisciplina) {
        this.idSubdisciplina = idSubdisciplina;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idDisciplina", nullable=false)
    public Disciplina getDisciplina() {
        return this.disciplina;
    }
    
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    
    @Column(name="nome", length=45)
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
    @JoinTable(name="questao_subdisciplina", catalog="sgeda", joinColumns = { 
        @JoinColumn(name="subdisciplina_idSubdisciplina", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="questao_idQuestao", nullable=false, updatable=false) })
    public Set<Questao> getQuestaos() {
        return this.questaos;
    }
    
    public void setQuestaos(Set<Questao> questaos) {
        this.questaos = questaos;
    }




}


