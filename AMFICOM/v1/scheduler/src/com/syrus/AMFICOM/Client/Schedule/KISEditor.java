/*
 * $Id: KISEditor.java,v 1.3 2005/03/15 11:42:11 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule;

import java.util.Collection;

import com.syrus.AMFICOM.configuration.KIS;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/15 11:42:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public interface KISEditor {

	void setKIS(KIS kis);

	KIS getKIS();
}
