package com.syrus.io;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import com.ofx.repository.*;
import com.ofx.base.*;

public class importmif 
{
	static SxMap GISmap = null;

	public importmif()
	{
/*	
		Frame frame = new ViewerFrame();
		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } });
		frame.setVisible(true);
*/
	}

	public static void main(String[] args)
	{
		try
		{
			opendb();

			openmap("euas");

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new importmif();
	}

	public static void opendb() throws Exception
	{
		System.out.println("open db");
		String sus1 = System.getProperty("user.dir").replace('\\', '/');

		String sus = "file://localhost/" + sus1 + "/AndreiDB";
		System.out.println(sus);
		SxEnvironment.singleton().getQuery().openSession(
			"xfile",
			"file://localhost/d:/SpatialFX2.0/data/euasDB",
			"userName",
			"password");
		SxEnvironment.singleton().getQuery().commitTransaction();
		System.out.println("db opened");
	}

	public static void closedb()
	{
		System.out.println("close db");
		SxEnvironment.singleton().getQuery().close();
		System.out.println("db closed");
	}

	public static void openmap(String name)
	{
		System.out.println("open map!");
/*
		try
		{
			GISmap = SxMap.retrieve(name);
		}
		catch(Exception ex)
		{
			System.out.println("Failed set map");
			System.out.println(ex);
		}
		SxEnvironment.singleton().getQuery().commitTransaction();
*/
		System.out.println("map opened");
	}

}

