/*
 * $Id: ReportComponent.java,v 1.1 2005/08/11 11:17:34 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.general.report;

import com.syrus.AMFICOM.report.RenderingElement;

public interface ReportComponent {
	public abstract RenderingElement getElement();
}
