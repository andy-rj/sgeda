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
/**
 * Filtro utilizado para restringir o acesso as paginas da aplicação
 *
 */
public class LoginFilter implements Filter {

    @Override
    public void destroy() {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LoginController login = (LoginController) ((HttpServletRequest) request).getSession().getAttribute("loginController");

        if (login == null || login.getUsuarioLogado() == null) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            //Redirecionamos o usuário imediatamente 
            //para a página de index.xhtml 
            ((HttpServletResponse) response).sendRedirect(contextPath + "/faces/index.xhtml");
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
