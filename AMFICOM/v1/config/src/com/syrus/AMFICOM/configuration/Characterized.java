package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identified;

import java.util.List;

public interface Characterized extends Identified {

	List getCharacteristicIds();

	void setCharacteristicIds(List characteristic_ids);
}
