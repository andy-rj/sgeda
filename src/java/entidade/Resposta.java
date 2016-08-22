package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Resposta generated by hbm2java
 */
@Entity
@Table(name="resposta"
    ,catalog="sgeda"
)
public class Resposta  implements java.io.Serializable {


     private Integer idResposta;
     private AlunoHasSimulado alunoHasSimulado;
     private Questao questao;
     private BigDecimal nota;
     private String resposta;

    public Resposta() {
    }

	
    public Resposta(AlunoHasSimulado alunoHasSimulado, Questao questao) {
        this.alunoHasSimulado = alunoHasSimulado;
        this.questao = questao;
    }
    public Resposta(AlunoHasSimulado alunoHasSimulado, Questao questao, BigDecimal nota, String resposta) {
       this.alunoHasSimulado = alunoHasSimulado;
       this.questao = questao;
       this.nota = nota;
       this.resposta = resposta;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idResposta", unique=true, nullable=false)
    public Integer getIdResposta() {
        return this.idResposta;
    }
    
    public void setIdResposta(Integer idResposta) {
        this.idResposta = idResposta;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns( { 
        @JoinColumn(name="idAluno", referencedColumnName="aluno_idAluno", nullable=false), 
        @JoinColumn(name="idSimulado", referencedColumnName="simulado_idSimulado", nullable=false) } )
    public AlunoHasSimulado getAlunoHasSimulado() {
        return this.alunoHasSimulado;
    }
    
    public void setAlunoHasSimulado(AlunoHasSimulado alunoHasSimulado) {
        this.alunoHasSimulado = alunoHasSimulado;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idQuestao", nullable=false)
    public Questao getQuestao() {
        return this.questao;
    }
    
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    
    @Column(name="nota", precision=5)
    public BigDecimal getNota() {
        return this.nota;
    }
    
    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    
    @Column(name="resposta", length=3000)
    public String getResposta() {
        return this.resposta;
    }
    
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }




}


