/*
 * $Id: NodeLink.java,v 1.7 2004/12/09 13:52:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/09 13:52:27 $
 * @author $Author: bob $
 * @module map_v1
 */
public class NodeLink extends AbstractLink {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257290240262617393L;

	private String					name;

	private PhysicalLink			physicalLink;

	private AbstractNode					startNode;
	private AbstractNode					endNode;

	private double					length;

	private StorableObjectDatabase	nodeLinkDatabase;

	public NodeLink(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.nodeLinkDatabase = MapDatabaseContext.getNodeLinkDatabase();
		try {
			this.nodeLinkDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public NodeLink(NodeLink_Transferable nlt) throws CreateObjectException {
		super(nlt.header);
		this.name = nlt.name;
		this.length = nlt.length;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(nlt.physicalLinkId), true);

			this.startNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(nlt.startNodeId), true);
			this.endNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(nlt.endNodeId), true);

			this.characteristics = new ArrayList(nlt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(nlt.characteristicIds.length);
			for (int i = 0; i < nlt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(nlt.characteristicIds[i]));
			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected NodeLink(final Identifier id,
			final Identifier creatorId,
			final String name,
			final PhysicalLink physicalLink,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final double length) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
		this.length = length;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.nodeLinkDatabase = MapDatabaseContext.getNodeLinkDatabase();
	}

	public static NodeLink getInstance(NodeLink_Transferable nlt) throws CreateObjectException {
		NodeLink nodeLink = new NodeLink(nlt);

		nodeLink.nodeLinkDatabase = MapDatabaseContext.getNodeLinkDatabase();
		try {
			if (nodeLink.nodeLinkDatabase != null)
				nodeLink.nodeLinkDatabase.insert(nodeLink);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}

		return nodeLink;
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.physicalLink);
		dependencies.add(this.startNode);
		dependencies.add(this.endNode);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new NodeLink_Transferable(super.getHeaderTransferable(),
					this.name,
					(Identifier_Transferable)this.physicalLink.getId().getTransferable(),
					(Identifier_Transferable)this.startNode.getId().getTransferable(),
					(Identifier_Transferable)this.endNode.getId().getTransferable(),
					this.length,
					charIds);
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}
	
	public void setEndNode(AbstractNode endNode) {
		this.endNode = endNode;
		super.currentVersion = super.getNextVersion();
	}
	
	public double getLength() {
		return this.length;
	}
	
	public void setLength(double length) {
		this.length = length;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}
	
	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		super.currentVersion = super.getNextVersion();
	}
	
	public AbstractNode getStartNode() {
		return this.startNode;
	}
	
	public void setStartNode(AbstractNode startNode) {
		this.startNode = startNode;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  PhysicalLink physicalLink,
											  AbstractNode startNode,
											  AbstractNode endNode,
											  double length) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.physicalLink = physicalLink;
			this.startNode = startNode;
			this.endNode = endNode;
			this.length = length;					
	}
}
