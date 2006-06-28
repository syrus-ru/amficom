/*-
 * $$Id: GenerateCablePathCablingCommandBundle.java,v 1.47 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  оманда генерации тоннелей в соответствии с прохождением кабел€. из
 * непроложенных линий генерируютс€ тоннели и кабель прив€зываетс€ к ним. ”же
 * существующа€ прив€зка сохран€етс€. ѕо неприв€занным элементам генерируютс€
 * сетевые узла и схемные элементы прив€зываютс€ к ним.
 * 
 * @version $Revision: 1.47 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class GenerateCablePathCablingCommandBundle extends
		MapActionCommandBundle {
	/**
	 * ”дал€емый узел
	 */
	CablePath cablePath;

	/**
	 * тип узлов дл€ генерации вместо неприв€занных элементов
	 */
	SiteNodeType type;

	/**
	 *  арта, на которой производитс€ операци€
	 */
	MapView mapView;

	Map map;

	public GenerateCablePathCablingCommandBundle(
			CablePath cablePath,
			SiteNodeType proto) {
		this.cablePath = cablePath;
		this.type = proto;
	}

	@Override
	public void execute() {
		Log.debugMessage("generate cabling for cable path " //$NON-NLS-1$
					+ this.cablePath.getName() 
					+ " (" + this.cablePath.getId() + ") " //$NON-NLS-1$ //$NON-NLS-2$
					+ "using site node type " + this.type.getName()  //$NON-NLS-1$
					+ " )" + this.type.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			// дл€ последующего цикла необходима последовательность
			// узлов от начального к конечному
			SiteNode startsite = (SiteNode)this.cablePath.getStartNode();
			SiteNode endsite = null;
			// проверить, что узел €вл€етс€ сетевым узлом (если это неприв€занный
			// элемент, сгенерировать на его месте сетевой узел)
			startsite = this.checkSite(startsite);
			// отдельный список, поскольку используетс€ удаление
			for (PhysicalLink physicalLink : new LinkedList<PhysicalLink>(this.cablePath.getLinks())) {
				// перейти к следующему узлу
				final AbstractNode startNode = physicalLink.getStartNode();
				final AbstractNode endNode = physicalLink.getEndNode();
				endsite = (startsite == endNode)
						? (SiteNode) startNode
						: (SiteNode) endNode;

				// проверить, что узел €вл€етс€ сетевым узлом (если это неприв€занный
				// элемент, сгенерировать на его месте сетевой узел)
				endsite = this.checkSite(endsite);

				// если неприв€занна€ лини€, генерировать тоннель
				if (physicalLink instanceof UnboundLink) {
					UnboundLink unbound = (UnboundLink)physicalLink;

					physicalLink = super.createPhysicalLink(startsite, endsite);
					// фрагменты перенос€тс€ в новый сгенерированный тоннель
					for (final NodeLink tmpNodeLink : new LinkedList<NodeLink>(unbound.getNodeLinks())) {
						unbound.removeNodeLink(tmpNodeLink);
						tmpNodeLink.setPhysicalLink(physicalLink);
						physicalLink.addNodeLink(tmpNodeLink);
						this.mapView.removeUnboundNodeLink(tmpNodeLink);
						this.map.addNodeLink(tmpNodeLink);
					}

					CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(unbound);
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								this.cablePath, 
								physicalLink,
								startsite,
								endsite);
					newCableChannelingItem.insertSelfBefore(cableChannelingItem);
					cableChannelingItem.setParentPathOwner(null, false);
					this.cablePath.removeLink(cableChannelingItem);
					this.cablePath.addLink(physicalLink, newCableChannelingItem);

					super.removePhysicalLink(unbound);
					physicalLink.getBinding().add(this.cablePath);
					super.setUndoable(false);
				}

				startsite = endsite;
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}
	}

	/**
	 * проверить, что узел €вз€етс€ сетевым узлом.
	 * если он €вл€етс€ неприв€занным элементом, сгенерировать на его месте
	 * сетевой узел
	 */
	private SiteNode checkSite(SiteNode site) {
		SiteNode site2 = site;
		if(site instanceof UnboundNode) {
			CreateSiteCommandAtomic command = 
					new CreateSiteCommandAtomic(
						this.type, 
						site.getLocation());
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();
			super.add(command);
			
			site2 = command.getSite();
	
			BindUnboundNodeToSiteCommandBundle command2 = 
					new BindUnboundNodeToSiteCommandBundle(
						(UnboundNode)site, 
						site2);
			command2.setNetMapViewer(this.netMapViewer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

