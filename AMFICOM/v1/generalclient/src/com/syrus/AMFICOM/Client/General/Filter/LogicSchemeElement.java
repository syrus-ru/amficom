package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import com.syrus.AMFICOM.filter.*;

public class LogicSchemeElement extends LogicSchemeElementBase
{
	public LogicSchemeElement(
			String type,
				FilterExpressionInterface fe,
				String operandType,
				int itsX,
				int itsY,
				LogicScheme ls)
	{
		super(type, fe, operandType, itsX, itsY, ls);
	}

	public void paint(Graphics g)
	{
		if (this.selected)
			g.setColor(Color.blue);
		else
			g.setColor(Color.black);

		g.drawRect(this.x, this.y, width, height);
		g.setColor(Color.pink);
		g.fillRect(this.x + 1, this.y + 1, width - 1, height - 1);

		g.setColor(Color.black);
		int titleX = this.x + 15;
		int titleY = this.y + 15;

		if (this.filterExpression != null)
			g.drawString(LangModel.getString(this.type) + ":" + this.filterExpression.getListID(), titleX, titleY);
		else
		{
			if (this.operandType != "")
				g.drawString(LangModel.getString(this.type) + ":" + LangModel.getString(this.operandType), titleX, titleY);
			else
				g.drawString(LangModel.getString(this.type), titleX, titleY);
		}
	}
}
