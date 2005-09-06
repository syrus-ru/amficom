/*-
 * $Id: SchemeCableLink.java,v 1.74 2005/09/06 17:30:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLinkHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * #13 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.74 $, $Date: 2005/09/06 17:30:26 $
 * @module scheme
 */
public final class SchemeCableLink extends AbstractSchemeLink
		implements PathOwner<CableChannelingItem>,
		XmlBeansTransferable<XmlSchemeCableLink> {
	private static final long serialVersionUID = 3760847878314274867L;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeCableLink(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		try {
			DatabaseContext.getDatabase(SCHEMECABLELINK_CODE).retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeCableLink(final IdlSchemeCableLink transferable) throws CreateObjectException {
		fromTransferable(transferable);
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
	public static SchemeCableLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final CableLinkType cableLinkType,
			final CableLink cableLink,
			final SchemeCablePort sourceSchemeCablePort,
			final SchemeCablePort targetSchemeCablePort,
			final Scheme parentScheme) throws CreateObjectException {
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
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					cableLinkType,
					cableLink,
					sourceSchemeCablePort,
					targetSchemeCablePort,
					parentScheme);
			schemeCableLink.markAsChanged();
			if (cableLink != null || cableLinkType != null)
				schemeCableLink.abstractLinkTypeSet = true;
			return schemeCableLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeCableLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * Adds <code>CableChannelingItem</code> to the end of this
	 * <code>SchemeCableLink</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 */
	public void addPathMember(final CableChannelingItem cableChannelingItem, final boolean processSubsequentSiblings) {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		cableChannelingItem.setParentPathOwner(this, processSubsequentSiblings);
	}

	public void addSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		schemeCableThread.setParentSchemeCableLink(this);
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeCableLink clone() throws CloneNotSupportedException {
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

			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			for (final SchemeCableThread schemeCableThread : this.getSchemeCableThreads0()) {
				final SchemeCableThread schemeCableThreadClone = schemeCableThread.clone();
				clone.clonedIdMap.putAll(schemeCableThreadClone.getClonedIdMap());
				clone.addSchemeCableThread(schemeCableThreadClone);
			}
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	public SortedSet<CableChannelingItem> getPathMembers() {
		try {
			return Collections.unmodifiableSortedSet(new TreeSet<CableChannelingItem>(this.getPathMembers0()));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.unmodifiableSortedSet(new TreeSet<CableChannelingItem>(Collections.<CableChannelingItem>emptySet()));
		}
	}

	/**
	 * @return child <code>CableChannelingItem</code>s in an unsorted manner.
	 */
	Set<CableChannelingItem> getPathMembers0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, CABLECHANNELINGITEM_CODE), true);
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
	Identifier getAbstractLinkTypeId() {
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
	Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert !parentSchemeId1.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		return parentSchemeId1;
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeCableThread> getSchemeCableThreads() {
		try {
			return Collections.unmodifiableSet(this.getSchemeCableThreads0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeCableThread> getSchemeCableThreads0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMECABLETHREAD_CODE), true);
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	Identifier getSourceAbstractSchemePortId() {
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
	Identifier getTargetAbstractSchemePortId() {
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeCableLink getTransferable(final ORB orb) {
		return IdlSchemeCableLinkHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				super.getPhysicalLength(),
				super.getOpticalLength(),
				this.getAbstractLinkTypeId().getTransferable(),
				this.getAbstractLinkId().getTransferable(),
				this.getSourceAbstractSchemePortId().getTransferable(),
				this.getTargetAbstractSchemePortId().getTransferable(),
				this.getParentSchemeId().getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	public XmlSchemeCableLink getXmlTransferable(final String importType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Removes the <code>CableChannelingItem</code> from this
	 * <code>SchemeCableLink</code>, changing its
	 * <code>sequentialNumber</code> to <code>-1</code> and removing all
	 * its subsequent <code>CableChannelingItem</code>s.
	 *
	 * @param cableChannelingItem
	 * @param processSubsequentSiblings
	 * @see SchemePath#removePathMember(PathElement, boolean)
	 */
	public void removePathMember(final CableChannelingItem cableChannelingItem, final boolean processSubsequentSiblings) {
		assert cableChannelingItem != null: NON_NULL_EXPECTED;
		assert cableChannelingItem.getParentSchemeCableLinkId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		cableChannelingItem.setParentPathOwner(null, processSubsequentSiblings);
	}

	public void removeSchemeCableThread(final SchemeCableThread schemeCableThread) {
		assert schemeCableThread != null: NON_NULL_EXPECTED;
		assert schemeCableThread.getParentSchemeCableLinkId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCableThread.setParentSchemeCableLink(null);
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
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	@Override
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		assert !super.parentSchemeId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		super.setParentScheme(parentScheme);
	}

	public void setSchemeCableThreads(final Set<SchemeCableThread> schemeCableThreads) throws ApplicationException {
		assert schemeCableThreads != null: NON_NULL_EXPECTED;
		final Set<SchemeCableThread> oldSchemeCableThreads = this.getSchemeCableThreads0();
		/*
		 * Check is made to prevent SchemeCableThreads from
		 * permanently losing their parents.
		 */
		oldSchemeCableThreads.removeAll(schemeCableThreads);
		for (final SchemeCableThread oldSchemeCableThread : oldSchemeCableThreads) {
			this.removeSchemeCableThread(oldSchemeCableThread);
		}
		for (final SchemeCableThread schemeCableThread : schemeCableThreads) {
			this.addSchemeCableThread(schemeCableThread);
		}
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
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable)
	throws CreateObjectException {
		synchronized (this) {
			final IdlSchemeCableLink schemeCableLink = (IdlSchemeCableLink) transferable;
			super.fromTransferable(schemeCableLink,
					schemeCableLink.cableLinkTypeId,
					schemeCableLink.cableLinkId,
					schemeCableLink.sourceSchemeCablePortId,
					schemeCableLink.targetSchemeCablePortId);
		}
	}

	/**
	 * @param xmlSchemeCableLink
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeCableLink xmlSchemeCableLink,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
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
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeCableThreads0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getPathMembers0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
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
		return cableChannelingItem.getParentSchemeCableLinkId().equals(super.id)
				&& (true || this.getPathMembers().headSet(cableChannelingItem).size() == cableChannelingItem.sequentialNumber);
	}

	/**
	 * @param sequentialNumber
	 * @throws ApplicationException
	 * @see PathOwner#getPathMember(int)
	 */
	public CableChannelingItem getPathMember(final int sequentialNumber) throws ApplicationException {
		if (sequentialNumber < 0) {
			throw new IndexOutOfBoundsException("sequential numbers usually start with 0");
		}
		final StorableObjectCondition typicalCondition = new TypicalCondition(
				sequentialNumber,
				sequentialNumber,
				OperationSort.OPERATION_EQUALS,
				CABLECHANNELINGITEM_CODE,
				CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER) {
			@Override
			public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
				return false;
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
	 * @param cableLinkType
	 * @throws ApplicationException 
	 */
	@Shitlet
	public void setAbstractLinkTypeExt(final CableLinkType cableLinkType,
			final Identifier creatorId)
	throws ApplicationException {
		this.setAbstractLinkType(cableLinkType);
		final Set<CableThreadType> cableThreadTypes = cableLinkType.getCableThreadTypes(true);
		final Set<SchemeCableThread> newCableThreadTypes = new HashSet<SchemeCableThread>(cableThreadTypes.size());

		final StorableObjectCondition condition = new TypicalCondition(CharacteristicTypeCodenames.COMMON_COLOUR, OperationSort.OPERATION_EQUALS, CHARACTERISTIC_TYPE_CODE, COLUMN_CODENAME);
		final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		final CharacteristicType characteristicType = characteristicTypes.isEmpty()
				? CharacteristicType.createInstance(creatorId, CharacteristicTypeCodenames.COMMON_COLOUR, "", "color", DataType.INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL)
				: characteristicTypes.iterator().next();

		assert characteristicType != null : NON_NULL_EXPECTED;

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
		this.setSchemeCableThreads(newCableThreadTypes);
	}
}
