/*
 * $Id: FinishedLinkBase.java,v 1.1 2004/06/17 10:23:05 krupenn Exp $
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
public class FinishedLinkBase extends ProSchemeElementBase
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter link";

	/**
	 * Value: {@value}
	 * @deprecated Use {@link #TYP} instead.
	 */
	public static final String typ = TYP;

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

	public boolean tryToSelect(int x, int y)
	{
		int x1 = az1.owner.x + az1.x + az1.size / 2;
		int y1 = az1.owner.y + az1.y + az1.size / 2;
		int x2 = az2.owner.x + az2.x + az2.size / 2;
		int y2 = az2.owner.y + az2.y + az2.size / 2;

// Проверяем лежит ли точка между двумя заданными
		if ((((x - x1) * (x - x2)) <= 0) &&
			(((y - y1) * (y - y2)) <= 0))
		{
			if ((y1 - 2 <= y) &&
				(y <= y1 + 2) &&
				(y2 - 2 <= y) &&
				(y <= y2 + 2))
				return true;

			if ((x1 - 2 <= x) &&
				(x <= x1 + 2) &&
				(x2 - 2 <= x) &&
				(x <= x2 + 2))
				return true;

			if (y1 == y2)
				return false;

			if (x1 == x2)
				return false;

			float val1 = (float) (x - x1) / (x2 - x1);
			float val2 = (float) (y - y1) / (y2 - y1);

			if (Math.abs(val1 - val2) < 0.2)
				return true;
		}

		return false;
	}
}
