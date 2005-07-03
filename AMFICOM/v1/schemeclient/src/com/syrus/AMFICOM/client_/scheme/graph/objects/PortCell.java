/*
 * $Id: PortCell.java,v 1.3 2005/06/24 14:13:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.Map;

import com.jgraph.graph.*;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/24 14:13:36 $
 * @module schemeclient_v1
 */

public class PortCell extends EllipseCell {
	private Identifier schemePortId;

	public static PortCell createInstance(Object userObject,
			Rectangle bounds, Map viewMap, DirectionType direction) {
		PortCell cell = new PortCell(userObject);
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, Color.RED);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort dp = new DefaultPort("Center"); //$NON-NLS-1$
		map = GraphConstants.createMap();
		GraphConstants.setOffset(map, new Point(direction.equals(DirectionType._IN) ? 0 : u, u / 2));
		viewMap.put(dp, map);
		cell.add(dp);
		
		return cell;
	}

	private PortCell(Object userObject) {
		super(userObject);
	}

	public SchemePort getSchemePort() {
		try {
			return (SchemePort) SchemeStorableObjectPool.getStorableObject(schemePortId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getSchemePortId() {
		return schemePortId;
	}

	public void setSchemePortId(Identifier portId) {
		schemePortId = portId;
	}
}
