package com.syrus.AMFICOM.filter;

import java.util.*;

public class ElementsActiveZone_yo extends ProSchemeElement_yo
{
	public static String typ = "Filter sensor zone";

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
		return typ;
	}

	public void addLink(FinishedLink_yo fl)
	{
		this.links.add(fl);
	}

	public void removeLink(FinishedLink_yo fl)
	{
		this.links.remove(fl);
	}

	public LinkedList getLinks()
	{
		return this.links;
	}
}