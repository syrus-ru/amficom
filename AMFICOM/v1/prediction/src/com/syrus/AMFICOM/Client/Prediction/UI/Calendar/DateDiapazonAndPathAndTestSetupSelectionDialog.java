package com.syrus.AMFICOM.Client.Prediction.UI.Calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.MonitoredElement;
import com.syrus.AMFICOM.Client.Resource.Result.TestSetup;

import oracle.jdeveloper.layout.VerticalFlowLayout;

public class DateDiapazonAndPathAndTestSetupSelectionDialog  extends JDialog
{
	public long from = 0;
	public long to = 0;
	public int retCode = 0;
	public String me_id = "";
	public String testSetup_id = "";

	private JPanel mainPanel = new JPanel();
	private JLabel label2 = new JLabel();
	private JPanel panel4 = new JPanel();
	private JLabel label3 = new JLabel();
	private JLabel label4 = new JLabel();
	private JPanel buttonPanel = new JPanel();
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();

	private TimeSpinnerPanel tsp1 = new TimeSpinnerPanel();
	private TimeSpinnerPanel tsp2 = new TimeSpinnerPanel();
	private JLabel label1 = new JLabel();
	private ObjectResourceComboBox pathComboBox = new ObjectResourceComboBox();
	private ObjectResourceComboBox testSetupComboBox = new ObjectResourceComboBox();

	private DataSourceInterface dataSource;
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private JPanel jPanel1 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel jPanel2 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel jPanel3 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	private BorderLayout borderLayout2 = new BorderLayout();

	public DateDiapazonAndPathAndTestSetupSelectionDialog(Frame frame, String title, boolean modal,
			DataSourceInterface dataSource)
	{
		super(frame, title, modal);
		this.dataSource = dataSource;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{

		pathComboBox.setDomainId(dataSource.getSession().getDomainId());
		pathComboBox.restrictToDomain(true);

		mainPanel.setLayout(verticalFlowLayout1);
		label2.setPreferredSize(new Dimension(20, 23));
		label2.setText("Начало");
		label3.setPreferredSize(new Dimension(21, 23));
		label3.setText("Конец");
		//okButton.setPreferredSize(new Dimension(90, 27));
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				okButton_actionPerformed(e);
			}
		});
		//cancelButton.setMinimumSize(new Dimension(90, 27));
		//cancelButton.setPreferredSize(new Dimension(90, 27));
		cancelButton.setText("Отмена");
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		label1.setPreferredSize(new Dimension(32, 23));
		label1.setText("Маршрут");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		panel4.setLayout(new FlowLayout());
		pathComboBox.setPreferredSize(new Dimension(268, 23));
		testSetupComboBox.setPreferredSize(new Dimension(268, 23));
		label4.setPreferredSize(new Dimension(31, 23));
		label4.setText("Шаблон");
		jPanel1.setLayout(borderLayout1);
		jPanel1.setPreferredSize(new Dimension(320, 120));
		jPanel2.setLayout(verticalFlowLayout2);
		jPanel3.setLayout(verticalFlowLayout3);
		tsp1.setPreferredSize(new Dimension(250, 23));
		tsp2.setPreferredSize(new Dimension(250, 23));
		jPanel2.setPreferredSize(new Dimension(60, 117));
		buttonPanel.setLayout(borderLayout2);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);

		setComboBoxes();


		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		tsp1.jSpin1.setValue(sdf.parse("18.06.2003 00:00:00"));

		mainPanel.add(panel4, null);

		buttonPanel.add(okButton, BorderLayout.WEST);
		buttonPanel.add(cancelButton, BorderLayout.EAST);
		mainPanel.add(jPanel1, null);
		jPanel1.add(jPanel2, BorderLayout.WEST);
		jPanel2.add(label1, null);
		jPanel2.add(label4, null);
		jPanel2.add(label2, null);
		jPanel2.add(label3, null);
		jPanel1.add(jPanel3, BorderLayout.CENTER);
		jPanel3.add(pathComboBox, null);
		jPanel3.add(testSetupComboBox, null);
		jPanel3.add(tsp1, null);
		jPanel3.add(tsp2, null);
		mainPanel.add(buttonPanel, null);



		pathComboBox.addItemListener(new java.awt.event.ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				pathComboBox_itemStateChanged(e);
			}
		});

		this.setSize(new Dimension(330, 215));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight() - 30) / 2);

		//this.setResizable(false);
		this.setVisible(true);
	}

	void okButton_actionPerformed(ActionEvent e)
	{
		from = tsp1.getSelectedTime();
		to   = tsp2.getSelectedTime();

		me_id = pathComboBox.getSelectedId();
		if(me_id == null)
			me_id = "";

		testSetup_id = testSetupComboBox.getSelectedId();
		if(testSetup_id == null)
			testSetup_id = "";

		if (from > to)
		{
			long tmp = from;
			from = to;
			to = tmp;
		}

		retCode = 1;
		dispose();
	}

	void cancelButton_actionPerformed(ActionEvent e)
	{
		dispose();
	}


	private void setComboBoxes()
	{
		new ConfigDataSourceImage(dataSource).LoadISM();

		Map h = Pool.getMap(MonitoredElement.typ);

		pathComboBox.removeAllItems();
		pathComboBox.setContents(MonitoredElement.typ, false);
		pathComboBox.setEditable(false);

		testSetupComboBox.removeAllItems();
		String []testSetupIds = dataSource.getTestSetupsByME(pathComboBox.getSelectedId());

		Collection v = new ArrayList();
		for(int i=0; i<testSetupIds.length; i++)
		{
			if(Pool.get(TestSetup.TYPE, testSetupIds[i]) != null)
			{
				Object o = Pool.get(TestSetup.TYPE, testSetupIds[i]);
				v.add(o);
			}
		}

		testSetupComboBox.setContents(v, false);
		testSetupComboBox.setEditable(false);
	}



	void pathComboBox_itemStateChanged(ItemEvent e)
	{
		setTestSetupComboBox();
	}

	private void setTestSetupComboBox()
	{
		testSetupComboBox.removeAllItems();
		String []testSetupIds = dataSource.getTestSetupsByME(pathComboBox.getSelectedId());

		Collection v = new ArrayList();
		for(int i=0; i<testSetupIds.length; i++)
		{
			if(Pool.get(TestSetup.TYPE, testSetupIds[i]) != null)
			{
				v.add(Pool.get(TestSetup.TYPE, testSetupIds[i]));
			}
		}

		testSetupComboBox.setContents(v, false);
		testSetupComboBox.setEditable(false);
	}
}

