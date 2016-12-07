package entidade;
// Generated 09/09/2016 09:36:48 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
 * Aluno generated by hbm2java
 */
@Entity
@Table(name = "aluno", catalog = "kemdixip_sgedanovo"
)
public class Aluno implements java.io.Serializable {

    private int idAluno;
    private Pessoa pessoa;
    private String nomeResponsavel;
    private String escolaridade;
    private String cpfResponsavel;
    private String telefoneResponsavel;
    private Set<TurmaAluno> turmaAlunos = new HashSet(0);
    private Set<AlunoSimulado> alunoSimulados = new HashSet(0);
    private Boolean desistente;
    private String motivoDesistencia;
    private Integer aprovado;

    @Column(name = "aprovado")
    public Integer getAprovado() {
        return aprovado;
    }

    public void setAprovado(Integer aprovado) {
        this.aprovado = aprovado;
    }

    BigDecimal getMedia(Set<TurmaSimulado> turmaSimulados) {
        BigDecimal media = new BigDecimal(BigInteger.ZERO);
        int quantidadeSimulados = 0;
        for (TurmaSimulado turmaSimulado : turmaSimulados) {
            BigDecimal nota = this.notaSimulado(turmaSimulado);
            if(nota!=null){
                quantidadeSimulados++;
                media = media.add(nota);
            }
        }
        if (quantidadeSimulados != 0) {
            media = media.divide(new BigDecimal(quantidadeSimulados), 2, RoundingMode.HALF_DOWN);
        }
        return media;
    }

    @Column(name = "posicao")
    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    @Column(name = "nota")
    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Column(name = "descricaoAprovado")
    public String getDescricaoAprovado() {
        return descricaoAprovado;
    }

    public void setDescricaoAprovado(String descricaoAprovado) {
        this.descricaoAprovado = descricaoAprovado;
    }
    private Integer posicao;
    private String nota;
    private String descricaoAprovado;

    public Aluno() {
    }

    public Aluno(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Aluno(Pessoa pessoa, String nomeResponsavel, String escolaridade, String cpfResponsavel, String telefoneResponsavel, Set<TurmaAluno> turmaAlunos, Set<AlunoSimulado> alunoSimulados) {
        this.pessoa = pessoa;
        this.nomeResponsavel = nomeResponsavel;
        this.escolaridade = escolaridade;
        this.cpfResponsavel = cpfResponsavel;
        this.telefoneResponsavel = telefoneResponsavel;
        this.turmaAlunos = turmaAlunos;
        this.alunoSimulados = alunoSimulados;
    }

    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "pessoa"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "idAluno", unique = true, nullable = false)
    public int getIdAluno() {
        return this.idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Column(name = "nomeResponsavel")
    public String getNomeResponsavel() {
        return this.nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    @Column(name = "motivoDesistencia")
    public String getMotivoDesistencia() {
        return this.motivoDesistencia;
    }

    public void setMotivoDesistencia(String motivo) {
        this.motivoDesistencia = motivo;
    }

    @Column(name = "escolaridade", length = 50)
    public String getEscolaridade() {
        return this.escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    @Column(name = "cpfResponsavel", length = 11)
    public String getCpfResponsavel() {
        return this.cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    @Column(name = "telefoneResponsavel", length = 11)
    public String getTelefoneResponsavel() {
        return this.telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno")
    public Set<TurmaAluno> getTurmaAlunos() {
        return this.turmaAlunos;
    }

    public void setTurmaAlunos(Set<TurmaAluno> turmaAlunos) {
        this.turmaAlunos = turmaAlunos;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aluno")
    public Set<AlunoSimulado> getAlunoSimulados() {
        return this.alunoSimulados;
    }

    public void setAlunoSimulados(Set<AlunoSimulado> alunoSimulados) {
        this.alunoSimulados = alunoSimulados;
    }

    @Transient
    public Curso getCurso() {
        if (turmaAlunos != null && !turmaAlunos.isEmpty()) {
            for (TurmaAluno turma : turmaAlunos) {
                return turma.getTurma().getCurso();
            }
        }
        return null;
    }

    @Transient
    public Date getDataInicio() {
        if (turmaAlunos != null && !turmaAlunos.isEmpty()) {
            for (TurmaAluno turma : turmaAlunos) {
                return turma.getTurma().getDataInicio();
            }
        }
        return null;
    }

    @Transient
    public Date getDataFim() {
        if (turmaAlunos != null && !turmaAlunos.isEmpty()) {
            for (TurmaAluno turma : turmaAlunos) {
                return turma.getTurma().getDataFim();
            }
        }
        return null;
    }

    @Transient
    public String getTurno() {
        if (turmaAlunos != null && !turmaAlunos.isEmpty()) {
            for (TurmaAluno turma : turmaAlunos) {
                return turma.getTurma().getTurno();
            }
        }
        return null;
    }

    @Transient
    public Date getDataInscricao() {
        if (turmaAlunos != null && !turmaAlunos.isEmpty()) {
            for (TurmaAluno turma : turmaAlunos) {
                return turma.getDataIncricao();
            }
        }
        return null;
    }

    @Transient
    public List<TurmaAluno> getTurmaAlunosOrd() {
        List<TurmaAluno> turmas = new ArrayList<>(this.turmaAlunos);
        Collections.sort(turmas, new Comparator<TurmaAluno>() {
            public int compare(TurmaAluno obj1, TurmaAluno obj2) {
                return obj1.getTurma().getIdTurma().compareTo(obj2.getTurma().getIdTurma());
            }
        });
        return turmas;
    }

    public boolean simuladoRealizado(TurmaSimulado simulado) {
        if (alunoSimulados == null) {
            return false;
        }
        for (AlunoSimulado alunoSimulado : alunoSimulados) {
            if (alunoSimulado.getTurmaSimulado().getId().equals(simulado.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean simuladoCorrigido(TurmaSimulado simulado) {
        if (alunoSimulados == null) {
            return false;
        }
        for (AlunoSimulado alunoSimulado : alunoSimulados) {
            if (alunoSimulado.getTurmaSimulado().getId().equals(simulado.getId())) {
                if (alunoSimulado.isCorrigido()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean existeSimuladoParaCorrecao() {
        if (alunoSimulados == null) {
            return false;
        }
        for (AlunoSimulado alunoSimulado : alunoSimulados) {
            if (!alunoSimulado.isCorrigido()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean existeSimuladoParaCorrecao(Integer turma) {
        if (alunoSimulados == null) {
            return false;
        }
        for (AlunoSimulado alunoSimulado : alunoSimulados) {
            if(alunoSimulado.getTurmaSimulado().getTurma().getIdTurma().equals(turma)){
                if (!alunoSimulado.isCorrigido()) {
                    return true;
                }
            }
        }
        return false;
    }

    public BigDecimal notaSimulado(TurmaSimulado simulado) {
        if (alunoSimulados == null && !simulado.isSimuladoAberto()) {
            return BigDecimal.ZERO;
        } else if (alunoSimulados == null) {
            return null;
        }
        for (AlunoSimulado alunoSimulado : alunoSimulados) {
            if (alunoSimulado.getTurmaSimulado().getId().equals(simulado.getId())) {
                return alunoSimulado.getNota();
            }
        }
        if (simulado.isSimuladoAberto()) {
            return null;
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Column(name = "desistente")
    public Boolean getDesistente() {
        return this.desistente;
    }

    public void setDesistente(Boolean desistente) {
        this.desistente = desistente;
    }
    
    public BigDecimal media(Turma turma){
        int i = 0;
        BigDecimal media = new BigDecimal(BigInteger.ZERO);
        for(TurmaSimulado simulado:turma.getTurmaSimulados()){
            if(!simulado.isSimuladoAberto()){
                media = media.add(notaSimulado(simulado));
                i++;
            }
        }
        if(i!=0){
            media = media.divide(new BigDecimal(i),1,RoundingMode.CEILING);
        }
        return media;
    }

}
