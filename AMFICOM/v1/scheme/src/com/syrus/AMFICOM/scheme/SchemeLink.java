/*-
 * $Id: SchemeLink.java,v 1.117 2006/07/02 21:53:11 bass Exp $
 *
 * Copyright ? 2004-2006 Syrus Systems.
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
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
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
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLink;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeLinkHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #12 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.117 $, $Date: 2006/07/02 21:53:11 $
 * @module scheme
 */
public final class SchemeLink extends AbstractSchemeLink
		implements XmlTransferableObject<XmlSchemeLink>,
		IdlTransferableObjectExt<IdlSchemeLink> {
	private static final long serialVersionUID = 3834587703751947064L;

	private Identifier siteNodeId;

	Identifier parentSchemeElementId;

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
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private SchemeLink(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMELINK_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeLink(final IdlSchemeLink transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
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
	@ParameterizationPending(value = {"final boolean usePool"})
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
			final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

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
					INITIAL_VERSION,
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
			parentScheme.getSchemeLinkContainerWrappee().addToCache(schemeLink, usePool);

			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);

			schemeLink.markAsChanged();
			return schemeLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
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
	@ParameterizationPending(value = {"final boolean usePool"})
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
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					INITIAL_VERSION,
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
			parentSchemeElement.getSchemeLinkContainerWrappee().addToCache(schemeLink, usePool);

			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);

			schemeLink.markAsChanged();
			return schemeLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
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
	@ParameterizationPending(value = {"final boolean usePool"})
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
			final SchemeProtoElement parentSchemeProtoElement)
	throws CreateObjectException {
		final boolean usePool = false;

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
					INITIAL_VERSION,
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
			parentSchemeProtoElement.getSchemeLinkContainerWrappee().addToCache(schemeLink, usePool);

			schemeLink.abstractLinkTypeSet = (link != null || linkType != null);

			schemeLink.markAsChanged();
			return schemeLink;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeLink.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
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
			final XmlIdentifier xmlId = xmlSchemeLink.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeLink schemeLink;
			if (id.isVoid()) {
				schemeLink = new SchemeLink(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeLink = StorableObjectPool.getStorableObject(id, true);
				if (schemeLink == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeLink = new SchemeLink(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeLink.fromXmlTransferable(xmlSchemeLink, importType);
			assert schemeLink.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeLink.markAsChanged();
			return schemeLink;
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
	public SchemeLink clone() throws CloneNotSupportedException {
		final boolean usePool = false;

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

			clone.characteristicContainerWrappee = null;
			for (final Characteristic characteristic : this.getCharacteristics0(usePool)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				clone.addCharacteristic(characteristicClone, usePool);
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
		assert this.siteNodeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependenciesTmpl());
		dependencies.add(this.siteNodeId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		return dependencies;
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
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((super.parentSchemeId.isVoid() ? 0 : 1)
						+ (this.parentSchemeElementId.isVoid() ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		return super.getParentSchemeId();
	}

	Identifier getParentSchemeElementId() {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((super.parentSchemeId.isVoid() ? 0 : 1)
						+ (this.parentSchemeElementId.isVoid() ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		assert this.parentSchemeElementId.isVoid() || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeProtoElementId() {
		assert super.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((super.parentSchemeId.isVoid() ? 0 : 1)
						+ (this.parentSchemeElementId.isVoid() ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
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
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see AbstractSchemeLink#getSourceAbstractSchemePortId()
	 */
	@Override
	public Identifier getSourceAbstractSchemePortId() {
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
	public Identifier getTargetAbstractSchemePortId() {
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeLink getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlSchemeLinkHelper.init(orb,
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
				this.getSiteNodeId().getIdlTransferable(),
				this.getSourceAbstractSchemePortId().getIdlTransferable(),
				this.getTargetAbstractSchemePortId().getIdlTransferable(),
				this.getParentSchemeId().getIdlTransferable(),
				this.getParentSchemeElementId().getIdlTransferable(),
				this.getParentSchemeProtoElementId().getIdlTransferable());
	}

	/**
	 * @param schemeLink
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeLink schemeLink,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			super.getXmlTransferable(schemeLink, importType, usePool);
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
			XmlComplementorRegistry.complementStorableObject(schemeLink, SCHEMELINK_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
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
	 * @param usePool
	 * @throws ApplicationException
	 * @see AbstractSchemeElement#setParentScheme(Scheme, boolean)
	 * @todo Remove null warning suppression as soon as
	 *       Eclipse's bug #137467/129907 gets fixed.
	 */
	@Override
	@SuppressWarnings("null")
	public void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException {
		final boolean thisParentSchemeIdVoid = this.parentSchemeId.isVoid();
		final boolean thisParentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert this.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((thisParentSchemeIdVoid ? 0 : 1)
						+ (thisParentSchemeElementIdVoid ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeNull = (parentScheme == null);

		final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeIdVoid) {
			if (thisParentSchemeElementIdVoid) {
				/*
				 * Moving from a scheme protoelement to a scheme.
				 * At this point, newParentSchemeId is non-void.
				 */
				this.getParentSchemeProtoElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme element to a scheme.
				 * At this point, newParentSchemeId is non-void.
				 */
				this.getParentSchemeElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				this.parentSchemeElementId = VOID_IDENTIFIER;
			}
		} else {
			/*
			 * Moving from a scheme to another scheme.
			 * At this point, newParentSchemeId may be void.
			 */
			this.getParentScheme().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		}

		if (!parentSchemeNull) {
			parentScheme.getSchemeLinkContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeId = newParentSchemeId;
		this.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeElement(SchemeElement, boolean)}.
	 *
	 * @param parentSchemeElementId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeElementId(final Identifier parentSchemeElementId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;

		if (this.parentSchemeElementId.equals(parentSchemeElementId)) {
			return;
		}

		this.setParentSchemeElement(
				StorableObjectPool.<SchemeElement>getStorableObject(parentSchemeElementId, true),
				usePool);
	}

	/**
	 * @param parentSchemeElement
	 * @param usePool
	 * @throws ApplicationException
	 * @todo Remove null warning suppression as soon as
	 *       Eclipse's bug #137467/129907 gets fixed.
	 */
	@SuppressWarnings("null")
	public void setParentSchemeElement(final SchemeElement parentSchemeElement,
			final boolean usePool)
	throws ApplicationException {
		final boolean thisParentSchemeIdVoid = this.parentSchemeId.isVoid();
		final boolean thisParentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert this.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((thisParentSchemeIdVoid ? 0 : 1)
						+ (thisParentSchemeElementIdVoid ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeElementIsNull = (parentSchemeElement == null);

		final Identifier newParentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeIdVoid) {
			if (thisParentSchemeElementIdVoid) {
				/*
				 * Moving from a scheme protoelement to a scheme element.
				 * At this point, newParentSchemeElementId is non-void.
				 */
				this.getParentSchemeProtoElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				this.parentSchemeProtoElementId = VOID_IDENTIFIER;
			} else {
				/*
				 * Moving from a scheme element to another scheme element.
				 * At this point, newParentSchemeElementId may be void.
				 */
				this.getParentSchemeElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				if (parentSchemeElementIsNull) {
					Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
					StorableObjectPool.delete(this.getReverseDependencies(usePool));
				}
			}
		} else {
			/*
			 * Moving from a scheme to a scheme element. At this
			 * point, newParentSchemeElementId is non-void.
			 */
			super.getParentScheme().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

			super.parentSchemeId = VOID_IDENTIFIER;
		}

		if (!parentSchemeElementIsNull) {
			parentSchemeElement.getSchemeLinkContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeElementId = newParentSchemeElementId;
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
	 * @param parentSchemeProtoElement
	 * @param usePool
	 * @throws ApplicationException
	 * @todo Remove null warning suppression as soon as
	 *       Eclipse's bug #137467/129907 gets fixed.
	 */
	@SuppressWarnings("null")
	public void setParentSchemeProtoElement(
			final SchemeProtoElement parentSchemeProtoElement,
			final boolean usePool)
	throws ApplicationException {
		final boolean thisParentSchemeIdVoid = this.parentSchemeId.isVoid();
		final boolean thisParentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert this.parentSchemeId != null
				&& this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& ((thisParentSchemeIdVoid ? 0 : 1)
						+ (thisParentSchemeElementIdVoid ? 0 : 1)
						+ (this.parentSchemeProtoElementId.isVoid() ? 0 : 1) == 1) : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeProtoElementNull = (parentSchemeProtoElement == null);

		final Identifier newParentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeIdVoid) { 
			if (thisParentSchemeElementIdVoid) {
				/*
				 * Moving from a scheme protoelement to another scheme protoelement.
				 * At this point, newParentSchemeProtoElementId may be void.
				 */
				this.getParentSchemeProtoElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				if (parentSchemeProtoElementNull) {
					Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
					StorableObjectPool.delete(this.getReverseDependencies(usePool));
				}
			} else {
				/*
				 * Moving from a scheme element to a scheme protoelement.
				 * At this point, newParentSchemeProtoElementId is non-void.
				 */
				this.getParentSchemeElement().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

				this.parentSchemeElementId = VOID_IDENTIFIER;
			}
		} else {
			/*
			 * Moving from a scheme to a scheme protoelement.
			 * At this point, newParentSchemeProtoElementId is non-void.
			 */
			this.getParentScheme().getSchemeLinkContainerWrappee().removeFromCache(this, usePool);

			super.parentSchemeId = VOID_IDENTIFIER;
		}

		if (!parentSchemeProtoElementNull) {
			parentSchemeProtoElement.getSchemeLinkContainerWrappee().addToCache(this, usePool);
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
	 * @param schemeLink
	 * @throws IdlConversionException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	public void fromIdlTransferable(final IdlSchemeLink schemeLink)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeLink,
					schemeLink.linkTypeId,
					schemeLink.linkId,
					schemeLink.sourceSchemePortId,
					schemeLink.targetSchemePortId);
			this.siteNodeId = Identifier.valueOf(schemeLink.siteNodeId);
			this.parentSchemeElementId = Identifier.valueOf(schemeLink.parentSchemeElementId);
			this.parentSchemeProtoElementId = Identifier.valueOf(schemeLink.parentSchemeProtoElementId);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param schemeLink
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemeLink schemeLink,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(schemeLink, SCHEMELINK_CODE, importType, PRE_IMPORT);
	
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
				throw new XmlConversionException(
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
				throw new XmlConversionException(
						"SchemeLink.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
	
			XmlComplementorRegistry.complementStorableObject(schemeLink, SCHEMELINK_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @throws IllegalStateException if this {@code SchemeLink} doesn&apos;t
	 *         belong to any scheme.
	 * @throws ApplicationException if any of the underlying invocations of 
	 *         {@link #getParentSchemeProtoElement() this.getParentSchemeProtoElement()},
	 *         {@link #getParentScheme() this.getParentScheme()},
	 *         {@link #getParentSchemeElement() this.getParentSchemeElement()} or
	 *         {@link SchemeElement#getNearestParentScheme()} throws an
	 *         {@code ApplicationException}.
	 * @see AbstractSchemeElement#getNearestParentScheme()
	 */
	@Override
	public Scheme getNearestParentScheme() throws ApplicationException {
		if (this.getParentSchemeProtoElement() != null) {
			throw new IllegalStateException(this + " doesn't belong to any scheme");
		}

		final Scheme parentScheme = this.getParentScheme();
		return parentScheme == null
				? this.getParentSchemeElement().getNearestParentScheme()
				: parentScheme;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeLinkWrapper getWrapper() {
		return SchemeLinkWrapper.getInstance();
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * @see AbstractSchemeElement#getNearestParentSchemeElement()
	 */
	@Override
	public SchemeElement getNearestParentSchemeElement()
	throws ApplicationException {
		return this.getParentSchemeElementId().isVoid()
				? this.getParentSchemeId().isVoid()
						? null
						: this.getParentScheme().getParentSchemeElement()
				: this.getParentSchemeElement();
	}

	void setSiteNodeId(Identifier siteNodeId) {
//		 TODO: introduce additional sanity checks
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert siteNodeId.isVoid() || siteNodeId.getMajor() == SITENODE_CODE;
		this.siteNodeId = siteNodeId;
		super.markAsChanged();
	}
}
