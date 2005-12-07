/*-
 * $Id: MapFeature.java,v 1.8 2005/12/07 17:17:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.map.corba.IdlMapFeature;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/12/07 17:17:18 $
 * @module map
 */
public class MapFeature implements IdlTransferableObject<IdlMapFeature> {
	private static final long serialVersionUID = 6461287614714505638L;

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

	/**
	 * @param orb
	 * @see IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlMapFeature getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}
	
	public IdlMapFeature getIdlTransferable() {
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
