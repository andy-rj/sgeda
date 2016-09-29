/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entidade.Foto;
import entidade.Papel;
import entidade.Pessoa;
import entidade.Usuario;
import hibernate.HibernateUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author anderson
 */
public class LoginHelper {

    public Foto getFotoPerfil(Pessoa pessoa) {
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Foto.class);
            crit.createAlias("pessoa", "pessoa");
            crit.add(Restrictions.eq("pessoa.idPessoa", pessoa.getIdPessoa()));
            Foto foto = (Foto) crit.uniqueResult();
            session.flush();
            tx.commit();
            return foto;
        } catch (Exception e) {
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

    Session session = null;

    /**
     * Encriptar a senha utilizndo md5
     *
     * @param senha
     * @return Senha encriptada
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String senha) throws NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();

        if (senha != null) {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());

            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        }

        return sb.toString();
    }

   
    
    public String encriptar(String senha) {

        try {
            return md5(senha);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Usuario getByLoginSenha(String usuario, String senha) {
        if (senha == null || usuario == null) {
            return null;
        }
        try {
            senha = md5(senha);
        } catch (NoSuchAlgorithmException e) {

        }
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Usuario.class);
            crit.add(Restrictions.eq("senha", senha)).add(Restrictions.eq("login", usuario));
            Usuario login = (Usuario) crit.uniqueResult();
            session.flush();
            tx.commit();
            return login;
        } catch (Exception e) {
            if(tx != null)
                tx.rollback();
        } finally {
            if(session != null)
                session.close();
        }

        return null;

    }
    
    public Papel getPapelProfessor(){
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Papel.class);
            crit.add(Restrictions.eq("idPapel", 3));
            Papel papel = (Papel) crit.uniqueResult();
            session.flush();
            tx.commit();
            return papel;
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
        } finally {
            if (session != null){
                session.close();
            }
        }

        return null;
    }
    
    public Papel getPapelAtendente(){
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Papel.class);
            crit.add(Restrictions.eq("idPapel", 2));
            Papel papel = (Papel) crit.uniqueResult();
            session.flush();
            tx.commit();
            return papel;
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
        } finally {
            if (session != null){
                session.close();
            }
        }

        return null;
    }
    
    public Papel getPapelAdminstrador(){
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Papel.class);
            crit.add(Restrictions.eq("idPapel", 1));
            Papel papel = (Papel) crit.uniqueResult();
            session.flush();
            tx.commit();
            return papel;
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
        } finally {
            if (session != null){
                session.close();
            }
        }

        return null;
    }
    
    public Papel getPapelAluno(){
        session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Papel.class);
            crit.add(Restrictions.eq("idPapel", 4));
            Papel papel = (Papel) crit.uniqueResult();
            session.flush();
            tx.commit();
            return papel;
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
        } finally {
            if (session != null){
                session.close();
            }
        }

        return null;
    }
    
     public boolean cadastrar(Usuario usuario, String senha){
        
        try {
            senha = md5(senha);
        } catch (NoSuchAlgorithmException e) {

        }
        
        session = HibernateUtil.getSessionFactory().openSession();
         Transaction tx = session.getTransaction();
         
         try {
            tx.begin();
            usuario.setSenha(senha);
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
     
      public void addPapel(Usuario usuario, Papel papelProfessor) {
          session = HibernateUtil.getSessionFactory().openSession();
          Transaction tx = session.getTransaction();
          try{
            tx.begin();
            usuario.setPapels(new HashSet());
            usuario.getPapels().add(papelProfessor);
            session.update(usuario);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
              if(session != null){
                  session.close();
              }
          }
      }
        
    
}
