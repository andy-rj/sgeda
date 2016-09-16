package helper;

import entidade.Disciplina;
import entidade.Endereco;
import entidade.Pessoa;
import entidade.Professor;
import entidade.Telefone;
import entidade.Usuario;
import static helper.LoginHelper.md5;
import hibernate.HibernateUtil;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ProfessorHelper {
    Session session = null;
    
    public boolean cadastrar(Pessoa pessoa, Endereco endereco, Professor professor, Usuario usuario){
        
        session = HibernateUtil.getSessionFactory().getCurrentSession();
         
        try {
            usuario.setSenha(LoginHelper.md5(usuario.getSenha()));
        } catch (NoSuchAlgorithmException e) {

        }
        try{
            session.beginTransaction();
            session.save(pessoa.getFoto());
            session.save(pessoa);
            Integer idPessoa = pessoa.getIdPessoa();
            String matricula = Integer.toString(idPessoa);
            while(matricula.length()<5){
                matricula = "0" + matricula;
            }
            matricula = "P2016" + matricula;
            pessoa.setMatricula(matricula);
            usuario.setLogin(matricula);
            session.update(pessoa);
            session.save(endereco);
            for(Telefone telefone : pessoa.getTelefones()){
                session.save(telefone);
            }
            session.save(professor);
            session.save(usuario);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public Professor getByCpf(String cpf){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Professor professor = (Professor) session.createCriteria(Professor.class).createAlias("pessoa", "pessoa").add(Restrictions.eq("pessoa.cpf", cpf)).uniqueResult();
        session.getTransaction().commit();
        return professor;
    }

    public List<Professor> getProfessores(String nome, String cpf, String matricula) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Criteria crit = session.createCriteria(Professor.class);

        crit.createAlias("pessoa", "pessoa");

        if (cpf != null && !cpf.isEmpty()) {
            crit.add(Restrictions.eq("pessoa.cpf", cpf));
        }

        if (matricula != null && !matricula.isEmpty()) {
            crit.add(Restrictions.ilike("pessoa.matricula", "%" + matricula + "%"));
        }

        if (nome != null && !nome.isEmpty()) {

            crit.add(Restrictions.ilike("pessoa.nome", "%" + nome + "%"));

        }
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Professor> retorno = crit.list();
        
        for(Professor professor : retorno){
            for(Telefone telefone :professor.getPessoa().getTelefones()){
                telefone.getDdd();
            }
            for(Disciplina disciplina: professor.getDisciplinas()){
                disciplina.getNome();
            }
        }
        
        session.getTransaction().commit();

        return retorno;    
    }

}
