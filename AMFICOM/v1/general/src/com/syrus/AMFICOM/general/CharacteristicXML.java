/*-
* $Id: CharacteristicXML.java,v 1.3 2005/09/08 16:34:41 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.syrus.AMFICOM.general.CharacteristicWrapper.*;
/**
 * @version $Revision: 1.3 $, $Date: 2005/09/08 16:34:41 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class CharacteristicXML extends AbstractStorableObjectXML<Characteristic> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.CHARACTERISTIC_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] { COLUMN_TYPE_ID,
					COLUMN_NAME,
					COLUMN_DESCRIPTION,
					COLUMN_VALUE,
					COLUMN_CHARACTERIZABLE_ID,
					COLUMN_EDITABLE,
					COLUMN_VISIBLE };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}
	
	@Override
	public Characteristic getStorableObject(final Map<String, Object> objectMap) {
		Characteristic characteristic = 
			new Characteristic(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				(CharacteristicType)objectMap.get(StorableObjectWrapper.COLUMN_TYPE_ID),
				this.getString(objectMap, COLUMN_NAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				this.getString(objectMap, COLUMN_VALUE),
				this.getIdentifier(objectMap, COLUMN_CHARACTERIZABLE_ID),
				this.getBoolean(objectMap, COLUMN_EDITABLE).booleanValue(),
				this.getBoolean(objectMap, COLUMN_VISIBLE).booleanValue());
		return characteristic;
	}
	
}

