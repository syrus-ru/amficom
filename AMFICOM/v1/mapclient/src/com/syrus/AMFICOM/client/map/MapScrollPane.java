package com.syrus.AMFICOM.Client.Configure.Map;


import javax.swing.*;
import com.ofx.component.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

//A0A
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
	JMapViewer myMapViewer;

//Флаг того что нужно обновить безсрабатывания listener
	boolean updataWithoutListen = false;

	public MapScrollPane(JMapViewer viewer)
	{
		super();
		try
		{
			this.myMapViewer = viewer;
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit()
	{
		try
		{
			double a = myMapViewer.getCenter()[0];
			double b = myMapViewer.getCenter()[1];

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
		gridbag.setConstraints(myMapViewer, c);
		this.add(myMapViewer);

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
//A0A
					if (horScrollBar.getValueIsAdjusting())
					{

						double a = myMapViewer.getCenter()[0];
						double b = myMapViewer.getCenter()[1];
						double new_lat = vert_left + (vert_right - vert_left)/(vertScrollBar.getMaximum() - vertScrollBar.getVisibleAmount())*vertScrollBar.getValue();
						myMapViewer.setCenter(a, new_lat);
					}
//A0A
					else
					{
						double a = myMapViewer.getCenter()[0];
						double b = myMapViewer.getCenter()[1];

						double new_lat = vert_left + (vert_right - vert_left)/(vertScrollBar.getMaximum() - vertScrollBar.getVisibleAmount())*vertScrollBar.getValue();
						myMapViewer.setCenter(a, new_lat);
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
				if ( updataWithoutListen == false)
				{
//A0A
					if (horScrollBar.getValueIsAdjusting())
					{

						double a = myMapViewer.getCenter()[0];
						double b = myMapViewer.getCenter()[1];

						double new_long =  horiz_left + (horiz_right - horiz_left)/(horScrollBar.getMaximum() - horScrollBar.getVisibleAmount())*horScrollBar.getValue();
						myMapViewer.setCenter(new_long, b);
					}
//A0A
					else
					{
						double a = myMapViewer.getCenter()[0];
						double b = myMapViewer.getCenter()[1];

						double new_long =  horiz_left + (horiz_right - horiz_left)/(horScrollBar.getMaximum() - horScrollBar.getVisibleAmount())*horScrollBar.getValue();
						myMapViewer.setCenter(new_long, b);
					}
				}
			}
		}
		);

	}

//Обновить прокрутку без listener
	public void upDateScroll()
	{
		updataWithoutListen =true;
		double a = myMapViewer.getCenter()[0];
		double b = myMapViewer.getCenter()[1];

		double hor =  (horiz_right - horiz_left)/(a - horiz_left);
		double ver =  (vert_right - vert_left)/(b - vert_left);

		horScrollBar.setValue( (int) ((horScrollBar.getMaximum() - horScrollBar.getVisibleAmount()) /hor));
		vertScrollBar.setValue( (int) ((vertScrollBar.getMaximum() - vertScrollBar.getVisibleAmount()) /ver));
		updataWithoutListen = false;
	}

}