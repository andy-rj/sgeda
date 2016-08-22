package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SimuladoHasQuestaoId generated by hbm2java
 */
@Embeddable
public class SimuladoHasQuestaoId  implements java.io.Serializable {


     private int simuladoIdSimulado;
     private int questaoIdQuestao;

    public SimuladoHasQuestaoId() {
    }

    public SimuladoHasQuestaoId(int simuladoIdSimulado, int questaoIdQuestao) {
       this.simuladoIdSimulado = simuladoIdSimulado;
       this.questaoIdQuestao = questaoIdQuestao;
    }
   


    @Column(name="simulado_idSimulado", nullable=false)
    public int getSimuladoIdSimulado() {
        return this.simuladoIdSimulado;
    }
    
    public void setSimuladoIdSimulado(int simuladoIdSimulado) {
        this.simuladoIdSimulado = simuladoIdSimulado;
    }


    @Column(name="questao_idQuestao", nullable=false)
    public int getQuestaoIdQuestao() {
        return this.questaoIdQuestao;
    }
    
    public void setQuestaoIdQuestao(int questaoIdQuestao) {
        this.questaoIdQuestao = questaoIdQuestao;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SimuladoHasQuestaoId) ) return false;
		 SimuladoHasQuestaoId castOther = ( SimuladoHasQuestaoId ) other; 
         
		 return (this.getSimuladoIdSimulado()==castOther.getSimuladoIdSimulado())
 && (this.getQuestaoIdQuestao()==castOther.getQuestaoIdQuestao());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getSimuladoIdSimulado();
         result = 37 * result + this.getQuestaoIdQuestao();
         return result;
   }   


}

