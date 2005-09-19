/*-
 * $Id: SchemeProtoGroup.java,v 1.66 2005/09/19 12:34:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.IMAGERESOURCE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoGroupHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoElementSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroupSeq;
import com.syrus.util.Log;

/**
 * #01 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.66 $, $Date: 2005/09/19 12:34:35 $
 * @module scheme
 */
public final class SchemeProtoGroup extends StorableObject
		implements Describable, SchemeSymbolContainer,
		ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemeProtoGroup> {
	private static final long serialVersionUID = 3256721788422862901L;

	private String name;

	private String description;

	private Identifier symbolId;

	/**
	 * Used in a condition, so left package-visible.
	 */
	Identifier parentSchemeProtoGroupId;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param symbol
	 * @param parentSchemeProtoGroup
	 */
	SchemeProtoGroup(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final BitmapImageResource symbol,
			final SchemeProtoGroup parentSchemeProtoGroup) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.symbolId = Identifier.possiblyVoid(symbol);
		this.parentSchemeProtoGroupId = Identifier.possiblyVoid(parentSchemeProtoGroup);
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
	private SchemeProtoGroup(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, SCHEMEPROTOGROUP_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
	}

	/**
	 * @param transferable
	 */
	public SchemeProtoGroup(final IdlSchemeProtoGroup transferable) {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, BitmapImageResource, SchemeProtoGroup)}.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeProtoGroup createInstance(
			final Identifier creatorId, final String name)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", null, null);
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param symbol may be <code>null</code>.
	 * @param parentSchemeProtoGroup may be <code>null</code> (for a top-level group).
	 * @throws CreateObjectException
	 */
	public static SchemeProtoGroup createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final BitmapImageResource symbol,
			final SchemeProtoGroup parentSchemeProtoGroup) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeProtoGroup schemeProtoGroup = new SchemeProtoGroup(IdentifierPool.getGeneratedIdentifier(SCHEMEPROTOGROUP_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					symbol,
					parentSchemeProtoGroup);
			schemeProtoGroup.markAsChanged();
			return schemeProtoGroup;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeProtoGroup.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlSchemeProtoGroup
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static SchemeProtoGroup createInstance(
			final Identifier creatorId,
			final XmlSchemeProtoGroup xmlSchemeProtoGroup,
			final String importType)
	throws CreateObjectException {
		try {
			final XmlIdentifier xmlId = xmlSchemeProtoGroup.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			SchemeProtoGroup schemeProtoGroup;
			if (id.isVoid()) {
				schemeProtoGroup = new SchemeProtoGroup(xmlId,
						importType,
						created,
						creatorId);
			} else {
				schemeProtoGroup = StorableObjectPool.getStorableObject(id, true);
				if (schemeProtoGroup == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					schemeProtoGroup = new SchemeProtoGroup(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			schemeProtoGroup.fromXmlTransferable(xmlSchemeProtoGroup, importType);
			assert schemeProtoGroup.isValid() : OBJECT_BADLY_INITIALIZED;
			schemeProtoGroup.markAsChanged();
			return schemeProtoGroup;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param schemeProtoElement cannot be <code>null</code>.
	 */
	public void addSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		schemeProtoElement.setParentSchemeProtoGroup(this);
	}

	/**
	 * @param schemeProtoGroup can be neither <code>null</code> nor
	 *        <code>this</code>.
	 * @bug provide a check to disallow addition of higher-level objects as
	 *      children for lower-level ones (within the same hierarchy tree).
	 * @todo add sanity checks for my own id.
	 */
	public void addSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup) {
		assert schemeProtoGroup != null: NON_NULL_EXPECTED;
		assert schemeProtoGroup != this: CIRCULAR_DEPS_PROHIBITED;
		schemeProtoGroup.setParentSchemeProtoGroup(this);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.symbolId != null
				&& this.parentSchemeProtoGroupId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.symbolId);
		dependencies.add(this.parentSchemeProtoGroupId);
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
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeProtoGroups0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeProtoElements0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeProtoGroupId() {
		assert this.parentSchemeProtoGroupId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeProtoGroupId.isVoid() || this.parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;
		return this.parentSchemeProtoGroupId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoGroupId()}.
	 *
	 * @return <code>schemeProtoGroup</code> parent for this
	 *         <code>schemeProtoGroup</code>, or <code>null</code> if
	 *         none.
	 */
	public SchemeProtoGroup getParentSchemeProtoGroup() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeProtoGroupId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeProtoElement> getSchemeProtoElements() {
		try {
			return Collections.unmodifiableSet(this.getSchemeProtoElements0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeProtoElement> getSchemeProtoElements0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPROTOELEMENT_CODE), true);
	}

	/**
	 * @return an immutable set.
	 */
	public Set<SchemeProtoGroup> getSchemeProtoGroups() {
		try {
			return Collections.unmodifiableSet(this.getSchemeProtoGroups0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeProtoGroup> getSchemeProtoGroups0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPROTOGROUP_CODE), true);
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
	public IdlSchemeProtoGroup getTransferable(final ORB orb) {
		return IdlSchemeProtoGroupHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.symbolId.getTransferable(),
				this.parentSchemeProtoGroupId.getTransferable());
	}

	/**
	 * @param schemeProtoGroup
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public XmlSchemeProtoGroup getXmlTransferable(
			final XmlSchemeProtoGroup schemeProtoGroup,
			final String importType)
	throws ApplicationException {
		super.id.getXmlTransferable(schemeProtoGroup.addNewId(), importType);
		schemeProtoGroup.setName(this.name);
		if (schemeProtoGroup.isSetDescription()) {
			schemeProtoGroup.unsetDescription();
		}
		if (this.description.length() != 0) {
			schemeProtoGroup.setDescription(this.description);
		}
		if (schemeProtoGroup.isSetSymbolId()) {
			schemeProtoGroup.unsetSymbolId();
		}
		if (!this.symbolId.isVoid()) {
			this.symbolId.getXmlTransferable(schemeProtoGroup.addNewSymbolId(), importType);
		}
		if (schemeProtoGroup.isSetParentSchemeProtoGroupId()) {
			schemeProtoGroup.unsetParentSchemeProtoGroupId();
		}
		if (!this.parentSchemeProtoGroupId.isVoid()) {
			this.parentSchemeProtoGroupId.getXmlTransferable(schemeProtoGroup.addNewParentSchemeProtoGroupId(), importType);
		}
		if (schemeProtoGroup.isSetSchemeProtoGroups()) {
			schemeProtoGroup.unsetSchemeProtoGroups();
		}
		final Set<SchemeProtoGroup> schemeProtoGroups = this.getSchemeProtoGroups0();
		if (!schemeProtoGroups.isEmpty()) {
			final XmlSchemeProtoGroupSeq schemeProtoGroupSeq = schemeProtoGroup.addNewSchemeProtoGroups();
			for (final SchemeProtoGroup schemeProtoGroup2 : schemeProtoGroups) {
				schemeProtoGroup2.getXmlTransferable(schemeProtoGroupSeq.addNewSchemeProtoGroup(), importType);
			}
		}
		if (schemeProtoGroup.isSetSchemeProtoElements()) {
			schemeProtoGroup.unsetSchemeProtoElements();
		}
		final Set<SchemeProtoElement> schemeProtoElements = this.getSchemeProtoElements0();
		if (!schemeProtoElements.isEmpty()) {
			final XmlSchemeProtoElementSeq schemeProtoElementSeq = schemeProtoGroup.addNewSchemeProtoElements();
			for (final SchemeProtoElement schemeProtoElement : schemeProtoElements) {
				schemeProtoElement.getXmlTransferable(schemeProtoElementSeq.addNewSchemeProtoElement(), importType);
			}
		}
		return schemeProtoGroup;
	}

	/**
	 * The <code>SchemeProtoElement</code> must belong to this
	 * <code>SchemeProtoGroup</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoElement
	 * @todo Decide how removal should be interpreted: setting a parent of
	 *       <code>null</code> (which is impossible for a
	 *       <code>schemeProtoElement</code>) or physical removal (which is
	 *       done so far).
	 */
	public void removeSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: NON_NULL_EXPECTED;
		assert schemeProtoElement.getParentSchemeProtoGroupId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeProtoElement.setParentSchemeProtoGroup(null);
	}

	/**
	 * The <code>SchemeProtoGroup</code> must belong to this
	 * <code>SchemeProtoGroup</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoGroup
	 * @todo Decide whether it's good to have more than one top-level
	 *       <code>schemeProtoGroup</code>.
	 */
	public void removeSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup) {
		assert schemeProtoGroup != null: NON_NULL_EXPECTED;
		assert schemeProtoGroup.getParentSchemeProtoGroupId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeProtoGroup.setParentSchemeProtoGroup(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name can be neither <code>null</code> nor an empty string.
	 * @param description cannot be <code>null</code>. For this purpose,
	 *        supply an empty string as an argument.
	 * @param symbolId cannot be <code>null</code>. For this purpose,
	 *        supply {@link Identifier#VOID_IDENTIFIER} as an argument.
	 * @param parentSchemeProtoGroupId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier symbolId,
			final Identifier parentSchemeProtoGroupId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert symbolId != null : NON_NULL_EXPECTED;
		assert parentSchemeProtoGroupId != null : NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.symbolId = symbolId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		assert description != null: NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0: OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param parentSchemeProtoGroupId
	 */
	void setParentSchemeProtoGroupId(final Identifier parentSchemeProtoGroupId) {
		assert !parentSchemeProtoGroupId.equals(this) : CIRCULAR_DEPS_PROHIBITED;
		assert parentSchemeProtoGroupId.isVoid() || parentSchemeProtoGroupId.getMajor() == SCHEMEPROTOGROUP_CODE;
		if (this.parentSchemeProtoGroupId.equals(parentSchemeProtoGroupId)) {
			return;
		}
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoGroupId()}
	 *
	 * @param parentSchemeProtoGroup
	 * @todo Check whether <code>parentSchemeProtoGroup</code> is not a
	 *       lower-level descendant of <code>this</code>.
	 */
	public void setParentSchemeProtoGroup(final SchemeProtoGroup parentSchemeProtoGroup) {
		this.setParentSchemeProtoGroupId(Identifier.possiblyVoid(parentSchemeProtoGroup));
	}

	/**
	 * To make a slight alteration of <code>schemeProtoElements</code> for
	 * this <code>schemeProtoGroup</code>, use
	 * {@link #addSchemeProtoElement(SchemeProtoElement)} and/or
	 * {@link #removeSchemeProtoElement(SchemeProtoElement)}. This method
	 * will completely overwrite old <code>schemeProtoElements</code> with
	 * the new ones (i. e. remove old and add new ones). Since
	 * <em>removal</em> of a <code>schemeProtoElement</code> means its
	 * <em>physical removal</em>, the collection of new ones <em>must
	 * not</em> contain any <code>schemeProtoElement</code> from old ones,
	 * or crap will meet the fan.
	 *
	 * @param schemeProtoElements
	 * @throws ApplicationException 
	 */
	public void setSchemeProtoElements(final Set<SchemeProtoElement> schemeProtoElements) throws ApplicationException {
		assert schemeProtoElements != null: NON_NULL_EXPECTED;
		final Set<SchemeProtoElement> oldSchemeProtoElements = this.getSchemeProtoElements0();
		/*
		 * Check is made to prevent SchemeProtoElements from
		 * permanently losing their parents.
		 */
		oldSchemeProtoElements.removeAll(schemeProtoElements);
		for (final SchemeProtoElement oldSchemeProtoElement : oldSchemeProtoElements) {
			this.removeSchemeProtoElement(oldSchemeProtoElement);
		}
		for (final SchemeProtoElement schemeProtoElement : schemeProtoElements) {
			this.addSchemeProtoElement(schemeProtoElement);
		}
	}

	/**
	 * To make a slight alteration of <code>schemeProtoGroups</code> for
	 * this <code>schemeProtoGroup</code>, use
	 * {@link #addSchemeProtoGroup(SchemeProtoGroup)} and/or
	 * {@link #removeSchemeProtoGroup(SchemeProtoGroup)}. This method
	 * will completely overwrite old <code>schemeProtoGroups</code> with
	 * the new ones (i. e. remove old and add new ones).
	 *
	 * @param schemeProtoGroups
	 * @throws ApplicationException 
	 */
	public void setSchemeProtoGroups(final Set<SchemeProtoGroup> schemeProtoGroups) throws ApplicationException {
		assert schemeProtoGroups != null: NON_NULL_EXPECTED;
		final Set<SchemeProtoGroup> oldSchemeProtoGroups = this.getSchemeProtoGroups0();
		oldSchemeProtoGroups.removeAll(schemeProtoGroups);
		for (final SchemeProtoGroup oldSchemeProtoGroup : this.getSchemeProtoGroups()) {
			this.removeSchemeProtoGroup(oldSchemeProtoGroup);
		}
		for (final SchemeProtoGroup schemeProtoGroup : schemeProtoGroups) {
			this.addSchemeProtoGroup(schemeProtoGroup);
		}
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
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		synchronized (this) {
			final IdlSchemeProtoGroup schemeProtoGroup = (IdlSchemeProtoGroup) transferable;
			try {
				super.fromTransferable(schemeProtoGroup);
			} catch (final ApplicationException ae) {
				/*
				 * Never.
				 */
				assert false;
			}
			this.name = schemeProtoGroup.name;
			this.description = schemeProtoGroup.description;
			this.symbolId = new Identifier(schemeProtoGroup.symbolId);
			this.parentSchemeProtoGroupId = new Identifier(schemeProtoGroup.parentSchemeProtoGroupId);
		}
	}

	/**
	 * @param schemeProtoGroup
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeProtoGroup schemeProtoGroup,
			final String importType)
	throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(schemeProtoGroup, SCHEMEPROTOGROUP_CODE, importType);

		this.name = schemeProtoGroup.getName();
		this.description = schemeProtoGroup.isSetDescription()
				? schemeProtoGroup.getDescription()
				: "";
		this.symbolId = schemeProtoGroup.isSetSymbolId()
				? Identifier.fromXmlTransferable(schemeProtoGroup.getSymbolId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		this.parentSchemeProtoGroupId = schemeProtoGroup.isSetParentSchemeProtoGroupId()
				? Identifier.fromXmlTransferable(schemeProtoGroup.getParentSchemeProtoGroupId(), importType, MODE_THROW_IF_ABSENT)
				: VOID_IDENTIFIER;
		if (schemeProtoGroup.isSetSchemeProtoGroups()) {
			for (final XmlSchemeProtoGroup schemeProtoGroup2 : schemeProtoGroup.getSchemeProtoGroups().getSchemeProtoGroupArray()) {
				createInstance(super.creatorId, schemeProtoGroup2, importType);
			}
		}
		if (schemeProtoGroup.isSetSchemeProtoElements()) {
			for (final XmlSchemeProtoElement schemeProtoElement : schemeProtoGroup.getSchemeProtoElements().getSchemeProtoElementArray()) {
				SchemeProtoElement.createInstance(super.creatorId, schemeProtoElement, importType);
			}
		}
	}
}
