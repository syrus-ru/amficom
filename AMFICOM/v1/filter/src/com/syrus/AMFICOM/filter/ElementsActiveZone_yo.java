/*
 * $Id: ElementsActiveZone_yo.java,v 1.2 2004/06/08 15:31:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.util.LinkedList;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/08 15:31:57 $
 * @module filter_v1
 */
public class ElementsActiveZone_yo extends ProSchemeElement_yo
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter sensor zone";

	/**
	 * Value: {@value}
	 * @deprecated Use {@link #TYP} instead.
	 */
	public static final String typ = TYP;

	public static String zt_in = "in";
	public static String zt_out = "out";

	public LogicSchemeElement_yo owner = null;
	private LinkedList links = new LinkedList();

	public String zoneType = "";

	public int size = 0;

	public ElementsActiveZone_yo(
			LogicSchemeElement_yo owner,
			String zoneType,
			int size,
			int x,
			int y)
	{
		this.owner = owner;
		this.zoneType = zoneType;
		this.size = size;
		this.x = x;
		this.y = y;
	}

	public String getTyp()
	{
		return TYP;
	}

	public void addLink(FinishedLink_yo fl)
	{
		links.add(fl);
	}

	public void removeLink(FinishedLink_yo fl)
	{
		links.remove(fl);
	}

	public LinkedList getLinks()
	{
		return links;
	}
}
