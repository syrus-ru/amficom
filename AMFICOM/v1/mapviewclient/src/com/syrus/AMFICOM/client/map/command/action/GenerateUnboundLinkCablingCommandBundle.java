/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.31 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
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
 * ������� ��������� ������� �� ������������� �����.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.31 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
 */
public class GenerateUnboundLinkCablingCommandBundle extends
		MapActionCommandBundle {
	/**
	 * ��������� ����
	 */
	CablePath cablePath;

	/**
	 * ������������� �����
	 */
	UnboundLink unbound;

	/**
	 * ��������� �������
	 */
	PhysicalLink link;

	/**
	 * �����, �� ������� ������������ ��������
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

		if(this.unbound.getStartNode() instanceof UnboundNode
				|| this.unbound.getEndNode() instanceof UnboundNode) {
			setResult(Command.RESULT_NO);
			return;
		}
		
		try {
			this.link = super.createPhysicalLink(
					this.unbound.getStartNode(), 
					this.unbound.getEndNode());
			// ��������� ��������� ����� � ��������������� �������
			for(Iterator it2 = new LinkedList(this.unbound.getNodeLinks()).iterator(); it2.hasNext();) {
				NodeLink tmpNodeLink = (NodeLink)it2.next();
				this.unbound.removeNodeLink(tmpNodeLink);
				tmpNodeLink.setPhysicalLink(this.link);
				this.link.addNodeLink(tmpNodeLink);
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
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}

