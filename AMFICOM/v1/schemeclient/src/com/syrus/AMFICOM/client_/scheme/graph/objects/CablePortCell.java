/*
 * $Id: CablePortCell.java,v 1.17 2005/10/30 15:20:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2005/10/30 15:20:56 $
 * @module schemeclient
 */

public class CablePortCell extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = 4049357521913722424L;

	private Identifier schemeCableportId;

	public static CablePortCell createInstance(Object userObject,
			Rectangle bounds, Map<Object, Map> viewMap, IdlDirectionType direction, Color color) {

		CablePortCell cell = new CablePortCell(userObject);
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, color);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		
		Font f = new Font("Dialog", Font.PLAIN, 2);
		GraphConstants.setFontName(map, f.getName());
		GraphConstants.setFontSize(map, f.getSize());
		GraphConstants.setFontStyle(map, f.getStyle());
		
		viewMap.put(cell, map);

		// Create Ports
		int u = GraphConstants.PERCENT;
		DefaultPort dp = new DefaultPort("Center"); //$NON-NLS-1$
		map = GraphConstants.createMap();

		GraphConstants.setOffset(map, new Point(direction == IdlDirectionType._IN ? 0 : u, u / 2));
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
			return (SchemeCablePort)StorableObjectPool.getStorableObject(this.schemeCableportId, true);
		} catch (ApplicationException e) {
			assert Log.errorMessage(e);
			return null;
		}
	}

	public Identifier getSchemeCablePortId() {
		return this.schemeCableportId;
	}
	
	/**
	 * used for clone scheme
	 * @param portId
	 */
	public void setSchemeCablePortId(Identifier portId) {
		assert portId != null;
		this.schemeCableportId = portId;
	}

	public Identifier getId() {
		return this.schemeCableportId;
	}

	public void setId(Identifier id) {
		assert id != null;
		this.schemeCableportId = id;
	}
}
