package br.com.sigpr.jsf.util.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "emailValidator")
public class EmailValidator implements Validator {

	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = String.valueOf(arg2);
		// Make the comparison case-insensitive.
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}

		if (!isValid) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail inválido.", null));
		}
	}
}
