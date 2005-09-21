/*-
 * $Id: IdentifiableCell.java,v 1.1 2005/09/21 13:19:34 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;

public interface IdentifiableCell extends Identifiable {
	void setId(Identifier id);
}
