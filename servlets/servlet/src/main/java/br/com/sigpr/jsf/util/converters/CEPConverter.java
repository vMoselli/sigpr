package br.com.sigpr.jsf.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "CEPConverter")
public class CEPConverter implements Converter {

	public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String string) {
		Object retorno = null;
		try {
			if (string != null && !string.equals("")) {
				final String valor = string.replaceAll("-", "");
				retorno = Integer.valueOf(valor);
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
		return retorno;
	}

	public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object object) {
		String retorno = null;
		try {
			if (object != null && !object.toString().equals("")) {
				String cepFormatado = object.toString();
				if (Integer.valueOf(cepFormatado) > 0) {
					while (cepFormatado.length() != 8) {
						cepFormatado = "0" + cepFormatado;
					}

					retorno = cepFormatado.substring(0, 5) + "-" + cepFormatado.substring(5, 8);
				} 
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
		return retorno;
	}

}
