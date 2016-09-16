package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.HashSet;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Grupo generated by hbm2java
 */
@Entity
@Table(name="grupo"
    ,catalog="sgeda"
)
public class Grupo  implements java.io.Serializable {


     private int idGrupo;
     private Questao questao;
     private String enunciado;
     private Set<Questao> questaos = new HashSet(0);

    public Grupo() {
    }

	
    public Grupo(Questao questao) {
        this.questao = questao;
    }
    public Grupo(Questao questao, String enunciado, Set<Questao> questaos) {
       this.questao = questao;
       this.enunciado = enunciado;
       this.questaos = questaos;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="questao"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="idGrupo", unique=true, nullable=false)
    public int getIdGrupo() {
        return this.idGrupo;
    }
    
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Questao getQuestao() {
        return this.questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    
    @Column(name="enunciado", length=3000)
    public String getEnunciado() {
        return this.enunciado;
    }
    
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="grupo")
    public Set<Questao> getQuestaos() {
        return this.questaos;
    }
    
    public void setQuestaos(Set<Questao> questaos) {
        this.questaos = questaos;
    }




}

