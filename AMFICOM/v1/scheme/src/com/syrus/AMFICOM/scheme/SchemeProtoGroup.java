/*
 * $Id: SchemeProtoGroup.java,v 1.5 2005/03/21 16:46:50 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.util.Log;
import java.util.*;

/**
 * #01 in hierarchy.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/03/21 16:46:50 $
 * @module scheme_v1
 */
public final class SchemeProtoGroup extends AbstractCloneableStorableObject
		implements Describable, SchemeSymbolContainer {
	private static final long serialVersionUID = 3256721788422862901L;

	private String description;

	private String name;

	private Identifier parentSchemeProtoGroupId;

	private StorableObjectDatabase schemeProtoGroupDatabase; 

	private Identifier symbolId;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeProtoGroup(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.schemeProtoGroupDatabase = SchemeDatabaseContext.schemeProtoGroupDatabase;
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
		this.symbolId
				= symbol == null
				? Identifier.VOID_IDENTIFIER
				: symbol.getId();
		this.parentSchemeProtoGroupId
				= parentSchemeProtoGroup == null
				? Identifier.VOID_IDENTIFIER
				: parentSchemeProtoGroup.getId();

		this.schemeProtoGroupDatabase = SchemeDatabaseContext.schemeProtoGroupDatabase;
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
		assert creatorId != null && !creatorId.equals(Identifier.VOID_IDENTIFIER): ErrorMessages.NON_VOID_EXPECTED;
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
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoGroup.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
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
		schemeProtoGroup.parentSchemeProtoGroupId = this.getId();
		schemeProtoGroup.changed = true;
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
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		assert this.symbolId != null
				&& this.parentSchemeProtoGroupId != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.symbolId.equals(Identifier.VOID_IDENTIFIER)) {
			if (this.parentSchemeProtoGroupId
					.equals(Identifier.VOID_IDENTIFIER))
				return Collections.EMPTY_LIST;
			return Collections
					.singletonList(this.parentSchemeProtoGroupId);
		}
		if (this.parentSchemeProtoGroupId
				.equals(Identifier.VOID_IDENTIFIER))
			return Collections.singletonList(this.symbolId);
		final List dependencies = new ArrayList(2);
		dependencies.add(this.symbolId);
		dependencies.add(this.parentSchemeProtoGroupId);
		return Collections.unmodifiableList(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @return <code>schemeProtoGroup</code> parent for this
	 *         <code>schemeProtoGroup</code>, or <code>null</code> if
	 *         none.
	 */
	public SchemeProtoGroup getParentSchemeProtoGroup() {
		assert this.parentSchemeProtoGroupId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.parentSchemeProtoGroupId.equals(Identifier.VOID_IDENTIFIER))
			return null;
		try {
			return (SchemeProtoGroup) SchemeStorableObjectPool.getStorableObject(this.parentSchemeProtoGroupId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @return an immutable collection.
	 */
	public Collection getSchemeProtoElements() {
		try {
			return Collections.unmodifiableList(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * @return an immutable collection.
	 */
	public Collection getSchemeProtoGroups() {
		try {
			return Collections.unmodifiableList(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		assert this.symbolId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		if (this.symbolId.equals(Identifier.VOID_IDENTIFIER))
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
	 * @see TransferableObject#getTransferable()
	 * @todo Implement.
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The <code>schemeProtoElement</code> must belong to this
	 * <code>schemeProtoGroup</code>, or crap will meet the fan.
	 *
	 * @param schemeProtoElement
	 * @todo Decide how removal should be interpreted: setting a parent of
	 *       <code>null</code> (which is impossible for a
	 *       <code>schemeProtoElement</code>) or physical removal (which is
	 *       done so far).
	 */
	public void removeSchemeProtoElement(final SchemeProtoElement schemeProtoElement) {
		assert schemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;
		Collection schemeProtoElements;
		try {
			schemeProtoElements = SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			schemeProtoElements = Collections.EMPTY_LIST;
		}
		assert schemeProtoElements.contains(schemeProtoElement);
		SchemeStorableObjectPool.delete(schemeProtoElement.getId());
	}

	/**
	 * The <code>schemeProtoGroup</code> must belong to this
	 * <code>schemeProtoGroup</code>, or crap will meet the fan.
	 * 
	 * @param schemeProtoGroup
	 * @todo Decide whether it's good to have more than one top-level
	 *       <code>schemeProtoGroup</code>.
	 */
	public void removeSchemeProtoGroup(final SchemeProtoGroup schemeProtoGroup) {
		assert schemeProtoGroup != null: ErrorMessages.NON_NULL_EXPECTED;
		Collection schemeProtoGroups;
		try {
			schemeProtoGroups = SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			schemeProtoGroups = Collections.EMPTY_LIST;
		}
		assert schemeProtoGroups.contains(schemeProtoGroup);
		schemeProtoGroup.parentSchemeProtoGroupId = Identifier.VOID_IDENTIFIER;
		schemeProtoGroup.changed = true;
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
	 * @see Namable#setName(String)
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
	 * To make a slight alteration of <code>schemeProtoGroups</code> for
	 * this <code>schemeProtoGroup</code>, use
	 * {@link #addSchemeProtoGroup(SchemeProtoGroup)} and/or
	 * {@link #removeSchemeProtoGroup(SchemeProtoGroup)}. This method
	 * will completely overwrite old <code>schemeProtoGroups</code> with
	 * the new ones (i. e. remove old and add new ones).
	 * 
	 * @param schemeProtoGroups
	 */
	public void setSchemeProtoGroups(final Collection schemeProtoGroups) {
		assert schemeProtoGroups != null: ErrorMessages.NON_NULL_EXPECTED;
		Collection oldSchemeProtoGroups;
		try {
			oldSchemeProtoGroups = SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			oldSchemeProtoGroups = Collections.EMPTY_LIST;
		}
		for (final Iterator oldSchemeProtoGroupIterator = oldSchemeProtoGroups.iterator(); oldSchemeProtoGroupIterator.hasNext();)
			removeSchemeProtoGroup((SchemeProtoGroup) oldSchemeProtoGroupIterator.next());
		for (final Iterator schemeProtoGroupIterator = schemeProtoGroups.iterator(); schemeProtoGroupIterator.hasNext();)
			addSchemeProtoGroup((SchemeProtoGroup) schemeProtoGroupIterator.next());
	}

	/**
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		Identifier newSymbolId;
		if (symbol == null)
			newSymbolId = Identifier.VOID_IDENTIFIER;
		else
			newSymbolId = symbol.getId();
		if (this.symbolId.equals(newSymbolId))
			return;
		this.symbolId = newSymbolId;
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
	public void setSchemeProtoElements(final Collection schemeProtoElements) {
		assert schemeProtoElements != null: ErrorMessages.NON_NULL_EXPECTED;
		Collection oldSchemeProtoElements;
		try {
			oldSchemeProtoElements = SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			oldSchemeProtoElements = Collections.EMPTY_LIST;
		}
		for (final Iterator oldSchemeProtoElementIterator = oldSchemeProtoElements.iterator(); oldSchemeProtoElementIterator.hasNext();) {
			final SchemeProtoElement oldSchemeProtoElement = (SchemeProtoElement) oldSchemeProtoElementIterator.next();
			assert !schemeProtoElements.contains(oldSchemeProtoElement);
			removeSchemeProtoElement(oldSchemeProtoElement);
		}
		for (final Iterator schemeProtoElementIterator = schemeProtoElements.iterator(); schemeProtoElementIterator.hasNext();)
			addSchemeProtoElement((SchemeProtoElement) schemeProtoElementIterator.next());
	}
}
