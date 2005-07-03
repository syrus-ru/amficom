/*
 * $Id: FinishedLinkBase.java,v 1.2 2005/04/13 19:09:41 arseniy Exp $
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
public class FinishedLinkBase extends ProSchemeElementBase
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter link";

	public ElementsActiveZoneBase az1 = null;
	public ElementsActiveZoneBase az2 = null;

	public FinishedLinkBase(ElementsActiveZoneBase az1, ElementsActiveZoneBase az2)
	{
		this.az1 = az1;
		this.az2 = az2;
	}

	public String getTyp()
	{
		return TYP;
	}

	public boolean tryToSelect(int xs, int ys)
	{
		int x1 = this.az1.owner.x + this.az1.x + this.az1.size / 2;
		int y1 = this.az1.owner.y + this.az1.y + this.az1.size / 2;
		int x2 = this.az2.owner.x + this.az2.x + this.az2.size / 2;
		int y2 = this.az2.owner.y + this.az2.y + this.az2.size / 2;

// Проверяем лежит ли точка между двумя заданными
		if ((((xs - x1) * (xs - x2)) <= 0) &&
			(((ys - y1) * (ys - y2)) <= 0))
		{
			if ((y1 - 2 <= ys) &&
				(ys <= y1 + 2) &&
				(y2 - 2 <= ys) &&
				(ys <= y2 + 2))
				return true;

			if ((x1 - 2 <= xs) &&
				(xs <= x1 + 2) &&
				(x2 - 2 <= xs) &&
				(xs <= x2 + 2))
				return true;

			if (y1 == y2)
				return false;

			if (x1 == x2)
				return false;

			float val1 = (float) (xs - x1) / (x2 - x1);
			float val2 = (float) (ys - y1) / (y2 - y1);

			if (Math.abs(val1 - val2) < 0.2)
				return true;
		}

		return false;
	}
}
