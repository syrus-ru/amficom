/**
 * $Id: MapToolTip.java,v 1.2 2005/02/10 11:48:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.UI.MultiRowToolTipUI;

import javax.swing.JToolTip;

/**
 * Класс $RCSfile: MapToolTip.java,v $ используется для отображения всплывающей подсказки в 
 * несколько строк
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/02/10 11:48:40 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapToolTip extends JToolTip 
{
	MultiRowToolTipUI toolTipUI = new MultiRowToolTipUI();
	
	public MapToolTip()
	{
		super();
		setUI(this.toolTipUI);
	}
}
