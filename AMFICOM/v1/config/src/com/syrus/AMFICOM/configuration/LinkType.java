/*-
 * $Id: LinkType.java,v 1.104 2006/06/08 15:54:58 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.104 $, $Date: 2006/06/08 15:54:58 $
 * @author $Author: arseniy $
 * @module config
 */

public final class LinkType extends AbstractLinkType
		implements XmlTransferableObject<XmlLinkType>, IdlTransferableObjectExt<IdlLinkType> {
	private static final long serialVersionUID = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	public LinkType(final IdlLinkType ltt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(ltt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	LinkType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
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
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
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
	private LinkType(final XmlIdentifier id, final String importType, final Date created, final Identifier creatorId)
			throws IdentifierGenerationException {
		super(id, importType, LINK_TYPE_CODE, created, creatorId);
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlLinkType
	 * @throws CreateObjectException
	 */
	public static LinkType createInstance(final Identifier creatorId, final String importType, final XmlLinkType xmlLinkType)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlLinkType.getCodename();
			final Set<LinkType> linkTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, LINK_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert linkTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlLinkType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			LinkType linkType;
			if (linkTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					linkType = new LinkType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					linkType = StorableObjectPool.getStorableObject(expectedId, true);
					if (linkType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						linkType = new LinkType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = linkType.getCodename();
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
				linkType = linkTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					linkType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = linkType.getId();
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
						linkType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			linkType.fromXmlTransferable(xmlLinkType, importType);
			assert linkType.isValid() : OBJECT_BADLY_INITIALIZED;
			linkType.markAsChanged();
			return linkType;
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
	 * @throws CreateObjectException
	 */
	public static LinkType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final LinkTypeSort sort,
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) throws CreateObjectException {
		if (creatorId == null
				|| codename == null
				|| description == null
				|| name == null
				|| sort == null
				|| manufacturer == null
				|| manufacturerCode == null
				|| imageId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final LinkType linkType = new LinkType(IdentifierPool.getGeneratedIdentifier(LINK_TYPE_CODE),
						creatorId,
						INITIAL_VERSION,
						codename,
						description,
						name,
						sort.value(),
						manufacturer,
						manufacturerCode,
						imageId);

			assert linkType.isValid() : OBJECT_BADLY_INITIALIZED;

			linkType.markAsChanged();

			return linkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public synchronized void fromIdlTransferable(final IdlLinkType ltt) throws IdlConversionException {
		super.fromIdlTransferable(ltt, ltt.codename, ltt.description);

		this.sort = ltt.sort.value();
		this.manufacturer = ltt.manufacturer;
		this.manufacturerCode = ltt.manufacturerCode;
		this.imageId = Identifier.valueOf(ltt.imageId);
		this.name = ltt.name;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param linkType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlLinkType linkType, final String importType) throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(linkType, LINK_TYPE_CODE, importType, PRE_IMPORT);

			this.name = linkType.getName();
			this.codename = linkType.getCodename();
			this.description = linkType.isSetDescription()
					? linkType.getDescription()
					: "";
			this.sort = linkType.getSort().intValue() - 1;
			this.manufacturer = linkType.isSetManufacturer()
					? linkType.getManufacturer()
					: "";
			this.manufacturerCode = linkType.isSetManufacturerCode() 
					? linkType.getManufacturerCode()
					: "";
			// TODO read imageId - see SiteNodeType.getImageId(Identifier userId, String codename) for example
			this.imageId = VOID_IDENTIFIER;
			if (linkType.isSetCharacteristics()) {
				for (final XmlCharacteristic characteristic : linkType.getCharacteristics().getCharacteristicList()) {
					Characteristic.createInstance(super.creatorId, characteristic, importType);
				}
			}

			XmlComplementorRegistry.complementStorableObject(linkType, LINK_TYPE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlLinkType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlLinkTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort), this.manufacturer, this.manufacturerCode,
				this.imageId.getIdlTransferable());
	}

	/**
	 * @param linkType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	@Shitlet
	public void getXmlTransferable(final XmlLinkType linkType, final String importType, final boolean usePool)
			throws XmlConversionException {
		try {
			this.id.getXmlTransferable(linkType.addNewId(), importType);
			linkType.setName(this.name);
			linkType.setCodename(this.codename);
			if (linkType.isSetDescription()) {
				linkType.unsetDescription();
			}
			if (this.description.length() != 0) {
				linkType.setDescription(this.description);
			}
			linkType.setSort(XmlLinkTypeSort.Enum.forInt(this.getSort().value() + 1));
			if (linkType.isSetManufacturer()) {
				linkType.unsetManufacturer();
			}
			if (this.manufacturer.length() != 0) {
				linkType.setManufacturer(this.manufacturer);
			}
			if (linkType.isSetManufacturerCode()) {
				linkType.unsetManufacturerCode();
			}
			if (this.manufacturerCode.length() != 0) {
				linkType.setManufacturerCode(this.manufacturerCode);
			}
			// TODO write image to file
			
			if (linkType.isSetCharacteristics()) {
				linkType.unsetCharacteristics();
			}
			final Set<Characteristic> characteristics = this.getCharacteristics(false);
			if (!characteristics.isEmpty()) {
				final XmlCharacteristicSeq characteristicSeq = linkType.addNewCharacteristics();
				for (final Characteristic characteristic : characteristics) {
					characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
				}
			}

			XmlComplementorRegistry.complementStorableObject(linkType, LINK_TYPE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null
				&& this.manufacturer != null
				&& this.manufacturerCode != null
				&& this.imageId != null;
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
			final String manufacturer,
			final String manufacturerCode,
			final Identifier imageId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.sort = sort;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
		this.imageId = imageId;
	}

	@Override
	public Identifier getImageId() {
		return this.imageId;
	}

	@Override
	public String getManufacturer() {
		return this.manufacturer;
	}

	@Override
	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	@Override
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	@Override
	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}

	@Override
	public LinkTypeSort getSort() {
		return LinkTypeSort.from_int(this.sort);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name= name;
		super.markAsChanged();
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

	/**
	 * @param imageId The imageId to set.
	 */
	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected LinkTypeWrapper getWrapper() {
		return LinkTypeWrapper.getInstance();
	}
}
