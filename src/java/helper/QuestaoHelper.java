package helper;

import entidade.Disciplina;
import entidade.Discursiva;
import entidade.Figura;
import entidade.Objetiva;
import entidade.Opcao;
import entidade.Questao;
import entidade.Redacao;
import entidade.Subdisciplina;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class QuestaoHelper {
    Session session = null;

    public boolean cadastrarObjetiva(Objetiva questao) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            
            if(questao.getQuestao().getSubdisciplinas()!= null && !questao.getQuestao().getSubdisciplinas().isEmpty()){
                for(Subdisciplina assunto : questao.getQuestao().getSubdisciplinas()){
                    if(assunto.getIdSubdisciplina() == null){
                        session.save(assunto);
                    }
                }
            }
            
            session.save(questao.getQuestao());
            
            for(Figura figura : questao.getQuestao().getFiguras()){
                figura.setQuestao(questao.getQuestao());
                session.save(figura);
            }
            
            session.save(questao);
            int letraA = 97;
            for(Opcao op : questao.getOpcaos()){
                op.setObjetiva(questao);
                op.setOpcao(Character.toChars(letraA)[0]);
                letraA++;
                session.save(op);
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
    
    public boolean cadastrarDiscursiva(Discursiva questao) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            
            if(questao.getQuestao().getSubdisciplinas()!= null && !questao.getQuestao().getSubdisciplinas().isEmpty()){
                for(Subdisciplina assunto : questao.getQuestao().getSubdisciplinas()){
                    if(assunto.getIdSubdisciplina() == null){
                        session.save(assunto);
                    }
                }
            }
            
            session.save(questao.getQuestao());
            
            for(Figura figura : questao.getQuestao().getFiguras()){
                figura.setQuestao(questao.getQuestao());
                session.save(figura);
            }
            
            session.save(questao);
            
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
    
    public boolean cadastrarRedacao(Redacao questao) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            
            if(questao.getQuestao().getSubdisciplinas()!= null && !questao.getQuestao().getSubdisciplinas().isEmpty()){
                for(Subdisciplina assunto : questao.getQuestao().getSubdisciplinas()){
                    if(assunto.getIdSubdisciplina() == null){
                        session.save(assunto);
                    }
                }
            }
            
            session.save(questao.getQuestao());
            
            for(Figura figura : questao.getQuestao().getFiguras()){
                figura.setQuestao(questao.getQuestao());
                session.save(figura);
            }
            
            session.save(questao);
            
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



}
