/*
 * $Id: SchemeElementImpl.java,v 1.15 2005/03/01 14:00:39 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.corba.SiteNode_Transferable;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.scheme.CharacteristicSeqContainer;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/03/01 14:00:39 $
 * @module scheme_v1
 */
final class SchemeElementImpl extends SchemeElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 3690758397188124983L;

	SchemeElementImpl() {
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}
	
	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
		throw new UnsupportedOperationException();
	}

	public SchemePath alarmedPath() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPath(SchemePath alarmedPath) {
		throw new UnsupportedOperationException();
	}

	public PathElement alarmedPathElement() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPathElement(PathElement alarmedPathElement) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl()
	 */
	public CharacteristicSeqContainer characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.Characterizable#characteristicsImpl(CharacteristicSeqContainer)
	 */
	public void characteristicsImpl(final CharacteristicSeqContainer characteristics) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement cloneInstance() {
		try {
			return (SchemeElement) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long getCreated() {
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

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public Equipment_Transferable equipment() {
		throw new UnsupportedOperationException();
	}

	public void equipment(Equipment_Transferable equipment) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentImpl()
	 */
	public Equipment equipmentImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentImpl(com.syrus.AMFICOM.configuration.Equipment)
	 */
	public void equipmentImpl(Equipment newEquipmentImpl) {
		throw new UnsupportedOperationException();
	}

	public EquipmentType_Transferable equipmentType() {
		throw new UnsupportedOperationException();
	}

	public void equipmentType(EquipmentType_Transferable equipmentType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentTypeImpl()
	 */
	public EquipmentType equipmentTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentTypeImpl(com.syrus.AMFICOM.configuration.EquipmentType)
	 */
	public void equipmentTypeImpl(EquipmentType newEquipmentTypeImpl) {
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

	public Scheme internalScheme() {
		throw new UnsupportedOperationException();
	}

	public void internalScheme(Scheme internalScheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeElement#label()
	 */
	public String label() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLabel
	 * @see SchemeElement#label(String)
	 */
	public void label(final String newLabel) {
		throw new UnsupportedOperationException();
	}

	public long getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public KIS_Transferable rtu() {
		throw new UnsupportedOperationException();
	}

	public void rtu(KIS_Transferable rtu) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#rtuImpl()
	 */
	public KIS rtuImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#rtuImpl(com.syrus.AMFICOM.configuration.KIS)
	 */
	public void rtuImpl(KIS newRtuImpl) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme scheme) {
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

	public SchemeDevice[] schemeDevices() {
		throw new UnsupportedOperationException();
	}

	public void schemeDevices(SchemeDevice[] schemeDevices) {
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

	public SchemeProtoElement schemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoElement
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeElement#schemeProtoElement(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement)
	 */
	public void schemeProtoElement(SchemeProtoElement newSchemeProtoElement) {
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

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeElement#siteNode()
	 */
	public SiteNode_Transferable siteNode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param siteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeElement#siteNode(SiteNode_Transferable)
	 */
	public void siteNode(final SiteNode_Transferable siteNode) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeElement#siteNodeImpl()
	 */
	public SiteNode siteNodeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param siteNode
	 * @see com.syrus.AMFICOM.map.ComSyrusAmficomMapSchemeElement#siteNodeImpl(SiteNode)
	 */
	public void siteNodeImpl(final SiteNode siteNode) {
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
	public void ugoCellImpl(final SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeElementImpl schemeElement = (SchemeElementImpl) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeElement;
	}
}
