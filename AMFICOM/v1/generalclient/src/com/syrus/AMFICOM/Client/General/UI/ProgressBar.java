package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;


import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: Прогрессбар на манер нетскейповского</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ProgressBar extends JPanel
{
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

		thread = new BarRepaintThread(this);
	}

	public void start(String caption)
	{
		state = 0;
		this.caption = caption;
		active = true;
		thread.start();
	}

	public void stop()
	{
		active = false;
		thread.active = false;
	}

	void refreshCoords()
	{
		if (movingTo)
			state++;
		else
			state--;
		if ((state == 2 * getPreferredSize().width) && movingTo)
			movingTo = false;
		if ((state == 0) && !movingTo)
			movingTo = true;
	}

  public void paintComponent(Graphics g)
  {
	  if (!active)
	  {
		  super.paintComponent(g);
		  return;
	  }

	  Dimension size = this.getPreferredSize();

	  if (state < size.width)
	  {
		  for (int i = this.getX(); i < state; i++)
		  {
			  float koef = 1;
			  if (movingTo)
				  koef = 1
							- ((float) (i - this.getX()) / (float) (state - this.getX()));
			  else
				  koef = (float) ((float) (i - this.getX())
										/ (float) (state - this.getX()));

			  g.setColor(new Color(koef, koef, 1));
			  g.drawLine(i, this.getY() + 1, i, size.height - 1);
		  }
		  g.setColor(Color.white);
		  g.fillRect(state, this.getY() + 1, size.width - state, size.height - 1);
	  }
	  else
	  {
		  int whiteSpace = state - size.width;

		  for (int i = this.getX() + whiteSpace; i < size.width; i++)
		  {
			  float koef = 1;
			  if (movingTo)
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

	  if (pbFont == null)
	  {
		  int font_size;
		  for (font_size = 6; font_size < 14; font_size++)
		  {
			  g.setFont(new Font("Arial", 0, font_size));
			  Rectangle curBounds = g.getFontMetrics().getStringBounds(caption, g).
											getBounds();
			  if ((curBounds.width > size.width)
					|| (curBounds.height > size.height - 2))
				  break;
		  }

		  pbFont = new Font("Arial", 0, --font_size);
		  g.setFont(pbFont);
	  }

	  Rectangle finalBounds = g.getFontMetrics().getStringBounds(caption, g).
									  getBounds();

	  g.drawString(
		  caption,
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
	  while (active)
	  {
		  try
		  {
			  bar.refreshCoords();
			  bar.repaint();
			  sleep(30);
		  }
		  catch (InterruptedException exc)
		  {
		  }
	  }
  }
}

