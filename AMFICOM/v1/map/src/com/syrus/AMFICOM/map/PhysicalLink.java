/*
 * $Id: PhysicalLink.java,v 1.13 2004/12/23 16:34:26 krupenn Exp $
 *
 * Copyright њ 2004 Syrus Systems.
 * оЅ’ёќѕ-‘≈»ќ…ё≈”Ћ…  √≈ќ‘“.
 * р“ѕ≈Ћ‘: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLink_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.13 $, $Date: 2004/12/23 16:34:26 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class PhysicalLink extends StorableObject implements Characterized, TypedObject, MapElement {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 4121409622671570743L;
	
	private String					name;
	private String					description;

	private PhysicalLinkType		physicalLinkType;

	private AbstractNode			startNode;
	private AbstractNode			endNode;
	private String					city;
	private String					street;
	private String					building;

	private int						dimensionX;
	private int						dimensionY;

	private boolean					leftToRight;
	private boolean					topToBottom;

	private List					nodeLinks;

	private List					characteristics;

	private StorableObjectDatabase	physicalLinkDatabase;


	protected transient Map map;

	protected transient boolean selected = false;

	protected transient boolean selectionVisible = false;

	protected transient boolean removed = false;

	protected transient boolean alarmState = false;

	protected transient PhysicalLinkBinding binding;

	protected transient List sortedNodes = new LinkedList();

	protected transient boolean nodeLinksSorted = false;

	public PhysicalLink(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkDatabase = MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			this.physicalLinkDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLink(PhysicalLink_Transferable plt) throws CreateObjectException {
		super(plt.header);
		this.name = plt.name;
		this.description = plt.description;

		this.city = plt.city;
		this.street = plt.street;
		this.building = plt.building;
		this.dimensionX = plt.dimensionX;
		this.dimensionY = plt.dimensionY;
		this.leftToRight = plt.leftToRight;
		this.topToBottom = plt.topToBottom;

		try {
			this.physicalLinkType = (PhysicalLinkType) MapStorableObjectPool.getStorableObject(
				new Identifier(plt.physicalLinkTypeId), true);

			this.startNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(plt.startNodeId), true);
			this.endNode = (AbstractNode) MapStorableObjectPool.getStorableObject(new Identifier(plt.endNodeId), true);

			this.nodeLinks = new ArrayList(plt.nodeLinkIds.length);
			ArrayList nodeLinksIds = new ArrayList(plt.nodeLinkIds.length);
			for (int i = 0; i < plt.nodeLinkIds.length; i++)
				nodeLinksIds.add(new Identifier(plt.nodeLinkIds[i]));
			this.nodeLinks.addAll(MapStorableObjectPool.getStorableObjects(nodeLinksIds, true));

			this.characteristics = new ArrayList(plt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(plt.characteristicIds.length);
			for (int i = 0; i < plt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(plt.characteristicIds[i]));
			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.selected = false;
		
		this.binding = new PhysicalLinkBinding(new IntDimension(this.dimensionX, this.dimensionY));
	}

	protected PhysicalLink(final Identifier id,
						   final Identifier creatorId,
						   final String name, 
						   final String description,
						   final PhysicalLinkType physicalLinkType,
						   final AbstractNode startNode, 
						   final AbstractNode endNode, 
						   final String city, 
						   final String street, 
						   final String building,
						   final int dimensionX,
						   final int dimensionY,
						   final boolean leftToRight,
						   final boolean topToBottom) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;
		this.physicalLinkType = physicalLinkType;
		this.startNode = startNode;
		this.endNode = endNode;
		this.city = city;
		this.street = street;
		this.building = building;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;

		this.characteristics = new LinkedList();
		this.nodeLinks = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.physicalLinkDatabase = MapDatabaseContext.getPhysicalLinkDatabase();

		this.selected = false;
		
		this.binding = new PhysicalLinkBinding(physicalLinkType.getBindingDimension());
	}

	public void insert() throws CreateObjectException {
		this.physicalLinkDatabase = MapDatabaseContext.getPhysicalLinkDatabase();
		try {
			if (this.physicalLinkDatabase != null)
				this.physicalLinkDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static PhysicalLink createInstance(
			final Identifier creatorId,
			final AbstractNode stNode, 
			final AbstractNode eNode, 
			final PhysicalLinkType type)
		throws CreateObjectException {

		return PhysicalLink.createInstance(
			creatorId,
			"",
			"",
			type,
			stNode,
			eNode,
			"",
			"",
			"",
			type.getBindingDimension().getWidth(),
			type.getBindingDimension().getHeight(),
			true,
			true);
	}

	public static PhysicalLink createInstance(final Identifier creatorId,
											  final String name, 
											  final String description,
											  final PhysicalLinkType physicalLinkType,
											  final AbstractNode startNode, 
											  final AbstractNode endNode, 
											  final String city, 
											  final String street, 
											  final String building,
											  final int dimensionX,
											  final int dimensionY,
											  final boolean leftToRight,
											  final boolean topToBottom)
		throws CreateObjectException {

		if (creatorId == null || name == null
				|| description == null || physicalLinkType == null || startNode == null || endNode == null ||
				city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new PhysicalLink(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
				creatorId,
				name, 
				description,
				physicalLinkType,
				startNode, 
				endNode, 
				city, 
				street, 
				building,
				dimensionX,
				dimensionY,
				leftToRight,
				topToBottom);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("PhysicalLink.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.physicalLinkType);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}
	
	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		i = 0;
		Identifier_Transferable[] nodeLinkIds = new Identifier_Transferable[this.nodeLinks.size()];
		for (Iterator iterator = this.nodeLinks.iterator(); iterator.hasNext();)
			nodeLinkIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		
		return new PhysicalLink_Transferable(super.getHeaderTransferable(),
						this.name,
						this.description,
						(Identifier_Transferable)this.physicalLinkType.getId().getTransferable(),
						(Identifier_Transferable)this.startNode.getId().getTransferable(),
						(Identifier_Transferable)this.endNode.getId().getTransferable(),
						this.city,
						this.street,
						this.building,
						this.dimensionX,
						this.dimensionY,
						this.leftToRight,
						this.topToBottom,
						nodeLinkIds,
						charIds);
	}
	
	public StorableObjectType getType() {
		return this.physicalLinkType;
	}
	
	public void setType(StorableObjectType type) {
		this.physicalLinkType = (PhysicalLinkType )type;
		super.currentVersion = super.getNextVersion();
	}

	public List getCharacteristics() {
		return  Collections.unmodifiableList(this.characteristics);
	}
	
	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
		super.currentVersion = super.getNextVersion();
	}

	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
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

	public String getBuilding() {
		return this.building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getDescription() {
		return this.description;		
	}
	
	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}
	
	public int getDimensionX() {
		return this.dimensionX;
	}
	
	public void setDimensionX(int dimensionX) {
		this.dimensionX = dimensionX;
		super.currentVersion = super.getNextVersion();
	}
	
	public int getDimensionY() {
		return this.dimensionY;
	}
	
	public void setDimensionY(int dimensionY) {
		this.dimensionY = dimensionY;
		super.currentVersion = super.getNextVersion();
	}
	
	public AbstractNode getEndNode() {
		return this.endNode;
	}
	
	public void setEndNode(AbstractNode endNode) {
		this.endNode = endNode;
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}
	
	public boolean isLeftToRight() {
		return this.leftToRight;
	}
	
	public void setLeftToRight(boolean leftToRight) {
		this.leftToRight = leftToRight;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	public List getNodeLinks() {
		return  Collections.unmodifiableList(this.nodeLinks);
	}
	
	public void setNodeLinks(final List nodeLinks) {
		this.nodeLinks.clear();
		if (nodeLinks != null)
			this.nodeLinks.addAll(nodeLinks);
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}
	
	public AbstractNode getStartNode() {
		return this.startNode;
	}
	
	public void setStartNode(AbstractNode startNode) {
		this.startNode = startNode;
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		super.currentVersion = super.getNextVersion();
	}
	
	public boolean isTopToBottom() {
		return this.topToBottom;
	}
	
	public void setTopToBottom(boolean topToBottom) {
		this.topToBottom = topToBottom;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description,
											  PhysicalLinkType physicalLinkType,
											  String city,
											  String street,
											  String building,											  
											  int dimensionX,
											  int dimensionY,
											  boolean leftToRight,
											  boolean topToBottom,
											  AbstractNode startNode,
											  AbstractNode endNode) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;
			this.physicalLinkType = physicalLinkType;
			this.city = city;
			this.street = street;
			this.building = building;				
			this.dimensionX = dimensionX;
			this.dimensionY = dimensionY;
			this.leftToRight = leftToRight;
			this.topToBottom = topToBottom;
			this.startNode = startNode;
			this.endNode = endNode;
	}

	public PhysicalLinkBinding getBinding()
	{
		return this.binding;
	}

	/**
	 * ¬озвращ€ет топологическую длинну линии
	 */
	public double getLengthLt()
	{
		double returnValue = 0;
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink )it.next();
			returnValue += nodeLink.getLengthLt();
		}
		return returnValue;
	}

	/**
	 * ¬нимание! концевые точки линии не обновл€ютс€
	 */
	public void removeNodeLink(NodeLink nodeLink)
	{
		this.nodeLinks.remove(nodeLink);
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * ¬нимание! концевые точки линии не обновл€ютс€
	 */
	public void addNodeLink(NodeLink addNodeLink)
	{
		this.nodeLinks.add(addNodeLink);
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}


	public void clearNodeLinks()
	{	
		this.nodeLinks.clear();
		this.nodeLinksSorted = false;
		super.currentVersion = super.getNextVersion();
	}

	/**
	 * ѕолучить NodeLinks содержащие данный node в данном transmissionPath
	 */
	public java.util.List getNodeLinksAt(AbstractNode node)
	{
		LinkedList returnNodeLink = new LinkedList();
		Iterator e = getNodeLinks().iterator();

		while (e.hasNext())
		{
			NodeLink nodeLink = (NodeLink )e.next();
			if ( (nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node)))
				returnNodeLink.add(nodeLink);
		}
		return returnNodeLink;
	}

	public NodeLink getStartNodeLink()
	{
		NodeLink startNodeLink = null;

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			startNodeLink = (NodeLink )it.next();
			if(startNodeLink.getStartNode().equals(getStartNode())
				|| startNodeLink.getEndNode().equals(getStartNode()))
			{
				break;
			}
		}
		return startNodeLink;
	}

	public void sortNodes()
	{
		sortNodeLinks();
	}
	
	public List getSortedNodes()
	{
		if(!this.nodeLinksSorted)
			return null;
		return this.sortedNodes;
	}

	public void sortNodeLinks()
	{
		if(!this.nodeLinksSorted)
		{
			AbstractNode smne = this.getStartNode();
			NodeLink nl = null;
			LinkedList list = new LinkedList();
			List nodevec = new LinkedList();
			int count = getNodeLinks().size();
			for (int i = 0; i < count; i++) 
			{
				nodevec.add(smne);

				for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
				{
					NodeLink nodeLink = (NodeLink )it.next();

					if(! nodeLink.equals(nl))
					{
						if(nodeLink.getStartNode().equals(smne))
						{
							list.add(nodeLink);
							it.remove();
							smne = nodeLink.getEndNode();
							nl = nodeLink;
							break;
						}
						else
						if(nodeLink.getEndNode().equals(smne))
						{
							list.add(nodeLink);
							it.remove();
							smne = nodeLink.getStartNode();
							nl = nodeLink;
							break;
						}
					}
				}
			}
			nodevec.add(this.getEndNode());
			this.nodeLinks = list;
			this.nodeLinksSorted = true;
			this.sortedNodes = nodevec;
		}
	}

	public NodeLink nextNodeLink(NodeLink nl)
	{
		sortNodeLinks();
		int index = getNodeLinks().indexOf(nl);
		if(index == getNodeLinks().size() - 1)
			return null;
		return (NodeLink )getNodeLinks().get(index + 1);
	}

	public NodeLink previousNodeLink(NodeLink nl)
	{
		sortNodeLinks();
		int index = getNodeLinks().indexOf(nl);
		if(index == 0)
			return null;
		return (NodeLink )getNodeLinks().get(index - 1);
	}


	/**
	 * получить наличие сигнала тревоги
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	/**
	 * ”становить наличие сигнала тревоги
	 */
	public void setAlarmState(boolean alarmState) 
	{
		this.alarmState = alarmState;
	}

	/**
	 * получить флаг удалени€ элемента
	 */
	public boolean isRemoved()
	{
		return this.removed;
	}
	
	/**
	 * установить флаг удалени€ элемента
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
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

	public Map getMap()
	{
		return this.map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.getEndNode().equals(node) )
			return getStartNode();
		if ( this.getStartNode().equals(node) )
			return getEndNode();
		return null;
	}

	public DoublePoint getLocation()
	{
		int count = 0;
		double x = 0.0;
		double y = 0.0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();){
			NodeLink mnle = (NodeLink )it.next();
			DoublePoint an = mnle.getLocation();
			x += an.getX();
			y += an.getY();
			count ++;
		}
		if (count > 0){
			x /= count;
			y /= count;
			point.setLocation(x, y);
		}
		
		return point;
	}

	public MapElementState getState()
	{
		return new PhysicalLinkState(this);
	}

	public void revert(MapElementState state)
	{
		PhysicalLinkState mples = (PhysicalLinkState)state;

		this.setName(mples.name);
		this.setDescription(mples.description);
		this.setStartNode(mples.startNode);
		this.setEndNode(mples.endNode);

		this.nodeLinks = new LinkedList();
		for(Iterator it = mples.nodeLinks.iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink )it.next();
			mnle.setPhysicalLink(this);
			this.nodeLinks.add(mnle);
		}
		try
		{
			setType((PhysicalLinkType )(MapStorableObjectPool.getStorableObject(mples.mapProtoId, true)));
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
		
		this.nodeLinksSorted = false;
	}
	
	public void setSelectionVisible(boolean selectionVisible)
	{
		this.selectionVisible = selectionVisible;
	}

	public boolean isSelectionVisible()
	{
		return selectionVisible;
	}
}
