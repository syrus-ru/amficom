/*-
 * $$Id: GenerateCablePathCablingCommandBundle.java,v 1.39 2005/10/03 10:35:00 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
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
 * ������� ��������� �������� � ������������ � ������������ ������. ��
 * ������������� ����� ������������ ������� � ������ ������������� � ���. ���
 * ������������ �������� �����������. �� ������������� ��������� ������������
 * ������� ���� � ������� �������� ������������� � ���.
 * 
 * @version $Revision: 1.39 $, $Date: 2005/10/03 10:35:00 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class GenerateCablePathCablingCommandBundle extends
		MapActionCommandBundle {
	/**
	 * ��������� ����
	 */
	CablePath cablePath;

	/**
	 * ��� ����� ��� ��������� ������ ������������� ���������
	 */
	SiteNodeType type;

	/**
	 * �����, �� ������� ������������ ��������
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
		Log.debugMessage(
				getClass().getName() + "::execute() | "  //$NON-NLS-1$
					+ "generate cabling for cable path " //$NON-NLS-1$
					+ this.cablePath.getName() 
					+ " (" + this.cablePath.getId() + ") " //$NON-NLS-1$ //$NON-NLS-2$
					+ "using site node type " + this.type.getName()  //$NON-NLS-1$
					+ " )" + this.type.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			// ��� ������������ ����� ���������� ������������������
			// ����� �� ���������� � ���������
			SiteNode startsite = (SiteNode)this.cablePath.getStartNode();
			SiteNode endsite = null;
			// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
			// �������, ������������� �� ��� ����� ������� ����)
			startsite = this.checkSite(startsite);
			// ��������� ������, ��������� ������������ ��������
			List list  = new LinkedList();
			list.addAll(this.cablePath.getLinks());
			// ���� �� ���� ������, ����������� � ��������� ����
			// �� ������������� ������ ������������ �������
			for(Iterator it = list.iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink)it.next();

				// ������� � ���������� ����
				if(startsite == link.getEndNode())
					endsite = (SiteNode)link.getStartNode();
				else
					endsite = (SiteNode)link.getEndNode();

				// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
				// �������, ������������� �� ��� ����� ������� ����)
				endsite = this.checkSite(endsite);

				// ���� ������������� �����, ������������ �������
				if(link instanceof UnboundLink) {
					UnboundLink unbound = (UnboundLink)link;

					link = super.createPhysicalLink(startsite, endsite);
					// ��������� ����������� � ����� ��������������� �������
					for(Iterator it2 = new LinkedList(unbound.getNodeLinks()).iterator(); it2.hasNext();) {
						NodeLink tmpNodeLink = (NodeLink)it2.next();
						unbound.removeNodeLink(tmpNodeLink);
						tmpNodeLink.setPhysicalLink(link);
						link.addNodeLink(tmpNodeLink);
					}

					CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(unbound);
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								this.cablePath, 
								link,
								startsite,
								endsite);
					newCableChannelingItem.insertSelfBefore(cableChannelingItem);
					cableChannelingItem.setParentPathOwner(null, false);
					this.cablePath.removeLink(cableChannelingItem);
					this.cablePath.addLink(link, newCableChannelingItem);

					super.removePhysicalLink(unbound);
					link.getBinding().add(this.cablePath);
					super.setUndoable(false);
				}

				startsite = endsite;
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	/**
	 * ���������, ��� ���� �������� ������� �����.
	 * ���� �� �������� ������������� ���������, ������������� �� ��� �����
	 * ������� ����
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

