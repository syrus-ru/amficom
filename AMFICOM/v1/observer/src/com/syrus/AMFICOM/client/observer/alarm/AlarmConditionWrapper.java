/**
 * $Id: AlarmConditionWrapper.java,v 1.1 2005/10/26 13:51:56 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.observer.alarm;

import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

public class AlarmConditionWrapper implements ConditionWrapper {

	public List<ConditionKey> getKeys() {
		return null;
	}

	public short getEntityCode() {
		return 0;
	}

}
