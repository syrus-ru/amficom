/*
 * $Id: SetParameter.java,v 1.13 2004/12/08 10:24:09 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;


import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.util.HashCodeGenerator;

/**
 * @version $Revision: 1.13 $, $Date: 2004/12/08 10:24:09 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class SetParameter implements TransferableObject, TypedObject {
	private Identifier id;
	private ParameterType type;
	private byte[] value;	
	
	public static final String ID_TYPE = "type";

	public SetParameter(Parameter_Transferable pt) throws DatabaseException, CommunicationException{
		this.id = new Identifier(pt.id);
		this.type = (ParameterType)MeasurementStorableObjectPool.getStorableObject(new Identifier(pt.type_id), true);
		this.value = new byte[pt.value.length];
		for (int i = 0; i < this.value.length; i++)
			this.value[i] = pt.value[i];
	}

	public SetParameter(Identifier id,
						ParameterType type,
						byte[] value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}
	
//	public SetParameter(Identifier id,
//											String codename,
//											byte[] value) throws RetrieveObjectException, ObjectNotFoundException {
//		this.id = id;
//		this.type = ParameterTypeDatabase.retrieveForCodename(codename);
//		this.value = value;
//	}

	public Object getTransferable() {
		byte[] ptValue = new byte[this.value.length];
		for (int i = 0; i < ptValue.length; i++)
			ptValue[i] = this.value[i];
		return new Parameter_Transferable((Identifier_Transferable)this.id.getTransferable(),
																			(Identifier_Transferable)this.type.getId().getTransferable(),
																			ptValue,
																			new String(this.type.getCodename()));
	}

	public Identifier getId() {
		return this.id;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public byte[] getValue() {
		return this.value;
	}
	
	public boolean equals(Object obj) {
		boolean equals = (obj==this);
		if ((!equals)&&(obj instanceof SetParameter)){
			SetParameter setParameter = (SetParameter)obj;
			if ((this.id.equals(setParameter.id))&&
				(this.type.equals(setParameter.type)) &&
				HashCodeGenerator.equalsArray(this.value, setParameter.value))
				 equals = true;
		}
		return equals;
	}
	
	
	public int hashCode() {
		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
		hashCodeGenerator.addObject(this.id);
		hashCodeGenerator.addObject(this.type);
		hashCodeGenerator.addByteArray(this.value);
		int result = hashCodeGenerator.getResult();
		hashCodeGenerator = null;
		return result;

	}	
	
	
	public String toString() {
		String str = getClass().getName() + EOSL
					+ ID + this.id.toString() + EOSL
					+ ID_TYPE + KEY_VALUE_SEPERATOR 
					+ OPEN_BLOCK
					+ this.type.toString()
					+ CLOSE_BLOCK;				
					
		return str;
	}
	
}
