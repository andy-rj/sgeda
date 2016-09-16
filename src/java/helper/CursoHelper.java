package helper;

import entidade.Curso;
import hibernate.HibernateUtil;
import org.hibernate.Session;

public class CursoHelper {
    Session session = null;

    public boolean cadastrar(Curso curso) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
         
        try{
            session.beginTransaction();
            session.save(curso);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
}
