/*
 * $Id: ElementsActiveZoneBase.java,v 1.2 2004/08/24 12:52:08 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/24 12:52:08 $
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
	private List links = new ArrayList();

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

	public List getLinks()
	{
		return links;
	}
}
