/*
 * $Id: ElementsActiveZoneBase.java,v 1.4 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:37:22 $
 * @module filter
 */
public class ElementsActiveZoneBase extends ProSchemeElementBase
{
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter sensor zone";

	public static String ztIn = "in";
	public static String ztOut = "out";

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
		this.links.add(fl);
	}

	public void removeLink(FinishedLinkBase fl)
	{
		this.links.remove(fl);
	}

	public List getLinks()
	{
		return this.links;
	}
}
