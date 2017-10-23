package Main;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import GetAttributeDispValue.GetAttributesValue;
import Login.LoginHandler;
import QueryReference.QueryChild;
import SearchWorkItem.MulConditionQuery;
import SearchWorkItem.SearchCondition;

import com.ibm.team.build.internal.common.model.BuildResultHandle;
import com.ibm.team.links.common.IReference;
import com.ibm.team.links.common.registry.IEndPointDescriptor;
import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.IAuditable;
import com.ibm.team.repository.common.Location;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.ItemProfile;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;


public class Main {
	
	public static void main(String[] args) 
	{
		String repositoryURI ="https://ccmclm.rockwellautomation.com/ccm";
	    
	    String userId = "JMa7";
	    String password = "***";
	    LoginHandler handler=new LoginHandler(userId,password,repositoryURI);
	    
	    ITeamRepository repository=handler.login();
	    
	    //[start]
	    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
	    List<String> projectAreaNames = new ArrayList<>();
	    for(int i = 0;i<iProcessAreas.size();i++)
		{
	    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
	    	projectAreaNames.add(iProcessArea.getName());
			System.out.println(i+ " "+iProcessArea.getName());
		}
	    //[end]
	    
	    // there suppose you take the first value
	    //Dev help, to list all the attribute name
		
		 GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(1));
		/*List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
		for(String str: allDispNames)
		{
			System.out.println(str);
		}*/

	    
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
//	    conditionsList.add(new SearchCondition(IWorkItem.ID_PROPERTY,"12177",AttributeOperation.EQUALS));
	    	
	    Calculate_Others( repository, handler,projectAreaNames,getAttributesValue,conditionsList);
	    }
	
	
	//Lane Ma
	//Calculate Agile Burndown of CCW
	public static void Calculate_BurnDown(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
	{
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    
	    //the definition of the array:0-Sprint 1-date 2-planed 3-remained
		List<List<String>> Point_of_Sprint=Arrays.asList(Arrays.asList("Sprint 3","1/26/2017","0","0"),
													   Arrays.asList("Sprint 4","2/16/2017","0","0"),
													   Arrays.asList("Sprint 5","3/9/2017","0","0"),
													   Arrays.asList("Sprint 6","3/23/2017","0","0"),
													   Arrays.asList("Sprint 7","4/13/2017","0","0"),
													   Arrays.asList("Sprint 8","5/4/2017","0","0"),
													   Arrays.asList("Sprint 9","5/25/2017","0","0"),
													   Arrays.asList("Sprint 10","6/8/2017","0","0"),
													   Arrays.asList("Sprint 11","6/29/2017","0","0"),
													   Arrays.asList("Sprint 12","7/20/2017","0","0"),
													   Arrays.asList("Sprint 13","8/10/2017","0","0"),
													   Arrays.asList("Sprint 14","8/24/2017","0","0"),
													   Arrays.asList("Sprint 15","9/14/2017","0","0"),
													   Arrays.asList("Sprint 16","10/5/2017","0","0"),
													   Arrays.asList("Sprint 17","10/19/2017","0","0"));
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(1000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
				while(resultOwner.hasNext(handler.getMonitor()))
				{
					 resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem();
					
					 //Print the Father's ID
					 System.out.println("Epic: " + workItem.getId()+"   "+workItem.getWorkItemType());
					 
					 //Find the father's comment
					 List<IWorkItem> FatherList = new ArrayList<>();
					 FatherList.add(workItem);
					 List<List<String>> resultList_father=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),FatherList,needAttributeList);
					 
					 //Print all the father's comment
					 int k=0;
		    		for(List<String> tmpList2 : resultList_father)
		    		{
		    			int i = 0;
		    			k++;
		    			System.out.print(k+"\t");
		    			for(String str : tmpList2)
		    			{
		    				System.out.print(i++ + "\t"+str+"\t");
		    			}
		    			System.out.println();
		    		}
					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
					 ChildList = queryChild.analyzeReferences(repository,references);
					 
					 for(IWorkItem tempWorkitem:ChildList)
					 {
						 System.out.println("child: "+tempWorkitem.getId()+"   Type: "+tempWorkitem.getWorkItemType());
					 }
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);
					 
					//Print all the child comment
			    /*		int j=0;
			    		for(List<String> tmpList2 : resultList)
			    		{
			    			int i = 0;
			    			j++;
			    			System.out.print(j+"\t");
			    			for(String str : tmpList2)
			    			{
			    				System.out.print(i++ + "\t"+str+"\t");
			    			}
			    			System.out.println();
			    		}
					 */
					 for (List<String> tempList3:Point_of_Sprint )//the record of the sprint point
					 {
						 for(List<String> tempList4:resultList)   //the story status of the sprint
						 {
							 if(tempList4.get(1).contains(tempList3.get(0)))
							 {
								//if can't get the attribute points,loop this for()
								 if(tempList4.get(2).equals("")) 
									 tempList4.set(2, "0");
								 
								 //get the plan point and add to the record
								 int temp_plan_point= Integer.parseInt(tempList3.get(2))+Integer.parseInt(tempList4.get(2));
								 tempList3.set(2,Integer.toString(temp_plan_point));
								 
								 //get the status ,if the status is "closed",add to finish
								 if(tempList4.get(3).equals("Closed"))
								 {									 
									 int temp_finish_point=Integer.parseInt(tempList3.get(3))+Integer.parseInt(tempList4.get(2));
									 tempList3.set(3, Integer.toString(temp_finish_point));
								 }
							 }
						 }
					 }
					 
					 //Print the sprint plan and finish
			    		int kk=0;
			    		for(List<String> tmpList2 : Point_of_Sprint)
			    		{
			    			int i = 0;
			    			kk++;
			    			System.out.print(kk+"\t");
			    			for(String str : tmpList2)
			    			{
			    				System.out.print(i++ + "\t"+str+"\t");
			    			}
			    			System.out.println();
			    		}
			    		
			    		int total_plan=0;
			    		//Calculate the total_plan and burndown
			    		for (List<String> tempList3:Point_of_Sprint )//the record of the sprint point
			    		{
			    			total_plan+=Integer.parseInt(tempList3.get(2));
			    		}
			    		int Point_Plan_Remain=total_plan;
			    		int Point_finish_Remain=total_plan;
			    		
			    		
			    		List<List<String>> Remain_of_Sprint=Arrays.asList(Arrays.asList("Sprint 3","1/26/2017","0","0"),
																		   Arrays.asList("Sprint 4","2/16/2017","0","0"),
																		   Arrays.asList("Sprint 5","3/9/2017","0","0"),
																		   Arrays.asList("Sprint 6","3/23/2017","0","0"),
																		   Arrays.asList("Sprint 7","4/13/2017","0","0"),
																		   Arrays.asList("Sprint 8","5/4/2017","0","0"),
																		   Arrays.asList("Sprint 9","5/25/2017","0","0"),
																		   Arrays.asList("Sprint 10","6/8/2017","0","0"),
																		   Arrays.asList("Sprint 11","6/29/2017","0","0"),
																		   Arrays.asList("Sprint 12","7/20/2017","0","0"),
																		   Arrays.asList("Sprint 13","8/10/2017","0","0"),
																		   Arrays.asList("Sprint 14","8/24/2017","0","0"),
																		   Arrays.asList("Sprint 15","9/14/2017","0","0"),
																		   Arrays.asList("Sprint 16","10/5/2017","0","0"),
																		   Arrays.asList("Sprint 17","10/19/2017","0","0"));
			    		for(int i=0;i<Point_of_Sprint.size();i++)
			    		{
			    			List<String> tempList5=Point_of_Sprint.get(i);
			    			Point_Plan_Remain=Point_Plan_Remain-Integer.parseInt(tempList5.get(2));
			    			Point_finish_Remain=Point_finish_Remain-Integer.parseInt(tempList5.get(3));
			    			
			    			Remain_of_Sprint.get(i).set(2, Integer.toString(Point_Plan_Remain));
			    			Remain_of_Sprint.get(i).set(3, Integer.toString(Point_finish_Remain));
			    		}  	
			    		
			    		//Print the sprint plan and finish
			    		int ii=0;
			    		for(List<String> tmpList2 : Remain_of_Sprint)
			    		{
			    			int i = 0;
			    			ii++;
			    			System.out.print(ii+"\t");
			    			for(String str : tmpList2)
			    			{
			    				System.out.print(i++ + "\t"+str+"\t");
			    			}
			    			System.out.println();
			    		}
				}
	    	}
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}
	
	//Lane Ma
	//Calculate CCW R11 Feature Progress- Logix-ladder
	public static void Calculate_LogixLike(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
	{
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    needAttributeList.add("Summary");//pass                  4
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(1000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
	    		
	    		List<List<String>> LogixLike=new ArrayList<>();
				while(resultOwner.hasNext(handler.getMonitor()))
				{
					 resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem();
					
					 if(!workItem.getHTMLSummary().toString().contains("LogixLike"))
					 {
						 continue;
					 }
					 					 
					 //Print the Father's ID
					// System.out.println("Epic:\t" + workItem.getId()+"\t"+workItem.getWorkItemType()+"\t"+workItem.getHTMLSummary());
					 
					//Find the father's comment
					 List<IWorkItem> FatherList = new ArrayList<>();
					 FatherList.add(workItem);
					 List<List<String>> resultList_father=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),FatherList,needAttributeList);
					 
					 if(!resultList_father.get(0).get(1).equals("R11"))
					 {
						 continue;//if not "R11"
					 }
					
					 //Print the Father's ID
				     System.out.println("Epic:\t" + workItem.getId()+"\t"+workItem.getWorkItemType()+"\t"+workItem.getHTMLSummary());
					 
					 //Print all the father's comment
				/*	 int k=0;
		    		for(List<String> tmpList2 : resultList_father)
		    		{
		    			int i = 0;
		    			k++;
		    			System.out.print(k+"\t");
		    			for(String str : tmpList2)
		    			{
		    				System.out.print(i++ + "\t"+str+"\t");
		    			}
		    			System.out.println();
		    		}*/
					 					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
				
					  ChildList = queryChild.analyzeReferences(repository,references);
					 
					 for(IWorkItem tempWorkitem:ChildList)
					 {
						 System.out.println("child: "+tempWorkitem.getId()+"   Type: "+tempWorkitem.getWorkItemType());
					 }
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);	 
					 int Epic_Close_Point=0;
					 int Epic_Open_Point=0;
					 for(List<String> tmpList2 : resultList)
					 {
						 if(tmpList2.get(3).equals("Closed"))
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Close_Point+=Integer.parseInt(tmpList2.get(2));
						 }
						 else
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Open_Point+=Integer.parseInt(tmpList2.get(2));
						 }						 
					 }
					 
					 List<String> Item_of_LogixLike=new ArrayList<String>();
					 Item_of_LogixLike.add( workItem.getHTMLSummary().toString());
					 Item_of_LogixLike.add(Integer.toString(Epic_Close_Point));
					 Item_of_LogixLike.add(Integer.toString(Epic_Open_Point));
					 
					 LogixLike.add(Item_of_LogixLike);
					 
					 //Print all the child comment
			    /*		int j=0;
			    		for(List<String> tmpList2 : resultList)
			    		{
			    			int i = 0;
			    			j++;
			    			System.out.print(j+"\t");
			    			for(String str : tmpList2)
			    			{
			    				System.out.print(i++ + "\t"+str+"\t");
			    			}
			    			System.out.println();
			    		}
					 */
				}
				int j=0;
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			int i = 0;
	    			j++;
	    			System.out.print(j+"\t");
	    			for(String str : tmpList2)
	    			{
	    				System.out.print(i++ + "\t"+str+"\t");
	    			}
	    			System.out.println();
	    		}				
	    	}
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}
	
	//Lane Ma
	//Calculate CCW R11 Feature Progress- FW related
	public static void Calculate_FW_Related(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
	{
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    needAttributeList.add("Summary");//pass                  4
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(1000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
	    		
	    		List<List<String>> LogixLike=new ArrayList<>();
				while(resultOwner.hasNext(handler.getMonitor()))
				{
					 resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem();
					
					 if(!workItem.getHTMLSummary().toString().contains("FW related"))
					 {
						 continue;
					 }
					 					 
					 
					//Find the father's comment
					 List<IWorkItem> FatherList = new ArrayList<>();
					 FatherList.add(workItem);
					 List<List<String>> resultList_father=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),FatherList,needAttributeList);
					 
					 if(!resultList_father.get(0).get(1).equals("R11"))
					 {
						 continue;//if not "R11"
					 }
					
					 //Print the Father's ID
				     System.out.println("Epic:\t" + workItem.getId()+"\t"+workItem.getWorkItemType()+"\t"+workItem.getHTMLSummary());
					 					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
				
					  ChildList = queryChild.analyzeReferences(repository,references);
					 
					 for(IWorkItem tempWorkitem:ChildList)
					 {
						 System.out.println("child: "+tempWorkitem.getId()+"   Type: "+tempWorkitem.getWorkItemType());
					 }
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);	 
					 int Epic_Close_Point=0;
					 int Epic_Open_Point=0;
					 for(List<String> tmpList2 : resultList)
					 {
						 if(tmpList2.get(3).equals("Closed"))
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Close_Point+=Integer.parseInt(tmpList2.get(2));
						 }
						 else
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Open_Point+=Integer.parseInt(tmpList2.get(2));
						 }						 
					 }
					 
					 List<String> Item_of_LogixLike=new ArrayList<String>();
					 Item_of_LogixLike.add( workItem.getHTMLSummary().toString());
					 Item_of_LogixLike.add(Integer.toString(Epic_Close_Point));
					 Item_of_LogixLike.add(Integer.toString(Epic_Open_Point));
					 
					 LogixLike.add(Item_of_LogixLike);					 
				}
				int j=0;
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			int i = 0;
	    			j++;
	    			System.out.print(j+"\t");
	    			for(String str : tmpList2)
	    			{
	    				System.out.print(i++ + "\t"+str+"\t");
	    			}
	    			System.out.println();
	    		}				
	    	}
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}

	//Lane Ma
	//Calculate CCW R11 Feature Progress- Others
	public static void Calculate_Others(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
	{
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    needAttributeList.add("Summary");//pass                  4
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(1000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
	    		
	    		List<List<String>> LogixLike=new ArrayList<>();
				while(resultOwner.hasNext(handler.getMonitor()))
				{
					 resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem();
					
					 if((!workItem.getHTMLSummary().toString().contains("MLX convertor"))&
					     (!workItem.getHTMLSummary().toString().contains("Installation"))&
					     (!workItem.getHTMLSummary().toString().contains("RSLinx Enterprise adoption"))&
					     (!workItem.getHTMLSummary().toString().contains("UDC trending support"))&
					     (!workItem.getHTMLSummary().toString().contains("DFS and common policy")))
					 {
						 continue;
					 }
					 					 
					 
					//Find the father's comment
					 List<IWorkItem> FatherList = new ArrayList<>();
					 FatherList.add(workItem);
					 List<List<String>> resultList_father=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),FatherList,needAttributeList);
					 
					 if(!resultList_father.get(0).get(1).equals("R11"))
					 {
						 continue;//if not "R11"
					 }
					
					 //Print the Father's ID
				     System.out.println("Epic:\t" + workItem.getId()+"\t"+workItem.getWorkItemType()+"\t"+workItem.getHTMLSummary());
					 					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
				
					  ChildList = queryChild.analyzeReferences(repository,references);
					 
					 for(IWorkItem tempWorkitem:ChildList)
					 {
						 System.out.println("child: "+tempWorkitem.getId()+"   Type: "+tempWorkitem.getWorkItemType());
					 }
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);	 
					 int Epic_Close_Point=0;
					 int Epic_Open_Point=0;
					 for(List<String> tmpList2 : resultList)
					 {
						 if(tmpList2.get(3).equals("Closed"))
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Close_Point+=Integer.parseInt(tmpList2.get(2));
						 }
						 else
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Open_Point+=Integer.parseInt(tmpList2.get(2));
						 }						 
					 }
					 
					 List<String> Item_of_LogixLike=new ArrayList<String>();
					 Item_of_LogixLike.add( workItem.getHTMLSummary().toString());
					 Item_of_LogixLike.add(Integer.toString(Epic_Close_Point));
					 Item_of_LogixLike.add(Integer.toString(Epic_Open_Point));
					 
					 LogixLike.add(Item_of_LogixLike);					 
				}
				int j=0;
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			int i = 0;
	    			j++;
	    			System.out.print(j+"\t");
	    			for(String str : tmpList2)
	    			{
	    				System.out.print(i++ + "\t"+str+"\t");
		    			}
		    			System.out.println();
		    		}				
		    	}
		    	
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e);
		    }
		}
	
	//Lane Ma
	//Calculate CCW R11 Feature Progress- All
	public static void Calculate_All(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
	{
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    needAttributeList.add("Summary");//pass                  4
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(1000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
	    		
	    		List<List<String>> LogixLike=new ArrayList<>();
				while(resultOwner.hasNext(handler.getMonitor()))
				{
					 resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem();
						 					 
					 
					//Find the father's comment
					 List<IWorkItem> FatherList = new ArrayList<>();
					 FatherList.add(workItem);
					 List<List<String>> resultList_father=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),FatherList,needAttributeList);
					 
					 if(!resultList_father.get(0).get(1).equals("R11"))
					 {
						 continue;//if not "R11"
					 }
					
					 //Print the Father's ID
				     System.out.println("Epic:\t" + workItem.getId()+"\t"+workItem.getWorkItemType()+"\t"+workItem.getHTMLSummary());
					 					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
				
					  ChildList = queryChild.analyzeReferences(repository,references);
					 
					 for(IWorkItem tempWorkitem:ChildList)
					 {
						 System.out.println("child: "+tempWorkitem.getId()+"   Type: "+tempWorkitem.getWorkItemType());
					 }
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);	 
					 int Epic_Close_Point=0;
					 int Epic_Open_Point=0;
					 for(List<String> tmpList2 : resultList)
					 {
						 if(tmpList2.get(3).equals("Closed"))
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Close_Point+=Integer.parseInt(tmpList2.get(2));
						 }
						 else
						 {
							 if(tmpList2.get(2).equals(""))
							 {
								 tmpList2.set(2, "0");
							 }
							 Epic_Open_Point+=Integer.parseInt(tmpList2.get(2));
						 }						 
					 }
					 
					 List<String> Item_of_LogixLike=new ArrayList<String>();
					 Item_of_LogixLike.add( workItem.getHTMLSummary().toString());
					 Item_of_LogixLike.add(Integer.toString(Epic_Close_Point));
					 Item_of_LogixLike.add(Integer.toString(Epic_Open_Point));
					 
					 LogixLike.add(Item_of_LogixLike);					 
				}
				int j=0;
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			int i = 0;
	    			j++;
	    			System.out.print(j+"\t");
	    			for(String str : tmpList2)
	    			{
	    				System.out.print(i++ + "\t"+str+"\t");
		    			}
		    			System.out.println();
		    		}				
		    	}
		    	
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e);
		    }
		}
}

