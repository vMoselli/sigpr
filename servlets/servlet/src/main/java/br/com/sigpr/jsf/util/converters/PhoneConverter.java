package br.com.sigpr.jsf.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "PhoneConverter")
public class PhoneConverter implements Converter{

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String string) {
		try {
			if (string != null && !string.equals("")) {
				String valor = string.replaceAll("-","").replaceAll("[(]", "").replaceAll("[)]", "");
				return Long.valueOf(valor);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		try {
			if(object != null && !"".equals(object.toString())){
				String cpfFormatado = object.toString();
				if(Long.valueOf(cpfFormatado)>0){
					while (cpfFormatado.length()!=10) {
						cpfFormatado = "0"+cpfFormatado;
					}
					
					return "("+cpfFormatado.substring(0,2)+")"+cpfFormatado.substring(2,6)+"-"+cpfFormatado.substring(6,10);
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
