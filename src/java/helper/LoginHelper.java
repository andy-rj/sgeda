/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entidade.Usuario;
import hibernate.HibernateUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author anderson
 */
public class LoginHelper {
    
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
        session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tx = session.getTransaction();

        try {
            tx.begin();
            Criteria crit = session.createCriteria(Usuario.class);
            crit.add(Restrictions.eq("senha", senha)).add(Restrictions.eq("login", usuario));
            Usuario login = (Usuario) crit.uniqueResult();
            tx.commit();
            return login;
        } catch (Exception e) {
            tx.rollback();
        }

        return null;

    }
    
}
