/*
 * $Id: ElementsIntersectException.java,v 1.1 2005/10/13 08:50:48 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.StorableElement;

public final class ElementsIntersectException extends ReportException {
	private static final long serialVersionUID = 4750407906759566458L;
	StorableElement element1 = null;
	StorableElement element2 = null;	

	public ElementsIntersectException(
			StorableElement element1,
			StorableElement element2) {
		this.element1 = element1;
		this.element2 = element2;		
	}

	@Override
	public String getMessage() {
		StringBuffer resultMessage = new StringBuffer();
		resultMessage.append(I18N.getString("report.reportForTemplate"));
		resultMessage.append(" ");
		resultMessage.append(I18N.getString("report.Exception.cantImplement"));
		resultMessage.append(" (");
		resultMessage.append(I18N.getString("report.Exception.elementsIntersect"));
		resultMessage.append(").");
		return resultMessage.toString();
	}

	public Set<StorableElement> getIntersectingElements() {
		Set<StorableElement> result = new HashSet<StorableElement>();
		result.add(this.element1);
		result.add(this.element2);
		return result;
	}
}
