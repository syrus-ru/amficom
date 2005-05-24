/*
 * $Id: Characteristic.java,v 1.31 2005/05/24 13:24:59 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.31 $, $Date: 2005/05/24 13:24:59 $
 * @author $Author: bass $
 * @module general_v1
 */

public class Characteristic extends StorableObject implements TypedObject {
	private static final long serialVersionUID = -2746555753961778403L;

	private CharacteristicType type;
	private String name;
	private String description;
	private int sort;
	private String value;
	private Identifier characterizableId;
	private boolean editable;
	private boolean visible;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param id
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public Characteristic(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		CharacteristicDatabase database = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @param ct
	 * @throws CreateObjectException
	 */
	public Characteristic(Characteristic_Transferable ct) throws CreateObjectException {
		try {
			this.fromTransferable(ct);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Characteristic(Identifier id,
						Identifier creatorId,
						long version,
						CharacteristicType type,
						String name,
						String description,
						int sort,
						String value,
						Identifier characterizableId,
						boolean editable,
						boolean visible) {
				super(id,
						new Date(System.currentTimeMillis()),
						new Date(System.currentTimeMillis()),
						creatorId,
						creatorId,
						version);
				this.type = type;
				this.name = name;
				this.description = description;
				this.sort = sort;
				this.value = value;
				this.characterizableId = characterizableId;

				this.editable = editable;
				this.visible = visible;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return super.isValid() && !(this.type == null || this.name == null || this.description == null ||
				this.value == null || this.characterizableId == null);		
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type see {@link CharacteristicType}
	 * @param name
	 * @param description
	 * @param sort
	 * @param value
	 * @param characterizableId
	 * @throws CreateObjectException
	 */
	public static Characteristic createInstance(Identifier creatorId,
												 CharacteristicType type,
												 String name,
												 String description,
												 CharacteristicSort sort,
												 String value,
												 Identifier characterizableId,
												 boolean editable,
												 boolean visible) throws CreateObjectException {		

		try {
			Characteristic characteristic = new Characteristic(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE),
										creatorId,
										0L,
										type,
										name,
										description,
										sort.value(),
										value,
										characterizableId,
										editable,
										visible);
			
			assert characteristic.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			
			characteristic.changed = true;
			return characteristic;
		}
		catch (IdentifierGenerationException e) {
			throw new CreateObjectException("Characteristic.createInstance | cannot generate identifier ", e);
		}
	}	

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Characteristic_Transferable ct = (Characteristic_Transferable) transferable;
		
		super.fromTransferable(ct.header);
		
		this.type = (CharacteristicType)StorableObjectPool.getStorableObject(new Identifier(ct.type_id), true);
		this.name = ct.name;
		this.description = ct.description;
		this.sort = ct.sort.value();
		this.value = ct.value;
		this.characterizableId = new Identifier(ct.characterizable_id);
		this.editable = ct.is_editable;
		this.visible = ct.is_visible;
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new Characteristic_Transferable(super.getHeaderTransferable(),
												 (Identifier_Transferable)this.type.getId().getTransferable(),
												 this.name,
												 this.description,
												 CharacteristicSort.from_int(this.sort),
												 (this.value != null) ? this.value : "",
												 (Identifier_Transferable)this.characterizableId.getTransferable(),
												 this.editable,
												 this.visible);
	}

	public boolean isEditable() {
		return this.editable;
	}

	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setEditable0(boolean editable) {
		this.editable = editable;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setVisible0(boolean visible) {
		this.visible = visible;
	}
	
	public void setEditable(boolean editable) {
		this.setEditable0(editable);
		super.changed = true;
	}

	public void setVisible(boolean visible) {
		this.setVisible0(visible);
		super.changed = true;
	}

	public StorableObjectType getType() {
		return this.type;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setType0(CharacteristicType type) {
		this.type = type;
	}

	public void setType(CharacteristicType type) {
		this.setType0(type);
		super.changed = true;
	}

	public String getName() {
		return this.name;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setName0(String name) {
		this.name = name;
	}
	
	protected void setName(String name) {
		this.setName0(name);
		super.changed = true;
	}


	public String getDescription() {
		return this.description;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDescription0(String description){
		this.description = description;
	}

	public void setDescription(String description){
		this.setDescription0(description);
		super.changed = true;
	}

	public CharacteristicSort getSort() {
		return CharacteristicSort.from_int(this.sort);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setSort0(CharacteristicSort sort) {
		this.sort = sort.value();
	}
	
	
	public void setSort(CharacteristicSort sort){
		this.setSort0(sort);
		super.changed = true;
	}
	

	public String getValue() {
		return this.value;
	}

	public Identifier getCharacterizableId() {
		return this.characterizableId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setCharacterizableId0(Identifier characterizableId) {
		this.characterizableId = characterizableId;
	}
	
	public void setCharacterizableId(Identifier characterizableId) {
		this.setCharacterizableId0(characterizableId);
		super.changed = true;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												CharacteristicType type,
												String name,
												String description,
												int sort,
												String value,
												Identifier characterizableId,
												boolean editable,
												boolean visible) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.sort = sort;
		this.value = value;
		this.characterizableId = characterizableId;
		this.editable = editable;
		this.visible = visible;
	}

	public void setValue(String value){
		super.changed = true;
		this.value = value;
	}

	protected static CharacteristicSort getSortForId(Identifier id) throws IllegalDataException{
		CharacteristicSort csort;
		switch(id.getMajor()){
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN;
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_SERVER;
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_MCM;
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH;
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT;
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_PORT;
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_LINK;
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_KIS;
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORT;
				break;
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE;
				break;
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE;
				break;
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE;
				break;
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATHTYPE;
				break;
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE;
				break;
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				csort = CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE;
				break;
			default:
				throw new IllegalDataException("Unknown characterizable entity, id: '" + id.getIdentifierString() + '\'');
		}

		return csort;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		Set dependencies = new HashSet(2);
		dependencies.add(this.characterizableId);
		dependencies.add(this.type);
		return dependencies;
	}	
}
