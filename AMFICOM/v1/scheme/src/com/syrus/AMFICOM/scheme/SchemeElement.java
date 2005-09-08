/*-
 * $Id: SchemeElement.java,v 1.93 2005/09/08 17:06:51 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind.SCHEME_CONTAINER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER;
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

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementHelper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.93 $, $Date: 2005/09/08 17:06:51 $
 * @module scheme
 */
public final class SchemeElement extends AbstractSchemeElement
		implements SchemeCellContainer,
		XmlBeansTransferable<XmlSchemeElement> {
	private static final long serialVersionUID = 3618977875802797368L;

	private IdlSchemeElementKind kind;
	
	private Identifier equipmentId;

	private Identifier equipmentTypeId;

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

	private boolean equipmentTypeSet = false;
		
	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeElement(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(SCHEMEELEMENT_CODE).retrieve(this);
			this.equipmentTypeSet = true;
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
	 * @param name
	 * @param description
	 * @param label
	 * @param equipmentType
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
			final EquipmentType equipmentType,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version, name, description, parentScheme);
		this.kind = kind;
		this.label = label;

		assert equipmentType == null || equipment == null;
		this.equipmentTypeId = Identifier.possiblyVoid(equipmentType);
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
	 * @param created
	 * @param creatorId
	 */
	private SchemeElement(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id, created, creatorId);
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
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, Scheme)}.
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
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
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
	 * and inserts the {@childScheme} into the newly created
	 * {@code SchemeElement}.
	 *
	 * @param creatorId
	 * @param childScheme
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final Scheme childScheme, final Scheme parentScheme)
	throws CreateObjectException {
		try {
			assert childScheme != null : NON_VOID_EXPECTED;
			String name = childScheme.getName();
			assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert parentScheme != null : NON_NULL_EXPECTED;
			
			BitmapImageResource symbol = null;
			try {
				symbol = StorableObjectPool.getStorableObject(childScheme.getSymbolId(), true);
			} catch (final ApplicationException ae) {
				Log.debugException(ae, SEVERE);
			}
			
			//final SchemeElement schemeElement = createInstance(creatorId, name, "", "", null, null,
			//		null, null, null, null, null, parentScheme);
			final SchemeElement schemeElement;
			try {
				final Date created = new Date();
				schemeElement = new SchemeElement(IdentifierPool.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
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
						symbol,
						null,
						null,
						parentScheme,
						null);
				schemeElement.markAsChanged();
			} catch (final IdentifierGenerationException ige) {
				throw new CreateObjectException("SchemeElement.createInstance | cannot generate identifier ", ige);
			}

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

			schemeElement.addScheme(childScheme);

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
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, Scheme)}
	 *
	 * @param creatorId
	 * @param schemeProtoElement
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final SchemeProtoElement schemeProtoElement,
			final Scheme parentScheme)
	throws CreateObjectException {
		try {
			final SchemeElement schemeElement = createInstance(creatorId,
					schemeProtoElement.getName(),
					schemeProtoElement.getDescription(),
					schemeProtoElement.getLabel(),
					schemeProtoElement.getEquipmentType0(),
					null,
					null,
					null,
					schemeProtoElement.getSymbol0(),
					null,
					null,
					parentScheme);

			schemeElement.fillProperties(schemeProtoElement, creatorId);
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, String, EquipmentType, Equipment, KIS, SiteNode, BitmapImageResource, SchemeImageResource, SchemeImageResource, SchemeElement)}.
	 *
	 * @param creatorId
	 * @param schemeProtoElement
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final SchemeProtoElement schemeProtoElement,
			final SchemeElement parentSchemeElement)
	throws CreateObjectException {
		try {
			final SchemeElement schemeElement = createInstance(creatorId,
					schemeProtoElement.getName(),
					schemeProtoElement.getDescription(),
					schemeProtoElement.getLabel(),
					schemeProtoElement.getEquipmentType0(),
					null,
					null,
					null,
					schemeProtoElement.getSymbol0(),
					null,
					null,
					parentSchemeElement);

			schemeElement.fillProperties(schemeProtoElement, creatorId);
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme) throws CreateObjectException {
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
					equipmentType,
					equipment,
					kis,
					siteNode,
					symbol,
					ugoCell,
					schemeCell,
					parentScheme,
					null);
			schemeElement.markAsChanged();
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeElement.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param label cannot be <code>null</code>, but can be empty.
	 * @param equipmentType
	 * @param equipment
	 * @param kis
	 * @param siteNode
	 * @param symbol
	 * @param ugoCell
	 * @param schemeCell
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeElement createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final String label,
			final EquipmentType equipmentType,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement) throws CreateObjectException {
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
					equipmentType,
					equipment,
					kis,
					siteNode,
					symbol,
					ugoCell,
					schemeCell,
					null,
					parentSchemeElement);
			schemeElement.markAsChanged();
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeElement.createInstance | cannot generate identifier ", ige);
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
			final Identifier id = Identifier.fromXmlTransferable(xmlSchemeElement.getId(), SCHEMEELEMENT_CODE, importType);
			SchemeElement schemeElement = StorableObjectPool.getStorableObject(id, true);
			if (schemeElement == null) {
				schemeElement = new SchemeElement(id, new Date(), creatorId);
			}
			schemeElement.fromXmlTransferable(xmlSchemeElement, importType);
			assert schemeElement.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeElement.markAsChanged();
			return schemeElement;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param scheme cannot be <code>null</code>.
	 */
	public void addScheme(final Scheme scheme) {
		assert scheme != null: NON_NULL_EXPECTED;
		scheme.setParentSchemeElement(this);
	}

	/**
	 * @param schemeDevice cannot be <code>null</code>.
	 */
	public void addSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		schemeDevice.setParentSchemeElement(this);
	}

	/**
	 * @param schemeElement can be neither <code>null</code> nor
	 *        <code>this</code>.
	 */
	public void addSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement != this: CIRCULAR_DEPS_PROHIBITED;
		schemeElement.setParentSchemeElement(this);
	}

	/**
	 * @param schemeLink cannot be <code>null</code>.
	 */
	public void addSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		schemeLink.setParentSchemeElement(this);
	}

	@Override
	public SchemeElement clone() throws CloneNotSupportedException {
		try {
			final SchemeElement clone = (SchemeElement) super.clone();

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
			for (final Characteristic characteristic : this.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				clone.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(clone.id);
			}
			for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
				final SchemeDevice schemeDeviceClone = schemeDevice.clone();
				clone.clonedIdMap.putAll(schemeDeviceClone.getClonedIdMap());
				clone.addSchemeDevice(schemeDeviceClone);
			}
			for (final SchemeLink schemeLink : this.getSchemeLinks0()) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				clone.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				clone.addSchemeLink(schemeLinkClone);
			}
			for (final Scheme scheme : this.getSchemes0()) {
				final Scheme schemeClone = scheme.clone();
				clone.clonedIdMap.putAll(schemeClone.getClonedIdMap());
				clone.addScheme(schemeClone);
			}
			for (final SchemeElement schemeElement : this.getSchemeElements0()) {
				final SchemeElement schemeElementClone =  schemeElement.clone();
				clone.clonedIdMap.putAll(schemeElementClone.getClonedIdMap());
				clone.addSchemeElement(schemeElementClone);
			}

			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : clone.getSchemeLinks0()) {
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
		assert this.equipmentId != null && this.equipmentTypeId != null
				&& this.kisId != null
				&& this.parentSchemeElementId != null
				&& this.schemeCellId != null
				&& this.siteNodeId != null && this.symbolId != null
				&& this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(super.getDependencies());
		dependencies.add(this.equipmentId);
		dependencies.add(this.equipmentTypeId);
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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public Set<Identifiable> getReverseDependencies() throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics(true)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeDevices0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeLinks0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeElements0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		// fix by Stas
		// no need save(or delete!) subschemes this way
		// they should save independently in order to save correct scheme image
		// fact of saving tracked in client
//		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemes0()) {
//			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
//		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	Identifier getEquipmentId() {
		if (this.kind == SCHEME_ELEMENT_CONTAINER) {
			assert true || this.assertEquipmentTypeSetStrict() : OBJECT_BADLY_INITIALIZED;
			if (!this.assertEquipmentTypeSetStrict()) {
				throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
			}
			assert this.equipmentId.isVoid() || this.equipmentId.getMajor() == EQUIPMENT_CODE;
		}
		return this.equipmentId;
	}
	
	/**
	 * A wrapper around {@link #getEquipmentId()}.
	 */
	public Equipment getEquipment() {
		try {
			return StorableObjectPool.getStorableObject(this.getEquipmentId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getEquipmentTypeId() {
		if (this.kind == SCHEME_ELEMENT_CONTAINER) {
			assert true || this.assertEquipmentTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
			if (!this.assertEquipmentTypeSetStrict()) {
				throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
			}
			assert this.equipmentTypeId.isVoid() || this.equipmentTypeId.getMajor() == EQUIPMENT_TYPE_CODE;
		}
		return this.equipmentTypeId;
	}

	/**
	 * A wrapper around {@link #getEquipmentTypeId()}. 
	 */
	public EquipmentType getEquipmentType() {
		try {
			return this.getEquipmentId().isVoid()
					? StorableObjectPool.<EquipmentType>getStorableObject(this.getEquipmentTypeId(), true)
					: this.getEquipment().getType();
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
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
			Log.debugException(ae, SEVERE);
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
	Identifier getParentSchemeId() {
		final Identifier parentSchemeId1 = super.getParentSchemeId();
		assert this.parentSchemeElementId != null : OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdVoid = parentSchemeId1.isVoid();
		assert parentSchemeIdVoid ^ this.parentSchemeElementId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeIdVoid) {
			Log.debugMessage("SchemeElement.getParentSchemeId() | Parent Scheme was requested, while parent is a SchemeElement; returning null",
					FINE);
		}
		return parentSchemeId1;
	}

	Identifier getParentSchemeElementId() {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid(); 
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("SchemeElement.getParentSchemeElementId() | Parent SchemeElement was requested, while parent is a Scheme; returnung null",
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

	/**
	 * @return <em>the first</em> <code>Scheme</code> inner to this
	 *         <code>SchemeElement</code>, or <code>null</code> if
	 *         none.
	 */
	public Scheme getScheme() {
		for (final Scheme scheme : this.getSchemes()) {
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
			Log.debugException(ae, SEVERE);
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

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeDevice> getSchemeDevices() {
		try {
			return Collections.unmodifiableSet(this.getSchemeDevices0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeDevice> getSchemeDevices0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEDEVICE_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeElement> getSchemeElements() {
		try {
			return Collections.unmodifiableSet(this.getSchemeElements0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeElement> getSchemeElements0() throws ApplicationException {
		return this.kind == SCHEME_ELEMENT_CONTAINER
				? StorableObjectPool.<SchemeElement>getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEELEMENT_CODE), true)
				: Collections.<SchemeElement>emptySet();
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeLink> getSchemeLinks() {
		try {
			return Collections.unmodifiableSet(this.getSchemeLinks0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<SchemeLink> getSchemeLinks0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMELINK_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<Scheme> getSchemes() {
		try {
			return Collections.unmodifiableSet(this.getSchemes0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Set<Scheme> getSchemes0() throws ApplicationException {
		return this.kind == SCHEME_CONTAINER
				? StorableObjectPool.<Scheme>getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEME_CODE), true)
				: Collections.<Scheme>emptySet();
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
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
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
				this.kind,
				this.getEquipmentTypeId().getTransferable(),
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
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	public XmlSchemeElement getXmlTransferable(final String importType) {
		throw new UnsupportedOperationException();
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
			Log.debugException(ae, SEVERE);
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
	 * The <code>Scheme</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param scheme
	 */
	public void removeScheme(final Scheme scheme) {
		assert scheme != null: NON_NULL_EXPECTED;
		assert scheme.getParentSchemeElementId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		scheme.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeDevice</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeDevice
	 */
	public void removeSchemeDevice(final SchemeDevice schemeDevice) {
		assert schemeDevice != null: NON_NULL_EXPECTED;
		assert schemeDevice.getParentSchemeElementId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeDevice.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeElement</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeElement
	 */
	public void removeSchemeElement(final SchemeElement schemeElement) {
		assert schemeElement != null: NON_NULL_EXPECTED;
		assert schemeElement.getParentSchemeElementId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeElement.setParentSchemeElement(null);
	}

	/**
	 * The <code>SchemeLink</code> must belong to this
	 * <code>SchemeElement</code>, or crap will meet the fan.
	 *
	 * @param schemeLink
	 */
	public void removeSchemeLink(final SchemeLink schemeLink) {
		assert schemeLink != null: NON_NULL_EXPECTED;
		assert schemeLink.getParentSchemeElementId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeLink.setParentSchemeElement(null);
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
	 * @param equipmentTypeId
	 * @param equipmentId
	 * @param kisId
	 * @param siteNodeId
	 * @param symbolId
	 * @param ugoCellId
	 * @param schemeCellId
	 * @param parentSchemeId
	 * @param parentSchemeElementId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final IdlSchemeElementKind kind,
			final String name,
			final String description,
			final String label,
			final Identifier equipmentTypeId,
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

		assert equipmentTypeId != null : NON_NULL_EXPECTED;
		assert equipmentId != null : NON_NULL_EXPECTED;
		assert kind == SCHEME_ELEMENT_CONTAINER
				? (equipmentTypeId.isVoid() ^ equipmentId.isVoid())
				: (equipmentTypeId.isVoid() && equipmentId.isVoid()) : OBJECT_BADLY_INITIALIZED;
		
		assert kisId != null : NON_NULL_EXPECTED;
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert symbolId != null : NON_NULL_EXPECTED;
		assert ugoCellId != null : NON_NULL_EXPECTED;
		assert schemeCellId != null : NON_NULL_EXPECTED;

		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() ^ parentSchemeElementId.isVoid();

		this.kind = kind;
		this.label = label;
		this.equipmentTypeId = equipmentTypeId;
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;

		this.equipmentTypeSet = true;
	}

	/**
	 * @param equipment
	 */
	public void setEquipment(final Equipment equipment) {
		assert this.assertEquipmentTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		final Identifier newEquipmentId = Identifier.possiblyVoid(equipment);
		if (this.equipmentId.equals(newEquipmentId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (this.equipmentId.isVoid())
			/*
			 * Erasing old object-type value, setting new object
			 * value.
			 */
			this.equipmentTypeId = VOID_IDENTIFIER;
		else if (newEquipmentId.isVoid())
			/*
			 * Erasing old object value, preserving old object-type
			 * value. This point is not assumed to be reached unless
			 * initial object value has already been set (i. e.
			 * there already is object-type value to preserve).
			 */
			this.equipmentTypeId = this.getEquipment().getType().getId();
		this.equipmentId = newEquipmentId;
		super.markAsChanged();
	}

	/**
	 * @param equipmentType
	 */
	public void setEquipmentType(final EquipmentType equipmentType) {
		assert this.assertEquipmentTypeSetNonStrict(): OBJECT_BADLY_INITIALIZED;
		assert equipmentType != null: NON_NULL_EXPECTED;

		if (!this.equipmentId.isVoid())
			this.getEquipment().setType(equipmentType);
		else {
			final Identifier newEquipmentTypeId = equipmentType.getId();
			if (this.equipmentTypeId.equals(newEquipmentTypeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			this.equipmentTypeId = newEquipmentTypeId;
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
	 * @see AbstractSchemeElement#setParentScheme(Scheme)
	 */
	@Override
	public void setParentScheme(final Scheme parentScheme) {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		if (!super.parentSchemeId.isVoid())
			/*
			 * Moving from a scheme to another scheme.
			 */
			super.setParentScheme(parentScheme);
		else {
			/*
			 * Moving from a scheme element to a scheme.
			 */
			if (parentScheme == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			super.parentSchemeId = parentScheme.getId();
			this.parentSchemeElementId = VOID_IDENTIFIER;
			super.markAsChanged();
		}
	}

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert super.parentSchemeId != null && this.parentSchemeElementId != null: OBJECT_NOT_INITIALIZED;
		assert super.parentSchemeId.isVoid() ^ this.parentSchemeElementId.isVoid(): EXACTLY_ONE_PARENT_REQUIRED;

		Identifier newParentSchemeElementId;
		if (!super.parentSchemeId.isVoid()) {
			/*
			 * Moving from a scheme to a scheme element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			super.parentSchemeId = VOID_IDENTIFIER;
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
			if (this.parentSchemeElementId.equals(newParentSchemeElementId))
				return;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	public void setScheme(final Scheme scheme) throws ApplicationException {
		this.setSchemes(scheme == null
				? Collections.<Scheme>emptySet()
				: Collections.singleton(scheme));
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

	public void setSchemeDevices(final Set<SchemeDevice> schemeDevices) throws ApplicationException {
		assert schemeDevices != null: NON_NULL_EXPECTED;
		final Set<SchemeDevice> oldSchemeDevices = this.getSchemeDevices0();
		/*
		 * Check is made to prevent SchemeDevices from
		 * permanently losing their parents.
		 */
		oldSchemeDevices.removeAll(schemeDevices);
		for (final SchemeDevice oldSchemeDevice : oldSchemeDevices) {
			this.removeSchemeDevice(oldSchemeDevice);
		}
		for (final SchemeDevice schemeDevice : schemeDevices) {
			this.addSchemeDevice(schemeDevice);
		}
	}

	public void setSchemeElements(final Set<SchemeElement> schemeElements) throws ApplicationException {
		assert schemeElements != null: NON_NULL_EXPECTED;
		final Set<SchemeElement> oldSchemeElements = this.getSchemeElements0();
		/*
		 * Check is made to prevent SchemeElements from
		 * permanently losing their parents.
		 */
		oldSchemeElements.removeAll(schemeElements);
		for (final SchemeElement oldSchemeElement : oldSchemeElements) {
			this.removeSchemeElement(oldSchemeElement);
		}
		for (final SchemeElement schemeElement : schemeElements) {
			this.addSchemeElement(schemeElement);
		}
	}

	public void setSchemeLinks(final Set<SchemeLink> schemeLinks) throws ApplicationException {
		assert schemeLinks != null: NON_NULL_EXPECTED;
		final Set<SchemeLink> oldSchemeLinks = this.getSchemeLinks0();
		/*
		 * Check is made to prevent SchemeLinks from
		 * permanently losing their parents.
		 */
		oldSchemeLinks.removeAll(schemeLinks);
		for (final SchemeLink oldSchemeLink : oldSchemeLinks) {
			this.removeSchemeLink(oldSchemeLink);
		}
		for (final SchemeLink schemeLink : schemeLinks) {
			this.addSchemeLink(schemeLink);
		}
	}

	/**
	 * @param schemes
	 * @throws ApplicationException 
	 * @see Scheme#setSchemeElements(Set)
	 * @todo Check for circular dependencies.
	 */
	public void setSchemes(final Set<Scheme> schemes) throws ApplicationException {
		assert schemes != null: NON_NULL_EXPECTED;
		final Set<Scheme> oldSchemes = this.getSchemes0();
		oldSchemes.removeAll(schemes);
		for (final Scheme oldScheme : oldSchemes) {
			this.removeScheme(oldScheme);
		}
		for (final Scheme scheme : schemes) {
			this.addScheme(scheme);
		}
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
			this.kind = schemeElement.kind;
			this.equipmentTypeId = new Identifier(schemeElement.equipmentTypeId);
			this.equipmentId = new Identifier(schemeElement.equipmentId);
			this.kisId = new Identifier(schemeElement.kisId);
			this.siteNodeId = new Identifier(schemeElement.siteNodeId);
			this.symbolId = new Identifier(schemeElement.symbolId);
			this.ugoCellId = new Identifier(schemeElement.ugoCellId);
			this.schemeCellId = new Identifier(schemeElement.schemeCellId);
			this.parentSchemeElementId = new Identifier(schemeElement.parentSchemeElementId);
	
			this.equipmentTypeSet = true;
		}
	}

	/**
	 * @param schemeElement
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeElement schemeElement,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeElement, SCHEMEELEMENT_CODE, importType);

		super.fromXmlTransferable(schemeElement, importType);

		this.label = schemeElement.isSetLabel()
				? schemeElement.getLabel()
				: "";
		this.kind = IdlSchemeElementKind.from_int(schemeElement.getKind().intValue() - 1);

		final boolean setEquipmentTypeId = schemeElement.isSetEquipmentTypeId();
		final boolean setEquipmentId = schemeElement.isSetEquipmentId();		
		if (setEquipmentTypeId) {
			assert !setEquipmentId : OBJECT_STATE_ILLEGAL;

			this.equipmentTypeId = Identifier.fromXmlTransferable(schemeElement.getEquipmentTypeId(), EQUIPMENT_TYPE_CODE, importType);
			this.equipmentId = VOID_IDENTIFIER;
		} else if (setEquipmentId) {
			assert !setEquipmentTypeId : OBJECT_STATE_ILLEGAL;

			this.equipmentTypeId = VOID_IDENTIFIER;
			this.equipmentId = Identifier.fromXmlTransferable(schemeElement.getEquipmentId(), EQUIPMENT_CODE, importType);
		} else {
			throw new UpdateObjectException(
					"SchemeElement.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		this.kisId = schemeElement.isSetKisId()
				? Identifier.fromXmlTransferable(schemeElement.getKisId(), KIS_CODE, importType)
				: VOID_IDENTIFIER;
		this.siteNodeId = schemeElement.isSetSiteNodeId()
				? Identifier.fromXmlTransferable(schemeElement.getSiteNodeId(), SITENODE_CODE, importType)
				: VOID_IDENTIFIER;
		this.symbolId = schemeElement.isSetSymbolId()
				? Identifier.fromXmlTransferable(schemeElement.getSymbolId(), IMAGERESOURCE_CODE, importType)
				: VOID_IDENTIFIER;
		this.ugoCellId = schemeElement.isSetUgoCellId()
				? Identifier.fromXmlTransferable(schemeElement.getUgoCellId(), IMAGERESOURCE_CODE, importType)
				: VOID_IDENTIFIER;
		this.schemeCellId = schemeElement.isSetSchemeCellId()
				? Identifier.fromXmlTransferable(schemeElement.getSchemeCellId(), IMAGERESOURCE_CODE, importType)
				: VOID_IDENTIFIER;
		
		final boolean setParentSchemeId = schemeElement.isSetParentSchemeId();
		final boolean setParentSchemeElementId = schemeElement.isSetParentSchemeElementId();
		if (setParentSchemeId) {
			assert !setParentSchemeElementId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeId = Identifier.fromXmlTransferable(schemeElement.getParentSchemeId(), SCHEME_CODE, importType);
			this.parentSchemeElementId = VOID_IDENTIFIER;
		} else if (setParentSchemeElementId) {
			assert !setParentSchemeId : OBJECT_STATE_ILLEGAL;

			this.parentSchemeId = VOID_IDENTIFIER;
			this.parentSchemeElementId = Identifier.fromXmlTransferable(schemeElement.getParentSchemeElementId(), SCHEMEELEMENT_CODE, importType);
		} else {
			throw new UpdateObjectException(
					"SchemeElement.fromXmlTransferable() | "
					+ XML_BEAN_NOT_COMPLETE);
		}

		if (schemeElement.isSetSchemes()) {
			for (final XmlScheme scheme : schemeElement.getSchemes().getSchemeArray()) {
				Scheme.createInstance(super.creatorId, scheme, importType);
			}
		}
		if (schemeElement.isSetSchemeElements()) {
			for (final XmlSchemeElement schemeElement2 : schemeElement.getSchemeElements().getSchemeElementArray()) {
				SchemeElement.createInstance(super.creatorId, schemeElement2, importType);
			}
		}
		if (schemeElement.isSetSchemeDevices()) {
			for (final XmlSchemeDevice schemeDevice : schemeElement.getSchemeDevices().getSchemeDeviceArray()) {
				SchemeDevice.createInstance(super.creatorId, schemeDevice, importType);
			}
		}
		if (schemeElement.isSetSchemeLinks()) {
			for (final XmlSchemeLink schemeLink : schemeElement.getSchemeLinks().getSchemeLinkArray()) {
				SchemeLink.createInstance(super.creatorId, schemeLink, importType);
			}
		}

		this.equipmentTypeSet = true;
	}

	public IdlSchemeElementKind getKind() {
		assert this.kind != null : NON_NULL_EXPECTED;
		return this.kind;
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertEquipmentTypeSetNonStrict() {
		if (this.equipmentTypeSet)
			return this.assertEquipmentTypeSetStrict();
		this.equipmentTypeSet = true;
		return this.equipmentId != null
				&& this.equipmentTypeId != null
				&& this.equipmentId.isVoid()
				&& this.equipmentTypeId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertEquipmentTypeSetStrict() {
		return this.equipmentId != null
				&& this.equipmentTypeId != null
				&& (this.equipmentId.isVoid() ^ this.equipmentTypeId.isVoid());
	}

	/**
	 * Invoked just upon creation of a new instance from
	 * {@link #createInstance(Identifier, SchemeProtoElement, Scheme)} and
	 * {@link #createInstance(Identifier, SchemeProtoElement, SchemeElement)}.
	 *
	 * Actions taken are similar to those in {@link SchemeProtoElement#clone()}.
	 *
	 * @param schemeProtoElement
	 * @throws ApplicationException
	 * @see SchemeProtoElement#clone()
	 */
	private void fillProperties(final SchemeProtoElement schemeProtoElement, Identifier creatorId)
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
			for (final Characteristic characteristic : schemeProtoElement.getCharacteristics(true)) {
				final Characteristic characteristicClone = characteristic.clone();
				super.clonedIdMap.putAll(characteristicClone.getClonedIdMap());
				characteristicClone.setCharacterizableId(super.id);
			}
			for (final SchemeDevice schemeDevice : schemeProtoElement.getSchemeDevices0()) {
				final SchemeDevice schemeDeviceClone = schemeDevice.clone();
				super.clonedIdMap.putAll(schemeDeviceClone.getClonedIdMap());
				this.addSchemeDevice(schemeDeviceClone);
			}
			for (final SchemeLink schemeLink : schemeProtoElement.getSchemeLinks0()) {
				final SchemeLink schemeLinkClone = schemeLink.clone();
				super.clonedIdMap.putAll(schemeLinkClone.getClonedIdMap());
				this.addSchemeLink(schemeLinkClone);
			}
			
			for (SchemeProtoElement proto : schemeProtoElement.getSchemeProtoElements()) {
				final SchemeElement schemeElement = SchemeElement.createInstance(creatorId, proto, this);
				super.clonedIdMap.putAll(schemeElement.getClonedIdMap());
				this.addSchemeElement(schemeElement);
			}
			/*-
			 * Port references remapping.
			 */
			for (final SchemeLink schemeLink : this.getSchemeLinks0()) {
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
	 * @throws ApplicationException
	 */
	public Set<SchemePort> getSchemePortsRecursively()
	throws ApplicationException {
		final Set<SchemePort> schemePorts = new HashSet<SchemePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
			schemePorts.addAll(schemeDevice.getSchemePorts0());
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0()) {
			schemePorts.addAll(schemeElement.getSchemePortsRecursively());
		}
		return Collections.unmodifiableSet(schemePorts);
	}

	/**
	 * @throws ApplicationException
	 */
	public Set<SchemeCablePort> getSchemeCablePortsRecursively()
	throws ApplicationException {
		final Set<SchemeCablePort> schemeCablePorts = new HashSet<SchemeCablePort>();
		for (final SchemeDevice schemeDevice : this.getSchemeDevices0()) {
			schemeCablePorts.addAll(schemeDevice.getSchemeCablePorts0());
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0()) {
			schemeCablePorts.addAll(schemeElement.getSchemeCablePortsRecursively());
		}
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	/**
	 * @throws ApplicationException
	 */
	public Set<SchemeLink> getSchemeLinksRecursively()
	throws ApplicationException {
		final Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
		schemeLinks.addAll(this.getSchemeLinks0());
		for (final Scheme scheme : this.getSchemes0()) {
			schemeLinks.addAll(scheme.getSchemeLinksRecursively());
		}
		return Collections.unmodifiableSet(schemeLinks);
	}

	/**
	 * @throws ApplicationException
	 */
	public Set<SchemeCableLink> getSchemeCableLinksRecursively()
	throws ApplicationException {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
		for (final Scheme scheme : this.getSchemes0()) {
			schemeCableLinks.addAll(scheme.getSchemeCableLinksRecursively());
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

	void setEquipmentTypeId(Identifier equipmentTypeId) {
//		TODO: inroduce additional sanity checks
		assert equipmentTypeId != null : NON_NULL_EXPECTED;
		assert equipmentTypeId.isVoid() || equipmentTypeId.getMajor() == EQUIPMENT_TYPE_CODE;
		this.equipmentTypeId = equipmentTypeId;
		super.markAsChanged();
	}

	void setSiteNodeId(Identifier siteNodeId) {
//		TODO: inroduce additional sanity checks
		assert siteNodeId != null : NON_NULL_EXPECTED;
		assert siteNodeId.isVoid() || siteNodeId.getMajor() == SITENODE_CODE;
		this.siteNodeId = siteNodeId;
		super.markAsChanged();
	}

	void setParentSchemeElementId(Identifier parentSchemeElementId) {
//		TODO: inroduce additional sanity checks
		assert parentSchemeElementId != null : NON_NULL_EXPECTED;
		assert parentSchemeElementId.isVoid() || parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		this.parentSchemeElementId = parentSchemeElementId;
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
	Set<SchemeElement> getChildSchemeElementsRecursively()
	throws ApplicationException {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		final Set<Scheme> schemes = this.getSchemes0();
		if (schemes.isEmpty()) {
			for (final SchemeElement schemeElement : this.getSchemeElements0()) {
				schemeElements.addAll(schemeElement.getChildSchemeElementsRecursively());
				schemeElements.add(schemeElement);
			}
		} else {
			for (final Scheme scheme : schemes) {
				schemeElements.addAll(scheme.getTopLevelSchemeElementsRecursively());
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
	public boolean containsSchemeLink(final Identifier schemeLinkId)
	throws ApplicationException {
		if (this.getSchemeLinks0().contains(schemeLinkId)) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0()) {
			if (schemeElement.getSchemes0().isEmpty()
					&& schemeElement.containsSchemeLink(schemeLinkId)) {
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
	public boolean containsSchemeElement(final SchemeElement schemeElement)
	throws ApplicationException {
		if (schemeElement.getParentSchemeElementId().equals(this)) {
			return true;
		}
		final Set<Scheme> schemes = this.getSchemes0();
		if (schemes.isEmpty()) {
			for (final SchemeElement schemeElement1 : this.getSchemeElements0()) {
				final Set<Scheme> schemes1 = schemeElement1.getSchemes0();
				if (schemes1.isEmpty()) {
					if (schemeElement1.containsSchemeElement(schemeElement)) {
						return true;
					}
				} else {
					for (final Scheme scheme : schemes1) {
						if (scheme.containsSchemeElement(schemeElement)) {
							return true;
						}
					}
				}
			}
		} else {
			for (final Scheme scheme : schemes) {
				if (scheme.containsSchemeElement(schemeElement)) {
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
	public boolean containsAbstractSchemePort(final AbstractSchemePort abstractSchemePort)
	throws ApplicationException {
		if (this.getSchemeDevices0().contains(abstractSchemePort.getParentSchemeDeviceId())) {
			return true;
		}
		for (final SchemeElement schemeElement : this.getSchemeElements0()) {
			if (schemeElement.getSchemes0().isEmpty()
					&& schemeElement.containsAbstractSchemePort(abstractSchemePort)) {
				return true;
			}
		}
		return false;
	}
}
