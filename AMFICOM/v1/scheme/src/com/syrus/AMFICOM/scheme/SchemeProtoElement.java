/*-
 * $Id: SchemeProtoElement.java,v 1.136 2006/03/15 19:50:28 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
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

import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
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
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #02 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.136 $, $Date: 2006/03/15 19:50:28 $
 * @module scheme
 */
public final class SchemeProtoElement
		extends AbstractCloneableStorableObject
		implements Describable, SchemeCellContainer,
		Characterizable, ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeProtoElement>,
		IdlTransferableObjectExt<IdlSchemeProtoElement> {
	private static final long serialVersionUID = 3689348806202569782L;

	private String name;

	private String description;

	private String label;

	Identifier protoEquipmentId;

	Identifier symbolId;

	Identifier ugoCellId;

	Identifier schemeCellId;

	Identifier parentSchemeProtoGroupId;

	Identifier parentSchemeProtoElementId;

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
	 * @param protoEquipment
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
			final ProtoEquipment protoEquipment,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup,
			final SchemeProtoElement parentSchemeProtoElement) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.label = label;
		this.protoEquipmentId = Identifier.possiblyVoid(protoEquipment);
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
		super(id, importType, SCHEMEPROTOELEMENT_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeProtoElement(final IdlSchemeProtoElement transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoElement)}.
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
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeProtoGroup)}.
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
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param protoEquipment may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoElement cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeProtoElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final ProtoEquipment protoEquipment,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoElement parentSchemeProtoElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					INITIAL_VERSION,
					name,
					description,
					label,
					protoEquipment,
					symbol,
					ugoCell,
					schemeCell,
					null,
					parentSchemeProtoElement);
			parentSchemeProtoElement.getSchemeProtoElementContainerWrappee().addToCache(schemeProtoElement, usePool);

			schemeProtoElement.markAsChanged();
			return schemeProtoElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param protoEquipment may be <code>null</code>.
	 * @param symbol may be <code>null</code>.
	 * @param ugoCell may be <code>null</code>.
	 * @param schemeCell may be <code>null</code>.
	 * @param parentSchemeProtoGroup cannot be <code>null</code>.
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeProtoElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final ProtoEquipment protoEquipment,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeProtoGroup parentSchemeProtoGroup)
	throws CreateObjectException {
		final boolean usePool = false;

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
					INITIAL_VERSION,
					name,
					description,
					label,
					protoEquipment,
					symbol,
					ugoCell,
					schemeCell,
					parentSchemeProtoGroup,
					null);
			parentSchemeProtoGroup.getSchemeProtoElementContainerWrappee().addToCache(schemeProtoElement, usePool);

			schemeProtoElement.markAsChanged();
			return schemeProtoElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoElement.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
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
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeProtoElement clone() throws CloneNotSupportedException {
		final boolean usePool = false;

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
			clone.characteristicContainerWrappee = null;
			for (final Characteristic characteristic : this.getCharacteristics0(usePool)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				clone.addCharacteristic(characteristicClone, usePool);
			}
			clone.schemeDeviceContainerWrappee = null;
			for (final SchemeDevice schemeDevice : this.getSchemeDevices0(usePool)) {
				final SchemeDevice schemeDeviceClone = schemeDevice.clone();
				clone.clonedIdMap.putAll(schemeDeviceClone.getClonedIdMap());
				clone.addSchemeDevice(schemeDeviceClone, usePool);
			}
			clone.schemeLinkContainerWrappee = null;
			for (final SchemeLink schemeLink : this.getSchemeLinks0(usePool)) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				clone.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				clone.addSchemeLink(schemeLinkClone, usePool);
			}
			clone.schemeProtoElementContainerWrappee = null;
			for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0(usePool)) {
				final SchemeProtoElement schemeProtoElementClone = schemeProtoElement.clone();
				clone.clonedIdMap.putAll(schemeProtoElementClone.getClonedIdMap());
				clone.addSchemeProtoElement(schemeProtoElementClone, usePool);
			}

			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : clone.getSchemeLinks0(usePool)) {
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
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependenciesTmpl()
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.protoEquipmentId != null
				&& this.symbolId != null
				&& this.ugoCellId != null
				&& this.schemeCellId != null
				&& this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.protoEquipmentId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.parentSchemeProtoGroupId);
		dependencies.add(this.parentSchemeProtoElementId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeDevices0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeLinks0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeProtoElements0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
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

	Identifier getProtoEquipmentId() {
		assert this.protoEquipmentId != null: OBJECT_NOT_INITIALIZED;
		assert this.protoEquipmentId.isVoid() || this.protoEquipmentId.getMajor() == PROTOEQUIPMENT_CODE;
		return this.protoEquipmentId;
	}

	/**
	 * A wrapper around {@link #getProtoEquipmentId()}.
	 *
	 * @return <code>protoEquipment</code> associated with this
	 *         <code>schemeProtoElement</code>, or <code>null</code> if
	 *         none.
	 * @throws ApplicationException
	 */
	public ProtoEquipment getProtoEquipment() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getProtoEquipmentId(), true);
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
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (parentSchemeProtoElementIdVoid) {
			Log.debugMessage("Parent SchemeProtoElement was requested, while parent is a SchemeProtoGroup; returning null.",
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
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeProtoGroupId() {
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeProtoGroupId.isVoid() ^ this.parentSchemeProtoElementId.isVoid()) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoGroupIdVoid = this.parentSchemeProtoGroupId.isVoid();
		assert parentSchemeProtoGroupIdVoid || this.parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;
		if (parentSchemeProtoGroupIdVoid) {
			Log.debugMessage("Parent SchemeProtoGroup was requested, while parent is a SchemeProtoElement; returnning null",
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
			Log.debugMessage(ae, SEVERE);
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
			Log.debugMessage(ae, SEVERE);
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
			Log.debugMessage(ae, SEVERE);
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeProtoElement getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSchemeProtoElementHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.label,
				this.protoEquipmentId.getIdlTransferable(),
				this.symbolId.getIdlTransferable(),
				this.ugoCellId.getIdlTransferable(),
				this.schemeCellId.getIdlTransferable(),
				this.parentSchemeProtoGroupId.getIdlTransferable(),
				this.parentSchemeProtoElementId.getIdlTransferable());
	}

	/**
	 * @param schemeProtoElement
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeProtoElement schemeProtoElement,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
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
			if (schemeProtoElement.isSetProtoEquipmentId()) {
				schemeProtoElement.unsetProtoEquipmentId();
			}
			if (!this.protoEquipmentId.isVoid()) {
				this.protoEquipmentId.getXmlTransferable(schemeProtoElement.addNewProtoEquipmentId(), importType);
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
			final Set<Characteristic> characteristics = this.getCharacteristics0(usePool);
			if (!characteristics.isEmpty()) {
				final XmlCharacteristicSeq xmlCharacteristicSeq = schemeProtoElement.addNewCharacteristics();
				for (final Characteristic characteristic : characteristics) {
					characteristic.getXmlTransferable(xmlCharacteristicSeq.addNewCharacteristic(), importType, usePool);
				}
			}
			if (schemeProtoElement.isSetSchemeProtoElements()) {
				schemeProtoElement.unsetSchemeProtoElements();
			}
			final Set<SchemeProtoElement> schemeProtoElements = this.getSchemeProtoElements0(usePool);
			if (!schemeProtoElements.isEmpty()) {
				final XmlSchemeProtoElementSeq schemeProtoElementSeq = schemeProtoElement.addNewSchemeProtoElements();
				for (final SchemeProtoElement schemeProtoElement2 : schemeProtoElements) {
					schemeProtoElement2.getXmlTransferable(schemeProtoElementSeq.addNewSchemeProtoElement(), importType, usePool);
				}
			}
			if (schemeProtoElement.isSetSchemeDevices()) {
				schemeProtoElement.unsetSchemeDevices();
			}
			final Set<SchemeDevice> schemeDevices = this.getSchemeDevices0(usePool);
			if (!schemeDevices.isEmpty()) {
				final XmlSchemeDeviceSeq schemeDeviceSeq = schemeProtoElement.addNewSchemeDevices();
				for (final SchemeDevice schemeDevice : schemeDevices) {
					schemeDevice.getXmlTransferable(schemeDeviceSeq.addNewSchemeDevice(), importType, usePool);
				}
			}
			if (schemeProtoElement.isSetSchemeLinks()) {
				schemeProtoElement.unsetSchemeLinks();
			}
			final Set<SchemeLink> schemeLinks = this.getSchemeLinks0(usePool);
			if (!schemeLinks.isEmpty()) {
				final XmlSchemeLinkSeq schemeLinkSeq = schemeProtoElement.addNewSchemeLinks();
				for (final SchemeLink schemeLink : schemeLinks) {
					schemeLink.getXmlTransferable(schemeLinkSeq.addNewSchemeLink(), importType, usePool);
				}
			}
			XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
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
			Log.debugMessage(ae, SEVERE);
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
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name can be neither <code>null</code> nor an empty string.
	 * @param description cannot be <code>null</code>. For this purpose,
	 *        supply an empty string as an argument.
	 * @param label
	 * @param protoEquipmentId
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
			final Identifier protoEquipmentId,
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
			assert protoEquipmentId != null : NON_NULL_EXPECTED;
			assert symbolId != null : NON_NULL_EXPECTED;
			assert ugoCellId != null : NON_NULL_EXPECTED;
			assert schemeCellId != null : NON_NULL_EXPECTED;
	
			assert parentSchemeProtoGroupId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoGroupId.isVoid() ^ parentSchemeProtoElementId.isVoid();
	
			this.name = name;
			this.description = description;
			this.label = label;
			this.protoEquipmentId = protoEquipmentId;
			this.symbolId = symbolId;
			this.ugoCellId = ugoCellId;
			this.schemeCellId = schemeCellId;
			this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
			this.parentSchemeProtoElementId = parentSchemeProtoElementId;
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
	 * @param protoEquipmentId
	 */
	void setProtoEquipmentId(final Identifier protoEquipmentId) {
		assert protoEquipmentId.isVoid() || protoEquipmentId.getMajor() == PROTOEQUIPMENT_CODE;
		if (this.protoEquipmentId.equals(protoEquipmentId)) {
			return;
		}
		this.protoEquipmentId = protoEquipmentId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setProtoEquipmentId(Identifier)}.
	 *
	 * @param protoEquipment can be <code>null</code>.
	 */
	public void setProtoEquipment(final ProtoEquipment protoEquipment) {
		this.setProtoEquipmentId(Identifier.possiblyVoid(protoEquipment));
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
	 * A wrapper around {@link #setParentSchemeProtoElement(SchemeProtoElement, boolean)}.
	 *
	 * @param parentSchemeProtoElementId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeProtoElementId(
			final Identifier parentSchemeProtoElementId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId.isVoid() || parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;

		if (this.parentSchemeProtoElementId.equals(parentSchemeProtoElementId)) {
			return;
		}

		this.setParentSchemeProtoElement(
				StorableObjectPool.<SchemeProtoElement>getStorableObject(parentSchemeProtoElementId, true), 
				usePool);
	}

	/**
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
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeProtoElement(
			final SchemeProtoElement parentSchemeProtoElement,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null : OBJECT_BADLY_INITIALIZED;
		final boolean thisParentSchemeProtoGroupIdVoid = this.parentSchemeProtoGroupId.isVoid();
		assert thisParentSchemeProtoGroupIdVoid ^ this.parentSchemeProtoElementId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert parentSchemeProtoElement == null || !parentSchemeProtoElement.equals(this) : CIRCULAR_DEPS_PROHIBITED;

		final boolean parentSchemeProtoElementNull = (parentSchemeProtoElement == null);

		final Identifier newParentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeProtoGroupIdVoid) {
			/*
			 * Moving from a protoelement to another protoelement.
			 * At this point, newParentSchemeProtoElementId may be void.
			 */
			this.getParentSchemeProtoElement().getSchemeProtoElementContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeProtoElementNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		} else {
			/*
			 * Moving from a protogroup to a protoelement. At this
			 * point, newParentSchemeProtoElementId is non-void.
			 */
			this.getParentSchemeProtoGroup().getSchemeProtoElementContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeProtoGroupId = VOID_IDENTIFIER;
		}

		if (!parentSchemeProtoElementNull) {
			parentSchemeProtoElement.getSchemeProtoElementContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeProtoGroup(SchemeProtoGroup, boolean)}.
	 *
	 * @param parentSchemeProtoGroupId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeProtoGroupId(
			final Identifier parentSchemeProtoGroupId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeProtoGroupId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoGroupId.isVoid() || parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;

		if (this.parentSchemeProtoGroupId.equals(parentSchemeProtoGroupId)) {
			return;
		}

		this.setParentSchemeProtoGroup(
				StorableObjectPool.<SchemeProtoGroup>getStorableObject(parentSchemeProtoGroupId, true),
				usePool);
	}

	/**
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
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeProtoGroup(
			final SchemeProtoGroup parentSchemeProtoGroup,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeProtoGroupId != null
				&& this.parentSchemeProtoElementId != null : OBJECT_BADLY_INITIALIZED;
		final boolean thisParentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert this.parentSchemeProtoGroupId.isVoid() ^ thisParentSchemeProtoElementIdVoid : EXACTLY_ONE_PARENT_REQUIRED;

		final boolean parentSchemeProtoGroupNull = (parentSchemeProtoGroup == null);

		final Identifier newParentSchemeProtoGroupId = Identifier.possiblyVoid(parentSchemeProtoGroup);
		if (this.parentSchemeProtoGroupId.equals(newParentSchemeProtoGroupId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeProtoElementIdVoid) {
			/*
			 * Moving from a protogroup to another protogroup.
			 * At this point, newParentSchemeProtoGroupId may be void.
			 */
			this.getParentSchemeProtoGroup().getSchemeProtoElementContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeProtoGroupNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		} else {
			/*
			 * Moving from a protoelement to a protogroup. At this
			 * point, newParentSchemeProtoGroupId is non-void.
			 */
			this.getParentSchemeProtoElement().getSchemeProtoElementContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}

		if (!parentSchemeProtoGroupNull) {
			parentSchemeProtoGroup.getSchemeProtoElementContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeProtoGroupId = newParentSchemeProtoGroupId;
		super.markAsChanged();
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
	 * @param schemeProtoElement
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	public void fromIdlTransferable(final IdlSchemeProtoElement schemeProtoElement)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeProtoElement);
			this.name = schemeProtoElement.name;
			this.description = schemeProtoElement.description;
			this.label = schemeProtoElement.label;
			this.protoEquipmentId = new Identifier(schemeProtoElement.protoEquipmentId);
			this.symbolId = new Identifier(schemeProtoElement.symbolId);
			this.ugoCellId = new Identifier(schemeProtoElement.ugoCellId);
			this.schemeCellId = new Identifier(schemeProtoElement.schemeCellId);
			this.parentSchemeProtoGroupId = new Identifier(schemeProtoElement.parentSchemeProtoGroupId);
			this.parentSchemeProtoElementId = new Identifier(schemeProtoElement.parentSchemeProtoElementId);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param schemeProtoElement
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeProtoElement schemeProtoElement,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, PRE_IMPORT);
	
			this.name = schemeProtoElement.getName();
			this.description = schemeProtoElement.isSetDescription()
					? schemeProtoElement.getDescription()
					: "";
			this.label = schemeProtoElement.isSetLabel()
					? schemeProtoElement.getLabel()
					: "";
			this.protoEquipmentId = schemeProtoElement.isSetProtoEquipmentId()
					? Identifier.fromXmlTransferable(schemeProtoElement.getProtoEquipmentId(), importType, MODE_THROW_IF_ABSENT)
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
				throw new XmlConversionException(
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
	
			XmlComplementorRegistry.complementStorableObject(schemeProtoElement, SCHEMEPROTOELEMENT_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeProtoElementWrapper getWrapper() {
		return SchemeProtoElementWrapper.getInstance();
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

	/*-********************************************************************
	 * Children manipulation: scheme devices                              *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeDevice> schemeDeviceContainerWrappee;

	StorableObjectContainerWrappee<SchemeDevice> getSchemeDeviceContainerWrappee() {
		return (this.schemeDeviceContainerWrappee == null)
				? this.schemeDeviceContainerWrappee = new StorableObjectContainerWrappee<SchemeDevice>(this, SCHEMEDEVICE_CODE)
				: this.schemeDeviceContainerWrappee;
	}

	/**
	 * @param schemeDevice cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeDevice(final SchemeDevice schemeDevice,
			final boolean usePool)
	throws ApplicationException {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		schemeDevice.setParentSchemeProtoElement(this, usePool);
	}

	/**
	 * The <code>SchemeDevice</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeDevice
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeDevice(final SchemeDevice schemeDevice,
			final boolean usePool)
	throws ApplicationException {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		assert schemeDevice.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeDevice.setParentSchemeProtoElement(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeDevice> getSchemeDevices(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeDevices0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeDevice> getSchemeDevices0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeDeviceContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeDevices
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeDevices(final Set<SchemeDevice> schemeDevices,
			final boolean usePool)
	throws ApplicationException {
		assert schemeDevices != null: NON_NULL_EXPECTED;

		final Set<SchemeDevice> oldSchemeDevices = this.getSchemeDevices0(usePool);

		final Set<SchemeDevice> toRemove = new HashSet<SchemeDevice>(oldSchemeDevices);
		toRemove.removeAll(schemeDevices);
		for (final SchemeDevice schemeDevice : toRemove) {
			this.removeSchemeDevice(schemeDevice, usePool);
		}

		final Set<SchemeDevice> toAdd = new HashSet<SchemeDevice>(schemeDevices);
		toAdd.removeAll(oldSchemeDevices);
		for (final SchemeDevice schemeDevice : toAdd) {
			this.addSchemeDevice(schemeDevice, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme links                                *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeLink> schemeLinkContainerWrappee;

	StorableObjectContainerWrappee<SchemeLink> getSchemeLinkContainerWrappee() {
		return (this.schemeLinkContainerWrappee == null)
				? this.schemeLinkContainerWrappee = new StorableObjectContainerWrappee<SchemeLink>(this, SCHEMELINK_CODE)
				: this.schemeLinkContainerWrappee;
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeLink(final SchemeLink schemeLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLink != null: NON_NULL_EXPECTED;
		schemeLink.setParentSchemeProtoElement(this, usePool);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeLink
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeLink(final SchemeLink schemeLink,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLink != null: NON_NULL_EXPECTED;
		assert schemeLink.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentSchemeProtoElement(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeLink> getSchemeLinks(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeLinks0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeLink> getSchemeLinks0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeLinkContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeLinks
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeLinks(final Set<SchemeLink> schemeLinks,
			final boolean usePool)
	throws ApplicationException {
		assert schemeLinks != null: NON_NULL_EXPECTED;

		final Set<SchemeLink> oldSchemeLinks = this.getSchemeLinks0(usePool);

		final Set<SchemeLink> toRemove = new HashSet<SchemeLink>(oldSchemeLinks);
		toRemove.removeAll(schemeLinks);
		for (final SchemeLink schemeLink : toRemove) {
			this.removeSchemeLink(schemeLink, usePool);
		}

		final Set<SchemeLink> toAdd = new HashSet<SchemeLink>(schemeLinks);
		toAdd.removeAll(oldSchemeLinks);
		for (final SchemeLink schemeLink : toAdd) {
			this.addSchemeLink(schemeLink, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme protoelements                        *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeProtoElement> schemeProtoElementContainerWrappee;

	StorableObjectContainerWrappee<SchemeProtoElement> getSchemeProtoElementContainerWrappee() {
		return (this.schemeProtoElementContainerWrappee == null)
				? this.schemeProtoElementContainerWrappee = new StorableObjectContainerWrappee<SchemeProtoElement>(this, SCHEMEPROTOELEMENT_CODE)
				: this.schemeProtoElementContainerWrappee;
	}

	/**
	 * @param schemeProtoElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeProtoElement(final SchemeProtoElement schemeProtoElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		assert schemeProtoElement != this: CIRCULAR_DEPS_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(this, usePool);
	}

	/**
	 * The <code>SchemeProtoElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeProtoElement(
			final SchemeProtoElement schemeProtoElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		assert schemeProtoElement.getParentSchemeProtoElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoElement(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeProtoElement> getSchemeProtoElements(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeProtoElements0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeProtoElement> getSchemeProtoElements0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeProtoElementContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeProtoElements
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeProtoElements(
			final Set<SchemeProtoElement> schemeProtoElements,
			final boolean usePool)
	throws ApplicationException {
		assert schemeProtoElements != null: NON_NULL_EXPECTED;

		final Set<SchemeProtoElement> oldSchemeProtoElements = this.getSchemeProtoElements0(usePool);

		final Set<SchemeProtoElement> toRemove = new HashSet<SchemeProtoElement>(oldSchemeProtoElements);
		toRemove.removeAll(schemeProtoElements);
		for (final SchemeProtoElement schemeProtoElement : toRemove) {
			this.removeSchemeProtoElement(schemeProtoElement, usePool);
		}

		final Set<SchemeProtoElement> toAdd = new HashSet<SchemeProtoElement>(schemeProtoElements);
		toAdd.removeAll(oldSchemeProtoElements);
		for (final SchemeProtoElement schemeProtoElement : toAdd) {
			this.addSchemeProtoElement(schemeProtoElement, usePool);
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Returns <code>SchemeCablePort</code>s (as an unmodifiable set) for
	 * this <code>schemeProtoElement</code>, recursively.
	 *
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeCablePort> getSchemeCablePortsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeCablePort> schemeCablePorts = new HashSet<SchemeCablePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0(usePool)) {
			schemeCablePorts.addAll(schemeDevice.getSchemeCablePorts0(usePool));
		}
		for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0(usePool)) {
			schemeCablePorts.addAll(schemeProtoElement.getSchemeCablePortsRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	/**
	 * Returns <code>SchemePort</code>s (as an unmodifiable set) for this
	 * <code>SchemeProtoElement</code>, recursively.
	 *
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemePort> getSchemePortsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemePort> schemePorts = new HashSet<SchemePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0(usePool)) {
			schemePorts.addAll(schemeDevice.getSchemePorts0(usePool));
		}
		for (final SchemeProtoElement schemeProtoElement : this.getSchemeProtoElements0(usePool)) {
			schemePorts.addAll(schemeProtoElement.getSchemePortsRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemePorts);
	}
}
