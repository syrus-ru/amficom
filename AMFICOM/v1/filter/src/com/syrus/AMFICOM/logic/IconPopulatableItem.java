/*-
 * $Id: IconPopulatableItem.java,v 1.3 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import javax.swing.Icon;


/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:37:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
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
