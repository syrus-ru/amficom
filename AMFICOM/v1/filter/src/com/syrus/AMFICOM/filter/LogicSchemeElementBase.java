/*
 * $Id: LogicSchemeElementBase.java,v 1.2 2005/04/13 19:09:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/13 19:09:41 $
 * @module filter_v1
 */
public class LogicSchemeElementBase extends ProSchemeElementBase
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter scheme object";

	public static String tCondition = "label_condition";
	public static String tOperand = "label_operand";
	public static String tResult = "label_result";

	public static String otAnd = "label_and";
	public static String otOr = "label_or";

	public static int width = 120;
	public static int height = 20;

	public ElementsActiveZoneBase out = null;
	public ElementsActiveZoneBase input = null;

	public String type = "";
	public String operandType = "";
	public FilterExpressionInterface filterExpression = null;

	LogicSchemeBase logicScheme = null;

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
		if (!((type.equals(LogicSchemeElementBase.tCondition)) ||
			(type.equals(LogicSchemeElementBase.tOperand)) ||
			(type.equals(LogicSchemeElementBase.tResult))))
		{
			System.out.print("Scheme element should have one of these" +
					   "types: Condition, Operand or Result!!");
			return;
		}

		if ((type.equals(LogicSchemeElementBase.tOperand)) &&
			!((operandType.equals(LogicSchemeElementBase.otAnd)) ||
			(operandType.equals(LogicSchemeElementBase.otOr))))
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

		if (this.type.equals(LogicSchemeElementBase.tOperand)
				|| this.type.equals(LogicSchemeElementBase.tCondition))
		{
			azX = width - 12;
			azY = 5;
			this.out = this.logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZoneBase.ztOut,
					10,
					azX,
					azY);
			this.logicScheme.activeZones.add(this.out);
		}

		if (this.type.equals(LogicSchemeElementBase.tOperand)
				|| this.type.equals(LogicSchemeElementBase.tResult))
		{
			azX = 2;
			azY = 4;
			this.input = this.logicScheme.createElementsActiveZone(
					this,
					ElementsActiveZoneBase.ztIn,
					12,
					azX,
					azY);
			this.logicScheme.activeZones.add(this.input);
		}
	}
}
