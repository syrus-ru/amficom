/*-
 * $Id: PortType.java,v 1.111 2005/12/07 16:41:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlPortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlPortType;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeKind;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeSort;
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
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.XmlConversionException;
import com.syrus.util.XmlTransferableObject;

/**
 * @version $Revision: 1.111 $, $Date: 2005/12/07 16:41:51 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public final class PortType extends StorableObjectType<PortType>
		implements Characterizable, Namable,
		XmlTransferableObject<XmlPortType> {
	private static final long serialVersionUID = -115251480084275101L;

	private String name;
	private int sort;
	private int kind;

	public PortType(final IdlPortType ptt) throws CreateObjectException {
		try {
			this.fromTransferable(ptt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	PortType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final int kind) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.kind = kind;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private PortType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, PORT_TYPE_CODE, created, creatorId);
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlPortType
	 * @throws CreateObjectException
	 */
	public  static PortType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPortType xmlPortType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlPortType.getCodename();
			final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, PORT_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert portTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlPortType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			PortType portType;
			if (portTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					portType = new PortType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					portType = StorableObjectPool.getStorableObject(expectedId, true);
					if (portType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						portType = new PortType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = portType.getCodename();
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
				portType = portTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					portType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = portType.getId();
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
						portType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			portType.fromXmlTransferable(xmlPortType, importType);
			assert portType.isValid() : OBJECT_BADLY_INITIALIZED;
			portType.markAsChanged();
			return portType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param sort
	 * @param kind
	 * @throws CreateObjectException
	 */
	public static PortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final PortTypeSort sort,
			final PortTypeKind kind) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null ||
				sort == null || kind == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final PortType portType = new PortType(IdentifierPool.getGeneratedIdentifier(PORT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description,
					name,
					sort.value(),
					kind.value());

			assert portType.isValid() : OBJECT_BADLY_INITIALIZED;

			portType.markAsChanged();

			return portType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPortType ptt = (IdlPortType) transferable;
		super.fromTransferable(ptt, ptt.codename, ptt.description);
		this.name = ptt.name;
		this.sort = ptt.sort.value();
		this.kind = ptt.kind.value();
	}

	/**
	 * @param portType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlPortType portType,
			final String importType)
	throws XmlConversionException {
		this.name = portType.getName();
		this.codename = portType.getCodename();
		this.description = portType.isSetDescription()
				? portType.getDescription()
				: "";
		this.sort = portType.getSort().intValue() - 1;
		this.kind = portType.getKind().intValue() - 1;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlPortType getIdlTransferable(final ORB orb) {
		return IdlPortTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				PortTypeSort.from_int(this.sort),
				PortTypeKind.from_int(this.kind));
	}

	/**
	 * @param portType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	@Shitlet
	public void getXmlTransferable(final XmlPortType portType,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		this.id.getXmlTransferable(portType.addNewId(), importType);
		portType.setName(this.name);
		portType.setCodename(this.codename);
		if (portType.isSetDescription()) {
			portType.unsetDescription();
		}
		if (this.description.length() != 0) {
			portType.setDescription(this.description);
		}
		portType.setSort(XmlPortTypeSort.Enum.forInt(this.getSort().value() + 1));
		portType.setKind(XmlPortTypeKind.Enum.forInt(this.getKind().value() + 1));
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final int kind) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				codename,
				description);
		this.name = name;
		this.sort = sort;
		this.kind = kind;
	}

	public String getName() {
		return this.name;
	}

	public PortTypeSort getSort() {
		return PortTypeSort.from_int(this.sort);
	}

	public void setSort(final PortTypeSort sort) {
		this.sort = sort.value();
	}

	public PortTypeKind getKind() {
		return PortTypeKind.from_int(this.kind);
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PortTypeWrapper getWrapper() {
		return PortTypeWrapper.getInstance();
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
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
