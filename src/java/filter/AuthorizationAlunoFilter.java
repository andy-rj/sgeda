package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.LoginController;
import entidade.Papel;
import entidade.Usuario;

/**
 * Filtro utilizado para restringir o acesso as paginas da aplicação
 *
 */
public class AuthorizationAlunoFilter implements Filter {

    @Override
    public void destroy() {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LoginController login = (LoginController) ((HttpServletRequest) request).getSession().getAttribute("loginController");
        boolean autorizado = false;
        for(Papel papel : login.getUsuarioLogado().getPapels()){
            if(papel.getIdPapel() == Usuario.PAPEL_ALUNO){
                autorizado = true;
                break;
            }
        }
        if (!autorizado) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            //Redirecionamos o usuário imediatamente 
            //para a página de index.xhtml 
            ((HttpServletResponse) response).sendRedirect(contextPath + "/faces/erro/autorizacao.xhtml");
        } else {
            //Caso ele esteja logado, apenas deixamos 
            //que o fluxo continue 
            chain.doFilter(request, response);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
