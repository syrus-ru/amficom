package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JPanel;
import javax.swing.JFileChooser;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.image.ImageObserver;


import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;

/*import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGCodec;*/

import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.CORBA.Report.ImagePane_Transferable;
/**
 * <p>Title: </p>
 * <p>Description: Панель для отображения GIF и JPG</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ImagePanel extends JPanel
{
	public Image bImage = null;
	public String fileName = "";

	static private int imagesTransferred = 0;

	public ImagePanel(int x, int y)
		throws CreateReportException
	{
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter filter =
			new ChoosableFileFilter(
			"gif",
			LangModelReport.String("label_GIFFiles"));
		fileChooser.addChoosableFileFilter(filter);

		filter =
			new ChoosableFileFilter(
			"jpg",
			LangModelReport.String("label_JPGFiles"));
		fileChooser.addChoosableFileFilter(filter);


		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
			fileName = fileChooser.getSelectedFile().getPath();

		if (fileName == null)
		{
			throw new CreateReportException("",CreateReportException.noImageSelected);
		}

		try
		{
			bImage = java.awt.Toolkit.getDefaultToolkit().getImage(fileName);

			this.setSize(200,200);
			this.setPreferredSize(this.getSize());
			this.setLocation(x,y);
		}
		catch (Exception exc)
		{
			throw new CreateReportException("",CreateReportException.noImageSelected);
		}
	}

	public ImagePanel(int x, int y, String fileName)
		throws CreateReportException
	{
		if (fileName == null)
		{
			throw new CreateReportException("",CreateReportException.noImageSelected);
		}

		try
		{
			bImage = java.awt.Toolkit.getDefaultToolkit().getImage(fileName);

			this.setSize(200,200);
			this.setPreferredSize(this.getSize());
			this.setLocation(x,y);
		}
		catch (Exception exc)
		{
			throw new CreateReportException("",CreateReportException.noImageSelected);
		}
	}


	public ImagePanel(ImagePane_Transferable ip_trans)
	{
		super();
		this.setLocation(ip_trans.x, ip_trans.y);
		this.setSize(ip_trans.width, ip_trans.height);
		this.setPreferredSize(this.getSize());

		try
		{
			fileName = System.getProperty("java.io.tmpdir") +
						  "image" + Integer.toString(imagesTransferred) + ".tmp";
			FileOutputStream fos = new FileOutputStream (fileName);
			fos.write(ip_trans.bytes);
			fos.close();

			bImage = java.awt.Toolkit.getDefaultToolkit().getImage(fileName);
		}
		catch(Exception ex)
		{
			System.out.println("Error reading image from server!");
		}

	}

	public void paint(Graphics g)
	{
		super.paint(g);

		double scaleX = this.getPreferredSize().getWidth() / bImage.getWidth(this);
		double scaleY = this.getPreferredSize().getHeight() / bImage.getHeight(this);

		Graphics2D g2D = (Graphics2D) g;
		g2D.scale(scaleX,scaleY);

		g.drawImage(bImage,0,0,Color.white,this);

		g.setColor(Color.black);
		g2D.scale(1 / scaleX,1 / scaleY);
		g.drawRect(
				 0,
				 0,
				 (int) this.getPreferredSize().getWidth(),
				 (int) this.getPreferredSize().getHeight());
	}
}
