/*
 * $Id: PhysicalLinkType.java,v 1.10 2004/12/20 12:36:01 krupenn Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ.
 * рТПЕЛФ: бнжйлпн.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.Characterized;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.PhysicalLinkType_Transferable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision: 1.10 $, $Date: 2004/12/20 12:36:01 $
 * @author $Author: krupenn $
 * @module map_v1
 */
public class PhysicalLinkType extends StorableObjectType implements Characterized {

	public static final String TUNNEL = "tunnel";
	public static final String COLLECTOR = "collector";
	public static final String UNBOUND = "cable";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3690191057812271924L;

	private List					characteristics;

	/**
	 * @deprecated does not belong to any domain
	 */
	private Identifier				domainId;

	private String					name;

	/**
	 * Размерность тоннеля.
	 * Для тоннеля обозначает размерность матрицы труб в разрезе,
	 * для участка коллектора - число полок и мест на полках
	 * @todo добавить сохранение в БД
	 */
	private IntDimension 			bindingDimension;

	private StorableObjectDatabase	physicalLinkTypeDatabase;
	
	public PhysicalLinkType(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			this.physicalLinkTypeDatabase.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLinkType(PhysicalLinkType_Transferable pltt) throws CreateObjectException {
		super(pltt.header, pltt.codename, pltt.description);
		this.name = pltt.name;
		this.domainId = new Identifier(pltt.domain_id);

		try {
			this.characteristics = new ArrayList(pltt.characteristicIds.length);
			ArrayList characteristicIds = new ArrayList(pltt.characteristicIds.length);
			for (int i = 0; i < pltt.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(pltt.characteristicIds[i]));

			this.characteristics.addAll(ConfigurationStorableObjectPool.getStorableObjects(characteristicIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PhysicalLinkType(final Identifier id,
						   final Identifier creatorId,
						   final String codename,
						   final String name,
						   final String description,
						   final IntDimension bindingDimension) {
		super(id);
		long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = creatorId;
		super.codename = codename;
		this.name = name;
		this.description = description;
		this.domainId = null;
		this.bindingDimension = new IntDimension(bindingDimension.width, bindingDimension.height);

		this.characteristics = new LinkedList();

		super.currentVersion = super.getNextVersion();

		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
	}

	public void insert() throws CreateObjectException {
		this.physicalLinkTypeDatabase = MapDatabaseContext.getPhysicalLinkTypeDatabase();
		try {
			if (this.physicalLinkTypeDatabase != null)
				this.physicalLinkTypeDatabase.insert(this);
		} catch (IllegalDataException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}

	public static PhysicalLinkType createInstance(
			final Identifier creatorId,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) throws CreateObjectException {
		
		if (creatorId == null || codename == null || name == null || description == null || bindingDimension == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			return new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE),
				creatorId,
				codename,
				name,
				description,
				bindingDimension);
		} catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("PhysicalLinkType.createInstance | cannot generate identifier ", e);
		}
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

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.domainId);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public String getDescription() {
		return this.description;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public String getName() {
		return this.name;
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();
		
		return new PhysicalLinkType_Transferable(super.getHeaderTransferable(), 
				(Identifier_Transferable)this.domainId.getTransferable(),
				this.codename,
				this.name,
				this.description,
				charIds);
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

	public void setDescription(final String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
	}

	public void setDomainId(final Identifier domainId) {
		this.domainId = domainId;
		super.currentVersion = super.getNextVersion();
	}

	public void setName(final String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	
	protected synchronized void setAttributes(Date created,
											  Date modified,
											  Identifier creatorId,
											  Identifier modifierId,
											  String codename,
											  Identifier domainId,
											  String name,
											  String description) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					codename,
					description);
			this.name = name;
			this.domainId = domainId;
		}

	public void setBindingDimension(IntDimension bindingDimension)
	{
		this.bindingDimension = new IntDimension(
				bindingDimension.width, 
				bindingDimension.height);
		super.currentVersion = super.getNextVersion();
	}


	public IntDimension getBindingDimension()
	{
		return new IntDimension(bindingDimension);
	}

}
