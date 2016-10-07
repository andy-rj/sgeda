package helper;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Telefone;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.TurmaSimulado;
import hibernate.HibernateUtil;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class TurmaHelper {

    Session session = null;

    public boolean cadastrar(List<Turma> turmas) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            for (Turma turma : turmas) {
                session.save(turma);
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

    public boolean cadastrarSimulado(TurmaSimulado turmaSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.save(turmaSimulado);
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
        return true;    }

    public List<Turma> getTurmas(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);

            crit.createAlias("disciplina", "disciplina");
            crit.createAlias("curso", "curso");
            crit.createAlias("professor", "professor");
            crit.createAlias("professor.pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("curso.nome", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("disciplina.nome", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("turno", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Turma> retorno = crit.list();

            //for (Turma turma : retorno) {
            //}
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

    public Set<TurmaAluno> getTurmasAlunoByIdAluno(int idAluno) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(TurmaAluno.class);

            crit.createAlias("aluno", "aluno");

            if (idAluno != 0) {
                crit.add(Restrictions.conjunction()
                        .add(Restrictions.eq("aluno.idAluno", idAluno))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            Set<TurmaAluno> retorno = new HashSet<>(crit.list());
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

    public List<Turma> getTurmasByIdProfessor(Integer idProfessor) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);
            crit.createAlias("professor", "professor");

            crit.add(Restrictions.conjunction()
                    .add(Restrictions.eq("professor.idProfessor", idProfessor))
            );

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Turma> retorno = crit.list();
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

    public List<Turma> getTurmasByCurso(Integer cursoSelecionado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);

            crit.createAlias("curso", "curso");

            if (cursoSelecionado != null) {
                crit.add(Restrictions.conjunction()
                        .add(Restrictions.eq("curso.idCurso", cursoSelecionado))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Turma> retorno = crit.list();

            //for (Turma turma : retorno) {
            //}
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

    public List<Turma> getTurmasByCurso(Integer cursoSelecionado, Date dataInicio, String turno) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);

            crit.createAlias("curso", "curso");

            if (cursoSelecionado != null && dataInicio != null) {
                crit.add(Restrictions.conjunction()
                        .add(Restrictions.eq("curso.idCurso", cursoSelecionado))
                        .add(Restrictions.eq("dataInicio", dataInicio))
                        .add(Restrictions.eq("turno", turno))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Turma> retorno = crit.list();

            //for (Turma turma : retorno) {
            //}
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

}
