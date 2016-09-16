package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.cepConverter")
public class cepConverter implements Converter{
   
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null) return null;
        return value.replaceAll("\\D", "");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null || value.toString().isEmpty()) return "";
        String cep = value.toString();
        String primeira = cep.substring(0, 5);
        String segunda = cep.substring(5);
        cep = primeira + "-" + segunda;
        return cep;
    }
}
