package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import com.syrus.AMFICOM.filter.*;

public class ElementsActiveZone extends ElementsActiveZoneBase
{
	public ElementsActiveZone(
			LogicSchemeElement owner,
            String zoneType,
            int size,
            int x,
            int y)
	{
		super(owner, zoneType, size, x, y);
	}

	public void paint(Graphics g)
	{
		g.setColor(Color.yellow);

		g.fillRect(
				owner.x + this.x,
				owner.y + this.y,
				this.size,
				this.size);
	}
}