package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identified;

import java.util.ArrayList;

public interface Characterized extends Identified {

	public ArrayList getCharacteristicIds();

	public void setCharacteristicIds(ArrayList characteristic_ids);
}