/*
 * $Id: ActionType.java,v 1.6 2004/11/16 15:48:44 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Date;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2004/11/16 15:48:44 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public abstract class ActionType extends StorableObjectType {
	static final long serialVersionUID = 8566361712792210504L;

	public ActionType(Identifier id) {
		super(id);
	}

	
	public ActionType(StorableObject_Transferable transferable,
					  String codename,
					  String description) {
		super(transferable,
			  codename,
			  description);
	}

	public ActionType(Identifier id,	
					  Date created,
					  Date modified,
					  Identifier creatorId,
					  Identifier modifierId,
					  String codename,
					  String description) {
		super(id,
			  created,
			  modified,
			  creatorId,
			  modifierId,
			  codename,
			  description);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
	}
}
