package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

public class SchemeSaveCommand extends AbstractCommand {
	public static final int CANCEL = 0;
	public static final int OK = 1;
	public int ret_code = CANCEL;
	
	ElementsTabbedPane schemeTab;

	public SchemeSaveCommand(ElementsTabbedPane schemeTab) {
		this.schemeTab = schemeTab;
	}

	@Override
	public Object clone() {
		return new SchemeSaveCommand(this.schemeTab);
	}

	@Override
	public void execute() {
		if (this.schemeTab.getCurrentPanel().isTopLevelSchemeMode()) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.save_top_level"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeGraph graph = this.schemeTab.getGraph();

		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.empty_scheme"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.ungrouped_device"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.component_not_found"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		SchemeResource res = this.schemeTab.getCurrentPanel().getSchemeResource();

		if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) // сохраняем компонент
		{
			SchemeElement se = res.getSchemeElement();

			// add internal objects - SL, SE
			Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
			Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
			Object[] objects = graph.getRoots();
			for (Object object : objects) {
				if (object instanceof DefaultLink)
					schemeLinks.add(((DefaultLink)object).getSchemeLink());
				else if (object instanceof DeviceGroup) {
					SchemeElement schemeElement = ((DeviceGroup)object).getSchemeElement();
					assert schemeElement != null;
					if (!schemeElement.equals(se))
						schemeElements.add(schemeElement);
				}
			}
			try {
				se.setSchemeLinks(schemeLinks);
				se.setSchemeElements(schemeElements, false);
			} catch (ApplicationException e1) {
				Log.errorException(e1);
			}
			
			//	create SchemeImageResource
			if (se.getSchemeCell() == null) {
				try {
					se.setSchemeCell(SchemeObjectsFactory.createSchemeImageResource());
				} catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}
			se.getSchemeCell().setData((List) graph.getArchiveableState());

			for (Iterator it = this.schemeTab.getAllPanels().iterator(); it.hasNext();) {
				ElementsPanel p = (ElementsPanel) it.next();
				SchemeResource res1 = p.getSchemeResource();
				if (res1.getCellContainerType() == SchemeResource.SCHEME) {
					Scheme s = res1.getScheme();
					if (s.equals(se.getParentScheme())) {
						// TODO refreshing view (ugo)
						
						this.schemeTab.setGraphChanged(true);
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								se.getName() + LangModelScheme.getString("Message.information.element_saved_in_scheme") + s.getName(),  //$NON-NLS-1$
								LangModelScheme.getString("Message.information"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			this.schemeTab.setGraphChanged(false);
			return;
		} else if (res.getCellContainerType() == SchemeResource.SCHEME) // сохраняем схему
		{
			Scheme scheme = res.getScheme();
//			if (scheme.getUgoCell() == null) {
//				int ret = JOptionPane.showConfirmDialog(
//								Environment.getActiveWindow(),
//								LangModelScheme.getString("Message.confirmation.no_ugo"), //$NON-NLS-1$
//								LangModelScheme.getString("Message.confirmation"),  //$NON-NLS-1$
//								JOptionPane.YES_NO_OPTION);
//				if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION)
//					return;
//			}
			
			// add internal objects - SL, SCL, SE
//			Set<SchemeLink> schemeLinks = new HashSet<SchemeLink>();
//			Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>();
//			Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
//			Object[] objects = graph.getRoots();
//			for (Object object : objects) {
//				if (object instanceof DefaultLink)
//					schemeLinks.add(((DefaultLink)object).getSchemeLink());
//				if (object instanceof DefaultCableLink)
//					schemeCableLinks.add(((DefaultCableLink)object).getSchemeCableLink());
//				else if (object instanceof DeviceGroup) {
//					SchemeElement schemeElement = ((DeviceGroup)object).getSchemeElement();
//					schemeElements.add(schemeElement);
//				}
//			}
			try {
//				scheme.setSchemeLinks(schemeLinks);
//				scheme.setSchemeCableLinks(schemeCableLinks);
//				scheme.setSchemeElements(schemeElements);
			
				//	create SchemeImageResource
				SchemeImageResource schemeIr = scheme.getSchemeCell();
				if (schemeIr == null)
					schemeIr = SchemeObjectsFactory.createSchemeImageResource();
				
//				try {
//					GraphActions.serialyze((List) graph.getArchiveableState());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				long start = System.currentTimeMillis();
				schemeIr.setData((List) graph.getArchiveableState());
				scheme.setSchemeCell(schemeIr);
				System.err.println("written for : " + (System.currentTimeMillis() - start) + "ms (" + schemeIr.getImage().length + " bytes)");
				Identifier userId = LoginManager.getUserId();
				StorableObjectPool.flush(scheme.getReverseDependencies(), userId, false);

				this.schemeTab.setGraphChanged(false);
				
				if (scheme.getUgoCell() == null) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.no_ugo"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);					
				} else {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							scheme.getName() + " " + LangModelScheme.getString("Message.information.scheme_saved"),  //$NON-NLS-1$ //$NON-NLS-2$
							LangModelScheme.getString("Message.information"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
	}
}
