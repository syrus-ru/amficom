/*-
 * $Id: SheetSize.java,v 1.2 2006/04/25 10:58:08 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2006/04/25 10:58:08 $
 * @module report
 * A4 is 11?" x 8?" multiply it on 96 dpi resolution
 */
public enum SheetSize {
	A0(new IntDimension (3168, 4512)),
	A1(new IntDimension (3168, 2256)),
	A2(new IntDimension (1584, 2256)),
	A3(new IntDimension (1584, 1128)),
	A4(new IntDimension (792, 1128));
	
	private IntDimension size;
	
	private SheetSize(IntDimension size) {
		this.size = size;
	}
	
	public IntDimension getSize() {
		return this.size;
	}
}
