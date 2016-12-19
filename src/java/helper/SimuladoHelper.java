package helper;

import entidade.AlunoSimulado;
import entidade.AlunoSimuladoId;
import entidade.Questao;
import entidade.Resposta;
import entidade.Simulado;
import entidade.SimuladoQuestao;
import entidade.SimuladoQuestaoId;
import entidade.TurmaAluno;
import entidade.TurmaSimulado;
import entidade.TurmaSimuladoId;
import hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class SimuladoHelper {
    Session session = null;

    
    public boolean cadastrarSimuladoAluno(AlunoSimulado simulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            
            session.saveOrUpdate(simulado);
            for(Resposta resposta: simulado.getRespostas()){
                session.saveOrUpdate(resposta);
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

    public boolean excluir(AlunoSimuladoId id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            AlunoSimulado simulado = (AlunoSimulado) session.createCriteria(AlunoSimulado.class).add(Restrictions.eq("id", id)).uniqueResult();
            if(simulado!=null){
                simulado.getRespostas().size();
                for(Resposta resposta:simulado.getRespostas()){
                    session.delete(resposta);
                }
                session.delete(simulado);
            }
            
            tx.commit();
        } catch (HibernateException e){
            if(tx != null){
                tx.rollback();
                return false;
            }
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
    
    public Simulado getSimuladoByIdEager(Integer idSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Simulado simulado = (Simulado) session.createCriteria(Simulado.class).add(Restrictions.eq("idSimulado", idSimulado)).uniqueResult();
            if(simulado!=null){
                simulado.getSimuladoQuestaos().size();
                for(SimuladoQuestao questao:simulado.getSimuladoQuestaos()){
                    questao.getQuestao().getFiguras().size();
                }
            }
            tx.commit();
            return simulado;
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
    
    public TurmaSimulado getTurmaSimuladoByIdEager(TurmaSimuladoId idTurmaSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            TurmaSimulado retorno = (TurmaSimulado) session.createCriteria(TurmaSimulado.class).add(Restrictions.eq("id", idTurmaSimulado)).uniqueResult();
            if(retorno!=null){
                retorno.getTurma().getTurmaSimulados().size();
                retorno.getSimulado().getSimuladoQuestaos().size();
                for(SimuladoQuestao simuladoQuestao: retorno.getSimulado().getSimuladoQuestaos()){
                    if(simuladoQuestao.getQuestao().getObjetiva()!=null){
                        simuladoQuestao.getQuestao().getObjetiva().getOpcaos().size();
                    }
                    simuladoQuestao.getQuestao().getSimuladoQuestaos().size();
                }
            }
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
    
    public TurmaSimulado getTurmaSimuladoByIdTurmaAlunoEager(TurmaSimuladoId idTurmaSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            TurmaSimulado retorno = (TurmaSimulado) session.createCriteria(TurmaSimulado.class).add(Restrictions.eq("id", idTurmaSimulado)).uniqueResult();
            if(retorno!=null){
                retorno.getTurma().getTurmaSimulados().size();
                retorno.getSimulado().getSimuladoQuestaos().size();
                for(SimuladoQuestao simuladoQuestao: retorno.getSimulado().getSimuladoQuestaos()){
                    if(simuladoQuestao.getQuestao().getObjetiva()!=null){
                        simuladoQuestao.getQuestao().getObjetiva().getOpcaos().size();
                    }
                }
                retorno.getTurma().getTurmaAlunos().size();
                for(TurmaAluno turmaAlunos:retorno.getTurma().getTurmaAlunos()){
                    turmaAlunos.getAluno().getAlunoSimulados().size();
                    for(AlunoSimulado alunoSimulado: turmaAlunos.getAluno().getAlunoSimulados()){
                        alunoSimulado.getRespostas().size();
                    }
                }
            }
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
    
    public AlunoSimulado getAlunoSimuladoByIdEager(AlunoSimuladoId idSimulado) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            AlunoSimulado simulado = (AlunoSimulado) session.createCriteria(AlunoSimulado.class).add(Restrictions.eq("id", idSimulado)).uniqueResult();
            if(simulado!=null){
                simulado.getRespostas().size();
                for(Resposta resposta:simulado.getRespostas()){
                    if(resposta.getQuestao().getObjetiva()!=null)
                        resposta.getQuestao().getObjetiva().getOpcaos().size();
                    resposta.getQuestao().getSimuladoQuestaos().size();
                }
            }
            tx.commit();
            return simulado;
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
    
    
    
    public List<Simulado> getSimulados(String string){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Simulado.class).createAlias("professor", "professor").createAlias("professor.pessoa", "pessoa");
            crit.createAlias("simuladoQuestaos", "simuladoQ").createAlias("simuladoQ.questao", "questao").createAlias("questao.disciplina", "disciplina");
            if (string != null && !string.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("disciplina.nome", "%" + string + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + string + "%"))
                );
            }

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
