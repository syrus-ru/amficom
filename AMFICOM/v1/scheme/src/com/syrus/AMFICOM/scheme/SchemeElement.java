/*-
 * $Id: SchemeElement.java,v 1.148 2005/11/28 09:09:50 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind.SCHEME_CONTAINER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind._SCHEME_CONTAINER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind._SCHEME_ELEMENT_CONTAINER;
import static com.syrus.AMFICOM.scheme.xml.XmlSchemeElement.Kind.INT_SCHEME_CONTAINER;
import static com.syrus.AMFICOM.scheme.xml.XmlSchemeElement.Kind.INT_SCHEME_ELEMENT_CONTAINER;
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

import com.syrus.AMFICOM.bugs.Crutch136;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementHelper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDeviceSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElementSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLinkSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement.Kind.Enum;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.148 $, $Date: 2005/11/28 09:09:50 $
 * @module scheme
 */
public final class SchemeElement extends AbstractSchemeElement<SchemeElement>
		implements SchemeCellContainer,
		XmlBeansTransferable<XmlSchemeElement> {
	private static final long serialVersionUID = 3618977875802797368L;

	private int kind;
	
	private Identifier equipmentId;

	private Identifier protoEquipmentId;

	private Identifier kisId;

	private String label;

	Identifier parentSchemeElementId;

	/**
	 * Takes non-null value at pack time.
	 */
	private Identifier schemeCellId;

	private Identifier siteNodeId;

	private Identifier symbolId;

	/**
	 * Takes non-null value at pack time.
	 */
	private Identifier ugoCellId;

	private boolean protoEquipmentSet = false;

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
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentScheme
	 * @param parentSchemeElement
	 */
	SchemeElement(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IdlSchemeElementKind kind,
			final String name,
			final String description,
			final String label,
			final ProtoEquipment protoEquipment,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version, name, description, parentScheme);
		this.kind = (kind == null) ? 0 : kind.value();
		this.label = label;

		assert protoEquipment == null || equipment == null;
		this.protoEquipmentId = Identifier.possiblyVoid(protoEquipment);
		this.equipmentId = Identifier.possiblyVoid(equipment);

		this.kisId = Identifier.possiblyVoid(kis);
		this.siteNodeId = Identifier.possiblyVoid(siteNode);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);

		assert parentScheme == null || parentSchemeElement == null : EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
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
	private SchemeElement(final XmlIdentifier id,
			final String importType,			
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, SCHEMEELEMENT_CODE, created, creatorId);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeElement(final IdlSchemeElement transferable) throws CreateObjectException {
		this.fromTransferable((IdlStorableObject) transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null,
				null, null, null, null, null, parentScheme);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", "", null, null,
				null, null, null, null, null, parentSchemeElement);
	}

	/**
	 * Creates a new {@code SchemeElement} with the same {@code name},
	 * {@code ugoCell} and {@code schemeCell} as the {@code parentScheme}
	 * and inserts the {@code childScheme} into the newly created
	 * {@code SchemeElement}.
	 *
	 * @param creatorId
	 * @param childScheme
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeElement createInstance(final Identifier creatorId,
			final Scheme childScheme, final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

		try {
			assert childScheme != null : NON_VOID_EXPECTED;
			String name = childScheme.getName();
			assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert parentScheme != null : NON_NULL_EXPECTED;
			
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(IdentifierPool.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					SCHEME_CONTAINER,
					name,
					childScheme.getDescription(),
					childScheme.getLabel(),
					null,
					null,
					null,
					null,
					childScheme.getSymbol(),
					null,
					null,
					parentScheme,
					null);
			parentScheme.getSchemeElementContainerWrappee().addToCache(schemeElement, usePool);

			if (schemeElement.clonedIdMap == null) {
				schemeElement.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			/*
			 * Though these are ids of different types, Stas needs
			 * the pair to update the icon or whatever. 
			 */
			schemeElement.clonedIdMap.put(childScheme.getId(), schemeElement.id);
	
			final SchemeImageResource ugoCell = childScheme.getUgoCell0();
			if (ugoCell == null) {
				schemeElement.setUgoCell(null);
			} else {
				final SchemeImageResource ugoCellClone = ugoCell.clone();
				schemeElement.clonedIdMap.putAll(ugoCellClone.getClonedIdMap());
				schemeElement.setUgoCell(ugoCellClone);
			}
			final SchemeImageResource schemeCell = childScheme.getSchemeCell0();
			if (schemeCell == null) {
				schemeElement.setSchemeCell(null);
			} else {
				final SchemeImageResource schemeCellClone = schemeCell.clone();
				schemeElement.clonedIdMap.putAll(schemeCellClone.getClonedIdMap());
				schemeElement.setSchemeCell(schemeCellClone);
			}

			schemeElement.addScheme(childScheme, usePool);

			schemeElement.markAsChanged();
			return schemeElement;
		} catch (final CloneNotSupportedException cnse) {
			throw new CreateObjectException(cnse);
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, Scheme)}
	 *
	 * @param creatorId
	 * @param schemeProtoElement
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeElement createInstance(final Identifier creatorId,
			final SchemeProtoElement schemeProtoElement,
			final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

		try {
			final SchemeElement schemeElement = createInstance(creatorId,
					schemeProtoElement.getName(),
					schemeProtoElement.getDescription(),
					schemeProtoElement.getLabel(),
					schemeProtoElement.getProtoEquipment(),
					null,
					null,
					null,
					schemeProtoElement.getSymbol0(),
					null,
					null,
					parentScheme);

			schemeElement.fillProperties(schemeProtoElement, usePool);
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, ProtoEquipment, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param schemeProtoElement
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeElement createInstance(final Identifier creatorId,
			final SchemeProtoElement schemeProtoElement,
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		final boolean usePool = false;

		try {
			final SchemeElement schemeElement = createInstance(creatorId,
					schemeProtoElement.getName(),
					schemeProtoElement.getDescription(),
					schemeProtoElement.getLabel(),
					schemeProtoElement.getProtoEquipment(),
					null,
					null,
					null,
					schemeProtoElement.getSymbol0(),
					null,
					null,
					parentSchemeElement);

			schemeElement.fillProperties(schemeProtoElement, usePool);
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * Creates a new instance of kind
	 * {@link IdlSchemeElementKind#SCHEME_ELEMENT_CONTAINER SCHEME_ELEMENT_CONTAINER}
	 * with either a {@link ProtoEquipment} or an {@link Equipment} set
	 * and returns the newly created instance.
	 *
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param protoEquipment can be {@code null} if and only if
	 *        {@code equipment} is {@code null}.
	 * @param equipment can be {@code null} if anf only if
	 *        {@code protoEquipment} is {@code null}.
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {""})
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final ProtoEquipment protoEquipment,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert label != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(IdentifierPool.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					SCHEME_ELEMENT_CONTAINER,
					name,
					description,
					label,
					protoEquipment,
					equipment,
					kis,
					siteNode,
					symbol,
					ugoCell,
					schemeCell,
					parentScheme,
					null);
			parentScheme.getSchemeElementContainerWrappee().addToCache(schemeElement, usePool);

			schemeElement.protoEquipmentSet = (equipment != null || protoEquipment != null);

			schemeElement.markAsChanged();
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeElement.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * Creates a new instance of kind
	 * {@link IdlSchemeElementKind#SCHEME_ELEMENT_CONTAINER SCHEME_ELEMENT_CONTAINER}
	 * with either a {@link ProtoEquipment} or an {@link Equipment} set
	 * and returns the newly created instance.
	 *
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param protoEquipment can be {@code null} if and only if
	 *        {@code equipment} is {@code null}.
	 * @param equipment can be {@code null} if anf only if
	 *        {@code protoEquipment} is {@code null}.
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final ProtoEquipment protoEquipment,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert label != null : NON_NULL_EXPECTED;
		assert parentSchemeElement != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(IdentifierPool.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					SCHEME_ELEMENT_CONTAINER,					
					name,
					description,
					label,
					protoEquipment,
					equipment,
					kis,
					siteNode,
					symbol,
					ugoCell,
					schemeCell,
					null,
					parentSchemeElement);
			parentSchemeElement.getSchemeElementContainerWrappee().addToCache(schemeElement, usePool);

			schemeElement.protoEquipmentSet = (equipment != null || protoEquipment != null);

			schemeElement.markAsChanged();
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeElement.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeElement
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final XmlSchemeElement xmlSchemeElement,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlSchemeElement.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeElement schemeElement;
			if (id.isVoid()) {
				schemeElement = new SchemeElement(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeElement = StorableObjectPool.getStorableObject(id, true);
				if (schemeElement == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeElement = new SchemeElement(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeElement.fromXmlTransferable(xmlSchemeElement, importType);
			assert schemeElement.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeElement.markAsChanged();
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	@Override
	public SchemeElement clone() throws CloneNotSupportedException {
		final boolean usePool = false;

		try {
			final SchemeElement clone = super.clone();

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
			clone.schemeContainerWrappee = null;
			for (final Scheme scheme : this.getSchemes0(usePool)) {
				final Scheme schemeClone = scheme.clone();
				clone.clonedIdMap.putAll(schemeClone.getClonedIdMap());
				clone.addScheme(schemeClone, usePool);
			}
			clone.schemeElementContainerWrappee = null;
			for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
				final SchemeElement schemeElementClone =  schemeElement.clone();
				clone.clonedIdMap.putAll(schemeElementClone.getClonedIdMap());
				clone.addSchemeElement(schemeElementClone, usePool);
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
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.equipmentId != null && this.protoEquipmentId != null
				&& this.kisId != null
				&& this.parentSchemeElementId != null
				&& this.schemeCellId != null
				&& this.siteNodeId != null && this.symbolId != null
				&& this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.equipmentId);
		dependencies.add(this.protoEquipmentId);
		dependencies.add(this.kisId);
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.schemeCellId);
		dependencies.add(this.siteNodeId);
		dependencies.add(this.symbolId);
		dependencies.add(this.ugoCellId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * This implementation differs from other generic ones in the way that
	 * child {@code Scheme}s along with their dependencies are not included
	 * since they are saved and deleted separately.
	 *
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
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeElements0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	@Crutch136(notes = "SCHEME_CONTAINER should contain no equipmentId, and no check should be performed")
	Identifier getEquipmentId() {
		switch (this.getKind().value()) {
		case _SCHEME_ELEMENT_CONTAINER:
			assert true || this.assertProtoEquipmentSetStrict() : OBJECT_BADLY_INITIALIZED;
			if (!this.assertProtoEquipmentSetStrict()) {
				throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
			}
			break;
		case _SCHEME_CONTAINER:
			assert this.protoEquipmentId.isVoid();
			break;
		}
		assert this.equipmentId.isVoid() || this.equipmentId.getMajor() == EQUIPMENT_CODE;
		return this.equipmentId;
	}
	
	/**
	 * A wrapper around {@link #getEquipmentId()}.
	 */
	public Equipment getEquipment() {
		try {
			return StorableObjectPool.getStorableObject(this.getEquipmentId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	@Crutch136(notes = "SCHEME_CONTAINER should contain no equipmentId, and no check should be performed")
	Identifier getProtoEquipmentId() {
		switch (this.getKind().value()) {
		case _SCHEME_ELEMENT_CONTAINER:
			assert true || this.assertProtoEquipmentSetStrict(): OBJECT_BADLY_INITIALIZED;
			if (!this.assertProtoEquipmentSetStrict()) {
				throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
			}
			break;
		case _SCHEME_CONTAINER:
			assert this.protoEquipmentId.isVoid();
			break;
		}
		assert this.protoEquipmentId.isVoid() || this.protoEquipmentId.getMajor() == PROTOEQUIPMENT_CODE;
		return this.protoEquipmentId;
	}

	/**
	 * A wrapper around {@link #getProtoEquipmentId()}.
	 * 
	 * @throws ApplicationException
	 */
	public ProtoEquipment getProtoEquipment() throws ApplicationException {
		return this.getEquipmentId().isVoid()
				? StorableObjectPool.<ProtoEquipment>getStorableObject(this.getProtoEquipmentId(), true)
				: this.getEquipment().getProtoEquipment();
	}

	Identifier getKisId() {
		assert this.kisId != null: OBJECT_NOT_INITIALIZED;
		assert this.kisId.isVoid() || this.kisId.getMajor() == KIS_CODE;
		return this.kisId;
	}
	
	/**
	 * A wrapper around {@link #getKisId()}.
	 */
	public KIS getKis() {
		try {
			return StorableObjectPool.getStorableObject(this.getKisId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return this <code>SchemeElement</code>&apos;s label, or
	 *         empty string if none. Never returns <code>null</code>s.
	 */
	public String getLabel() {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		return this.label;
	}

	@Override
	public Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert this.parentSchemeElementId != null : OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdVoid = parentSchemeId1.isVoid();
		assert parentSchemeIdVoid ^ this.parentSchemeElementId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeIdVoid) {
			Log.debugMessage("Parent Scheme was requested, while parent is a SchemeElement; returning null",
					FINE);
		}
		return parentSchemeId1;
	}

	public Identifier getParentSchemeElementId() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid(); 
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("Parent SchemeElement was requested, while parent is a Scheme; returnung null",
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
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return <em>the first</em> <code>Scheme</code> inner to this
	 *         <code>SchemeElement</code>, or <code>null</code> if
	 *         none.
	 */
	public Scheme getScheme(final boolean usePool) throws ApplicationException {
		for (final Scheme scheme : this.getSchemes0(usePool)) {
			return scheme;
		}
		return null;
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
	private SchemeImageResource getSchemeCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getSchemeCellId(), true);
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

	Identifier getSymbolId() {
		assert this.symbolId != null: OBJECT_NOT_INITIALIZED;
		assert this.symbolId.isVoid() || this.symbolId.getMajor() == IMAGERESOURCE_CODE;
		return this.symbolId;
	}

	/**
	 * A wrapper around {@link #getSymbolId()}.
	 *
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		try {
			return StorableObjectPool.getStorableObject(this.getSymbolId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeElement getTransferable(final ORB orb) {
		return IdlSchemeElementHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.getName(),
				super.getDescription(),
				this.label,
				this.getKind(),
				this.getProtoEquipmentId().getTransferable(),
				this.getEquipmentId().getTransferable(),
				this.getKisId().getTransferable(),
				this.getSiteNodeId().getTransferable(),
				this.getSymbolId().getTransferable(),
				this.getUgoCellId().getTransferable(),
				this.getSchemeCellId().getTransferable(),
				this.getParentSchemeId().getTransferable(),
				this.getParentSchemeElementId().getTransferable());
	}

	/**
	 * @param schemeElement
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeElement schemeElement,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		super.getXmlTransferable(schemeElement, importType, usePool);
		if (schemeElement.isSetLabel()) {
			schemeElement.unsetLabel();
		}
		if (this.label.length() != 0) {
			schemeElement.setLabel(this.label);
		}
		schemeElement.setKind(XmlSchemeElement.Kind.Enum.forInt(this.getKind().value() + 1));
		if (schemeElement.isSetProtoEquipmentId()) {
			schemeElement.unsetProtoEquipmentId();
		}
		if (!this.protoEquipmentId.isVoid()) {
			this.protoEquipmentId.getXmlTransferable(schemeElement.addNewProtoEquipmentId(), importType);
		}
		if (schemeElement.isSetEquipmentId()) {
			schemeElement.unsetEquipmentId();
		}
		if (!this.equipmentId.isVoid()) {
			this.equipmentId.getXmlTransferable(schemeElement.addNewEquipmentId(), importType);
		}
		if (schemeElement.isSetKisId()) {
			schemeElement.unsetKisId();
		}
		if (!this.kisId.isVoid()) {
			this.kisId.getXmlTransferable(schemeElement.addNewKisId(), importType);
		}
		if (schemeElement.isSetSiteNodeId()) {
			schemeElement.unsetSiteNodeId();
		}
		if (!this.siteNodeId.isVoid()) {
			this.siteNodeId.getXmlTransferable(schemeElement.addNewSiteNodeId(), importType);
		}
		if (schemeElement.isSetSymbolId()) {
			schemeElement.unsetSymbolId();
		}
		if (!this.symbolId.isVoid()) {
			this.symbolId.getXmlTransferable(schemeElement.addNewSymbolId(), importType);
		}
		if (schemeElement.isSetUgoCellId()) {
			schemeElement.unsetUgoCellId();
		}
		if (!this.ugoCellId.isVoid()) {
			this.ugoCellId.getXmlTransferable(schemeElement.addNewUgoCellId(), importType);
		}
		if (schemeElement.isSetSchemeCellId()) {
			schemeElement.unsetSchemeCellId();
		}
		if (!this.schemeCellId.isVoid()) {
			this.schemeCellId.getXmlTransferable(schemeElement.addNewSchemeCellId(), importType);
		}
		if (schemeElement.isSetParentSchemeId()) {
			schemeElement.unsetParentSchemeId();
		}
		if (!super.parentSchemeId.isVoid()) {
			super.parentSchemeId.getXmlTransferable(schemeElement.addNewParentSchemeId(), importType);
		}
		if (schemeElement.isSetParentSchemeElementId()) {
			schemeElement.unsetParentSchemeElementId();
		}
		if (!this.parentSchemeElementId.isVoid()) {
			this.parentSchemeElementId.getXmlTransferable(schemeElement.addNewParentSchemeElementId(), importType);
		}
		if (schemeElement.isSetSchemeDevices()) {
			schemeElement.unsetSchemeDevices();
		}
		final Set<SchemeDevice> schemeDevices = this.getSchemeDevices0(usePool);
		if (!schemeDevices.isEmpty()) {
			final XmlSchemeDeviceSeq schemeDeviceSeq = schemeElement.addNewSchemeDevices();
			for (final SchemeDevice schemeDevice : schemeDevices) {
				schemeDevice.getXmlTransferable(schemeDeviceSeq.addNewSchemeDevice(), importType, usePool);
			}
		}
		if (schemeElement.isSetSchemes()) {
			schemeElement.unsetSchemes();
		}
		final Set<Scheme> schemes = this.getSchemes0(usePool);
		if (!schemes.isEmpty()) {
			final XmlSchemeSeq schemeSeq = schemeElement.addNewSchemes();
			for (final Scheme scheme : schemes) {
				scheme.getXmlTransferable(schemeSeq.addNewScheme(), importType, usePool);
			}
		}
		if (schemeElement.isSetSchemeElements()) {
			schemeElement.unsetSchemeElements();
		}
		final Set<SchemeElement> schemeElements = this.getSchemeElements0(usePool);
		if (!schemeElements.isEmpty()) {
			final XmlSchemeElementSeq schemeElementSeq = schemeElement.addNewSchemeElements();
			for (final SchemeElement schemeElement2 : schemeElements) {
				schemeElement2.getXmlTransferable(schemeElementSeq.addNewSchemeElement(), importType, usePool);
			}
		}
		if (schemeElement.isSetSchemeLinks()) {
			schemeElement.unsetSchemeLinks();
		}
		final Set<SchemeLink> schemeLinks = this.getSchemeLinks0(usePool);
		if (!schemeLinks.isEmpty()) {
			final XmlSchemeLinkSeq schemeLinkSeq = schemeElement.addNewSchemeLinks();
			for (final SchemeLink schemeLink : schemeLinks) {
				schemeLink.getXmlTransferable(schemeLinkSeq.addNewSchemeLink(), importType, usePool);
			}
		}
		XmlComplementorRegistry.complementStorableObject(schemeElement, SCHEMEELEMENT_CODE, importType, EXPORT);
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
	private SchemeImageResource getUgoCell0() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getUgoCellId(), true);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param label
	 * @param protoEquipmentId
	 * @param equipmentId
	 * @param kisId
	 * @param siteNodeId
	 * @param symbolId
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeId
	 * @param parentSchemeElementId
	 */
	@Crutch136(notes = "SCHEME_CONTAINER should contain no equipmentId, and no check should be performed")
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IdlSchemeElementKind kind,
			final String name,
			final String description,
			final String label,
			final Identifier protoEquipmentId,
			final Identifier equipmentId,
			final Identifier kisId,
			final Identifier siteNodeId,
			final Identifier symbolId,
			final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, parentSchemeId);

		assert kind != null : NON_NULL_EXPECTED;
		
		assert label != null : NON_NULL_EXPECTED;

		assert protoEquipmentId != null : NON_NULL_EXPECTED;
		assert equipmentId != null : NON_NULL_EXPECTED;

		switch (kind.value()) {
		case _SCHEME_ELEMENT_CONTAINER:
			assert protoEquipmentId.isVoid() ^ equipmentId.isVoid() : OBJECT_BADLY_INITIALIZED;
			break;
		case _SCHEME_CONTAINER:
			/*
			 * And I don't care what equipmentId holds.
			 */
			assert protoEquipmentId.isVoid() : OBJECT_BADLY_INITIALIZED;
			break;
		}

		assert kisId != null : NON_NULL_EXPECTED;
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert symbolId != null : NON_NULL_EXPECTED;
		assert ugoCellId != null : NON_NULL_EXPECTED;
		assert schemeCellId != null : NON_NULL_EXPECTED;

		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() ^ parentSchemeElementId.isVoid();

		this.kind = kind.value();
		this.label = label;
		this.protoEquipmentId = protoEquipmentId;
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;

		this.protoEquipmentSet = true;
	}

	/**
	 * @param equipment
	 */
	@Crutch136(notes = "SCHEME_CONTAINER should have no equipment at all")
	public void setEquipment(final Equipment equipment) {
		final Identifier newEquipmentId = Identifier.possiblyVoid(equipment);

		switch (this.getKind().value()) {
		case _SCHEME_ELEMENT_CONTAINER:
			assert this.assertProtoEquipmentSetNonStrict(): OBJECT_BADLY_INITIALIZED;

			if (this.equipmentId.equals(newEquipmentId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}

			if (this.equipmentId.isVoid()) {
				/*
				 * Erasing old object-type value, setting new object
				 * value.
				 */
				this.protoEquipmentId = VOID_IDENTIFIER;
			} else if (newEquipmentId.isVoid()) {
				/*
				 * Erasing old object value, preserving old object-type
				 * value. This point is not assumed to be reached unless
				 * initial object value has already been set (i. e.
				 * there already is object-type value to preserve).
				 */
				this.protoEquipmentId = this.getEquipment().getProtoEquipmentId();
			}
			break;
		case _SCHEME_CONTAINER:
			/*
			 * No check here.
			 */
			break;
		}

		this.equipmentId = newEquipmentId;
		super.markAsChanged();
	}

	/**
	 * @param protoEquipment
	 */
	public void setProtoEquipment(final ProtoEquipment protoEquipment) {
		assert this.assertProtoEquipmentSetNonStrict(): OBJECT_BADLY_INITIALIZED;
		assert protoEquipment != null: NON_NULL_EXPECTED;

		if (!this.equipmentId.isVoid())
			this.getEquipment().setProtoEquipment(protoEquipment);
		else {
			final Identifier newProtoEquipmentId = protoEquipment.getId();
			if (this.protoEquipmentId.equals(newProtoEquipmentId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.protoEquipmentId = newProtoEquipmentId;
			super.markAsChanged();
		}
	}

	/**
	 * @param kis
	 */
	public void setKis(final KIS kis) {
		final Identifier newKisId = Identifier.possiblyVoid(kis);
		if (this.kisId.equals(newKisId))
			return;
		this.kisId = newKisId;
		super.markAsChanged();
	}

	/**
	 * @param label cannot be <code>null</code>. For this purpose, supply
	 *        an empty string as an argument.
	 */
	public void setLabel(final String label) {
		assert this.label != null: OBJECT_NOT_INITIALIZED;
		assert label != null: NON_NULL_EXPECTED;
		if (this.label.equals(label))
			return;
		this.label = label;
		super.markAsChanged();
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
		assert this.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		final boolean thisParentSchemeIdVoid = this.parentSchemeId.isVoid();
		assert thisParentSchemeIdVoid ^ this.parentSchemeElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		final boolean parentSchemeNull = (parentScheme == null);

		final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeIdVoid) {
			/*
			 * Moving from a scheme element to a scheme. At this
			 * point, newParentSchemeId is non-void.
			 */
			this.getParentSchemeElement().getSchemeElementContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeElementId = VOID_IDENTIFIER;
		} else {
			/*
			 * Moving from a scheme to another scheme. At this
			 * point, newParentSchemeId may be void.
			 */
			this.getParentScheme().getSchemeElementContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		}

		if (!parentSchemeNull) {
			parentScheme.getSchemeElementContainerWrappee().addToCache(this, usePool);
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
	 */
	public void setParentSchemeElement(final SchemeElement parentSchemeElement,
			final boolean usePool)
	throws ApplicationException {
		if (parentSchemeElement != null
				&& parentSchemeElement.getKind() == SCHEME_CONTAINER) {
			throw new ClassCastException();
		}

		assert super.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		final boolean thisParentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert super.parentSchemeId.isVoid() ^ thisParentSchemeElementIdVoid: EXACTLY_ONE_PARENT_REQUIRED;
		assert parentSchemeElement == null || !parentSchemeElement.equals(this) : CIRCULAR_DEPS_PROHIBITED;

		final boolean parentSchemeElementNull = (parentSchemeElement == null);

		final Identifier newParentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
		if (this.parentSchemeElementId.equals(newParentSchemeElementId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeElementIdVoid) {
			/*
			 * Moving from a scheme to a scheme element. At this
			 * point, newParentSchemeElementId is non-void.
			 */
			super.getParentScheme().getSchemeElementContainerWrappee().removeFromCache(this, usePool);

			super.parentSchemeId = VOID_IDENTIFIER;
		} else {
			/*
			 * Moving from a scheme element to another scheme element.
			 * At this point, newParentSchemeElementId may be void.
			 */
			this.getParentSchemeElement().getSchemeElementContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeElementNull) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			}
		}

		if (!parentSchemeElementNull) {
			parentSchemeElement.getSchemeElementContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setScheme(final Scheme scheme, final boolean usePool)
	throws ApplicationException {
		this.setSchemes(scheme == null
						? Collections.<Scheme>emptySet()
						: Collections.singleton(scheme),
				usePool);
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

	public void setSiteNode(final SiteNode siteNode) {
		final Identifier newSiteNodeId = Identifier.possiblyVoid(siteNode);
		if (this.siteNodeId.equals(newSiteNodeId))
			return;
		this.siteNodeId = newSiteNodeId;
		super.markAsChanged();
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
			final IdlSchemeElement schemeElement = (IdlSchemeElement) transferable;
			super.fromTransferable(schemeElement);
			this.label = schemeElement.label;
			this.kind = schemeElement.kind.value();
			this.protoEquipmentId = new Identifier(schemeElement.protoEquipmentId);
			this.equipmentId = new Identifier(schemeElement.equipmentId);
			this.kisId = new Identifier(schemeElement.kisId);
			this.siteNodeId = new Identifier(schemeElement.siteNodeId);
			this.symbolId = new Identifier(schemeElement.symbolId);
			this.ugoCellId = new Identifier(schemeElement.ugoCellId);
			this.schemeCellId = new Identifier(schemeElement.schemeCellId);
			this.parentSchemeElementId = new Identifier(schemeElement.parentSchemeElementId);
	
			this.protoEquipmentSet = true;
		}
	}

	/**
	 * @param schemeElement
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Crutch136(notes = "update the XML model")
	public void fromXmlTransferable(
			final XmlSchemeElement schemeElement,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeElement, SCHEMEELEMENT_CODE, importType, PRE_IMPORT);

		super.fromXmlTransferable(schemeElement, importType);

		this.label = schemeElement.isSetLabel()
				? schemeElement.getLabel()
				: "";
		final Enum xmlKind = schemeElement.getKind();
		this.kind = xmlKind.intValue() - 1;

		final boolean setProtoEquipmentId = schemeElement.isSetProtoEquipmentId();
		final boolean setEquipmentId = schemeElement.isSetEquipmentId();
		switch (xmlKind.intValue()) {
		case INT_SCHEME_ELEMENT_CONTAINER:
			if (setProtoEquipmentId) {
				assert !setEquipmentId : OBJECT_STATE_ILLEGAL;

				this.protoEquipmentId = Identifier.fromXmlTransferable(schemeElement.getProtoEquipmentId(), importType, MODE_THROW_IF_ABSENT);
				this.equipmentId = VOID_IDENTIFIER;
			} else if (setEquipmentId) {
				assert !setProtoEquipmentId : OBJECT_STATE_ILLEGAL;

				this.protoEquipmentId = VOID_IDENTIFIER;
				this.equipmentId = Identifier.fromXmlTransferable(schemeElement.getEquipmentId(), importType, MODE_THROW_IF_ABSENT);
			} else {
				throw new UpdateObjectException(
						"SchemeElement.fromXmlTransferable() | "
						+ XML_BEAN_NOT_COMPLETE);
			}
			break;
		case INT_SCHEME_CONTAINER:
			assert !setProtoEquipmentId : OBJECT_STATE_ILLEGAL;
			this.protoEquipmentId = VOID_IDENTIFIER;
			this.equipmentId = setEquipmentId
					? Identifier.fromXmlTransferable(schemeElement.getEquipmentId(), importType, MODE_THROW_IF_ABSENT)
					: VOID_IDENTIFIER;
			break;
		}

		this.kisId = schemeElement.isSetKisId()
				? Identifier.fromXmlTransferable(schemeElement.getKisId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.siteNodeId = schemeElement.isSetSiteNodeId()
				? Identifier.fromXmlTransferable(schemeElement.getSiteNodeId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.symbolId = schemeElement.isSetSymbolId()
				? Identifier.fromXmlTransferable(schemeElement.getSymbolId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.ugoCellId = schemeElement.isSetUgoCellId()
				? Identifier.fromXmlTransferable(schemeElement.getUgoCellId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.schemeCellId = schemeElement.isSetSchemeCellId()
				? Identifier.fromXmlTransferable(schemeElement.getSchemeCellId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;

		final boolean setParentSchemeId = schemeElement.isSetParentSchemeId();
		final boolean setParentSchemeElementId = schemeElement.isSetParentSchemeElementId();
		if (setParentSchemeId) {
			assert !setParentSchemeElementId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeId = Identifier.fromXmlTransferable(schemeElement.getParentSchemeId(), importType, MODE_THROW_IF_ABSENT);
			this.parentSchemeElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeElementId) {
			assert !setParentSchemeId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeId = VOID_IDENTIFIER;
			this.parentSchemeElementId = Identifier.fromXmlTransferable(schemeElement.getParentSchemeElementId(), importType, MODE_THROW_IF_ABSENT);
		} else {
			throw new UpdateObjectException(
					"SchemeElement.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		if (schemeElement.isSetSchemeDevices()) {
			for (final XmlSchemeDevice schemeDevice : schemeElement.getSchemeDevices().getSchemeDeviceArray()) {
				SchemeDevice.createInstance(super.creatorId, schemeDevice, importType);
			}
		}
		if (schemeElement.isSetSchemes()) {
			for (final XmlScheme scheme : schemeElement.getSchemes().getSchemeArray()) {
				Scheme.createInstance(super.creatorId, scheme);
			}
		}
		if (schemeElement.isSetSchemeElements()) {
			for (final XmlSchemeElement schemeElement2 : schemeElement.getSchemeElements().getSchemeElementArray()) {
				createInstance(super.creatorId, schemeElement2, importType);
			}
		}
		if (schemeElement.isSetSchemeLinks()) {
			for (final XmlSchemeLink schemeLink : schemeElement.getSchemeLinks().getSchemeLinkArray()) {
				SchemeLink.createInstance(super.creatorId, schemeLink, importType);
			}
		}

		this.protoEquipmentSet = true;

		XmlComplementorRegistry.complementStorableObject(schemeElement, SCHEMEELEMENT_CODE, importType, POST_IMPORT);
	}

	public IdlSchemeElementKind getKind() {
		return IdlSchemeElementKind.from_int(this.kind);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeElementWrapper getWrapper() {
		return SchemeElementWrapper.getInstance();
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
		schemeDevice.setParentSchemeElement(this, usePool);
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
		assert schemeDevice.getParentSchemeElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeDevice.setParentSchemeElement(null, usePool);
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
	 * Children manipulation: scheme links, lynx and w3m                  *
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
		schemeLink.setParentSchemeElement(this, usePool);
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
		assert schemeLink.getParentSchemeElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentSchemeElement(null, usePool);
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
	 * Children manipulation: scheme elements                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeElement> schemeElementContainerWrappee;

	StorableObjectContainerWrappee<SchemeElement> getSchemeElementContainerWrappee() {
		return (this.schemeElementContainerWrappee == null)
				? this.schemeElementContainerWrappee = new StorableObjectContainerWrappee<SchemeElement>(this, SCHEMEELEMENT_CODE)
				: this.schemeElementContainerWrappee;
	}

	/**
	 * @param schemeElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement != this: CIRCULAR_DEPS_PROHIBITED;
		schemeElement.setParentSchemeElement(this, usePool);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement.getParentSchemeElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentSchemeElement(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<SchemeElement> getSchemeElements(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeElements0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	@Crutch136(notes = "SCHEME_CONTAINER should contain no #getSchemeElements() method")
	Set<SchemeElement> getSchemeElements0(final boolean usePool)
	throws ApplicationException {
		switch (this.getKind().value()) {
		case _SCHEME_ELEMENT_CONTAINER:
			return this.getSchemeElementContainerWrappee().getContainees(usePool);
		case _SCHEME_CONTAINER:
		default:
			return Collections.emptySet();	
		}
	}

	/**
	 * @param schemeElements
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeElements(final Set<SchemeElement> schemeElements,
			final boolean usePool)
	throws ApplicationException {
		assert schemeElements != null: NON_NULL_EXPECTED;

		final Set<SchemeElement> oldSchemeElements = this.getSchemeElements0(usePool);

		final Set<SchemeElement> toRemove = new HashSet<SchemeElement>(oldSchemeElements);
		toRemove.removeAll(schemeElements);
		for (final SchemeElement schemeElement : toRemove) {
			this.removeSchemeElement(schemeElement, usePool);
		}

		final Set<SchemeElement> toAdd = new HashSet<SchemeElement>(schemeElements);
		toAdd.removeAll(oldSchemeElements);
		for (final SchemeElement schemeElement : toAdd) {
			this.addSchemeElement(schemeElement, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: schemes                                     *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Scheme> schemeContainerWrappee;

	StorableObjectContainerWrappee<Scheme> getSchemeContainerWrappee() {
		return (this.schemeContainerWrappee == null)
				? this.schemeContainerWrappee = new StorableObjectContainerWrappee<Scheme>(this, SCHEME_CODE)
				: this.schemeContainerWrappee;
	}

	/**
	 * @param scheme cannot be <code>null</code>.
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addScheme(final Scheme scheme, final boolean usePool)
	throws ApplicationException {
		assert scheme != null: NON_NULL_EXPECTED;
		scheme.setParentSchemeElement(this, usePool);
	}

	/**
	 * The <code>Scheme</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param scheme
	 * @param usePool
	 */
	public void removeScheme(final Scheme scheme, final boolean usePool)
	throws ApplicationException {
		assert scheme != null: NON_NULL_EXPECTED;
		assert scheme.getParentSchemeElementId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		scheme.setParentSchemeElement(null, usePool);
	}

	/**
	 * @param usePool
	 * @return an immutable set.
	 * @throws ApplicationException
	 */
	public Set<Scheme> getSchemes(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemes0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	@Crutch136(notes = "SCHEME_ELEMENT_CONTAINER should have no #getSchemes() method")
	Set<Scheme> getSchemes0(final boolean usePool)
	throws ApplicationException {
		switch (this.getKind().value()) {
		case _SCHEME_CONTAINER:
			return this.getSchemeContainerWrappee().getContainees(usePool);
		case _SCHEME_ELEMENT_CONTAINER:
		default:
			return Collections.emptySet();
		}
	}

	/**
	 * @param schemes
	 * @param usePool
	 * @throws ApplicationException 
	 * @see Scheme#setSchemeElements(Set, boolean)
	 * @todo Check for circular dependencies.
	 */
	public void setSchemes(final Set<Scheme> schemes, final boolean usePool)
	throws ApplicationException {
		assert schemes != null: NON_NULL_EXPECTED;

		final Set<Scheme> oldSchemes = this.getSchemes0(usePool);

		final Set<Scheme> toRemove = new HashSet<Scheme>(oldSchemes);
		toRemove.removeAll(schemes);
		for (final Scheme scheme : toRemove) {
			this.removeScheme(scheme, usePool);
		}

		final Set<Scheme> toAdd = new HashSet<Scheme>(schemes);
		toAdd.removeAll(oldSchemes);
		for (final Scheme scheme : toAdd) {
			this.addScheme(scheme, usePool);
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertProtoEquipmentSetNonStrict() {
		if (this.protoEquipmentSet)
			return this.assertProtoEquipmentSetStrict();
		this.protoEquipmentSet = true;
		return this.equipmentId != null
				&& this.protoEquipmentId != null
				&& this.equipmentId.isVoid()
				&& this.protoEquipmentId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertProtoEquipmentSetStrict() {
		return this.equipmentId != null
				&& this.protoEquipmentId != null
				&& (this.equipmentId.isVoid() ^ this.protoEquipmentId.isVoid());
	}

	/**
	 * Invoked just upon creation of a new instance from
	 * {@link #createInstance(Identifier, SchemeProtoElement, Scheme)} and
	 * {@link #createInstance(Identifier, SchemeProtoElement, SchemeElement)}.
	 *
	 * Actions taken are similar to those in {@link SchemeProtoElement#clone()}.
	 *
	 * @param schemeProtoElement
	 * @param usePool
	 * @throws ApplicationException
	 * @see SchemeProtoElement#clone()
	 */
	private void fillProperties(final SchemeProtoElement schemeProtoElement, final boolean usePool)
	throws ApplicationException {
		try {
			if (super.clonedIdMap == null) {
				super.clonedIdMap = new HashMap<Identifier, Identifier>();
			}

			/*
			 * Though these are ids of different types, Stas needs
			 * the pair to update the icon or whatever. 
			 */
			super.clonedIdMap.put(schemeProtoElement.getId(), super.id);
	
			final SchemeImageResource ugoCell = schemeProtoElement.getUgoCell0();
			if (ugoCell == null) {
				this.setUgoCell(null);
			} else {
				final SchemeImageResource ugoCellClone = ugoCell.clone();
				super.clonedIdMap.putAll(ugoCellClone.getClonedIdMap());
				this.setUgoCell(ugoCellClone);
			}
			final SchemeImageResource schemeCell = schemeProtoElement.getSchemeCell0();
			if (schemeCell == null) {
				this.setSchemeCell(null);
			} else {
				final SchemeImageResource schemeCellClone = schemeCell.clone();
				super.clonedIdMap.putAll(schemeCellClone.getClonedIdMap());
				this.setSchemeCell(schemeCellClone);
			}
			for (final Characteristic characteristic : schemeProtoElement.getCharacteristics0(usePool)) {
				final Characteristic characteristicClone = characteristic.clone();
				super.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				this.addCharacteristic(characteristicClone, usePool);
			}
			for (final SchemeDevice schemeDevice : schemeProtoElement.getSchemeDevices0(usePool)) {
				final SchemeDevice schemeDeviceClone = schemeDevice.clone();
				super.clonedIdMap.putAll(schemeDeviceClone.getClonedIdMap());
				this.addSchemeDevice(schemeDeviceClone, usePool);
			}
			for (final SchemeLink schemeLink : schemeProtoElement.getSchemeLinks0(usePool)) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				super.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				this.addSchemeLink(schemeLinkClone, usePool);
			}
			for (final SchemeProtoElement schemeProtoElement2 : schemeProtoElement.getSchemeProtoElements(usePool)) {
				final SchemeElement schemeElement = SchemeElement.createInstance(super.creatorId, schemeProtoElement2, this);
				super.clonedIdMap.putAll(schemeElement.getClonedIdMap());
				this.addSchemeElement(schemeElement, usePool);
			}
			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : this.getSchemeLinks0(usePool)) {
				final Identifier sourceSchemePortId = super.clonedIdMap.get(schemeLink.sourceAbstractSchemePortId);
				final Identifier targetSchemePortId = super.clonedIdMap.get(schemeLink.targetAbstractSchemePortId);
				schemeLink.setSourceAbstractSchemePortId((sourceSchemePortId == null) ? VOID_IDENTIFIER : sourceSchemePortId);
				schemeLink.setTargetAbstractSchemePortId((targetSchemePortId == null) ? VOID_IDENTIFIER : targetSchemePortId);
			}
		} catch (final CloneNotSupportedException cnse) {
			throw new CreateObjectException(cnse);
		}
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemePort> getSchemePortsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemePort> schemePorts = new HashSet<SchemePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0(usePool)) {
			schemePorts.addAll(schemeDevice.getSchemePorts0(usePool));
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemePorts.addAll(schemeElement.getSchemePortsRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemePorts);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeCablePort> getSchemeCablePortsRecursively(
			final boolean usePool)
	throws ApplicationException {
		final Set<SchemeCablePort> schemeCablePorts = new HashSet<SchemeCablePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0(usePool)) {
			schemeCablePorts.addAll(schemeDevice.getSchemeCablePorts0(usePool));
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemeCablePorts.addAll(schemeElement.getSchemeCablePortsRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeLink> getSchemeLinksRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
		schemeLinks.addAll(this.getSchemeLinks0(usePool));
		for (final Scheme scheme : this.getSchemes0(usePool)) {
			schemeLinks.addAll(scheme.getSchemeLinksRecursively(usePool));
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemeLinks.addAll(schemeElement.getSchemeLinksRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeLinks);
	}

	/**
	 * @throws ApplicationException
	 */
	public Set<SchemeCableLink> getSchemeCableLinksRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
		for (final Scheme scheme : this.getSchemes0(usePool)) {
			schemeCableLinks.addAll(scheme.getSchemeCableLinksRecursively(usePool));
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			schemeCableLinks.addAll(schemeElement.getSchemeCableLinksRecursively(usePool));
		}
		return Collections.unmodifiableSet(schemeCableLinks);
	}

	public SchemePath getAlarmedPath() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public PathElement getAlarmedPathElement() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public void setAlarmedPath(@SuppressWarnings("unused") final SchemePath alarmedPath) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public void setAlarmedPathElement(@SuppressWarnings("unused") final PathElement alarmedPathElement) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	void setEquipmentId(Identifier equipmentId) {
//		TODO: inroduce additional sanity checks
		assert equipmentId != null : NON_NULL_EXPECTED;
		assert equipmentId.isVoid() || equipmentId.getMajor() == EQUIPMENT_CODE;
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}

	void setProtoEquipmentId(Identifier protoEquipmentId) {
//		TODO: inroduce additional sanity checks
		assert protoEquipmentId != null : NON_NULL_EXPECTED;
		assert protoEquipmentId.isVoid() || protoEquipmentId.getMajor() == PROTOEQUIPMENT_CODE;
		this.protoEquipmentId = protoEquipmentId;
		super.markAsChanged();
	}

	void setSiteNodeId(Identifier siteNodeId) {
//		TODO: inroduce additional sanity checks
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert siteNodeId.isVoid() || siteNodeId.getMajor() == SITENODE_CODE;
		this.siteNodeId = siteNodeId;
		super.markAsChanged();
	}

	void setKisId(Identifier kisId) {
//		TODO: inroduce additional sanity checks
		assert kisId != null : NON_NULL_EXPECTED;
		assert kisId.isVoid() || kisId.getMajor() == KIS_CODE;
		this.kisId = kisId;
		super.markAsChanged();
	}

	/*-********************************************************************
	 * Shitlets                                                           *
	 **********************************************************************/

	/**
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	Set<SchemeElement> getChildSchemeElementsRecursively(final boolean usePool)
	throws ApplicationException {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		final Set<Scheme> schemes = this.getSchemes0(usePool);
		if (schemes.isEmpty()) {
			for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
				schemeElements.addAll(schemeElement.getChildSchemeElementsRecursively(usePool));
				schemeElements.add(schemeElement);
			}
		} else {
			for (final Scheme scheme : schemes) {
				schemeElements.addAll(scheme.getTopLevelSchemeElementsRecursively(usePool));
			}
		}
		return schemeElements;
	}

	/**
	 * @param schemeLinkId
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsSchemeLink(final Identifier schemeLinkId,
			final boolean usePool)
	throws ApplicationException {
		if (this.getSchemeLinks0(usePool).contains(schemeLinkId)) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			if (schemeElement.getSchemes0(usePool).isEmpty()
					&& schemeElement.containsSchemeLink(schemeLinkId, usePool)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param schemeElement
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsSchemeElement(final SchemeElement schemeElement,
			final boolean usePool)
	throws ApplicationException {
		if (schemeElement.getParentSchemeElementId().equals(this)) {
			return true;
		}
		final Set<Scheme> schemes = this.getSchemes0(usePool);
		if (schemes.isEmpty()) {
			for (final SchemeElement schemeElement1 : this.getSchemeElements0(usePool)) {
				final Set<Scheme> schemes1 = schemeElement1.getSchemes0(usePool);
				if (schemes1.isEmpty()) {
					if (schemeElement1.containsSchemeElement(schemeElement, usePool)) {
						return true;
					}
				} else {
					for (final Scheme scheme : schemes1) {
						if (scheme.containsSchemeElement(schemeElement, usePool)) {
							return true;
						}
					}
				}
			}
		} else {
			for (final Scheme scheme : schemes) {
				if (scheme.containsSchemeElement(schemeElement, usePool)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param abstractSchemePort
	 * @throws ApplicationException
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public boolean containsAbstractSchemePort(
			final AbstractSchemePort<?> abstractSchemePort,
			final boolean usePool)
	throws ApplicationException {
		if (this.getSchemeDevices0(usePool).contains(abstractSchemePort.getParentSchemeDeviceId())) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0(usePool)) {
			if (schemeElement.getSchemes0(usePool).isEmpty()
					&& schemeElement.containsAbstractSchemePort(abstractSchemePort, usePool)) {
				return true;
			}
		}
		return false;
	}
}
