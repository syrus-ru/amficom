/*-
 * $Id: SchemeImportCommand.java,v 1.28 2005/10/04 06:15:38 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.Dimension;
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

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
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
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
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
					
					scheme.setWidth(SCHEME_SIZE.width);
					scheme.setHeight(SCHEME_SIZE.height);
					SchemeActions.putToGraph(scheme, this.pane);
					
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
	
	protected Scheme loadSchemeXML(String fileName) throws XmlException, IOException, ApplicationException {
		Scheme scheme = null;
		File xmlfile = new File(fileName);
		
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
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
			for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks(false)) {
				if (schemeCableLink.getSchemeCableThreads(false).size() == 0) {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_threads") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
				if (sourcePort != null) {
					this.portThreadsCount.put(sourcePort, schemeCableLink.getSchemeCableThreads(false));
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_source") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
				if (targetPort != null) {
					this.portThreadsCount.put(targetPort, schemeCableLink.getSchemeCableThreads(false));
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_target") + schemeCableLink.getName()); //$NON-NLS-1$
				}
			}
//			if (errorMessages.size() > 0) {
//				if (!ClientUtils.showConfirmDialog(new JScrollPane(new JList(errorMessages.toArray())),
//						LangModelScheme.getString("Message.confirmation.continue_parse"))) { //$NON-NLS-1$
//					throw new CreateObjectException("incorrect input data");
//				}
//			}
			
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
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
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
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
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
	
}
