/**
 * $Id: MapScrollPane.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
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
import javax.swing.JScrollBar;

/**
 * Класс выполняет функции прокрутки в окне обозревателя карты с помощью
 * объектов JScrollBar
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapScrollPane extends JPanel
{
	//Устанавливаем верхнюю, нижнюю границу
	double horiz_left = -0.118503;
	double horiz_right = 0.236697;

	//Устанавливаем левую, правую границу
	double vert_left = 0.241678;
	double vert_right = -0.060722;

	double horiz_incr = 0;
	double vert_incr = 0;

	JScrollBar horScrollBar;
	JScrollBar vertScrollBar;

	NetMapViewer viewer;

	//Флаг того что нужно обновить без срабатывания listener
	boolean updateWithoutListen = false;


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
/*
	private void jbInit()
	{
		try
		{
			double a = viewer.getCenter().x;
			double b = viewer.getCenter().y;

			int horValue = (int)(( a - horiz_left) / (horiz_right - horiz_left) * 100);
			int vertValue = (int)(( b - vert_left) / (vert_right - vert_left) * 100);

			horScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, horValue, 0, 0, 100);
			vertScrollBar = new JScrollBar(JScrollBar.VERTICAL, vertValue, 0, 0, 100);
		}
		catch (Exception ex)
		{
			horScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 0, 100);
			vertScrollBar = new JScrollBar(JScrollBar.VERTICAL, 50, 0, 0, 100);
		}

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		this.setLayout(gridbag);

		horScrollBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		horScrollBar.setVisibleAmount(20);

		vertScrollBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		vertScrollBar.setVisibleAmount(20);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		c.gridx = 0;

		c.gridy = 0;
		gridbag.setConstraints(viewer, c);
		this.add(viewer.getComponent());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;

		c.gridy = 1;
		gridbag.setConstraints(horScrollBar, c);
		this.add(horScrollBar);

		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;

		c.gridy = 0;
		gridbag.setConstraints(vertScrollBar, c);
		this.add(vertScrollBar);
		// vertScrollBar.getModel().setExtent(0);
		vertScrollBar.addAdjustmentListener
		(
		new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				if ( updataWithoutListen == false)
				{
					if (horScrollBar.getValueIsAdjusting())
					{

						double a = viewer.getCenter().x;
						double b = viewer.getCenter().y;
						double new_lat = vert_left 
							+ (vert_right - vert_left)
								/ (vertScrollBar.getMaximum() 
									- vertScrollBar.getVisibleAmount())
								* vertScrollBar.getValue();
						viewer.setCenter(
							new Point2D.Double(a, new_lat));
					}
					else
					{
						double a = viewer.getCenter().x;
						double b = viewer.getCenter().y;

						double new_lat = vert_left 
							+ (vert_right - vert_left)
								/ (vertScrollBar.getMaximum() 
									- vertScrollBar.getVisibleAmount())
								* vertScrollBar.getValue();
						viewer.setCenter(
							new Point2D.Double(a, new_lat));
					}
				}
			}
		}
		);

		horScrollBar.addAdjustmentListener
		(
		new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				if (!updateWithoutListen)
				{
					if (horScrollBar.getValueIsAdjusting())
					{

						double a = viewer.getCenter().x;
						double b = viewer.getCenter().y;

						double new_long =  horiz_left 
							+ (horiz_right - horiz_left)
								/ (horScrollBar.getMaximum() 
									- horScrollBar.getVisibleAmount()) 
								* horScrollBar.getValue();
						viewer.setCenter(
							new Point2D.Double(new_long, b));
					}
					else
					{
						double a = viewer.getCenter().x;
						double b = viewer.getCenter().y;

						double new_long =  horiz_left 
							+ (horiz_right - horiz_left) 
							/ (horScrollBar.getMaximum() 
								- horScrollBar.getVisibleAmount())
							* horScrollBar.getValue();
						viewer.setCenter(
							new Point2D.Double(new_long, b));
					}
				}
			}
		}
		);

	}

//Обновить прокрутку без listener
	public void updateScroll()
	{
		updateWithoutListen = true;
		double a = viewer.getCenter().x;
		double b = viewer.getCenter().y;

		double hor =  (horiz_right - horiz_left)/(a - horiz_left);
		double ver =  (vert_right - vert_left)/(b - vert_left);

		horScrollBar.setValue( (int) ((horScrollBar.getMaximum() - horScrollBar.getVisibleAmount()) /hor));
		vertScrollBar.setValue( (int) ((vertScrollBar.getMaximum() - vertScrollBar.getVisibleAmount()) /ver));
		updateWithoutListen = false;
	}
*/
}
