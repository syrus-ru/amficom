/*-
 * $Id: SchemeImportCommand.java,v 1.5 2005/09/07 18:33:01 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlComplementor;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;

public class SchemeImportCommand extends AbstractCommand {
	static {
		XmlComplementorRegistry.registerComplementor(SCHEME_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject scheme,
					final String importType)
			throws CreateObjectException, UpdateObjectException {
				((XmlScheme) scheme).setDomainId(LoginManager.getDomainId().getXmlTransferable(importType));
			}
		});
	}

	@Override
	public void execute() {
		String user_dir = System.getProperty("user.dir");
		
		final String fileName = openFileForReading(user_dir);
		if(fileName == null)
			return;

		String ext = fileName.substring(fileName.lastIndexOf("."));

		if(ext.equals(".xml")) {
			try {
				Scheme scheme = loadXML(fileName);
				parseScheme(scheme);
			} catch (ApplicationException e) {
				Log.errorException(e);
			} catch (XmlException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelScheme.getString("Message.error.parse_xml"), LangModelScheme.getString("Message.error"), JOptionPane.ERROR_MESSAGE);
				Log.errorException(e);
			} catch (IOException e) {
				Log.errorException(e);
			}
		}
	}
	
	protected Scheme loadXML(String fileName) throws CreateObjectException, XmlException, IOException {
		Scheme scheme = null;
		
		File xmlfile = new File(fileName);
		
//		Create an instance of a type generated from schema to hold the XML.
//		Parse the instance into the type generated from the schema.
		
		System.out.println("start parse scheme");
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		System.out.println("end parse scheme");
		
//		if(!validateXml(doc)) {
//			throw new XmlException("Invalid XML");
//		}
		
//		make sure default types loaded
		String user_dir = System.getProperty("user.dir");
		System.setProperty("user.dir",  xmlfile.getParent());
		
		XmlSchemeSeq xmlSchemes = doc.getSchemes();
		XmlScheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			XmlScheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(LoginManager.getUserId(), xmlScheme, "ucm");
			scheme.setName(scheme.getName()	+ "(imported " + " from \'" + xmlfile.getName() + "\')");
			break;
		}
		System.setProperty("user.dir",  user_dir);
		return scheme;
	}
	
	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				"esf",
				"Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

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
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
	
	private void parseScheme(Scheme scheme) throws ApplicationException {
		TypicalCondition condition1 = new TypicalCondition(EquipmentTypeCodename.MUFF.stringValue(), OperationSort.OPERATION_EQUALS, ObjectEntities.EQUIPMENT_TYPE_CODE, StorableObjectWrapper.COLUMN_CODENAME);
		Set<EquipmentType> muffTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
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
		Map<Integer, SchemeProtoElement> straightMuffs = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement muff : muffs) {
			Set<SchemeCablePort> cablePorts = muff.getSchemeCablePortsRecursively();
			if (cablePorts.size() == 2) {
				straightMuffs.put(muff.getSchemePortsRecursively().size() / 2, muff);
			}
		}
		
		for (SchemeElement schemeElement : scheme.getSchemeElements()) {
 			if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_CONTAINER) {
				// if no real Scheme associated
				if (schemeElement.getScheme() == null) {
					Log.debugMessage("No real scheme for " + schemeElement.getName(), Level.FINEST);
				}
			} else if (schemeElement.getKind().value() == IdlSchemeElementKind._SCHEME_ELEMENT_CONTAINER) {
				// if no real EqT associated
				if (schemeElement.getEquipmentType() == null) {
					Log.debugMessage("No real eqt for " + schemeElement.getName(), Level.FINEST);
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively();
					if (existingCablePorts.size() == 2) { // straight muff
						// count how many threads in connected fibers
						int maxFibers = 0;
						for (SchemeCablePort cablePort : existingCablePorts) {
							SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
							cableLink = cablePort.getAbstractSchemeLink();
							if (cableLink != null) {
								maxFibers = Math.max(maxFibers, cableLink.getSchemeCableThreads().size());
							}
						}
						//  search for muff with corresponding number of ports, if nothing found search with larger number, 
						//  if not again - get any
						SchemeProtoElement suitableMuff = null;
						suitableMuff = straightMuffs.get(maxFibers);
						if (suitableMuff == null) {
							for (Integer i : straightMuffs.keySet()) {
								if (i > maxFibers) {
									suitableMuff = straightMuffs.get(i);
									break;
								}
							}
							if (suitableMuff == null) {
								suitableMuff = straightMuffs.values().iterator().next();
							}
						}
						// next create SchemeElement from suitableMuff
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(scheme, suitableMuff);
						// and substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
						for (Identifier id : clonedIds.keySet()) {
							if (id.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
								SchemeCablePort cport = StorableObjectPool.getStorableObject(id, false);
								for (SchemeCablePort existingPort : existingCablePorts) {
									if (cport.getDirectionType().equals(existingPort.getDirectionType())) {
										existingPortsMapping.put(id, existingPort);
									}
								}
							}
						}
						for (Identifier id : existingPortsMapping.keySet()) {
							SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
							SchemeCablePort portToAdd = existingPortsMapping.get(id);
							
							SchemeDevice parent = portToRemove.getParentSchemeDevice();
							portToAdd.setParentSchemeDevice(parent);
							clonedIds.put(id, portToAdd.getId());
							
							StorableObjectPool.delete(portToRemove.getId());
						}
						
						// write it to cell
						UgoTabbedPane pane = new UgoTabbedPane();
						SchemeGraph graph = pane.getGraph();
						graph.setMakeNotifications(false);
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.insertSEbyPE(graph, newSchemeElement, null, false);
						
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						
						newSchemeElement.getSchemeCell().setData((List<Object>)graph.getArchiveableState());
						

						
					} else if (existingCablePorts.size() > 2) { // split muff
						
					} else { // unknown device
						Log.debugMessage("Unknown object with " + existingCablePorts.size() + " cable ports", Level.FINE);
					}
				}
			}
		}
	}
}
