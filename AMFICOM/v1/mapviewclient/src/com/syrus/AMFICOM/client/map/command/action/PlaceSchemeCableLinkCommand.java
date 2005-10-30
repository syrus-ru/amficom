/*-
 * $$Id: PlaceSchemeCableLinkCommand.java,v 1.58 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * Разместить кабель на карте.
 * 
 * @version $Revision: 1.58 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle {
	/**
	 * начальный узел кабельного пути
	 */
	SiteNode startNode = null;

	/**
	 * конечный узел кабельного пути
	 */
	SiteNode endNode = null;

	/**
	 * создаваемый кабельный путь
	 */
	CablePath cablePath = null;

	/**
	 * размещаемый кабель
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
		assert Log.debugMessage(
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
			// если кабельный путь уже есть - ничего не делать
			if(this.cablePath != null)
			{
//			super.checkCablePathLinks(cablePath);
				return;
			}
			if(this.startNode == null || this.endNode == null)
				return;
			this.cablePath = super.createCablePath(this.schemeCableLink, this.startNode, this.endNode);
			long t5 = System.currentTimeMillis();
			// идем по всем узлам кабельного пути от начального
			SiteNode bufferStartSite = this.startNode;
			// цикл по элементам привязки кабеля.
			XmlIdentifier xmlId1 = XmlIdentifier.Factory.newInstance();
			this.schemeCableLink.getId().getXmlTransferable(xmlId1, "ucm");
			if(xmlId1.getStringValue().equals("682647")) {
				@SuppressWarnings("unused") int a = 0;
			}
			final Set<AbstractNode> siteNodesPresent = this.mapView.getAllNodes();
			final SortedSet<CableChannelingItem> pathMembers = this.schemeCableLink.getPathMembers();
//			System.out.println("SchemeCableLink " + xmlId1.getStringValue() + " has " + pathMembers.size() + " cci's");
			for(Iterator iter = new LinkedList(pathMembers).iterator(); iter.hasNext();) {
				CableChannelingItem cci = (CableChannelingItem )iter.next();
				SiteNode currentStartNode = cci.getStartSiteNode();
				SiteNode currentEndNode = cci.getEndSiteNode();

				// если элемент привязки не соответствует топологической схеме
				// (один из узлов привязки не нанесен на карту) то элемент
				// привязки опускается
				if(currentStartNode == null
						|| currentEndNode == null) {
					cci.setParentPathOwner(null, false);
					continue;
				}
				// если элемент привязки не соответствует топологической схеме
				// (один из узлов привязки находится в другой топологической 
				// схеме) то элемент привязки опускается
				if(!siteNodesPresent.contains(currentEndNode)
						 || !siteNodesPresent.contains(currentStartNode)) {
					cci.setParentPathOwner(null, false);
					continue;
				}

				// a link between bufferStartSite and current cci exists
				boolean exists = false;
				
				// переходим к следующему узлу кабельного пути
				if(bufferStartSite.equals(currentStartNode)) {
					bufferStartSite = currentEndNode;
					exists = true;
				}
				else if(bufferStartSite.equals(currentEndNode)) {
					bufferStartSite = currentStartNode;
					exists = true;
				}
				
				// если ни одно из двух предыдущих условий не выполнено, то есть
				// существует разрыв последовательности привязки (линии 
				// bufferStartSite - cci.startSiteId не существует), то
				// создать на месте разрыва непроложенную линию из одного фрагмента
				if(!exists) {
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, currentStartNode);
					System.out.println("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + bufferStartSite.getName() + "' and '" + currentStartNode.getName() + "'");
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								this.cablePath, 
								unbound,
								bufferStartSite,
								currentStartNode);
					try {
						newCableChannelingItem.insertSelfBefore(cci);
					} catch(AssertionError e) {
						assert Log.debugMessage(e, Level.SEVERE);
						this.schemeCableLink.getPathMembers().first().setParentPathOwner(null, true);
						bufferStartSite = this.startNode;
						break;
					}
 					this.cablePath.addLink(unbound, newCableChannelingItem);
					unbound.setCablePath(this.cablePath);

					bufferStartSite = currentEndNode;
				}
				// привязать кабель к существующей линии
				{
					
					PhysicalLink link = cci.getPhysicalLink();
					
					// если линия не существует, опустить данный элемент привязки
					if(link == null) {
						UnboundLink unbound = super.createUnboundLinkWithNodeLink(currentStartNode, currentEndNode);
						System.out.println("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + currentStartNode.getName() + "' and '" + currentEndNode.getName() + "'");
						this.cablePath.addLink(unbound, cci);
						unbound.setCablePath(this.cablePath);
					}
					else {
						link.getBinding().add(this.cablePath);
						if(cci.getRowX() != -1
								&& cci.getPlaceY() != -1) {
							PipeBlock block = null;
							try {
								block = cci.getPipeBlock();
								if(block == null) {
									XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
									link.getId().getXmlTransferable(xmlId, "ucm");
									String linkUn = xmlId.getStringValue();
									cci.getId().getXmlTransferable(xmlId, "ucm");
									String cciUn = xmlId.getStringValue();
									System.out.println(
											"link '" + link.getName() 
											+ "' (un " + linkUn 
											+ ") cci '" + cciUn 
											+ "' has null block!");
								}
								else {
									block.bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
								}
							} catch(ArrayIndexOutOfBoundsException e) {
								XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
								link.getId().getXmlTransferable(xmlId, "ucm");
								String linkUn = xmlId.getStringValue();
								cci.getId().getXmlTransferable(xmlId, "ucm");
								String cciUn = xmlId.getStringValue();
								final IntDimension dimension = block.getDimension();
								System.out.println(
										"link '" + link.getName() 
										+ "' (un " + linkUn 
										+ ") block '" + block.getNumber() 
										+ "' has dimensions (" + dimension.getWidth()
										+ ", " + dimension.getHeight()
										+ "), which is inconsistent with cci (un " + cciUn
										+ ") with position (" + cci.getRowX()
										+ ", " + cci.getPlaceY() + ")");
//								e.printStackTrace();
							}
						}
			
						this.cablePath.addLink(link, cci);
					}
				}
			}
			long t6 = System.currentTimeMillis();
			// если элементы привязки не доходят до конца, создать непривязанную
			// линию от текущего до конечного узла
			if(this.endNode != bufferStartSite) {
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
				System.out.println("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + bufferStartSite.getName() + "' and '" + this.endNode.getName() + "'");
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
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.setCurrentMapElement(this.cablePath);
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: get start node for scl " + (t2 - t1) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: get end node for scl " + (t3 - t2) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: find cable path " + (t4 - t3) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: create cable path " + (t5 - t4) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: walk through CCIs " + (t6 - t5) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
			assert Log.debugMessage("PlaceSchemeCableLinkCommand :: create final unbound node " + (t7 - t6) + " ms", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}

}
