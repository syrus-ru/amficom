/*-
 * $$Id: SelectionPopupMenu.java,v 1.37 2006/06/08 12:32:53 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.37 $, $Date: 2006/06/08 12:32:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SelectionPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 905543726291925317L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem insertSiteMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();
	
	private static SelectionPopupMenu instance;
	
	Selection selection;	

	private SelectionPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	@Override
	public void setElement(Object me) {
		this.selection = (Selection)me;

		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		
		this.insertSiteMenuItem.setVisible(editable && this.selection.isPhysicalNodeSelection());
		this.generateMenuItem.setVisible(editable && this.selection.isUnboundSelection());
		this.newCollectorMenuItem.setVisible(editable && this.selection.isPhysicalLinkSelection());
		this.addToCollectorMenuItem.setVisible(editable && this.selection.isPhysicalLinkSelection());
		this.removeCollectorMenuItem.setVisible(editable && this.selection.isPhysicalLinkSelection());
		this.removeFromCollectorMenuItem.setVisible(editable && this.selection.isPhysicalLinkSelection());
	}
	
	public static SelectionPopupMenu getInstance() {
		if (instance == null) {
			instance = new SelectionPopupMenu();
		}
		return instance;
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelection();
			}
		});
		this.insertSiteMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_PLACE_SITE));
		this.insertSiteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertSite();
			}
		});
		this.generateMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_CABLING));
		this.generateMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCabling();
			}
		});
		this.newCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_CREATE_COLLECTOR));
		this.newCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					newCollector();
				} catch(MapException e1) {
					Log.errorMessage(e1);
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
				} catch(MapException e1) {
					Log.errorMessage(e1);
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
				} catch(MapException e1) {
					Log.errorMessage(e1);
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
				} catch(MapException e1) {
					Log.errorMessage(e1);
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.add(this.removeMenuItem);
		this.add(this.insertSiteMenuItem);
		this.add(this.generateMenuItem);
		//		this.addSeparator();
		this.add(this.addToCollectorMenuItem);
		this.add(this.removeFromCollectorMenuItem);
		//		this.addSeparator();
		this.add(this.newCollectorMenuItem);
		this.add(this.removeCollectorMenuItem);
	}

	void removeSelection() {
		if (confirmDelete()) {
			DeleteSelectionCommand command = new DeleteSelectionCommand();
			command.setNetMapViewer(this.netMapViewer);
			this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
			this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
			if(!command.isUndoable()) {
				this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
			}
		}
	}

	void insertSite() {
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			for(MapElement me : new LinkedList<MapElement>(this.selection.getElements())) {
				if (me instanceof TopologicalNode) {
					TopologicalNode node = (TopologicalNode)me;
					super.insertSiteInPlaceOfANode(node, proto);
				}
			}
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void generateCabling() {
		SiteNodeType proto = super.selectSiteNodeType();

		if(proto != null) {
			List<MapElement> elementsToBind = new LinkedList<MapElement>(this.selection.getElements());
			List<UnboundNode> nodesToBind = new LinkedList<UnboundNode>();
			for(Iterator<MapElement> it = elementsToBind.iterator(); it.hasNext();) {
				MapElement me = it.next();
				if(me instanceof UnboundNode) {
					nodesToBind.add((UnboundNode)me);
					it.remove();
				}
			}

			if(!nodesToBind.isEmpty()) {
				for(UnboundNode un : nodesToBind) {
					super.convertUnboundNodeToSite(un, proto);
				}
			}

			List<MapElement> alreadyBound = new LinkedList<MapElement>();
			for(MapElement me : elementsToBind) {
				if(me instanceof CablePath) {
					CablePath path = (CablePath )me;
					if(!alreadyBound.contains(path)) {
						super.generatePathCabling(path, proto);
						alreadyBound.add(path);
					}
				}
				else if(me instanceof UnboundLink) {
					CablePath path = ((UnboundLink )me).getCablePath();
					if(!alreadyBound.contains(path)) {
						super.generatePathCabling(path, proto);
						alreadyBound.add(path);
					}
				}
			}
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void newCollector() throws ApplicationException, MapDataException, MapConnectionException {
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			this.netMapViewer.repaint(false);

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() throws ApplicationException, MapConnectionException, MapDataException {
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			this.netMapViewer.repaint(false);

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() throws ApplicationException, MapConnectionException, MapDataException {
		for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			Collector collector = 
				this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(link);
			if(collector != null) {
				super.removeLinkFromCollector(collector, link);
			}
		}

		this.netMapViewer.repaint(false);
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void removeCollector() throws ApplicationException, MapConnectionException, MapDataException {
		if (confirmDelete()) {
			for(Iterator it = this.selection.getElements().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink )it.next();
				Collector collector = 
					this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(link);
				if(collector != null) {
					super.removeLinksFromCollector(collector, collector
							.getPhysicalLinks());
					super.removeCollector(collector);
				}
			}
			
			this.netMapViewer.repaint(false);
			
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
}
