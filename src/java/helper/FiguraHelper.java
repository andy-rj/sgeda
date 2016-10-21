package helper;

import entidade.Figura;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class FiguraHelper {
    Session session = null;

    public boolean cadastrar(Figura figura) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            session.save(figura);
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

    public Figura getByNome(String nome) {

        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Figura figura = (Figura) session.createCriteria(Figura.class).add(Restrictions.eq("nome", nome)).uniqueResult();
            session.flush();
            tx.commit();
            return figura;
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

}
