/*-
 * $Id: SchemeLink.java,v 1.76 2005/09/16 15:58:22 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NATURE_INVALID;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.XML_BEAN_NOT_COMPLETE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
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

import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.AbstractLinkType;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLinkHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.util.Log;

/**
 * #12 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.76 $, $Date: 2005/09/16 15:58:22 $
 * @module scheme
 */
public final class SchemeLink extends AbstractSchemeLink
		implements XmlBeansTransferable<XmlSchemeLink> {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier siteNodeId;

	Identifier parentSchemeElementId;

	Identifier parentSchemeProtoElementId;


	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop. 
	 */
	private boolean parentSet = false;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentScheme
	 * @param parentSchemeElement
	 * @param parentSchemeProtoElement
	 */
	SchemeLink(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final LinkType linkType,
			final Link link,
			final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement,
			final SchemeProtoElement parentSchemeProtoElement) {
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
				linkType,
				link,
				sourceSchemePort,
				targetSchemePort,
				parentScheme);
		this.siteNodeId = Identifier.possiblyVoid(siteNode);

		assert ((parentScheme == null) ? 0 : 1) + ((parentSchemeElement == null) ? 0 : 1) + ((parentSchemeProtoElement == null) ? 0 : 1) <= 1 : EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private SchemeLink(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeLink(final IdlSchemeLink transferable) throws CreateObjectException {
		this.fromTransferable((IdlStorableObject) transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort)}.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name) throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentScheme);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentSchemeElement);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, double, double, LinkType, Link, SiteNode, SchemePort, SchemePort, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, null, null,
				null, null, null, parentSchemeProtoElement);
	}

        /**
         * This method breaks some assertions, so clients should consider using
         * other ones to create a new instance.
         *
         * @param creatorId
         * @param name
         * @param description
         * @param physicalLength
         * @param opticalLength
         * @param linkType
         * @param link
         * @param siteNode
         * @param sourceSchemePort
         * @param targetSchemePort
         * @throws CreateObjectException
         */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final LinkType linkType,
			final Link link,
			final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(IdentifierPool.getGeneratedIdentifier(SCHEMELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					linkType,
					link,
					siteNode,
					sourceSchemePort,
					targetSchemePort,
					null,
					null,
					null);
			schemeLink.markAsChanged();
			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final LinkType linkType,
			final Link link,
			final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final Scheme parentScheme) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(IdentifierPool.getGeneratedIdentifier(SCHEMELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					linkType,
					link,
					siteNode,
					sourceSchemePort,
					targetSchemePort,
					parentScheme,
					null,
					null);
			schemeLink.markAsChanged();
			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);
			schemeLink.parentSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final LinkType linkType,
			final Link link,
			final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeElement parentSchemeElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(IdentifierPool.getGeneratedIdentifier(SCHEMELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					linkType,
					link,
					siteNode,
					sourceSchemePort,
					targetSchemePort,
					null,
					parentSchemeElement,
					null);
			schemeLink.markAsChanged();
			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);
			schemeLink.parentSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param physicalLength
	 * @param opticalLength
	 * @param linkType
	 * @param link
	 * @param siteNode
	 * @param sourceSchemePort
	 * @param targetSchemePort
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final LinkType linkType,
			final Link link,
			final SiteNode siteNode,
			final SchemePort sourceSchemePort,
			final SchemePort targetSchemePort,
			final SchemeProtoElement parentSchemeProtoElement) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeLink schemeLink = new SchemeLink(IdentifierPool.getGeneratedIdentifier(SCHEMELINK_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					physicalLength,
					opticalLength,
					linkType,
					link,
					siteNode,
					sourceSchemePort,
					targetSchemePort,
					null,
					null,
					parentSchemeProtoElement);
			schemeLink.markAsChanged();
			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);
			schemeLink.parentSet = true;
			return schemeLink;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeLink
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeLink createInstance(final Identifier creatorId,
			final XmlSchemeLink xmlSchemeLink,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlSchemeLink.getId(), importType, SCHEMELINK_CODE);
			SchemeLink schemeLink = StorableObjectPool.getStorableObject(id, true);
			if (schemeLink == null) {
				schemeLink = new SchemeLink(id, new Date(), creatorId);
			}
			schemeLink.fromXmlTransferable(xmlSchemeLink, importType);
			assert schemeLink.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeLink.markAsChanged();
			return schemeLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	public SchemeLink clone() throws CloneNotSupportedException {
		final StackTraceElement stackTrace[] = (new Throwable()).getStackTrace();
		final int depth = 1;
		if (stackTrace.length > depth) {
			final StackTraceElement stackTraceElement = stackTrace[depth];
			final String className = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			if ((!className.equals(SchemeElement.class.getName())
					&& !className.equals(SchemeProtoElement.class.getName())
					&& !className.equals(Scheme.class.getName()))
					|| (!methodName.equals("clone")
					&& !methodName.equals("fillProperties"))) {
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
			final SchemeLink clone = (SchemeLink) super.clone();

			if (clone.clonedIdMap == null) {
				clone.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			clone.clonedIdMap.put(this.id, clone.id);

			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			return clone;
		} catch (final ApplicationException ae) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ae);
			throw cnse;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.siteNodeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.siteNodeId);
		dependencies.add(this.parentSchemeElementId);
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
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkId()
	 */
	@Override
	Identifier getAbstractLinkId() {
		final Identifier linkId = super.getAbstractLinkId();
		assert linkId.isVoid() || linkId.getMajor() == LINK_CODE;
		return linkId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLink()
	 */
	@Override
	public Link getAbstractLink() {
		final AbstractLink abstractLink = super.getAbstractLink();
		assert abstractLink == null || abstractLink instanceof Link : OBJECT_BADLY_INITIALIZED;
		return (Link) abstractLink;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkTypeId()
	 */
	@Override
	Identifier getAbstractLinkTypeId() {
		final Identifier linkTypeId = super.getAbstractLinkTypeId();
		assert linkTypeId.isVoid() || linkTypeId.getMajor() == LINK_TYPE_CODE;
		return linkTypeId;
	}

	/**
	 * @see AbstractSchemeLink#getAbstractLinkType()
	 */
	@Override
	public LinkType getAbstractLinkType() {
		final AbstractLinkType abstractLinkType = super.getAbstractLinkType();
		assert abstractLinkType instanceof LinkType : OBJECT_BADLY_INITIALIZED;
		return (LinkType) abstractLinkType;
	}

	/**
	 * @see AbstractSchemeElement#getParentSchemeId()
	 */
	@Override
	Identifier getParentSchemeId() {
		assert true || this.assertParentSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		if (parentSchemeId1.isVoid()) {
			Log.debugMessage("SchemeLink.getParentSchemeId() | Parent Scheme was requested, while parent is either a SchemeElement or a SchemeProtoElement; returning null",
					FINE);
		}
		return parentSchemeId1;
	}

	Identifier getParentSchemeElementId() {
		assert true || this.assertParentSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("SchemeLink.getParentSchemeElementId() | Parent SchemeElement was requested, while parent is either a Scheme or a SchemeProtoElement; returning null",
					FINE);
		}
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeProtoElementId() {
		assert true || this.assertParentSetStrict() : OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (this.parentSchemeProtoElementId.isVoid()) {
			Log.debugMessage("SchemeLink.getParentSchemeProtoElementId() | Parent SchemeProtoElement was requested, while parent is either a Scheme or a SchemeElement; returning null",
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

	Identifier getSiteNodeId() {
		assert this.siteNodeId != null: OBJECT_NOT_INITIALIZED;
		assert this.siteNodeId.isVoid() || this.siteNodeId.getMajor() == SITENODE_CODE;
		return this.siteNodeId;
	}

	/**
	 * A wrapper around {@link #getSiteNodeId()}.
	 */
	public SiteNode getSiteNode() {
		try {
			return StorableObjectPool.getStorableObject(this.getSiteNodeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	Identifier getSourceAbstractSchemePortId() {
		final Identifier sourceSchemePortId = super.getSourceAbstractSchemePortId();
		assert sourceSchemePortId.isVoid() || sourceSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return sourceSchemePortId;
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePort()
	 */
	@Override
	public SchemePort getSourceAbstractSchemePort() {
		final AbstractSchemePort sourceAbstractSchemePort = super.getSourceAbstractSchemePort();
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: OBJECT_BADLY_INITIALIZED;
		return (SchemePort) sourceAbstractSchemePort;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePortId()
	 */
	@Override
	Identifier getTargetAbstractSchemePortId() {
		final Identifier targetSchemePortId = super.getTargetAbstractSchemePortId();
		assert targetSchemePortId.isVoid() || targetSchemePortId.getMajor() == SCHEMEPORT_CODE;
		return targetSchemePortId;
	}

	/**
	 * @see AbstractSchemeLink#getTargetAbstractSchemePort()
	 */
	@Override
	public SchemePort getTargetAbstractSchemePort() {
		final AbstractSchemePort targetAbstractSchemePort = super.getTargetAbstractSchemePort();
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: OBJECT_BADLY_INITIALIZED;
		return (SchemePort) targetAbstractSchemePort;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeLink getTransferable(final ORB orb) {
		return IdlSchemeLinkHelper.init(orb,
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
				this.getSiteNodeId().getTransferable(),
				this.getSourceAbstractSchemePortId().getTransferable(),
				this.getTargetAbstractSchemePortId().getTransferable(),
				this.getParentSchemeId().getTransferable(),
				this.getParentSchemeElementId().getTransferable(),
				this.getParentSchemeProtoElementId().getTransferable());
	}

	/**
	 * @param schemeLink
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public XmlSchemeLink getXmlTransferable(
			final XmlSchemeLink schemeLink,
			final String importType)
	throws ApplicationException {
		super.getXmlTransferable(schemeLink, importType);
		if (schemeLink.isSetLinkTypeId()) {
			schemeLink.unsetLinkTypeId();
		}
		if (!super.abstractLinkTypeId.isVoid()) {
			super.abstractLinkTypeId.getXmlTransferable(schemeLink.addNewLinkTypeId(), importType);
		}
		if (schemeLink.isSetLinkId()) {
			schemeLink.unsetLinkId();
		}
		if (!super.abstractLinkId.isVoid()) {
			super.abstractLinkId.getXmlTransferable(schemeLink.addNewLinkId(), importType);
		}
		if (schemeLink.isSetSourceSchemePortId()) {
			schemeLink.unsetSourceSchemePortId();
		}
		if (!super.sourceAbstractSchemePortId.isVoid()) {
			super.sourceAbstractSchemePortId.getXmlTransferable(schemeLink.addNewSourceSchemePortId(), importType);
		}
		if (schemeLink.isSetTargetSchemePortId()) {
			schemeLink.unsetTargetSchemePortId();
		}
		if (!super.targetAbstractSchemePortId.isVoid()) {
			super.targetAbstractSchemePortId.getXmlTransferable(schemeLink.addNewTargetSchemePortId(), importType);
		}
		if (schemeLink.isSetSiteNodeId()) {
			schemeLink.unsetSiteNodeId();
		}
		if (!this.siteNodeId.isVoid()) {
			this.siteNodeId.getXmlTransferable(schemeLink.addNewSiteNodeId(), importType);
		}
		if (schemeLink.isSetParentSchemeId()) {
			schemeLink.unsetParentSchemeId();
		}
		if (!super.parentSchemeId.isVoid()) {
			super.parentSchemeId.getXmlTransferable(schemeLink.addNewParentSchemeId(), importType);
		}
		if (schemeLink.isSetParentSchemeElementId()) {
			schemeLink.unsetParentSchemeElementId();
		}
		if (!this.parentSchemeElementId.isVoid()) {
			this.parentSchemeElementId.getXmlTransferable(schemeLink.addNewParentSchemeElementId(), importType);
		}
		if (schemeLink.isSetParentSchemeProtoElementId()) {
			schemeLink.unsetParentSchemeProtoElementId();
		}
		if (!this.parentSchemeProtoElementId.isVoid()) {
			this.parentSchemeProtoElementId.getXmlTransferable(schemeLink.addNewParentSchemeProtoElementId(), importType);
		}
		return schemeLink;
	}

	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final double physicalLength,
			final double opticalLength,
			final Identifier linkTypeId,
			final Identifier linkId,
			final Identifier siteNodeId,
			final Identifier sourceSchemePortId,
			final Identifier targetSchemePortId,
			final Identifier parentSchemeId,
			final Identifier parentSchemeElementId,
			final Identifier parentSchemeProtoElementId) {
		synchronized (this) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version,
					name,
					description,
					physicalLength,
					opticalLength,
					linkTypeId,
					linkId,
					sourceSchemePortId,
					targetSchemePortId,
					parentSchemeId);
	
			assert siteNodeId != null : NON_NULL_EXPECTED;
	
			assert parentSchemeElementId != null : NON_NULL_EXPECTED;
			assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
			assert (parentSchemeId.isVoid() ? 0 : 1)
					+ (parentSchemeElementId.isVoid() ? 0 : 1)
					+ (parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1;
	
			this.siteNodeId = siteNodeId;
			this.parentSchemeElementId = parentSchemeElementId;
			this.parentSchemeProtoElementId = parentSchemeProtoElementId;

			this.parentSet = true;
		}
	}

	/**
	 * @param abstractLink
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	@Override
	public void setAbstractLink(final AbstractLink abstractLink) {
		assert abstractLink == null || abstractLink instanceof Link : NATURE_INVALID;
		this.setAbstractLink((Link) abstractLink);
	}

	/**
	 * @param link
	 * @see AbstractSchemeLink#setAbstractLink(AbstractLink)
	 */
	public void setAbstractLink(final Link link) {
		super.setAbstractLink(link);
	}

	/**
	 * @param abstractLinkType
	 * @see AbstractSchemeLink#setAbstractLinkType(AbstractLinkType)
	 */
	@Override
	public void setAbstractLinkType(final AbstractLinkType abstractLinkType) {
		assert abstractLinkType instanceof LinkType : NATURE_INVALID;
		this.setAbstractLinkType((LinkType) abstractLinkType);
	}

	/**
	 * @param linkType
	 */
	public void setAbstractLinkType(final LinkType linkType) {
		super.setAbstractLinkType(linkType);
	}

	/**
	 * @param parentScheme
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	@Override
	public void setParentScheme(final Scheme parentScheme) {
		assert this.assertParentSetNonStrict() : OBJECT_BADLY_INITIALIZED;

		if (super.parentSchemeId.isVoid()) {
			if (this.parentSchemeElementId.isVoid()) {
				/*
				 * Moving from a scheme protoelement to a scheme.
				 */
				if (parentScheme == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme element to a scheme.
				 */
				if (parentScheme == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				this.parentSchemeElementId = VOID_IDENTIFIER;
			}
			super.parentSchemeId = parentScheme.getId();
			super.markAsChanged();
		} else {
			/*
			 * Moving from a scheme to another scheme.
			 */
			super.setParentScheme(parentScheme);
		}
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert this.assertParentSetNonStrict() : OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeElementId;
		if (super.parentSchemeId.isVoid()) {
			if (this.parentSchemeElementId.isVoid()) {
				/*
				 * Moving from a scheme protoelement to a scheme element.
				 */
				if (parentSchemeElement == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				newParentSchemeElementId = parentSchemeElement.getId();
				this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme element to another scheme element.
				 */
				if (parentSchemeElement == null) {
					Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
					StorableObjectPool.delete(this.id);
					return;
				}
				newParentSchemeElementId = parentSchemeElement.getId();
				if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
					return;
				}
			}
		} else {
			/*
			 * Moving from a scheme to a scheme element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			super.parentSchemeId = VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert this.assertParentSetNonStrict() : OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeProtoElementId;
		if (super.parentSchemeId.isVoid()) { 
			if (this.parentSchemeElementId.isVoid()) {
				/*
				 * Moving from a scheme protoelement to another scheme protoelement.
				 */
				if (parentSchemeProtoElement == null) {
					Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
					StorableObjectPool.delete(this.id);
					return;
				}
				newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
				if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId)) {
					return;
				}
			} else {
				/*
				 * Moving from a scheme element to a scheme protoelement.
				 */
				if (parentSchemeProtoElement == null) {
					Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
					return;
				}
				newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
				this.parentSchemeElementId = VOID_IDENTIFIER;
			}
		} else {
			/*
			 * Moving from a scheme to a scheme protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			super.parentSchemeId = VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	public void setSiteNode(final SiteNode siteNode) {
		final Identifier newSiteNodeId = Identifier.possiblyVoid(siteNode);
		if (this.siteNodeId.equals(newSiteNodeId)) {
			return;
		}
		this.siteNodeId = newSiteNodeId;
		super.markAsChanged();
	}

	/**
	 * @param sourceAbstractSchemePortId
	 * @see AbstractSchemeLink#setSourceAbstractSchemePortId(Identifier)
	 */
	@Override
	void setSourceAbstractSchemePortId(final Identifier sourceAbstractSchemePortId) {
		assert sourceAbstractSchemePortId.isVoid() || sourceAbstractSchemePortId.getMajor() == SCHEMEPORT_CODE;
		super.setSourceAbstractSchemePortId(sourceAbstractSchemePortId);
	}

	/**
	 * @param sourceAbstractSchemePort
	 * @see AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setSourceAbstractSchemePort(final AbstractSchemePort sourceAbstractSchemePort) {
		assert sourceAbstractSchemePort == null || sourceAbstractSchemePort instanceof SchemePort: NATURE_INVALID;
		this.setSourceAbstractSchemePort((SchemePort) sourceAbstractSchemePort);
	}

	/**
	 * @param sourceSchemePort
	 */
	public void setSourceAbstractSchemePort(final SchemePort sourceSchemePort) {
		super.setSourceAbstractSchemePort(sourceSchemePort);
	}

	/**
	 * @param targetAbstractSchemePortId
	 * @see AbstractSchemeLink#setTargetAbstractSchemePortId(Identifier)
	 */
	@Override
	void setTargetAbstractSchemePortId(final Identifier targetAbstractSchemePortId) {
		assert targetAbstractSchemePortId.isVoid() || targetAbstractSchemePortId.getMajor() == SCHEMEPORT_CODE;
		super.setTargetAbstractSchemePortId(targetAbstractSchemePortId);
	}

	/**
	 * @param targetAbstractSchemePort
	 * @see AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)
	 */
	@Override
	public void setTargetAbstractSchemePort(final AbstractSchemePort targetAbstractSchemePort) {
		assert targetAbstractSchemePort == null || targetAbstractSchemePort instanceof SchemePort: NATURE_INVALID;
		this.setTargetAbstractSchemePort((SchemePort) targetAbstractSchemePort);
	}

	/**
	 * @param targetSchemePort
	 */
	public void setTargetAbstractSchemePort(final SchemePort targetSchemePort) {
		super.setTargetAbstractSchemePort(targetSchemePort);
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
			final IdlSchemeLink schemeLink = (IdlSchemeLink) transferable;
			super.fromTransferable(schemeLink,
					schemeLink.linkTypeId,
					schemeLink.linkId,
					schemeLink.sourceSchemePortId,
					schemeLink.targetSchemePortId);
			this.siteNodeId = new Identifier(schemeLink.siteNodeId);
			this.parentSchemeElementId = new Identifier(schemeLink.parentSchemeElementId);
			this.parentSchemeProtoElementId = new Identifier(schemeLink.parentSchemeProtoElementId);
	
			this.parentSet = true;
		}
	}

	/**
	 * @param schemeLink
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemeLink schemeLink,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeLink, SCHEMELINK_CODE, importType);

		super.fromXmlTransferable(schemeLink, importType);

		final boolean setLinkTypeId = schemeLink.isSetLinkTypeId();
		final boolean setLinkId = schemeLink.isSetLinkId();
		if (setLinkTypeId) {
			assert !setLinkId : OBJECT_STATE_ILLEGAL;

			super.abstractLinkTypeId = Identifier.fromXmlTransferable(schemeLink.getLinkTypeId(), importType, MODE_THROW_IF_ABSENT);
			super.abstractLinkId = VOID_IDENTIFIER;
		} else if (setLinkId) {
			assert !setLinkTypeId : OBJECT_STATE_ILLEGAL;

			super.abstractLinkTypeId = VOID_IDENTIFIER;
			super.abstractLinkId = Identifier.fromXmlTransferable(schemeLink.getLinkId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeLink.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		super.sourceAbstractSchemePortId = schemeLink.isSetSourceSchemePortId()
				? Identifier.fromXmlTransferable(schemeLink.getSourceSchemePortId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		super.targetAbstractSchemePortId = schemeLink.isSetTargetSchemePortId()
				? Identifier.fromXmlTransferable(schemeLink.getTargetSchemePortId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.siteNodeId = schemeLink.isSetSiteNodeId()
				? Identifier.fromXmlTransferable(schemeLink.getSiteNodeId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;

		final boolean setParentSchemeId = schemeLink.isSetParentSchemeId();
		final boolean setParentSchemeElementId = schemeLink.isSetParentSchemeElementId();
		final boolean setParentSchemeProtoElementId = schemeLink.isSetParentSchemeProtoElementId();
		if (setParentSchemeId) {
			assert !setParentSchemeElementId : OBJECT_STATE_ILLEGAL;
			assert !setParentSchemeProtoElementId : OBJECT_STATE_ILLEGAL;

			super.parentSchemeId = Identifier.fromXmlTransferable(schemeLink.getParentSchemeId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeElementId = VOID_IDENTIFIER;
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeElementId) {
			assert !setParentSchemeId : OBJECT_STATE_ILLEGAL;
			assert !setParentSchemeProtoElementId : OBJECT_STATE_ILLEGAL;

			super.parentSchemeId = VOID_IDENTIFIER;
			this.parentSchemeElementId = Identifier.fromXmlTransferable(schemeLink.getParentSchemeElementId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeProtoElementId) {
			assert !setParentSchemeId : OBJECT_STATE_ILLEGAL;
			assert !setParentSchemeElementId : OBJECT_STATE_ILLEGAL;

			super.parentSchemeId = VOID_IDENTIFIER;
			this.parentSchemeElementId = VOID_IDENTIFIER;
			this.parentSchemeProtoElementId = Identifier.fromXmlTransferable(schemeLink.getParentSchemeProtoElementId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeLink.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		this.parentSet = true;
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
		return super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& super.parentSchemeId.isVoid()
				&& this.parentSchemeElementId.isVoid()
				&& this.parentSchemeProtoElementId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertParentSetStrict() {
		return super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((super.parentSchemeId.isVoid() ? 0 : 1)
						+ (this.parentSchemeElementId.isVoid() ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1);
	}

	void setSiteNodeId(Identifier siteNodeId) {
//		 TODO: inroduce additional sanity checks
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert siteNodeId.isVoid() || siteNodeId.getMajor() == SITENODE_CODE;
		this.siteNodeId = siteNodeId;
		super.markAsChanged();
	}

	void setParentSchemeElementId(Identifier parentSchemeElementId) {
//		 TODO: inroduce additional sanity checks
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		this.parentSchemeElementId = parentSchemeElementId;
		super.markAsChanged();
	}

	void setParentSchemeProtoElementId(Identifier parentSchemeProtoElementId) {
//		 TODO: inroduce additional sanity checks
		assert parentSchemeProtoElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId.isVoid() || parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
		super.markAsChanged();
	}
}
