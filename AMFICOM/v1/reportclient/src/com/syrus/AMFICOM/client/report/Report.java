/*
 * $Id: Report.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Просто набор визуальных компонент - неважно реализованных в отчёт
 * или нет.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 10:23:10 $
 * @module reportclient_v1
 */
public class Report{
	private List<ReportComponent> components = new ArrayList<ReportComponent>();

	public List<ReportComponent> getComponents() {
		return this.components;
	}

	public void addComponent(ReportComponent component) {
		this.components.add(component);
	}
}
