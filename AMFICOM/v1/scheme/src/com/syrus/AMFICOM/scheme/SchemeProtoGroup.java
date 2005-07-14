/*-
 * $Id: SchemeProtoGroup.java,v 1.47 2005/07/14 19:25:47 bass Exp $
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
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.ErrorMessages.UNSUPPORTED_CHILD_TYPE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
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
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemListener;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeProtoGroupHelper;
import com.syrus.AMFICOM.scheme.logic.Library;
import com.syrus.AMFICOM.scheme.logic.LibraryEntry;
import com.syrus.util.Log;

/**
 * #01 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.47 $, $Date: 2005/07/14 19:25:47 $
 * @module scheme_v1
 * @todo Implement fireParentChanged() and call it on any setParent*() invocation.
 */
public final class SchemeProtoGroup extends AbstractCloneableStorableObject
		implements Describable, SchemeSymbolContainer, Library {
	private static final long serialVersionUID = 3256721788422862901L;

	private String name;

	private String description;

	private Identifier symbolId;

	/**
	 * Used in a condition, so left package-visible.
	 */
	Identifier parentSchemeProtoGroupId;

	private ArrayList<ItemListener> itemListeners = new ArrayList<ItemListener>();

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeProtoGroup(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(SCHEMEPROTOGROUP_CODE).retrieve(this);
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
	 * @param symbol
	 * @param parentSchemeProtoGroup
	 */
	SchemeProtoGroup(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
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
	public static SchemeProtoGroup createInstance(
			final Identifier creatorId, final String name,
			final String description,
			final BitmapImageResource symbol,
			final SchemeProtoGroup parentSchemeProtoGroup)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			final SchemeProtoGroup schemeProtoGroup = new SchemeProtoGroup(
					IdentifierPool.getGeneratedIdentifier(SCHEMEPROTOGROUP_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, symbol,
					parentSchemeProtoGroup);
			schemeProtoGroup.markAsChanged();
			return schemeProtoGroup;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeProtoGroup.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param itemListener
	 * @see Item#addChangeListener(ItemListener)
	 */
	public void addChangeListener(final ItemListener itemListener) {
		assert !this.itemListeners.contains(itemListener);
		this.itemListeners.add(0, itemListener);
	}

	/**
	 * @param childItem
	 * @throws UnsupportedOperationException if <code>childItem</code> is
	 *         an instance of neither {@link Library}nor
	 *         {@link LibraryEntry}.
	 * @see Item#addChild(Item)
	 */
	public void addChild(final Item childItem) {
		if (childItem instanceof Library)
			addChild((Library) childItem);
		else if (childItem instanceof LibraryEntry)
			addChild((LibraryEntry) childItem);
		else
			throw new UnsupportedOperationException(UNSUPPORTED_CHILD_TYPE);
	}

	/**
	 * @param library
	 * @see Library#addChild(Library)
	 */
	public void addChild(final Library library) {
		addSchemeProtoGroup((SchemeProtoGroup) library);
	}

	/**
	 * @param libraryEntry
	 * @see Library#addChild(LibraryEntry)
	 */
	public void addChild(final LibraryEntry libraryEntry) {
		addSchemeProtoElement((SchemeProtoElement) libraryEntry);
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
	 * @see Item#canHaveChildren()
	 */
	public boolean canHaveChildren() {
		return getMaxChildrenCount() != 0;
	}

	/**
	 * @see Item#canHaveParent()
	 */
	public boolean canHaveParent() {
		return true;
	}

	/**
	 * @see Object#clone()
	 * @bug If a parent <code>schemeProtoGroup</code> is <code>null</code>
	 *      for <em>this</em> object, that doesn't mean it'll be
	 *      <code>null</code> for its clone (since there can't be
	 *      <em>two</em> roots of hierarchy).
	 * @todo Decide whether it's necessary to clone child
	 *       <code>schemeProtoGroup</code>s and
	 *       <code>schemeProtoElement</code>s.
	 */
	@Override
	public SchemeProtoGroup clone() {
		final SchemeProtoGroup clone = (SchemeProtoGroup) super.clone();
		clone.name = this.name;
		clone.description = this.description;
		clone.symbolId = this.symbolId;
		clone.parentSchemeProtoGroupId = this.parentSchemeProtoGroupId;
		return clone;
	}

	/**
	 * @return a <em>mutable</em> <code>List</code> of
	 *         <code>SchemeProtoGroup</code>s and
	 *         <code>SchemeProtoElement</codes>s, which can be freely
	 *         modified. One of the drawbacks of this approach is that a new
	 *         object is created on every invocation. If you need an
	 *         <em>immutable</em> shared instance, let me know.
	 * @see Item#getChildren()
	 */
	public List<Item> getChildren() {
		final Set<SchemeProtoGroup> schemeProtoGroups = getSchemeProtoGroups();
		final Set<SchemeProtoElement> schemeProtoElements = getSchemeProtoElements();
		final List<Item> children = new ArrayList<Item>(schemeProtoGroups.size() + schemeProtoElements.size());
		children.addAll(schemeProtoGroups);
		children.addAll(schemeProtoElements);
		return children;
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
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Item#getMaxChildrenCount()
	 */
	public int getMaxChildrenCount() {
		return Integer.MAX_VALUE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @return <code>this</code>.
	 * @see Item#getObject()
	 */
	public Object getObject() {
		return this;
	}

	/**
	 * @see Item#getParent()
	 */
	public SchemeProtoGroup getParent() {
		return this.getParentSchemeProtoGroup();
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
			return (SchemeProtoGroup) StorableObjectPool.getStorableObject(this.getParentSchemeProtoGroupId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set<SchemeProtoElement> getSchemeProtoElements() {
		try {
			final Set<SchemeProtoElement> schemeProtoElements = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPROTOELEMENT_CODE), true, true);
			return Collections.unmodifiableSet(schemeProtoElements);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set<SchemeProtoGroup> getSchemeProtoGroups() {
		try {
			final Set<SchemeProtoGroup> schemeProtoGroups = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPROTOGROUP_CODE), true, true);
			return Collections.unmodifiableSet(schemeProtoGroups);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	Identifier getSymbolId() {
		assert this.symbolId != null: OBJECT_NOT_INITIALIZED;
		assert this.symbolId.isVoid() || this.symbolId.getMajor() == SCHEMEPROTOGROUP_CODE;
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
	public IdlSchemeProtoGroup getTransferable(final ORB orb) {
		return IdlSchemeProtoGroupHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version,
				this.name,
				this.description,
				this.symbolId.getTransferable(),
				this.parentSchemeProtoGroupId.getTransferable());
	}

	/**
	 * @see Item#isService()
	 */
	public boolean isService() {
		return false;
	}

	/**
	 * @param itemListener
	 * @see Item#removeChangeListener(ItemListener)
	 */
	public void removeChangeListener(final ItemListener itemListener) {
		assert this.itemListeners.contains(itemListener);
		this.itemListeners.remove(itemListener);
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
		assert schemeProtoElement.getParentSchemeProtoGroupId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
		assert schemeProtoGroup.getParentSchemeProtoGroupId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier symbolId,
			final Identifier parentSchemeProtoGroupId) {
		super.setAttributes(created, modified, creatorId, modifierId,
				version);

		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert symbolId != null: NON_NULL_EXPECTED;
		assert parentSchemeProtoGroupId != null: NON_NULL_EXPECTED;

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
	 * @param library
	 * @see Item#setParent(Item)
	 */
	public void setParent(final Item library) {
		setParent((Library) library);
	}

	/**
	 * @param library
	 * @see Library#setParent(Library)
	 */
	public void setParent(final Library library) {
		setParentSchemeProtoGroup((SchemeProtoGroup) library);
	}

	/**
	 * @param parentSchemeProtoGroupId
	 */
	void setParentSchemeProtoGroupId(final Identifier parentSchemeProtoGroupId) {
		assert !parentSchemeProtoGroupId.equals(this.id) : CIRCULAR_DEPS_PROHIBITED;
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
	 */
	public void setSchemeProtoElements(final Set<SchemeProtoElement> schemeProtoElements) {
		assert schemeProtoElements != null: NON_NULL_EXPECTED;
		for (final SchemeProtoElement oldSchemeProtoElement : this.getSchemeProtoElements()) {
			/*
			 * Check is made to prevent SchemeProtoElements from
			 * permanently losing their parents.
			 */
			assert !schemeProtoElements.contains(oldSchemeProtoElement);
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
	 */
	public void setSchemeProtoGroups(final Set<SchemeProtoGroup> schemeProtoGroups) {
		assert schemeProtoGroups != null: NON_NULL_EXPECTED;
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
		assert symbolId.isVoid() || symbolId.getMajor() == SCHEMEPROTOGROUP_CODE;
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
