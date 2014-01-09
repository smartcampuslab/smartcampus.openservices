package com.openserviceproject.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class checks if an email is valid or not.
 * code from 
 * http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
 * @author Giulia Canobbio
 *
 */
public class EmailValidator {
	
	private Pattern pattern;
	private Matcher matcher;
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public EmailValidator(){
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public boolean validate(final String hex){
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

}
