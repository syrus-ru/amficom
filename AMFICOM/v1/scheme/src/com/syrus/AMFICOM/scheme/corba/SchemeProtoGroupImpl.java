/*
 * $Id: SchemeProtoGroupImpl.java,v 1.11 2005/03/01 14:00:39 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/03/01 14:00:39 $
 * @module scheme_v1
 */
final class SchemeProtoGroupImpl extends SchemeProtoGroup implements Cloneable {
	private static final Identifier EMPTY_DEPENDENCIES[] = new Identifier[0];

	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3257003267744937273L;

	private Identifier cachedDependencies[];

	private final Identifier ownDependencies[] = new Identifier[1];

	SchemeProtoGroupImpl() {
		// empty
	}

	/**
	 * @param id
	 */
	SchemeProtoGroupImpl(final Identifier id) {
		throw new UnsupportedOperationException();
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
	SchemeProtoGroupImpl(final Identifier id, final long created, final long modified, final Identifier creatorId, final Identifier modifierId, final long version, final String name, final SchemeProtoGroup parentSchemeProtoGroup) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup cloneInstance() {
		try {
			return (SchemeProtoGroup) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
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

	/**
	 * @see IStorableObject#getCreated()
	 */
	public long getCreated() {
		return this.delegate.getCreated();
	}

	/**
	 * @see IStorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		return this.delegate.getCreatorId();
	}

	/**
	 * @see IStorableObject#getDependencies()
	 */
	public synchronized Identifier[] getDependencies() {
		Identifier localOwnDependencies[];
		if (this.parentSchemeProtoGroupId == null) {
			assert parentSchemeProtoGroup() == null;
			localOwnDependencies = EMPTY_DEPENDENCIES;
		} else { 
			assert parentSchemeProtoGroup().getId().equals(this.parentSchemeProtoGroupId);
			this.ownDependencies[0] = this.parentSchemeProtoGroupId;
			localOwnDependencies = this.ownDependencies;
		}

		final Identifier delegateDependencies[] = this.delegate.getDependencies();
		assert delegateDependencies != null;

		final int delegateDependenciesLength = delegateDependencies.length;
		final int localOwnDependenciesLength = localOwnDependencies.length;

		if (delegateDependenciesLength == 0)
			return localOwnDependencies;
		else if (localOwnDependenciesLength == 0)
			return delegateDependencies;
		else {
			if (this.cachedDependencies == null
					|| this.cachedDependencies.length != delegateDependenciesLength + localOwnDependenciesLength)
				this.cachedDependencies = new Identifier[delegateDependenciesLength + localOwnDependenciesLength];
			System.arraycopy(delegateDependencies, 0,
					this.cachedDependencies, 0,
					delegateDependenciesLength);
			System.arraycopy(localOwnDependencies, 0,
					this.cachedDependencies, delegateDependenciesLength,
					localOwnDependenciesLength);
			return this.cachedDependencies;
		}
	}

	/**
	 * @see IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		return this.delegate.getHeaderTransferable();
	}

	/**
	 * @see Identifiable#getId()
	 */
	public Identifier getId() {
		return this.delegate.getId();
	}

	/**
	 * @see IStorableObject#getModified()
	 */
	public long getModified() {
		return this.delegate.getModified();
	}

	/**
	 * @see IStorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		return this.delegate.getModifierId();
	}

	/**
	 * @see IStorableObject#getVersion()
	 */
	public long getVersion() {
		return this.delegate.getVersion();
	}

	/**
	 * Getter for <code>changed</code> property.
	 *
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		return this.delegate.isChanged();
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
	 * @see ISchemeProtoGroup#parentSchemeProtoGroup()
	 */
	public SchemeProtoGroup parentSchemeProtoGroup() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see ISchemeProtoGroup#schemeProtoElements()
	 * @todo Implement.
	 */
	public SchemeProtoElement[] schemeProtoElements() {
		return new SchemeProtoElement[0];
	}

	/**
	 * @param newSchemeProtoElements
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
	 */
	public void schemeProtoElements(SchemeProtoElement[] newSchemeProtoElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see ISchemeProtoGroup#schemeProtoGroups()
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
	 * @see SchemeProtoGroup#setAttributes(long, long, Identifier, Identifier, long, String, String, ImageResource_Transferable, SchemeProtoGroup)
	 */
	public void setAttributes(final long created, final long modified, final Identifier creatorId, final Identifier modifierId, final long version, final String name, final String description, final ImageResource_Transferable symbol, final SchemeProtoGroup parentSchemeProtoGroup) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Setter for <code>changed</code> property.
	 *
	 * @param invoker
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory invoker, final boolean changed) {
		this.delegate.setChanged(invoker, changed);
	}

	/**
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbol()
	 */
	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable symbol) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl()
	 */
	public BitmapImageResource symbolImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl(BitmapImageResource)
	 */
	public void symbolImpl(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeProtoGroupImpl schemeProtoGroup = (SchemeProtoGroupImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeProtoGroup;
	}
}
