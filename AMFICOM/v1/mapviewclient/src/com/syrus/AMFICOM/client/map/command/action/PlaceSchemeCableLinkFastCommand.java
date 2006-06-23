/*-
 * $Id: PlaceSchemeCableLinkFastCommand.java,v 1.1 2006/06/23 13:55:27 stas Exp $
 *
 * Copyright ї 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.SortedSet;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PipeBlock;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

public class PlaceSchemeCableLinkFastCommand extends MapActionCommandBundle {
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


	
	public PlaceSchemeCableLinkFastCommand(SchemeCableLink schemeCableLink, 
			SiteNode startNode, SiteNode endNode) {
		super();
		this.schemeCableLink = schemeCableLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	@Override
	public void execute() {
		try {
//			long t1 = System.currentTimeMillis();
			this.cablePath = super.createCablePath(this.schemeCableLink, this.startNode, this.endNode);
//			long t2 = System.currentTimeMillis();
			
			// идем по всем узлам кабельного пути от начального
//			SiteNode bufferStartSite = this.startNode;
			Identifier bufferStartSiteId = this.startNode.getId();
			// цикл по элементам привязки кабеля.

			//TODO эта операция и прохождение по всем элементам равны по времени, остальные = 0 
			final SortedSet<CableChannelingItem> pathMembers = this.schemeCableLink.getPathMembers();
//			long t3 = System.currentTimeMillis();
			
			for(CableChannelingItem cci : pathMembers) {
				PhysicalLink link = cci.getPhysicalLink();
				
				Identifier currentStartNodeId = cci.getStartSiteNodeId();
				Identifier currentEndNodeId = cci.getEndSiteNodeId();

				// check if link and cci's ends equals
				if (link != null) {
					final boolean bs1 = currentStartNodeId.equals(link.getStartNodeId());
					final boolean bs2 = currentStartNodeId.equals(link.getEndNodeId());
					final boolean bs = bs1 || bs2;
					
					final boolean be1 = currentEndNodeId.equals(link.getStartNodeId());
					final boolean be2 = currentEndNodeId.equals(link.getEndNodeId());
					final boolean be = be1 || be2;
					
					if (!bs) {
						Log.errorMessage("Link's startNode and CCI's startNode not equals for '" + link.getName() + "' (cable '" + this.schemeCableLink.getName() + "')");
						if (be1) {
							cci.setEndSiteNode((SiteNode)link.getStartNode());
							currentEndNodeId = link.getStartNodeId();
						} else {
							cci.setStartSiteNode((SiteNode)link.getStartNode());
							currentStartNodeId = link.getStartNodeId();
						}
					}
					if (!be) {
						Log.errorMessage("Link's endNode and CCI's endNode not equals for '" + link.getName() + "' (cable '" + this.schemeCableLink.getName() + "')");
						if (bs2) {
							cci.setStartSiteNode((SiteNode)link.getEndNode());
							currentStartNodeId = link.getEndNodeId();
						} else {
							cci.setEndSiteNode((SiteNode)link.getEndNode());
							currentEndNodeId = link.getEndNodeId();
						}
					}
				}
				
				// a link between bufferStartSite and current cci exists
				boolean exists = false;

				// переходим к следующему узлу кабельного пути
				if(bufferStartSiteId.equals(currentStartNodeId)) {
					bufferStartSiteId = currentEndNodeId;
					exists = true;
				}	else if(bufferStartSiteId.equals(currentEndNodeId)) {
					bufferStartSiteId = currentStartNodeId;
					exists = true;
				}
				
				// если ни одно из двух предыдущих условий не выполнено, то есть
				// существует разрыв последовательности привязки (линии 
				// bufferStartSite - cci.startSiteId не существует), то
				// создать на месте разрыва непроложенную линию из одного фрагмента
				if(!exists) {
					AbstractNode bufferStartSite = StorableObjectPool.getStorableObject(bufferStartSiteId, true);
					AbstractNode currentStartNode = StorableObjectPool.getStorableObject(currentStartNodeId, true);

					UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, currentStartNode);
					CableChannelingItem newCableChannelingItem = CableController.generateCCI(
							this.cablePath, unbound, bufferStartSite, currentStartNode);
					
					try {
						newCableChannelingItem.insertSelfBefore(cci);
					} catch(AssertionError e) {
						Log.errorMessage(e);
						this.schemeCableLink.getPathMembers().first().setParentPathOwner(null, true);
						bufferStartSite = this.startNode;
						break;
					}
					this.cablePath.addLink(unbound, newCableChannelingItem);
					unbound.setCablePath(this.cablePath);
					bufferStartSiteId = currentEndNodeId;
				}
				// привязать кабель к существующей линии
					
				// если линия не существует, опустить данный элемент привязки
				if(link == null) {
					AbstractNode currentStartNode = StorableObjectPool.getStorableObject(currentStartNodeId, true);
					AbstractNode currentEndNode = StorableObjectPool.getStorableObject(currentEndNodeId, true);
					
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(currentStartNode, currentEndNode);
//					Log.debugMessage("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + currentStartNode.getName() + "' and '" + currentEndNode.getName() + "'", Log.DEBUGLEVEL09);
					this.cablePath.addLink(unbound, cci);
					unbound.setCablePath(this.cablePath);
				} else {
					link.getBinding().add(this.cablePath);
					if(cci.getRowX() != -1
							&& cci.getPlaceY() != -1) {
						PipeBlock block = null;
						try {
							block = cci.getPipeBlock();
							if(block == null) {
//								XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
//								link.getId().getXmlTransferable(xmlId, "ucm");
//								String linkUn = xmlId.getStringValue();
//								cci.getId().getXmlTransferable(xmlId, "ucm");
//								String cciUn = xmlId.getStringValue();
								Log.errorMessage(
										"link '" + link.getName() 
										+ "' (id " + link.getId() 
										+ ") cci '" + cci.getId() 
										+ "' has null block!");
							}
							else {
								block.bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
							}
						} catch(ArrayIndexOutOfBoundsException e) {
//							XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
//							link.getId().getXmlTransferable(xmlId, "ucm");
//							String linkUn = xmlId.getStringValue();
//							cci.getId().getXmlTransferable(xmlId, "ucm");
//							String cciUn = xmlId.getStringValue();
							final IntDimension dimension = block.getDimension();
							Log.errorMessage(
									"link '" + link.getName() 
									+ "' (id " + link.getId() 
									+ ") block '" + block.getNumber() 
									+ "' has dimensions (" + dimension.getWidth()
									+ ", " + dimension.getHeight()
									+ "), which is inconsistent with cci (id " + cci.getId()
									+ ") with position (" + cci.getRowX()
									+ ", " + cci.getPlaceY() + ")");
//							Log.errorMessage(e);
						}
					}
					this.cablePath.addLink(link, cci);
				}
			}
//			long t4 = System.currentTimeMillis();
			
			// если элементы привязки не доходят до конца, создать непривязанную
			// линию от текущего до конечного узла
			if(!this.endNode.getId().equals(bufferStartSiteId)) {
				AbstractNode bufferStartSite = StorableObjectPool.getStorableObject(bufferStartSiteId, true);
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, endNode);
//				Log.debugMessage("Unbound fragment for Cable " + xmlId1.getStringValue() + " between nodes  '" + bufferStartSite.getName() + "' and '" + this.endNode.getName() + "'", Log.DEBUGLEVEL09);
				CableChannelingItem newCableChannelingItem = 
					CableController.generateCCI(
							this.cablePath, 
							unbound,
							bufferStartSite,
							this.endNode);
				this.cablePath.addLink(unbound, newCableChannelingItem);
				unbound.setCablePath(this.cablePath);
			}
//			long t5 = System.currentTimeMillis();
//			Log.debugMessage("PlaceSchemeCableLinkCommand :: create cable path " + (t2 - t1) + " ms", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$
//			Log.debugMessage("PlaceSchemeCableLinkCommand :: get CCIs " + (t3 - t2) + " ms", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$
//			Log.debugMessage("PlaceSchemeCableLinkCommand :: walk through CCIs " + (t4 - t3) + " ms", Level.FINEST); //$NON-NLS-1$ //$NON-NLS-2$
//			Log.debugMessage("PlaceSchemeCableLinkCommand :: Total " + (t5 - t1) + " ms", Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$
		} catch(Throwable e) {
			Log.errorMessage(e);
		}
	}
}
