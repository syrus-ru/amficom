/*
 * $Id: CharacteristicType.java,v 1.13 2004/09/01 15:08:01 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.configuration.corba.CharacteristicType_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2004/09/01 15:08:01 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class CharacteristicType extends StorableObjectType {
	private int dataType;
	private boolean editable;
	private boolean visible;

	private StorableObjectDatabase characteristicTypeDatabase;

	public CharacteristicType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CharacteristicType(CharacteristicType_Transferable ctt) throws CreateObjectException {
		super(new Identifier(ctt.id),
					new Date(ctt.created),
					new Date(ctt.modified),
					new Identifier(ctt.creator_id),
					new Identifier(ctt.modifier_id),
					new String(ctt.codename),
					new String(ctt.description));
		this.dataType = ctt.data_type.value();
		this.editable = ctt.is_editable;
		this.visible = ctt.is_visible;		
	}
	
	protected CharacteristicType(Identifier id,
							Identifier creatorId,
							String codename,
							String description,
							int dataType,
							boolean editable,
							boolean visible){
					super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							codename,
							description);
					this.dataType = dataType;
					this.editable = editable;
					this.visible = visible;
					
					this.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
	}
	
	/**
	 * create new instance for client 
	 * @param id
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param dataType see {@link DataType} 
	 * @param editable
	 * @param visible
	 * @return
	 */
	public static CharacteristicType createInstance(Identifier id,
							Identifier creatorId,
							String codename,
							String description,
							int dataType,
							boolean editable,
							boolean visible){
		return new CharacteristicType(id,
									  creatorId,
									  codename,
									  description,
									  dataType,
									  editable,
									  visible);
	
	}
	
	
	public static CharacteristicType getInstance(CharacteristicType_Transferable ctt) throws CreateObjectException {
		CharacteristicType characteristicType = new CharacteristicType(ctt);
		
		characteristicType.characteristicTypeDatabase = ConfigurationDatabaseContext.characteristicTypeDatabase;
		try {
			if (characteristicType.characteristicTypeDatabase != null)
				characteristicType.characteristicTypeDatabase.insert(characteristicType);
		}
		catch (IllegalDataException ide) {
			throw new CreateObjectException(ide.getMessage(), ide);
		}
		
		return characteristicType;
	}

	public Object getTransferable() {
		return new CharacteristicType_Transferable((Identifier_Transferable)super.id.getTransferable(),
																							 super.created.getTime(),
																							 super.modified.getTime(),
																							 (Identifier_Transferable)super.creatorId.getTransferable(),
																							 (Identifier_Transferable)super.modifierId.getTransferable(),
																							 new String(super.codename),
																							 (super.description != null) ? (new String(super.description)) : "",
																							 DataType.from_int(this.dataType),
																							 this.editable,
																							 this.visible);
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}

	public boolean isEditable() {
		return this.editable;
	}

	public boolean isVisible() {
		return this.visible;
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						int dataType,
																						boolean editable,
																						boolean visible) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.dataType = dataType;
		this.editable = editable;
		this.visible = visible;
	}
}
