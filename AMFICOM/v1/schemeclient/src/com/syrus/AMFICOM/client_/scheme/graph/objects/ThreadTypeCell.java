/*
 * $Id: ThreadTypeCell.java,v 1.6 2006/06/01 14:30:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Map;

import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2006/06/01 14:30:40 $
 * @module schemeclient
 */

public class ThreadTypeCell extends EllipseCell implements IdentifiableCell {
	private static final long serialVersionUID = 3905527090152224055L;

	private CableThreadType threadType;

	public static ThreadTypeCell createInstance(Object userObject, Rectangle bounds,
			Color color, Map viewMap, CableThreadType threadType) {
		
		ThreadTypeCell cell = new ThreadTypeCell(userObject);
		cell.setCableThreadType(threadType);
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);
		
		return cell;
	}

	private ThreadTypeCell(Object userObject) {
		super(userObject);
	}

	public CableThreadType getCableThreadType() {
		return this.threadType;
	}

	public void setCableThreadType(CableThreadType threadType) {
		this.threadType = threadType;
	}

	public void setId(Identifier id) {
		try {
			this.threadType = StorableObjectPool.getStorableObject(id, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	public Identifier getId() {
		return this.threadType.getId();
	}
}

