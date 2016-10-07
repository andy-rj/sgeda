package helper;

import entidade.Questao;
import entidade.Simulado;
import entidade.SimuladoQuestao;
import entidade.SimuladoQuestaoId;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class SimuladoHelper {
    Session session = null;

    public boolean cadastrar(Simulado simulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            
            session.save(simulado);
            
            if(simulado.getSimuladoQuestaos() != null && !simulado.getSimuladoQuestaos().isEmpty()){
                for(SimuladoQuestao questao : simulado.getSimuladoQuestaos()){
                    SimuladoQuestaoId id = new SimuladoQuestaoId();
                    id.setQuestaoIdQuestao(questao.getQuestao().getIdQuestao());
                    id.setSimuladoIdSimulado(simulado.getIdSimulado());
                    questao.setId(id);
                    questao.setSimulado(simulado);
                    session.save(questao);
                }
            }
            
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
    
    public List<Questao> getQuestoes(String string){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Questao.class).createAlias("disciplina", "disciplina");

            if (string != null && !string.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("disciplina.nome", "%" + string + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Questao> retorno = crit.list();
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
    
    public List<Questao> getAllQuestoes(){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Questao.class);

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Questao> retorno = crit.list();
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
    
    public List<Simulado> getSimulados(){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Simulado.class);

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Simulado> retorno = crit.list();
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
