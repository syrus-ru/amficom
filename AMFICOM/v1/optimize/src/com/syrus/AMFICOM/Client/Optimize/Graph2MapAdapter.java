package com.syrus.AMFICOM.Client.Optimize;

import com.syrus.AMFICOM.Client.General.Event.*;

//---------------------------------------------------------------------------------------------------------------
// ��������� ���� �� �������, � ������� �������� Dll � ������, � ������� �������� �����
public class Graph2MapAdapter
{
	//private ISMMapContext ismMapContext;
	private OptimizeMDIMain mdiMain;
	private Dispatcher dispatcher;
	//-----------------------------------------------------------------------------------------------------------
	public Graph2MapAdapter(OptimizeMDIMain mdiMain)
	{  this.mdiMain = mdiMain;
		// ismMapContext = mdiMain.ismMapContext;// ismMapContext �������� ����� ��, ����� � mdiMain
		 this.dispatcher = mdiMain.getInternalDispatcher();
	}
	//-----------------------------------------------------------------------------------------------------------
	// �������� �� ������ ������ : ��������� ��� ��������������� � ���� Solution::OptimizedData[] � ismMapContext
	// ����� �� ��������� ������ � ismMapContext-e
	public void CreateMapByGraph(Solution solution)
	{
//		//��������� ������ ������� ���������� ��������
//		OptimizedData[] optimizedData = new OptimizedData[]
//				{  new OptimizedData("Nettest_OTDR_proto1",
//															new String[]{ "mapeq301", "maplink392", "mapeq302", "maplink393",	"mapeq301", "maplink394",
//																						"mapeq304", "maplink396","mapeq302", "maplink397","mapeq303", "maplink395", "mapeq304"}
//														),
//					 new OptimizedData("Nettest_OTDR_proto1", new String[]{"mapnode599", "maplink398","mapeq303"})
//				};


/*
		if(ismMapContext == null)
			 return;
		cleanIteration();// ������� ismMapContext �� ���������� �������
		// � ���� ������ �������� optimizedData[i].vectorOfNodesIDs[0], �.�. ��������� �����, ���� ���� ��������� KIS
		Vector vec = new Vector();
		//	�������� ��������
		MapNodeElement buferStartNode = null, buferEndNode = null, theNode = null;;
		MapNodeLinkElement myNodeLink = null;
		MapPhysicalLinkElement myPhysicalLinkElement = null;
		DataSourceInterface dataSource = mdiMain.getContext().getDataSourceInterface();
		Hashtable kishash = new Hashtable(), linkhash = new Hashtable();

		for (int i = 0; i < optimizedData.length; i++)
		{	MapNodeElement node = ismMapContext.getNode(optimizedData[i].vectorOfNodesIDs[0]);
			buferEndNode = node;
			if ( !vec.contains(optimizedData[i].vectorOfNodesIDs[0]) )
			{   //��������� �����
					vec.add(optimizedData[i].vectorOfNodesIDs[0]);
					theNode = new MapKISNodeElement( dataSource.GetUId(MapKISNodeElement.typ),
																					 //������� �������
																					 new SxDoublePoint (node.getAnchor().x-0.0007, node.getAnchor().y+0.0007),
																					 ismMapContext,
																					 ismMapContext.curentStandartMapElementSize,
																					 (MapKISProtoElement)Pool.get(MapKISProtoElement.typ, optimizedData[i].kis_type_id),
																					 new Vector() );
					kishash.put(optimizedData[i].vectorOfNodesIDs[0], theNode);
					Pool.put(MapKISNodeElement.typ, theNode.getId(), theNode);//��������� � ���
					//Nettest_OTDR_proto1
					buferStartNode = theNode;
					ismMapContext.addNode(theNode);
					//������
					myNodeLink = new MapNodeLinkElement( dataSource.GetUId(MapNodeLinkElement.typ),
																							 theNode,
																							 node,
																							 ismMapContext );
					Pool.put(MapNodeLinkElement.typ, myNodeLink.getId(), myNodeLink);
					ismMapContext.addNodeLink(myNodeLink);
					myPhysicalLinkElement = new MapPhysicalLinkElement(	dataSource.GetUId(MapPhysicalLinkElement.typ),
																															theNode,
																															node,
																															ismMapContext );
					myPhysicalLinkElement.nodeLink_ids.add(myNodeLink.getID());
					Pool.put(MapPhysicalLinkElement.typ, myPhysicalLinkElement.getId(), myPhysicalLinkElement);//��������� � ���

					ismMapContext.addPhysicalLink(myPhysicalLinkElement);
					linkhash.put(optimizedData[i].vectorOfNodesIDs[0], myPhysicalLinkElement);
			 }
			 else
			 {   buferStartNode = (MapKISNodeElement) kishash.get(optimizedData[i].vectorOfNodesIDs[0]);
					 myPhysicalLinkElement = (MapPhysicalLinkElement) linkhash.get(optimizedData[i].vectorOfNodesIDs[0]);
			 }

			 MapTransmissionPathElement
							 myMapTransmissionPathElement
								 = new MapTransmissionPathElement( dataSource.GetUId(MapTransmissionPathElement.typ),
																									 buferStartNode,
																									 buferEndNode,
																									 ismMapContext );
			 myMapTransmissionPathElement.type_id = "Path_proto1";
			 myMapTransmissionPathElement.physicalLink_ids.add( myPhysicalLinkElement.getID() );
			 Pool.put(MapTransmissionPathElement.typ, myMapTransmissionPathElement.getId(), myMapTransmissionPathElement);//��������� � ���

			 // ��������� �������������� ���� �� �����
			 ismMapContext.addTransmissionPath (myMapTransmissionPathElement);

			 for (int j=1 ; j<optimizedData[i].vectorOfNodesIDs.length; j++)
			 {
//	    	buferEndNode = buferStartNode;
//				myMapTransmissionPathElement.startNode = buferStartNode = ismMapContext.getNode(optimizedData[i].vectorOfNodesIDs[j]);
//				myMapTransmissionPathElement.physicalLink_ids.add( ismMapContext.getPhysicalLink(buferEndNode, buferStartNode).getID());

					buferStartNode = buferEndNode;
					myMapTransmissionPathElement.physicalLink_ids.add( optimizedData[i].vectorOfNodesIDs[j++]);
					myMapTransmissionPathElement.endNode = buferEndNode = ismMapContext.getNode(optimizedData[i].vectorOfNodesIDs[j]);
				}
				//buferEndNode = theNode;
		 }
*/
		 //����� ���������, ������ ���� ��������� ����, ���� ��� ���� �����
		 dispatcher.notify(new OperationEvent (this, 0, "MapUpdatedByNewSolutionEvent"));

		}
	//-----------------------------------------------------------------------------------------------------------
	public void cleanIteration()
	{  //������� �� KIS ������� ������� �� ismMapContext
/*
		if(!ismMapContext.getMapKISNodeElements().isEmpty() )
		 {  for (int i=0; i<ismMapContext.getMapKISNodeElements().size(); i++)
				{  MapKISNodeElement kis = (MapKISNodeElement) ismMapContext.getMapKISNodeElements().get(i);
					 if ( kis.ism_map_id.equals(ismMapContext.ISM_id) )
					{  ismMapContext.removeNode(kis);
						 Pool.remove(kis);//������� �� ����
						 i--;
					}
			 }
		 }
		 //������� �� TransmissionPath ������� ������� �� ismMapContext
		 if(!ismMapContext.getTransmissionPath().isEmpty())
		 { for (int i=0; i<ismMapContext.getTransmissionPath().size(); i++)
			 { 	MapTransmissionPathElement path = (MapTransmissionPathElement) ismMapContext.getTransmissionPath().get(i);
					if ( path.ism_map_id.equals(ismMapContext.ISM_id) )
					{ ismMapContext.removeTransmissionPath(path);
						Pool.remove(path);//������� �� ����
						i--;
					}
			 }
		 }
		 //������� �� PhysicalLink ������� ������� �� ismMapContext
		 if(!ismMapContext.getPhysicalLinks().isEmpty())
		 {  for (int i=0; i<ismMapContext.getPhysicalLinks().size(); i++)
				{ MapPhysicalLinkElement physicalLink = (MapPhysicalLinkElement) ismMapContext.getPhysicalLinks().get(i);
					if ( physicalLink.ism_map_id.equals(ismMapContext.ISM_id) )
					{ ismMapContext.removePhysicalLink(physicalLink);
						Pool.remove(physicalLink);//������� �� ����
						i--;
					}
				}
		 }
		 //������� �� NodeLink ������� ������� �� ismMapContext
		 if(!ismMapContext.getNodeLinks().isEmpty())
		 { for (int i=0; i<ismMapContext.getNodeLinks().size(); i++)
			 {	MapNodeLinkElement link = (MapNodeLinkElement) ismMapContext.getNodeLinks().get(i);
					if ( link.ism_map_id.equals(ismMapContext.ISM_id) )
					{	ismMapContext.removeNodeLink(link);
						Pool.remove(link);//������� �� ����
						i--;
					}
			 }
		}
		if(ismMapContext.getLogicalNetLayer() != null)
				ismMapContext.getLogicalNetLayer().postDirtyEvent();
		if(ismMapContext.getLogicalNetLayer() != null)
				ismMapContext.getLogicalNetLayer().postPaintEvent();
*/
	}
	//-----------------------------------------------------------------------------------------------------------
}
