/*
 * $Id: LogicSchemeElement_yo.java,v 1.2 2004/06/08 15:31:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/08 15:31:57 $
 * @module filter_v1
 */
public class LogicSchemeElement_yo extends ProSchemeElement_yo
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter scheme object";

	/**
	 * Value: {@value}
	 * @deprecated Use {@link #TYP} instead.
	 */
	public static final String typ = TYP;

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

	/**
	 * @deprecated Use {@link #string(String)} instead.
	 */
	public static String String(String operandType) {
		return string(operandType);
	}

	public static String string(String operandType)
	{
		/*
		 * ???: if something is unimplemented, there should be a comment with
		 *      an explanation.
		 */
		return operandType;
	}

	public LogicSchemeElement_yo(
			String type,
			FilterExpressionInterface filterExpression,
			String operandType,
			int x,
			int y,
			LogicScheme_yo logicScheme)
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
		this.filterExpression = filterExpression;
		this.operandType = operandType;
		this.x = x;
		this.y = y;
		this.logicScheme = logicScheme;

		setActiveZones();
	}

	public String getTyp()
	{
		return TYP;
	}

	private void setActiveZones()
	{
		int azX = 0;
		int azY = 0;

		if (type.equals(LogicSchemeElement_yo.t_operand)
				|| type.equals(LogicSchemeElement_yo.t_condition))
		{
			azX = width - 12;
			azY = 5;
			out = logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZone_yo.zt_out,
					10,
					azX,
					azY);
			logicScheme.activeZones.add(out);
		}

		if (type.equals(LogicSchemeElement_yo.t_operand)
				|| type.equals(LogicSchemeElement_yo.t_result))
		{
			azX = 2;
			azY = 4;
			input = logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZone_yo.zt_in,
					12,
					azX,
					azY);
			logicScheme.activeZones.add(input);
		}
	}
}
