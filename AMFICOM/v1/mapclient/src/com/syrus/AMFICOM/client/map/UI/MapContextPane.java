package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import oracle.jdeveloper.layout.PaneConstraints;
import oracle.jdeveloper.layout.PaneLayout;
import oracle.jdeveloper.layout.VerticalFlowLayout;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class MapContextPane extends GeneralPanel
{
	public ApplicationContext aContext = new ApplicationContext();

	MapContext mapContext;

//	Border border1;
//	TitledBorder titledBorder1;
//	Border border2;
//	TitledBorder titledBorder2;
//	Border border3;
//	TitledBorder titledBorder3;
//	Border border4;
//	TitledBorder titledBorder4;
	JPanel mainPanel = new JPanel();
//	JTextField ownerTextField = new JTextField();
//	JTextField domainTextField = new JTextField();


	private JPanel bottomPanel = new JPanel();
	private JPanel propsPanel = new JPanel();
	private BorderLayout borderLayout3 = new BorderLayout();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descriptionTextPane = new JTextPane();
	private BorderLayout borderLayout4 = new BorderLayout();
	private JLabel jLabel1 = new JLabel();
	private JPanel descLabelPanel = new JPanel();
	JPanel scalePanel = new JPanel();
	JPanel centerPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	JLabel schemeLabel = new JLabel();
	JLabel domainLabel = new JLabel();
	JLabel ownerLabel = new JLabel();
	JLabel nameLabel = new JLabel();
	JLabel idLabel = new JLabel();
	public JTextField idTextField = new JTextField();
	public JTextField nameTextField = new JTextField();
	public ObjectResourceComboBox ownerTextField = new ObjectResourceComboBox("user");
	public ObjectResourceComboBox domainTextField = new ObjectResourceComboBox("domain");
	public ObjectResourceComboBox schemeTextField = new ObjectResourceComboBox("scheme", false);
	private BorderLayout borderLayout5 = new BorderLayout();
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private JPanel jPanel3 = new JPanel();
	private JPanel jPanel4 = new JPanel();
	JLabel minScaleLabel = new JLabel();
	JLabel maxScaleLabel = new JLabel();
	public JTextField minScaleTextField = new JTextField();
	public JTextField maxScaleTextField = new JTextField();
	private BorderLayout borderLayout6 = new BorderLayout();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
	JLabel longitudeLabel = new JLabel();
	JLabel latitudeLabel = new JLabel();
	public JTextField longitudeTextField = new JTextField();
	public JTextField latitudeTextField = new JTextField();
	private BorderLayout borderLayout7 = new BorderLayout();
	private VerticalFlowLayout verticalFlowLayout5 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout6 = new VerticalFlowLayout();
	private PaneLayout paneLayout1 = new PaneLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	public MapContextPane()
	{
		super();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public MapContextPane(MapContext mapContext)
	{
		this();
		setObjectResource(mapContext);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResource getObjectResource()
	{
		return mapContext;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		System.out.println("set OR " + or.getName());
		this.mapContext = (MapContext )or;

		idTextField.setText(mapContext.id);
		nameTextField.setText(mapContext.name);
		descriptionTextPane.setText(mapContext.description);
        ownerTextField.setSelected(mapContext.user_id);
        domainTextField.setSelected(mapContext.domain_id);

		schemeTextField.setDomainId(mapContext.domain_id);
		schemeTextField.restrictToDomain(true);
		schemeTextField.setSelected(mapContext.scheme_id);

		latitudeTextField.setText(String.valueOf(MyUtil.fourdigits(mapContext.latitude)));
		longitudeTextField.setText(String.valueOf(MyUtil.fourdigits(mapContext.longitude)));

		return true;
	}

	public boolean modify()
	{
		if(schemeTextField.getSelectedIndex() == -1)
			return false;
//		mapContext.id = idTextField.getText();
		mapContext.name = nameTextField.getText();
		mapContext.description = descriptionTextPane.getText();
        mapContext.user_id = (String)(ownerTextField.getSelected());
        mapContext.domain_id = (String)(domainTextField.getSelected());
		mapContext.scheme_id = (String)(schemeTextField.getSelected());

		try 
		{
			mapContext.latitude = Double.parseDouble(latitudeTextField.getText());
		} 
		catch (Exception ex) 
		{
		} 
		
		try 
		{
			mapContext.longitude = Double.parseDouble(longitudeTextField.getText());
		} 
		catch (Exception ex) 
		{
		} 

		return true;
	}

	private void jbInit() throws Exception
	{
//		setName(LangModel.String("labelTabbedProperties"));
		this.setLayout(gridBagLayout1);

		propsPanel.setLayout(paneLayout1);
		scalePanel.setLayout(borderLayout6);
		centerPanel.setLayout(borderLayout7);
		schemeLabel.setText("Схема");
		schemeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		domainLabel.setText("Домен");
		domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		ownerLabel.setText("Владелец");
		ownerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setText("Название");
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idLabel.setText("Идентификатор");
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		idTextField.setEnabled(false);
		ownerTextField.setEnabled(false);
		domainTextField.setEnabled(false);
		minScaleTextField.setEnabled(false);
		maxScaleTextField.setEnabled(false);
		longitudeTextField.setEnabled(false);
		latitudeTextField.setEnabled(false);

		minScaleLabel.setText("Мин. масштаб");
		minScaleLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		maxScaleLabel.setText("Макс. масштаб");
		maxScaleLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		longitudeLabel.setText("Долгота");
		longitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		latitudeLabel.setText("Широта");
		latitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descriptionScrollPane.getViewport().add(descriptionTextPane, null);
		jLabel1.setText("Комментарии");
		jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		bottomPanel.add(descriptionPanel, BorderLayout.CENTER);
		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
		this.add(ownerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(schemeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jLabel1, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(minScaleLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(maxScaleLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(minScaleTextField, new GridBagConstraints(1, 4, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(maxScaleTextField, new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(longitudeLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		this.add(latitudeLabel, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		this.add(longitudeTextField, new GridBagConstraints(3, 4, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(latitudeTextField, new GridBagConstraints(3, 5, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameTextField, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
		this.add(ownerTextField, new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(domainTextField, new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(schemeTextField, new GridBagConstraints(1, 3, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionScrollPane, new GridBagConstraints(1, 6, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idTextField, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	}

	public boolean save()
	{
		return false;
	}
	
	public boolean create()
	{
		return false;
	}
	
	public boolean delete()
	{
		return false;
	}
	
	public boolean open()
	{
		return false;
	}

}