package br.com.sigpr.jsf.util.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "MoneyConverter")
public class MoneyConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
		try {
			if (string != null && !string.equals("")) {
				String valor = string.replaceAll("[.]","").replaceAll(",", ".");
				return new BigDecimal(valor);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		try {
			if(object != null && !object.equals("")){
				NumberFormat formatter = NumberFormat.getInstance();
				formatter.setMinimumFractionDigits(2);
				formatter.setMaximumFractionDigits(2);
				
				return formatter.format(object);
			}
			else{
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

}