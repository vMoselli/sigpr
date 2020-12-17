package br.com.sigpr.jsf.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "CPFConverter")
public class CPFConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
		try {
			if (string != null && !string.equals("")) {
				String valor = string.replaceAll("-","");
				return Long.valueOf(valor);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		try {
			if(object != null && !"".equals(object.toString())){
				String cpfFormatado = object.toString();
				if(Long.valueOf(cpfFormatado)>0){
					while (cpfFormatado.length()<11) {
						cpfFormatado = "0"+cpfFormatado;
					}
					
					return cpfFormatado.substring(0,9)+"-"+cpfFormatado.substring(9,11);
				}else{
					return null;
				}
					
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

}