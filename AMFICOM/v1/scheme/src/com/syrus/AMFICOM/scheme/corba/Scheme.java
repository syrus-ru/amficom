/*
 * $Id: Scheme.java,v 1.2 2005/03/10 15:06:08 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.*;
import com.syrus.AMFICOM.map.corba.Map_Transferable;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;
import com.syrus.util.Log;
import java.util.Date;


/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/10 15:06:08 $
 * @module scheme_v1
 */
public final class Scheme extends StorableObject implements Namable, Describable, SchemeCellContainer, ComSyrusAmficomAdministrationScheme, ComSyrusAmficomMapScheme {

	private static final long serialVersionUID = 3546639914939594546L;

 /**
 						 * Value is assigned automatically.
 						 */
  protected Identifier domainId = null;
  protected Identifier mapId = null;
  protected Identifier schemeCableLinkIds[] = null;

 /**
 						 * Takes non-null value at pack time.
 						 */
  protected Identifier schemeCellId = null;
  protected Identifier schemeElementIds[] = null;
  protected Identifier schemeLinkIds[] = null;
  protected Identifier schemeMonitoringSolutionId = null;
  protected Identifier symbolId = null;
  protected String thisDescription = null;
  protected int thisHeight = 0;
  protected String thisLabel = null;
  protected String thisName = null;
  protected Type thisType = null;
  protected int thisWidth = 0;

 /**
 						 * Takes non-null value at pack time.
 						 */
  protected Identifier ugoCellId = null;

	protected Scheme(Identifier id) {
		super(id);
	}

	protected Scheme(final Identifier id, final Date created, final Date modified, final Identifier creatorId, final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public Object clone() {
		try {
			final Scheme scheme = (Scheme) super.clone();
			/**
			 * @todo Update the newly created object.
			 */
			return scheme;
		} catch (final CloneNotSupportedException cnse) {
			Log.debugException(cnse, Log.SEVERE);
			return null;
		}
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.administration.ComSyrusAmficomAdministrationScheme#domain()
	 */
	public Domain_Transferable domain() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomain
	 * @see com.syrus.AMFICOM.administration.ComSyrusAmficomAdministrationScheme#domain(com.syrus.AMFICOM.administration.corba.Domain_Transferable)
	 */
	public void domain(Domain_Transferable newDomain) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.administration.ComSyrusAmficomAdministrationScheme#domainImpl()
	 */
	public Domain domainImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomainImpl
	 * @see com.syrus.AMFICOM.administration.ComSyrusAmficomAdministrationScheme#domainImpl(com.syrus.AMFICOM.administration.Domain)
	 */
	public void domainImpl(Domain newDomainImpl) {
		throw new UnsupportedOperationException();
	}

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Identifier[] getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	public int height() {
		throw new UnsupportedOperationException();
	}

	public void height(int height) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public String label() {
		throw new UnsupportedOperationException();
	}

	public void label(String label) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapScheme#map()
	 */
	public Map_Transferable map() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param map
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapScheme#map(Map_Transferable)
	 */
	public void map(final Map_Transferable map) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapScheme#mapImpl()
	 */
	public Map mapImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param map
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapScheme#mapImpl(Map)
	 */
	public void mapImpl(final Map map) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink[] schemeCableLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeCableLinks(SchemeCableLink[] schemeCableLinks) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable schemeCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newSchemeCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl()
	 */
	public SchemeImageResource schemeCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl(SchemeImageResource)
	 */
	public void schemeCellImpl(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement[] schemeElements() {
		throw new UnsupportedOperationException();
	}

	public void schemeElements(SchemeElement[] schemeElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] schemeLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeLinks(SchemeLink[] schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution schemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	public void schemeMonitoringSolution(
			SchemeMonitoringSolution schemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		throw new UnsupportedOperationException();
	}

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

	public Type type() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newType
	 * @see com.syrus.AMFICOM.scheme.corba.Scheme#type(com.syrus.AMFICOM.scheme.corba.SchemePackage.Type)
	 */
	public void type(Type newType) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable ugoCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newUgoCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newUgoCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl()
	 */
	public SchemeImageResource ugoCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl(SchemeImageResource)
	 */
	public void ugoCellImpl(SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public int width() {
		throw new UnsupportedOperationException();
	}

	public void width(int width) {
		throw new UnsupportedOperationException();
	}
}
