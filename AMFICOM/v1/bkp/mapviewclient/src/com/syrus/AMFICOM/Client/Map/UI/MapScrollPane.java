/**
 * $Id: MapScrollPane.java,v 1.5 2005/01/12 15:32:34 krupenn Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/01/12 15:32:34 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapScrollPane extends JPanel
{
	//Устанавливаем верхнюю, нижнюю границу
//	double horiz_left = -0.118503;
//	double horiz_right = 0.236697;

	//Устанавливаем левую, правую границу
//	double vert_left = 0.241678;
//	double vert_right = -0.060722;

//	double horiz_incr = 0;
//	double vert_incr = 0;

//	JScrollBar horScrollBar;
//	JScrollBar vertScrollBar;

	NetMapViewer viewer;

	//Флаг того что нужно обновить без срабатывания listener
//	boolean updateWithoutListen = false;


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
		this.setLayout(new BorderLayout());
		if(viewer.getComponent() != null)
			this.add(viewer.getComponent());
		else
		if(viewer.getJComponent() != null)
			this.add(viewer.getJComponent());
	}

}
