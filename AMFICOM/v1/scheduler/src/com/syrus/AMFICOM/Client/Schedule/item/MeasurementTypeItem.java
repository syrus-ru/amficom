/*
* $Id: MeasurementTypeItem.java,v 1.5 2005/03/29 15:48:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Populatable;
import com.syrus.AMFICOM.measurement.MeasurementType;


/**
 * @version $Revision: 1.5 $, $Date: 2005/03/29 15:48:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MeasurementTypeItem extends ElementItem implements Populatable {

	private ChildrenFactory childrenFactory;
	
	private boolean populatedChildren;
	
	public MeasurementTypeItem(MeasurementType measurementType) {
		super(measurementType);
		this.populatedChildren = false;
	}
	
	protected Class getChildenClass() {
		return KIS.class;
	}
	
	public String getName() {
		return ((MeasurementType)super.object).getDescription();
	}	
	
	public boolean canHaveParent() {
		return true;
	}
	
	public boolean canHaveChildren() {
		return true;
	}
	
	public void populate() {
		if (!this.populatedChildren) {
			this.childrenFactory.populate(this);
			this.populatedChildren = true;
		}
	}
	
	public void setChildrenFactory(ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}
	
	
}
