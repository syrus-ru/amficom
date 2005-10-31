/*-
 * $$Id: DeletePhysicalLinkCommandBundle.java,v 1.37 2005/10/31 12:30:07 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * В данном классе реализуется алгоритм удаления связи. В зависимости
 * от того, какие конечные точки на концах происходит операция удаления 
 * фрагментов линий, линий, узлов  (и путей). Команда
 * состоит из последовательности атомарных действий
 * 
 * @version $Revision: 1.37 $, $Date: 2005/10/31 12:30:07 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DeletePhysicalLinkCommandBundle extends MapActionCommandBundle {
	/**
	 * Удаляемый фрагмент
	 */
	PhysicalLink link;
	
	/**
	 * Карта
	 */
	Map map;

	public DeletePhysicalLinkCommandBundle(PhysicalLink link) {
		super();
		this.link = link;
	}


	@Override
	public void execute() {
		Log.debugMessage("delete physical link "  //$NON-NLS-1$
				+ this.link.getName() + " (" + this.link.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);
		
		// связь может быть удалена в результате атомарной команды в составе
		// другой команды удаления, в этом случае у неё будет выставлен
		// флаг isRemoved
		if(this.link.isRemoved())
			return;

		setResult(Command.RESULT_OK);

		try {
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			Collection<CablePath> cablePathsToScan = mapView.getCablePaths(this.link);
			this.link.sortNodes();
			/// удаляются все топологические узлы линии
			for(Iterator it = this.link.getSortedNodes().iterator(); it.hasNext();) {
				AbstractNode ne = (AbstractNode)it.next();
				if(ne instanceof TopologicalNode) {
					TopologicalNode node = (TopologicalNode)ne;
					super.removeNode(node);
				}
			}
			// удаляются все фрагменты линии
			for(Iterator it = this.link.getNodeLinks().iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink)it.next();
				super.removeNodeLink(nodeLink);
			}
			// удаляется сама линия
			super.removePhysicalLink(this.link);
			// проверяются все кабельные пути, которые проходили по удаленной линии,
			// и прохождение по ней заменяется непривязанной связью
			for(CablePath cablePath : cablePathsToScan) {
				setUndoable(false);
				
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(
						this.link.getStartNode(),
						this.link.getEndNode());
				unbound.setCablePath(cablePath);

//				CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.link);
				for(CableChannelingItem cableChannelingItem : cablePath.getSchemeCableLink().getPathMembers()) {
					if(cablePath.getBinding().get(cableChannelingItem) == this.link) {
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
					}
				}
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}
}
