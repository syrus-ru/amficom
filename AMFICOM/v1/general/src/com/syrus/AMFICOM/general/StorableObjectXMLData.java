/*-
* $Id: StorableObjectXMLData.java,v 1.8 2006/06/06 11:28:05 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * @version $Revision: 1.8 $, $Date: 2006/06/06 11:28:05 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
public abstract class StorableObjectXMLData {

	public final Object getObject(final String className, final String value) throws IllegalDataException {
		Object object = null;
		if (className.equals(StorableObject.class.getName())) {
			final Identifier id = value != null ? Identifier.valueOf(value) : VOID_IDENTIFIER;
			try {
				object = StorableObjectPool.getStorableObject(id, true);
			} catch (ApplicationException e) {
				throw new IllegalDataException("StorableObjectXMLData.getObject | Caught ApplicationException " + e.getMessage());
			}
		} else if (className.equals(Identifier.class.getName())) {
			object = value != null ? Identifier.valueOf(value) : VOID_IDENTIFIER;
		} else if (className.equals(StorableObjectVersion.class.getName())) {
			object = StorableObjectVersion.valueOf(Long.parseLong(value));
		} else if (className.equals(DataType.class.getName())) {
			object = DataType.valueOf(Integer.parseInt(value));
		} else if (className.equals(Date.class.getName())) {
			object = new Date(Long.parseLong(value));
		} else if (className.equals(BigInteger.class.getName())) {
			final BigInteger bigInteger = new BigInteger(value);
			object = bigInteger;
		} else if (className.equals(Short.class.getName())) {
			final Short short1 = Short.valueOf(value);
			object = short1;
		} else if (className.equals(Integer.class.getName())) {
			final Integer integer = Integer.valueOf(value);
			object = integer;
		} else if (className.equals(Long.class.getName())) {
			Long long1 = Long.valueOf(value);
			object = long1;
		} else if (className.equals(Float.class.getName())) {
			final Float float1 = Float.valueOf(value);
			object = float1;
		} else if (className.equals(Double.class.getName())) {
			final Double double1 = Double.valueOf(value);
			object = double1;
		} else if (className.equals(String.class.getName())) {
			object = value != null ? value : "";
		} else if (className.equals(Boolean.class.getName())) {
			object = Boolean.valueOf(value);
		} else if (className.equals(byte[].class.getName())) {
			/* if value is null, array is empty */
			if (value != null) {
				byte[] bs = new byte[value.length() / 2];
				for (int j = 0; j < bs.length; j++)
					bs[j] = (byte) ((char) Integer.parseInt(value.substring(j << 1, (j + 1) << 1), 16));
				object = bs;
			} else {
				object = new byte[0];
			}
		} else {
			object = value;
		}
		return object;
	}

	public final String getClassName(final Object object) {
		String className = object.getClass().getName();
		if (object instanceof StorableObject) {
			className = StorableObject.class.getName();
		} else if (object instanceof Collection) {
			className = Collection.class.getName();
		} else if (object instanceof Map) {
			className = Map.class.getName();
		} else if (object instanceof Enum) {
			className = Integer.class.getName();
		}
		return className;
	}

	public String getValue(final Object object) {
		String value = null;
		if (object == null) {
			return value;
		}
		if (object instanceof StorableObject) {
			final StorableObject storableObject = (StorableObject) object;
			value = storableObject.getId().getIdentifierString();
		} else if (object instanceof Identifier) {
			final Identifier id = (Identifier) object;
			value = !id.isVoid() ? id.getIdentifierString() : null;
		} else if (object instanceof StorableObjectVersion) {
			final StorableObjectVersion version = (StorableObjectVersion) object;
			value = Long.toString(version.longValue());
		} else if (object instanceof DataType) {
			final DataType dataType = (DataType) object;
			value = Integer.toString(dataType.ordinal());
		} else if (object instanceof Date) {
			final Date date = (Date) object;
			value = Long.toString(date.getTime());
		} else if (object instanceof byte[]) {
			final byte[] bs = (byte[]) object;
			final StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < bs.length; j++) {
				final String s = Integer.toString(bs[j] & 0xFF, 16);
				if (s.length() == 1) {
					buffer.append('0');
				}
				buffer.append(s);
			}
			value = buffer.toString();
		} else if (object instanceof Enum) {
			value = Integer.toString(((Enum)object).ordinal());
		} else {
			value = object.toString();
		}
		return value;
	}
}

