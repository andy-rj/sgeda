package helper;

import entidade.AlunoSimulado;
import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Telefone;
import entidade.Turma;
import entidade.TurmaAluno;
import entidade.TurmaSimulado;
import hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
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

    public Turma getTurmaEager(Integer idTurma) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);

            crit.add(Restrictions.eq("idTurma", idTurma));

            Turma retorno = (Turma) crit.uniqueResult();

            if (retorno != null) {
                retorno.getTurmaAlunos().size();
                retorno.getTurmaSimulados().size();
                retorno.getDisciplina().getProfessors().size();
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

    public boolean salvarAlteracaoTurma(Turma turma) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.update(turma);
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
        return true;
    }

    public boolean atualizarSimulado(TurmaSimulado turmaSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.update(turmaSimulado);
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
                        .add(Restrictions.ilike("turma", "%" + stringConsulta + "%"))
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
    
    public List<Turma> getTurmas(List<String> cursosSelecionados) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Turma.class);

            crit.createAlias("curso", "curso");
            
            List<Integer> ids = new ArrayList<>();
            
            if(cursosSelecionados != null && !cursosSelecionados.isEmpty()){
                for(String str:cursosSelecionados){
                    ids.add(Integer.parseInt(str));
                }
                crit.add(Restrictions.in("curso.idCurso", ids));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Turma> retorno = crit.list();
            
            session.flush();
            tx.commit();
            return retorno;
        } catch (HibernateException e){
            if(tx != null){
                tx.rollback();
            }
        } finally {
            if(session != null){
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
            
            if(retorno!=null){
                for(Turma turma : retorno){
                    turma.getTurmaAlunos().size();
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
    
    public List<Turma> getTurmasByIdProfessorEager(Integer idProfessor) {
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
            
            if(retorno!=null){
                for(Turma turma : retorno){
                    turma.getTurmaAlunos().size();
                    if(turma.getTurmaAlunos()!=null){
                    for(TurmaAluno turmaAluno:turma.getTurmaAlunos()){
                        turmaAluno.getAluno().getAlunoSimulados().size();
                        for(AlunoSimulado simulado:turmaAluno.getAluno().getAlunoSimulados()){
                            simulado.getRespostas().size();
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
