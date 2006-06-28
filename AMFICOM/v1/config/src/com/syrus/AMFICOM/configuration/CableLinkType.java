/*-
 * $Id: CableLinkType.java,v 1.98 2006/06/08 15:54:58 arseniy Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
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

import com.syrus.AMFICOM.configuration.corba.IdlCableLinkType;
import com.syrus.AMFICOM.configuration.corba.IdlCableLinkTypeHelper;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
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
 * @version $Revision: 1.98 $, $Date: 2006/06/08 15:54:58 $
 * @author $Author: arseniy $
 * @module config
 */
public final class CableLinkType extends AbstractLinkType
		implements XmlTransferableObject<XmlCableLinkType>, IdlTransferableObjectExt<IdlCableLinkType> {
	private static final long serialVersionUID = 3257007652839372857L;

	private String name;
	private int sort;
	private String manufacturer;
	private String manufacturerCode;
	private Identifier imageId;

	public CableLinkType(final IdlCableLinkType cltt) throws CreateObjectException {
		try {
			this.fromIdlTransferable(cltt);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	CableLinkType(final Identifier id,
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
	private CableLinkType(final XmlIdentifier id, final String importType, final Date created, final Identifier creatorId)
			throws IdentifierGenerationException {
		super(id, importType, CABLELINK_TYPE_CODE, created, creatorId);
	}

	/**
	 * @param creatorId
	 * @param xmlCableLinkType
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CableLinkType createInstance(final Identifier creatorId,
			final XmlCableLinkType xmlCableLinkType,
			final String importType) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlCableLinkType.getCodename();
			final Set<CableLinkType> cableLinkTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, CABLELINK_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert cableLinkTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlCableLinkType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			CableLinkType cableLinkType;
			if (cableLinkTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableLinkType = new CableLinkType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					cableLinkType = StorableObjectPool.getStorableObject(expectedId, true);
					if (cableLinkType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						cableLinkType = new CableLinkType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = cableLinkType.getCodename();
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
				cableLinkType = cableLinkTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableLinkType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = cableLinkType.getId();
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
						cableLinkType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			cableLinkType.fromXmlTransferable(xmlCableLinkType, importType);
			assert cableLinkType.isValid() : OBJECT_BADLY_INITIALIZED;
			cableLinkType.markAsChanged();
			return cableLinkType;
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
	public static CableLinkType createInstance(final Identifier creatorId,
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
				|| manufacturerCode == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final CableLinkType cableLinkType = new CableLinkType(IdentifierPool.getGeneratedIdentifier(CABLELINK_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description,
					name,
					sort.value(),
					manufacturer,
					manufacturerCode,
					imageId);

			assert cableLinkType.isValid() : OBJECT_BADLY_INITIALIZED;

			cableLinkType.markAsChanged();

			return cableLinkType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	public synchronized void fromIdlTransferable(final IdlCableLinkType cltt) throws IdlConversionException {
		super.fromIdlTransferable(cltt, cltt.codename, cltt.description);
		this.sort = cltt.sort.value();
		this.manufacturer = cltt.manufacturer;
		this.manufacturerCode = cltt.manufacturerCode;
		this.imageId = Identifier.valueOf(cltt.imageId);
		this.name = cltt.name;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param cableLinkType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableLinkType cableLinkType, final String importType) throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(cableLinkType, CABLELINK_TYPE_CODE, importType, PRE_IMPORT);

			this.name = cableLinkType.getName();
			this.codename = cableLinkType.getCodename();
			this.description = cableLinkType.isSetDescription()
					? cableLinkType.getDescription()
					: "";
			this.sort = cableLinkType.getSort().intValue();
			this.manufacturer = cableLinkType.isSetManufacturer()
					? cableLinkType.getManufacturer()
					: "";
			this.manufacturerCode = cableLinkType.isSetManufacturerCode()
					? cableLinkType.getManufacturerCode()
					: "";
			// TODO read imageId - see SiteNodeType.getImageId(Identifier userId, String codename) for example
			this.imageId = VOID_IDENTIFIER;
			if (cableLinkType.isSetCableThreadTypes()) {
				for (final XmlCableThreadType cableThreadType : cableLinkType.getCableThreadTypes().getCableThreadTypeList()) {
					CableThreadType.createInstance(this.creatorId, cableThreadType, importType);
				}
			}
			if (cableLinkType.isSetCharacteristics()) {
				for (final XmlCharacteristic characteristic : cableLinkType.getCharacteristics().getCharacteristicList()) {
					Characteristic.createInstance(super.creatorId, characteristic, importType);
				}
			}
			
			XmlComplementorRegistry.complementStorableObject(cableLinkType, CABLELINK_TYPE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableLinkType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlCableLinkTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				LinkTypeSort.from_int(this.sort),
				this.manufacturer,
				this.manufacturerCode,
				this.imageId.getIdlTransferable());
	}

	/**
	 * @param cableLinkType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	@Shitlet
	public void getXmlTransferable(final XmlCableLinkType cableLinkType, final String importType, final boolean usePool)
			throws XmlConversionException {
		try {
			super.id.getXmlTransferable(cableLinkType.addNewId(), importType);
			cableLinkType.setName(this.name);
			cableLinkType.setCodename(this.codename);
			if (cableLinkType.isSetDescription()) {
				cableLinkType.unsetDescription();
			}
			if (this.description.length() != 0) {
				cableLinkType.setDescription(this.description);
			}
			cableLinkType.setSort(XmlLinkTypeSort.Enum.forInt(this.sort));
			if (cableLinkType.isSetManufacturer()) {
				cableLinkType.unsetManufacturer();
			}
			if (this.manufacturer.length() != 0) {
				cableLinkType.setManufacturer(this.manufacturer);
			}
			if (cableLinkType.isSetManufacturerCode()) {
				cableLinkType.unsetManufacturerCode();
			}
			if (this.manufacturerCode.length() != 0) {
				cableLinkType.setManufacturerCode(this.manufacturerCode);
			}
			// TODO write image to file
	
			if (cableLinkType.isSetCableThreadTypes()) {
				cableLinkType.unsetCableThreadTypes();
			}
			final Set<CableThreadType> cableThreadTypes = this.getCableThreadTypes(true);
			if (!cableThreadTypes.isEmpty()) {
				final XmlCableThreadTypeSeq cableThreadTypeSeq = cableLinkType.addNewCableThreadTypes();
				for (final CableThreadType cableThreadType : cableThreadTypes) {
					cableThreadType.getXmlTransferable(cableThreadTypeSeq.addNewCableThreadType(), importType, usePool);
				}
			}
	
			if (cableLinkType.isSetCharacteristics()) {
				cableLinkType.unsetCharacteristics();
			}
			final Set<Characteristic> characteristics = this.getCharacteristics(false);
			if (!characteristics.isEmpty()) {
				final XmlCharacteristicSeq characteristicSeq = cableLinkType.addNewCharacteristics();
				for (final Characteristic characteristic : characteristics) {
					characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
				}
			}

			XmlComplementorRegistry.complementStorableObject(cableLinkType, CABLELINK_TYPE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.name != null && this.manufacturer != null && this.manufacturerCode != null;
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

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
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
	
	public void setSort(final LinkTypeSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public Set<CableThreadType> getCableThreadTypes(final boolean breakOnLoadError) {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, CABLETHREAD_TYPE_CODE);
		Set<CableThreadType> cableThreadTypes;
		try {
			cableThreadTypes = StorableObjectPool.getStorableObjectsByCondition(lic, true, breakOnLoadError);
			return cableThreadTypes;
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			return Collections.emptySet();
		}
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableLinkTypeWrapper getWrapper() {
		return CableLinkTypeWrapper.getInstance();
	}
}
