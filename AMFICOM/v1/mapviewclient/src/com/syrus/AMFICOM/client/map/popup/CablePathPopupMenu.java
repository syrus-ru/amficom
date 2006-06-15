/*-
 * $$Id: CablePathPopupMenu.java,v 1.32 2006/06/15 06:39:17 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedSet;

import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.command.action.CreateUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.32 $, $Date: 2006/06/15 06:39:17 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CablePathPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 7578207099905441307L;
	private JMenuItem removeMenuItem = new JMenuItem();

	// убрано - читай 'xxx' ниже по тексту
//	private JMenuItem generateMenuItem = new JMenuItem();

//	private JMenu unbindCableMenuItem = new JMenu();
	private JMenuItem clearBindingMenuItem = new JMenuItem();
	
	private CablePath cablePath;

	private static CablePathPopupMenu instance;

	private CablePathPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static CablePathPopupMenu getInstance() {
		if (instance == null) {
			instance = new CablePathPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object object) {
		this.cablePath = (CablePath) object;

		final boolean editable = isEditable();		
		this.removeMenuItem.setVisible(editable);
		
//		boolean canGenerate = false;
//		if (editable) {
//			try {
//				for(PhysicalLink link : this.cablePath.getLinks()) {
//					if(link instanceof UnboundLink) {
//						canGenerate = true;
//						break;
//					}
//				}
//			} catch(ApplicationException e) {
//				Log.errorMessage(e);
//			}
//		}
//		this.generateMenuItem.setVisible(canGenerate);
		
		this.clearBindingMenuItem.setVisible(editable);
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeCablePath();
			}
		});
//		this.generateMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_GENERATE_CABLING));
//		this.generateMenuItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				generateCabling();
//			}
//		});
		this.clearBindingMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_CLEAR_BINDING));
		this.clearBindingMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearBinding();
			}
		});
		this.add(this.removeMenuItem);
//		this.add(this.generateMenuItem);
		this.add(this.clearBindingMenuItem);
	}

	void removeCablePath() {
		if (confirmDelete()) {
			super.removeMapElement(this.cablePath);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	//XXX я ваще не въехал зачем это надо. Ётот метод генерирует тоннель под кабелем.
	//»ћ’ќ пользователь должен рисовать тоннели ручками а в них уже вкладывать кабель
	//это подразумеваетс€ логикой работы с системой.
	//к тому же этот генератор глючит - с созданным объектом нельз€ работать как с 
	//PhysicalLink, со всеми вытекающими - к нему не прив€зываетс€ кабель и т.д. 
	//Stas
	
//	void generateCabling() {
//		SiteNodeType proto = super.selectSiteNodeType();
//		if(proto != null) {
//			super.generatePathCabling(this.cablePath, proto);
//			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
//		}
//	}
	
	void clearBinding() {
		final SchemeCableLink schemeCableLink = this.cablePath.getSchemeCableLink();
		UnPlaceSchemeCableLinkCommand command1 = new UnPlaceSchemeCableLinkCommand(this.cablePath);
		command1.setNetMapViewer(this.netMapViewer);
		command1.execute();
		
		try {
			final SortedSet<CableChannelingItem> pathMembers = schemeCableLink.getPathMembers();
			if (!pathMembers.isEmpty()) {
				pathMembers.first().setParentPathOwner(null, true);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		
		final PlaceSchemeCableLinkCommand command2 = new PlaceSchemeCableLinkCommand(schemeCableLink);
		command2.setNetMapViewer(this.netMapViewer);
		command2.execute();

		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	// TODO ќтв€зывать cablePath от определенного линка, если будет найден способ
	// узнать в какой линк пользователь ткнул - пока что можно отв€зать только 
	// cablePath целиком 
	/*
	void removeBinding(CablePath cablePath) throws ApplicationException {
		CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.link);
		
		// replace binding to physical link with unbound link
		CreateUnboundLinkCommandBundle command = new CreateUnboundLinkCommandBundle(
				this.link.getStartNode(),
				this.link.getEndNode());
		command.setNetMapViewer(this.netMapViewer);
		command.execute();
		
		UnboundLink unbound = command.getUnbound();
		unbound.setCablePath(cablePath);
		
		CableChannelingItem newCableChannelingItem = 
			CableController.generateCCI(
					cablePath, 
					unbound,
					cableChannelingItem.getStartSiteNode(),
					cableChannelingItem.getEndSiteNode());
		newCableChannelingItem.insertSelfBefore(cableChannelingItem);
		cableChannelingItem.setParentPathOwner(null, false);
		
		cablePath.removeLink(cableChannelingItem);
		cablePath.addLink(unbound, newCableChannelingItem);
		
		this.link.getBinding().remove(cablePath);
		this.netMapViewer.getLogicalNetLayer().getCommandList().flush();

		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}
	*/
}
