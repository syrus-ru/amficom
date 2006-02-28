/*-
 * $Id: SiteNodeType.java,v 1.115 2006/02/28 15:20:01 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.logic.Library;
import com.syrus.AMFICOM.general.logic.LibraryEntry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypeHelper;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSort;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * Тип сетевого узла топологической схемы. Существует несколько
 * предустановленных  типов сетевых узлов, которые определяются полем
 * {@link #codename}, соответствующим какому-либо значению {@link #DEFAULT_WELL},
 * {@link #DEFAULT_PIQUET}, {@link #DEFAULT_ATS}, {@link #DEFAULT_BUILDING}, {@link #DEFAULT_UNBOUND},
 * {@link #DEFAULT_CABLE_INLET}, {@link #DEFAULT_TOWER}
 * 
 * Узлы специального типа CABLE_INLET должны быть привязаны к какому-либо
 * узлу BUILDING или ATS и самостоятельно не живут
 *  
 * @author $Author: arseniy $
 * @version $Revision: 1.115 $, $Date: 2006/02/28 15:20:01 $
 * @module map
 */
public final class SiteNodeType extends StorableObjectType<SiteNodeType>
		implements Characterizable, Namable,
		LibraryEntry, XmlTransferableObject<XmlSiteNodeType> {

	public static final String DEFAULT_WELL = "defaultwell";
	public static final String DEFAULT_PIQUET = "defaultpiquet";
	public static final String DEFAULT_ATS = "defaultats";
	public static final String DEFAULT_BUILDING = "defaultbuilding";
	public static final String DEFAULT_CABLE_INLET = "defaultcableinlet";
	public static final String DEFAULT_TOWER = "defaulttower";
	public static final String DEFAULT_UNBOUND = "unbound";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3690481316080464696L;

	private Identifier imageId;
	private String name;
	private boolean topological;

	private SiteNodeTypeSort sort;

	private Identifier mapLibraryId;

	public SiteNodeType(final IdlSiteNodeType sntt) throws CreateObjectException {
		try {
			this.fromTransferable(sntt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	SiteNodeType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological,
			final Identifier mapLibraryId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;
		this.sort = sort;
		this.mapLibraryId = mapLibraryId;
	}

	public static SiteNodeType createInstance(final Identifier creatorId,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological,
			final Identifier mapLibraryId) throws CreateObjectException {

		assert creatorId != null 
				&& codename != null 
				&& name != null 
				&& description != null 
				&& imageId != null 
				&& sort != null
				&& mapLibraryId != null : NON_NULL_EXPECTED;
		assert !mapLibraryId.isVoid() : NON_VOID_EXPECTED;

		try {
			final SiteNodeType siteNodeType = new SiteNodeType(IdentifierPool.getGeneratedIdentifier(SITENODE_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					sort,
					codename,
					name,
					description,
					imageId,
					topological,
					mapLibraryId);

			assert siteNodeType.isValid() : OBJECT_BADLY_INITIALIZED;

			siteNodeType.markAsChanged();

			return siteNodeType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlSiteNodeType sntt = (IdlSiteNodeType) transferable;
		super.fromTransferable(sntt, sntt.codename, sntt.description);

		this.name = sntt.name;
		this.imageId = new Identifier(sntt.imageId);
		this.topological = sntt.topological;
		this.sort = sntt.sort;
		this.mapLibraryId = new Identifier(sntt.mapLibraryId);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.imageId);
		dependencies.add(this.mapLibraryId);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return dependencies;
	}

	public Identifier getImageId() {
		return this.imageId;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSiteNodeType getIdlTransferable(final ORB orb) {
		return IdlSiteNodeTypeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.sort,
				this.codename,
				this.name,
				this.description,
				this.imageId.getIdlTransferable(),
				this.topological,
				this.mapLibraryId.getIdlTransferable());
	}

	public boolean isTopological() {
		return this.topological;
	}

	public void setImageId(final Identifier imageId) {
		this.imageId = imageId;
		super.markAsChanged();
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}
	
	protected void setName0(final String name) {
		this.name = name;
	}

	public void setTopological(final boolean topological) {
		this.setTopological0(topological);
		super.markAsChanged();
	}
	
	protected void setTopological0(final boolean topological) {
		this.topological = topological;
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final SiteNodeTypeSort sort,
			final String codename,
			final String name,
			final String description,
			final Identifier imageId,
			final boolean topological,
			final Identifier mapLibraryId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.imageId = imageId;
		this.topological = topological;
		this.sort = sort;
		this.mapLibraryId = mapLibraryId;
	}

	public SiteNodeTypeSort getSort() {
		return this.sort;
	}

	public void setSort(final SiteNodeTypeSort sort) {
		this.setSort0(sort);
		super.markAsChanged();
	}
	
	protected void setSort0(final SiteNodeTypeSort sort) {
		this.sort = sort;
	}

	/**
	 * @param siteNodeType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSiteNodeType siteNodeType,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			this.id.getXmlTransferable(siteNodeType.addNewId(), importType);
			siteNodeType.setName(this.name);
			if(this.description != null && this.description.length() != 0) {
				siteNodeType.setDescription(this.description);
			}
			// NOTE: '+ 1' is obligatory since enumerations in idl and xsd
			// have different indexing
			siteNodeType.setSort(XmlSiteNodeTypeSort.Enum.forInt(this.sort.value() + 1));
			siteNodeType.setTopological(this.isTopological());
			final AbstractBitmapImageResource abstractBitmapImageResource = StorableObjectPool.getStorableObject(this.getImageId(), true);
			String imageCodename = abstractBitmapImageResource.getCodename();
			String imageCodenameToWrite;
			byte[] image;
			if (abstractBitmapImageResource instanceof FileImageResource) {
				final FileImageResource fileImageResource = (FileImageResource) abstractBitmapImageResource;
				imageCodenameToWrite = new File(fileImageResource.getFileName()).getName();
				image = fileImageResource.getImage();
			} else if (abstractBitmapImageResource instanceof BitmapImageResource) {
				imageCodenameToWrite = imageCodename;
				image = ((BitmapImageResource) abstractBitmapImageResource).getImage();
			} else {
				throw new XmlConversionException("Invalid imsge resource type for \'" 
						+ abstractBitmapImageResource.getCodename()
						+ "'\'");
			}
			FileOutputStream out = new FileOutputStream(new File(imageCodenameToWrite));
			out.write(image);
			out.flush();
			out.close();
			siteNodeType.setImage(imageCodenameToWrite);
		} catch (final IOException ioe) {
			throw new XmlConversionException(ioe);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws IdentifierGenerationException
	 */
	private SiteNodeType(final XmlIdentifier id,
			String importType,
			final Date created,
			final Identifier creatorId,
			@Deprecated final String codename,
			@Deprecated final String description)
	throws IdentifierGenerationException {
		super(id, importType, SITENODE_TYPE_CODE, created, creatorId);
		/**
		 * @todo to be moved to #fromXmlTransferable() or XmlComplementor
		 */
		this.codename = codename;
		this.description = description;
	}

	public void fromXmlTransferable(final XmlSiteNodeType xmlSiteNodeType,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(xmlSiteNodeType, SITENODE_TYPE_CODE, importType, PRE_IMPORT);
	
			this.name = xmlSiteNodeType.getName();
			if(xmlSiteNodeType.isSetDescription()) {
				this.description = xmlSiteNodeType.getDescription();
			}
			else {
				this.description = "";
			}
			// NOTE: '- 1' is obligatory since enumerations in idl and xsd
			// have different indexing
			this.sort = SiteNodeTypeSort.from_int(xmlSiteNodeType.getSort().intValue() - 1);
			this.topological = xmlSiteNodeType.getTopological();
	
			final String imageCodeName = xmlSiteNodeType.getImage();
			Identifier loadedImageId = getImageId(this.modifierId, imageCodeName);
	
			if (loadedImageId == null) {
				throw new CreateObjectException("ImageResource \'" + imageCodeName + "\' not found");
			}
			
			this.imageId = loadedImageId;
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlSiteNodeType
	 * @throws CreateObjectException
	 */
	public static SiteNodeType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlSiteNodeType xmlSiteNodeType) 
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlSiteNodeType.getCodename();
			final Set<SiteNodeType> siteNodeTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, SITENODE_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert siteNodeTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlSiteNodeType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			SiteNodeType siteNodeType;
			if (siteNodeTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					siteNodeType = new SiteNodeType(xmlId,
							importType,
							created,
							creatorId,
							xmlSiteNodeType.getCodename(),
							xmlSiteNodeType.getDescription());
				} else {
					siteNodeType = StorableObjectPool.getStorableObject(expectedId, true);
					if (siteNodeType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						siteNodeType = new SiteNodeType(xmlId,
								importType,
								created,
								creatorId,
								xmlSiteNodeType.getCodename(),
								xmlSiteNodeType.getDescription());
					} else {
						final String oldCodename = siteNodeType.getCodename();
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
				siteNodeType = siteNodeTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					siteNodeType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = siteNodeType.getId();
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
						siteNodeType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			siteNodeType.fromXmlTransferable(xmlSiteNodeType, importType);
			assert siteNodeType.isValid() : OBJECT_BADLY_INITIALIZED;
			siteNodeType.markAsChanged();
			return siteNodeType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	public Library getParent() {
		return getMapLibrary();
	}

	public void setParent(Library library) {
		this.setParent0(library);
		super.markAsChanged();
	}
	
	protected void setParent0(Library library) {
		assert library instanceof MapLibrary : "must be instance of MapLibrary";
		this.setMapLibrary0((MapLibrary)library);
	}

	public MapLibrary getMapLibrary() {
		try {
			return StorableObjectPool.getStorableObject(this.getMapLibraryId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}
	
	public Identifier getMapLibraryId() {
		return this.mapLibraryId;
	}

	public void setMapLibrary(MapLibrary mapLibrary) {
		this.setMapLibrary0(mapLibrary);
		this.markAsChanged();
	}
	
	protected void setMapLibrary0(MapLibrary mapLibrary) {
		this.mapLibraryId = mapLibrary.getId();
	}
	
	/**
	 * Получить пиктограмму по кодовому имени. Если пиктограмма не существует, 
	 * на создается.
	 * 
	 * @param userId пользователь
	 * @param codename кодовое имя
	 * @return Идентификатор пиктограммы ({@link com.syrus.AMFICOM.resource.AbstractImageResource})
	 * @throws ApplicationException 
	 */
	static Identifier getImageId(
			final Identifier userId, 
			final String codename)
			throws ApplicationException {
		StorableObjectCondition condition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				IMAGERESOURCE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<AbstractBitmapImageResource> bitMaps = 
			StorableObjectPool.getStorableObjectsByCondition(condition, true);

		if(!bitMaps.isEmpty()) {
			AbstractBitmapImageResource imageResource = bitMaps.iterator().next();
			if(imageResource instanceof BitmapImageResource
					&& ((BitmapImageResource)imageResource).getImage().length == 0) {
				try {
					((BitmapImageResource)imageResource).setImage(readData(codename));
					StorableObjectPool.flush(imageResource, userId, true);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			return imageResource.getId();
		}

		// interprete codename as a file name
		// and create new bitmap image resource
		try {
			byte[] data = readData(codename);

			BitmapImageResource bitmapImageResource = BitmapImageResource.createInstance(
					userId,
					codename,
					data);
			StorableObjectPool.flush(bitmapImageResource, userId, true);
			return bitmapImageResource.getId();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] readData(String filename) throws IOException {
		File file = new File(filename);
		File realFile = new File(file.getAbsolutePath());
		byte[] data = new byte[(int) realFile.length()];
		FileInputStream in = new FileInputStream(realFile);
		in.read(data);
		in.close();
		return data;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SiteNodeTypeWrapper getWrapper() {
		return SiteNodeTypeWrapper.getInstance();
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
