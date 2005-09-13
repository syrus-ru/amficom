/*
 * $Id: PortCell.java,v 1.11 2005/09/13 10:19:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/09/13 10:19:05 $
 * @module schemeclient
 */

public class PortCell extends EllipseCell {
	private static final long serialVersionUID = 3256438093014775864L;

	private Identifier schemePortId;

	public static PortCell createInstance(Object userObject,
			Rectangle bounds, Map viewMap, IdlDirectionType direction, Color color) {

		PortCell cell = new PortCell(userObject);
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort dp = new DefaultPort("Center"); //$NON-NLS-1$
		map = GraphConstants.createMap();
		GraphConstants.setOffset(map, new Point(direction == IdlDirectionType._IN ? 0 : u, u / 2));
		viewMap.put(dp, map);
		cell.add(dp);
		
		return cell;
	}

	private PortCell(Object userObject) {
		super(userObject);
	}

	public SchemePort getSchemePort() {
		try {
			return (SchemePort) StorableObjectPool.getStorableObject(this.schemePortId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getSchemePortId() {
		return this.schemePortId;
	}

	public void setSchemePortId(Identifier portId) {
		assert portId != null;
		this.schemePortId = portId;
	}
}
