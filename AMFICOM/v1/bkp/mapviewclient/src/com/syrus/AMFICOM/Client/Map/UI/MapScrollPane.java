/**
 * $Id: MapScrollPane.java,v 1.7 2005/02/22 14:45:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.NetMapViewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Класс выполняет функции прокрутки в окне обозревателя карты с помощью
 * объектов JScrollBar
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/02/22 14:45:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapScrollPane extends JPanel
{
	NetMapViewer viewer;

	public MapScrollPane(NetMapViewer viewer)
	{
		super();

		this.viewer = viewer;

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit()
	{
//		this.setLayout(new BorderLayout());
//		if(this.viewer.getComponent() != null)
//			this.add(this.viewer.getComponent());
//		else
//		if(this.viewer.getJComponent() != null)
//			this.add(this.viewer.getJComponent());
	}

}
