/*-
* $Id: CharacteristicTypeXML.java,v 1.4 2005/12/06 11:31:12 bass Exp $
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

import static com.syrus.AMFICOM.general.CharacteristicTypeWrapper.*;
/**
 * @version $Revision: 1.4 $, $Date: 2005/12/06 11:31:12 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class CharacteristicTypeXML extends AbstractStorableObjectXML<CharacteristicType> {

	private static List<String> keys;
	
	@Override
	public short getEntityCode() {
		return ObjectEntities.CHARACTERISTIC_TYPE_CODE;
	}
	
	@Override
	protected List<String> getKeysTmpl() {
		if (keys == null) {
			final String[] keysArray = new String[] {COLUMN_CODENAME, 
					COLUMN_DESCRIPTION, 
					COLUMN_NAME, 
					COLUMN_DATA_TYPE_CODE, 
					COLUMN_SORT };
			keys = Collections.unmodifiableList(Arrays.asList(keysArray));
		}
		return keys;
	}

	@Override
	public CharacteristicType getStorableObject(final Map<String, Object> objectMap) {
		CharacteristicType characteristicType = 
			new CharacteristicType(this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_ID),
				this.getIdentifier(objectMap, StorableObjectWrapper.COLUMN_CREATOR_ID),
				this.getVersion(objectMap, StorableObjectWrapper.COLUMN_VERSION),
				this.getString(objectMap, COLUMN_CODENAME),
				this.getString(objectMap, COLUMN_DESCRIPTION),
				this.getString(objectMap, COLUMN_NAME),
				this.getDataType(objectMap, COLUMN_DATA_TYPE_CODE),
				CharacteristicTypeSort.valueOf(this.getInteger(objectMap, COLUMN_SORT).intValue()));
		return characteristicType;
	}
	
}

