package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;


public class TestPlan extends JPanel implements OperationListener{

	Dispatcher internal_dispatcher;
	ScheduleMDIMain parent;
	ApplicationContext aContext;

	public String meid;
	public String portid;
	public String kisid;
	public String kisname;
	public String portname;
	public String mename;
	AComboBox jComboBox8;
	Calendar calen;
	Calendar current_calen1 = Calendar.getInstance();
	Calendar current_calen2 = Calendar.getInstance();
	Date date = Calendar.getInstance().getTime();
	CurrentTime ct = new CurrentTime(this);

	public TestPlan(ScheduleMDIMain parent, ApplicationContext aContext, AComboBox jComboBox8, String kisid, String portid, String meid)
	{
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"TestPlan_Time");
		this.kisid = kisid;
		this.portid = portid;
		this.meid = meid;
		this.jComboBox8=jComboBox8;
		this.setLayout(new XYLayout());
		this.setPreferredSize(new Dimension (1010,40));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.ct.start();
		kisname = Pool.getName("kis", kisid);
		portname = Pool.getName("accessport", portid);
		mename = Pool.getName("monitoredelement", meid);
		this.setToolTipText(kisname+";   "+portname+";   "+mename);
	}

	public void paint(Graphics g) {
	super.paint(g);
	calen = Calendar.getInstance();
	current_calen1.setTime(date);
	current_calen2.setTime(date);
	FontMetrics fm = g.getFontMetrics();
	int otstup = 10;
	g.setColor(Color.blue);
	g.drawString(kisname+";   "+portname+";   "+mename,10,36);
	g.setColor(Color.black);
	if (this.jComboBox8.getSelectedIndex()==0)
	{
		g.drawLine(0+otstup,20,960+otstup,20);
		current_calen1.set(current_calen1.get(Calendar.YEAR), current_calen1.get(Calendar.MONTH), current_calen1.get(Calendar.DAY_OF_MONTH), current_calen1.get(Calendar.HOUR_OF_DAY),0,0);
		current_calen2.set(current_calen2.get(Calendar.YEAR), current_calen2.get(Calendar.MONTH), current_calen2.get(Calendar.DAY_OF_MONTH), current_calen2.get(Calendar.HOUR_OF_DAY),59,59);
		if (current_calen2.getTime().getTime() < calen.getTime().getTime())
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,960+otstup,20);
		}
		else if ( (current_calen1.getTime().getTime() < calen.getTime().getTime()) && (current_calen2.getTime().getTime() > calen.getTime().getTime()) )
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,16*calen.get(Calendar.MINUTE)+otstup,20);
		}

		for (int i = 0; i <= 60; i++)
		{
		g.setColor(Color.green);
		g.drawLine(i*16+otstup,17,i*16+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+i,16*i-fm.stringWidth(""+i)/2+otstup,16);
		}
	}
	else if (this.jComboBox8.getSelectedIndex()==1)
	{
		g.drawLine(0+otstup,20,984+otstup,20);
		current_calen1.set(current_calen1.get(Calendar.YEAR), current_calen1.get(Calendar.MONTH), current_calen1.get(Calendar.DAY_OF_MONTH), 0,0,0);
		current_calen2.set(current_calen2.get(Calendar.YEAR), current_calen2.get(Calendar.MONTH), current_calen2.get(Calendar.DAY_OF_MONTH), 23,59,59);

		if (current_calen2.getTime().getTime() < calen.getTime().getTime())
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,984+otstup,20);
		}
		else if ( (current_calen1.getTime().getTime() < calen.getTime().getTime()) && (current_calen2.getTime().getTime() > calen.getTime().getTime()) )
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,41*calen.get(Calendar.HOUR_OF_DAY)+otstup,20);
		}

		for (int i = 0; i <= 24; i++)
		{
		g.setColor(Color.green);
		g.drawLine(i*41+otstup,17,i*41+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+i,41*i-fm.stringWidth(""+i)/2+otstup,16);
		}
	}
	else if (this.jComboBox8.getSelectedIndex()==2)
	{
		g.drawLine(0+otstup,20,992+otstup,20);
		current_calen1.set(current_calen1.get(Calendar.YEAR), current_calen1.get(Calendar.MONTH), 1, 0,0,0);
		current_calen2.set(current_calen2.get(Calendar.YEAR), current_calen2.get(Calendar.MONTH), current_calen1.getActualMaximum(Calendar.DAY_OF_MONTH), 23,59,59);

		if (current_calen2.getTime().getTime() < calen.getTime().getTime())
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,992+otstup,20);
		}
		else if ( (current_calen1.getTime().getTime() < calen.getTime().getTime()) && (current_calen2.getTime().getTime() > calen.getTime().getTime()) )
		{
			g.setColor(Color.red);
			g.drawLine(0+otstup,20,32*(calen.get(Calendar.DAY_OF_MONTH))+otstup,20);
		}

		for (int i = 1; i <= 32; i++)
		{
		g.setColor(Color.green);
		g.drawLine(i*32+otstup,17,i*32+otstup,23);
		g.setColor(Color.black);
		if (i==32)
			break;
		g.drawString(""+i,32*(i-1)-fm.stringWidth(""+i)/2+otstup,16);
		}
	}
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("TestPlan_Time"))
		{
			date = (Date )ae.getSource();
		}
	}
}

