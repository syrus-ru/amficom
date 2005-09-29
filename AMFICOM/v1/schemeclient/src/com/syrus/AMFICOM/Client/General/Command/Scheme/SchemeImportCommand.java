/*-
 * $Id: SchemeImportCommand.java,v 1.25 2005/09/29 13:20:56 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.xmlbeans.XmlException;

import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlConfigurationLibrary;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlPortType;
import com.syrus.AMFICOM.configuration.xml.XmlPortTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipmentSeq;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.xml.SchemeProtoGroupsDocument;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroupSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;

public class SchemeImportCommand extends ImportExportCommand {
//	private Map<Integer, SchemeProtoElement> straightMuffs;
//	private Map<Integer, SchemeProtoElement> inVrms;
//	private Map<Integer, SchemeProtoElement> outVrms;
//	private Set<EquipmentType> muffTypes;
	SchemeTabbedPane pane;
	
	private Map<SchemeCablePort, Set<SchemeCableThread>> portThreadsCount = new HashMap<SchemeCablePort, Set<SchemeCableThread>>();
	private static final Dimension SCHEME_SIZE = new Dimension(6720, 9520);
	
	public SchemeImportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
		
//		ImportUCMConverter c = new ImportUCMConverter(pane.getContext(), pane.getGraph());
//		try {
//			c.initMuffs();
//		} catch (ApplicationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		String user_dir = System.getProperty(USER_DIR);
		
		final String fileName = openFileForReading(user_dir);
		if(fileName == null)
			return;

		String ext = fileName.substring(fileName.lastIndexOf("."));
		File f = new File(fileName);

		if(ext.equals(".xml")) {
			try {
				if (f.getName().contains("proto")) {
					try {
						loadProtosXML(fileName);
					} catch (CreateObjectException e) {
						Log.errorMessage(e.getMessage());
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					} 
					ApplicationModel aModel = this.pane.getContext().getApplicationModel();
					aModel.setEnabled("menuSchemeImportCommit", true);
					aModel.fireModelChanged();
				} else if (f.getName().contains("config")) {
					try {
						loadConfigXML(fileName);
					} catch (CreateObjectException e) {
						Log.errorMessage(e.getMessage());
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					} 
										
					ApplicationModel aModel = this.pane.getContext().getApplicationModel();
					aModel.setEnabled("menuSchemeImportCommit", true);
					aModel.fireModelChanged();
				} else if (f.getName().contains("scheme")) {
					Scheme scheme;
					try {
						scheme = loadSchemeXML(fileName);
					} catch (CreateObjectException e) {
						Log.errorMessage(e.getMessage());
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					} catch (XmlException e) {
						Log.errorException(e);
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
								LangModelScheme.getString("Message.error.xml_format_incorrect"), //$NON-NLS-1$
								LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//	fix connection - connect threads at scheme
//					for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
//						SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
//						if (sourcePort != null) {
//							Set<SchemeCableThread> threads = this.portThreadsCount.get(sourcePort);
//							SchemeActions.connect(sourcePort, threads, true);
//						}
//						SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
//						if (targetPort != null) {
//							Set<SchemeCableThread> threads = this.portThreadsCount.get(targetPort);
//							SchemeActions.connect(targetPort, threads, false);
//						}
//					}
					
					putToGraph(scheme);
					ApplicationModel aModel = this.pane.getContext().getApplicationModel();
					aModel.setEnabled("menuSchemeImportCommit", true);
					aModel.fireModelChanged();
				}
			}	catch (CreateObjectException e) {
				Log.errorMessage(e.getMessage());
			} catch (ApplicationException e) {
				Log.errorException(e);
			} catch (XmlException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelScheme.getString("Message.error.parse_xml"),  //$NON-NLS-1$
						LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				Log.errorException(e);
			} catch (IOException e) {
				Log.errorException(e);
			}
		}
	}
	
	protected Scheme loadSchemeXML(String fileName) throws CreateObjectException, XmlException, IOException {
		Scheme scheme = null;
		File xmlfile = new File(fileName);
		
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		
		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}
		
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		
		XmlSchemeSeq xmlSchemes = doc.getSchemes();
		XmlScheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			XmlScheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(this.userId, xmlScheme);
//			scheme.setName(scheme.getName()	+ "(imported " + " from \'" + xmlfile.getName() + "\')");
			
			List<String> errorMessages = new LinkedList<String>();
			for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
				if (schemeCableLink.getSchemeCableThreads().size() == 0) {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_threads") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
				if (sourcePort != null) {
					this.portThreadsCount.put(sourcePort, schemeCableLink.getSchemeCableThreads());
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_source") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
				if (targetPort != null) {
					this.portThreadsCount.put(targetPort, schemeCableLink.getSchemeCableThreads());
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_target") + schemeCableLink.getName()); //$NON-NLS-1$
				}
			}
			if (errorMessages.size() > 0) {
				if (!ClientUtils.showConfirmDialog(new JScrollPane(new JList(errorMessages.toArray())),
						LangModelScheme.getString("Message.confirmation.continue_parse"))) { //$NON-NLS-1$
					throw new CreateObjectException("incorrect input data");
				}
			}
			
			try {
				ImportUCMConverter converter = new ImportUCMConverter(this.pane.getContext(), this.pane.getGraph());
				converter.parseSchemeCableLinks(scheme);
				converter.parseSchemeElements(scheme);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			
			break;
		}
		System.setProperty(USER_DIR,  user_dir);
		return scheme;
	}
	
	protected void loadConfigXML(String fileName) throws CreateObjectException, XmlException, IOException {
		File xmlfile = new File(fileName);
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.parse(xmlfile);
		
		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}
		
//		make sure default types loaded
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		String importType = doc.getImportType();
		
		XmlPortTypeSeq xmlPortTypes = doc.getPortTypes();
		if (xmlPortTypes != null) {
			for(XmlPortType xmlPortType : xmlPortTypes.getPortTypeArray()) {
				PortType.createInstance(this.userId, importType, xmlPortType);
			}
		}
		XmlLinkTypeSeq xmlLinkTypes = doc.getLinkTypes();
		if (xmlLinkTypes != null) {
			for(XmlLinkType xmlLinkType : xmlLinkTypes.getLinkTypeArray()) {
				LinkType.createInstance(this.userId, importType, xmlLinkType);
			}
		}
		List<String> errorMessages = new LinkedList<String>();
		XmlCableLinkTypeSeq xmlCableLinkTypes = doc.getCableLinkTypes();
		if (xmlCableLinkTypes != null) {
			for (final XmlCableLinkType cableLinkType : xmlCableLinkTypes.getCableLinkTypeArray()) {
				CableLinkType cableLinkType2 = CableLinkType.createInstance(this.userId, cableLinkType, importType);
				if (cableLinkType2.getCableThreadTypes(false).size() == 0) {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_type_no_threads") + cableLinkType2.getName()); //$NON-NLS-1$
				}
			}
		}
		if (errorMessages.size() > 0) {
			if (!ClientUtils.showConfirmDialog(new JScrollPane(new JList(errorMessages.toArray())),
					LangModelScheme.getString("Message.confirmation.continue_parse"))) { //$NON-NLS-1$
				throw new CreateObjectException("incorrect input data");
			}
		}
		XmlProtoEquipmentSeq xmlProtoEquipments = doc.getProtoEquipments();
		if (xmlProtoEquipments != null) {
			for(XmlProtoEquipment xmlEquipmentType : xmlProtoEquipments.getProtoEquipmentArray()) {
				ProtoEquipment.createInstance(this.userId, xmlEquipmentType, importType);
			}
		}
		XmlEquipmentSeq xmlEquipments = doc.getEquipments();
		if (xmlEquipments != null) {
			for(XmlEquipment xmlEquipment : xmlEquipments.getEquipmentArray()) {
				Equipment.createInstance(this.userId, xmlEquipment, importType);
			}
		}
		
		System.setProperty(USER_DIR,  user_dir);
	}
	
	protected void loadProtosXML(String fileName) throws CreateObjectException, XmlException, IOException {
		File xmlfile = new File(fileName);
		SchemeProtoGroupsDocument doc = SchemeProtoGroupsDocument.Factory.parse(xmlfile);
		
		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML");
		}
		
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		
		XmlSchemeProtoGroupSeq xmlProtoGroups = doc.getSchemeProtoGroups();
		XmlSchemeProtoGroup[] xmlProtoGroupsArray = xmlProtoGroups.getSchemeProtoGroupArray();
		for(int i = 0; i < xmlProtoGroupsArray.length; i++) {
			XmlSchemeProtoGroup xmlSchemeProtoGroup = xmlProtoGroupsArray[i];
			SchemeProtoGroup.createInstance(this.userId, xmlSchemeProtoGroup);
		}
		System.setProperty(USER_DIR,  user_dir);
	}
	
	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
	/*
	private void parseSchemeCableLinks(Scheme scheme) throws ApplicationException {
		EquivalentCondition condition1 = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
		Set<CableLinkType> cableLinkTypes1 = StorableObjectPool.getStorableObjectsByCondition(condition1, true);

		if (cableLinkTypes1.size() == 0) {
			Log.debugMessage("no CableLinkTypes found", Level.WARNING);
			return;
		}
		
		Map<CableLinkType, List<CableThreadType>> cableToThreadsMapping = new HashMap<CableLinkType, List<CableThreadType>>();
		for (CableLinkType type : cableLinkTypes1) {
			cableToThreadsMapping.put(type, ClientUtils.getSortedThreadTypes(type));	
		}
		Map<Integer, CableLinkType> cableLinkTypes = new HashMap<Integer, CableLinkType>();
		for (CableLinkType type : cableToThreadsMapping.keySet()) {
			List<CableThreadType> threadTypes = cableToThreadsMapping.get(type);
			cableLinkTypes.put(Integer.valueOf(threadTypes.size()), type);
		}
		
		final StorableObjectCondition condition = new TypicalCondition(CharacteristicTypeCodenames.COMMON_COLOUR, OperationSort.OPERATION_EQUALS, CHARACTERISTIC_TYPE_CODE, COLUMN_CODENAME);
		final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		final CharacteristicType characteristicType = characteristicTypes.isEmpty()
				? CharacteristicType.createInstance(this.userId, CharacteristicTypeCodenames.COMMON_COLOUR, "", "color", DataType.INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL)
				: characteristicTypes.iterator().next();
		assert characteristicType != null : NON_NULL_EXPECTED;
		final String name = characteristicType.getName();
		final String description = characteristicType.getDescription();

		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
			CableLinkType cableLinkType = schemeCableLink.getAbstractLinkType();
			if (cableLinkType == null) {
				Log.debugMessage("No real CableLinkType for " + schemeCableLink.getName(), Level.FINEST);
				CableLinkType suitableType;
				Integer fibers = Integer.valueOf(schemeCableLink.getSchemeCableThreads().size());
				suitableType = cableLinkTypes.get(fibers);
				if (suitableType == null) {
					for (Integer i : cableLinkTypes.keySet()) {
						if (i.compareTo(fibers) > 0) {
							suitableType = cableLinkTypes.get(i);
							break;
						}
					}
					if (suitableType == null) {
						suitableType = cableLinkTypes.values().iterator().next();
					}
				}
				schemeCableLink.setAbstractLinkType(suitableType);
			} else { // fix colors
				Iterator<CableThreadType> it1 = cableToThreadsMapping.get(cableLinkType).iterator();
				Iterator<SchemeCableThread> it2 = ClientUtils.getSortedCableThreads(schemeCableLink).iterator();
				for(; it1.hasNext() && it2.hasNext();) {
					final CableThreadType cableThreadType = it1.next();
					final SchemeCableThread schemeCableThread = it2.next();
					Characteristic.createInstance(this.userId,
							characteristicType,
							name,
							description,
							Integer.toString(cableThreadType.getColor()),
							schemeCableThread,
							true,
							true);
				}
			}
		}
	}
	
	/*private void parseSchemeElements(Scheme scheme) throws ApplicationException {
		initMuffs();
		initVrms();
		
		ApplicationContext internalContext =  new ApplicationContext();
		internalContext.setDispatcher(new Dispatcher());
		SchemeGraph invisibleGraph = new UgoTabbedPane(internalContext).getGraph();
		invisibleGraph.setMakeNotifications(false);
		Identifier domainId = LoginManager.getDomainId();

		Map<SchemeElement, SchemeElement> schemeElementMapping = new HashMap<SchemeElement, SchemeElement>();
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {

			try{
				if (schemeElement.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
				// if no real Scheme associated create new scheme with internal VRM's
				if (schemeElement.getScheme(false) == null) {
					Log.debugMessage("No real scheme for " + schemeElement.getName(), Level.FINEST);
				
					Scheme internalScheme = Scheme.createInstance(this.userId, schemeElement.getName(), IdlKind.BUILDING, domainId);
					internalScheme.setDescription(schemeElement.getDescription());
					internalScheme.setLabel(schemeElement.getLabel());
					internalScheme.setSymbol(schemeElement.getSymbol());
					schemeElement.setScheme(internalScheme, false);
					// graph for schemeCell
					ElementsTabbedPane schemePane = new ElementsTabbedPane(internalContext);
					SchemeGraph schemeGraph = schemePane.getGraph();
					schemePane.getCurrentPanel().getSchemeResource().setScheme(internalScheme);
					int grid = schemeGraph.getGridSize();
					schemeGraph.setMakeNotifications(false);
					SchemeImageResource res = internalScheme.getSchemeCell();
					if (res == null) {
						res = SchemeObjectsFactory.createSchemeImageResource();
						internalScheme.setSchemeCell(res);
					}
					
					//	next create vrm for every cable port
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively();
					int inCount = 0, outCount = 0;
					for (SchemeCablePort cablePort : existingCablePorts) {
//						SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
						int fibers = 8;
						Set<SchemeCableThread> threads = this.portThreadsCount.get(cablePort);
						if (threads != null) {
							fibers = threads.size();
						}
						boolean isInput = cablePort.getDirectionType() == IdlDirectionType._IN;
						SchemeProtoElement suitableVrm = getSuitableProto(isInput ? this.inVrms : this.outVrms, Integer.valueOf(fibers));
						
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(internalScheme, suitableVrm);
						// substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, cablePort);

						// write it to cell
						SchemeImageResource res1 = newSchemeElement.getSchemeCell();
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res1.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						// place to scheme cell
						Point p = isInput 
								?	new Point(grid * 15, grid * (10 + 20 * inCount))
								: new Point(grid * 45, grid * (10 + 20 * outCount));
						clonedObjects = SchemeActions.openSchemeImageResource(schemeGraph, res1, true, p, false);
						// add top level port
						CablePortCell portCell = SchemeActions.findCablePortCellById(schemeGraph, cablePort.getId());
						CreateBlockPortAction.create(schemeGraph, portCell);

						if (isInput) {
							inCount++;
						} else {
							outCount++;
						}
					}
					res.setData((List)schemeGraph.getArchiveableState());
					// create UGO
					if (existingCablePorts.size() > 0) {
						new CreateTopLevelSchemeAction(schemePane).execute();
					}
					
					internalScheme.setParentSchemeElement(null, false);
					// next create SchemeElement from Scheme
					SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(scheme, internalScheme);
					newSchemeElement.setName(schemeElement.getName());
					newSchemeElement.setDescription(schemeElement.getDescription());
					newSchemeElement.setSiteNode(schemeElement.getSiteNode());
					schemeElementMapping.put(schemeElement, newSchemeElement);
				}
			} else if (schemeElement.getKind() == IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER) {
				// if no real EqT associated
				if (true) { //schemeElement.getEquipmentType() == null) {
					Log.debugMessage("No real eqt for " + schemeElement.getName(), Level.FINEST);
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively();
					if (existingCablePorts.size() < 3) { // straight muff
						// count how many threads in connected fibers
						int maxFibers = 8;
						for (SchemeCablePort cablePort : existingCablePorts) {
//							SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
							Set<SchemeCableThread> threads = this.portThreadsCount.get(cablePort);
							if (threads != null) {
								maxFibers = Math.max(maxFibers, threads.size());
							}
						}
						SchemeProtoElement suitableMuff = getSuitableProto(this.straightMuffs, Integer.valueOf(maxFibers));
						
						SchemeImageResource schemeCell = SchemeObjectsFactory.createSchemeImageResource();  
						schemeCell.setImage(suitableMuff.getSchemeCell().getImage().clone());
						schemeElement.setSchemeCell(schemeCell);
						
						SchemeImageResource ugoCell = SchemeObjectsFactory.createSchemeImageResource();  
						ugoCell.setImage(suitableMuff.getUgoCell().getImage().clone());
						schemeElement.setUgoCell(ugoCell);

						// next create SchemeElement from suitableMuff
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(scheme, suitableMuff);
						newSchemeElement.setName(schemeElement.getName());
						newSchemeElement.setDescription(schemeElement.getDescription());
						newSchemeElement.setSiteNode(schemeElement.getSiteNode());

						// and substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, existingCablePorts);

						// write it to cell
						SchemeImageResource res1 = newSchemeElement.getSchemeCell();
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res1.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						SchemeImageResource res2 = newSchemeElement.getUgoCell();
						clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res2, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res2.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						schemeElementMapping.put(schemeElement, newSchemeElement);
					} else { // split muff
						// create muff like scheme
						SchemeElement muff = SchemeElement.createInstance(this.userId, schemeElement.getName(), scheme);
						muff.setEquipmentType(this.muffTypes.iterator().next()); // schemeElement.getEquipmentType()
						muff.setDescription(schemeElement.getDescription());

						// graph for schemeCell
						ElementsTabbedPane schemePane = new ElementsTabbedPane(internalContext);
						SchemeGraph schemeGraph = schemePane.getGraph();
						schemePane.getCurrentPanel().getSchemeResource().setSchemeElement(muff);
						int grid = schemeGraph.getGridSize();
						schemeGraph.setMakeNotifications(false);
						SchemeImageResource res = muff.getSchemeCell();
						if (res == null) {
							res = SchemeObjectsFactory.createSchemeImageResource();
							muff.setSchemeCell(res);
						}
						
						//	next create vrm for every cable port
						int inCount = 0, outCount = 0;
						for (SchemeCablePort cablePort : existingCablePorts) {
//							SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
							int fibers = 8;
							Set<SchemeCableThread> threads = this.portThreadsCount.get(cablePort);
							if (threads != null) {
								fibers = threads.size();
							}
							boolean isInput = cablePort.getDirectionType() == IdlDirectionType._IN;
							SchemeProtoElement suitableVrm = getSuitableProto(isInput ? this.inVrms : this.outVrms, Integer.valueOf(fibers));
							
							SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(muff, suitableVrm);
							// substitute existing cable ports
							Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
							substituteExistingPorts(clonedIds, cablePort);

							// write it to cell
							SchemeImageResource res1 = newSchemeElement.getSchemeCell();
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							res1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							// place to scheme cell
							Point p = isInput 
									?	new Point(grid * 15, grid * (10 + 20 * inCount))
									: new Point(grid * 45, grid * (10 + 20 * outCount));
							clonedObjects = SchemeActions.openSchemeImageResource(schemeGraph, res1, true, p, false);
							// add top level port
							CablePortCell portCell = SchemeActions.findCablePortCellById(schemeGraph, cablePort.getId());
							CreateBlockPortAction.create(schemeGraph, portCell);

							if (isInput) {
								inCount++;
							} else {
								outCount++;
							}
						}
						res.setData((List)schemeGraph.getArchiveableState());
						// create UGO
						if (existingCablePorts.size() > 0) {
							new CreateTopLevelSchemeAction(schemePane).execute();
						}
						schemeElementMapping.put(schemeElement, muff);
					} 
				}
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (SchemeElement schemeElement : schemeElementMapping.keySet()) {
			SchemeElement newSchemeElement = schemeElementMapping.get(schemeElement);
			newSchemeElement.setEquipment(schemeElement.getEquipment());
			schemeElement.setEquipment(null);
			StorableObjectPool.delete(schemeElement.getId());
			StorableObjectPool.flush(schemeElement.getId(), this.userId, false);
			scheme.addSchemeElement(newSchemeElement);
		}
	}*/
	
	private void putToGraph(Scheme scheme) throws ApplicationException {
		scheme.setWidth(SCHEME_SIZE.width);
		scheme.setHeight(SCHEME_SIZE.height);
		
		ApplicationContext aContext =  this.pane.getContext();
		Dispatcher internalDispatcher = aContext.getDispatcher();
		SchemeGraph schemeGraph = this.pane.getGraph();
		int grid = schemeGraph.getGridSize();
		internalDispatcher.firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));
		
		// determine bounds
		double xmin = 180, ymin = 90, xmax = -180, ymax = -90; 
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
			Equipment equipment = schemeElement.getEquipment();
			if (equipment != null) {
				double x0 = equipment.getLatitude();
				double y0 = equipment.getLongitude();
				xmin = Math.min(xmin, x0);
				ymin = Math.min(ymin, y0);
				xmax = Math.max(xmax, x0);
				ymax = Math.max(ymax, y0);
			}
		}
		
		double kx = (xmax - xmin == 0) ? 1 : (SCHEME_SIZE.width - grid * 20) / (xmax - xmin);
		double ky = (ymax - ymin == 0) ? 1 : (SCHEME_SIZE.height - grid * 20) / (ymax - ymin);
		
		Set<Identifier> placedObjects = ImportUCMConverter.getPlacedObjects(schemeGraph);
		
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (!ImportUCMConverter.contains(placedObjects, schemeElement)) {
				Equipment equipment = schemeElement.getEquipment();
				Point p;
				if (equipment != null) {
					double x0 = equipment.getLatitude();
					double y0 = equipment.getLongitude();
					p = new Point((int)(grid * 10 + (x0 - xmin) * kx), (grid * 5 + SCHEME_SIZE.height - (int)((y0 - ymin) * ky))); 
				} else {
					Log.errorMessage("No equipment for " + schemeElement.getName() + " (" + schemeElement.getId() + ")");
					p = new Point((int)(SCHEME_SIZE.width * Math.random()), (int)(SCHEME_SIZE.height * Math.random()));
				}
				if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_CONTAINER) {
					internalDispatcher.firePropertyChange(new SchemeEvent(this, schemeElement.getScheme(false).getId(), p, SchemeEvent.INSERT_SCHEME));	
				} else {
					internalDispatcher.firePropertyChange(new SchemeEvent(this, schemeElement.getId(), p, SchemeEvent.INSERT_SCHEMEELEMENT));
				}
			}
		}
		
		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
			Point p = null;
			SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
			SchemeCablePort targetPort = schemeCableLink.getSourceAbstractSchemePort();
			if (sourcePort != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, sourcePort.getId());
				if (cell != null) {
					try {
						DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
						PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
						p = sourceView.getBounds().getLocation();
					} catch (CreateObjectException e) {
						Log.errorException(e);
						return;
					}
				} else {
					Log.errorMessage("No source found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				}
			} 
			if (p == null && targetPort != null) {
				CablePortCell cell = SchemeActions.findCablePortCellById(schemeGraph, targetPort.getId());
				if (cell != null) {
					try {
						DefaultPort source = SchemeActions.getSuitablePort(cell, schemeCableLink.getId());
						PortView sourceView = (PortView)schemeGraph.getGraphLayoutCache().getMapping(source, false);
						p = sourceView.getBounds().getLocation();
					} catch (CreateObjectException e) {
						Log.errorMessage(e.getMessage());
						return;
					}
				} else {
					Log.errorMessage("No target found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				}
			} 
			if (p == null) {
				Log.errorMessage("Both source and target not found for " + schemeCableLink.getName() + " (" + schemeCableLink.getId() + ")");
				p = new Point(10 * grid, 10 * grid); 
			}
			
			internalDispatcher.firePropertyChange(new SchemeEvent(this, schemeCableLink.getId(), p, SchemeEvent.INSERT_SCHEME_CABLELINK));
		}
		schemeGraph.setMakeNotifications(true);
	}
/*
	private void initMuffs() throws ApplicationException {
		TypicalCondition condition1 = new TypicalCondition(EquipmentTypeCodename.MUFF.stringValue(), OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		this.muffTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> muffTypeIds = new HashSet<Identifier>();
		for (EquipmentType eqt : muffTypes) {
			muffTypeIds.add(eqt.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(muffTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> muffs = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (muffs.size() == 0) {
			Log.debugMessage("No muffs found", Level.WARNING);
			return;
		}

		// put <number of ports, muff>
		this.straightMuffs = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement muff : muffs) {
			if (muff.getParentSchemeProtoElement() == null) {
				Set<SchemeCablePort> cablePorts = muff.getSchemeCablePortsRecursively();
				if (cablePorts.size() == 2) {
					this.straightMuffs.put(Integer.valueOf(muff.getSchemePortsRecursively().size() / 2), muff);
				}
			}
		}
	}
	
	private void initVrms() throws ApplicationException {
		TypicalCondition condition1 = new TypicalCondition(EquipmentTypeCodename.CABLE_PANEL.stringValue(), OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		Set<EquipmentType> vrmTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> vrmTypeIds = new HashSet<Identifier>();
		for (EquipmentType eqt : vrmTypes) {
			vrmTypeIds.add(eqt.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(vrmTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> vrms = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (vrms.size() == 0) {
			Log.debugMessage("No vrms found", Level.WARNING);
			return;
		}

		// put <number of ports, muff>
		this.inVrms = new HashMap<Integer, SchemeProtoElement>();
		this.outVrms = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement vrm : vrms) {
			if (vrm.getParentSchemeProtoElement() == null) {
				Set<SchemePort> ports = vrm.getSchemePortsRecursively();
				if (ports.size() > 0) {
					SchemePort port = ports.iterator().next();
					if (port.getDirectionType() == IdlDirectionType._OUT) {
						this.inVrms.put(Integer.valueOf(ports.size()), vrm);
					} else {
						this.outVrms.put(Integer.valueOf(ports.size()), vrm);
					}
				}
			}
		}
	}
	
	private SchemeProtoElement getSuitableProto(Map<Integer, SchemeProtoElement> mapping, Integer num) {
		//  search for proto with corresponding number of ports, if nothing found search with larger number, 
		//  if not again - get any
		SchemeProtoElement suitableProto = mapping.get(num);
		if (suitableProto == null) {
			for (Integer i : mapping.keySet()) {
				if (i.compareTo(num) > 0) {
					suitableProto = mapping.get(i);
					break;
				}
			}
			if (suitableProto == null) {
				suitableProto = mapping.values().iterator().next();
			}
		}
		return suitableProto;
	}
	
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, Set<SchemeCablePort> existingCablePorts) throws ApplicationException {
		Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false);
				for (SchemeCablePort existingPort : existingCablePorts) {
					if (cport.getDirectionType() == existingPort.getDirectionType()) {
						existingPortsMapping.put(id1, existingPort);
					}
				}
			}
		}
		for (Identifier id : existingPortsMapping.keySet()) {
			SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
			SchemeCablePort portToAdd = existingPortsMapping.get(id);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			portToAdd.setParentSchemeDevice(parent);
			portToAdd.setPortType(portToRemove.getPortType());
			clonedIds.put(id, portToAdd.getId());
			StorableObjectPool.delete(portToRemove.getId());
			StorableObjectPool.flush(portToRemove.getId(), this.userId, false);
		}
	}
	
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, SchemeCablePort existingCablePort) throws ApplicationException {
		Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false);
				if (cport.getDirectionType() == existingCablePort.getDirectionType()) {
					existingPortsMapping.put(id1, existingCablePort);
					break;
				}
			}
		}
		for (Identifier id : existingPortsMapping.keySet()) {
			SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
			SchemeCablePort portToAdd = existingPortsMapping.get(id);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			portToAdd.setParentSchemeDevice(parent);
			portToAdd.setPortType(portToRemove.getPortType());
			portToAdd.setName(portToRemove.getName());
			clonedIds.put(id, portToAdd.getId());
			StorableObjectPool.delete(portToRemove.getId());
			StorableObjectPool.flush(portToRemove.getId(), this.userId, false);
		}
	}*/
}
