/*
* $Id: MeasurementTypeItem.java,v 1.6 2005/03/30 14:26:20 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Populatable;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;


/**
 * @version $Revision: 1.6 $, $Date: 2005/03/30 14:26:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MeasurementTypeItem extends ElementItem implements Populatable {

	private ChildrenFactory childrenFactory;
	
	private boolean populatedChildren;
	
	public MeasurementTypeItem(Identifier measurementTypeId) {
		super(measurementTypeId);
		this.populatedChildren = false;
	}
	
	protected short getChildenEntityCode() {
		return ObjectEntities.KIS_ENTITY_CODE;
	}
	
	public String getName() {
		try {
			MeasurementType measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(super.identifier, true);
			return  measurementType.getDescription();
		} catch (ApplicationException e) {
			// nothing
		}
		return null;
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
	
	public List getChildren() {		
		return super.getChildren();
	}
	
	public void setChildrenFactory(ChildrenFactory childrenFactory) {
		this.childrenFactory = childrenFactory;
	}
	
	
}
