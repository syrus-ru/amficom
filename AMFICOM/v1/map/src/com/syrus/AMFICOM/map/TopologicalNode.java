/*
 * $Id: TopologicalNode.java,v 1.9 2004/12/23 16:34:26 krupenn Exp $
 *
 * Copyright њ 2004 Syrus Systems.
 * оЅ’ёќѕ-‘≈»ќ…ё≈”Ћ…  √≈ќ‘“.
 * р“ѕ≈Ћ‘: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalNode_Transferable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.9 $, $Date: 2004/12/23 16:34:26 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class TopologicalNode extends AbstractNode {

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258130254244885554L;

	private PhysicalLink			physicalLink;

	/**
	 * ‘лаг показывающий закрыт ли узел
	 * true значит что из узла выходит две линии, false одна
	 */
	private boolean					active;

	private StorableObjectDatabase	topologicalNodeDatabase;

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private transient boolean canBind = false;

	public TopologicalNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			this.topologicalNodeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public TopologicalNode(TopologicalNode_Transferable tnt) throws CreateObjectException {
		super(tnt.header);
		super.name = tnt.name;
		super.description = tnt.description;
		super.location = new DoublePoint(tnt.longitude, tnt.latitude);
		this.active = tnt.active;

		try {
			this.physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(
				new Identifier(tnt.physicalLinkId), true);

			this.characteristics = new ArrayList(tnt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(tnt.characteristicIds.length);
			for (int i = 0; i < tnt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(tnt.characteristicIds[i]));

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected TopologicalNode(final Identifier id,
			final Identifier creatorId,
			String name,
			String description,
			double longitude,
			double latitude,
			boolean active) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.name = name;
		super.description = description;
		super.location = new DoublePoint(longitude, latitude);
		this.active = active;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
	}

	protected TopologicalNode(final Identifier id,
			final Identifier creatorId,
			String name,
			String description,
			PhysicalLink physicalLink,
			double longitude,
			double latitude,
			boolean active) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.name = name;
		this.description = description;
		this.physicalLink = physicalLink;
		this.location = new DoublePoint(longitude, latitude);
		this.active = active;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();

		this.selected = false;
	}

	public void insert() throws CreateObjectException {
		this.topologicalNodeDatabase = MapDatabaseContext.getTopologicalNodeDatabase();
		try {
			if (this.topologicalNodeDatabase != null)
				this.topologicalNodeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	protected static TopologicalNode createInstance0(
			Identifier creatorId,
			String name,
			String description,
			PhysicalLink physicalLink, 
			DoublePoint location)
		throws CreateObjectException {

		// IMPORTANT!!
		// physicalLink validation should NOT be performad
		// as long as a known case of creation with null PhysicalLink exists.
		// It is programmer's responsibility to make sure that call to
		// 
		// topologicalNode.setPhysicalLink(PhysicalLink physicalLink)
		// 
		// is performed after creation of TopologicalNode with physicalLink
		// set to null.
		// 
		// Where needed physicalLink validation is performed in corresponding
		// public createInstance methods

		if (creatorId == null || name == null || description == null || location == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			Identifier id =
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE);
			return new TopologicalNode(
				id,
				creatorId,
				name,
				description,
				physicalLink,
				location.getX(),
				location.getY(),
				false);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TopologicalNode.createInstance | cannot generate identifier ", e);
		}
	}

	public static TopologicalNode createInstance(
			Identifier creatorId,
			String name,
			String description,
			PhysicalLink physicalLink, 
			DoublePoint location)
		throws CreateObjectException {

		// validate physicalLink as long as validation is not performad
		// in createInstance0

		if (physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return TopologicalNode.createInstance0(
				creatorId,
				name,
				description,
				physicalLink,
				location);
	}

	public static TopologicalNode createInstance(
			final Identifier creatorId,
			final PhysicalLink physicalLink, 
			final DoublePoint location)
		throws CreateObjectException {

		// validate physicalLink as long as validation is not performad
		// in createInstance0

		if (physicalLink == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return TopologicalNode.createInstance0(
			creatorId,
			"",
			"",
			physicalLink,
			location);
	}

	public static TopologicalNode createInstance(
			final Identifier creatorId,
			final DoublePoint location)
		throws CreateObjectException {

		return TopologicalNode.createInstance0(
				creatorId,
				"",
				"",
				null,
				location);
	}

	public List getDependencies() {
		return this.characteristics;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();
		return new TopologicalNode_Transferable(
				super.getHeaderTransferable(), 
				this.name, 
				this.description,
				this.location.getX(), 
				this.location.getY(),
				(Identifier_Transferable) this.physicalLink.getId().getTransferable(),
				this.active, 
				charIds);
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * установить активность топологического узла.
	 * узел активен, если он находитс€ в середине св€зи, и не активен, если
	 * он находитс€ на конце св€зи. активные и неактивные топологические узлы
	 * отображаютс€ разными иконками
	 */
	public void setActive(boolean active) {
		this.active = active;
		super.currentVersion = super.getNextVersion();
	}

	public PhysicalLink getPhysicalLink() {
		return this.physicalLink;
	}
	
	public void setPhysicalLink(PhysicalLink physicalLink) {
		this.physicalLink = physicalLink;
		// do not change version due to physical link is not dependence object
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description,
											  double longitude,
											  double latitude,
											  boolean active) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;
			this.location.setLocation(longitude, latitude);
			this.active = active;					
	}

	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}


	public boolean isCanBind()
	{
		return this.canBind;
	}

	public MapElementState getState()
	{
		return new TopologicalNodeState(this);
	}

	/**
	 * восстановить состо€ние
	 */
	public void revert(MapElementState state)
	{
		TopologicalNodeState mpnes = (TopologicalNodeState)state;
		
		setName(mpnes.name);
		setDescription(mpnes.description);
		setImageId(mpnes.imageId);
		setLocation(mpnes.location);
		setActive(mpnes.active);
		try
		{
			setPhysicalLink((PhysicalLink )MapStorableObjectPool.getStorableObject(mpnes.physicalLinkId, false));
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
}
