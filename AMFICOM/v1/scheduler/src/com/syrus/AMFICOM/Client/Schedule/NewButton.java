package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import javax.swing.*;

public class NewButton extends JButton
{
	public Color col;
	NewButtonThread nbt;
	public String baza_type;
	public int int_tempor_type;
	public boolean global;
	public String testid;
	public int count;
	public long testtime;
	public String kisid;
	public String meid;
	public String portid;
	public NewButton(Color col, int int_tempor_type, String baza_type, boolean global, String testid,
				int count, long testtime, String kisid, String portid, String meid)
	{
		this.col = col;
		this.int_tempor_type = int_tempor_type;
		this.baza_type = baza_type;
		this.global = global;
		this.testid = testid;
		this.count = count;
		this.testtime = testtime;
		this.kisid = kisid;
		this.portid = portid;
		this.meid = meid;
		if (!baza_type.equals("BazaTest"))
		{
			nbt = new NewButtonThread(this, col);
			nbt.start();
		}
		this.setBackground(col);
	}
}