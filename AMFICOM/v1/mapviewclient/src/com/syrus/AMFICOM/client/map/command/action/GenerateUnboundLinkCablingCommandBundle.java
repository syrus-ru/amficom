/*-
 * $$Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.37 2005/10/30 14:48:55 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  оманда генерации тоннел€ по неприв€занной линии.
 * 
 * @version $Revision: 1.37 $, $Date: 2005/10/30 14:48:55 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class GenerateUnboundLinkCablingCommandBundle extends
		MapActionCommandBundle {
	/**
	 * кабельный путь
	 */
	CablePath cablePath;

	/**
	 * неприв€занна€ лини€
	 */
	UnboundLink unbound;

	/**
	 * созданный тоннель
	 */
	PhysicalLink link;

	/**
	 *  арта, на которой производитс€ операци€
	 */
	MapView mapView;

	Map map;

	public GenerateUnboundLinkCablingCommandBundle(UnboundLink unbound) {
		this.unbound = unbound;
		this.cablePath = unbound.getCablePath();
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
			+ "generate cabling for unbound link " //$NON-NLS-1$
			+ " (" + this.unbound.getId() + ") " //$NON-NLS-1$ //$NON-NLS-2$
			+ "in cable path " //$NON-NLS-1$
			+ this.cablePath.getName() 
			+ " (" + this.cablePath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();

		final AbstractNode startNode = this.unbound.getStartNode();
		final AbstractNode endNode = this.unbound.getEndNode();
		if (startNode instanceof UnboundNode
				|| endNode instanceof UnboundNode) {
			setResult(Command.RESULT_NO);
			return;
		}
		
		try {
			this.link = super.createPhysicalLink(
					startNode, 
					endNode);
			// перенести фрагменты линии в сгенерированный тоннель
			for(Iterator it2 = new LinkedList(this.unbound.getNodeLinks()).iterator(); it2.hasNext();) {
				NodeLink tmpNodeLink = (NodeLink)it2.next();
				this.unbound.removeNodeLink(tmpNodeLink);
				tmpNodeLink.setPhysicalLink(this.link);
				this.link.addNodeLink(tmpNodeLink);
				this.mapView.removeUnboundNodeLink(tmpNodeLink);
				this.map.addNodeLink(tmpNodeLink);
			}

			CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(this.unbound);
			CableChannelingItem newCableChannelingItem = 
				CableController.generateCCI(
						this.cablePath, 
						this.link,
						cableChannelingItem.getStartSiteNode(),
						cableChannelingItem.getEndSiteNode());
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);
			this.cablePath.removeLink(cableChannelingItem);
			this.cablePath.addLink(this.link, newCableChannelingItem);

			super.removePhysicalLink(this.unbound);
			this.link.getBinding().add(this.cablePath);
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}

}

