package helper;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Turma;
import hibernate.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class CursoHelper {

    Session session = null;

    public boolean cadastrar(Curso curso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.save(curso);
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

    public Curso getCursoByIdEager(Integer idCurso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Curso curso = (Curso) session.createCriteria(Curso.class).add(Restrictions.eq("idCurso", idCurso)).uniqueResult();
            if(curso!=null){
                curso.getDisciplinas().size();
                curso.getTurmas().size();
            }
            tx.commit();
            return curso;
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
    
    public Curso getCursoByIdEagerTurmaSimulado(Integer idCurso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Curso curso = (Curso) session.createCriteria(Curso.class).add(Restrictions.eq("idCurso", idCurso)).uniqueResult();
            if(curso!=null){
                curso.getDisciplinas().size();
                curso.getTurmas().size();
                for(Turma turma: curso.getTurmas()){
                    turma.getTurmaSimulados().size();
                }
            }
            tx.commit();
            return curso;
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

    public boolean salvarAlteracaoCurso(Curso curso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            session.update(curso);
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

    public List<Curso> getCursos(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Curso.class);

            crit.createAlias("disciplinas", "disciplina");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("nome", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("descricao", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("disciplina.nome", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("codigo", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Curso> retorno = crit.list();

            for (Curso curso : retorno) {
                curso.getDisciplinas().size();
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
    
    public List<Curso> getCursosEmAndamento() {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        Date now = new Date();
        now = new Date(now.getTime() + 1296000000);
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Curso.class);

            crit.createAlias("turmas", "turmas");

            crit.add(Restrictions.ge("turmas.dataFim", now));
                        
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Curso> retorno = crit.list();

            for (Curso curso : retorno) {
                curso.getTurmas().size();
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

    public List<Curso> getCursosDisponiveis() {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            List<Curso> cursos = session.createCriteria(Curso.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            return cursos;
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

    public List<Curso> getCursosDisponiveisEager() {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            List<Curso> cursos = session.createCriteria(Curso.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            if (cursos != null) {
                for (Curso curso : cursos) {
                    for (Turma turma : curso.getTurmas()) {
                        turma.getTurmaAlunos().size();
                    }
                }
            }
            return cursos;
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
