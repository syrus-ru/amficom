/*
 * $Id: DeviceCell.java,v 1.10 2005/10/30 14:49:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.ImageIcon;

import com.jgraph.graph.CellMapper;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/10/30 14:49:21 $
 * @module schemeclient
 */

public class DeviceCell extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = 3257008739499652403L;
	public static final int SQUARED = 0;
	public static final int ROUNDED = 1;

	private Identifier schemeDeviceId;
	private int kind = SQUARED;

	public static DeviceCell createInstance(Object userObject, Rectangle bounds,
			Map viewMap) {
		return createInstance(userObject, bounds, viewMap, Color.WHITE);
	}
	
	public static DeviceCell createInstance(Object userObject, Rectangle bounds,
			Map viewMap, Color color) {

		Object obj = (userObject instanceof String) ? userObject : ""; //$NON-NLS-1$
		DeviceCell cell = new DeviceCell(obj);
		
		Map map = GraphConstants.createMap();
		if (userObject instanceof ImageIcon)
			GraphConstants.setIcon(map, (ImageIcon) userObject);
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);

		// Create Ports
		DefaultPort port = new DefaultPort("Center"); //$NON-NLS-1$
		cell.add(port);
		
		return cell;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public VertexView getView(SchemeGraph graph, CellMapper cm) {
		if (this.kind == SQUARED) {
			return new DeviceView(this, graph, cm);	
		}
		return new SchemeEllipseView(this, graph, cm);
		
	}
	
	private DeviceCell(Object userObject) {
		super(userObject);
	}

	public SchemeDevice getSchemeDevice() {
		try {
			return (SchemeDevice) StorableObjectPool.getStorableObject(this.schemeDeviceId, true);
		} catch (ApplicationException ex) {
			Log.errorMessage(ex);
			return null;
		}
	}

	public Identifier getSchemeDeviceId() {
		return this.schemeDeviceId;
	}

	public void setSchemeDeviceId(Identifier id) {
		assert id != null;
		this.schemeDeviceId = id;
	}

	public void setId(Identifier id) {
		assert id != null;
		this.schemeDeviceId = id;
	}

	public Identifier getId() {
		return this.schemeDeviceId;
	}
}
