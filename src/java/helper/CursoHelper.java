package helper;

import entidade.Curso;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Turma;
import hibernate.HibernateUtil;
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
         
        try{
            tx.begin();
            session.save(curso);
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
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Curso> retorno = crit.list();

            //for (Curso curso : retorno) {
                
            //}
            
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
    
    public List<Curso> getCursosDisponiveis(){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            List<Curso> cursos = session.createCriteria(Curso.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
            session.flush();
            tx.commit();
            return cursos;
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
    
}
