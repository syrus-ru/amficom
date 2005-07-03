/*
 * $Id: BlockPortCell.java,v 1.2 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.Map;

import javax.swing.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class BlockPortCell  extends DefaultGraphCell {
	private Identifier schemePortId;
	private boolean isCablePort = false;
	
	public static BlockPortCell createInstance(Object userObject,
			Rectangle bounds, Map viewMap, AbstractSchemePort port) {
		
		BlockPortCell cell = new BlockPortCell(userObject);
		cell.setAbstractSchemePortId(port.getId());
		cell.isCablePort = port instanceof SchemeCablePort;
		
		UIDefaults defaults = UIManager.getDefaults();
		Font f = defaults.getFont("Label.font"); //$NON-NLS-1$
		if (f == null)
			f = new Font("Dialog", Font.PLAIN, 12); //$NON-NLS-1$
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setBackground(map, new Color(220, 255, 255));
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setFontName(map, f.getName());
		GraphConstants.setFontSize(map, f.getSize() - 2);
		GraphConstants.setFontStyle(map, f.getStyle());
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);
		
		return cell;
	}
	
	private BlockPortCell(Object userObject) {
		super(userObject);
	}

	public boolean isCablePort() {
		return isCablePort;
	}

	public AbstractSchemePort getAbstractSchemePort() {
		try {
			return (AbstractSchemePort) SchemeStorableObjectPool.getStorableObject(
					schemePortId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
		}
		return null;
	}

	public Identifier getAbstractSchemePortId() {
		return schemePortId;
	}

	/**
	 * used for clone scheme
	 * @param portId
	 */
	public void setAbstractSchemePortId(Identifier portId) {
		this.schemePortId = portId;
	}
}

