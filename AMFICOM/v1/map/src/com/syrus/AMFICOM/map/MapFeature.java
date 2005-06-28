/*-
 * $Id: MapFeature.java,v 1.2 2005/06/28 10:06:44 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.map.corba.IdlMapFeature;

public class MapFeature {
	
	private double centerX;
	private double centerY;
	
	private String name;
	
	public MapFeature (double centerX, double centerY, String name) {
		assert name != null : ErrorMessages.NON_NULL_EXPECTED;
		this.centerX = centerX;
		this.centerY = centerY;
		this.name = name;
	}
	
	MapFeature (IdlMapFeature ft) {
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
