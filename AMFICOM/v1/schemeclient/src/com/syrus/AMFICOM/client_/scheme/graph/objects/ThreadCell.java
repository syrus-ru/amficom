/*
 * $Id: ThreadCell.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeCableThread;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class ThreadCell extends EllipseCell {
	private static final long serialVersionUID = 3256719572169143093L;

	private SchemeCableThread thread;

	public static ThreadCell createInstance(Object userObject, Rectangle bounds,
			Color color, Map viewMap, SchemeCableThread thread) {
		
		ThreadCell cell = new ThreadCell(userObject);
		cell.setSchemeCableThread(thread);
		
		Map map = GraphConstants.createMap();
		GraphConstants.setBounds(map, bounds);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setSizeable(map, false);
		GraphConstants.setBackground(map, color);
		GraphConstants.setBorderColor(map, Constants.COLOR_BORDER);
		viewMap.put(cell, map);
		
		return cell;
	}

	private ThreadCell(Object userObject) {
		super(userObject);
	}

	public SchemeCableThread getSchemeCableThread() {
		return this.thread;
	}

	public void setSchemeCableThread(SchemeCableThread thread) {
		this.thread = thread;
	}
}
