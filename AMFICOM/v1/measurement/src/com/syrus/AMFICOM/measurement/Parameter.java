/*
 * $Id: Parameter.java,v 1.21 2005/10/30 15:20:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;


import java.io.IOException;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.21 $, $Date: 2005/10/30 15:20:39 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class Parameter implements TransferableObject<IdlParameter>,
		Identifiable {
	private static final long serialVersionUID = -5102988777073070109L;

	private Identifier id;
	private ParameterType type;
	private byte[] value;	

	public static final String ID_TYPE = "type";

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Parameter(final IdlParameter pt) {
		this.id = new Identifier(pt.id);
		this.type = ParameterType.fromTransferable(pt.type);
		this.value = new byte[pt.value.length];
		System.arraycopy(pt.value, 0, this.value, 0, this.value.length);

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected Parameter(final Identifier id, final ParameterType type, final byte[] value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public static Parameter createInstance(final ParameterType type, final byte[] value) throws CreateObjectException {
		try {
			final Parameter setParameter = new Parameter(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PARAMETER_CODE),
					type,
					value);
			assert setParameter.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			return setParameter;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IdlParameter getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final byte[] ptValue = new byte[this.value.length];
		System.arraycopy(this.value, 0, ptValue, 0, ptValue.length);
		return new IdlParameter(this.id.getTransferable(), this.type.getTransferable(orb), ptValue);
	}
	
	/**
	 * <p>
	 * <b>Clients must never explicitly call this method.</b>
	 * </p>
	 */
	protected boolean isValid() {
		return this.id != null && this.type != null && this.value != null;
	}

	public Identifier getId() {
		return this.id;
	}

	public ParameterType getType() {
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
//			if ((this.equals(setParameter))
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
		final ByteArray byteArray = new ByteArray(this.value);
		final DataType dataType = this.type.getDataType();
		switch (dataType) {
			case INTEGER:
				try {
					return Integer.toString(byteArray.toInt());
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
			case DOUBLE:
				try {
					return Double.toString(byteArray.toDouble());
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
			case STRING:
				try {
					return byteArray.toUTFString();
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
			case DATE:
				try {
					return byteArray.toUTFString();
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
			case LONG:
				try {
					return Long.toString(byteArray.toLong());
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
			case RAW:
				return "Array of " + byteArray.getLength() + " elements";
			case BOOLEAN:
				try {
					return LangModelMeasurement.getString(byteArray.toBoolean() ? "on" : "off");
				} catch (IOException ioe) {
					// Never
					assert Log.errorMessage(ioe);
				}
				break;
		}
		return null;
	}

	public static byte[] getValueForType(final Parameter[] parameters, final ParameterType parameterType)
			throws ObjectNotFoundException {
		for (final Parameter parameter : parameters) {
			if (parameter.getType().equals(parameterType)) {
				return parameter.getValue();
			}
		}
		throw new ObjectNotFoundException("Parameter.getValueByTypeCodename | cannot find set parameter for type codename '"
				+ parameterType.getCodename() + '\'');
	}

	/**
	 * @deprecated Use @link getValueForType
	 * @param parameters
	 * @param keyCodename
	 * @throws ObjectNotFoundException
	 */
	@Deprecated
	public static byte[] getValueByTypeCodename(final Parameter[] parameters, final String keyCodename)
			throws ObjectNotFoundException {
		for (final Parameter parameter : parameters) {
			final ParameterType parameterType = parameter.getType();
			if (parameterType.getCodename().equals(keyCodename)) {
				return parameter.getValue();
			}
		}
		throw new ObjectNotFoundException("Parameter.getValueByTypeCodename | cannot find set parameter for type codename '"
				+ keyCodename + '\'');
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(final Object that) {
		return this.id.equals(that);
	}
}
