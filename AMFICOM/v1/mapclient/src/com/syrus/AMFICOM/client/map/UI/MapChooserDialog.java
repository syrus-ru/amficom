package com.syrus.AMFICOM.Client.Map.UI;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTablePane;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.jdeveloper.layout.VerticalFlowLayout;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapChooserDialog extends JDialog 
{
	JPanel topPanel = new JPanel();
	XYLayout xYLayout1 = new XYLayout();
	JButton buttonHelp = new JButton();
	JButton buttonCancel = new JButton();
	JButton buttonOpen = new JButton();
	public JButton buttonDelete = new JButton();

	public ObjectResourceTablePane listPane = new ObjectResourceTablePane();

	public ObjectResource retObject;
	public int retCode = 2;
	public final int RET_OK = 1;
	public final int RET_CANCEL = 2;
	private JPanel eastPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private FlowLayout flowLayout2 = new FlowLayout();
	private FlowLayout flowLayout3 = new FlowLayout();
	private BorderLayout borderLayout3 = new BorderLayout();

	DataSourceInterface dataSource = null;

	public MapChooserDialog(DataSourceInterface dataSource)
	{
		this(Environment.getActiveWindow(), "", false);
		this.dataSource = dataSource;
	}

	protected MapChooserDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setContents(ObjectResourceDisplayModel odm, List dataSet)
	{
		listPane.initialize(odm, dataSet);
		buttonOpen.setEnabled(false);
		buttonDelete.setEnabled(false);
	}
	
	public void jbInit() throws Exception
	{
		this.setResizable(false);
		this.setSize(new Dimension(550, 320));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

//		this.setsetFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.getContentPane().setLayout(borderLayout2);
		this.setTitle("Топологическая схема");
		topPanel.setLayout(borderLayout3);
		topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		buttonHelp.setText("Помощь");
		buttonHelp.setEnabled(false);
		buttonCancel.setText("Отменить");
		buttonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonCancel_actionPerformed(e);
				}
			});
		buttonOpen.setText("OK");
		buttonOpen.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonOpen_actionPerformed(e);
				}
			});
		buttonDelete.setText("Удалить");
		buttonDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonDelete_actionPerformed(e);
				}
			});
		eastPanel.setLayout(flowLayout3);
		westPanel.setLayout(flowLayout2);
		bottomPanel.setLayout(borderLayout1);
		flowLayout3.setAlignment(2);
		eastPanel.add(buttonOpen, null);
		eastPanel.add(buttonCancel, null);
		eastPanel.add(buttonHelp, null);
		westPanel.add(buttonDelete, null);
		bottomPanel.add(westPanel, BorderLayout.WEST);
		bottomPanel.add(eastPanel, BorderLayout.CENTER);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.getContentPane().add(topPanel, BorderLayout.CENTER);

		topPanel.add(listPane, BorderLayout.CENTER);

		buttonDelete.setVisible(false);

		listPane.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					listPane_valueChanged(e);
				}
			});
	}

	public void buttonOpen_actionPerformed(ActionEvent e)
	{
		retObject = (ObjectResource)listPane.getSelectedObject();
		if(retObject == null)
			return;

		retCode = RET_OK;
		this.dispose();
	}

	public void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		this.dispose();
	}

	public void buttonDelete_actionPerformed(ActionEvent e)
	{
		ObjectResource obj = (ObjectResource)listPane.getSelectedObject();
		if(obj == null)
			return;

		if(obj instanceof MapContext)
		{
			dataSource.RemoveMap(obj.getId());
			listPane.getContents().remove(obj);
			Pool.remove(MapContext.typ, obj.getId());
			listPane.updateUI();
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
		else
		if(obj instanceof Scheme)
		{
			dataSource.RemoveScheme(obj.getId());
			listPane.getContents().remove(obj);
			Pool.remove(Scheme.typ, obj.getId());
			listPane.updateUI();
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
	}

	void listPane_valueChanged(ListSelectionEvent e)
	{
//		if (e.getValueIsAdjusting())
//			return;

		ObjectResource or = (ObjectResource )listPane.getSelectedObject();
		if (or != null)
		{
			buttonOpen.setEnabled(true);
			buttonDelete.setEnabled(true);
		}
		else
		{
			buttonOpen.setEnabled(false);
			buttonDelete.setEnabled(false);
		}
	}

}