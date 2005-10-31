/*-
* $Id: IdlLayoutItemImpl.java,v 1.4 2005/10/31 12:29:57 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource.corba;

import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/10/31 12:29:57 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module resource
 */
public class IdlLayoutItemImpl extends IdlLayoutItem {

	IdlLayoutItemImpl() {
		// empty
	}

	IdlLayoutItemImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final IdlIdentifier parentId,
			final String layoutName,
			final String name) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.parentId = parentId;
		this.layoutName = layoutName;
		this.name = name;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public LayoutItem getNative() throws IdlCreateObjectException {
		try {
			return new LayoutItem(this);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new IdlCreateObjectException(coe.getMessage());
		}
	}
}

