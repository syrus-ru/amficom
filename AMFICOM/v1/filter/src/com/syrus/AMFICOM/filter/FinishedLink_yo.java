package com.syrus.AMFICOM.filter;

public class FinishedLink_yo extends ProSchemeElement_yo
{
	static String typ = "Filter link";
	public ElementsActiveZone_yo az1 = null;
	public ElementsActiveZone_yo az2 = null;

	public FinishedLink_yo(ElementsActiveZone_yo az1, ElementsActiveZone_yo az2)
	{
		this.az1 = az1;
		this.az2 = az2;
	}

	public String getTyp()
	{
		return typ;
	}

	public boolean tryToSelect(int x, int y)
	{
		int x1 = az1.owner.x + az1.x + az1.size / 2;
		int y1 = az1.owner.y + az1.y + az1.size / 2;
		int x2 = az2.owner.x + az2.x + az2.size / 2;
		int y2 = az2.owner.y + az2.y + az2.size / 2;

// ѕровер€ем лежит ли точка между двум€ заданными
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