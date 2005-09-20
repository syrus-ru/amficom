/*-
 * $Id: SchemeProtoElement.java,v 1.92 2005/09/20 18:13:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoElementHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDeviceSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLinkSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoElementSeq;
import com.syrus.util.Log;

/**
 * #02 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.92 $, $Date: 2005/09/20 18:13:35 $
 * @module scheme
 */
public final class SchemeProtoElement extends AbstractCloneableStorableObject
		implements Describable, SchemeCellContainer, Characterizable,
		ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemeProtoElement> {
	private static final long serialVersionUID = 3689348806202569782L;

	private String name;

	private String description;

	private String label;

	Identifier equipmentTypeId;

	Identifier symbolId;

	Identifier ugoCellId;

	Identifier schemeCellId;

	Identifier parentSchemeProtoGroupId;

	Identifier parentSchemeProtoElementId;

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	private boolean parentSet = false;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param label
	 * @param equipmentType
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeProtoGroup
	 * @param parentSchemeProtoElement
	 */
	SchemeProtoElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup,
			final SchemeProtoElement parentSchemeProtoElement) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.label = label;
		this.equipmentTypeId = Identifier.possiblyVoid(equipmentType);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);

		assert parentSchemeProtoGroup == null || parentSchemeProtoElement == null : EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeProtoGroupId = Identifier.possiblyVoid(parentSchemeProtoGroup);
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
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
	private SchemeProtoElement(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, SCHEMEPROTOELEMENT_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeProtoElement(final IdlSchemeProtoElement transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource)}.
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null, parentSchemeProtoElement);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoGroup)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoGroup
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId, final String name,
			final SchemeProtoGroup parentSchemeProtoGroup)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null, null,
				null, parentSchemeProtoGroup);
	}

	/**
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId cannot be <code>null</code>.
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert label != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(IdentifierPool.getGeneratedIdentifier(SCHEMEPROTOELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					label,
					equipmentType,
					symbol,
					ugoCell,
					schemeCell,
					null,
					null);
			schemeProtoElement.markAsChanged();
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoElement cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoElement parentSchemeProtoElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert label != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(IdentifierPool.getGeneratedIdentifier(SCHEMEPROTOELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					label,
					equipmentType,
					symbol,
					ugoCell,
					schemeCell,
					null,
					parentSchemeProtoElement);
			schemeProtoElement.markAsChanged();
			schemeProtoElement.parentSet = true;
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoGroup cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert label != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoGroup != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(IdentifierPool.getGeneratedIdentifier(SCHEMEPROTOELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					label,
					equipmentType,
					symbol,
					ugoCell,
					schemeCell,
					parentSchemeProtoGroup,
					null);
			schemeProtoElement.markAsChanged();
			schemeProtoElement.parentSet = true;
			return schemeProtoElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeProtoElement
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeProtoElement createInstance(
			final Identifier creatorId,
			final XmlSchemeProtoElement xmlSchemeProtoElement,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemeProtoElement.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeProtoElement schemeProtoElement;
			if (id.isVoid()) {
				schemeProtoElement = new SchemeProtoElement(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeProtoElement = StorableObjectPool.getStorableObject(id, true);
				if (schemeProtoElement == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeProtoElement = new SchemeProtoElement(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeProtoElement.fromXmlTransferable(xmlSchemeProtoElement, importType);
			assert schemeProtoElement.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeProtoElement.markAsChanged();
			return schemeProtoElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param schemeDevice cannot be <code>null</code>.
	 */
	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		schemeDevice.setParentSchemeProtoElement(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		schemeLink.setParentSchemeProtoElement(this);
	}

	/**
	 * @param schemeProtoElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 */
	public void addSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		assert schemeProtoElement != this: CIRCULAR_DEPS_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(this);
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeProtoElement clone() throws CloneNotSupportedException {
		try {
			final SchemeProtoElement clone = (SchemeProtoElement) super.clone();

			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			clone.clonedIdMap.put(this.id, clone.id);

			final SchemeImageResource ugoCell = this.getUgoCell0();
			if (ugoCell == null) {
				clone.setUgoCell(null);
			} else {
				final SchemeImageResource ugoCellClone = ugoCell.clone();
				clone.clonedIdMap.putAll(ugoCellClone.getClonedIdMap());
				clone.setUgoCell(ugoCellClone);
			}
			final SchemeImageResource schemeCell = this.getSchemeCell0();
			if (schemeCell == null) {
				clone.setSchemeCell(null);
			} else {
				final SchemeImageResource schemeCellClone = schemeCell.clone();
				clone.clonedIdMap.putAll(schemeCellClone.getClonedIdMap());
				clone.setSchemeCell(schemeCellClone);
			}
			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
				final SchemeDevice schemeDeviceClone = schemeDevice.clone();
				clone.clonedIdMap.putAll(schemeDeviceClone.getClonedIdMap());
				clone.addSchemeDevice(schemeDeviceClone);
			}
			for (final SchemeLink schemeLink : this.getSchemeLinks0()) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				clone.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				clone.addSchemeLink(schemeLinkClone);
			}
			for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0()) {
				final SchemeProtoElement schemeProtoElementClone = schemeProtoElement.clone();
				clone.clonedIdMap.putAll(schemeProtoElementClone.getClonedIdMap());
				clone.addSchemeProtoElement(schemeProtoElementClone);
			}

			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : clone.getSchemeLinks0()) {
				final Identifier sourceSchemePortId = clone.clonedIdMap.get(schemeLink.sourceAbstractSchemePortId);
				final Identifier targetSchemePortId = clone.clonedIdMap.get(schemeLink.targetAbstractSchemePortId);
				schemeLink.setSourceAbstractSchemePortId((sourceSchemePortId == null) ? VOID_IDENTIFIER : sourceSchemePortId);
				schemeLink.setTargetAbstractSchemePortId((targetSchemePortId == null) ? VOID_IDENTIFIER : targetSchemePortId);
			}

			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	/**
	 * @see Characterizable#getCharacteristics(boolean usePool)
	 */
	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.equipmentTypeId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.equipmentTypeId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.parentSchemeProtoGroupId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public Set<Identifiable> getReverseDependencies() throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics(true)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeDevices0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeLinks0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeProtoElements0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	Identifier getEquipmentTypeId() {
		assert this.equipmentTypeId != null: OBJECT_NOT_INITIALIZED;
		assert this.equipmentTypeId.isVoid() || this.equipmentTypeId.getMajor() == EQUIPMENT_TYPE_CODE;
		return this.equipmentTypeId;
	}

	/**
	 * A wrapper around {@link #getEquipmentType0()}.
	 *
	 * @return <code>equipmentType</code> associated with this
	 *         <code>schemeProtoElement</code>, or <code>null</code> if
	 *         none.
	 */
	public EquipmentType getEquipmentType() {
		try {
			return this.getEquipmentType0();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getEquipmentTypeId()}.
	 *
	 * @throws ApplicationException
	 */
	EquipmentType getEquipmentType0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getEquipmentTypeId(), true);
	}

	/**
	 * @return this <code>SchemeProtoElement</code>&apos;s label, or
	 *         empty string if none. Never returns <code>null</code>s.
	 */
	public String getLabel() {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0: OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeProtoElementId() {
		assert true || this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (parentSchemeProtoElementIdVoid) {
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoElementId() | Parent SchemeProtoElement was requested, while parent is a SchemeProtoGroup; returning null.",
					FINE);
		}
		return this.parentSchemeProtoElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoElementId()}.
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeProtoElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeProtoGroupId() {
		assert true || this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeProtoGroupIdVoid = this.parentSchemeProtoGroupId.isVoid();
		assert parentSchemeProtoGroupIdVoid || this.parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;
		if (parentSchemeProtoGroupIdVoid) {
			Log.debugMessage("SchemeProtoElement.getParentSchemeProtoGroupId() | Parent SchemeProtoGroup was requested, while parent is a SchemeProtoElement; returnning null",
					FINE);
		}
		return this.parentSchemeProtoGroupId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoGroupId()}.
	 */
	public SchemeProtoGroup getParentSchemeProtoGroup() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeProtoGroupId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getSchemeCellId() {
		assert this.schemeCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.schemeCellId.isVoid() || this.schemeCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.schemeCellId;
	}

	/**
	 * A wrapper around {@link #getSchemeCell0()}.
	 *
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		try {
			return this.getSchemeCell0();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getSchemeCellId()}.
	 *
	 * @throws ApplicationException
	 */
	SchemeImageResource getSchemeCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getSchemeCellId(), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeDevice> getSchemeDevices() {
		try {
			return Collections.unmodifiableSet(this.getSchemeDevices0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeDevice> getSchemeDevices0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEDEVICE_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeLink> getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(this.getSchemeLinks0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeLink> getSchemeLinks0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMELINK_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeProtoElement> getSchemeProtoElements() {
		try {
			return Collections.unmodifiableSet(this.getSchemeProtoElements0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeProtoElement> getSchemeProtoElements0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPROTOELEMENT_CODE), true);
	}

	Identifier getSymbolId() {
		assert this.symbolId != null: OBJECT_NOT_INITIALIZED;
		assert this.symbolId.isVoid() || this.symbolId.getMajor() == IMAGERESOURCE_CODE;
		return this.symbolId;
	}

	/**
	 * A wrapper around {@link #getSymbol0()}.
	 *
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		try {
			return this.getSymbol0();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getSymbolId()}.
	 *
	 * @throws ApplicationException
	 */
	BitmapImageResource getSymbol0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getSymbolId(), true);
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeProtoElement getTransferable(final ORB orb) {
		return IdlSchemeProtoElementHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.label,
				this.equipmentTypeId.getTransferable(),
				this.symbolId.getTransferable(),
				this.ugoCellId.getTransferable(),
				this.schemeCellId.getTransferable(),
				this.parentSchemeProtoGroupId.getTransferable(),
				this.parentSchemeProtoElementId.getTransferable());
	}

	/**
	 * @param schemeProtoElement
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void getXmlTransferable(
			final XmlSchemeProtoElement schemeProtoElement,
			final String importType)
	throws ApplicationException {
		super.id.getXmlTransferable(schemeProtoElement.addNewId(), importType);
		schemeProtoElement.setName(this.name);
		if (schemeProtoElement.isSetDescription()) {
			schemeProtoElement.unsetDescription();
		}
		if (this.description.length() != 0) {
			schemeProtoElement.setDescription(this.description);
		}
		if (schemeProtoElement.isSetLabel()) {
			schemeProtoElement.unsetLabel();
		}
		if (this.label.length() != 0) {
			schemeProtoElement.setLabel(this.label);
		}
		if (schemeProtoElement.isSetEquipmentTypeId()) {
			schemeProtoElement.unsetEquipmentTypeId();
		}
		if (!this.equipmentTypeId.isVoid()) {
			this.equipmentTypeId.getXmlTransferable(schemeProtoElement.addNewEquipmentTypeId(), importType);
		}
		if (schemeProtoElement.isSetSymbolId()) {
			schemeProtoElement.unsetSymbolId();
		}
		if (!this.symbolId.isVoid()) {
			this.symbolId.getXmlTransferable(schemeProtoElement.addNewSymbolId(), importType);
		}
		if (schemeProtoElement.isSetUgoCellId()) {
			schemeProtoElement.unsetUgoCellId();
		}
		if (!this.ugoCellId.isVoid()) {
			this.ugoCellId.getXmlTransferable(schemeProtoElement.addNewUgoCellId(), importType);
		}
		if (schemeProtoElement.isSetSchemeCellId()) {
			schemeProtoElement.unsetSchemeCellId();
		}
		if (!this.schemeCellId.isVoid()) {
			this.schemeCellId.getXmlTransferable(schemeProtoElement.addNewSchemeCellId(), importType);
		}
		if (schemeProtoElement.isSetParentSchemeProtoGroupId()) {
			schemeProtoElement.unsetParentSchemeProtoGroupId();
		}
		if (!this.parentSchemeProtoGroupId.isVoid()) {
			this.parentSchemeProtoGroupId.getXmlTransferable(schemeProtoElement.addNewParentSchemeProtoGroupId(), importType);
		}
		if (schemeProtoElement.isSetParentSchemeProtoElementId()) {
			schemeProtoElement.unsetParentSchemeProtoElementId();
		}
		if (!this.parentSchemeProtoElementId.isVoid()) {
			this.parentSchemeProtoElementId.getXmlTransferable(schemeProtoElement.addNewParentSchemeProtoElementId(), importType);
		}
		if (schemeProtoElement.isSetCharacteristics()) {
			schemeProtoElement.unsetCharacteristics();
		}
		final Set<Characteristic> characteristics = this.getCharacteristics(false);
		if (!characteristics.isEmpty()) {
			final XmlCharacteristicSeq xmlCharacteristicSeq = schemeProtoElement.addNewCharacteristics();
			for (final Characteristic characteristic : characteristics) {
				characteristic.getXmlTransferable(xmlCharacteristicSeq.addNewCharacteristic(), importType);
			}
		}
		if (schemeProtoElement.isSetSchemeProtoElements()) {
			schemeProtoElement.unsetSchemeProtoElements();
		}
		final Set<SchemeProtoElement> schemeProtoElements = this.getSchemeProtoElements0();
		if (!schemeProtoElements.isEmpty()) {
			final XmlSchemeProtoElementSeq schemeProtoElementSeq = schemeProtoElement.addNewSchemeProtoElements();
			for (final SchemeProtoElement schemeProtoElement2 : schemeProtoElements) {
				schemeProtoElement2.getXmlTransferable(schemeProtoElementSeq.addNewSchemeProtoElement(), importType);
			}
		}
		if (schemeProtoElement.isSetSchemeDevices()) {
			schemeProtoElement.unsetSchemeDevices();
		}
		final Set<SchemeDevice> schemeDevices = this.getSchemeDevices0();
		if (!schemeDevices.isEmpty()) {
			final XmlSchemeDeviceSeq schemeDeviceSeq = schemeProtoElement.addNewSchemeDevices();
			for (final SchemeDevice schemeDevice : schemeDevices) {
				schemeDevice.getXmlTransferable(schemeDeviceSeq.addNewSchemeDevice(), importType);
			}
		}
		if (schemeProtoElement.isSetSchemeLinks()) {
			schemeProtoElement.unsetSchemeLinks();
		}
		final Set<SchemeLink> schemeLinks = this.getSchemeLinks0();
		if (!schemeLinks.isEmpty()) {
			final XmlSchemeLinkSeq schemeLinkSeq = schemeProtoElement.addNewSchemeLinks();
			for (final SchemeLink schemeLink : schemeLinks) {
				schemeLink.getXmlTransferable(schemeLinkSeq.addNewSchemeLink(), importType);
			}
		}
		XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, EXPORT);
	}

	Identifier getUgoCellId() {
		assert this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.ugoCellId.isVoid() || this.ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.ugoCellId;
	}

	/**
	 * A wrapper around {@link #getUgoCell0()}.
	 *
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		try {
			return this.getUgoCell0();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #getUgoCellId()}.
	 *
	 * @throws ApplicationException
	 */
	SchemeImageResource getUgoCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getUgoCellId(), true);
	}

	/**
	 * The <code>SchemeDevice</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeDevice
	 */
	public void removeSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		assert schemeDevice.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeDevice.setParentSchemeProtoElement(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		assert schemeLink.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentSchemeProtoElement(null);
	}

	/**
	 * The <code>SchemeProtoElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoElement
	 */
	public void removeSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		assert schemeProtoElement.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name can be neither <code>null</code> nor an empty string.
	 * @param description cannot be <code>null</code>. For this purpose,
	 *        supply an empty string as an argument.
	 * @param label
	 * @param equipmentTypeId
	 * @param symbolId cannot be <code>null</code>. For this purpose,
	 *        supply {@link Identifier#VOID_IDENTIFIER} as an argument.
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeProtoGroupId
	 * @param parentSchemeProtoElementId
	 */
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final String label,
			final Identifier equipmentTypeId,
			final Identifier symbolId,
			final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeProtoGroupId,
			final Identifier parentSchemeProtoElementId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert description != null : NON_NULL_EXPECTED;
			assert label != null : NON_NULL_EXPECTED;
			assert equipmentTypeId != null : NON_NULL_EXPECTED;
			assert symbolId != null : NON_NULL_EXPECTED;
			assert ugoCellId != null : NON_NULL_EXPECTED;
			assert schemeCellId != null : NON_NULL_EXPECTED;
	
			assert parentSchemeProtoGroupId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoGroupId.isVoid() ^ parentSchemeProtoElementId.isVoid();
	
			this.name = name;
			this.description = description;
			this.label = label;
			this.equipmentTypeId = equipmentTypeId;
			this.symbolId = symbolId;
			this.ugoCellId = ugoCellId;
			this.schemeCellId = schemeCellId;
			this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
			this.parentSchemeProtoElementId = parentSchemeProtoElementId;
	
			this.parentSet = true;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		assert description != null: NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @param equipmentTypeId
	 */
	void setEquipmentTypeId(final Identifier equipmentTypeId) {
		assert equipmentTypeId.isVoid() || equipmentTypeId.getMajor() == EQUIPMENT_TYPE_CODE;
		if (this.equipmentTypeId.equals(equipmentTypeId)) {
			return;
		}
		this.equipmentTypeId = equipmentTypeId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setEquipmentTypeId(Identifier)}.
	 *
	 * @param equipmentType can be <code>null</code>.
	 */
	public void setEquipmentType(final EquipmentType equipmentType) {
		this.setEquipmentTypeId(Identifier.possiblyVoid(equipmentType));
	}

	/**
	 * @param label cannot be <code>null</code>. For this purpose, supply
	 *        an empty string as an argument.
	 */
	public void setLabel(final String label) {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		assert label != null: NON_NULL_EXPECTED;
		if (this.label.equals(label)) {
			return;
		}
		this.label = label;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0: OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeProtoElementId
	 */
	void setParentSchemeProtoElementId(final Identifier parentSchemeProtoElementId) {
		assert this.assertParentSetNonStrict(): OBJECT_BADLY_INITIALIZED;
		assert !parentSchemeProtoElementId.equals(this) : CIRCULAR_DEPS_PROHIBITED;
		assert parentSchemeProtoElementId.isVoid() || parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;

		if (this.parentSchemeProtoGroupId.isVoid()) {
			/*
			 * Moving from an element to another element.
			 */
			if (parentSchemeProtoElementId.isVoid()) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			if (this.parentSchemeProtoElementId.equals(parentSchemeProtoElementId)) {
				return;
			}
		} else {
			/*
			 * Moving from a group to an element.
			 */
			if (parentSchemeProtoElementId.isVoid()) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.parentSchemeProtoGroupId = VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
		super.markAsChanged();
	}

	/**
	 * <p>
	 * A wrapper around {@link #setParentSchemeProtoElementId(Identifier)}.
	 * </p>
	 *
	 * <p>
	 * If this <code>SchemeProtoElement</code> is initially inside another
	 * <code>SchemeProtoElement</code>, and
	 * <code>parentSchemeProtoElement</code> is <code>null</code>, then
	 * this <code>SchemeProtoElement</code> will delete itself from the
	 * pool. Alternatively, if this <code>SchemeProtoElement</code> is
	 * initially inside a <code>SchemeProtoGroup</code>, and
	 * <code>parentSchemeProtoElement</code> is <code>null</code>, then
	 * no action is taken.
	 * </p>
	 *
	 * @param parentSchemeProtoElement
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		this.setParentSchemeProtoElementId(Identifier.possiblyVoid(parentSchemeProtoElement));
	}

	/**
	 * @param parentSchemeProtoGroupId
	 */
	void setParentSchemeProtoGroupId(final Identifier parentSchemeProtoGroupId) {
		assert this.assertParentSetNonStrict() : OBJECT_BADLY_INITIALIZED;
		assert parentSchemeProtoGroupId.isVoid() || parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;

		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from a group to another group.
			 */
			if (parentSchemeProtoGroupId.isVoid()) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			if (this.parentSchemeProtoGroupId.equals(parentSchemeProtoGroupId)) {
				return;
			}
		} else {
			/*
			 * Moving from an element to a group.
			 */
			if (parentSchemeProtoGroupId.isVoid()) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
		super.markAsChanged();
	}

	/**
	 * <p>
	 * A wrapper around {@link #setParentSchemeProtoGroupId(Identifier)}.
	 * </p>
	 *
	 * <p>
	 * If this <code>SchemeProtoElement</code> is initially inside another
	 * <code>SchemeProtoElement</code>, and
	 * <code>parentSchemeProtoGroup</code> is <code>null</code>, then
	 * no action is taken. Alternatively, if this
	 * <code>SchemeProtoElement</code> is initially inside a
	 * <code>SchemeProtoGroup</code>, and
	 * <code>parentSchemeProtoGroup</code> is <code>null</code>, then
	 * this <code>SchemeProtoElement</code> will delete itself from the
	 * pool.
	 * </p>
	 *
	 * @param parentSchemeProtoGroup
	 */
	public void setParentSchemeProtoGroup(final SchemeProtoGroup parentSchemeProtoGroup) {
		this.setParentSchemeProtoGroupId(Identifier.possiblyVoid(parentSchemeProtoGroup));
	}

	/**
	 * @param schemeCellId
	 */
	void setSchemeCellId(final Identifier schemeCellId) {
		assert schemeCellId.isVoid() || schemeCellId.getMajor() == IMAGERESOURCE_CODE;
		if (this.schemeCellId.equals(schemeCellId)) {
			return;
		}
		this.schemeCellId = schemeCellId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSchemeCellId(Identifier)}.
	 *
	 * @param schemeCell
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(final SchemeImageResource schemeCell) {
		this.setSchemeCellId(Identifier.possiblyVoid(schemeCell));
	}

	/**
	 * @param schemeDevices
	 * @throws ApplicationException 
	 */
	public void setSchemeDevices(final Set<SchemeDevice> schemeDevices) throws ApplicationException {
		assert schemeDevices != null: NON_NULL_EXPECTED;
		final Set<SchemeDevice> oldSchemeDevices = this.getSchemeDevices0();
		/*
		 * Check is made to prevent SchemeDevices from
		 * permanently losing their parents.
		 */
		oldSchemeDevices.removeAll(schemeDevices);
		for (final SchemeDevice oldSchemeDevice : oldSchemeDevices) {
			this.removeSchemeDevice(oldSchemeDevice);
		}
		for (final SchemeDevice schemeDevice : schemeDevices) {
			this.addSchemeDevice(schemeDevice);
		}
	}

	/**
	 * @param schemeLinks
	 * @throws ApplicationException 
	 */
	public void setSchemeLinks(final Set<SchemeLink> schemeLinks) throws ApplicationException {
		assert schemeLinks != null: NON_NULL_EXPECTED;
		final Set<SchemeLink> oldSchemeLinks = this.getSchemeLinks0();
		/*
		 * Check is made to prevent SchemeLinks from
		 * permanently losing their parents.
		 */
		oldSchemeLinks.removeAll(schemeLinks);
		for (final SchemeLink oldSchemeLink : oldSchemeLinks) {
			this.removeSchemeLink(oldSchemeLink);
		}
		for (final SchemeLink schemeLink : schemeLinks) {
			this.addSchemeLink(schemeLink);
		}
	}

	/**
	 * @param schemeProtoElements
	 * @throws ApplicationException 
	 */
	public void setSchemeProtoElements(final Set<SchemeProtoElement> schemeProtoElements) throws ApplicationException {
		assert schemeProtoElements != null: NON_NULL_EXPECTED;
		final Set<SchemeProtoElement> oldSchemeProtoElements = this.getSchemeProtoElements0();
		/*
		 * Check is made to prevent SchemeProtoElements from
		 * permanently losing their parents.
		 */
		oldSchemeProtoElements.removeAll(schemeProtoElements);
		for (final SchemeProtoElement oldSchemeProtoElement : oldSchemeProtoElements) {
			this.removeSchemeProtoElement(oldSchemeProtoElement);
		}
		for (final SchemeProtoElement schemeProtoElement : schemeProtoElements) {
			this.addSchemeProtoElement(schemeProtoElement);
		}
	}

	/**
	 * @param symbolId
	 */
	void setSymbolId(final Identifier symbolId) {
		assert symbolId.isVoid() || symbolId.getMajor() == IMAGERESOURCE_CODE;
		if (this.symbolId.equals(symbolId)) {
			return;
		}
		this.symbolId = symbolId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setSymbolId(Identifier)}.
	 *
	 * @param symbol
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		this.setSymbolId(Identifier.possiblyVoid(symbol));
	}

	/**
	 * @param ugoCellId
	 */
	void setUgoCellId(final Identifier ugoCellId) {
		assert ugoCellId.isVoid() || ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		if (this.ugoCellId.equals(ugoCellId)) {
			return;
		}
		this.ugoCellId = ugoCellId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setUgoCellId(Identifier)}.
	 *
	 * @param ugoCell
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		this.setUgoCellId(Identifier.possiblyVoid(ugoCell));
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable)
	throws CreateObjectException {
		synchronized (this) {
			final IdlSchemeProtoElement schemeProtoElement = (IdlSchemeProtoElement) transferable;
			try {
				super.fromTransferable(schemeProtoElement);
			} catch (final CreateObjectException coe) {
				throw coe;
			} catch (final ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			this.name = schemeProtoElement.name;
			this.description = schemeProtoElement.description;
			this.label = schemeProtoElement.label;
			this.equipmentTypeId = new Identifier(schemeProtoElement.equipmentTypeId);
			this.symbolId = new Identifier(schemeProtoElement.symbolId);
			this.ugoCellId = new Identifier(schemeProtoElement.ugoCellId);
			this.schemeCellId = new Identifier(schemeProtoElement.schemeCellId);
			this.parentSchemeProtoGroupId = new Identifier(schemeProtoElement.parentSchemeProtoGroupId);
			this.parentSchemeProtoElementId = new Identifier(schemeProtoElement.parentSchemeProtoElementId);

			this.parentSet = true;
		}
	}

	/**
	 * @param schemeProtoElement
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeProtoElement schemeProtoElement,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, PRE_IMPORT);

		this.name = schemeProtoElement.getName();
		this.description = schemeProtoElement.isSetDescription()
				? schemeProtoElement.getDescription()
				: "";
		this.label = schemeProtoElement.isSetLabel()
				? schemeProtoElement.getLabel()
				: "";
		this.equipmentTypeId = schemeProtoElement.isSetEquipmentTypeId()
				? Identifier.fromXmlTransferable(schemeProtoElement.getEquipmentTypeId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.symbolId = schemeProtoElement.isSetSymbolId()
				? Identifier.fromXmlTransferable(schemeProtoElement.getSymbolId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.ugoCellId = schemeProtoElement.isSetUgoCellId()
				? Identifier.fromXmlTransferable(schemeProtoElement.getUgoCellId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.schemeCellId = schemeProtoElement.isSetSchemeCellId()
				? Identifier.fromXmlTransferable(schemeProtoElement.getSchemeCellId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		final boolean setParentSchemeProtoGroupId = schemeProtoElement.isSetParentSchemeProtoGroupId();
		final boolean setParentSchemeProtoElementId = schemeProtoElement.isSetParentSchemeProtoElementId();
		if (setParentSchemeProtoGroupId) {
			assert !setParentSchemeProtoElementId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeProtoGroupId = Identifier.fromXmlTransferable(schemeProtoElement.getParentSchemeProtoGroupId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeProtoElementId) {
			assert !setParentSchemeProtoGroupId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeProtoGroupId = VOID_IDENTIFIER;
			this.parentSchemeProtoElementId = Identifier.fromXmlTransferable(schemeProtoElement.getParentSchemeProtoElementId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeProtoElement.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}
		if (schemeProtoElement.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : schemeProtoElement.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}
		if (schemeProtoElement.isSetSchemeProtoElements()) {
			for (final XmlSchemeProtoElement schemeProtoElement2 : schemeProtoElement.getSchemeProtoElements().getSchemeProtoElementArray()) {
				createInstance(super.creatorId, schemeProtoElement2, importType);
			}
		}
		if (schemeProtoElement.isSetSchemeDevices()) {
			for (final XmlSchemeDevice schemeDevice : schemeProtoElement.getSchemeDevices().getSchemeDeviceArray()) {
				SchemeDevice.createInstance(super.creatorId, schemeDevice, importType);
			}
		}
		if (schemeProtoElement.isSetSchemeLinks()) {
			for (final XmlSchemeLink schemeLink : schemeProtoElement.getSchemeLinks().getSchemeLinkArray()) {
				SchemeLink.createInstance(super.creatorId, schemeLink, importType);
			}
		}

		this.parentSet = true;

		XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, POST_IMPORT);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertParentSetNonStrict() {
		if (this.parentSet) {
			return this.assertParentSetStrict();
		}
		this.parentSet = true;
		return this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& this.parentSchemeProtoGroupId.isVoid()
				&& this.parentSchemeProtoElementId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertParentSetStrict() {
		return this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid());
	}

	/**
	 * Returns <code>SchemeCablePort</code>s (as an unmodifiable set) for
	 * this <code>schemeProtoElement</code>, recursively.
	 *
	 * @throws ApplicationException
	 */
	public Set<SchemeCablePort> getSchemeCablePortsRecursively()
	throws ApplicationException {
		final Set<SchemeCablePort> schemeCablePorts = new HashSet<SchemeCablePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
			schemeCablePorts.addAll(schemeDevice.getSchemeCablePorts0());
		}
		for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0()) {
			schemeCablePorts.addAll(schemeProtoElement.getSchemeCablePortsRecursively());
		}
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	/**
	 * Returns <code>SchemePort</code>s (as an unmodifiable set) for this
	 * <code>SchemeProtoElement</code>, recursively.
	 *
	 * @throws ApplicationException
	 */
	public Set<SchemePort> getSchemePortsRecursively()
	throws ApplicationException {
		final Set<SchemePort> schemePorts = new HashSet<SchemePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
			schemePorts.addAll(schemeDevice.getSchemePorts0());
		}
		for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0()) {
			schemePorts.addAll(schemeProtoElement.getSchemePortsRecursively());
		}
		return Collections.unmodifiableSet(schemePorts);
	}
}
