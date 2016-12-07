package helper;

import entidade.Discursiva;
import entidade.Figura;
import entidade.Objetiva;
import entidade.Opcao;
import entidade.Questao;
import entidade.Redacao;
import entidade.Assunto;
import entidade.Resposta;
import hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
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
            
            if(questao.getQuestao().getAssuntos()!= null && !questao.getQuestao().getAssuntos().isEmpty()){
                for(Assunto assunto : questao.getQuestao().getAssuntos()){
                    if(assunto.getIdAssunto()== null){
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
            
            if(questao.getQuestao().getAssuntos()!= null && !questao.getQuestao().getAssuntos().isEmpty()){
                for(Assunto assunto : questao.getQuestao().getAssuntos()){
                    if(assunto.getIdAssunto()== null){
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
            
            if(questao.getQuestao().getAssuntos()!= null && !questao.getQuestao().getAssuntos().isEmpty()){
                for(Assunto assunto : questao.getQuestao().getAssuntos()){
                    if(assunto.getIdAssunto()== null){
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

    public Integer getNumeroRespostasCorretas(int idObjetiva, Date inicio, Date fim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Questao getQuestaoByIdEager(Integer idQuestao) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Questao questao = (Questao) session.createCriteria(Questao.class).add(Restrictions.eq("idQuestao", idQuestao)).uniqueResult();
            if(questao!=null){
                questao.getFiguras().size();
                questao.getAssuntos().size();
                if(questao.getObjetiva()!=null){
                    questao.getObjetiva().getOpcaos().size();
                }
            }
            tx.commit();
            return questao;
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
            if(retorno != null){
                for(Questao questao: retorno){
                    questao.getAssuntos().size();
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

    public List<Objetiva> getQuestoesObjetivas(List<String> disciplinasSelecionadas, Date dataInicio, Date dataFim) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Objetiva.class);

            crit.createAlias("questao", "questao");
            crit.createAlias("questao.disciplina", "disciplina");
            crit.createAlias("questao.respostas", "respostas");
            crit.createAlias("respostas.alunoSimulado", "alunoSimulado");

            if (dataInicio != null) {
                crit.add(Restrictions.ge("alunoSimulado.data", dataInicio));
            }

            if (dataFim != null) {
                crit.add(Restrictions.le("alunoSimulado.data", dataFim));
            }

            List<Integer> ids = new ArrayList<>();

            if (disciplinasSelecionadas != null && !disciplinasSelecionadas.isEmpty()) {
                for (String str : disciplinasSelecionadas) {
                    ids.add(Integer.parseInt(str));
                }
                crit.add(Restrictions.in("disciplina.idDisciplina", ids));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Objetiva> retorno = crit.list();
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
        return null;    }

    public List<Resposta> getRespostasPorDisciplina(String disciplinasSelecionadas, Date dataInicio, Date dataFim) {
         session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Resposta.class);

            crit.createAlias("questao", "questao");
            crit.createAlias("questao.objetiva","objetiva");
            crit.createAlias("questao.disciplina", "disciplina");
            crit.createAlias("alunoSimulado", "alunoSimulado");

            if (dataInicio != null) {
                crit.add(Restrictions.ge("alunoSimulado.data", dataInicio));
            }

            if (dataFim != null) {
                crit.add(Restrictions.le("alunoSimulado.data", dataFim));
            }


            if (disciplinasSelecionadas != null) {
                crit.add(Restrictions.eq("disciplina.idDisciplina", Integer.parseInt(disciplinasSelecionadas)));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Resposta> retorno = crit.list();
            if(retorno!=null){
                for(Resposta resposta: retorno){
                    resposta.getQuestao().getObjetiva().getOpcaos().size();
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




}
