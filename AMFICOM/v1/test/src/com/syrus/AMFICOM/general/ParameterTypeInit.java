/*
 * $Id: ParameterTypeInit.java,v 1.1 2005/05/06 16:07:51 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/06 16:07:51 $
 * @author $Author: bob $
 * @module tools
 */
public class ParameterTypeInit extends GeneralTestCase {

	public ParameterTypeInit(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = ParameterTypeInit.class;
		junit.awtui.TestRunner.run(clazz);
		// junit.swingui.TestRunner.run(clazz);
		// junit.textui.TestRunner.run(clazz);

	}

	public static Test suite() {
		return suiteWrapper(ParameterTypeInit.class);
	}

	public void testCreateCharacteristics() throws ApplicationException {

		
		{	
			

			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.DADARA_CRITERIA, "Dadara criteria parameter type", "Dadara criteria",
				DataType.DATA_TYPE_RAW);
			
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}

		if (false){	
			

			ParameterType parameterType = ParameterType.createInstance(creatorId, ParameterTypeCodenames.REFLECTOGRAMMA, "Reflectoramm parameter type", "Reflectoramm",
				DataType.DATA_TYPE_RAW);
			
			GeneralStorableObjectPool.putStorableObject(parameterType);
		}

	}
}
