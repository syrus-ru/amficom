/*
 * $Id: SiteNode.java,v 1.7 2004/12/23 09:38:39 bob Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.7 $, $Date: 2004/12/23 09:38:39 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNode extends AbstractNode implements TypedObject {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257567325699190835L;
	private SiteNodeType			type;

	private String					city;
	private String					street;
	private String					building;

	private StorableObjectDatabase	siteNodeDatabase;

	public SiteNode(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();
		try {
			this.siteNodeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public SiteNode(SiteNode_Transferable snt) throws CreateObjectException {
		super(snt.header);
		super.name = snt.name;
		super.description = snt.description;
		super.location = new DoublePoint(snt.longitude, snt.latitude);
		this.imageId = new Identifier(snt.imageId);		
		this.city = snt.city;
		this.street = snt.street;
		this.building = snt.building;

		try {
			this.type = (SiteNodeType) MapStorableObjectPool.getStorableObject(new Identifier(snt.siteNodeTypeId), true);
			
			this.characteristics = new ArrayList(snt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(snt.characteristicIds.length);
			for (int i = 0; i < snt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(snt.characteristicIds[i]));

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected SiteNode(final Identifier id,
			final Identifier creatorId,
			final Identifier imageId,
			String name,
			String description,
			SiteNodeType type,
			double longitude,
			double latitude,
			String city,
			String street,
			String building) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		this.imageId = imageId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.location = new DoublePoint(longitude, latitude);
		this.city = city;
		this.street = street;
		this.building = building;

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();

		this.selected = false;
	}

	public void insert() throws CreateObjectException {
		this.siteNodeDatabase = MapDatabaseContext.getSiteNodeDatabase();
		try {
			if (this.siteNodeDatabase != null)
				this.siteNodeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static SiteNode createInstance(
			final Identifier creatorId,
			String name,
			String description,
			SiteNodeType siteNodeType,
			DoublePoint location,
			String city,
			String street,
			String building)
		throws CreateObjectException {

		if (creatorId == null || name == null || description == null ||						 
				siteNodeType == null || location == null || city == null || street == null || building == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new SiteNode(
				IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE),
				creatorId,
				siteNodeType.getImageId(),
				name,
				description,
				siteNodeType,
				location.getX(),
				location.getY(),
				city,
				street,
				building);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("SiteNode.createInstance | cannot generate identifier ", e);
		}
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.type);
		dependencies.add(this.imageId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new SiteNode_Transferable(super.getHeaderTransferable(), 
							this.name,
							this.description,
							this.location.getX(),
							this.location.getY(), 
							(Identifier_Transferable) this.imageId.getTransferable(),
							(Identifier_Transferable) this.type.getId().getTransferable(),
							this.city,
							this.street, 
							this.building, 
							charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public void setType(StorableObjectType type) {
		this.type = (SiteNodeType )type;
		setImageId(this.type.getImageId());
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
	
	public String getStreet() {
		return this.street;
	}
	
	public void setStreet(String street) {
		this.street = street;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,											  
											  String name,
											  String description,
											  double longitude,
											  double latitude,
											  Identifier imageId,
											  SiteNodeType type,
											  String city,
											  String street,
											  String building) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId);
			this.name = name;
			this.description = description;
			this.location.setLocation(longitude, latitude);
			this.imageId = imageId;
			this.type = type;
			this.city = city;
			this.street = street;
			this.building = building;					
	}

	public MapElementState getState()
	{
		return new SiteNodeState(this);
	}

	/**
	 * ‚ÓÒÒÚ‡ÌÓ‚ËÚ¸ ÒÓÒÚÓˇÌËÂ
	 */
	public void revert(MapElementState state)
	{
		SiteNodeState msnes = (SiteNodeState)state;
		
		setName(msnes.name);
		setDescription(msnes.description);
		setImageId(msnes.imageId);
		setLocation(msnes.location);
		
		try
		{
			setType((SiteNodeType )(MapStorableObjectPool.getStorableObject(msnes.mapProtoId, true)));
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
