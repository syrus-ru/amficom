/*-
 * $Id: FiltrableIconedNode.java,v 1.1 2005/06/22 07:30:17 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.filter.UI.Filtrable;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.newFilter.Filter;

/**
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/06/22 07:30:17 $
 * @module commonclient_v1
 */

public class FiltrableIconedNode extends PopulatableIconedNode implements Filtrable {
	private StorableObjectCondition condition;
	private CompoundConditionSort defaultOperation = CompoundConditionSort.AND;
	private Filter filter;
	
	public void setDefaultCondition(StorableObjectCondition condition) {
		this.condition = condition;		
	}

	public StorableObjectCondition getDefaultCondition() {
		return this.condition;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Filter getFilter() {
		return this.filter;
	}
	
	public void setDefaultOperation(CompoundConditionSort sort) {
		this.defaultOperation = sort;
	}
	
	public StorableObjectCondition getResultingCondition() throws CreateObjectException {
		if (this.condition != null && this.filter != null && this.filter.getCondition() != null) {
			return new CompoundCondition(this.condition, this.defaultOperation, this.filter.getCondition());
		} else if (this.condition != null) {
			return this.condition;
		} else if (this.filter != null && this.filter.getCondition() != null) {
			return this.filter.getCondition();
		}
		throw new IllegalStateException("Object badly initialized");
	}
}
