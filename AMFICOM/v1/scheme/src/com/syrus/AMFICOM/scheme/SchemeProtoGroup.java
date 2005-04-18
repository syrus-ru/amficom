/*-
 * $Id: SchemeProtoGroup.java,v 1.19 2005/04/18 12:34:45 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemListener;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup_Transferable;
import com.syrus.AMFICOM.scheme.logic.Library;
import com.syrus.AMFICOM.scheme.logic.LibraryEntry;
import com.syrus.util.Log;

/**
 * #01 in hierarchy.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/04/18 12:34:45 $
 * @module scheme_v1
 * @todo Implement fireParentChanged() and call it on any setParent*() invocation. 
 */
public final class SchemeProtoGroup extends AbstractCloneableStorableObject
		implements Describable, SchemeSymbolContainer, Library {
	private static final long serialVersionUID = 3256721788422862901L;

	private String name;

	private String description;

	private Identifier symbolId;

	private Identifier parentSchemeProtoGroupId;

	private SchemeProtoGroupDatabase schemeProtoGroupDatabase; 

	private ArrayList itemListeners = new ArrayList();

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeProtoGroup(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeProtoGroupDatabase = SchemeDatabaseContext.getSchemeProtoGroupDatabase();
		try {
			this.schemeProtoGroupDatabase.retrieve(this);
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

		this.schemeProtoGroupDatabase = SchemeDatabaseContext.getSchemeProtoGroupDatabase();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException 
	 */
	SchemeProtoGroup(final SchemeProtoGroup_Transferable transferable) throws CreateObjectException {
		try {
			this.schemeProtoGroupDatabase = SchemeDatabaseContext.getSchemeProtoGroupDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId cannot be <code>null</code>.
	 * @param name cannot be <code>null</code>.
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
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		
		try {
			final Date created = new Date();
			final SchemeProtoGroup schemeProtoGroup = new SchemeProtoGroup(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, symbol,
					parentSchemeProtoGroup);
			schemeProtoGroup.changed = true;
			return schemeProtoGroup;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeProtoGroup.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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
			throw new UnsupportedOperationException(ErrorMessages.UNSUPPORTED_CHILD_TYPE);
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
		assert schemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;
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
		assert schemeProtoGroup != null: ErrorMessages.NON_NULL_EXPECTED;
		assert schemeProtoGroup != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
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
	public Object clone() {
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
	public List getChildren() {
		final Set schemeProtoGroups = getSchemeProtoGroups();
		final Set schemeProtoElements = getSchemeProtoElements();
		final List children = new ArrayList(schemeProtoGroups.size() + schemeProtoElements.size());
		children.addAll(schemeProtoGroups);
		children.addAll(schemeProtoElements);
		return children;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.symbolId != null
				&& this.parentSchemeProtoGroupId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.symbolId);
		dependencies.add(this.parentSchemeProtoGroupId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
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
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
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
	public Item getParent() {
		return getParentSchemeProtoGroup();
	}

	/**
	 * @return <code>schemeProtoGroup</code> parent for this
	 *         <code>schemeProtoGroup</code>, or <code>null</code> if
	 *         none.
	 */
	public SchemeProtoGroup getParentSchemeProtoGroup() {
		assert this.parentSchemeProtoGroupId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		if (this.parentSchemeProtoGroupId.isVoid())
			return null;

		try {
			return (SchemeProtoGroup) SchemeStorableObjectPool.getStorableObject(this.parentSchemeProtoGroupId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemeProtoElements() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemeProtoGroups() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		assert this.symbolId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.symbolId.isVoid())
			return null;
		try {
			return (BitmapImageResource) ResourceStorableObjectPool
					.getStorableObject(this.symbolId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeProtoGroup_Transferable(
				getHeaderTransferable(),
				this.name,
				this.description,
				(Identifier_Transferable) this.symbolId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeProtoGroupId.getTransferable());
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
		assert schemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeProtoElements().contains(schemeProtoElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
		assert schemeProtoGroup != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeProtoGroups().contains(schemeProtoGroup): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
	public void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier symbolId,
			final Identifier parentSchemeProtoGroupId) {
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert symbolId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoGroupId != null: ErrorMessages.NON_NULL_EXPECTED;
		super.setAttributes(created, modified, creatorId, modifierId,
				version);
		this.name = name;
		this.description = description;
		this.symbolId = symbolId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		this.changed = true;
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
	 * @param parentSchemeProtoGroup
	 * @todo Check whether <code>parentSchemeProtoGroup</code> is not a
	 *       lower-level descendant of <code>this</code>.
	 */
	public void setParentSchemeProtoGroup(final SchemeProtoGroup parentSchemeProtoGroup) {
		assert parentSchemeProtoGroup != this: ErrorMessages.CIRCULAR_DEPS_PROHIBITED;
		final Identifier newParentSchemeProtoGroupId = Identifier.possiblyVoid(parentSchemeProtoGroup);
		if (this.parentSchemeProtoGroupId.equals(newParentSchemeProtoGroupId))
			return;
		this.parentSchemeProtoGroupId = newParentSchemeProtoGroupId;
		this.changed = true;
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
	public void setSchemeProtoElements(final Set schemeProtoElements) {
		assert schemeProtoElements != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeProtoElementIterator = getSchemeProtoElements().iterator(); oldSchemeProtoElementIterator.hasNext();) {
			final SchemeProtoElement oldSchemeProtoElement = (SchemeProtoElement) oldSchemeProtoElementIterator.next();
			/*
			 * Check is made to prevent SchemeProtoElements from
			 * permanently losing their parents.
			 */
			assert !schemeProtoElements.contains(oldSchemeProtoElement);
			removeSchemeProtoElement(oldSchemeProtoElement);
		}
		for (final Iterator schemeProtoElementIterator = schemeProtoElements.iterator(); schemeProtoElementIterator.hasNext();)
			addSchemeProtoElement((SchemeProtoElement) schemeProtoElementIterator.next());
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
	public void setSchemeProtoGroups(final Set schemeProtoGroups) {
		assert schemeProtoGroups != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeProtoGroupIterator = getSchemeProtoGroups().iterator(); oldSchemeProtoGroupIterator.hasNext();)
			removeSchemeProtoGroup((SchemeProtoGroup) oldSchemeProtoGroupIterator.next());
		for (final Iterator schemeProtoGroupIterator = schemeProtoGroups.iterator(); schemeProtoGroupIterator.hasNext();)
			addSchemeProtoGroup((SchemeProtoGroup) schemeProtoGroupIterator.next());
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
		this.changed = true;
	}

	/**
	 * @param transferable
	 * @throws ApplicationException 
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final SchemeProtoGroup_Transferable schemeProtoGroup = (SchemeProtoGroup_Transferable) transferable;
		super.fromTransferable(schemeProtoGroup.header);
		this.name = schemeProtoGroup.name;
		this.description = schemeProtoGroup.description;
		this.symbolId = new Identifier(schemeProtoGroup.symbolId);
		this.parentSchemeProtoGroupId = new Identifier(schemeProtoGroup.parentSchemeProtoGroupId);
	}
}
