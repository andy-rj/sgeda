package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.telefoneConverter")
public class telefoneConverter implements Converter{
   
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null) return null;
        return value.replaceAll("\\D", "");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null|| value.toString().isEmpty()) return "";
        String telefone = value.toString();
        String primeira = telefone.substring(0, 4);
        String segunda = telefone.substring(4);
        telefone = primeira + "-" + segunda;
        return telefone;
    }
}
