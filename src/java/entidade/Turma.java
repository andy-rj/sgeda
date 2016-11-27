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
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Turma generated by hbm2java
 */
@Entity
@Table(name = "turma", catalog = "kemdixip_sgedanovo"
)
public class Turma implements java.io.Serializable {

    private Integer idTurma;
    private Disciplina disciplina;
    private Professor professor;
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataFim;
    private String turno;
    private Set<TurmaSimulado> turmaSimulados = new HashSet(0);
    private Set<TurmaAluno> turmaAlunos = new HashSet(0);
    private Curso curso;

    public Turma() {
    }

    public Turma(Disciplina disciplina, Professor professor) {
        this.disciplina = disciplina;
        this.professor = professor;
    }

    public Turma(Disciplina disciplina, Professor professor, String nome, String descricao, Date dataInicio, Date dataFim, String turno, Set<TurmaSimulado> turmaSimulados, Set<TurmaAluno> turmaAlunos) {
        this.disciplina = disciplina;
        this.professor = professor;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.turno = turno;
        this.turmaSimulados = turmaSimulados;
        this.turmaAlunos = turmaAlunos;
    }

    @Transient
    public BigDecimal getDesempenho() {
        BigDecimal media = new BigDecimal(BigInteger.ZERO);
        int alunosAtivos = 0;
        if (this.turmaAlunos != null) {
            for (TurmaAluno turmaAluno : turmaAlunos) {
                if (turmaAluno.getAluno().getPessoa().getAtivo()) {
                    alunosAtivos++;
                    media = media.add(turmaAluno.getAluno().getMedia(turmaSimulados));
                }
            }
        }
        if (alunosAtivos != 0) {
            media = media.divide(new BigDecimal(alunosAtivos), 2, RoundingMode.HALF_DOWN);
        }
        return media;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "idTurma", unique = true, nullable = false)
    public Integer getIdTurma() {
        return this.idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDisciplina", nullable = false)
    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProfessor", nullable = false)
    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Column(name = "nome")
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "descricao")
    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataInicio", length = 19)
    public Date getDataInicio() {
        return this.dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataFim", length = 19)
    public Date getDataFim() {
        return this.dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    @Column(name = "turno", length = 50)
    public String getTurno() {
        return this.turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
    public Set<TurmaSimulado> getTurmaSimulados() {
        return this.turmaSimulados;
    }

    public void setTurmaSimulados(Set<TurmaSimulado> turmaSimulados) {
        this.turmaSimulados = turmaSimulados;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
    public Set<TurmaAluno> getTurmaAlunos() {
        return this.turmaAlunos;
    }

    public void setTurmaAlunos(Set<TurmaAluno> turmaAlunos) {
        this.turmaAlunos = turmaAlunos;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCurso", nullable = false)
    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Transient
    public boolean isSimuladoAberto() {
        if (turmaSimulados == null) {
            return false;
        }
        for (TurmaSimulado simulado : turmaSimulados) {
            if (simulado.isSimuladoAberto()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSimuladoRealizado(TurmaSimulado simulado, Set<AlunoSimulado> simulados) {
        if (simulado == null || simulados == null) {
            return false;
        }
        for (AlunoSimulado simuladoT : simulados) {
            if (simulado.getId().equals(simuladoT.getTurmaSimulado().getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isSimuladosAbertosRealizados(Aluno aluno) {
        if (turmaSimulados == null) {
            return false;
        }

        if (!isSimuladoAberto()) {
            return false;
        }

        for (TurmaSimulado simulado : turmaSimulados) {
            if (simulado.isSimuladoAberto() && !isSimuladoRealizado(simulado, aluno.getAlunoSimulados())) {
                return false;
            }
        }

        return true;
    }

    @Transient
    public List<TurmaSimulado> getTurmaSimuladosListOrd() {
        List<TurmaSimulado> lista = new ArrayList<>(this.getTurmaSimulados());
        Collections.sort(lista, new Comparator<TurmaSimulado>() {
            public int compare(TurmaSimulado obj1, TurmaSimulado obj2) {
                return obj1.getDataAbertura().compareTo(obj2.getDataAbertura());
            }
        });
        return lista;
    }

    @Transient
    public String getProgresso() {
        Integer progresso = 0;
        Long duracao = getDataFim().getTime() - getDataInicio().getTime();
        Long atual = (new Date()).getTime() - getDataInicio().getTime();
        if (atual < 0) {
            atual = new Long(0);
        }
        if (duracao == 0) {
            duracao = new Long(1);
        }
        Long progressoAtual = ((atual * 100) / duracao);
        progresso = progressoAtual.intValue();
        if (progresso > 100) {
            progresso = 100;
        }

        return progresso.toString();
    }

}
