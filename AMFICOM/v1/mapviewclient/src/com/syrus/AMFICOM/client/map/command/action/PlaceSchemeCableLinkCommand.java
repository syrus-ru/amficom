/*-
 * $$Id: PlaceSchemeCableLinkCommand.java,v 1.48 2005/10/04 17:11:31 krupenn Exp $$
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
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * ���������� ������ �� �����.
 * 
 * @version $Revision: 1.48 $, $Date: 2005/10/04 17:11:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle {
	/**
	 * ��������� ���� ���������� ����
	 */
	SiteNode startNode = null;

	/**
	 * �������� ���� ���������� ����
	 */
	SiteNode endNode = null;

	/**
	 * ����������� ��������� ����
	 */
	CablePath cablePath = null;

	/**
	 * ����������� ������
	 */
	SchemeCableLink schemeCableLink = null;

	Map map;

	MapView mapView;

	public PlaceSchemeCableLinkCommand(SchemeCableLink schemeCableLink) {
		super();
		this.schemeCableLink = schemeCableLink;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "place scheme cable link " //$NON-NLS-1$
				+ this.schemeCableLink.getName()
				+ " (" + this.schemeCableLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			long t1 = System.currentTimeMillis();
			this.startNode = this.mapView.getStartNode(this.schemeCableLink);
			long t2 = System.currentTimeMillis();
			this.endNode = this.mapView.getEndNode(this.schemeCableLink);
			long t3 = System.currentTimeMillis();
			this.cablePath = this.mapView.findCablePath(this.schemeCableLink);
			long t4 = System.currentTimeMillis();
			// ���� ��������� ���� ��� ���� - ������ �� ������
			if(this.cablePath != null)
			{
//			super.checkCablePathLinks(cablePath);
				return;
			}
			if(this.startNode == null || this.endNode == null)
				return;
			this.cablePath = super.createCablePath(this.schemeCableLink, this.startNode, this.endNode);
			long t5 = System.currentTimeMillis();
			// ���� �� ���� ����� ���������� ���� �� ����������
			SiteNode bufferStartSite = this.startNode;
			// ���� �� ��������� �������� ������.
			for(Iterator iter = new LinkedList(this.schemeCableLink.getPathMembers()).iterator(); iter.hasNext();) {
				CableChannelingItem cci = (CableChannelingItem )iter.next();
				SiteNode currentStartNode = cci.getStartSiteNode();
				SiteNode currentEndNode = cci.getEndSiteNode();

				// ���� ������� �������� �� ������������� �������������� �����
				// (���� �� ����� �������� �� ������� �� �����) �� �������
				// �������� ����������
				if(currentStartNode == null
						|| currentEndNode == null) {
					continue;
				}

				// a link between bufferStartSite and current cci exists
				boolean exists = false;
				
				// ��������� � ���������� ���� ���������� ����
				if(bufferStartSite.equals(currentStartNode)) {
					bufferStartSite = currentEndNode;
					exists = true;
				}
				else if(bufferStartSite.equals(currentEndNode)) {
					bufferStartSite = currentStartNode;
					exists = true;
				}
				
				// ���� �� ���� �� ���� ���������� ������� �� ���������, �� ����
				// ���������� ������ ������������������ �������� (����� 
				// bufferStartSite - cci.startSiteId �� ����������), ��
				// ������� �� ����� ������� ������������� ����� �� ������ ���������
				if(!exists) {
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, currentStartNode);
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								this.cablePath, 
								unbound,
								bufferStartSite,
								currentStartNode);
					newCableChannelingItem.insertSelfBefore(cci);
 					this.cablePath.addLink(unbound, newCableChannelingItem);
					unbound.setCablePath(this.cablePath);

					bufferStartSite = currentEndNode;
				}
				// ��������� ������ � ������������ �����
				{
					
					PhysicalLink link = cci.getPhysicalLink();
					
					// ���� ����� �� ����������, �������� ������ ������� ��������
					if(link == null) {
						UnboundLink unbound = super.createUnboundLinkWithNodeLink(currentStartNode, currentEndNode);
						this.cablePath.addLink(unbound, cci);
						unbound.setCablePath(this.cablePath);
					}
					else {
						link.getBinding().add(this.cablePath);
						if(cci.getRowX() != -1
								&& cci.getPlaceY() != -1) {
							try {
								link.getBinding().bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
							} catch(ArrayIndexOutOfBoundsException e) {
								XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
								link.getId().getXmlTransferable(xmlId, "ucm");
								String linkUn = xmlId.getStringValue();
								cci.getId().getXmlTransferable(xmlId, "ucm");
								String cciUn = xmlId.getStringValue();
								final IntDimension dimension = link.getBinding().getDimension();
								System.out.println("link '" + link.getName() 
										+ "' (un " + linkUn 
										+ ") has dimensions (" + dimension.getWidth()
										+ ", " + dimension.getHeight()
										+ "), which is inconsistent with cci (un " + cciUn
										+ ") with position (" + cci.getRowX()
										+ ", " + cci.getPlaceY() + ")");
								e.printStackTrace();
							}
						}
			
						this.cablePath.addLink(link, cci);
					}
				}
			}
			long t6 = System.currentTimeMillis();
			// ���� �������� �������� �� ������� �� �����, ������� �������������
			// ����� �� �������� �� ��������� ����
			if(this.endNode != bufferStartSite) {
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
				CableChannelingItem newCableChannelingItem = 
					CableController.generateCCI(
							this.cablePath, 
							unbound,
							bufferStartSite,
							this.endNode);
				this.cablePath.addLink(unbound, newCableChannelingItem);
				unbound.setCablePath(this.cablePath);
			}
			long t7 = System.currentTimeMillis();
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.setCurrentMapElement(this.cablePath);
			Log.debugMessage("PlaceSchemeCableLinkCommand :: get start node for scl " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("PlaceSchemeCableLinkCommand :: get end node for scl " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("PlaceSchemeCableLinkCommand :: find cable path " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("PlaceSchemeCableLinkCommand :: create cable path " + (t5 - t4) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("PlaceSchemeCableLinkCommand :: walk through CCIs " + (t6 - t5) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			Log.debugMessage("PlaceSchemeCableLinkCommand :: create final unbound node " + (t7 - t6) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}
