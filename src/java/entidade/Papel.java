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
 * Papel generated by hbm2java
 */
@Entity
@Table(name="papel"
    ,catalog="sgeda"
)
public class Papel  implements java.io.Serializable {


     private Integer idPapel;
     private String nome;
     private String descricao;
     private Set usuarios = new HashSet(0);

    public Papel() {
    }

    public Papel(String nome, String descricao, Set usuarios) {
       this.nome = nome;
       this.descricao = descricao;
       this.usuarios = usuarios;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idPapel", unique=true, nullable=false)
    public Integer getIdPapel() {
        return this.idPapel;
    }
    
    public void setIdPapel(Integer idPapel) {
        this.idPapel = idPapel;
    }

    
    @Column(name="nome", length=45)
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    
    @Column(name="descricao", length=45)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="papel_has_usuario", catalog="sgeda", joinColumns = { 
        @JoinColumn(name="papel_idPapel", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="usuario_idUsuario", nullable=false, updatable=false) })
    public Set getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }




}


