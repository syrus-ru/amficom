package com.syrus.AMFICOM.client.UI;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;


import javax.swing.JPanel;

/**
 * @deprecated as an invalid evolution branch
 * <p>Title: </p>
 * <p>Description: Прогрессбар на манер нетскейповского</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */
public class ProgressBar extends JPanel
{
	private static final long serialVersionUID = 3258132436071429426L;

	private boolean active = false;
	private BarRepaintThread thread = null;
	private String caption = "";
	private Font pbFont = null;
	private int state = 0;
	private boolean movingTo = true; //true - вправо, false - влево

	public ProgressBar()
	{
		this.setSize(200, 15);
		this.setPreferredSize(this.getSize());
		this.setDoubleBuffered(true);

		this.thread = new BarRepaintThread(this);
	}

	public void start(String caption1)
	{
		this.state = 0;
		this.caption = caption1;
		this.active = true;
		this.thread.start();
	}

	public void stop()
	{
		this.active = false;
		this.thread.active = false;
	}

	void refreshCoords()
	{
		if (this.movingTo)
			this.state++;
		else
			this.state--;
		if ((this.state == 2 * getPreferredSize().width) && this.movingTo)
			this.movingTo = false;
		if ((this.state == 0) && !this.movingTo)
			this.movingTo = true;
	}

  public void paintComponent(Graphics g)
  {
	  if (!this.active)
	  {
		  super.paintComponent(g);
		  return;
	  }

	  Dimension size = this.getPreferredSize();

	  if (this.state < size.width)
	  {
		  for (int i = this.getX(); i < this.state; i++)
		  {
			  float koef = 1;
			  if (this.movingTo)
				  koef = 1
							- ((float) (i - this.getX()) / (float) (this.state - this.getX()));
			  else
				  koef = ((float) (i - this.getX())
										/ (float) (this.state - this.getX()));

			  g.setColor(new Color(koef, koef, 1));
			  g.drawLine(i, this.getY() + 1, i, size.height - 1);
		  }
		  g.setColor(Color.white);
		  g.fillRect(this.state, this.getY() + 1, size.width - this.state, size.height - 1);
	  }
	  else
	  {
		  int whiteSpace = this.state - size.width;

		  for (int i = this.getX() + whiteSpace; i < size.width; i++)
		  {
			  float koef = 1;
			  if (this.movingTo)
				  koef = 1
							- ((float) (i - this.getX() - whiteSpace)
								/ (float) (size.width - whiteSpace - this.getX()));
			  else
				  koef = (float) (i - this.getX() - whiteSpace)
							/ (float) (size.width - whiteSpace - this.getX());

			  g.setColor(new Color(koef, koef, 1));
			  g.drawLine(i, this.getY() + 1, i, size.height - 1);
		  }

		  g.setColor(Color.white);
		  g.fillRect(this.getX(), this.getY() + 1, whiteSpace, size.height - 1);
	  }

	  g.setColor(Color.black);

	  if (this.pbFont == null)
	  {
		  int fontSize;
		  for (fontSize = 6; fontSize < 14; fontSize++)
		  {
			  g.setFont(new Font("Arial", 0, fontSize));
			  Rectangle curBounds = g.getFontMetrics().getStringBounds(this.caption, g).
											getBounds();
			  if ((curBounds.width > size.width)
					|| (curBounds.height > size.height - 2))
				  break;
		  }

		  this.pbFont = new Font("Arial", 0, --fontSize);
		  g.setFont(this.pbFont);
	  }

	  Rectangle finalBounds = g.getFontMetrics().getStringBounds(this.caption, g).
									  getBounds();

	  g.drawString(
		  this.caption,
		  (size.width - finalBounds.width) / 2,
		  size.height - 3);
  }

}

class BarRepaintThread extends Thread
{
  public volatile boolean active = true;

  ProgressBar bar = null;
  int x;
  int y;
  int width;
  int height;

  public BarRepaintThread(ProgressBar bar)
  {
//		System.loadLibrary("ProgressBarRepainter");
	  this.bar = bar;
	  setPriority(Thread.MAX_PRIORITY);
  }

  public void run()
  {
	  while (this.active)
	  {
		  try
		  {
			  this.bar.refreshCoords();
			  this.bar.repaint();
			  sleep(30);
		  }
		  catch (InterruptedException exc)
		  {
		  }
	  }
  }
}

