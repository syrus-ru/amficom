package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// ����� ��������� �����
//================================================================================================================
public class ViewSchemeFrame extends JInternalFrame implements OperationListener
{
	private BorderLayout borderLayout1 = new BorderLayout();
	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	public  SchemePanelNoEdition schemePanel;//���� public SchemeSimplePanel schemePanel;
	public SchemeGraph graph;
	//-------------------------------------------------------------------------------------------------------------
	public ViewSchemeFrame(OptimizeMDIMain mdiMain)
	{	super();
		try
		{  dispatcher = mdiMain.getInternalDispatcher();
			 this.mdiMain = mdiMain;
			 schemePanel = new SchemePanelNoEdition(mdiMain.aContext);// ������ ������ �� ������
			 //���� ��� schemePanel = new SchemeSimplePanel(mdiMain.aContext);// ������ ������ �� ������
			 graph = schemePanel.getGraph();

			 dispatcher.register(this, "scheme_is_opened");
			 dispatcher.register(this, "scheme_updated_event");

			 jbInit();
			 place();
			 update(mdiMain.scheme);
		}
		catch (Exception e)
		{  e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	// �������� �������� (������ �������� ) ������� ����� ������������ paths
	public void update(Collection paths)
	{  //���� graph.updatePathsAtScheme(paths); // �-� ��������� ���� �����O �� ��������, � ����� ���� ��������� ��������
		schemePanel.updatePathsAtScheme(paths);
	    //���� graph.scheme.paths = paths;
		schemePanel.scheme.paths = paths;
	}
	//-------------------------------------------------------------------------------------------------------------
	// �������� ���� ����� ������
	public void update(Scheme scheme)
	{ // ���� ������ ����������� ����� ��� �� �������, �� ������ �
		if(schemePanel == null)
		{  schemePanel = new SchemePanelNoEdition(mdiMain.aContext);// �������������� ������ �� ������
			//schemePanel = new SchemeSimplePanel(mdiMain.aContext);// �������������� ������ �� ������
		}
		// ������� ���������� �����
    GraphActions.clearGraph(graph);
		// ����������� ����� �����
    schemePanel.scheme = scheme;
    if(schemePanel.scheme != null)
    { schemePanel.scheme.unpack();
      graph.setFromArchivedState(scheme.serializable_cell);
      System.out.println("update(Scheme scheme): graph = " + graph);
      System.out.println("update(Scheme scheme): scheme = " + schemePanel.scheme.getId() + "\n" + schemePanel.scheme);
    }
    else
    { System.err.println("Current map has no scheme attached! ViewSchemeFrame.update(): scheme = null");
    }
	}
	//-------------------------------------------------------------------------------------------------------------
	//�������������������� � ����������
	public void place()
	{		Dimension dim = mdiMain.scrollPane.getSize();
			int width = (int)( (1.0-0.22)*dim.width ), height = (int)( (1.0-0.2)*dim.height );
			setBounds( 0, 0, width, height );
			setVisible(true);
	}
 //-------------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
                this.setTitle( LangModelOptimize.getString( "frameSchemeTitle") );
		setDefaultCloseOperation(HIDE_ON_CLOSE);// �� ���������, � ������
		this.getContentPane().setLayout(borderLayout1);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setResizable(true);
		this.getContentPane().add(schemePanel, BorderLayout.CENTER);

		this.schemePanel.getGraph().setBorderVisible(false);//������� ����� �� ������

		if( mdiMain.scheme != null) { setTitle(mdiMain.scheme.getName());}
	}
	//-------------------------------------------------------------------------------------------------------------
	// ���������� �������
	public void operationPerformed(OperationEvent ae)
	{ //���� ������� ����� �����, �� ��������� ������
		if(ae.getActionCommand().equals("scheme_is_opened"))
		{  update(mdiMain.scheme);
		}
		if(ae.getActionCommand().equals("scheme_updated_event"))
		{  update(mdiMain.scheme.paths);
		}

//		if(ae.getActionCommand().equals(MapNavigateEvent.type))
//		{	MapNavigateEvent mne = (MapNavigateEvent )ae;
//			if(mne.MAP_MAP_PATH_SELECTED)
//			{	// tablePane.setSelected(mne.pathID);
//				// tablePane.setSelected(Pool.get(MapTransmissionPathElement.typ, mne.pathID));
//				tablePane.setSelected(Pool.get(MapTransmissionPathElement.typ, mne.mappathID));
//			}
//		}
	}
}
