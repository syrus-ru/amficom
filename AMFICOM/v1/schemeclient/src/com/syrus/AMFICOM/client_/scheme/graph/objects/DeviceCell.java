/*
 * $Id: DeviceCell.java,v 1.1 2005/04/05 14:07:54 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.Map;

import javax.swing.ImageIcon;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:54 $
 * @module schemeclient_v1
 */

public class DeviceCell extends DefaultGraphCell {
	private Identifier schemeDeviceId;

	public static DeviceCell createInstance(Object userObject, Rectangle bounds,
			Map viewMap, SchemeDevice device) {

		Object obj = (userObject instanceof String) ? userObject : "";
		DeviceCell cell = new DeviceCell(obj);
		cell.setSchemeDeviceId(device.getId());
		
		Map map = GraphConstants.createMap();
		if (userObject instanceof ImageIcon)
			GraphConstants.setIcon(map, (ImageIcon) userObject);
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setBackground(map, Color.WHITE);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);

		// Create Ports
		DefaultPort port = new DefaultPort("Center");
		cell.add(port);
		
		return cell;
	}
	
	private DeviceCell(Object userObject) {
		super(userObject);
	}

	public SchemeDevice getSchemeDevice() {
		try {
			return (SchemeDevice) SchemeStorableObjectPool.getStorableObject(
					schemeDeviceId, true);
		} catch (Exception ex) {
			return null;
		}
	}

	public Identifier getSchemeDeviceId() {
		return schemeDeviceId;
	}

	public void setSchemeDeviceId(Identifier id) {
		schemeDeviceId = id;
	}
}
