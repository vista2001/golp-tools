package dev.diagram.model;

/**
 * 异常和补偿节点模型
 * 
 * @author 木木
 * 
 */
public class ExAndComModel extends ElementModel
{
	private String name = "异常和补偿";

	public ExAndComModel()
	{
		super();
	}

	@Override
	public void setText(String text)
	{

	}

	@Override
	public String getDesc()
	{

		return null;
	}

	@Override
	public String getText()
	{

		return name;
	}

	@Override
	public String getTypeName()
	{
		return new String("");
	}

}
