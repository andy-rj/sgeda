package controller;

import helper.AlunoHelper;
import helper.CursoHelper;
import helper.LoginHelper;
import helper.TurmaHelper;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

@ManagedBean
@SessionScoped
public class RelatorioAlunoController {
    private final AlunoHelper alunoHelper;
    private final CursoHelper cursoHelper;
    private final LoginHelper loginHelper;
    private final TurmaHelper turmaHelper;


   
    
    public RelatorioAlunoController() {
        cursoHelper = new CursoHelper();
        alunoHelper = new AlunoHelper();
        loginHelper = new LoginHelper();
        turmaHelper = new TurmaHelper();
    }

    public void addMessage(String id, String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }

    public void addMessage(String id, FacesMessage.Severity severidade, String summary) {
        FacesMessage message = new FacesMessage(severidade, summary, null);
        FacesContext.getCurrentInstance().addMessage(id, message);
    }
    
    private LineChartModel lineModel;

 
    public LineChartModel getLineModel2() {
        return lineModel;
    }
     
    private void createLineModel() {
                
        lineModel = initCategoryModel();
        lineModel.setTitle("Category Chart");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);
        lineModel.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
         
    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries voce = new ChartSeries();
        voce.setLabel("Você");
        voce.set("2004", 120);
        voce.set("2005", 100);
        voce.set("2006", 44);
        voce.set("2007", 150);
        voce.set("2008", 25);
 
        ChartSeries turma = new ChartSeries();
        turma.setLabel("Turma");
        turma.set("2004", 52);
        turma.set("2005", 60);
        turma.set("2006", 110);
        turma.set("2007", 90);
        turma.set("2008", 120);
 
        model.addSeries(voce);
        model.addSeries(turma);
         
        return model;
    }
    
}
