/*
 * $Id: BlockPortCell.java,v 1.7 2005/08/05 18:44:38 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/05 18:44:38 $
 * @module schemeclient_v1
 */

public class BlockPortCell  extends DefaultGraphCell {
	private static final long serialVersionUID = 4051325651938522933L;

	private Identifier schemePortId;
	private boolean isCablePort = false;
	
	public static BlockPortCell createInstance(Object userObject,
			Rectangle bounds, Map<Object, Map> viewMap, AbstractSchemePort port) {
		
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
		return this.isCablePort;
	}

	public AbstractSchemePort getAbstractSchemePort() {
		try {
			return (AbstractSchemePort) StorableObjectPool.getStorableObject(this.schemePortId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getAbstractSchemePortId() {
		return this.schemePortId;
	}

	/**
	 * used for clone scheme
	 * @param portId
	 */
	public void setAbstractSchemePortId(Identifier portId) {
		assert portId != null;
		this.schemePortId = portId;
	}
}

