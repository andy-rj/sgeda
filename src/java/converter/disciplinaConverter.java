package converter;

import entidade.Disciplina;
import helper.DisciplinaHelper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.disciplinaConverter")
public class disciplinaConverter implements Converter{
   
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         if(value == null || value.isEmpty()){
            return null;
        }else{
            Integer id = new Integer(value);
            Disciplina disciplina = new DisciplinaHelper().getById(id);
            return disciplina;
        } 
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null || value.toString().equals("0")) return "0";
        Disciplina disciplina = (Disciplina)value;
        if(disciplina != null){
            return String.valueOf(disciplina.getIdDisciplina());
        }else{
            return "0";
        }
    }
}
