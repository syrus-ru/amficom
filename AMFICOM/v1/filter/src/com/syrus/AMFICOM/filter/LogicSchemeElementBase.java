/*
 * $Id: LogicSchemeElementBase.java,v 1.1 2004/06/17 10:23:05 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/17 10:23:05 $
 * @module filter_v1
 */
public class LogicSchemeElementBase extends ProSchemeElementBase
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

	public ElementsActiveZoneBase out = null;
	public ElementsActiveZoneBase input = null;

	public String type = "";
	public String operandType = "";
	public FilterExpressionInterface filterExpression = null;

	LogicSchemeBase logicScheme = null;

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

	public LogicSchemeElementBase(
			String type,
			FilterExpressionInterface filterExpression,
			String operandType,
			int x,
			int y,
			LogicSchemeBase logicScheme)
	{
		if (!((type.equals(LogicSchemeElementBase.t_condition)) ||
			(type.equals(LogicSchemeElementBase.t_operand)) ||
			(type.equals(LogicSchemeElementBase.t_result))))
		{
			System.out.print("Scheme element should have one of these" +
					   "types: Condition, Operand or Result!!");
			return;
		}

		if ((type.equals(LogicSchemeElementBase.t_operand)) &&
			!((operandType.equals(LogicSchemeElementBase.ot_and)) ||
			(operandType.equals(LogicSchemeElementBase.ot_or))))
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

		if (type.equals(LogicSchemeElementBase.t_operand)
				|| type.equals(LogicSchemeElementBase.t_condition))
		{
			azX = width - 12;
			azY = 5;
			out = logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZoneBase.zt_out,
					10,
					azX,
					azY);
			logicScheme.activeZones.add(out);
		}

		if (type.equals(LogicSchemeElementBase.t_operand)
				|| type.equals(LogicSchemeElementBase.t_result))
		{
			azX = 2;
			azY = 4;
			input = logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZoneBase.zt_in,
					12,
					azX,
					azY);
			logicScheme.activeZones.add(input);
		}
	}
}
