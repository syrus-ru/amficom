/*-
 * $Id: SchemeCableLink.java,v 1.131 2006/06/02 17:23:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.COMMON_COLOUR;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
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
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLinkHelper;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItemSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThreadSeq;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #13 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.131 $, $Date: 2006/06/02 17:23:20 $
 * @module scheme
 */
public final class SchemeCableLink extends AbstractSchemeLink
		implements PathOwner<CableChannelingItem>,
		XmlTransferableObject<XmlSchemeCableLink>,
		IdlTransferableObjectExt<IdlSchemeCableLink> {
	private static final long serialVersionUID = 3760847878314274867L;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	SchemeCableLink(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final CableLink cableLink,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) {
		super(id,
				created,
				modified,
				creatorId,
				modifierId,
				version,
				name,
				description,
				physicalLength,
				opticalLength,
				cableLinkType,
				cableLink,
				sourceSchemeCablePort,
				targetSchemeCablePort,
				parentScheme);
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
	private SchemeCableLink(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMECABLELINK_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCableLink(final IdlSchemeCableLink transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, CableLinkType, CableLink, SchemeCablePort, SchemeCablePort, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId, final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null, null, null, parentScheme);
	}

	
	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkType
	 * @param cableLink
	 * @param sourceSchemeCablePort
	 * @param targetSchemeCablePort
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final CableLink cableLink,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeCableLink schemeCableLink = new SchemeCableLink(IdentifierPool.getGeneratedIdentifier(SCHEMECABLELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					INITIAL_VERSION,
					name,
					description,
					physicalLength,
					opticalLength,
					cableLinkType,
					cableLink,
					sourceSchemeCablePort,
					targetSchemeCablePort,
					parentScheme);
			parentScheme.getSchemeCableLinkContainerWrappee().addToCache(schemeCableLink, usePool);

			schemeCableLink.abstractLinkTypeSet = (cableLink != null || cableLinkType != null);

			schemeCableLink.markAsChanged();
			return schemeCableLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeCableLink.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeCableLink
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final XmlSchemeCableLink xmlSchemeCableLink,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemeCableLink.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeCableLink schemeCableLink;
			if (id.isVoid()) {
				schemeCableLink = new SchemeCableLink(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeCableLink = StorableObjectPool.getStorableObject(id, true);
				if (schemeCableLink == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeCableLink = new SchemeCableLink(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeCableLink.fromXmlTransferable(xmlSchemeCableLink, importType);
			assert schemeCableLink.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeCableLink.markAsChanged();
			return schemeCableLink;
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
	public SchemeCableLink clone() throws CloneNotSupportedException {
		final boolean usePool = false;

		final StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
		final int depth = 1;
		if (stackTrace.length > depth) {
			final StackTraceElement stackTraceElement = stackTrace[depth];
			final String className = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			if (!(className.equals(Scheme.class.getName())
					&& methodName.equals("clone"))) {
				final StackTraceElement rootStackTraceElement = stackTrace[depth - 1];
				throw new CloneNotSupportedException(
						"invocation of "
						+ rootStackTraceElement.getClassName()
						+ '.' + rootStackTraceElement.getMethodName()
						+ '(' + rootStackTraceElement.getFileName()
						+ ':' + (rootStackTraceElement.getLineNumber() - 1)
						+ ") from " + className + '.' + methodName + '('
						+ stackTraceElement.getFileName() + ':'
						+ stackTraceElement.getLineNumber() + ')'
						+ " is prohibited");
			}
		}
		try {
			final SchemeCableLink clone = (SchemeCableLink) super.clone();

			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			clone.clonedIdMap.put(this.id, clone.id);

			clone.characteristicContainerWrappee = null;
			for (final Characteristic characteristic : this.getCharacteristics0(usePool)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				clone.addCharacteristic(characteristicClone, usePool);
			}
			clone.schemeCableThreadContainerWrappee = null;
			for (final SchemeCableThread schemeCableThread : this.getSchemeCableThreads0(usePool)) {
				final SchemeCableThread schemeCableThreadClone = schemeCableThread.clone();
				clone.clonedIdMap.putAll(schemeCableThreadClone.getClonedIdMap());
				clone.addSchemeCableThread(schemeCableThreadClone, usePool);
			}
			/*
			 * Though CableChannelingItems themselves aren't being
			 * cloned, we still must prevent clone from sharing its
			 * cache with the original.
			 */
			clone.cableChannelingItemContainerWrappee = null;
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkId()
	 */
	@Override
	Identifier getAbstractLinkId() {
		final Identifier cableLinkId = super.getAbstractLinkId();
		assert cableLinkId.isVoid() || cableLinkId.getMajor() == CABLELINK_CODE;
		return cableLinkId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public CableLink getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof CableLink : OBJECT_BADLY_INITIALIZED;
		return (CableLink) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkTypeId()
	 */
	@Override
	public Identifier getAbstractLinkTypeId() {
		final Identifier cableLinkTypeId = super.getAbstractLinkTypeId();
		assert cableLinkTypeId.isVoid() || cableLinkTypeId.getMajor() == CABLELINK_TYPE_CODE;
		return cableLinkTypeId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public CableLinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof CableLinkType: OBJECT_BADLY_INITIALIZED;
		return (CableLinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentSchemeId()
	 */
	@Override
	public Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert !parentSchemeId1.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		return parentSchemeId1;
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	public Identifier getSourceAbstractSchemePortId() {
		final Identifier sourceSchemeCablePortId = super.getSourceAbstractSchemePortId();
		assert sourceSchemeCablePortId.isVoid() || sourceSchemeCablePortId.getMajor() == SCHEMECABLEPORT_CODE;
		return sourceSchemeCablePortId;
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePortId()
	 */
	@Override
	public Identifier getTargetAbstractSchemePortId() {
		final Identifier targetSchemeCablePortId = super.getTargetAbstractSchemePortId();
		assert targetSchemeCablePortId.isVoid() || targetSchemeCablePortId.getMajor() == SCHEMECABLEPORT_CODE;
		return targetSchemeCablePortId;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemeCablePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: OBJECT_BADLY_INITIALIZED;
		return (SchemeCablePort) targetAbstractSchemePort;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCableLink getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSchemeCableLinkHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				this.getAbstractLinkTypeId().getIdlTransferable(),
				this.getAbstractLinkId().getIdlTransferable(),
				this.getSourceAbstractSchemePortId().getIdlTransferable(),
				this.getTargetAbstractSchemePortId().getIdlTransferable(),
				this.getParentSchemeId().getIdlTransferable());
	}

	/**
	 * @param schemeCableLink
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeCableLink schemeCableLink,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.getXmlTransferable(schemeCableLink, importType, usePool);
			if (schemeCableLink.isSetCableLinkTypeId()) {
				schemeCableLink.unsetCableLinkTypeId();
			}
			if (!super.abstractLinkTypeId.isVoid()) {
				super.abstractLinkTypeId.getXmlTransferable(schemeCableLink.addNewCableLinkTypeId(), importType);
			}
			if (schemeCableLink.isSetCableLinkId()) {
				schemeCableLink.unsetCableLinkId();
			}
			if (!super.abstractLinkId.isVoid()) {
				super.abstractLinkId.getXmlTransferable(schemeCableLink.addNewCableLinkId(), importType);
			}
			if (schemeCableLink.isSetSourceSchemeCablePortId()) {
				schemeCableLink.unsetSourceSchemeCablePortId();
			}
			if (!super.sourceAbstractSchemePortId.isVoid()) {
				super.sourceAbstractSchemePortId.getXmlTransferable(schemeCableLink.addNewSourceSchemeCablePortId(), importType);
			}
			if (schemeCableLink.isSetTargetSchemeCablePortId()) {
				schemeCableLink.unsetTargetSchemeCablePortId();
			}
			if (!super.targetAbstractSchemePortId.isVoid()) {
				super.targetAbstractSchemePortId.getXmlTransferable(schemeCableLink.addNewTargetSchemeCablePortId(), importType);
			}
			super.parentSchemeId.getXmlTransferable(schemeCableLink.addNewParentSchemeId(), importType);
			if (schemeCableLink.isSetSchemeCableThreads()) {
				schemeCableLink.unsetSchemeCableThreads();
			}
			final Set<SchemeCableThread> schemeCableThreads = this.getSchemeCableThreads0(usePool);
			if (!schemeCableThreads.isEmpty()) {
				final XmlSchemeCableThreadSeq schemeCableThreadSeq = schemeCableLink.addNewSchemeCableThreads();
				for (final SchemeCableThread schemeCableThread : schemeCableThreads) {
					schemeCableThread.getXmlTransferable(schemeCableThreadSeq.addNewSchemeCableThread(), importType, usePool);
				}
			}
			if (schemeCableLink.isSetCableChannelingItems()) {
				schemeCableLink.unsetCableChannelingItems();
			}
			final SortedSet<CableChannelingItem> cableChannelingItems = this.getPathMembers0();
			if (!cableChannelingItems.isEmpty()) {
				final XmlCableChannelingItemSeq cableChannelingItemSeq = schemeCableLink.addNewCableChannelingItems();
				for (final CableChannelingItem cableChannelingItem : cableChannelingItems) {
					cableChannelingItem.getXmlTransferable(cableChannelingItemSeq.addNewCableChannelingItem(), importType, usePool);
				}
			}
			XmlComplementorRegistry.complementStorableObject(schemeCableLink, SCHEMECABLELINK_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param cableLinkTypeId
	 * @param cableLinkId
	 * @param sourceSchemeCablePortId
	 * @param targetSchemeCablePortId
	 * @param parentSchemeId
	 * @see AbstractSchemeLink#setAttributes(Date, Date, Identifier, Identifier, StorableObjectVersion, String, String, double, double, Identifier, Identifier, Identifier, Identifier, Identifier)
	 */
	@Override
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier cableLinkTypeId,
			final Identifier cableLinkId,
			final Identifier sourceSchemeCablePortId,
			final Identifier targetSchemeCablePortId,
			final Identifier parentSchemeId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				name,
				description,
				physicalLength,
				opticalLength,
				cableLinkTypeId,
				cableLinkId,
				sourceSchemeCablePortId,
				targetSchemeCablePortId,
				parentSchemeId);

		assert !parentSchemeId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
	}

	/**
	 * @param abstractLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	@Override
	public void setAbstractLink(final AbstractLink abstractLink) {
		assert abstractLink == null || abstractLink instanceof CableLink : NATURE_INVALID;
		this.setAbstractLink((CableLink) abstractLink);
	}

	/**
	 * @param cableLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	public void setAbstractLink(final CableLink cableLink) {
		super.setAbstractLink(cableLink);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	@Override
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof CableLinkType : NATURE_INVALID;
		this.setAbstractLinkType((CableLinkType) abstractLinkType);
	}

	/**
	 * @param cableLinkType
	 */
	public void setAbstractLinkType(final CableLinkType cableLinkType) {
		super.setAbstractLinkType(cableLinkType);
	}

	/**
	 * @param parentScheme
	 * @param usePool
	 * @throws ApplicationException
	 * @see AbstractSchemeElement#setParentScheme(Scheme, boolean)
	 */
	@Override
	public void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			return;
		}

		this.getParentScheme().getSchemeCableLinkContainerWrappee().removeFromCache(this, usePool);

		if (parentScheme == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentScheme.getSchemeCableLinkContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeId = newParentSchemeId;
		super.markAsChanged();
	}

	/**
	 * @param sourceAbstractSchemePortId
	 * @see AbstractSchemeLink#setSourceAbstractSchemePortId(Identifier)
	 */
	@Override
	void setSourceAbstractSchemePortId(final Identifier sourceAbstractSchemePortId) {
		assert sourceAbstractSchemePortId.isVoid() || sourceAbstractSchemePortId.getMajor() == SCHEMECABLEPORT_CODE;
		super.setSourceAbstractSchemePortId(sourceAbstractSchemePortId);
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemeCablePort: NATURE_INVALID;
		this.setSourceAbstractSchemePort((SchemeCablePort) sourceAbstractSchemePort);
	}

	/**
	 * @param sourceSchemeCablePort
	 */
	public void setSourceAbstractSchemePort(final SchemeCablePort sourceSchemeCablePort) {
		super.setSourceAbstractSchemePort(sourceSchemeCablePort);
	}

	/**
	 * @param targetAbstractSchemePortId
	 * @see AbstractSchemeLink#setTargetAbstractSchemePortId(Identifier)
	 */
	@Override
	void setTargetAbstractSchemePortId(final Identifier targetAbstractSchemePortId) {
		assert targetAbstractSchemePortId.isVoid() || targetAbstractSchemePortId.getMajor() == SCHEMECABLEPORT_CODE;
		super.setTargetAbstractSchemePortId(targetAbstractSchemePortId);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemeCablePort: NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemeCablePort) targetAbstractSchemePort);
	}

	/**
	 * @param targetSchemeCablePort
	 */
	public void setTargetAbstractSchemePort(final SchemeCablePort targetSchemeCablePort) {
		super.setTargetAbstractSchemePort(targetSchemeCablePort);
	}

	/**
	 * @param schemeCableLink
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	public void fromIdlTransferable(final IdlSchemeCableLink schemeCableLink)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeCableLink,
					schemeCableLink.cableLinkTypeId,
					schemeCableLink.cableLinkId,
					schemeCableLink.sourceSchemeCablePortId,
					schemeCableLink.targetSchemeCablePortId);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param schemeCableLink
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeCableLink schemeCableLink,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemeCableLink, SCHEMECABLELINK_CODE, importType, PRE_IMPORT);
	
			super.fromXmlTransferable(schemeCableLink, importType);
	
			final boolean setCableLinkTypeId = schemeCableLink.isSetCableLinkTypeId();
			final boolean setCableLinkId = schemeCableLink.isSetCableLinkId();
			if (setCableLinkTypeId) {
				assert !setCableLinkId : OBJECT_STATE_ILLEGAL;
	
				super.abstractLinkTypeId = Identifier.fromXmlTransferable(schemeCableLink.getCableLinkTypeId(), importType, MODE_THROW_IF_ABSENT);
				super.abstractLinkId = VOID_IDENTIFIER;
			} else if (setCableLinkId) {
				assert !setCableLinkTypeId : OBJECT_STATE_ILLEGAL;
	
				super.abstractLinkTypeId = VOID_IDENTIFIER;
				super.abstractLinkId = Identifier.fromXmlTransferable(schemeCableLink.getCableLinkId(), importType, MODE_THROW_IF_ABSENT);
			} else {
				throw new XmlConversionException(
						"SchemeCableLink.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
	
			super.sourceAbstractSchemePortId = schemeCableLink.isSetSourceSchemeCablePortId()
					? Identifier.fromXmlTransferable(schemeCableLink.getSourceSchemeCablePortId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			super.targetAbstractSchemePortId = schemeCableLink.isSetTargetSchemeCablePortId()
					? Identifier.fromXmlTransferable(schemeCableLink.getTargetSchemeCablePortId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			super.parentSchemeId = Identifier.fromXmlTransferable(schemeCableLink.getParentSchemeId(), importType, MODE_THROW_IF_ABSENT);
			if (schemeCableLink.isSetSchemeCableThreads()) {
				for (final XmlSchemeCableThread schemeCableThread : schemeCableLink.getSchemeCableThreads().getSchemeCableThreadArray()) {
					SchemeCableThread.createInstance(super.creatorId, schemeCableThread, importType);
				}
			}
			if (schemeCableLink.isSetCableChannelingItems()) {
				for (final XmlCableChannelingItem cableChannelingItem : schemeCableLink.getCableChannelingItems().getCableChannelingItemArray()) {
					CableChannelingItem.createInstance(super.creatorId, cableChannelingItem, importType);
				}
			}
	
			XmlComplementorRegistry.complementStorableObject(schemeCableLink, SCHEMECABLELINK_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getReverseDependencies(boolean)
	 */
	@Override
	protected Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.addAll(super.getReverseDependencies(usePool));
		for (final StorableObject storableObject : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(getReverseDependencies(storableObject, usePool));
		}
		for (final StorableObject storableObject : this.getSchemeCableThreads0(usePool)) {
			reverseDependencies.addAll(getReverseDependencies(storableObject, usePool));
		}
		for (final StorableObject storableObject : this.getPathMembers0()) {
			reverseDependencies.addAll(getReverseDependencies(storableObject, usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @param cableChannelingItem
	 * @see SchemePath#assertContains(PathElement)
	 */
	boolean assertContains(final CableChannelingItem cableChannelingItem) {
		/*
		 * The second precondition is intentionally turned off since
		 * getPathMembers() cannot always return the correct number of
		 * path members when the code is executed server-side (path
		 * members preceding the one in question may be not saved yet).
		 *
		 * Making a path member depend on its precursor (if any) may be
		 * a solution, but it'll complicate the code too much.
		 */
		try {
			return cableChannelingItem.getParentSchemeCableLinkId().equals(this)
					&& (true || this.getPathMembers().headSet(cableChannelingItem).size() == cableChannelingItem.sequentialNumber);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return true;
		}
	}

	/**
	 * @param sequentialNumber
	 * @throws ApplicationException
	 * @see PathOwner#getPathMember(int)
	 * @bug this call doesn't utilize the local cache
	 */
	public CableChannelingItem getPathMember(final int sequentialNumber) throws ApplicationException {
		if (sequentialNumber < 0) {
			throw new IndexOutOfBoundsException("sequential numbers usually start with 0");
		}
		final StorableObjectCondition typicalCondition = new TypicalCondition(
				sequentialNumber,
				sequentialNumber,
				OPERATION_EQUALS,
				CABLECHANNELINGITEM_CODE,
				CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER) {
			private static final long serialVersionUID = -3614279715565428694L;

			@Override
			public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
				return identifiables.isEmpty();
			}
		};
		final StorableObjectCondition linkedIdsCondition = new LinkedIdsCondition(
				super.id,
				CABLECHANNELINGITEM_CODE);
		final StorableObjectCondition compoundCondition = new CompoundCondition(
				typicalCondition,
				CompoundConditionSort.AND,
				linkedIdsCondition);
		final Set<CableChannelingItem> pathMembers = StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		if (pathMembers.isEmpty()) {
			throw new NoSuchElementException("no path member found with sequential number: " + sequentialNumber);
		}
		assert pathMembers.size() == 1;
		return pathMembers.iterator().next();
	}

	/**
	 * @throws ApplicationException if the underlying invocation of 
	 *         {@link #getParentScheme() this.getParentScheme()},
	 *         throws an {@code ApplicationException}.
	 * @see AbstractSchemeElement#getNearestParentScheme()
	 */
	@Override
	public Scheme getNearestParentScheme() throws ApplicationException {
		return this.getParentScheme();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeCableLinkWrapper getWrapper() {
		return SchemeCableLinkWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: cableChannelingItems                        *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<CableChannelingItem> cableChannelingItemContainerWrappee;

	StorableObjectContainerWrappee<CableChannelingItem> getCableChannelingItemContainerWrappee() {
		return (this.cableChannelingItemContainerWrappee == null)
				? this.cableChannelingItemContainerWrappee = new StorableObjectContainerWrappee<CableChannelingItem>(this, CABLECHANNELINGITEM_CODE)
				: this.cableChannelingItemContainerWrappee;
	}

	/**
	 * Adds <code>CableChannelingItem</code> to the end of this
	 * <code>SchemeCableLink</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 * @throws ApplicationException
	 */
	public void addPathMember(final CableChannelingItem cableChannelingItem,
			final boolean processSubsequentSiblings)
	throws ApplicationException {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		cableChannelingItem.setParentPathOwner(this, processSubsequentSiblings);
	}

	/**
	 * Removes the <code>CableChannelingItem</code> from this
	 * <code>SchemeCableLink</code>, changing its
	 * <code>sequentialNumber</code> to <code>-1</code> and removing all
	 * its subsequent <code>CableChannelingItem</code>s.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 * @throws ApplicationException
	 * @see SchemePath#removePathMember(PathElement, boolean)
	 */
	public void removePathMember(final CableChannelingItem cableChannelingItem,
			final boolean processSubsequentSiblings)
	throws ApplicationException {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		assert cableChannelingItem.getParentSchemeCableLinkId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		cableChannelingItem.setParentPathOwner(null, processSubsequentSiblings);
	}

	public SortedSet<CableChannelingItem> getPathMembers()
	throws ApplicationException {
		return Collections.unmodifiableSortedSet(this.getPathMembers0());
	}

	/**
	 * @return child <code>CableChannelingItem</code>s in an unsorted manner.
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	SortedSet<CableChannelingItem> getPathMembers0() throws ApplicationException {
		final boolean usePool = false;
		return new TreeSet<CableChannelingItem>(
				this.getCableChannelingItemContainerWrappee().getContainees(usePool));
	}

	/*-********************************************************************
	 * Children manipulation: scheme cable threads                        *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeCableThread> schemeCableThreadContainerWrappee;

	StorableObjectContainerWrappee<SchemeCableThread> getSchemeCableThreadContainerWrappee() {
		return (this.schemeCableThreadContainerWrappee == null)
				? this.schemeCableThreadContainerWrappee = new StorableObjectContainerWrappee<SchemeCableThread>(this, SCHEMECABLETHREAD_CODE)
				: this.schemeCableThreadContainerWrappee;
	}

	/**
	 * @param schemeCableThread
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeCableThread(
			final SchemeCableThread schemeCableThread,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		schemeCableThread.setParentSchemeCableLink(this, usePool);
	}

	/**
	 * @param schemeCableThread
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeCableThread(
			final SchemeCableThread schemeCableThread,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		assert schemeCableThread.getParentSchemeCableLinkId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableThread.setParentSchemeCableLink(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeCableThread> getSchemeCableThreads(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeCableThreads0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemeCableThread> getSchemeCableThreads0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemeCableThreadContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeCableThreads
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeCableThreads(
			final Set<SchemeCableThread> schemeCableThreads,
			final boolean usePool)
	throws ApplicationException {
		assert schemeCableThreads != null: NON_NULL_EXPECTED;

		final Set<SchemeCableThread> oldSchemeCableThreads = this.getSchemeCableThreads0(usePool);

		final Set<SchemeCableThread> toRemove = new HashSet<SchemeCableThread>(oldSchemeCableThreads);
		toRemove.removeAll(schemeCableThreads);
		for (final SchemeCableThread schemeCableThread : toRemove) {
			this.removeSchemeCableThread(schemeCableThread, usePool);
		}

		final Set<SchemeCableThread> toAdd = new HashSet<SchemeCableThread>(schemeCableThreads);
		toAdd.removeAll(oldSchemeCableThreads);
		for (final SchemeCableThread schemeCableThread : toAdd) {
			this.addSchemeCableThread(schemeCableThread, usePool);
		}
	}

	/*-********************************************************************
	 * Non-model members                                                  *
	 **********************************************************************/	

	/**
	 * @param cableLinkType
	 * @param creatorId
	 * @param usePool
	 * @throws ApplicationException 
	 */
	@Shitlet
	public void setAbstractLinkTypeExt(final CableLinkType cableLinkType,
			final Identifier creatorId,
			final boolean usePool)
	throws ApplicationException {
		this.setAbstractLinkType(cableLinkType);
		final Set<CableThreadType> cableThreadTypes = cableLinkType.getCableThreadTypes(true);
		final Set<SchemeCableThread> newCableThreadTypes = new HashSet<SchemeCableThread>(cableThreadTypes.size());

		final CharacteristicType characteristicType;
		try {
			characteristicType = CharacteristicType.valueOf(COMMON_COLOUR);
		} catch (final ObjectNotFoundException onfe) {
			throw new Error("Cannot find CharacteristicType '"
					+ COMMON_COLOUR
					+ "'; system setup incomplete");
		}

		final String name = characteristicType.getName();
		final String description = characteristicType.getDescription();

		for (final CableThreadType cableThreadType : cableThreadTypes) {
			final SchemeCableThread schemeCableThread = SchemeCableThread.createInstance(
				creatorId,
				cableThreadType.getName(),
				cableThreadType.getLinkType(),
				this);
			Characteristic.createInstance(creatorId,
					characteristicType,
					name,
					description,
					/*
					 * Is it OK to treat color in
					 * such a way?
					 */
					Integer.toString(cableThreadType.getColor()),
					schemeCableThread,
					true,
					true);
			newCableThreadTypes.add(schemeCableThread);
		}
		this.setSchemeCableThreads(newCableThreadTypes, usePool);
	}
}
