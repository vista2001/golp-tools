package dev.diagram.helper;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * 边权值有效性检验
 * 
 * @author 木木
 * 
 */
public class InputValidator implements IInputValidator
{

	@Override
	public String isValid(String newText)
	{

		if (Pattern.matches("0|[1-9][0-9]*", newText))
			return null;
		return "你输入的权值不正确！请重新输入数字。。。";
	}

}
