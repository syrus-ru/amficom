/*-
 * $Id: SchemeElement.java,v 1.56 2005/07/24 17:10:19 bass Exp $
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
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementHelper;
import com.syrus.util.Log;

/**
 * #04 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.56 $, $Date: 2005/07/24 17:10:19 $
 * @module scheme
 */
public final class SchemeElement extends AbstractSchemeElement implements
		SchemeCellContainer {
	private static final long serialVersionUID = 3618977875802797368L;

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
	SchemeElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final EquipmentType equipmentType,
			final Equipment equipment, final KIS kis,
			final SiteNode siteNode, final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version,
				name, description, parentScheme);
		this.label = label;

		assert equipmentType == null || equipment == null;
		this.equipmentTypeId = Identifier.possiblyVoid(equipmentType);
		this.equipmentId = Identifier.possiblyVoid(equipment);

		this.kisId = Identifier.possiblyVoid(kis);
		this.siteNodeId = Identifier.possiblyVoid(siteNode);
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.ugoCellId = Identifier.possiblyVoid(ugoCell);
		this.schemeCellId = Identifier.possiblyVoid(schemeCell);

		assert parentScheme == null || parentSchemeElement == null: EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeElement(final IdlSchemeElement transferable) throws CreateObjectException {
		fromTransferable(transferable);
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
	public static SchemeElement createInstance(final Identifier creatorId, final String name,
			final String description, final String label,
			final EquipmentType equipmentType,
			final Equipment equipment,
			final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert label != null: NON_NULL_EXPECTED;
		assert parentScheme != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label, equipmentType, equipment, kis, siteNode, symbol, ugoCell, schemeCell, parentScheme, null);
			schemeElement.markAsChanged();
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ige);
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
			final String name, final String description,
			final String label, final EquipmentType equipmentType,
			final Equipment equipment, final KIS kis,
			final SiteNode siteNode,
			final BitmapImageResource symbol,
			final SchemeImageResource ugoCell,
			final SchemeImageResource schemeCell,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert label != null: NON_NULL_EXPECTED;
		assert parentSchemeElement != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeElement schemeElement = new SchemeElement(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMEELEMENT_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, label, equipmentType, equipment, kis, siteNode, symbol, ugoCell, schemeCell, null, parentSchemeElement);
			schemeElement.markAsChanged();
			if (equipment != null || equipmentType != null)
				schemeElement.equipmentTypeSet = true;
			return schemeElement;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeElement.createInstance | cannot generate identifier ", ige);
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
		final SchemeElement schemeElement = (SchemeElement) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeElement;
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

	Identifier getEquipmentId() {
		assert this.assertEquipmentTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		assert this.equipmentId.isVoid() || this.equipmentId.getMajor() == EQUIPMENT_CODE;
		return this.equipmentId;
	}
	
	/**
	 * A wrapper around {@link #getEquipmentId()}.
	 */
	public Equipment getEquipment() {
		try {
			return (Equipment) StorableObjectPool.getStorableObject(this.getEquipmentId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getEquipmentTypeId() {
		assert this.assertEquipmentTypeSetStrict(): OBJECT_BADLY_INITIALIZED;
		assert this.equipmentTypeId.isVoid() || this.equipmentTypeId.getMajor() == EQUIPMENT_TYPE_CODE;
		return this.equipmentTypeId;
	}

	/**
	 * A wrapper around {@link #getEquipmentTypeId()}. 
	 */
	public EquipmentType getEquipmentType() {
		try {
			return this.getEquipmentId().isVoid()
					? (EquipmentType) StorableObjectPool.getStorableObject(this.getEquipmentTypeId(), true)
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
			return (KIS) StorableObjectPool.getStorableObject(this.getKisId(), true);
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
			return (SchemeElement) StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
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
	 * A wrapper around {@link #getSchemeCellId()}.
	 *
	 * @see SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		try {
			return (SchemeImageResource) StorableObjectPool.getStorableObject(this.getSchemeCellId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeDevice> getSchemeDevices() {
		return Collections.unmodifiableSet(this.getSchemeDevices0());
	}

	private Set<SchemeDevice> getSchemeDevices0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEDEVICE_CODE), true, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeElement> getSchemeElements() {
		return Collections.unmodifiableSet(this.getSchemeElements0());
	}

	private Set<SchemeElement> getSchemeElements0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEELEMENT_CODE), true, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeLink> getSchemeLinks() {
		return Collections.unmodifiableSet(this.getSchemeLinks0());
	}

	private Set<SchemeLink> getSchemeLinks0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMELINK_CODE), true, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<Scheme> getSchemes() {
		try {
			final Set<Scheme> schemes = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEME_CODE), true, true);
			return Collections.unmodifiableSet(schemes);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
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
			return (SiteNode) StorableObjectPool.getStorableObject(this.getSiteNodeId(), true);
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
			return (BitmapImageResource) StorableObjectPool.getStorableObject(this.getSymbolId(), true);
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
				this.version,
				super.getName(),
				super.getDescription(),
				this.label,
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

	Identifier getUgoCellId() {
		assert this.ugoCellId != null: OBJECT_NOT_INITIALIZED;
		assert this.ugoCellId.isVoid() || this.ugoCellId.getMajor() == IMAGERESOURCE_CODE;
		return this.ugoCellId;
	}

	/**
	 * A wrapper around {@link #getUgoCellId()}.
	 *
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		try {
			return (SchemeImageResource) StorableObjectPool.getStorableObject(this.getUgoCellId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
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
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final String label, final Identifier equipmentTypeId,
			final Identifier equipmentId, final Identifier kisId,
			final Identifier siteNodeId,
			final Identifier symbolId, final Identifier ugoCellId,
			final Identifier schemeCellId,
			final Identifier parentSchemeId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version, name, description, parentSchemeId);

		assert label != null: NON_NULL_EXPECTED;

		assert equipmentTypeId != null: NON_NULL_EXPECTED;
		assert equipmentId != null: NON_NULL_EXPECTED;
		assert equipmentTypeId.isVoid() ^ equipmentId.isVoid();

		assert kisId != null: NON_NULL_EXPECTED;
		assert siteNodeId != null: NON_NULL_EXPECTED;
		assert symbolId != null: NON_NULL_EXPECTED;
		assert ugoCellId != null: NON_NULL_EXPECTED;
		assert schemeCellId != null: NON_NULL_EXPECTED;
		
		assert parentSchemeElementId != null: NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() ^ parentSchemeElementId.isVoid();
		
		this.label = label;
		this.equipmentTypeId = equipmentTypeId;
		this.equipmentId = equipmentId;
		this.kisId = kisId;
		this.siteNodeId = siteNodeId;
		this.symbolId = symbolId;
		this.ugoCellId = ugoCellId;
		this.schemeCellId = schemeCellId;
		this.parentSchemeElementId = parentSchemeElementId;
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

	public void setScheme(final Scheme scheme) {
		Set<Scheme> schemes;
		if (scheme == null) {
			schemes = Collections.emptySet();
		} else {
			schemes = Collections.singleton(scheme);
		}
		setSchemes(schemes);
	}

	/**
	 * @param schemeCell
	 * @see SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(final SchemeImageResource schemeCell) {
		final Identifier newSchemeCellId = Identifier.possiblyVoid(schemeCell);
		if (this.schemeCellId.equals(newSchemeCellId))
			return;
		this.schemeCellId = newSchemeCellId;
		super.markAsChanged();
	}

	public void setSchemeDevices(final Set<SchemeDevice> schemeDevices) {
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

	public void setSchemeElements(final Set<SchemeElement> schemeElements) {
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

	public void setSchemeLinks(final Set<SchemeLink> schemeLinks) {
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
	 * @see Scheme#setSchemeElements(Set)
	 * @todo Check for circular depsendencies.
	 */
	public void setSchemes(final Set<Scheme> schemes) {
		assert schemes != null: NON_NULL_EXPECTED;
		for (final Scheme oldScheme : getSchemes()) {
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
	 * @param symbol
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		final Identifier newSymbolId = Identifier.possiblyVoid(symbol);
		if (this.symbolId.equals(newSymbolId))
			return;
		this.symbolId = newSymbolId;
		super.markAsChanged();
	}

	/**
	 * @param ugoCell
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		final Identifier newUgoCellId = Identifier.possiblyVoid(ugoCell);
		if (this.ugoCellId.equals(newUgoCellId))
			return;
		this.ugoCellId = newUgoCellId;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeElement schemeElement = (IdlSchemeElement) transferable;
		super.fromTransferable(schemeElement, schemeElement.name,
				schemeElement.description,
				schemeElement.parentSchemeId);
		this.label = schemeElement.label;
		this.equipmentTypeId = new Identifier(schemeElement.equipmentTypeId);
		this.equipmentId = new Identifier(schemeElement.equipmentId);
		this.kisId = new Identifier(schemeElement.kisId);
		this.siteNodeId = new Identifier(schemeElement.siteNodeId);
		this.symbolId = new Identifier(schemeElement.symbolId);
		this.ugoCellId = new Identifier(schemeElement.ugoCellId);
		this.schemeCellId = new Identifier(schemeElement.schemeCellId);
		this.parentSchemeElementId = new Identifier(schemeElement.parentSchemeElementId);
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
	 * @see SchemeCellContainer#getIdMap()
	 * @todo Implement.
	 */
	public Map<Identifier, Identifier> getIdMap() {
		return Collections.emptyMap();
	}

	public Set<SchemeCablePort> getSchemeCablePorts() {
		final Set<SchemeCablePort> schemeCablePorts = new HashSet<SchemeCablePort>();
		for (final SchemeDevice schemeDevice : getSchemeDevices()) {
			schemeCablePorts.addAll(schemeDevice.getSchemeCablePorts());
		}
		return Collections.unmodifiableSet(schemeCablePorts);
	}

	public Set<SchemePort> getSchemePorts() {
		final Set<SchemePort> schemePorts = new HashSet<SchemePort>();
		for (final SchemeDevice schemeDevice : getSchemeDevices()) {
			schemePorts.addAll(schemeDevice.getSchemePorts());
		}
		return Collections.unmodifiableSet(schemePorts);
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
}
