/*-
* $Id: DomainNetworkItem.java,v 1.1 2005/08/23 15:02:14 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/23 15:02:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface DomainNetworkItem {

	void setDomainId(final Identifier oldDomainId,
	                 final Identifier newDomainId);
	
}

