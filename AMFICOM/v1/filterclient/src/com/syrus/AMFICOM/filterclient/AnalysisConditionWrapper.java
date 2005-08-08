/*
 * $Id: AnalysisConditionWrapper.java,v 1.4 2005/08/08 11:41:00 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.filterclient;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:41:00 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class AnalysisConditionWrapper {
	
	private static short entityCode = ObjectEntities.ANALYSIS_CODE;
	
	private static final String MEASUREMENT = "filter by measurement";
	
	private static String[] keys = {MEASUREMENT};
	private static String[] keyNames = {MEASUREMENT};
	private static byte[] keyTypes = {ConditionWrapper.LIST};
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
}
