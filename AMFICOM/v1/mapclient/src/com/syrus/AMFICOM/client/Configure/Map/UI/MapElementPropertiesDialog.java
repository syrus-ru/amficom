package com.syrus.AMFICOM.Client.Configure.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapElementPropertiesDialog extends JDialog
{
	JPanel jPanel1 = new JPanel();
	JButton buttonHelp = new JButton();
	JButton buttonCancel = new JButton();
	JButton buttonModify = new JButton();
	JButton buttonClose = new JButton();
//	MapEquipmentPane mapequipmentPane = new MapEquipmentPane();
	PropertiesPanel elementPane = new GeneralPanel();
	JPanel bottomPanel = new JPanel();
	private FlowLayout flowLayout3 = new FlowLayout();

	public ObjectResource retObject;
	public int retCode = 2;
	public final int RET_OK = 1;
	public final int RET_CANCEL = 2;

	ObjectResource myNode;
	JFrame myFame;
//	MapConnectionPoints dataConnectionPoints;
	ApplicationContext aContext;

	public MapElementPropertiesDialog()
	{
		this(null, new ApplicationContext(), "", false, null);
	}

	public MapElementPropertiesDialog(JFrame parent, ApplicationContext aContext, String title, boolean modal, ObjectResource node)
	{
		super(Environment.getActiveWindow(), title, modal);
		try
		{
			myFame = parent;
		    myNode = (ObjectResource )node;
			this.aContext = aContext;
			ObjectResourceModel orm = ((ObjectResource )myNode).getModel();
			elementPane = orm.getPropertyPane();
//			elementPane = ((ObjectResource )myNode).getPropertyPane();
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
//		mapequipmentPane.setContext(aContext);
//		mapequipmentPane.setObjectResource(myNode);
		elementPane.setContext(aContext);
		elementPane.setObjectResource((ObjectResource )myNode);
	}

	void jbInit() throws Exception
	{
		this.setResizable(false);
		this.setSize(new Dimension(590, 300));
		this.getContentPane().setLayout(new BorderLayout());
//		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setTitle("Свойства элемента");
		jPanel1.setLayout(new XYLayout());
		jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		buttonModify.setText("OK");
		buttonCancel.setText("Отменить");
		buttonHelp.setText("Помощь");
		buttonClose.setText("Закрыть");
		buttonModify.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    buttonModify_actionPerformed(e);
                }
            });
		buttonClose.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    buttonClose_actionPerformed(e);
                }
            });
		buttonCancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					buttonCancel_actionPerformed(e);
				}
			});
//		this.getContentPane().add(buttonCancel, new XYConstraints(110, 515, 75, 25));
//		this.getContentPane().add(buttonHelp, new XYConstraints(465, 515, 75, 25));
//		this.getContentPane().add(jPanel1, new XYConstraints(10, 10, 555, 495));
//		jPanel1.add(buttonModify, new XYConstraints(10, 535, 125, 25));
//		jPanel1.add(buttonCancel, new XYConstraints(150, 535, 125, 25));
//		jPanel1.add(buttonHelp, new XYConstraints(295, 535, 125, 25));
//		jPanel1.add(buttonClose, new XYConstraints(435, 535, 125, 25));
		jPanel1.add((JComponent )elementPane, new XYConstraints(10, 10, 555, 215));

		bottomPanel.setLayout(flowLayout3);
		flowLayout3.setAlignment(2);
		bottomPanel.add(buttonModify, null);
		bottomPanel.add(buttonCancel, null);
//		bottomPanel.add(buttonHelp, null);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
	}

	void buttonModify_actionPerformed(ActionEvent e)
	{
		elementPane.modify();
		retCode = RET_OK;
		this.dispose();
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		elementPane.setObjectResource(elementPane.getObjectResource());
		retCode = RET_CANCEL;
		this.dispose();
	}

	void buttonClose_actionPerformed(ActionEvent e)
	{
	}

}



