package dev.diagram.helper;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * ��Ȩֵ��Ч�Լ���
 * 
 * @author ľľ
 * 
 */
public class InputValidator implements IInputValidator
{

	@Override
	public String isValid(String newText)
	{

		if (Pattern.matches("0|[1-9][0-9]*", newText))
			return null;
		return "�������Ȩֵ����ȷ���������������֡�����";
	}

}
