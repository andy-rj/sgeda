package servlet;

import controller.QuestaoController;
import entidade.Figura;
import helper.FiguraHelper;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/imagem/*")
public class ImagemServlet extends HttpServlet {
    
    private FiguraHelper figuraHelper;

    // Init ---------------------------------------------------------------------------------------

    @Override
    public void init() throws ServletException {
        figuraHelper = new FiguraHelper();
    }

    // Actions ------------------------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get ID from request.
        String imageId = request.getParameter("id");

        // Check if ID is supplied to the request.
        if (imageId == null) {
            // Do your thing if the ID is not supplied to the request.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Lookup Image by ImageId in database.
        // Do your "SELECT * FROM Image WHERE ImageID" thing.
        Figura image = null;
        
        QuestaoController questão = (QuestaoController) ((HttpServletRequest) request).getSession().getAttribute("questaoController");
        if(questão!=null && questão.getFiguras() != null){
            for(Figura figura : questão.getFiguras()){
                if(figura.getNome().equals(imageId)){
                    image = figura;
                    break;
                }
            }
        }
        
        if(image == null){
            synchronized(this){
                image = figuraHelper.getByNome(imageId);
            }
        }
        
        // Check if image is actually retrieved from database.
        if (image == null) {
            // Do your thing if the image does not exist in database.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Init servlet response.
        response.reset();
        response.setContentType(image.getTipo());
        response.setContentLength(Integer.parseInt(image.getTamanho()));

        // Write image content to response.
        response.getOutputStream().write(image.getImagem());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}