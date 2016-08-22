package entidade;
// Generated 22/08/2016 17:01:00 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TelefoneId generated by hbm2java
 */
@Embeddable
public class TelefoneId  implements java.io.Serializable {


     private int idTelefone;
     private int idPessoa;

    public TelefoneId() {
    }

    public TelefoneId(int idTelefone, int idPessoa) {
       this.idTelefone = idTelefone;
       this.idPessoa = idPessoa;
    }
   


    @Column(name="idTelefone", nullable=false)
    public int getIdTelefone() {
        return this.idTelefone;
    }
    
    public void setIdTelefone(int idTelefone) {
        this.idTelefone = idTelefone;
    }


    @Column(name="idPessoa", nullable=false)
    public int getIdPessoa() {
        return this.idPessoa;
    }
    
    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TelefoneId) ) return false;
		 TelefoneId castOther = ( TelefoneId ) other; 
         
		 return (this.getIdTelefone()==castOther.getIdTelefone())
 && (this.getIdPessoa()==castOther.getIdPessoa());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdTelefone();
         result = 37 * result + this.getIdPessoa();
         return result;
   }   


}


