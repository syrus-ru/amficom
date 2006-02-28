/*
 * $Id: AnalysisConditionWrapper.java,v 1.5 2006/02/28 15:20:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;

import com.syrus.AMFICOM.general.ConditionWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2006/02/28 15:20:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class AnalysisConditionWrapper {
	
	private static short entityCode = ANALYSIS_CODE;
	
	private static final String MEASUREMENT = "filter by measurement";
	
	private static String[] keys = {MEASUREMENT};
	private static String[] keyNames = {MEASUREMENT};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
