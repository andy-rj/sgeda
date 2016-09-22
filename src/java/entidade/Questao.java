package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1


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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Questao generated by hbm2java
 */
@Entity
@Table(name="questao"
    ,catalog="kemdixip_sgeda"
)
public class Questao  implements java.io.Serializable {


     private Integer idQuestao;
     private Grupo grupo;
     private Professor professor;
     private Disciplina disciplina;
     private String enunciado;
     private String ano;
     private String instituicao;
     private Discursiva discursiva;
     private Set<SimuladoQuestao> simuladoQuestaos = new HashSet(0);
     private Set<Figura> figuras = new HashSet(0);
     private Redacao redacao;
     private Objetiva objetiva;
     private Set<Subdisciplina> subdisciplinas = new HashSet(0);
     private Set<Resposta> respostas = new HashSet(0);

    public Questao() {
    }

	
    public Questao(Professor professor, Disciplina disciplina) {
        this.professor = professor;
        this.disciplina = disciplina;
    }
    public Questao(Grupo grupo, Professor professor, Disciplina disciplina, String enunciado, String ano, String instituicao, Discursiva discursiva, Set<SimuladoQuestao> simuladoQuestaos, Set<Figura> figuras, Redacao redacao, Objetiva objetiva, Set<Subdisciplina> subdisciplinas, Set<Resposta> respostas) {
       this.grupo = grupo;
       this.professor = professor;
       this.disciplina = disciplina;
       this.enunciado = enunciado;
       this.ano = ano;
       this.instituicao = instituicao;
       this.discursiva = discursiva;
       this.simuladoQuestaos = simuladoQuestaos;
       this.figuras = figuras;
       this.redacao = redacao;
       this.objetiva = objetiva;
       this.subdisciplinas = subdisciplinas;
       this.respostas = respostas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idQuestao", unique=true, nullable=false)
    public Integer getIdQuestao() {
        return this.idQuestao;
    }
    
    public void setIdQuestao(Integer idQuestao) {
        this.idQuestao = idQuestao;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idQuestaoGrupo")
    public Grupo getGrupo() {
        return this.grupo;
    }
    
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idProfessor", nullable=false)
    public Professor getProfessor() {
        return this.professor;
    }
    
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idDisciplina", nullable=false)
    public Disciplina getDisciplina() {
        return this.disciplina;
    }
    
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    
    @Column(name="enunciado", length=1500)
    public String getEnunciado() {
        return this.enunciado;
    }
    
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    
    @Column(name="ano", length=4)
    public String getAno() {
        return this.ano;
    }
    
    public void setAno(String ano) {
        this.ano = ano;
    }

    
    @Column(name="instituicao", length=10)
    public String getInstituicao() {
        return this.instituicao;
    }
    
    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

@OneToOne(fetch=FetchType.LAZY, mappedBy="questao")
    public Discursiva getDiscursiva() {
        return this.discursiva;
    }
    
    public void setDiscursiva(Discursiva discursiva) {
        this.discursiva = discursiva;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="questao")
    public Set<SimuladoQuestao> getSimuladoQuestaos() {
        return this.simuladoQuestaos;
    }
    
    public void setSimuladoQuestaos(Set<SimuladoQuestao> simuladoQuestaos) {
        this.simuladoQuestaos = simuladoQuestaos;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="questao")
    public Set<Figura> getFiguras() {
        return this.figuras;
    }
    
    public void setFiguras(Set<Figura> figuras) {
        this.figuras = figuras;
    }

@OneToOne(fetch=FetchType.LAZY, mappedBy="questao")
    public Redacao getRedacao() {
        return this.redacao;
    }
    
    public void setRedacao(Redacao redacao) {
        this.redacao = redacao;
    }

@OneToOne(fetch=FetchType.LAZY, mappedBy="questao")
    public Objetiva getObjetiva() {
        return this.objetiva;
    }
    
    public void setObjetiva(Objetiva objetiva) {
        this.objetiva = objetiva;
    }

@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="questao_subdisciplina", catalog="kemdixip_sgeda", joinColumns = { 
        @JoinColumn(name="questao_idQuestao", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="subdisciplina_idSubdisciplina", nullable=false, updatable=false) })
    public Set<Subdisciplina> getSubdisciplinas() {
        return this.subdisciplinas;
    }
    
    public void setSubdisciplinas(Set<Subdisciplina> subdisciplinas) {
        this.subdisciplinas = subdisciplinas;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="questao")
    public Set<Resposta> getRespostas() {
        return this.respostas;
    }
    
    public void setRespostas(Set<Resposta> respostas) {
        this.respostas = respostas;
    }




}


