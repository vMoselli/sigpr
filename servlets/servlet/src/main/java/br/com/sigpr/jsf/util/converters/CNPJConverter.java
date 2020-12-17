package br.com.sigpr.jsf.util.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "CNPJConverter")
public class CNPJConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
		try {
			if (string != null && !string.equals("")) {
				String valor = string.replaceAll("-","").replaceAll("/", "");
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
				String cnpjFormatado = object.toString();
				if(Long.valueOf(cnpjFormatado)>0){
					while (cnpjFormatado.length()<14) {
						cnpjFormatado = "0"+cnpjFormatado;
					}
					
					return cnpjFormatado.substring(0,8)+"/"+cnpjFormatado.substring(8,12)+"-"+cnpjFormatado.substring(12);
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