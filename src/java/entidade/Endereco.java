package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Endereco generated by hbm2java
 */
@Entity
@Table(name="endereco"
    ,catalog="sgeda"
)
public class Endereco  implements java.io.Serializable {


     private EnderecoId id;
     private Pessoa pessoa;
     private String logradouro;
     private String numero;
     private String complemento;
     private String cep;
     private String bairro;
     private String cidade;
     private String estado;

    public Endereco() {
    }

	
    public Endereco(EnderecoId id, Pessoa pessoa, String cep) {
        this.id = id;
        this.pessoa = pessoa;
        this.cep = cep;
    }
    public Endereco(EnderecoId id, Pessoa pessoa, String logradouro, String numero, String complemento, String cep, String bairro, String cidade, String estado) {
       this.id = id;
       this.pessoa = pessoa;
       this.logradouro = logradouro;
       this.numero = numero;
       this.complemento = complemento;
       this.cep = cep;
       this.bairro = bairro;
       this.cidade = cidade;
       this.estado = estado;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idEndereco", column=@Column(name="idEndereco", nullable=false) ), 
        @AttributeOverride(name="idPessoa", column=@Column(name="idPessoa", nullable=false) ) } )
    public EnderecoId getId() {
        return this.id;
    }
    
    public void setId(EnderecoId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idPessoa", nullable=false, insertable=false, updatable=false)
    public Pessoa getPessoa() {
        return this.pessoa;
    }
    
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    
    @Column(name="logradouro")
    public String getLogradouro() {
        return this.logradouro;
    }
    
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    
    @Column(name="numero", length=50)
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }

    
    @Column(name="complemento")
    public String getComplemento() {
        return this.complemento;
    }
    
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    
    @Column(name="cep", nullable=false, length=8)
    public String getCep() {
        return this.cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }

    
    @Column(name="bairro", length=50)
    public String getBairro() {
        return this.bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    
    @Column(name="cidade", length=50)
    public String getCidade() {
        return this.cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    
    @Column(name="estado", length=2)
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }




}


