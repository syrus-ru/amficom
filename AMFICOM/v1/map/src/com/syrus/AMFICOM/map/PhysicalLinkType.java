/*-
 * $Id: PhysicalLinkType.java,v 1.117.2.1 2006/05/18 17:47:22 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypeHelper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkType;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * Тип линии топологической схемы. Существует несколько предустановленных
 * типов линий, которые определяются полем {@link #codename}, соответствующим
 * какому-либо значению {@link #DEFAULT_TUNNEL}, {@link #DEFAULT_COLLECTOR}, {@link #DEFAULT_INDOOR},
 * {@link #DEFAULT_SUBMARINE}, {@link #DEFAULT_OVERHEAD}, {@link #DEFAULT_UNBOUND}
 * @author $Author: bass $
 * @version $Revision: 1.117.2.1 $, $Date: 2006/05/18 17:47:22 $
 * @module map
 */
public final class PhysicalLinkType extends StorableObjectType 
		implements Characterizable, Namable,
		LibraryEntry, XmlTransferableObject<XmlPhysicalLinkType>,
		IdlTransferableObjectExt<IdlPhysicalLinkType> {

	/** тоннель */
	public static final String DEFAULT_TUNNEL = "defaulttunnel";
	/** участок коллектора */
	public static final String DEFAULT_COLLECTOR = "defaultcollector";
	/** внутренняя проводка */
	public static final String DEFAULT_INDOOR = "defaultindoor";
	/** подводная линия */
	public static final String DEFAULT_SUBMARINE = "defaultsubmarine";
	/** навесная линия */
	public static final String DEFAULT_OVERHEAD = "defaultoverhead";
	/** непривязанный кабель */
	public static final String DEFAULT_UNBOUND = "cable";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690191057812271924L;

	private PhysicalLinkTypeSort sort;

	private String name;

	/**
	 * Размерность тоннеля. Для тоннеля обозначает размерность матрицы труб в
	 * разрезе, для участка коллектора - число полок и мест на полках
	 */
	private IntDimension bindingDimension;

	private Identifier mapLibraryId;
	
	private boolean topological;

	public PhysicalLinkType(final IdlPhysicalLinkType pltt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(pltt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	PhysicalLinkType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension,
			final boolean topological,
			final Identifier mapLibraryId) {
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
		this.topological = topological;
		if (bindingDimension == null) {
			this.bindingDimension = new IntDimension(0, 0);
		}
		else {
			this.bindingDimension = new IntDimension(bindingDimension.getWidth(), bindingDimension.getHeight());
		}
		this.mapLibraryId = mapLibraryId;
	}

	public static PhysicalLinkType createInstance(final Identifier creatorId,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final IntDimension bindingDimension,
			final boolean topological,
			final Identifier mapLibraryId) throws CreateObjectException {

		assert creatorId != null 
				&& codename != null 
				&& name != null 
				&& description != null 
				&& bindingDimension != null 
				&& mapLibraryId != null : NON_NULL_EXPECTED;
		assert !mapLibraryId.isVoid() : NON_VOID_EXPECTED;
		
		try {
			final PhysicalLinkType physicalLinkType = new PhysicalLinkType(IdentifierPool.getGeneratedIdentifier(PHYSICALLINK_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					sort,
					codename,
					name,
					description,
					bindingDimension,
					topological,
					mapLibraryId);

			assert physicalLinkType.isValid() : OBJECT_BADLY_INITIALIZED;

			physicalLinkType.markAsChanged();

			return physicalLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}
	
	public synchronized void fromIdlTransferable(final IdlPhysicalLinkType pltt)
	throws IdlConversionException {
		super.fromIdlTransferable(pltt, pltt.codename, pltt.description);
		
		this.name = pltt.name;

		this.sort = pltt.sort;
		this.bindingDimension = new IntDimension(pltt.dimensionX, pltt.dimensionY);
		this.topological = pltt.topological;
		this.mapLibraryId = new Identifier(pltt.mapLibraryId);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.mapLibraryId);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPhysicalLinkType getIdlTransferable(final ORB orb) {
		return IdlPhysicalLinkTypeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.sort,
				this.codename,
				this.name,
				this.description,
				this.bindingDimension.getWidth(),
				this.bindingDimension.getHeight(),
				this.topological,
				this.mapLibraryId.getIdlTransferable());
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setTopological(final boolean topological) {
		this.setTopological0(topological);
		super.markAsChanged();
	}
	
	protected void setTopological0(final boolean topological) {
		this.topological = topological;
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
			final StorableObjectVersion version,
			final PhysicalLinkTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final int width,
			final int height,
			final boolean topological,
			final Identifier mapLibraryId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.bindingDimension = new IntDimension(width, height);
		assert name != null: NON_NULL_EXPECTED;
		this.name = name;

		this.sort = sort;
		this.topological = topological;
		
		this.mapLibraryId = mapLibraryId;
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

	@Override
	protected void setCodename0(final String codename) {
		super.setCodename0(codename);
	}
	
	public PhysicalLinkTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final PhysicalLinkTypeSort sort) {
		this.setSort0(sort);
		super.markAsChanged();
	}
	
	protected void setSort0(final PhysicalLinkTypeSort sort) {
		this.sort = sort;
	}

	/**
	 * @param physicalLinkType
	 * @param importType
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void getXmlTransferable(
			final XmlPhysicalLinkType physicalLinkType,
			final String importType)
	throws XmlConversionException {
		this.id.getXmlTransferable(physicalLinkType.addNewId(), importType);
		physicalLinkType.setName(this.name);
		if(this.description != null && this.description.length() != 0) {
			physicalLinkType.setDescription(this.description);
		}
		// NOTE: '+ 1' is obligatory since enumerations in idl and xsd
		// have different indexing
		physicalLinkType.setSort(XmlPhysicalLinkTypeSort.Enum.forInt(this.sort.value() + 1));
		
		physicalLinkType.setDimensionX(this.getBindingDimension().getWidth());
		physicalLinkType.setDimensionY(this.getBindingDimension().getHeight());
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws IdentifierGenerationException
	 */
	private PhysicalLinkType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId,
			@Deprecated final String codename,
			@Deprecated final String description)
	throws IdentifierGenerationException {
		super(id, importType, PHYSICALLINK_TYPE_CODE, created, creatorId);
		/**
		 * @todo Move to #fromXmlTransferable() or XmlComplementor
		 */
		super.codename = codename;
		super.description = description;
	}

	public void fromXmlTransferable(
			final XmlPhysicalLinkType xmlPhysicalLinkType,
			final String importType)
	throws XmlConversionException {
		this.name = xmlPhysicalLinkType.getName();
		this.codename = xmlPhysicalLinkType.getCodename();
		if(xmlPhysicalLinkType.isSetDescription()) {
			this.description = xmlPhysicalLinkType.getDescription();
		}
		else {
			this.description = "";
		}
		if(xmlPhysicalLinkType.isSetDimensionX()
				&& xmlPhysicalLinkType.isSetDimensionY()) {
			this.bindingDimension = new IntDimension(
					xmlPhysicalLinkType.getDimensionX(),
					xmlPhysicalLinkType.getDimensionY());
		}
		else {
			this.bindingDimension = new IntDimension(1, 1);
		}
		// NOTE: '- 1' is obligatory since enumerations in idl and xsd
		// have different indexing
		this.sort = PhysicalLinkTypeSort.from_int(xmlPhysicalLinkType.getSort().intValue() - 1);
		this.topological = true;
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlPhysicalLinkType
	 * @throws CreateObjectException
	 */
	public static PhysicalLinkType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPhysicalLinkType xmlPhysicalLinkType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlPhysicalLinkType.getCodename();
			final Set<PhysicalLinkType> physicalLinkTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, PHYSICALLINK_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert physicalLinkTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlPhysicalLinkType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			PhysicalLinkType physicalLinkType;
			if (physicalLinkTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					physicalLinkType = new PhysicalLinkType(xmlId,
							importType,
							created,
							creatorId,
							xmlPhysicalLinkType.getCodename(),
							xmlPhysicalLinkType.getDescription());
				} else {
					physicalLinkType = StorableObjectPool.getStorableObject(expectedId, true);
					if (physicalLinkType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						physicalLinkType = new PhysicalLinkType(xmlId,
								importType,
								created,
								creatorId,
								xmlPhysicalLinkType.getCodename(),
								xmlPhysicalLinkType.getDescription());
					} else {
						final String oldCodename = physicalLinkType.getCodename();
						if (!oldCodename.equals(newCodename)) {
							Log.debugMessage("WARNING: "
									+ expectedId + " will change its codename from ``"
									+ oldCodename + "'' to ``"
									+ newCodename + "''",
									WARNING);
						}
					}
				}
			} else {
				physicalLinkType = physicalLinkTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					physicalLinkType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = physicalLinkType.getId();
					if (!actualId.equals(expectedId)) {
						/*
						 * Arghhh, no match.
						 */
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + actualId
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						physicalLinkType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			physicalLinkType.fromXmlTransferable(xmlPhysicalLinkType, importType);
			assert physicalLinkType.isValid() : OBJECT_BADLY_INITIALIZED;
			physicalLinkType.markAsChanged();
			return physicalLinkType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}
	
	public Identifier getMapLibraryId() {
		return this.mapLibraryId;
	}

	public void setParent(Library library) {
		setParent0(library);
		super.markAsChanged();
	}
	
	protected void setParent0(Library library) {
		MapLibrary mapLibrary = (MapLibrary)library;
		setMapLibrary0(mapLibrary);
	}

	public Library getParent() {
		return getMapLibrary();
	}

	public MapLibrary getMapLibrary() {
		try {
			return StorableObjectPool.getStorableObject(this.getMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public void setMapLibrary(MapLibrary mapLibrary) {
		this.setMapLibrary0(mapLibrary);
		super.markAsChanged();
	}
	
	protected void setMapLibrary0(MapLibrary mapLibrary) {
		this.mapLibraryId = mapLibrary.getId();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PhysicalLinkTypeWrapper getWrapper() {
		return PhysicalLinkTypeWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @param characteristic
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this);
	}

	/**
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics()
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0());
	}

	/**
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0()
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees();
	}

	/**
	 * @param characteristics
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0();

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic);
		}
	}
}
