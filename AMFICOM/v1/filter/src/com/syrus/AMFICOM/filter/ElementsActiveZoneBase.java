/*-
 * $Id: ElementsActiveZoneBase.java,v 1.6 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
 */
public class ElementsActiveZoneBase extends ProSchemeElementBase {
	/**
	 * Value: {@value}
	 */
	public static final String TYP = "Filter sensor zone";

	public static String ztIn = "in";
	public static String ztOut = "out";

	public LogicSchemeElementBase owner = null;
	private List<FinishedLinkBase> links = new ArrayList<FinishedLinkBase>();

	public String zoneType = "";

	public int size = 0;

	public ElementsActiveZoneBase(LogicSchemeElementBase owner, String zoneType, int size, int x, int y) {
		this.owner = owner;
		this.zoneType = zoneType;
		this.size = size;
		this.x = x;
		this.y = y;
	}

	@Override
	public String getTyp() {
		return TYP;
	}

	public void addLink(FinishedLinkBase fl) {
		this.links.add(fl);
	}

	public void removeLink(FinishedLinkBase fl) {
		this.links.remove(fl);
	}

	public List<FinishedLinkBase> getLinks() {
		return this.links;
	}
}
