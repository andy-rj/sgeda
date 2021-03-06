package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Telefone generated by hbm2java
 */
@Entity
@Table(name="telefone"
    ,catalog="kemdixip_sgedanovo"
)
public class Telefone  implements java.io.Serializable {


     private Integer idTelefone;
     private Pessoa pessoa;
     private String ddd;
     private String numero;
     private String descricao;

    public Telefone() {
    }

	
    public Telefone(Pessoa pessoa, String ddd, String numero) {
        this.pessoa = pessoa;
        this.ddd = ddd;
        this.numero = numero;
    }
    public Telefone(Pessoa pessoa, String ddd, String numero, String descricao) {
       this.pessoa = pessoa;
       this.ddd = ddd;
       this.numero = numero;
       this.descricao = descricao;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idTelefone", unique=true, nullable=false)
    public Integer getIdTelefone() {
        return this.idTelefone;
    }
    
    public void setIdTelefone(Integer idTelefone) {
        this.idTelefone = idTelefone;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPessoa", nullable=false)
    public Pessoa getPessoa() {
        return this.pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    
    @Column(name="ddd", nullable=false, length=2)
    public String getDdd() {
        return this.ddd;
    }
    
    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    
    @Column(name="numero", nullable=false, length=9)
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }

    
    @Column(name="descricao", length=50)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }




}


