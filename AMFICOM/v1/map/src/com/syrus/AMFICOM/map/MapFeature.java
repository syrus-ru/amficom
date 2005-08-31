/*-
 * $Id: MapFeature.java,v 1.6 2005/08/31 05:50:36 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import com.syrus.AMFICOM.map.corba.IdlMapFeature;

/**
 * 
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/08/31 05:50:36 $
 * @module map
 */

public class MapFeature {
	
	private double centerX;
	private double centerY;
	
	private String name;
	
	public MapFeature (final double centerX, final double centerY, final String name) {
		assert name != null : NON_NULL_EXPECTED;
		this.centerX = centerX;
		this.centerY = centerY;
		this.name = name;
	}
	
	MapFeature (final IdlMapFeature ft) {
		this.centerX = ft.centerX;
		this.centerY = ft.centerY;
		this.name = ft.name;
	}
	
	public IdlMapFeature getTransferable() {
		return new IdlMapFeature(this.centerX, this.centerY, this.name);
	}

	public double getCenterX() {
		return this.centerX;
	}

	public double getCenterY() {
		return this.centerY;
	}

	public String getName() {
		return this.name;
	}

}
