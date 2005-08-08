/*
 * $Id: DeviceCell.java,v 1.7 2005/08/08 11:58:07 arseniy Exp $
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

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class DeviceCell extends DefaultGraphCell {
	private static final long serialVersionUID = 3257008739499652403L;

	private Identifier schemeDeviceId;

	public static DeviceCell createInstance(Object userObject, Rectangle bounds,
			Map viewMap) {

		Object obj = (userObject instanceof String) ? userObject : ""; //$NON-NLS-1$
		DeviceCell cell = new DeviceCell(obj);
		
		Map map = GraphConstants.createMap();
		if (userObject instanceof ImageIcon)
			GraphConstants.setIcon(map, (ImageIcon) userObject);
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setBackground(map, Color.WHITE);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);

		// Create Ports
		DefaultPort port = new DefaultPort("Center"); //$NON-NLS-1$
		cell.add(port);
		
		return cell;
	}
	
	private DeviceCell(Object userObject) {
		super(userObject);
	}

	public SchemeDevice getSchemeDevice() {
		try {
			return (SchemeDevice) StorableObjectPool.getStorableObject(this.schemeDeviceId, true);
		} catch (ApplicationException ex) {
			Log.errorException(ex);
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
}
