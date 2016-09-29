package helper;

import entidade.Disciplina;
import entidade.Subdisciplina;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class DisciplinaHelper {
    Session session = null;

    public boolean cadastrar(Disciplina disciplina) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            session.save(disciplina);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if(session != null){
                session.close();
            }
        }
        return true;
    }

    public boolean cadastrarSubdisciplina(Subdisciplina subdisciplina) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            session.save(subdisciplina);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if(session != null){
                session.close();
            }
        }
        return true;
    }

    public Disciplina getById(Integer id) {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Disciplina disciplina = (Disciplina) session.createCriteria(Disciplina.class).add(Restrictions.eq("idDisciplina", id)).uniqueResult();
            session.flush();
            tx.commit();
            return disciplina;
        } catch (Exception e) {
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

    public Disciplina getByNome(String nome) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Disciplina disciplina = (Disciplina) session.createCriteria(Disciplina.class).add(Restrictions.eq("nome", nome)).uniqueResult();;
            session.flush();
            tx.commit();
            return disciplina;
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }

        return null;
    }

    public List<Disciplina> getDisciplinas(String string) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Disciplina.class);

            if (string != null && !string.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("descricao", "%" + string + "%"))
                        .add(Restrictions.ilike("nome", "%" + string + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Disciplina> retorno = crit.list();
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
    
    
    public List<Disciplina> getDisciplinasDisponiveis(){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            List<Disciplina> disciplinas = session.createCriteria(Disciplina.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            return disciplinas;
        }catch(HibernateException e){
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
        return null;
    }

    public List<Subdisciplina> getSubdisciplinas(Integer disciplina) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
        tx.begin();
        List<Subdisciplina> subdisciplina = session.createCriteria(Subdisciplina.class).createAlias("disciplina", "disciplina").add(Restrictions.eq("disciplina.idDisciplina", disciplina)).list();
        session.flush();
        tx.commit();
        return subdisciplina;
        } catch(HibernateException e){
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

    public List<Subdisciplina> getSubdisciplinas(String nomeConsulta, String descricaoConsulta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Disciplina> getDisciplinasByCurso(Integer curso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            List<Disciplina> disciplina = session.createCriteria(Disciplina.class).createAlias("cursos", "curso").add(Restrictions.eq("curso.idCurso", curso)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            return disciplina;
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
