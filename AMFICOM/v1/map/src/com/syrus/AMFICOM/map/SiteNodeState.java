/*-
 * $Id: SiteNodeState.java,v 1.6 2005/08/24 15:00:28 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * состояние узла
 *
 * @version $Revision: 1.6 $, $Date: 2005/08/24 15:00:28 $
 * @module map
 * @author $Author: bass $
 */
public class SiteNodeState extends NodeState {
	public Identifier mapProtoId;

	public SiteNodeState(final SiteNode msne) {
		super(msne);

		this.mapProtoId = msne.getType().getId();
	}

	@Override
	public boolean equals(final Object obj) {
		final SiteNodeState msnes = (SiteNodeState) obj;
		return super.equals(obj) && this.mapProtoId.equals(msnes.mapProtoId);
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.mapProtoId);
		return codeGenerator.getResult();
	}
}
