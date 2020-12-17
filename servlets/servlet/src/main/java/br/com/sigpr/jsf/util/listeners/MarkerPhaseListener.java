package br.com.sigpr.jsf.util.listeners;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 
 * @author mmueller
 */
public class MarkerPhaseListener implements PhaseListener {
	private static final long serialVersionUID = -6234919027810770555L;

	UIViewRoot _root;

	@Override
	public void beforePhase(PhaseEvent event) {
		_root = event.getFacesContext().getViewRoot();
		if (_root != null && event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
			markLabels4Required(_root);
		}
	}

	@Override
	public void afterPhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	private void markLabels4Required(UIComponent parent) {
		String marker = " <span style=\"color: red;\">*</span>";
		for (UIComponent child : parent.getChildren()) {
			if (child instanceof HtmlOutputLabel) {
				HtmlOutputLabel label = (HtmlOutputLabel) child;
				String targetId = label.getNamingContainer().getClientId() + ":" + label.getFor();
				UIComponent target = _root.findComponent(targetId);
				if (target instanceof UIInput) {
					UIInput input = (UIInput) target;
					String labelText = label.getValue().toString();

					if (input.isRequired() && !labelText.endsWith(marker)) {
						label.setValue(labelText + marker);
					}

					if (!input.isRequired() && labelText.endsWith(marker)) {
						label.setValue(labelText.substring(0, labelText.length() - marker.length()));
					}
				}
			}

			markLabels4Required(child);
		}
	}
}