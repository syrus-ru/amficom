/*
 * $Id: Characteristic.java,v 1.18 2005/04/01 14:12:32 bob Exp $
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
 * @version $Revision: 1.18 $, $Date: 2005/04/01 14:12:32 $
 * @author $Author: bob $
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

	private StorableObjectDatabase characteristicDatabase;

	public Characteristic(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicDatabase = GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			this.characteristicDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Characteristic(Characteristic_Transferable ct) throws CreateObjectException {
		this.characteristicDatabase = GeneralDatabaseContext.getCharacteristicDatabase();
		this.fromTransferable(ct);
	}

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

				this.characteristicDatabase = GeneralDatabaseContext.getCharacteristicDatabase();
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
												 int sort,
												 String value,
												 Identifier characterizableId,
												 boolean editable,
												 boolean visible) throws CreateObjectException {

		if (creatorId == null || type == null || name == null || description == null ||
				value == null || characterizableId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Characteristic characteristic = new Characteristic(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_ENTITY_CODE),
										creatorId,
										0L,
										type,
										name,
										description,
										sort,
										value,
										characterizableId,
										editable,
										visible);
			characteristic.changed = true;
			return characteristic;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Characteristic.createInstance | cannot generate identifier ", e);
		}
	}	

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		Characteristic_Transferable ct = (Characteristic_Transferable) transferable;
		
		super.fromTransferable(ct.header);
		
		try {
			this.type = (CharacteristicType)GeneralStorableObjectPool.getStorableObject(new Identifier(ct.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = new String(ct.name);
		this.description = new String(ct.description);
		this.sort = ct.sort.value();
		this.value = new String(ct.value);
		this.characterizableId = new Identifier(ct.characterizable_id);
		this.editable = ct.is_editable;
		this.visible = ct.is_visible;
		
	}

	public Object getTransferable() {
		return new Characteristic_Transferable(super.getHeaderTransferable(),
												 (Identifier_Transferable)this.type.getId().getTransferable(),
												 new String(this.name),
												 new String(this.description),
												 CharacteristicSort.from_int(this.sort),
												 new String(this.value),
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
	
	protected void setEditable0(boolean editable) {
		this.editable = editable;
	}

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
	
	protected void setCharacterizableId0(Identifier characterizableId) {
		this.characterizableId = characterizableId;
	}
	
	public void setCharacterizableId(Identifier characterizableId) {
		this.setCharacterizableId0(characterizableId);
		super.changed = true;
	}
	
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

	public Set getDependencies() {
		Set dependencies = new HashSet(2);
		dependencies.add(this.characterizableId);
		dependencies.add(this.type);
		return dependencies;
	}	
}
