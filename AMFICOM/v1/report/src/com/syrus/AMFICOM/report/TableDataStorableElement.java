/*
 * $Id: TableDataStorableElement.java,v 1.3 2005/09/07 08:43:27 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.io.IOException;
import java.io.Serializable;
/**
 * Класс для отображения данных в табличном виде
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2005/09/07 08:43:27 $
 * @module report_v1
 */
public final class TableDataStorableElement extends DataStorableElement implements Serializable {
	private static final long serialVersionUID = -2699698026579054587L;
	/**
	 * Число вертикальных разбиений таблицы (применяется для длинных и узких
	 * таблиц)
	 */
	private int verticalDivisionsCount = 1;
	
	public TableDataStorableElement (
			String reportName,
			String modelClassName,
			int verticalDivisionsCount) {
		super(reportName,modelClassName);		
		this.verticalDivisionsCount = verticalDivisionsCount;
	}
	
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		super.writeObject(out);
		out.writeInt(this.verticalDivisionsCount);
	}

	public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		super.readObject(in);
		this.verticalDivisionsCount = in.readInt();
	}

	public int getVerticalDivisionsCount() {
		return this.verticalDivisionsCount;
	}

	public void setVerticalDivisionsCount(int verticalDivisionsCount) {
		this.verticalDivisionsCount = verticalDivisionsCount;
	}
}
