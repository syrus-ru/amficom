/**
 * $Id: BindUnboundNodeToSiteCommandBundle.java,v 1.31 2005/09/16 14:53:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 *  Команда привязывания непривязанного элемента к узлу.
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/09/16 14:53:32 $
 * @module mapviewclient
 */
public class BindUnboundNodeToSiteCommandBundle extends MapActionCommandBundle {
	/**
	 * привязываемый элемент.
	 */
	UnboundNode unbound;

	/**
	 * узел.
	 */
	SiteNode site;

	/**
	 * Карта, на которой производится операция.
	 */
	Map map;

	public BindUnboundNodeToSiteCommandBundle(UnboundNode unbound, SiteNode site) {
		this.unbound = unbound;
		this.site = site;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | "  //$NON-NLS-1$
				+ "bind " + this.unbound.getId() + " to "  //$NON-NLS-1$ //$NON-NLS-2$
				+ this.site.getName() + " (" + this.site.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// список кабельных путей, включающий привязываемый элемент
			List cablePaths = mapView.getCablePaths(this.unbound);
			// обновляются концевые узлы кабельных путей
			for(Iterator it = cablePaths.iterator(); it.hasNext();) {
				CablePath cablePath = (CablePath)it.next();
				if(cablePath.getEndNode().equals(this.unbound))
					cablePath.setEndNode(this.site);
				if(cablePath.getStartNode().equals(this.unbound))
					cablePath.setStartNode(this.site);
				
				for(CableChannelingItem cci : cablePath.getSchemeCableLink().getPathMembers()) {
					if(cci.getStartSiteNode().equals(this.unbound)) {
						if(cci.getEndSiteNode().equals(this.site)) {
							cci.setParentPathOwner(null, false);
						}
						else {
							cci.setStartSiteNode(this.site);
						}
					}
					if(cci.getEndSiteNode().equals(this.unbound)) {
						if(cci.getStartSiteNode().equals(this.site)) {
							cci.setParentPathOwner(null, false);
						}
						else {
							cci.setEndSiteNode(this.site);
						}
					}
				}
			}
			// список кабельных путей, включающий привязываемый элемент
			List measurementPaths = mapView.getMeasurementPaths(this.unbound);
			// обновляются концевые узлы кабельных путей
			for(Iterator it = measurementPaths.iterator(); it.hasNext();) {
				MeasurementPath mp = (MeasurementPath )it.next();
				if(mp.getEndNode().equals(this.unbound))
					mp.setEndNode(this.site);
				if(mp.getStartNode().equals(this.unbound))
					mp.setStartNode(this.site);
			}
			//При привязывании меняются концевые узлы линий и фрагментов линий
			for(Iterator it = this.map.getNodeLinks(this.unbound).iterator(); it.hasNext();) {
				NodeLink nodeLink = (NodeLink)it.next();
				PhysicalLink physicalLink = nodeLink.getPhysicalLink();

				MapElementState pls = nodeLink.getState();
						
				if(nodeLink.getEndNode().equals(this.unbound))
					nodeLink.setEndNode(this.site);
				if(nodeLink.getStartNode().equals(this.unbound))
					nodeLink.setStartNode(this.site);

				super.registerStateChange(nodeLink, pls, nodeLink.getState());
					
				MapElementState pls2 = physicalLink.getState();

				if(physicalLink.getEndNode().equals(this.unbound))
					physicalLink.setEndNode(this.site);
				if(physicalLink.getStartNode().equals(this.unbound))
					physicalLink.setStartNode(this.site);

				super.registerStateChange(physicalLink, pls2, physicalLink.getState());
			}//while(e.hasNext())
			super.removeNode(this.unbound);
			SchemeElement se = this.unbound.getSchemeElement();
			se.setSiteNode(this.site);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

