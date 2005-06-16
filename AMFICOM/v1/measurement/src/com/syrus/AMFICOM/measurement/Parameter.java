/*
 * $Id: Parameter.java,v 1.1 2005/06/16 10:34:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;


import java.io.IOException;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.resource.LangModelMeasurement;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/16 10:34:03 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class Parameter implements TransferableObject, TypedObject, Identifiable {
	private Identifier id;
	private ParameterType type;
	private byte[] value;	
	
	public static final String ID_TYPE = "type";

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Parameter(Parameter_Transferable pt) throws ApplicationException {
		this.id = new Identifier(pt.id);
		this.type = (ParameterType) StorableObjectPool.getStorableObject(new Identifier(pt.type_id), true);
		this.value = new byte[pt.value.length];
		for (int i = 0; i < this.value.length; i++)
			this.value[i] = pt.value[i];
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Parameter(Identifier id,
								ParameterType type,
								byte[] value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public static Parameter createInstance(ParameterType type, byte[] value) throws CreateObjectException {
		try {
			Parameter setParameter = new Parameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETER_ENTITY_CODE), type, value);
			assert setParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			return setParameter;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		byte[] ptValue = new byte[this.value.length];
		for (int i = 0; i < ptValue.length; i++)
			ptValue[i] = this.value[i];
		return new Parameter_Transferable((Identifier_Transferable) this.id.getTransferable(),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				ptValue);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return this.id != null && this.type != null && this.value != null;
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
//	
//	public boolean equals(Object obj) {
//		boolean equals = (obj == this);
//		if ((!equals) && (obj instanceof Parameter)) {
//			Parameter setParameter = (Parameter) obj;
//			if ((this.id.equals(setParameter.id))
//					&& (this.type.equals(setParameter.type))
//					&& HashCodeGenerator.equalsArray(this.value, setParameter.value))
//				equals = true;
//		}
//		return equals;
//	}
//	
//	
//	public int hashCode() {
//		HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
//		hashCodeGenerator.addObject(this.id);
//		hashCodeGenerator.addObject(this.type);
//		hashCodeGenerator.addByteArray(this.value);
//		int result = hashCodeGenerator.getResult();
//		hashCodeGenerator = null;
//		return result;
//
//	}	
//
//	public String toString() {
//		String str = getClass().getName() + EOSL
//					+ ID + this.id.toString() + EOSL
//					+ ID_TYPE + KEY_VALUE_SEPERATOR
//					+ OPEN_BLOCK
//					+ this.type.toString()
//					+ CLOSE_BLOCK;				
//					
//		return str;
//	}
	
	public String getStringValue() {
		String string = null;
		ByteArray byteArray = new ByteArray(this.value);
		DataType dataType = this.type.getDataType();
		switch (dataType.value()) {
			case DataType._DATA_TYPE_INTEGER:
				try {
					string = Integer.toString(byteArray.toInt());
				}
				catch (IOException ioe) {
					// Never
					Log.errorException(ioe);
				}
				break;
			case DataType._DATA_TYPE_DOUBLE:
				try {
					string = Double.toString(byteArray.toDouble());
				}
				catch (IOException ioe) {
					// Never
					Log.errorException(ioe);
				}
				break;
			case DataType._DATA_TYPE_STRING:
				try {
					string = byteArray.toUTFString();
				}
				catch (IOException ioe) {
					// Never
					Log.errorException(ioe);
				}
				break;
			case DataType._DATA_TYPE_LONG:
				try {
					string = Long.toString(byteArray.toLong());
				}
				catch (IOException ioe) {
					// Never
					Log.errorException(ioe);
				}
				break;
			case DataType._DATA_TYPE_BOOLEAN:
				try {
					string = LangModelMeasurement.getString(byteArray.toBoolean() ? "on" : "off");
				}
				catch (IOException ioe) {
					// Never
					Log.errorException(ioe);
				}
				break;
		}
		return string;
	}

	public static byte[] getValueByTypeCodename(Parameter[] params, String keyCodename) throws ObjectNotFoundException {
		for (int i = 0; i < params.length; i++) {
			ParameterType p = (ParameterType) params[i].getType();
			if (p.getCodename().equals(keyCodename))
				return params[i].getValue();
		}
		throw new ObjectNotFoundException("Parameter.getValueByTypeCodename | cannot find set parameter for type codename '"
				+ keyCodename + '\'');
	}
}
