package com.syrus.AMFICOM.filter;

public class LogicSchemeElement_yo extends ProSchemeElement_yo
{
	static public String typ = "Filter scheme object";
  
	public static String t_condition = "label_condition";
	public static String t_operand = "label_operand";
	public static String t_result = "label_result";  

	public static String ot_and = "label_and";
	public static String ot_or = "label_or";

	public static int width = 120;
	public static int height = 20;

	public ElementsActiveZone_yo out = null;
	public ElementsActiveZone_yo input = null;

	public String type = "";
	public String operandType = "";
	public FilterExpressionInterface filterExpression = null;

	LogicScheme_yo logicScheme = null;

	static public String String(String operandType)
	{
		return operandType;
	}
	
	public LogicSchemeElement_yo(
			String type,
            FilterExpressionInterface fe,
            String operandType,
            int itsX,
            int itsY,
            LogicScheme_yo ls)
	{
		if (!((type.equals(LogicSchemeElement_yo.t_condition)) ||
			(type.equals(LogicSchemeElement_yo.t_operand)) ||
			(type.equals(LogicSchemeElement_yo.t_result))))
		{
			System.out.print("Scheme element should have one of these" +
                       "types: Condition, Operand or Result!!");
			return;
		}

		if ((type.equals(LogicSchemeElement_yo.t_operand)) &&
			!((operandType.equals(LogicSchemeElement_yo.ot_and)) ||
			(operandType.equals(LogicSchemeElement_yo.ot_or))))
		{
			System.out.print("Operand should be AND or OR");
			return;
		}

		this.type = type;
		this.filterExpression = fe;
		this.operandType = operandType;
		this.x = itsX;
		this.y = itsY;
		this.logicScheme = ls;

		setActiveZones();
	}

	public String getTyp()
	{
		return typ;
	}

	private void setActiveZones()
	{
		int azX = 0;
		int azY = 0;
	
		if (this.type.equals(LogicSchemeElement_yo.t_operand) ||
			this.type.equals(LogicSchemeElement_yo.t_condition))
		{
			azX = this.width - 12;
			azY = 5;
			out = this.logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZone_yo.zt_out,
					10,
					azX,
					azY);
			this.logicScheme.activeZones.add(out);
		}

		if (this.type.equals(LogicSchemeElement_yo.t_operand) ||
			this.type.equals(LogicSchemeElement_yo.t_result))
		{
			azX = 2;
			azY = 4;
			input = this.logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZone_yo.zt_in,
					12,
					azX,
					azY);
			this.logicScheme.activeZones.add(input);
		}
	}
}