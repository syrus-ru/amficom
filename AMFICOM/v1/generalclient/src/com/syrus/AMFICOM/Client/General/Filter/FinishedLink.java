package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.Graphics;
import java.awt.Color;
import com.syrus.AMFICOM.Client.General.Filter.LogicSchemeElement;

import com.syrus.AMFICOM.filter.*;

public class FinishedLink extends FinishedLinkBase
{
	public FinishedLink(ElementsActiveZone az1, ElementsActiveZone az2)
	{
		super(az1, az2);
	}

	public void paint(Graphics g)
	{
		if (selected)
			g.setColor(Color.blue);
		else
			g.setColor(Color.black);
	
		g.drawLine(az1.owner.x + az1.x + az1.size / 2,
				az1.owner.y + az1.y + az1.size / 2,
				az2.owner.x + az2.x + az2.size / 2,
				az2.owner.y + az2.y + az2.size / 2);
	}
}
