package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.*;
import javax.swing.*;
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

		g.drawRect(this.x, this.y, this.width, this.height);
		g.setColor(Color.pink);
		g.fillRect(this.x + 1, this.y + 1, this.width - 1, this.height - 1);

		g.setColor(Color.black);
		int titleX = this.x + 15;
		int titleY = this.y + 15;

		if (this.filterExpression != null)
			g.drawString(LangModel.String(this.type) + ":" + this.filterExpression.getListID(), titleX, titleY);
		else
		{
			if (this.operandType != "")
				g.drawString(LangModel.String(this.type) + ":" + LangModel.String(this.operandType), titleX, titleY);
			else
				g.drawString(LangModel.String(this.type), titleX, titleY);
		}
	}
}
