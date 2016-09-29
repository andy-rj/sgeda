package converter;

import controller.TurmaController;
import entidade.Disciplina;
import entidade.Professor;
import entidade.Turma;
import helper.ProfessorHelper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("converter.professorConverter")
public class professorConverter implements Converter{
   
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            TurmaController service = (TurmaController) fc.getExternalContext().getSessionMap().get("turmaController");
            for(Turma turma :service.getTurmas()){
                for(Professor professor: turma.getDisciplina().getProfessors()){
                    if(professor.getIdProfessor()==(new Integer(value))){
                        return professor;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            if(object instanceof Professor){
                return String.valueOf(((Professor) object).getIdProfessor());
            }
            return String.valueOf(object);
        }
        else {
            return null;
        }
    }   
}
