/*-
 * $Id: SheetSize.java,v 1.3 2006/04/26 13:00:20 stas Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.resource.IntDimension;

/**
 * @author Maxim Selivanov
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2006/04/26 13:00:20 $
 * @module report
 * A4 is 297mm x 210mm multiply it on 96 dpi resolution and 2.54 mm per inch
 */
public enum SheetSize {
	A0(new IntDimension (3168, 4488)),
	A1(new IntDimension (3168, 2244)),
	A2(new IntDimension (1584, 2244)),
	A3(new IntDimension (1584, 1122)),
	A4(new IntDimension (792, 1122));
	
	private IntDimension size;
	
	private SheetSize(IntDimension size) {
		this.size = size;
	}
	
	public IntDimension getSize() {
		return this.size;
	}
}
