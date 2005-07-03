/*-
 * $Id: Filtrable.java,v 1.1 2005/06/22 07:30:17 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.newFilter.Filter;

/**
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/06/22 07:30:17 $
 * @module commonclient_v1
 */

public interface Filtrable {
	void setDefaultCondition(StorableObjectCondition condition);
	StorableObjectCondition getDefaultCondition();
	
	void setFilter(Filter filter);
	Filter getFilter(); 
}
