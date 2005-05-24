/*
 * $Id: AnalysisConditionWrapper.java,v 1.2 2005/05/24 13:45:42 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/24 13:45:42 $
 * @author $Author: max $
 * @module filterclient_v1
 */
public class AnalysisConditionWrapper {
	
	private static short entityCode = ObjectEntities.ANALYSIS_ENTITY_CODE;
	
	private static final String MEASUREMENT = "filter by measurement";
	
	private static String[] keys = {MEASUREMENT};
	private static String[] keyNames = {MEASUREMENT};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
