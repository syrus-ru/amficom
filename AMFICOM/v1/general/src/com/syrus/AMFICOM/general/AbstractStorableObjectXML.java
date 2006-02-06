/*-
* $Id: AbstractStorableObjectXML.java,v 1.4 2005/09/09 17:17:22 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @version $Revision: 1.4 $, $Date: 2005/09/09 17:17:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
public abstract class AbstractStorableObjectXML<T extends StorableObject> {

	public final List<String> getKeys() {
		List<String> keys = new ArrayList<String>();
		keys.add(StorableObjectWrapper.COLUMN_ID);
		keys.add(StorableObjectWrapper.COLUMN_CREATED);
		keys.add(StorableObjectWrapper.COLUMN_MODIFIED);
		keys.add(StorableObjectWrapper.COLUMN_CREATOR_ID);
		keys.add(StorableObjectWrapper.COLUMN_MODIFIER_ID);
		keys.add(StorableObjectWrapper.COLUMN_VERSION);
		keys.addAll(this.getKeysTmpl());
		return keys;
	}
	
	public abstract short getEntityCode();
	
	protected abstract List<String> getKeysTmpl();
	
	public abstract T getStorableObject(final Map<String, Object> objectMap);

	protected final Boolean getBoolean(final Map<String, Object> objectMap, 
                                 final String key) {
		return (Boolean) objectMap.get(key);
	}
	
	protected final BigInteger getBigInteger(final Map<String, Object> objectMap, 
	                                   final String key) {
  		return (BigInteger) objectMap.get(key);
  	}

	protected final Date getDate(final Map<String, Object> objectMap, 
                                 final String key) {
		return (Date) objectMap.get(key);
	}	
	
	protected final Integer getInteger(final Map<String, Object> objectMap, 
	                                   final String key) {
		return (Integer) objectMap.get(key);
	}
	
	protected final Long getLong(final Map<String, Object> objectMap, 
	                                   final String key) {
		return (Long) objectMap.get(key);
	}
	
	protected final Short getShort(final Map<String, Object> objectMap, 
	                                 final String key) {
		return (Short) objectMap.get(key);
	}
	
	protected final String getString(final Map<String, Object> objectMap, 
                                 final String key) {
		return (String) objectMap.get(key);
	}
	
	protected final DataType getDataType(final Map<String, Object> objectMap, 
	                                         final String key) {
		return (DataType) objectMap.get(key);
	}
	
	protected final Identifier getIdentifier(final Map<String, Object> objectMap, 
	                                         final String key) {
		return (Identifier) objectMap.get(key);
	}	
	
	protected final StorableObjectVersion getVersion(final Map<String, Object> objectMap, 
                                 final String key) {
		return (StorableObjectVersion) objectMap.get(key);
	}

}

