/*-
 * $Id: IconPopulatableItem.java,v 1.1 2005/03/31 15:53:04 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import javax.swing.Icon;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/31 15:53:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public class IconPopulatableItem extends PopulatableItem {

	protected Icon	icon;

	public Icon getIcon() {
		return this.icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

}
