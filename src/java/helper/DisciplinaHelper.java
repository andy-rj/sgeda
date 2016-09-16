package helper;

import entidade.Disciplina;
import entidade.Subdisciplina;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class DisciplinaHelper {
    Session session = null;

    public boolean cadastrar(Disciplina disciplina) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        try{
            session.beginTransaction();
            session.save(disciplina);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean cadastrarSubdisciplina(Subdisciplina subdisciplina) {
         session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        try{
            session.beginTransaction();
            session.save(subdisciplina);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Disciplina getById(Integer id) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Disciplina disciplina = (Disciplina) session.createCriteria(Disciplina.class).add(Restrictions.eq("idDisciplina", id)).uniqueResult();;
            session.getTransaction().commit();
            return disciplina;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        return null;

    }

    public List<Disciplina> getDisciplinas(String nome, String descricao) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Criteria crit = session.createCriteria(Disciplina.class);

        if (descricao != null && !descricao.isEmpty()) {
            crit.add(Restrictions.ilike("descricao", "%" + descricao + "%"));
        }

        if (nome != null && !nome.isEmpty()) {

            crit.add(Restrictions.ilike("nome", "%" + nome + "%"));

        }
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Disciplina> retorno = crit.list();
               
        session.getTransaction().commit();

        return retorno;       
    }
    
    
    public List<Disciplina> getDisciplinasDisponiveis(){
        
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Disciplina> disciplinas = session.createCriteria(Disciplina.class).list();
        session.getTransaction().commit();

        return disciplinas;
    }

    public List<Subdisciplina> getSubdisciplinas(Integer disciplina) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Subdisciplina> subdisciplina = session.createCriteria(Subdisciplina.class).createAlias("disciplina", "disciplina").add(Restrictions.eq("disciplina.idDisciplina", disciplina)).list();
        session.getTransaction().commit();

        return subdisciplina;
    }

    public List<Subdisciplina> getSubdisciplinas(String nomeConsulta, String descricaoConsulta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
