package wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CursoTurmaWrapper {

    public void orderList() {
        Collections.sort( turmas, new Comparator<Wrapper>() {
            public int compare(Wrapper obj1, Wrapper obj2) {
                return Integer.parseInt(obj1.getProgresso()) - (Integer.parseInt(obj2.getProgresso()));
            }
        });
    }
    
    public class Wrapper{
        String nome;
        String progresso;
        String look;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getProgresso() {
            return progresso;
        }

        public void setProgresso(String progresso) {
            this.progresso = progresso;
        }

        public String getLook() {
            return look;
        }

        public void setLook(String look) {
            this.look = look;
        }
    }
    
    String nomeCurso;
    List<Wrapper> turmas;

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public List<Wrapper> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Wrapper> turmas) {
        this.turmas = turmas;
    }
    
    public void setWrapper(String nome, String progresso, String look){
        Wrapper wrap = new Wrapper();
        wrap.setLook(look);
        wrap.setNome(nome);
        wrap.setProgresso(progresso);
        if(this.turmas==null){
            this.turmas = new ArrayList<>();
        }
        this.turmas.add(wrap);
    }
    
    public boolean contains(String nome){
        if(turmas==null)return false;
        for(Wrapper w:turmas){
            if(w.getNome().equalsIgnoreCase(nome)) return true;
        }
        return false;
    }
}
