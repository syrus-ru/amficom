/*
 * $Id: SchemeProtoGroup.java,v 1.4 2005/03/15 17:47:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.scheme.SchemeSymbolContainer;
import com.syrus.util.Log;
import java.util.*;

/**
 * #01 in hierarchy.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/15 17:47:57 $
 * @module scheme_v1
 */
public final class SchemeProtoGroup extends AbstractCloneableStorableObject implements Namable,
		Describable, SchemeSymbolContainer {

	private static final Identifier EMPTY_DEPENDENCIES[] = new Identifier[0];

	protected StorableObject delegate = null;

	protected Identifier parentSchemeProtoGroupId = null;

	protected Identifier symbolId = null;

	protected String thisDescription = null;

	protected String thisName = null;

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

	/**
	 * @see Describable#description()
	 */
	public String description() {
		return this.thisDescription;
	}

	/**
	 * @param description
	 * @see Describable#description(String)
	 */
	public void description(final String description) {
		this.thisDescription = description;
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
	 * @see Namable#name()
	 */
	public String name() {
		return this.thisName;
	}

	/**
	 * @param name
	 * @see Namable#name(String)
	 */
	public void name(final String name) {
		this.thisName = name;
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
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
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoGroups(com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup[])
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
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
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
}
