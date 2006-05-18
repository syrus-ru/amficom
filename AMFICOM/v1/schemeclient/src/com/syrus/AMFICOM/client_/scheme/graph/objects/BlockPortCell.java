/*
 * $Id: BlockPortCell.java,v 1.14 2005/10/31 12:30:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Map;

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
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/10/31 12:30:29 $
 * @module schemeclient
 */

public class BlockPortCell  extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = 4051325651938522933L;

	private Identifier schemePortId;
	private boolean isCablePort = false;
	
	public static BlockPortCell createInstance(Object userObject,
			Rectangle bounds, Map<Object, Map> viewMap, AbstractSchemePort port) {
		
		BlockPortCell cell = new BlockPortCell(userObject);
		cell.setAbstractSchemePortId(port.getId());
		cell.isCablePort = port instanceof SchemeCablePort;
		
//		UIDefaults defaults = UIManager.getDefaults();
//		Font f = defaults.getFont("Label.font"); //$NON-NLS-1$
//		if (f == null)
		Font f = new Font("Dialog", Font.PLAIN, 12); //$NON-NLS-1$
		
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
			Log.errorMessage(e);
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

	public Identifier getId() {
		return this.schemePortId;
	}

	public void setId(Identifier id) {
		assert id != null;
		this.schemePortId = id;
	}
}
