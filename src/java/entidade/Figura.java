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
 * Figura generated by hbm2java
 */
@Entity
@Table(name="figura"
    ,catalog="kemdixip_sgedanovo"
)
public class Figura  implements java.io.Serializable {


    private Integer idFigura;
    private Questao questao;
    private String nome;
    private String descricao;
    private String tamanho;
    private String tipo;
    private byte[] imagem;
    
    public Figura() {
    }

	
    public Figura(Questao questao) {
        this.questao = questao;
    }
    public Figura(Questao questao, String nome, String descricao, String tamanho, String tipo, byte[] imagem) {
       this.questao = questao;
       this.nome = nome;
       this.descricao = descricao;
       this.tamanho = tamanho;
       this.tipo = tipo;
       this.imagem = imagem;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idFigura", unique=true, nullable=false)
    public Integer getIdFigura() {
        return this.idFigura;
    }
    
    public void setIdFigura(Integer idFigura) {
        this.idFigura = idFigura;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idQuestao", nullable=false)
    public Questao getQuestao() {
        return this.questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    
    @Column(name="nome", length=25)
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

    
    @Column(name="tamanho", length=25)
    public String getTamanho() {
        return this.tamanho;
    }
    
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    
    @Column(name="tipo", length=25)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    @Column(name="imagem")
    public byte[] getImagem() {
        return this.imagem;
    }
    
    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }




}


