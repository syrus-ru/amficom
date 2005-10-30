/*-
 * $Id: TopLevelElement.java,v 1.2 2005/10/30 14:49:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class TopLevelElement extends DefaultGraphCell {
	private static final long serialVersionUID = 8577742124097035339L;
	private Identifier schemeId;
	
	public static TopLevelElement createInstance(Object userObject, Rectangle bounds,
			Map viewMap, Identifier schemeId) {

		Object obj = (userObject instanceof String) ? userObject : ""; //$NON-NLS-1$
		TopLevelElement cell = new TopLevelElement(obj);
		
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
		
		cell.schemeId = schemeId;
		return cell;
	}
	
	private TopLevelElement(Object userObject) {
		super(userObject);
	}
	
	public Identifier getSchemeId() {
		return this.schemeId;
	}
	
	public void setSchemeId(Identifier schemeId) {
		this.schemeId = schemeId;
	}
	
	public Scheme getScheme() {
		try {
			return (Scheme)StorableObjectPool.getStorableObject(this.schemeId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return null;
	}
}
