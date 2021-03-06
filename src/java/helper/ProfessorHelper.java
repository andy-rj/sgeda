package helper;

import entidade.AlunoSimulado;
import entidade.Disciplina;
import entidade.Endereco;
import entidade.Funcionario;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Resposta;
import entidade.Telefone;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.Usuario;
import hibernate.HibernateUtil;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ProfessorHelper {

    Session session = null;

    public boolean cadastrar(Pessoa pessoa, Endereco endereco, Professor professor, Usuario usuario) {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            usuario.setSenha(LoginHelper.md5(usuario.getSenha()));
        } catch (NoSuchAlgorithmException e) {

        }

        try {
            tx.begin();
            if (pessoa.getFoto() != null) {
                session.save(pessoa.getFoto());
            }
            pessoa.setAtivo(Boolean.TRUE);
            session.save(pessoa);
            Integer idPessoa = pessoa.getIdPessoa();
            String matricula = Integer.toString(idPessoa);
            while (matricula.length() < 5) {
                matricula = "0" + matricula;
            }
            int ano = Calendar.getInstance().get(Calendar.YEAR);
            int mes = Calendar.getInstance().get(Calendar.MONTH);
            matricula = "P" + ano + "." + ((mes / 6) + 1) + "-" + matricula;
            pessoa.setMatricula(matricula);
            usuario.setLogin(matricula);
            session.update(pessoa);
            session.save(endereco);
            for (Telefone telefone : pessoa.getTelefones()) {
                session.save(telefone);
            }
            session.save(professor);
            session.save(usuario);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    public boolean alterarAtividade(Professor professor) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.update(professor.getPessoa());
            session.update(professor.getPessoa().getUsuario());
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    public boolean salvarAlteracaoProfessor(Professor professor, Set<Telefone> telefonesExcluir) {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.update(professor.getPessoa());
            session.update(professor.getPessoa().getEnderecos());
            for (Telefone telefone : telefonesExcluir) {
                session.delete(telefone);
            }
            for (Telefone telefone : professor.getPessoa().getTelefones()) {
                if (telefone.getPessoa() == null) {
                    telefone.setPessoa(professor.getPessoa());
                }
                session.save(telefone);
            }
            session.update(professor);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    public Pessoa getByCpf(String cpf) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Pessoa professor = (Pessoa) session.createCriteria(Pessoa.class).add(Restrictions.eq("cpf", cpf)).uniqueResult();
            session.flush();
            tx.commit();
            return professor;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<Professor> getProfessorByDisciplina(Integer disciplinaSelecionada) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            List<Professor> professores = session.createCriteria(Professor.class).createAlias("disciplinas", "disciplina").add(Restrictions.eq("disciplina.idDisciplina", disciplinaSelecionada)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            return professores;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<Professor> getProfessores(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Professor.class);

            crit.createAlias("pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Professor> retorno = crit.list();

            for (Professor professor : retorno) {
                professor.getPessoa().getTelefones().size();
                professor.getDisciplinas().size();
            }
            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
    
    public List<Professor> getProfessoresComTurma(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Professor.class);

            crit.createAlias("pessoa", "pessoa");
            crit.createAlias("turmas", "turmas");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Professor> retorno = crit.list();

            for (Professor professor : retorno) {
                professor.getPessoa().getTelefones().size();
                professor.getDisciplinas().size();
            }
            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<Professor> getProfessoresByIdRelatorio(List<String> idsProfessor, Date dataInicio, Date dataFim) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Professor.class);

            crit.createAlias("turmas", "turmas");

            if (dataInicio != null) {
                crit.add(Restrictions.ge("turmas.dataFim", dataInicio));
            }

            if (dataFim != null) {
                crit.add(Restrictions.le("turmas.dataFim", dataFim));
            }

            List<Integer> ids = new ArrayList<>();

            if (idsProfessor != null && !idsProfessor.isEmpty()) {
                for (String str : idsProfessor) {
                    ids.add(Integer.parseInt(str));
                }
                crit.add(Restrictions.in("idProfessor", ids));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            crit.addOrder(Order.asc("turmas.dataFim"));
            List<Professor> retorno = crit.list();

            for (Professor prof : retorno) {
                prof.getTurmas().size();
                if (prof.getTurmas() != null) {
                    for (Turma turma : prof.getTurmas()) {
                        turma.getTurmaAlunos().size();
                        turma.getTurmaSimulados().size();
                        if(turma.getTurmaAlunos()!=null){
                            for(TurmaAluno turmaAluno: turma.getTurmaAlunos()){
                                turmaAluno.getAluno().getAlunoSimulados().size();
                                if(turmaAluno.getAluno().getAlunoSimulados()!=null){
                                    for(AlunoSimulado alunoSimulado: turmaAluno.getAluno().getAlunoSimulados()){
                                        alunoSimulado.getRespostas().size();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<Professor> getProfessoresEager(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Professor.class);

            crit.createAlias("pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Professor> retorno = crit.list();

            for (Professor professor : retorno) {
                professor.getPessoa().getTelefones().size();
                professor.getDisciplinas().size();
                professor.getTurmas().size();
            }
            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public List<Professor> getProfessores(List<String> stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Professor.class);

            crit.createAlias("pessoa", "pessoa");
            List<Boolean> list = new ArrayList<>();
            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                for (String str : stringConsulta) {
                    if (str.equals("1")) {
                        list.add(Boolean.TRUE);
                    } else if (str.equals("0")) {
                        list.add(Boolean.FALSE);
                    }
                }
                crit.add(Restrictions.in("pessoa.ativo", list));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Professor> retorno = crit.list();

            for (Professor professor : retorno) {
                professor.getPessoa().getTelefones().size();
                professor.getDisciplinas().size();
            }
            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public Professor getById(Integer Id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Professor professore = (Professor) session.createCriteria(Professor.class).add(Restrictions.eq("idProfessor", Id)).uniqueResult();
            session.flush();
            tx.commit();
            return professore;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public Professor getByIdEagerDisciplinas(Integer Id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Professor professor = (Professor) session.createCriteria(Professor.class).add(Restrictions.eq("idProfessor", Id)).uniqueResult();
            if (professor != null) {
                professor.getDisciplinas().size();
            }
            session.flush();
            tx.commit();
            return professor;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public Pessoa getPessoaById(Integer Id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Pessoa pessoa = (Pessoa) session.createCriteria(Pessoa.class).add(Restrictions.eq("idPessoa", Id)).uniqueResult();
            session.flush();
            tx.commit();
            return pessoa;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public boolean salvarNotasSimulado(AlunoSimulado alunoSimuladoCorrigir) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            for (Resposta resposta : alunoSimuladoCorrigir.getRespostas()) {
                session.update(resposta);
            }
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

}
