/*
 * $Id: SchemeProtoGroup.java,v 1.3 2005/03/17 18:17:27 bass Exp $ Copyright ¿
 * 2004 Syrus Systems. Dept. of Science & Technology. Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.Log;
import java.util.*;

/**
 * #01 in hierarchy.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/17 18:17:27 $
 * @module scheme_v1
 */
public final class SchemeProtoGroup extends AbstractCloneableStorableObject
		implements Describable, SchemeSymbolContainer {
	private static final long serialVersionUID = 3256721788422862901L;

	private static final Identifier EMPTY_DEPENDENCIES[] = new Identifier[0];

	protected StorableObject delegate = null;

	protected Identifier parentSchemeProtoGroupId = null;

	protected Identifier symbolId = null;

	private String description;

	private String name;

	private Identifier cachedDependencies[];

	private final Identifier ownDependencies[] = new Identifier[1];

	/**
	 * @param id
	 */
	SchemeProtoGroup(final Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param parentSchemeProtoGroup
	 */
	SchemeProtoGroup(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name,
			final SchemeProtoGroup parentSchemeProtoGroup) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public Object clone() {
		final SchemeProtoGroup schemeProtoGroup = (SchemeProtoGroup) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeProtoGroup;
	}

	public List getDependencies() {
		return Arrays.asList(getDependenciesAsArray());
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public synchronized Identifier[] getDependenciesAsArray() {
		Identifier localOwnDependencies[];
		if (this.parentSchemeProtoGroupId == null) {
			assert parentSchemeProtoGroup() == null;
			localOwnDependencies = EMPTY_DEPENDENCIES;
		} else {
			assert parentSchemeProtoGroup().getId().equals(
					this.parentSchemeProtoGroupId);
			this.ownDependencies[0] = this.parentSchemeProtoGroupId;
			localOwnDependencies = this.ownDependencies;
		}

		final Identifier delegateDependencies[] = (Identifier[]) this.delegate
				.getDependencies()
				.toArray(
						new Identifier[this.delegate
								.getDependencies()
								.size()]);
		assert delegateDependencies != null;

		final int delegateDependenciesLength = delegateDependencies.length;
		final int localOwnDependenciesLength = localOwnDependencies.length;

		if (delegateDependenciesLength == 0)
			return localOwnDependencies;
		else if (localOwnDependenciesLength == 0)
			return delegateDependencies;
		else {
			if (this.cachedDependencies == null
					|| this.cachedDependencies.length != delegateDependenciesLength
							+ localOwnDependenciesLength)
				this.cachedDependencies = new Identifier[delegateDependenciesLength
						+ localOwnDependenciesLength];
			System.arraycopy(delegateDependencies, 0,
					this.cachedDependencies, 0,
					delegateDependenciesLength);
			System.arraycopy(localOwnDependencies, 0,
					this.cachedDependencies,
					delegateDependenciesLength,
					localOwnDependenciesLength);
			return this.cachedDependencies;
		}
	}

	/**
	 * @see SchemeProtoGroup#parentSchemeProtoGroup()
	 */
	public SchemeProtoGroup parentSchemeProtoGroup() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeProtoGroup#schemeProtoElements()
	 * @todo Implement.
	 */
	public SchemeProtoElement[] schemeProtoElements() {
		return new SchemeProtoElement[0];
	}

	/**
	 * @param newSchemeProtoElements
	 * @see com.syrus.AMFICOM.scheme.SchemeProtoGroup#schemeProtoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
	 */
	public void schemeProtoElements(
			SchemeProtoElement[] newSchemeProtoElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeProtoGroup#schemeProtoGroups()
	 * @todo Implement.
	 */
	public SchemeProtoGroup[] schemeProtoGroups() {
		return new SchemeProtoGroup[0];
	}

	/**
	 * @param newSchemeProtoGroups
	 * @see com.syrus.AMFICOM.scheme.SchemeProtoGroup#schemeProtoGroups(com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup[])
	 */
	public void schemeProtoGroups(SchemeProtoGroup[] newSchemeProtoGroups) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param symbol
	 * @param parentSchemeProtoGroup
	 * @see SchemeProtoGroup#setAttributes(long, long, Identifier,
	 *      Identifier, long, String, String, ImageResource_Transferable,
	 *      SchemeProtoGroup)
	 */
	public void setAttributes(final long created, final long modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final ImageResource_Transferable symbol,
			final SchemeProtoGroup parentSchemeProtoGroup) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return <code>symbol</code> associated with this
	 *         <code>SchemeProtoGroup</code>, or <code>null</code> if
	 *         none.
	 * @see SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
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
	 * @param symbol can be <code>null</code>.
	 * @see SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(final BitmapImageResource symbol) {
		Identifier newSymbolId;
		if (symbol == null)
			newSymbolId = Identifier.VOID_IDENTIFIER;
		else
			newSymbolId = symbol.getId();
		if (!this.symbolId.equals(newSymbolId)) {
			this.symbolId = newSymbolId;
			this.changed = true;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param creatorId cannot be null
	 * @param name cannot be null
	 * @param parentSchemeProtoGroup may be null (for a top-level group).
	 * @throws CreateObjectException
	 */
	public static SchemeProtoGroup createInstance(
			final Identifier creatorId, final String name,
			final SchemeProtoGroup parentSchemeProtoGroup)
			throws CreateObjectException {
		assert creatorId != null && name != null;
		try {
			final Date created = new Date();
			final SchemeProtoGroup schemeProtoGroup = new SchemeProtoGroup(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, parentSchemeProtoGroup);
			schemeProtoGroup.changed = true;
			return schemeProtoGroup;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoGroup.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}
}
