/*-
 * $Id: SiteNodeTypeSort.java,v 1.3 2005/05/18 11:48:20 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;


/**
 * Тип сетевого узла топологической схемы. Существует несколько
 * предустановленных  типов сетевых узлов, которые определяются полем
 * {@link #codename}, соответствующим какому-либо значению {@link #WELL},
 * {@link #PIQUET}, {@link #ATS}, {@link #BUILDING}, {@link #UNBOUND},
 * {@link #CABLE_INLET}, {@link #TOWER}
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 11:48:20 $
 * @module map_v1
 */
public class SiteNodeTypeSort {

	public static final String _WELL = "well";
	public static final String _PIQUET = "piquet";
	public static final String _ATS = "ats";
	public static final String _BUILDING = "building";
	public static final String _UNBOUND = "unbound";
	public static final String _CABLE_INLET = "cableinlet";
	public static final String _TOWER = "tower";

	public static final SiteNodeTypeSort WELL = new SiteNodeTypeSort(_WELL);
	public static final SiteNodeTypeSort PIQUET = new SiteNodeTypeSort(_PIQUET);
	public static final SiteNodeTypeSort ATS = new SiteNodeTypeSort(_ATS);
	public static final SiteNodeTypeSort BUILDING = new SiteNodeTypeSort(_BUILDING);
	public static final SiteNodeTypeSort UNBOUND = new SiteNodeTypeSort(_UNBOUND);
	public static final SiteNodeTypeSort CABLE_INLET = new SiteNodeTypeSort(_CABLE_INLET);
	public static final SiteNodeTypeSort TOWER = new SiteNodeTypeSort(_TOWER);

	private String codename;

	private SiteNodeTypeSort(
			String codename) {
		this.codename = codename;
	}

	public String value() {
		return this.codename;
	}
	
	public static SiteNodeTypeSort fromString(String codename) {
		if(codename.equals(_WELL))
			return WELL;
		if(codename.equals(_PIQUET))
			return PIQUET;
		if(codename.equals(_ATS))
			return ATS;
		if(codename.equals(_BUILDING))
			return BUILDING;
		if(codename.equals(_UNBOUND))
			return UNBOUND;
		if(codename.equals(_CABLE_INLET))
			return CABLE_INLET;
		if(codename.equals(_TOWER))
			return TOWER;
		return null;
	}
}
