/*-
 * $$Id: LinkPopupMenu.java,v 1.33 2006/06/08 12:32:53 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.action.CreateMarkCommandAtomic;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LinkPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 201932651602063281L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();

	private PhysicalLink link;

	private static LinkPopupMenu instance;

	private LinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static LinkPopupMenu getInstance() {
		if (instance == null) {
			instance  = new LinkPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (PhysicalLink) me;

		final boolean editable = isEditable();
		
		Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
		
		this.addToCollectorMenuItem.setVisible(editable && collector == null);
		this.newCollectorMenuItem.setVisible(editable && collector == null);
		this.removeCollectorMenuItem.setVisible(editable && collector != null);
		this.removeFromCollectorMenuItem.setVisible(editable && collector != null);
		
		this.addMarkMenuItem.setVisible(editable);
		
		if(collector != null) {
			this.removeCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR)
					+ " (" + collector.getName() + ")");  //$NON-NLS-1$//$NON-NLS-2$
			this.removeFromCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR)
					+ " (" + collector.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLink();
			}
		});
		
		this.addMarkMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_MARK));
		this.addMarkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMark();
			}
		});
		
		this.newCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_CREATE_COLLECTOR));
		this.newCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					newCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.removeCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR));
		this.removeCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.addToCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_TO_COLLECTOR));
		this.addToCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addToCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.removeFromCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR));
		this.removeFromCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeFromCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.add(this.removeMenuItem);
		this.add(this.addMarkMenuItem);
		// this.addSeparator();
		this.add(this.addToCollectorMenuItem);
		this.add(this.removeFromCollectorMenuItem);
		// this.addSeparator();
		this.add(this.newCollectorMenuItem);
		this.add(this.removeCollectorMenuItem);
	}

	void removeLink() {
		if (confirmDelete()) {
			super.removeMapElement(this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addMark() {
		CreateMarkCommandAtomic command = new CreateMarkCommandAtomic(
				this.link,
				this.point);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void newCollector() throws ApplicationException {
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() throws ApplicationException {
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() throws ApplicationException {
		Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			super.removeLinkFromCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeCollector() throws ApplicationException {
		this.netMapViewer.getLogicalNetLayer().deselectAll();

		Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			Set<PhysicalLink> set = new HashSet<PhysicalLink>();
			set.addAll(collector.getPhysicalLinks());
			super.removeLinksFromCollector(collector, set);
			super.removeCollector(collector);
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
