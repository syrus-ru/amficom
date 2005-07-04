/*-
 * $Id: SchemeCableLinkFactory.java,v 1.6 2005/07/04 13:00:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeCableLink;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/04 13:00:50 $
 * @module scheme_v1
 */
final class SchemeCableLinkFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlSchemeCableLink[] allocateArrayOfTransferables(final int length) {
		return new IdlSchemeCableLink[length];
	}
}
