package helper;

import entidade.Aluno;
import entidade.AlunoSimulado;
import entidade.Endereco;
import entidade.Funcionario;
import entidade.Pessoa;
import entidade.Telefone;
import entidade.TurmaAluno;
import entidade.TurmaAlunoId;
import entidade.Usuario;
import hibernate.HibernateUtil;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class AlunoHelper {
    Session session = null;
    
    public boolean cadastrar(Pessoa pessoa, Endereco endereco, Aluno aluno, Usuario usuario){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try {
            usuario.setSenha(LoginHelper.md5(usuario.getSenha()));
        } catch (NoSuchAlgorithmException e) {

        }
        
        try{
            tx.begin();
            if(pessoa.getFoto() != null) session.save(pessoa.getFoto());
            session.save(pessoa);
            Integer idPessoa = pessoa.getIdPessoa();
            String matricula = Integer.toString(idPessoa);
            while(matricula.length()<5){
                matricula = "0" + matricula;
            }
            int ano = Calendar.getInstance().get(Calendar.YEAR);
            int mes = Calendar.getInstance().get(Calendar.MONTH);
            matricula = "A" + ano + "." +((mes/6)+1) + "-" + matricula;
            pessoa.setMatricula(matricula);
            usuario.setLogin(matricula);
            session.update(pessoa);
            session.save(endereco);
            for(Telefone telefone : pessoa.getTelefones()){
                session.save(telefone);
            }
            
            session.save(aluno);
            for(TurmaAluno turmaAluno: aluno.getTurmaAlunos()){
               turmaAluno.setId(new TurmaAlunoId(turmaAluno.getAluno().getIdAluno(), turmaAluno.getTurma().getIdTurma()));
               session.save(turmaAluno);
            }
            
            session.save(usuario);
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
    
    public boolean alterarAtividade(Aluno aluno){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try{
            tx.begin();
            session.update(aluno.getPessoa());
            session.update(aluno.getPessoa().getUsuario());
            session.update(aluno);
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
    
    public boolean alterarAprovado(Aluno aluno){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try{
            tx.begin();
            session.update(aluno);
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

    public List<Aluno> getAlunosByIdCurso(List<String> cursosSelecionados, Date dataInicio, Date dataFim) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Aluno.class);

            crit.createAlias("turmaAlunos", "turmaAlunos").createAlias("turmaAlunos.turma", "turma").createAlias("turma.curso", "curso");

            if(dataInicio != null){
                crit.add(Restrictions.ge("turmaAlunos.dataIncricao", dataInicio));
            }
            
            if(dataFim != null){
                crit.add(Restrictions.le("turmaAlunos.dataIncricao", dataFim));
            }
            
            List<Integer> ids = new ArrayList<>();
            
            if(cursosSelecionados != null && !cursosSelecionados.isEmpty()){
                for(String str:cursosSelecionados){
                    ids.add(Integer.parseInt(str));
                }
                crit.add(Restrictions.in("curso.idCurso", ids));
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            crit.addOrder(Order.asc("turmaAlunos.dataIncricao"));
            List<Aluno> retorno = crit.list();

            for (Aluno aluno : retorno) {
                aluno.getPessoa().getTelefones().size();
                aluno.getTurmaAlunos().size();
            }
            
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
        
    public boolean salvarAlteracaoAluno(Aluno aluno, Set<Telefone> telefonesExcluir){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try{
            tx.begin();
            session.update(aluno.getPessoa());
            session.update(aluno.getPessoa().getEnderecos());
            for(Telefone telefone : telefonesExcluir){
                session.delete(telefone);
            }
            for(Telefone telefone : aluno.getPessoa().getTelefones()){
                if(telefone.getPessoa()==null){
                    telefone.setPessoa(aluno.getPessoa());
                }
                session.save(telefone);
            }
            session.update(aluno);
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
    
    public Pessoa getByCpf(String cpf){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try{
            tx.begin();
            Pessoa aluno = (Pessoa) session.createCriteria(Pessoa.class).add(Restrictions.eq("cpf", cpf)).uniqueResult();
            session.flush();
            tx.commit();
            return aluno;
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

    public List<Aluno> getAlunos(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Aluno.class);

            crit.createAlias("pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Aluno> retorno = crit.list();

            for (Aluno aluno : retorno) {
                for (Telefone telefone : aluno.getPessoa().getTelefones()) {
                    telefone.getDdd();
                }
            }
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
    
    public List<Aluno> getAlunosTop20(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Aluno.class);

            crit.createAlias("pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            crit.addOrder(Order.desc("pessoa.dataCadastro"));
            crit.setMaxResults(20);

            List<Aluno> retorno = crit.list();

            for (Aluno aluno : retorno) {
                for (Telefone telefone : aluno.getPessoa().getTelefones()) {
                    telefone.getDdd();
                }
            }
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
    
    public Aluno getById(Integer idAluno) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Aluno aluno = (Aluno) session.createCriteria(Aluno.class).add(Restrictions.eq("idAluno", idAluno)).uniqueResult();
            tx.commit();
            return aluno;
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

    public Aluno getByIdEager(Integer idAluno) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Aluno aluno = (Aluno) session.createCriteria(Aluno.class).add(Restrictions.eq("idAluno", idAluno)).uniqueResult();
            if(aluno!=null){
                aluno.getAlunoSimulados().size();
                aluno.getTurmaAlunos().size();
                aluno.getPessoa().getTelefones().size();
                
            }
            tx.commit();
            return aluno;
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
    
    public Aluno getByIdEagerAlunoSimulado(Integer idAluno) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Aluno aluno = (Aluno) session.createCriteria(Aluno.class).add(Restrictions.eq("idAluno", idAluno)).uniqueResult();
            if(aluno!=null){
                aluno.getAlunoSimulados().size();
                for(AlunoSimulado alunoSimulado:aluno.getAlunoSimulados()){
                    alunoSimulado.getRespostas().size();
                }
                aluno.getTurmaAlunos().size();
                for(TurmaAluno turmaAluno:aluno.getTurmaAlunos()){
                    turmaAluno.getTurma().getTurmaSimulados().size();
                }
                aluno.getPessoa().getTelefones().size();
                
            }
            tx.commit();
            return aluno;
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

}
