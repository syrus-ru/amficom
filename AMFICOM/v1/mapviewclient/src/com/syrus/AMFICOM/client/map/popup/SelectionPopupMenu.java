/*-
 * $$Id: SelectionPopupMenu.java,v 1.35 2006/02/15 11:12:25 stas Exp $$
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

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
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
 * @version $Revision: 1.35 $, $Date: 2006/02/15 11:12:25 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SelectionPopupMenu extends MapPopupMenu {
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem insertSiteMenuItem = new JMenuItem();
	private JMenuItem generateMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();
	
	private static SelectionPopupMenu instance = new SelectionPopupMenu();
	
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

		this.insertSiteMenuItem.setVisible(this.selection.isPhysicalNodeSelection());
		this.generateMenuItem.setVisible(this.selection.isUnboundSelection());
		this.newCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.addToCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.removeCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
		this.removeFromCollectorMenuItem.setVisible(this.selection.isPhysicalLinkSelection());
	}
	
	public static SelectionPopupMenu getInstance() {
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
					e1.printStackTrace();
					SelectionPopupMenu.this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(
							new StatusMessageEvent(
									this, 
									StatusMessageEvent.STATUS_MESSAGE, 
									MapException.DEFAULT_STRING));
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.removeCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR));
		this.removeCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeCollector();
				} catch(MapException e1) {
					e1.printStackTrace();
					SelectionPopupMenu.this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(
							new StatusMessageEvent(
									this, 
									StatusMessageEvent.STATUS_MESSAGE, 
									MapException.DEFAULT_STRING));
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.addToCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_TO_COLLECTOR));
		this.addToCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addToCollector();
				} catch(MapException e1) {
					e1.printStackTrace();
					SelectionPopupMenu.this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(
							new StatusMessageEvent(
									this, 
									StatusMessageEvent.STATUS_MESSAGE, 
									MapException.DEFAULT_STRING));
				} catch(ApplicationException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.removeFromCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR));
		this.removeFromCollectorMenuItem
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							removeFromCollector();
						} catch(MapException e1) {
							e1.printStackTrace();
							SelectionPopupMenu.this.netMapViewer.getLogicalNetLayer().getContext().getDispatcher().firePropertyChange(
									new StatusMessageEvent(
											this, 
											StatusMessageEvent.STATUS_MESSAGE, 
											MapException.DEFAULT_STRING));
						} catch(ApplicationException e1) {
							e1.printStackTrace();
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
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setNetMapViewer(this.netMapViewer);
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		if(!command.isUndoable()) {
			this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
		}
	}

	void insertSite() {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		SiteNodeType proto = super.selectSiteNodeType();
		if(proto != null) {
			for(Iterator it = new LinkedList(this.selection.getElements()).iterator(); it.hasNext();) {
				TopologicalNode node = (TopologicalNode )it.next();
				super.insertSiteInPlaceOfANode(node, proto);
			}
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void generateCabling() {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		SiteNodeType proto = super.selectSiteNodeType();

		if(proto != null) {
			List elementsToBind = new LinkedList(this.selection.getElements());
			List nodesToBind = new LinkedList();
			for(Iterator it = elementsToBind.iterator(); it.hasNext();) {
				MapElement me = (MapElement )it.next();
				if(me instanceof UnboundNode) {
					nodesToBind.add(me);
					it.remove();
				}
			}

			if(!nodesToBind.isEmpty()) {
				for(Iterator it = nodesToBind.iterator(); it.hasNext();) {
					UnboundNode un = (UnboundNode )it.next();
					super.convertUnboundNodeToSite(un, proto);
				}
			}

			List alreadyBound = new LinkedList();
			for(Iterator it = elementsToBind.iterator(); it.hasNext();) {
				MapElement me = (MapElement )it.next();
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
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			this.netMapViewer.repaint(false);

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() throws ApplicationException, MapConnectionException, MapDataException {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinksToCollector(collector, this.selection.getElements());

			this.netMapViewer.repaint(false);

			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() throws ApplicationException, MapConnectionException, MapDataException {
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
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
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
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
