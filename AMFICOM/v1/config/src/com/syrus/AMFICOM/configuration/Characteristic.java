/*
 * $Id: Characteristic.java,v 1.26 2004/11/15 13:50:27 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

/**
 * @version $Revision: 1.26 $, $Date: 2004/11/15 13:50:27 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class Characteristic extends StorableObject implements TypedObject {
	static final long serialVersionUID = -2746555753961778403L;

	private CharacteristicType type;
	private String name;
	private String description;
	private int sort;
	private String value;
	private Identifier characterizedId;
	private boolean editable;
	private boolean visible;

	private StorableObjectDatabase characteristicDatabase;

	public Characteristic(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			this.characteristicDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Characteristic(Characteristic_Transferable ct) throws CreateObjectException {
		super(ct.header);
		
		try {
			this.type = (CharacteristicType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(ct.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = new String(ct.name);
		this.description = new String(ct.description);
		this.sort = ct.sort.value();
		this.value = new String(ct.value);
		this.characterizedId = new Identifier(ct.characterized_id);
		this.editable = ct.is_editable;
		this.visible = ct.is_visible;

	}
	
	protected Characteristic(Identifier id,
						Identifier creatorId,
						CharacteristicType type,
						String name,
						String description,
						int sort,
						String value,
						Identifier characterizedId,
						boolean editable,
						boolean visible){		
				super(id,
						new Date(System.currentTimeMillis()),
						new Date(System.currentTimeMillis()),
						creatorId,
						creatorId);				
				this.type = type;
				this.name = name;
				this.description = description;				
				this.sort = sort;
				this.value = value;
				this.characterizedId = characterizedId;
				
				this.editable = editable;
				this.visible = visible;
				
				this.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param type see {@link CharacteristicType}
	 * @param name
	 * @param description
	 * @param sort
	 * @param value
	 * @param characterizedId
	 * @return
	 */
	public static Characteristic  createInstance(Identifier id,
													Identifier creatorId,
													CharacteristicType type,
													String name,
													String description,
													int sort,
													String value,
													Identifier characterizedId,
													boolean editable,
													boolean visible){
		return new Characteristic(id,
								  creatorId,
								  type,
								  name,
								  description,
								  sort,
								  value,
								  characterizedId,
								  editable,
								  visible);
	}
	
	public static Characteristic getInstance(Characteristic_Transferable ct) throws CreateObjectException {
		Characteristic characteristic = new Characteristic(ct);
		
		characteristic.characteristicDatabase = ConfigurationDatabaseContext.characteristicDatabase;
		try {
			if (characteristic.characteristicDatabase != null)
				characteristic.characteristicDatabase.insert(characteristic);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return characteristic;
	}

	public Object getTransferable() {
		return new Characteristic_Transferable(super.getHeaderTransferable(),
											   (Identifier_Transferable)this.type.getId().getTransferable(),
											   new String(this.name),
											   new String(this.description),
											   CharacteristicSort.from_int(this.sort),
											   new String(this.value),
											   (Identifier_Transferable)this.characterizedId.getTransferable(),
											   this.editable,
											   this.visible);
	}


	public boolean isEditable() {
		return this.editable;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public CharacteristicSort getSort() {
		return CharacteristicSort.from_int(this.sort);
	}

	public String getValue() {
		return this.value;
	}

	public Identifier getCharacterizedId() {
		return this.characterizedId;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						CharacteristicType type,
																						String name,
																						String description,
																						int sort,
																						String value,
																						Identifier characterizedId,
																						boolean editable,
																						boolean visible) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId);
		this.type = type;
		this.name = name;
		this.description = description;
		this.sort = sort;
		this.value = value;
		this.characterizedId = characterizedId;
		this.editable = editable;
		this.visible = visible;
	}
	
	public void setValue(String value){
		this.currentVersion = super.getNextVersion();
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
			default:
				throw new IllegalDataException("Unknown characterized entity, id: '" + id.getIdentifierString() + '\'');
		}
		
		return csort;
	}	
	
	public List getDependencies() {
		List dependencies = new ArrayList(2);
		dependencies.add(this.characterizedId);
		dependencies.add(this.type);
		return null;
	}
	
}
