package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Funcionario generated by hbm2java
 */
@Entity
@Table(name="funcionario"
    ,catalog="sgeda"
)
public class Funcionario  implements java.io.Serializable {


     private int idFuncionario;
     private Pessoa pessoa;
     private String cargo;

    public Funcionario() {
    }

	
    public Funcionario(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    public Funcionario(Pessoa pessoa, String cargo) {
       this.pessoa = pessoa;
       this.cargo = cargo;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="pessoa"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="idFuncionario", unique=true, nullable=false)
    public int getIdFuncionario() {
        return this.idFuncionario;
    }
    
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Pessoa getPessoa() {
        return this.pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    
    @Column(name="cargo", length=100)
    public String getCargo() {
        return this.cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }




}


