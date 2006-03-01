/*-
 * $Id: Parameter.java,v 1.24.2.3 2006/03/01 10:19:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.Date;

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

/**
 * @version $Revision: 1.24.2.3 $, $Date: 2006/03/01 10:19:54 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class Parameter<T extends Parameter<T>> extends StorableObject<T> {
	private byte[] value;

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
			this.fromTransferable(idlStorableObject);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlParameter idlParameter = (IdlParameter) transferable;
		super.fromTransferable(transferable);
		this.value = idlParameter.value;
	}

	public final byte[] getValue() {
		return this.value;
	}

	public final void setValue(final byte[] value) {
		this.value = value;
		super.markAsChanged();
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
		final ByteArray byteArrayValue = new ByteArray(this.value);
		final ParameterType parameterType = StorableObjectPool.getStorableObject(this.getTypeId(), true);
		final DataType dataType = parameterType.getDataType();
		switch (dataType) {
			case INTEGER:
				return Integer.toString(byteArrayValue.toInt());
			case DOUBLE:
				return Double.toString(byteArrayValue.toDouble());
			case STRING:
				return byteArrayValue.toUTFString();
			case DATE:
				return byteArrayValue.toDate().toString();
			case LONG:
				return Long.toString(byteArrayValue.toLong());
			case BOOLEAN:
				return Boolean.toString(byteArrayValue.toBoolean());
			case RAW:
				return String.valueOf(this.value);
			default:
				Log.errorMessage("Illegal data type: " + dataType);
				return String.valueOf(this.value);
		}
	}
}
