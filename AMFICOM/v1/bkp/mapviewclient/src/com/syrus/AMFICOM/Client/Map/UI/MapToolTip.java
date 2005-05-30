/**
 * $Id: MapToolTip.java,v 1.3 2005/05/30 12:19:02 krupenn Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.Client.Map.UI;

import javax.swing.JToolTip;

import com.syrus.AMFICOM.client.UI.MultiRowToolTipUI;

/**
 * Класс используется для отображения всплывающей
 * подсказки в несколько строк
 * 
 * @version $Revision: 1.3 $, $Date: 2005/05/30 12:19:02 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapToolTip extends JToolTip {
	MultiRowToolTipUI toolTipUI = new MultiRowToolTipUI();

	public MapToolTip() {
		super();
		setUI(this.toolTipUI);
	}
}
