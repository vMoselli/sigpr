package br.com.sigpr.jsf.util.listeners;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

public class SessionPhaseListener implements PhaseListener {
	 
    private static final long serialVersionUID = -1065005858605121693L;
    private static final String loginPage = "login.prime";
 
    @Override
    public void afterPhase(PhaseEvent event) {
        //não faz nada
    }
 
    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ext = context.getExternalContext();
        HttpSession session = (HttpSession) ext.getSession(false);
        boolean newSession = (session == null) || (session.isNew());
        boolean postback = !ext.getRequestParameterMap().isEmpty();
        boolean timedout = postback && newSession;
        if (timedout) {
            Application app = context.getApplication();
            ViewHandler viewHandler = app.getViewHandler();
            UIViewRoot view = viewHandler.createView(context, "/" + loginPage);
 
            context.setViewRoot(view);
            context.renderResponse();
            try {
                viewHandler.renderView(context, view);
                context.responseComplete();
            } catch (Throwable t) {
                 // opcionalmente
                 //throw new FacesException("Sessão expirada!!!", t);
            }
        }
    }
 
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
