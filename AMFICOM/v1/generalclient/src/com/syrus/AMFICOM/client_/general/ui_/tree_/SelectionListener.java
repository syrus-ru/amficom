/*
* $Id: SelectionListener.java,v 1.1 2005/03/23 10:19:14 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.util.Collection;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/23 10:19:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public interface SelectionListener {

	void selectedItems(Collection items);
}
