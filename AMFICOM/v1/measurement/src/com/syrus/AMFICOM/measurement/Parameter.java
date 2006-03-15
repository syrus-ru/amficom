/*-
 * $Id: Parameter.java,v 1.24.2.6 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlParameter;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;

/**
 * @version $Revision: 1.24.2.6 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class Parameter extends StorableObject {
	private byte[] value;

	private transient ByteArray byteArrayValue;

	Parameter(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final byte[] value) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.value = value;
	}

	Parameter(final IdlStorableObject idlStorableObject) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlStorableObject);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	@Override
	protected void fromIdlTransferable(final IdlStorableObject transferable) throws IdlConversionException {
		final IdlParameter idlParameter = (IdlParameter) transferable;
		super.fromIdlTransferable(transferable);
		this.value = idlParameter.value;
	}

	public final byte[] getValue() {
		return this.value;
	}

	public abstract Identifier getTypeId() throws ApplicationException;

	public abstract String getTypeCodename() throws ApplicationException;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final byte[] value) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.value = value;
	}

	@Override
	protected boolean isValid() {
		return super.isValid() && this.value != null;
	}

	public final String stringValue() throws ApplicationException, IOException {
		if (this.byteArrayValue == null) {
			this.byteArrayValue = new ByteArray(this.value);
		}
		final ParameterType parameterType = StorableObjectPool.getStorableObject(this.getTypeId(), true);
		final DataType dataType = parameterType.getDataType();
		switch (dataType) {
			case INTEGER:
				return Integer.toString(this.byteArrayValue.toInt());
			case DOUBLE:
				return Double.toString(this.byteArrayValue.toDouble());
			case STRING:
				return this.byteArrayValue.toUTFString();
			case DATE:
				return this.byteArrayValue.toDate().toString();
			case LONG:
				return Long.toString(this.byteArrayValue.toLong());
			case BOOLEAN:
				return Boolean.toString(this.byteArrayValue.toBoolean());
			case RAW:
				return String.valueOf(this.value);
			default:
				Log.errorMessage("Illegal data type: " + dataType);
				return String.valueOf(this.value);
		}
	}

	/**
	 * Create Map codename-value
	 * @param parameters
	 * @return Unmodifiable Map<String codename, byte[] value>
	 * @throws ApplicationException
	 */
	public static Map<String, byte[]> getTypeCodenameValueMap(final Set<? extends Parameter> parameters) throws ApplicationException {
		assert parameters != null : NON_NULL_EXPECTED;

		if (parameters.isEmpty()) {
			return Collections.emptyMap();
		}

		final Map<String, byte[]> typeCodenameValueMap = new HashMap<String, byte[]>();
		for (final Parameter parameter: parameters) {
			typeCodenameValueMap.put(parameter.getTypeCodename(), parameter.getValue());
		}
		return Collections.unmodifiableMap(typeCodenameValueMap);
	}
}
