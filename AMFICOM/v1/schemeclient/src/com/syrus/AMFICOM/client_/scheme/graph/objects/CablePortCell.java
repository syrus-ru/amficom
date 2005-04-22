/*
 * $Id: CablePortCell.java,v 1.2 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.Map;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class CablePortCell extends DefaultGraphCell {
	private Identifier schemeCableportId;

	public static CablePortCell createInstance(Object userObject,
			Rectangle bounds, Map viewMap, AbstractSchemePortDirectionType direction) {
		CablePortCell cell = new CablePortCell(userObject);
		
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

		GraphConstants.setOffset(map, new Point(direction.equals(AbstractSchemePortDirectionType._IN) ? 0 : u, u / 2));
//		GraphConstants.setOffset(map, new Point(u / 2, u / 2));
		viewMap.put(dp, map);
		cell.add(dp);
		
		return cell;
	}
	
	private CablePortCell(Object userObject) {
		super(userObject);
	}

	public SchemeCablePort getSchemeCablePort() {
		try {
			return (SchemeCablePort) SchemeStorableObjectPool.getStorableObject(schemeCableportId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getSchemeCablePortId() {
		return schemeCableportId;
	}
	
	/**
	 * used for clone scheme
	 * @param portId
	 */
	public void setSchemeCablePortId(Identifier portId) {
		this.schemeCableportId = portId;
	}
}
