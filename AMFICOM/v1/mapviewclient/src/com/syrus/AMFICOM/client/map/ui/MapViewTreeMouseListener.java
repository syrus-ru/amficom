/*-
 * $$Id: MapViewTreeMouseListener.java,v 1.7 2005/12/14 09:40:28 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * @version $Revision: 1.7 $, $Date: 2005/12/14 09:40:28 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapViewTreeMouseListener extends MouseAdapter {
	private final ApplicationContext aContext;
	private final JTree tree;

	public MapViewTreeMouseListener(JTree tree, ApplicationContext context) {
		this.tree = tree;
		this.aContext = context;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			TreePath treePath = this.tree.getPathForLocation(e.getX(), e.getY());
			if (treePath != null) {
				Item item = (Item )treePath.getLastPathComponent();
				Object object = item.getObject();
				if(object instanceof SiteNodeType
						|| object instanceof PhysicalLinkType) {
					this.tree.setSelectionPath(treePath);
					JPopupMenu popupMenu = new TreePopupMenu(object, this.aContext);
					popupMenu.show(this.tree, e.getX(), e.getY());
				}
			}
		}
		else if(SwingUtilities.isLeftMouseButton(e)) {
			if(e.getClickCount() == 2) {
				TreePath treePath = this.tree.getPathForLocation(e.getX(), e.getY());
				if(treePath == null)
					return;
				Item item = (Item )treePath.getLastPathComponent();
				Object object = item.getObject();
				if(object instanceof MapElement) {
					this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.DO_SELECT, object));		
				}
				else if(object instanceof SpatialObject) {
					this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(this, MapEvent.DO_SELECT, object));		
				}
			}
		}
	}
}