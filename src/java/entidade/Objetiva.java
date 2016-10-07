package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * Objetiva generated by hbm2java
 */
@Entity
@Table(name="objetiva"
    ,catalog="kemdixip_sgedanovo"
)
public class Objetiva  implements java.io.Serializable {


     private int idObjetiva;
     private Questao questao;
     private Set<Opcao> opcaos = new HashSet(0);

    public Objetiva() {
    }

	
    public Objetiva(Questao questao) {
        this.questao = questao;
    }
    public Objetiva(Questao questao, Set<Opcao> opcaos) {
       this.questao = questao;
       this.opcaos = opcaos;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="questao"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="idObjetiva", unique=true, nullable=false)
    public int getIdObjetiva() {
        return this.idObjetiva;
    }
    
    public void setIdObjetiva(int idObjetiva) {
        this.idObjetiva = idObjetiva;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Questao getQuestao() {
        return this.questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

@OneToMany(fetch=FetchType.EAGER, mappedBy="objetiva")
    public Set<Opcao> getOpcaos() {
        return this.opcaos;
    }
    
    public void setOpcaos(Set<Opcao> opcaos) {
        this.opcaos = opcaos;
    }
    
    @Transient
    public List<Opcao> getOpcoesList(){
        List<Opcao> opcoesList = new ArrayList<>(opcaos);
        Collections.sort( opcoesList, new Comparator<Opcao>() {
            public int compare(Opcao obj1, Opcao obj2) {
                return obj1.getIdOpcao().compareTo(obj2.getIdOpcao());
            }
        });
        return opcoesList ;
    }




}


