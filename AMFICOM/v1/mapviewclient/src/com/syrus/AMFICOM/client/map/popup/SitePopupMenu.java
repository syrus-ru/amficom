/*-
 * $$Id: SitePopupMenu.java,v 1.27 2006/06/13 06:45:28 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.action.MoveSchemeElementCommand;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2006/06/13 06:45:28 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class SitePopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = -773545416471282757L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem moveSEMenuItem = new JMenuItem();
	private JMenuItem attachCableInletMenuItem = new JMenuItem();

	private SiteNode site;
	private SchemeElement schemeElement;

	private static SitePopupMenu instance;

	private SitePopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static SitePopupMenu getInstance() {
		if (instance == null) {
			instance = new SitePopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.site = (SiteNode )me;
		this.schemeElement = null;
		
		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		
		final ApplicationModel aModel = this.netMapViewer.getLogicalNetLayer().getContext().getApplicationModel();
		boolean schemeActionsEnabled = aModel.isSelected(MapApplicationModel.MODE_CABLE_PATH);
		
		boolean attachable = false;
		if (editable) {
			SiteNodeTypeSort sort = this.site.getType().getSort();
			attachable = sort == SiteNodeTypeSort.ATS || sort == SiteNodeTypeSort.BUILDING;

			if (schemeActionsEnabled) {
				LinkedIdsCondition condition = new LinkedIdsCondition(this.site, ObjectEntities.SCHEMEELEMENT_CODE);
				try {
					Set<SchemeElement> schemeElements = StorableObjectPool.getStorableObjectsByCondition(condition, true);
					if (!schemeElements.isEmpty()) {
						this.schemeElement = schemeElements.iterator().next();
						this.moveSEMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_MOVE_SE) 
								+ " " + this.schemeElement.getName());
					}
				} catch (ApplicationException e) {
					e.printStackTrace();
				}
			}
		}
		this.attachCableInletMenuItem.setVisible(attachable);
		this.moveSEMenuItem.setVisible(this.schemeElement != null);
	}

	private void jbInit() {
		this.attachCableInletMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ATTACH_CABLE_INLET));
		this.attachCableInletMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					attachCableInlet();
				} catch(MapException ex) {
					Log.errorMessage(ex);
				}
			}
		});
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSite();
			}
		});
		this.moveSEMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_MOVE_SE));
		this.moveSEMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveSchemeElement();
			}
		});
		this.add(this.attachCableInletMenuItem);
		this.add(this.moveSEMenuItem);
		this.add(this.removeMenuItem);
	}

	protected void attachCableInlet() throws MapConnectionException, MapDataException {
		SiteNodeType siteNodeType = super.selectAttachedSiteNodeType();
		if(siteNodeType != null) {
			super.createAttachedSiteNode(this.site, siteNodeType);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeSite() {
		if (confirmDelete()) {
			super.removeMapElement(this.site);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void moveSchemeElement() {
		SiteNode destination = super.selectSiteNode();
		if (destination != null) {
			if (destination.equals(this.site)) {
				JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
						I18N.getString("Message.Error.source_target_equals"), //$NON-NLS-1$
						I18N.getString("Message.Error"),  //$NON-NLS-1$
						JOptionPane.OK_OPTION);
				return;
			}
			
			MoveSchemeElementCommand command = new MoveSchemeElementCommand(
					this.schemeElement, this.site, destination);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();
		}
	}
}
