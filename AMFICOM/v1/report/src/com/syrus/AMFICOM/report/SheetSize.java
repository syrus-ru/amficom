/*-
 * $Id: SheetSize.java,v 1.1 2005/09/30 12:34:07 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.resource.IntDimension;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/09/30 12:34:07 $
 * @module report
 */
public enum SheetSize {
	A0(new IntDimension (3360, 4760)),
	A1(new IntDimension (3360, 2380)),
	A2(new IntDimension (1680, 2380)),
	A3(new IntDimension (1680, 1190)),
	A4(new IntDimension (840, 1190));
	
	private IntDimension size;
	
	private SheetSize(IntDimension size) {
		this.size = size;
	}
	
	public IntDimension getSize() {
		return this.size;
	}
}
