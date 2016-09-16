package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.cpfConverter")
public class cpfConverter implements Converter{
   
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null) return null;
        return value.replaceAll("\\D", "");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null || value.toString().isEmpty()) return "";
        String cpf = value.toString();
        String primeira = cpf.substring(0, 3);
        String segunda = cpf.substring(3,6);
        String terceira = cpf.substring(6,9);
        String quarta = cpf.substring(9);
        cpf = primeira + "." + segunda + "." + terceira + "-" + quarta;
        return cpf;
    }
}
