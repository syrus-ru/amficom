/*
 * $Id: ElementsActiveZoneBase.java,v 1.1 2004/06/17 10:23:05 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.util.LinkedList;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/17 10:23:05 $
 * @module filter_v1
 */
public class ElementsActiveZoneBase extends ProSchemeElementBase
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

	public LogicSchemeElementBase owner = null;
	private LinkedList links = new LinkedList();

	public String zoneType = "";

	public int size = 0;

	public ElementsActiveZoneBase(
			LogicSchemeElementBase owner,
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

	public void addLink(FinishedLinkBase fl)
	{
		links.add(fl);
	}

	public void removeLink(FinishedLinkBase fl)
	{
		links.remove(fl);
	}

	public LinkedList getLinks()
	{
		return links;
	}
}
