/*-
 * $Id: CableThreadType.java,v 1.85.2.1 2006/02/28 15:19:58 arseniy Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.WARNING;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableThreadType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * <code>CableThreadType</code>, among other fields, contains references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract</i> optical fiber), the latter is a type of
 * cable (or an <i>abstract</i> cable containing this thread).
 *
 * @version $Revision: 1.85.2.1 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @module configuration
 */
public final class CableThreadType extends StorableObjectType<CableThreadType>
		implements Namable, XmlTransferableObject<XmlCableThreadType> {
	private static final long  serialVersionUID	= 3689355429075628086L;

	/**
	 * In hex representation, this is {@code 0xffffffff}, which corresponds to
	 * {@link java.awt.Color#WHITE}.
	 *
	 * @see java.awt.Color#WHITE
	 * @see java.awt.Color#Color(int)
	 * @see java.awt.Color#getRGB()
	 */
	private static final int COLOR_WHITE = -1;

	/**
	 * Can&apos;t be {@code null}; instead, for an empty {@code name}, empty string
	 * should be supplied. Maximum length for {@code name} is 128 characters.
	 *
	 * @serial include
	 */
	private String name;

	/**
	 * @serial include
	 */
	private int color;

	/**
	 * Can be neither {@code null} nor {@link Identifier#VOID_IDENTIFIER void}.
	 *
	 * @serial include
	 */
	private Identifier linkTypeId;

	/**
	 * Can be neither {@code null} nor {@link Identifier#VOID_IDENTIFIER void}.
	 *
	 * @serial include
	 */
	private Identifier cableLinkTypeId;

	public CableThreadType(final IdlCableThreadType cttt) throws CreateObjectException {
		try {
			this.fromTransferable(cttt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableThreadType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final Identifier linkTypeId,
			final Identifier cableLinkTypeId) {
		super(id,
			new Date(),
			new Date(),
			creatorId,
			creatorId,
			version,
			codename,
			description);

		this.name = name;
		this.color = color;
		this.linkTypeId = linkTypeId;
		this.cableLinkTypeId = cableLinkTypeId;
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
	private CableThreadType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, CABLETHREAD_TYPE_CODE, created, creatorId);
	}

	/**
	 * @param creatorId
	 * @param xmlCableThreadType
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(
			final Identifier creatorId,
			final XmlCableThreadType xmlCableThreadType,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlCableThreadType.getCodename();
			final Set<CableThreadType> cableThreadTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, CABLETHREAD_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert cableThreadTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlCableThreadType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			CableThreadType cableThreadType;
			if (cableThreadTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableThreadType = new CableThreadType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					cableThreadType = StorableObjectPool.getStorableObject(expectedId, true);
					if (cableThreadType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						cableThreadType = new CableThreadType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = cableThreadType.getCodename();
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
				cableThreadType = cableThreadTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableThreadType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = cableThreadType.getId();
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
						cableThreadType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			cableThreadType.fromXmlTransferable(xmlCableThreadType, importType);
			assert cableThreadType.isValid() : OBJECT_BADLY_INITIALIZED;
			cableThreadType.markAsChanged();
			return cableThreadType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectType#isValid()
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.isNameValid()
				&& this.isLinkTypeIdValid()
				&& this.isCableLinkTypeIdValid();
	}

	/**
	 * A shorthand for {@link #createInstance(Identifier, String, String, String, int, LinkType, CableLinkType)}.
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param linkType
	 * @param cableLinkType
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final LinkType linkType,
			final CableLinkType cableLinkType)
	throws CreateObjectException {
		return createInstance(creatorId, codename, description, name, COLOR_WHITE, linkType, cableLinkType);
	}

	/**
	 * Creates a new instance for client.
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param name
	 * @param color
	 * @param linkType
	 * @param cableLinkType
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final int color,
			final LinkType linkType,
			final CableLinkType cableLinkType)
	throws CreateObjectException {
		assert creatorId != null
				&& codename != null
				&& description != null;

		final Identifier linkTypeId = Identifier.possiblyVoid(linkType);
		final Identifier cableLinkTypeId = Identifier.possiblyVoid(cableLinkType);

		checkNameValid(name);
		checkLinkTypeIdValid(linkTypeId);
		checkCableLinkTypeIdValid(cableLinkTypeId);

		try {
			final CableThreadType cableThreadType = new CableThreadType(IdentifierPool.getGeneratedIdentifier(CABLETHREAD_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description,
					name,
					color,
					linkTypeId,
					cableLinkTypeId);

			assert cableThreadType.isValid() : OBJECT_BADLY_INITIALIZED;

			cableThreadType.markAsChanged();

			return cableThreadType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(
			final IdlStorableObject transferable)
	throws ApplicationException {
		final IdlCableThreadType idlCableThreadType = (IdlCableThreadType) transferable;

		super.fromTransferable(idlCableThreadType, idlCableThreadType.codename, idlCableThreadType.description);

		this.name = idlCableThreadType.name;
		this.color = idlCableThreadType.color;
		this.linkTypeId = Identifier.valueOf(idlCableThreadType.linkTypeId);
		this.cableLinkTypeId = Identifier.valueOf(idlCableThreadType.cableLinkTypeId);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param cableThreadType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(final XmlCableThreadType cableThreadType,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(cableThreadType, CABLETHREAD_TYPE_CODE, importType, PRE_IMPORT);

			this.codename = cableThreadType.getCodename();
			this.description = cableThreadType.isSetDescription()
					? cableThreadType.getDescription()
					: "";

			this.name = cableThreadType.isSetName()
					? cableThreadType.getName()
					: "";
			this.color = cableThreadType.getColor();
			this.linkTypeId = Identifier.fromXmlTransferable(
					cableThreadType.getLinkTypeId(),
					importType,
					MODE_THROW_IF_ABSENT);
			this.cableLinkTypeId = Identifier.fromXmlTransferable(
					cableThreadType.getCableLinkTypeId(),
					importType,
					MODE_THROW_IF_ABSENT);
			
			XmlComplementorRegistry.complementStorableObject(cableThreadType, CABLETHREAD_TYPE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableThreadType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlCableThreadTypeHelper.init(orb,
				this.id.getIdlTransferable(orb),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(orb),
				this.modifierId.getIdlTransferable(orb),
				this.version.longValue(),
				this.codename,
				this.description != null ? this.description : "",
				this.name != null ? this.name : "",
				this.color,
				this.linkTypeId.getIdlTransferable(orb),
				this.cableLinkTypeId.getIdlTransferable(orb));
	}

	/**
	 * @param cableThreadType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCableThreadType cableThreadType,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		try {
			this.id.getXmlTransferable(cableThreadType.addNewId(), importType);
			cableThreadType.setCodename(this.codename);

			if (cableThreadType.isSetDescription()) {
				cableThreadType.unsetDescription();
			}
			if (this.description.length() != 0) {
				cableThreadType.setDescription(this.description);
			}

			if (cableThreadType.isSetName()) {
				cableThreadType.unsetName();
			}
			if (this.name.length() != 0) {
				cableThreadType.setName(this.name);
			}

			cableThreadType.setColor(this.color);
			this.linkTypeId.getXmlTransferable(cableThreadType.addNewLinkTypeId(), importType);
			this.cableLinkTypeId.getXmlTransferable(cableThreadType.addNewCableLinkTypeId(), importType);

			XmlComplementorRegistry.complementStorableObject(cableThreadType, CABLETHREAD_TYPE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final Identifier linkTypeId,
			final Identifier cableLinkTypeId) {
		synchronized (this) {
			super.setAttributes(created,
					modified,
					creatorId,
					modifierId,
					version,
					codename,
					description);
			this.name = name;
			this.color = color;
			this.linkTypeId = linkTypeId;
			this.cableLinkTypeId = cableLinkTypeId;
		}

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	public void setColor(final int color) {
		this.color = color;
		super.markAsChanged();
	}

	public int getColor() {
		return this.color;
	}	

	/*-****************************************************************************************
	 * name property.                                                                         *
	 ******************************************************************************************/

	public String getName() {
		assert this.isNameValid();
		return this.name;
	}

	private void setName0(final String name) {
		checkNameValid(name);
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		this.markAsChanged();
	}

	/*-****************************************************************************************
	 * linkTypeId property.                                                                   *
	 ******************************************************************************************/

	public Identifier getLinkTypeId() {
		assert this.isLinkTypeIdValid();
		return this.linkTypeId;
	}
	
	private void setLinkTypeId0(final Identifier linkTypeId) {
		checkLinkTypeIdValid(linkTypeId);
		this.linkTypeId = linkTypeId;
	}

	public void setLinkTypeId(final Identifier linkTypeId) {
		this.setLinkTypeId0(linkTypeId);
		this.markAsChanged();
	}

	/*-****************************************************************************************
	 * linkType virtual property.                                                             *
	 ******************************************************************************************/

	/**
	 * A wrapper around {@link #getLinkTypeId()}.
	 */
	public LinkType getLinkType() {
		try {
			return StorableObjectPool.<LinkType>getStorableObject(this.getLinkTypeId(), true);
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #setLinkTypeId(Identifier)}.
	 *
	 * @param linkType
	 */
	public void setLinkType(final LinkType linkType) {
		assert linkType != null;
		this.setLinkTypeId(linkType.getId());
	}

	/*-****************************************************************************************
	 * cableLinkTypeId property.                                                              *
	 ******************************************************************************************/

	public Identifier getCableLinkTypeId() {
		assert this.isCableLinkTypeIdValid();
		return this.cableLinkTypeId;
	}
	
	private void setCableLinkTypeId0(final Identifier cableLinkTypeId) {
		checkCableLinkTypeIdValid(cableLinkTypeId);
		this.cableLinkTypeId = cableLinkTypeId;
	}

	public void setCableLinkTypeId(final Identifier cableLinkTypeId) {
		this.setCableLinkTypeId0(cableLinkTypeId);
		this.markAsChanged();
	}

	/*-****************************************************************************************
	 * cableLinkType virtual property.                                                        *
	 ******************************************************************************************/

	/**
	 * A wrapper around {@link #getCableLinkTypeId()}.
	 */
	public CableLinkType getCableLinkType() {
		try {
			return StorableObjectPool.<CableLinkType>getStorableObject(this.getCableLinkTypeId(), true);
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			return null;
		}
	}

	/**
	 * A wrapper around {@link #setCableLinkTypeId(Identifier)}.
	 *
	 * @param cableLinkType
	 */
	public void setCableLinkType(final CableLinkType cableLinkType) {
		assert cableLinkType != null;
		this.setCableLinkTypeId(cableLinkType.getId());
	}


	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.linkTypeId);
		dependencies.add(this.cableLinkTypeId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableThreadTypeWrapper getWrapper() {
		return CableThreadTypeWrapper.getInstance();
	}

	/*-****************************************************************************************
	 * Contract.                                                                              *
	 * For javadocs, see the section of the same name in Characteristic class.                *
	 ******************************************************************************************/

	private static void checkNameValid(final String name) {
		if (name == null) {
			throw new NullPointerException("name is null");
		}
		final int length = name.length();
		if (length > 128) {
			throw new IllegalArgumentException("expected name length: 0..128; actual: " + length);
		}
	}

	private boolean isNameValid() {
		try {
			checkNameValid(this.name);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}

	private static void checkLinkTypeIdValid(final Identifier linkTypeId) {
		if (linkTypeId == null) {
			throw new NullPointerException("linkTypeId is null");
		}
		if (linkTypeId.isVoid()) {
			throw new IllegalArgumentException("linkTypeId is void");
		}
		final short entityCode = linkTypeId.getMajor();
		if (entityCode != LINK_TYPE_CODE) {
			throw new IllegalArgumentException("expected entity code: "
					+ ObjectEntities.codeToString(LINK_TYPE_CODE) + "; actual: "
					+ ObjectEntities.codeToString(entityCode)); 
		}
	}

	private boolean isLinkTypeIdValid() {
		try {
			checkLinkTypeIdValid(this.linkTypeId);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}

	private static void checkCableLinkTypeIdValid(final Identifier cableLinkTypeId) {
		if (cableLinkTypeId == null) {
			throw new NullPointerException("cableLinkTypeId is null");
		}
		if (cableLinkTypeId.isVoid()) {
			throw new IllegalArgumentException("cableLinkTypeId is void");
		}
		final short entityCode = cableLinkTypeId.getMajor();
		if (entityCode != CABLELINK_TYPE_CODE) {
			throw new IllegalArgumentException("expected entity code: "
					+ ObjectEntities.codeToString(CABLELINK_TYPE_CODE) + "; actual: "
					+ ObjectEntities.codeToString(entityCode)); 
		}
	}

	private boolean isCableLinkTypeIdValid() {
		try {
			checkCableLinkTypeIdValid(this.cableLinkTypeId);
		} catch (final RuntimeException re) {
			assert false : re.getMessage();
		}
		return true;
	}
}
