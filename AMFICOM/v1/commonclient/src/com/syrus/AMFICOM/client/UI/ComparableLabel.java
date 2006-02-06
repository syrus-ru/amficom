/*-
* $Id: ComparableLabel.java,v 1.1 2005/09/22 15:24:36 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import javax.swing.JLabel;


/**
 * @version $Revision: 1.1 $, $Date: 2005/09/22 15:24:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class ComparableLabel extends JLabel implements Comparable<ComparableLabel> {

	private static final long	serialVersionUID	= 3546642105390084920L;

	public ComparableLabel(final String string) {
		super(string);
	}

	public int compareTo(ComparableLabel otherComparableLabel) {
		return otherComparableLabel.getText().compareTo(this.getText());
	}
}

