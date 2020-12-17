package br.com.sigpr.jsf.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter( value = "fileSizeConverter")
public class FileSizeConverter implements Converter{

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return (Long.valueOf(value.replace("Kb", "")) * 1024);
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (String.valueOf(((Long)value)/1024))+"Kb";
	}

}
