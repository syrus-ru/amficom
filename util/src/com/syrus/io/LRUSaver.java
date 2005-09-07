/*-
* $Id: LRUSaver.java,v 1.1 2005/09/07 13:00:29 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.io;

import java.util.Set;

import com.syrus.util.LRUMap;


/**
 * @version $Revision: 1.1 $, $Date: 2005/09/07 13:00:29 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module util
 */
public interface LRUSaver<K, V> {

	void save(final LRUMap<K, V> lruMap, 
	          final String objectEntityName, 
	          final boolean cleanLRUMap);
	
	Set<V> load(final String objectEntityName);
}

