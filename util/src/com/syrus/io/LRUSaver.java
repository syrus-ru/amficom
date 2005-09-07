/*-
* $Id: LRUSaver.java,v 1.2 2005/09/07 14:07:07 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.io;

import java.util.Set;

import com.syrus.util.LRUMap;


/**
 * @version $Revision: 1.2 $, $Date: 2005/09/07 14:07:07 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module util
 */
public interface LRUSaver<K, V> {

	void save(final LRUMap<K, V> lruMap, final String objectEntityName, final boolean cleanLRUMap);

	Set<V> load(final String objectEntityName);
}

