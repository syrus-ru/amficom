package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ImagesDialog;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import oracle.jdeveloper.layout.VerticalFlowLayout;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapEquipmentGeneralPanel extends GeneralPanel 
{
	MapEquipmentNodeElement mapequipment;

	private JPanel descriptionPanel = new JPanel();

	String image_id;
	private JPanel labelsPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel3 = new JLabel();
	private JButton imageButton = new JButton();
	ObjectResourceComboBox equipmentComboBox = new ObjectResourceComboBox(SchemeElement.typ, true);
	ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox("mapprotoelement");
	JTextField idField = new JTextField();
	JTextField nameField = new JTextField();
	private JPanel imagePanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout3 = new BorderLayout();
	private JPanel descLabelPanel = new JPanel();
	JLabel jLabel7 = new JLabel();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout5 = new BorderLayout();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descriptionTextPane = new JTextPane();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	
	public MapEquipmentGeneralPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public MapEquipmentGeneralPanel(MapEquipmentNodeElement mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}

	private void jbInit() throws Exception
	{
		setName("Общие");

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.setLayout(gridBagLayout1);
		jLabel1.setText("Название");
		jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel2.setText("Идентификатор");
		jLabel2.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel4.setText("Тип");
		jLabel4.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel5.setText("Элемент схемы");
		jLabel5.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabel3.setText("Изображение");
		jLabel3.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		imageButton.setText("Изменить");
		imageButton.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		imageButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					imageButton_actionPerformed(e);
				}
			});
		equipmentComboBox.setEnabled(false);
		idField.setEnabled(false);
		imagePanel.setLayout(borderLayout1);
		jLabel7.setText("Примечания");
		jLabel7.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descriptionScrollPane.getViewport().add(descriptionTextPane, null);

		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
		this.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(imageButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel7, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(jLabel2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
		this.add(typeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(equipmentComboBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(imagePanel, new GridBagConstraints(1, 3, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionScrollPane, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	}

	public ObjectResource getObjectResource()
	{
		return mapequipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		this.mapequipment = (MapEquipmentNodeElement )or;

		if(mapequipment == null)
		{
			equipmentComboBox.setSelected("");
			nameField.setText("");
			idField.setText("");
			typeComboBox.setSelected("");
			descriptionTextPane.setText("");
			image_id = "";
			imagePanel.removeAll();
			imageButton.setEnabled(false);
			return;
		}

//		System.out.println("set prop pane to " + mapequipment.name);

		if(mapequipment.element_id != null && !mapequipment.element_id.equals(""))
			equipmentComboBox.setSelected(mapequipment.element_id);
		else
			equipmentComboBox.setSelected("");

		nameField.setText(mapequipment.getName());
		idField.setText(mapequipment.getId());
		typeComboBox.setSelected(mapequipment.type_id);
		descriptionTextPane.setText(mapequipment.description);

		image_id = mapequipment.getImageID();
		imagePanel.removeAll();
		ImageResource ir = ImageCatalogue.get(image_id);
		imagePanel.add(new JLabel(new ImageIcon(ir.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))));
		imagePanel.revalidate();
		imageButton.setEnabled(true);
	}

	public boolean modify()
	{
		mapequipment.name = nameField.getText();

		MapProtoElement mpe = (MapProtoElement )typeComboBox.getSelectedObjectResource();
		mapequipment.type_id = mpe.getId();
		mapequipment.setImageID(image_id);

		mapequipment.description = descriptionTextPane.getText();
		return true;
	}

	private void imageButton_actionPerformed(ActionEvent e)
	{
		ImagesDialog frame = new ImagesDialog(aContext);
		frame.setImageResource(ImageCatalogue.get(image_id));

//		frame.validate();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) 
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width) 
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setModal(true);
		frame.setVisible(true);
		if(frame.ret_code == 1)
		{
			imagePanel.removeAll();
			ImageResource ir = frame.getImageResource();
			image_id = ir.getId();
			imagePanel.add(new JLabel(new ImageIcon(ir.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))));
			imagePanel.revalidate();
		}
	}

}