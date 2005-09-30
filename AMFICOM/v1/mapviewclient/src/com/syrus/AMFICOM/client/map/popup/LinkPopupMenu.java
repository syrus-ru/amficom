/*-
 * $$Id: LinkPopupMenu.java,v 1.28 2005/09/30 16:08:39 krupenn Exp $$
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

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.action.CreateMarkCommandAtomic;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * @version $Revision: 1.28 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LinkPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();

	private PhysicalLink link;

	private static LinkPopupMenu instance = new LinkPopupMenu();

	private LinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static LinkPopupMenu getInstance() {
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (PhysicalLink) me;

		Collector collector = this.netMapViewer.getLogicalNetLayer()
				.getMapView().getMap().getCollector(this.link);
		this.addToCollectorMenuItem.setVisible(collector == null);
		this.newCollectorMenuItem.setVisible(collector == null);
		this.removeCollectorMenuItem.setVisible(collector != null);
		this.removeFromCollectorMenuItem.setVisible(collector != null);
		this.addMarkMenuItem.setVisible(this.netMapViewer.getLogicalNetLayer()
				.getContext().getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_MAP));
		if(collector != null) {
			this.removeCollectorMenuItem.setText(LangModelMap
					.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR)
					+ " (" //$NON-NLS-1$
					+ collector.getName() + ")"); //$NON-NLS-1$
			this.removeFromCollectorMenuItem
					.setText(LangModelMap
							.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR)
							+ " (" //$NON-NLS-1$
							+ collector.getName() + ")"); //$NON-NLS-1$
		}
	}

	private void jbInit() {
		this.removeMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLink();
			}
		});
		this.addMarkMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_ADD_MARK));
		this.addMarkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMark();
			}
		});
		this.newCollectorMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_CREATE_COLLECTOR));
		this.newCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					newCollector();
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.removeCollectorMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR));
		this.removeCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeCollector();
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});

		this.addToCollectorMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_ADD_TO_COLLECTOR));
		this.addToCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addToCollector();
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.removeFromCollectorMenuItem.setText(LangModelMap
				.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR));
		this.removeFromCollectorMenuItem
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							removeFromCollector();
						} catch(ApplicationException e1) {
							e1.printStackTrace();
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
		super.removeMapElement(this.link);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(
				MapEvent.MAP_CHANGED);
	}

	void addMark() {
		CreateMarkCommandAtomic command = new CreateMarkCommandAtomic(
				this.link,
				this.point);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(
				MapEvent.MAP_CHANGED);
	}

	void newCollector() throws ApplicationException {
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() throws ApplicationException {
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() throws ApplicationException {
		Collector collector = this.netMapViewer.getLogicalNetLayer()
				.getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			super.removeLinkFromCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

	void removeCollector() throws ApplicationException {
		this.netMapViewer.getLogicalNetLayer().deselectAll();

		Collector collector = this.netMapViewer.getLogicalNetLayer()
				.getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			Set set = new HashSet();
			set.addAll(collector.getPhysicalLinks());
			super.removeLinksFromCollector(collector, set);
			super.removeCollector(collector);
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(
					MapEvent.MAP_CHANGED);
		}
	}

}
