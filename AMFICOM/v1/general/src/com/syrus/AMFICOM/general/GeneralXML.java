/*
 * $Id: GeneralXML.java,v 1.1 2005/01/24 15:29:27 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 15:29:27 $
 * @author $Author: bob $
 * @module general_v1
 */
public class GeneralXML extends StorableObjectXML {

	public GeneralXML(StorableObjectXMLDriver driver) {
		super(driver);
	}

	protected Class getStorableObjectClass(short entityCode) {
		Class clazz = null;
		switch (entityCode) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				clazz = Characteristic.class;
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				clazz = CharacteristicType.class;
				break;
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				clazz = ParameterType.class;
				break;
		}
		return clazz;
	}

	protected Wrapper getWrapper(short entityCode) {
		Wrapper wrapper = null;
		switch (entityCode) {
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				wrapper = CharacteristicWrapper.getInstance();
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				wrapper = CharacteristicTypeWrapper.getInstance();
				break;
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				wrapper = ParameterTypeWrapper.getInstance();
				break;
		}
		return wrapper;
	}	

	

}
