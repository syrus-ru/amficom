/*-
 * $Id: PhysicalLinkTypeSort.java,v 1.2 2005/05/11 06:33:43 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;


/**
 * Тип линии топологической схемы. Существует несколько предустановленных 
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #TUNNEL}, {@link #COLLECTOR}, {@link #INDOOR}, 
 * {@link #SUBMARINE}, {@link #OVERHEAD}, {@link #UNBOUND}
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/05/11 06:33:43 $
 * @module map_v1
 */
public class PhysicalLinkTypeSort {

	/** тоннель */
	public static final String _TUNNEL = "tunnel";
	/** участок коллектора */
	public static final String _COLLECTOR = "collector";
	/** внутренняя проводка */
	public static final String _INDOOR = "indoor";
	/** подводная линия */
	public static final String _SUBMARINE = "submarine";
	/** навесная линия */
	public static final String _OVERHEAD = "overhead";
	/** непривязанный кабель */
	public static final String _UNBOUND = "cable";

	public static final PhysicalLinkTypeSort TUNNEL = new PhysicalLinkTypeSort(_TUNNEL);
	/** участок коллектора */
	public static final PhysicalLinkTypeSort COLLECTOR = new PhysicalLinkTypeSort(_COLLECTOR);
	/** внутренняя проводка */
	public static final PhysicalLinkTypeSort INDOOR = new PhysicalLinkTypeSort(_INDOOR);
	/** подводная линия */
	public static final PhysicalLinkTypeSort SUBMARINE = new PhysicalLinkTypeSort(_SUBMARINE);
	/** навесная линия */
	public static final PhysicalLinkTypeSort OVERHEAD = new PhysicalLinkTypeSort(_OVERHEAD);
	/** непривязанный кабель */
	public static final PhysicalLinkTypeSort UNBOUND = new PhysicalLinkTypeSort(_UNBOUND);
	private String codename;

	PhysicalLinkTypeSort(
			String codename) {
		this.codename = codename;
	}

	public String value() {
		return this.codename;
	}
	
	public static PhysicalLinkTypeSort fromString(String codename) {
		if(codename.equals(_TUNNEL))
			return TUNNEL;
		if(codename.equals(_COLLECTOR))
			return COLLECTOR;
		if(codename.equals(_INDOOR))
			return INDOOR;
		if(codename.equals(_SUBMARINE))
			return SUBMARINE;
		if(codename.equals(_OVERHEAD))
			return OVERHEAD;
		if(codename.equals(_UNBOUND))
			return UNBOUND;
		return null;
	}
}
