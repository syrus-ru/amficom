/**
 * $Id: MapToolTip.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapToolTip extends JToolTip 
{
//	LogicalNetLayer lnl;
	MultiRowToolTipUI bttui = new MultiRowToolTipUI();
	
	public MapToolTip()
	{
		super();
		setUI(bttui);
	}

//	public MapToolTip(LogicalNetLayer lnl)
//	{
//		super();
//		this.lnl = lnl;
//		setUI(bttui);
//	}
}
