/*
 * $Id: NodeLink.java,v 1.18 2005/01/24 16:48:55 krupenn Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ.
 * рТПЕЛФ: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.NodeLink_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.18 $, $Date: 2005/01/24 16:48:55 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class NodeLink extends StorableObject implements Characterized, MapElement {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257290240262617393L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LENGTH = "length";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_START_NODE_ID = "start_node_id";
	public static final String COLUMN_END_NODE_ID = "end_node_id";

	/** 
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private String					name;

	private PhysicalLink			physicalLink;

	private AbstractNode			startNode;
	private AbstractNode			endNode;

	private double					length;

	private List					characteristics;

	private StorableObjectDatabase	nodeLinkDatabase;


	protected transient Map map;

	protected transient boolean selected = false;

	protected transient boolean removed = false;

	protected transient boolean alarmState = false;

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
			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
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

		this.selected = false;
	}

	public void insert() throws CreateObjectException {
		this.nodeLinkDatabase = MapDatabaseContext.getNodeLinkDatabase();
		try {
			if (this.nodeLinkDatabase != null)
				this.nodeLinkDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static NodeLink createInstance(
			final Identifier creatorId,
			final PhysicalLink physicalLink,
			final AbstractNode stNode, 
			final AbstractNode eNode) 
		throws CreateObjectException 
	{
		return NodeLink.createInstance(
			creatorId,
			"",
			physicalLink,
			stNode,
			eNode,
			0.0D);
	}

	public static NodeLink createInstance(										  
			Identifier creatorId,
			String name,
			PhysicalLink physicalLink,
			AbstractNode starNode, 
			AbstractNode endNode,
			double length) 
		throws CreateObjectException 
	{
		if (name == null || physicalLink == null || starNode == null || endNode == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new NodeLink(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.NODE_LINK_ENTITY_CODE),
				creatorId,
				name,
				physicalLink,
				starNode,
				endNode,
				length);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("NodeLink.createInstance | cannot generate identifier ", e);
		}
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

	
	public List getCharacteristics() {
		return  Collections.unmodifiableList(this.characteristics);
	}
	
	public void addCharacteristic(Characteristic characteristic)
	{
		this.characteristics.add(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCharacteristic(Characteristic characteristic)
	{
		this.characteristics.remove(characteristic);
		super.currentVersion = super.getNextVersion();
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}
	
	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
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
		if(getStartNode() instanceof TopologicalNode)
			((TopologicalNode )getStartNode()).setPhysicalLink(physicalLink);
		if(getEndNode() instanceof TopologicalNode)
			((TopologicalNode )getEndNode()).setPhysicalLink(physicalLink);
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

	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.getEndNode().equals(node) )
			return getStartNode();
		if ( this.getStartNode().equals(node) )
			return getEndNode();
		return null;
	}

	public Map getMap()
	{
		return this.map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	public boolean getAlarmState()
	{
		return getPhysicalLink().getAlarmState();
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(
			(getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
			(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}

	public MapElementState getState()
	{
		return new NodeLinkState(this);
	}

	public void revert(MapElementState state)
	{
		NodeLinkState mnles = (NodeLinkState)state;

		this.setName(mnles.name);
		this.setStartNode(mnles.startNode);
		this.setEndNode(mnles.endNode);

		try
		{
			setPhysicalLink((PhysicalLink )MapStorableObjectPool.getStorableObject(mnles.physicalLinkId, false));
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isRemoved()
	{
		return this.removed;
	}
	
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	/**
	 * Получить топологическую длинну NodeLink
	 */
	public double getLengthLt()
	{
		return getLength();
	}

	public java.util.Map getExportMap() {
		if(exportMap == null)
			exportMap = new HashMap();		
		synchronized(exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_LENGTH, String.valueOf(this.length));
			exportMap.put(COLUMN_PHYSICAL_LINK_ID, this.physicalLink.getId());
			exportMap.put(COLUMN_START_NODE_ID, this.startNode.getId());
			exportMap.put(COLUMN_END_NODE_ID, this.endNode.getId());
			return Collections.unmodifiableMap(exportMap);
		}		
	}

	public static NodeLink createInstance(Identifier creatorId,
	                          			java.util.Map exportMap)
	                          		throws CreateObjectException {
		Identifier id = (Identifier) exportMap.get(COLUMN_ID);
		String name = (String) exportMap.get(COLUMN_NAME);
		double length = Double.parseDouble((String) exportMap.get(COLUMN_LENGTH));
		Identifier physicalLinkId = (Identifier) exportMap.get(COLUMN_PHYSICAL_LINK_ID);
  		Identifier startNodeId = (Identifier) exportMap.get(COLUMN_START_NODE_ID);
  		Identifier endNodeId = (Identifier) exportMap.get(COLUMN_END_NODE_ID);
	

  		if (id == null || creatorId == null || name == null || physicalLinkId == null 
  				|| startNodeId == null || endNodeId == null)
  			throw new IllegalArgumentException("Argument is 'null'");
	
  		try {
  			PhysicalLink physicalLink = (PhysicalLink ) 
  				MapStorableObjectPool.getStorableObject(
  					physicalLinkId, false);
  			AbstractNode startNode = (AbstractNode )
  				MapStorableObjectPool.getStorableObject(
  					startNodeId, true);
  			AbstractNode endNode = (AbstractNode )
  				MapStorableObjectPool.getStorableObject(
  					endNodeId, true);
  			NodeLink nodeLink = new NodeLink(
  					id, 
  					creatorId, 
  					name,
  					physicalLink,
  					startNode,
  					endNode,
  					length);
			physicalLink.addNodeLink(nodeLink);
			
			return nodeLink;
  		} catch (ApplicationException e) {
  			throw new CreateObjectException("NodeLink.createInstance |  ", e);
  		}
  	}
}
