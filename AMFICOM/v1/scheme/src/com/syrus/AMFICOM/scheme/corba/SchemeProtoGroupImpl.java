/*
 * $Id: SchemeProtoGroupImpl.java,v 1.10 2005/02/28 14:24:19 bass Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/02/28 14:24:19 $
 * @module scheme_v1
 */
final class SchemeProtoGroupImpl extends SchemeProtoGroup implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3257003267744937273L;

	SchemeProtoGroupImpl() {
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

	public long created() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#creatorId()
	 */
	public Identifier creatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#dependencies()
	 */
	public Identifier[] dependencies() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#headerTransferable()
	 */
	public StorableObject_Transferable headerTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter for <code>changed</code> property.
	 *
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		return this.delegate.isChanged();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#modifierId()
	 */
	public Identifier modifierId() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup parent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newParent
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#parent(com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup)
	 */
	public void parent(SchemeProtoGroup newParent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see ISchemeProtoGroup#parentSchemeProtoGroup()
	 */
	public SchemeProtoGroup parentSchemeProtoGroup() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement[] schemeProtoElements() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoElements
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup#schemeProtoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
	 */
	public void schemeProtoElements(SchemeProtoElement[] newSchemeProtoElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoGroup[] schemeProtoGroups() {
		throw new UnsupportedOperationException();
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
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		this.delegate.setChanged(storableObjectFactory, changed);
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

	public long version() {
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
