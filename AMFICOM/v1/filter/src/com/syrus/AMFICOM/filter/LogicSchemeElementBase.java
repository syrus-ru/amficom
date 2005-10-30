/*-
 * $Id: LogicSchemeElementBase.java,v 1.7 2005/10/30 15:20:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/10/30 15:20:27 $
 * @module filter
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

	public static String string(final String operandType) {
		/*
		 * ???: if something is unimplemented, there should be a comment with an
		 * explanation.
		 */
		return operandType;
	}

	public LogicSchemeElementBase(final String type,
			final FilterExpressionInterface filterExpression,
			final String operandType,
			final int x,
			final int y,
			final LogicSchemeBase logicScheme) {
		if (!((type.equals(LogicSchemeElementBase.tCondition)) || (type.equals(LogicSchemeElementBase.tOperand)) || (type.equals(LogicSchemeElementBase.tResult)))) {
			assert Log.debugMessage("LogicSchemeElementBase.<init> Scheme element should have one of these" + "types: Condition, Operand or Result!!", Level.FINEST);
			return;
		}

		if ((type.equals(LogicSchemeElementBase.tOperand))
				&& !((operandType.equals(LogicSchemeElementBase.otAnd)) || (operandType.equals(LogicSchemeElementBase.otOr)))) {
			assert Log.debugMessage("LogicSchemeElementBase.<init> Operand should be AND or OR", Level.FINEST);
			return;
		}

		this.type = type;
		this.filterExpression = filterExpression;
		this.operandType = operandType;
		this.x = x;
		this.y = y;
		this.logicScheme = logicScheme;

		this.setActiveZones();
	}

	@Override
	public String getTyp() {
		return TYP;
	}

	private void setActiveZones() {
		int azX = 0;
		int azY = 0;

		if (this.type.equals(LogicSchemeElementBase.tOperand) || this.type.equals(LogicSchemeElementBase.tCondition)) {
			azX = width - 12;
			azY = 5;
			this.out = this.logicScheme.createElementsActiveZone(this, ElementsActiveZoneBase.ztOut, 10, azX, azY);
			this.logicScheme.activeZones.add(this.out);
		}

		if (this.type.equals(LogicSchemeElementBase.tOperand) || this.type.equals(LogicSchemeElementBase.tResult)) {
			azX = 2;
			azY = 4;
			this.input = this.logicScheme.createElementsActiveZone(this, ElementsActiveZoneBase.ztIn, 12, azX, azY);
			this.logicScheme.activeZones.add(this.input);
		}
	}
}
