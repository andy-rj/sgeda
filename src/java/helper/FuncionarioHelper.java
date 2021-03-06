package helper;

import entidade.Endereco;
import entidade.Funcionario;
import entidade.Pessoa;
import entidade.Telefone;
import entidade.Usuario;
import hibernate.HibernateUtil;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class FuncionarioHelper {
    Session session = null;
    
    public boolean cadastrar(Pessoa pessoa, Endereco endereco, Funcionario funcionario, Usuario usuario){
        
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
            matricula = "F" + ano + "." +((mes/6)+1) + "-" + matricula;
            pessoa.setMatricula(matricula);
            usuario.setLogin(matricula);
            session.update(pessoa);
            session.save(endereco);
            for(Telefone telefone : pessoa.getTelefones()){
                session.save(telefone);
            }
            session.save(funcionario);
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
    
    public boolean alterarAtividade(Funcionario funcionario){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try{
            tx.begin();
            session.update(funcionario.getPessoa());
            session.update(funcionario.getPessoa().getUsuario());
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

    public Funcionario getFuncionarioEager(int idFuncionario) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Funcionario funcionario = (Funcionario) session.createCriteria(Funcionario.class).add(Restrictions.eq("idFuncionario", idFuncionario)).uniqueResult();
            if(funcionario!=null){
                funcionario.getPessoa().getUsuario().getPapels().size();
                funcionario.getPessoa().getTelefones().size();
            }
            tx.commit();
            return funcionario;
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
    
     public boolean salvarAlteracaoFuncionario(Funcionario funcionario, Set<Telefone> telefonesExcluir){
        
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
         
        try{
            tx.begin();
            session.update(funcionario.getPessoa());
            session.update(funcionario.getPessoa().getEnderecos());
            for(Telefone telefone : telefonesExcluir){
                session.delete(telefone);
            }
            for(Telefone telefone : funcionario.getPessoa().getTelefones()){
                if(telefone.getPessoa()==null){
                    telefone.setPessoa(funcionario.getPessoa());
                }
                session.save(telefone);
            }
            session.update(funcionario);
            session.update(funcionario.getPessoa().getUsuario());
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
            Pessoa pessoa = (Pessoa) session.createCriteria(Pessoa.class).add(Restrictions.eq("cpf", cpf)).uniqueResult();
            session.flush();
            tx.commit();
            return pessoa;
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
    
    public List<Funcionario> getFuncionarios(List<String> stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Funcionario.class);

            crit.createAlias("pessoa", "pessoa");
            List<Boolean> list = new ArrayList<>();
            if(stringConsulta !=null && !stringConsulta.isEmpty()){
                for(String str:stringConsulta){
                    if(str.equals("1")){
                        list.add(Boolean.TRUE);
                    }else if(str.equals("0")){
                        list.add(Boolean.FALSE);
                    }
                }
                crit.add(Restrictions.in("pessoa.ativo", list));
            }
            
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Funcionario> retorno = crit.list();
            if(retorno !=null)
                for (Funcionario funcionario : retorno) {
                    funcionario.getPessoa().getTelefones().size();
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

    public List<Funcionario> getFuncionarios(String stringConsulta) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Criteria crit = session.createCriteria(Funcionario.class);

            crit.createAlias("pessoa", "pessoa");

            if (stringConsulta != null && !stringConsulta.isEmpty()) {
                crit.add(Restrictions.disjunction()
                        .add(Restrictions.ilike("pessoa.cpf", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.matricula", "%" + stringConsulta + "%"))
                        .add(Restrictions.ilike("pessoa.nome", "%" + stringConsulta + "%"))
                );
            }

            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            List<Funcionario> retorno = crit.list();

            for (Funcionario funcionario : retorno) {
                for (Telefone telefone : funcionario.getPessoa().getTelefones()) {
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

}
