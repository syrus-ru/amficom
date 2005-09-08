/*-
* $Id: LRUSaver.java,v 1.3 2005/09/08 05:35:41 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.io;

import java.util.Set;

import com.syrus.util.LRUMap;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/08 05:35:41 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module util
 */
public interface LRUSaver<K, V> {

//	 Арсений, заебал своими COSMETICs и менять постоянно code style
	void save(final LRUMap<K, V> lruMap, 
	          final String objectEntityName, 
	          final boolean cleanLRUMap);

	Set<V> load(final String objectEntityName);
}

