/*-
 * $Id: PhysicalLinkType.java,v 1.54 2005/07/07 13:12:30 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypeHelper;

/**
 * ��� ����� �������������� �����. ���������� ��������� �����������������
 * ����� �����, ������� ������������ ����� {@link #codename}, ���������������
 * ������-���� �������� {@link #DEFAULT_TUNNEL}, {@link #DEFAULT_COLLECTOR}, {@link #DEFAULT_INDOOR},
 * {@link #DEFAULT_SUBMARINE}, {@link #DEFAULT_OVERHEAD}, {@link #DEFAULT_UNBOUND}
 * @author $Author: bass $
 * @version $Revision: 1.54 $, $Date: 2005/07/07 13:12:30 $
 * @module map_v1
 * @todo add 'topological' to constructor
 * @todo make 'topological' persistent
 * @todo make 'sort' transient (update database scheme as well)
 */
public final class PhysicalLinkType extends StorableObjectType 
		implements Characterizable, Namable, XMLBeansTransferable {

	/** ������� */
	public static final String DEFAULT_TUNNEL = "tunnel";
	/** ������� ���������� */
	public static final String DEFAULT_COLLECTOR = "collector";
	/** ���������� �������� */
	public static final String DEFAULT_INDOOR = "indoor";
	/** ��������� ����� */
	public static final String DEFAULT_SUBMARINE = "submarine";
	/** �������� ����� */
	public static final String DEFAULT_OVERHEAD = "overhead";
	/** ������������� ������ */
	public static final String DEFAULT_UNBOUND = "cable";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690191057812271924L;

	private transient PhysicalLinkTypeSort sort;
	
	private Set<Characteristic> characteristics;

	private String name;

	/**
	 * ����������� �������. ��� ������� ���������� ����������� ������� ���� �
	 * �������, ��� ������� ���������� - ����� ����� � ���� �� ������
	 *
	 * @todo �������� ���������� � ��
	 */
	private IntDimension 			bindingDimension;

	private transient boolean topological = true;

	PhysicalLinkType(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		PhysicalLinkTypeDatabase database = (PhysicalLinkTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.PHYSICALLINK_TYPE_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public PhysicalLinkType(final IdlPhysicalLinkType pltt) throws CreateObjectException {
		try {
			this.fromTransferable(pltt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PhysicalLinkType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.sort = sort;
		this.name = name;
		if (bindingDimension == null)
			this.bindingDimension = new IntDimension(0, 0);
		else
			this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());

		this.characteristics = new HashSet();
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension) throws CreateObjectException {

		if (creatorId == null || codename == null || name == null || description == null || bindingDimension == null || sort == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			PhysicalLinkType physicalLinkType = new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICALLINK_TYPE_CODE),
					creatorId,
					0L,
					sort,
					codename,
					name,
					description,
					bindingDimension);

			assert physicalLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			physicalLinkType.markAsChanged();

			return physicalLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		IdlPhysicalLinkType pltt = (IdlPhysicalLinkType) transferable;
		super.fromTransferable(pltt, pltt.codename, pltt.description);

		this.name = pltt.name;

		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(pltt.codename);

		Set ids = Identifier.fromTransferables(pltt.characteristicIds);
		this.characteristics = StorableObjectPool.getStorableObjects(ids, true);
		this.bindingDimension = new IntDimension(pltt.dimensionX, pltt.dimensionY);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPhysicalLinkType getTransferable(final ORB orb) {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);
		return IdlPhysicalLinkTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version,
				this.codename,
				this.name,
				this.description,
				this.bindingDimension.getWidth(),
				this.bindingDimension.getHeight(),
				charIds);
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setTopological(final boolean topological) {
		this.topological = topological;
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(final String name) {
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String name,
			final String description,
			final int width,
			final int height) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.bindingDimension = new IntDimension(width, height);
		assert name != null: ErrorMessages.NON_NULL_EXPECTED;
		this.name = name;

		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(codename);
	}

	public void setCodename(String codename) {
		super.setCodename(codename);
		//@todo retreive from transferable!
		this.sort = PhysicalLinkTypeSort.fromString(codename);
	}

	protected void setBindingDimension0(final IntDimension bindingDimension) {
		this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());
	}

	public void setBindingDimension(final IntDimension bindingDimension) {
		this.setBindingDimension0(bindingDimension);
		super.markAsChanged();
	}

	public IntDimension getBindingDimension() {
		return new IntDimension(this.bindingDimension);
	}

	protected void setCodename0(final String codename) {
		super.setCodename0(codename);
	}

	public Set getCharacteristics() {
		return  Collections.unmodifiableSet(this.characteristics);
	}
	
	public void addCharacteristic(final Characteristic characteristic){
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	public void removeCharacteristic(final Characteristic characteristic)
	{
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public PhysicalLinkTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final PhysicalLinkTypeSort sort) {
		this.sort = sort;
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = com.syrus.amficom.map.xml.PhysicalLinkType.Factory.newInstance();
		fillXMLTransferable(xmlPhysicalLinkType);
		return xmlPhysicalLinkType;
}
	public void fillXMLTransferable(XmlObject xmlObject) {
		com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType )xmlObject; 

		com.syrus.amficom.general.xml.UID uid = xmlPhysicalLinkType.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlPhysicalLinkType.setName(this.name);
		xmlPhysicalLinkType.setDescription(this.description);
		xmlPhysicalLinkType.setSort(com.syrus.amficom.map.xml.PhysicalLinkTypeSort.Enum.forString(this.sort.value()));

		xmlPhysicalLinkType.setDimensionX(BigInteger.valueOf(this.getBindingDimension().getWidth()));
		xmlPhysicalLinkType.setDimensionY(BigInteger.valueOf(this.getBindingDimension().getHeight()));
	}

	PhysicalLinkType(
			Identifier creatorId,
			String codename,
			String description,
			com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType, 
			ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.PHYSICALLINK_CODE, 
						xmlPhysicalLinkType.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0,
				codename,
				description);
		this.characteristics = new HashSet();
		this.fromXMLTransferable(xmlPhysicalLinkType, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType )xmlObject; 

		this.name = xmlPhysicalLinkType.getName();
		this.description = xmlPhysicalLinkType.getDescription();
		this.bindingDimension = new IntDimension(
				xmlPhysicalLinkType.getDimensionX().intValue(), 
				xmlPhysicalLinkType.getDimensionY().intValue());
	}

	public static PhysicalLinkType createInstance(
			final Identifier creatorId,
			final XmlObject xmlObject,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		com.syrus.amficom.map.xml.PhysicalLinkType xmlPhysicalLinkType = (com.syrus.amficom.map.xml.PhysicalLinkType )xmlObject;

		try {
			PhysicalLinkType physicalLinkType = new PhysicalLinkType(
					creatorId, 
					xmlPhysicalLinkType.getSort().toString(),
					xmlPhysicalLinkType.getDescription(),
					xmlPhysicalLinkType, 
					clonedIdsPool);
			assert physicalLinkType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			physicalLinkType.markAsChanged();
			return physicalLinkType;
		} catch (Exception e) {
			System.out.println(xmlPhysicalLinkType);
			throw new CreateObjectException("PhysicalLinkType.createInstance |  ", e);
		}
	}
}
