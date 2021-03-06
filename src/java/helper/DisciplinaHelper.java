package helper;

import entidade.Disciplina;
import entidade.Assunto;
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

    public boolean cadastrarSubdisciplina(Assunto subdisciplina) {
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
    
    public boolean salvarAlteracaoDisciplina(Disciplina disciplina){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            session.update(disciplina);
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
                        .add(Restrictions.ilike("codigo", "%" + string + "%"))
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
    
    public List<Disciplina> getDisciplinasDisponiveisQuestaoEager(){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            List<Disciplina> disciplinas = session.createCriteria(Disciplina.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            if(disciplinas!=null){
                for(Disciplina disciplina: disciplinas){
                    disciplina.getQuestaos().size();
                }
            }
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

    public List<Assunto> getSubdisciplinas(Integer disciplina) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
        tx.begin();
        List<Assunto> subdisciplina = session.createCriteria(Assunto.class).createAlias("disciplina", "disciplina").add(Restrictions.eq("disciplina.idDisciplina", disciplina)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
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

    public Assunto getSubdisciplina(String nomeSubdisciplina, Integer idDisciplina) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
        tx.begin();
        Assunto subdisciplina = (Assunto)session.createCriteria(Assunto.class).createAlias("disciplina", "disciplina").
                add(Restrictions.eq("disciplina.idDisciplina", idDisciplina)).
                add(Restrictions.eq("nome", nomeSubdisciplina).ignoreCase()).uniqueResult();
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
    
    public List<Disciplina> getDisciplinasByCurso(Integer curso) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            List<Disciplina> disciplina = session.createCriteria(Disciplina.class).createAlias("cursos", "curso").add(Restrictions.eq("curso.idCurso", curso)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            for(Disciplina dis: disciplina){
                dis.getProfessors().size();
            }
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
