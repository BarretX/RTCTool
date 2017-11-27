package DataForChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import Charts.BarChart;
import Charts.Chart;
import Charts.ColumnChart;
import Charts.ComboChart;
import Charts.LineChart;
import ConstVar.ConstString;
import GetAttributeDispValue.GetAttributesValue;
import Login.LoginHandler;
import Main.Program;
import QueryReference.QueryChild;
import SearchWorkItem.MulConditionQuery;
import SearchWorkItem.SearchCondition;

public class FTViewSEDataFactory {
	private static LoginHandler handler=Program.handler;
	private static ITeamRepository repository=Program.repository;	

	
	public static ProductData Get_FTVIEWSE_PM_Data_Weekly_Trend()
	{
		if(FTVIEWSE_PM_Data_Weekly_Trend==null)
			ChartOfPM();
		
		return FTVIEWSE_PM_Data_Weekly_Trend;
	}
	
	public static ProductData Get_FTVIEWSE_PM_Data_Trend_Epic()
	{
		
		return FTVIEWSE_PM_Data_Trend_Epic;
	}
	public static ProductData Get_FTVIEWSE_PM_Data_Trend_Team()
	{		
		ChartOfPM();
		return FTVIEWSE_PM_Data_Trend_Team;
	}
	public static ProductData Get_FTVIEWSE_PM_Data_ThroughputVelocity_sprint()
	{
		if(FTVIEWSE_PM_Data_ThroughputVelocity_sprint==null)
			ChartOfPM();
		
		return FTVIEWSE_PM_Data_ThroughputVelocity_sprint;
	}
	public static ProductData Get_FTVIEWSE_PM_Data_Plan_Actual_Sprint()
	{
		if(FTVIEWSE_PM_Data_Plan_Actual_Sprint==null)
			ChartOfPM();
		
		return FTVIEWSE_PM_Data_Plan_Actual_Sprint;
	}
	public static ProductData Get_FTVIEWSE_PM_Data_Feature_Progress()
	{
		if(FTVIEWSE_PM_Data_Feature_Progress==null)
			ChartOfPM();
		
		return FTVIEWSE_PM_Data_Feature_Progress;
	}
	public static ProductData Get_FTVIEWSE_PM_Data_Resource_Sprint()
	{
		
		return FTVIEWSE_PM_Data_Resource_Sprint;
	}
	
	
	
	
	
	
	
	
	
	
	
	public static ProductData FTVIEWSE_PM_Data_Weekly_Trend=null;
	public static ProductData FTVIEWSE_PM_Data_Trend_Epic=null;
	public static ProductData FTVIEWSE_PM_Data_Trend_Team=null;
	public static ProductData FTVIEWSE_PM_Data_ThroughputVelocity_sprint=null;
	public static ProductData FTVIEWSE_PM_Data_Plan_Actual_Sprint=null;
	public static ProductData FTVIEWSE_PM_Data_Feature_Progress=null;
	public static ProductData FTVIEWSE_PM_Data_Resource_Sprint=null;
	
	public static void ChartOfPM()
	{	
		
		//[start]
	    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
	    List<String> projectAreaNames = new ArrayList<>();
	    for(int i = 0;i<iProcessAreas.size();i++)
		{
	    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
	    	projectAreaNames.add(iProcessArea.getName());
			System.out.println(iProcessArea.getName());
		}
	    //[end]
	    
	    // there suppose you take the first value
	    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(2));
		List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
		for(String str: allDispNames)
		{
			System.out.println(str);
		}
		
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
	    
//	    Calculate_BurnDown	( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//1,2,6,7,8
//	    Calculate_LogixLike( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//3
//	    Calculate_FW_Related( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//4
//	    Calculate_Others( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//5
	    Calculate_Team(repository, handler,projectAreaNames,getAttributesValue,conditionsList);//9
	}
	
	//Calculate Agile Burndown of CCW 1,2,6,7,8
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
		
		//the definition of the array:0-Epic Summary; 1-Finish Point; 2-Remain Point;
		List<List<String>> All_Epic=new ArrayList<>();
	    
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
					 
					 ////Calculate all Point of every epic[begin]
					 int Epic_Plan_Point=0;
					 int Epic_Finish_Point=0;
					 int Epic_Remain_Point=0;
					 for(List<String> tempList4:resultList)   //the story status of the sprint
					 {

							//if can't get the attribute points,loop this for()
							 if(tempList4.get(2).equals("")) 
								 tempList4.set(2, "0");
							 
							 //get the plan point and add to the record
							 Epic_Plan_Point+= Integer.parseInt(tempList4.get(2));
							 
							 //get the status ,if the status is "closed",add to finish
							 if(tempList4.get(3).equals("Closed"))
							 {									 
								 Epic_Finish_Point+=Integer.parseInt(tempList4.get(2));								 
							 }
					 }
					 Epic_Remain_Point=Epic_Plan_Point-Epic_Finish_Point;
					
					 //the fotmat of record is :1.epic name 2.epic finish point 3.epic remain point
					 //the record will be used to draw Chart : All Epic
					List<String> One_Epic=new ArrayList<>(); 
					One_Epic.add(workItem.getHTMLSummary().toString());
					One_Epic.add(Integer.toString(Epic_Finish_Point));
					One_Epic.add(Integer.toString(Epic_Remain_Point));
					All_Epic.add(One_Epic);		
					////Calculate all Epic Point [end]
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
	    		
	    		List<List<String>> BurnUp_of_Sprint=Arrays.asList(Arrays.asList("Sprint 3","1/26/2017","0","0"),
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
	    		
	    		int sum_plan=0;
	    		int sum_close=0;
	    		for(int i=0;i<Point_of_Sprint.size();i++)
	    		{
	    			List<String> tempList6=Point_of_Sprint.get(i);
	    			sum_plan+=Integer.parseInt(tempList6.get(2));
	    			sum_close+=Integer.parseInt(tempList6.get(3));
	    			
	    			BurnUp_of_Sprint.get(i).set(2, Integer.toString(sum_plan));
	    			BurnUp_of_Sprint.get(i).set(3, Integer.toString(sum_close));
	    		}
	    		   		
	    		
	    		List<String> x1=new ArrayList<>();
	    		List<Integer> y1=new ArrayList<>();
	    		List<Integer> y2=new ArrayList<>();
	    		
	    		//P1:Draw the burndown
	    		for(List<String> item:BurnUp_of_Sprint)
	    		{
	    			x1.add(item.get(0));
	    			y1.add(Integer.parseInt(item.get(2)));
	    			y2.add(Integer.parseInt(item.get(3)));
	    		}
	    		
	    		Create_P1(x1,y1,y2);
	
	    		
	    		//P2:Draw the story point by sprint
	    		x1.clear();
	    		y1.clear();
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{
	    			x1.add(item.get(0));
	    			y1.add(Integer.parseInt(item.get(2)));//plan    			
	    		}
	    		
	    		String chartID2=Create_P2(x1,y1);
	    		System.out.println("CCW Team Velocity by Sprint:\n"+ ConstString.CHART_URL + chartID2);
	    		
	    		//P6:Draw the story point by sprint
	    		x1.clear();
	    		y1.clear();
	    		y2.clear();
	    		int Sprint_Average=0;
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{  
	    			Sprint_Average+=Integer.parseInt(item.get(3));
	    		}
	    		Sprint_Average=Sprint_Average/Point_of_Sprint.size();
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{
	    			x1.add(item.get(0));
	    			y1.add(Integer.parseInt(item.get(3)));//finish    
	    			y2.add(Sprint_Average);
	    		}

	    		Create_P6(x1,y2,y1);
	    		
	    		//P7:Draw the Plan vs Actual
	    		x1.clear();
	    		y1.clear();
	    		y2.clear();
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{
	    			x1.add(item.get(0));
	    			y1.add(Integer.parseInt(item.get(2)));//plan
	    			y2.add(Integer.parseInt(item.get(3)));//finish	    				
	    		}
	    		
	    		Create_P7(x1,y1,y2);
	    		
	    		
	    		//P8:Draw the all epic
	    		x1.clear();
	    		y1.clear();
	    		y2.clear();
	    		
	    		for(List<String> item:All_Epic)
	    		{
	    			x1.add(item.get(0));
	    			y1.add(Integer.parseInt(item.get(1)));
	    			y2.add(Integer.parseInt(item.get(2)));
	    		}
	    		
	    		Create_P8(x1,y1,y2);    		
	    		
	    	}
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}
	
	//Lane Ma
	//Calculate CCW R11 Feature Progress- Logix-ladder 3
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
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
	    		
	    		//P3:Draw Logix-Like
	    		List<String> x1=new ArrayList<>();
	    		List<Integer> y1=new ArrayList<>();
	    		List<Integer> y2=new ArrayList<>();
	    		
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			x1.add(tmpList2.get(0)); //Summary
	    			y1.add(Integer.parseInt(tmpList2.get(1)));//finish
	    			y2.add(Integer.parseInt(tmpList2.get(2)));//remain
	    		}
	    		
	    		String chartID3=Create_P3(x1,y1,y2);
	    		System.out.println("Logix Like:\n"+ ConstString.CHART_URL + chartID3);
	    		
	    	}
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}
	
	//Lane Ma
		//Calculate CCW trend by team-9
		public static void Calculate_Team(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
		{
		    List<String> needAttributeList = new ArrayList<>();
		    needAttributeList.add("Id");//pass                       0
		    needAttributeList.add("Planned For");//pass              1 
		    needAttributeList.add("Story Points (numeric)");//pass   2
		    needAttributeList.add("Status");//pass                   3
		    needAttributeList.add("Summary");//pass                  4
		    needAttributeList.add("Team Area");//pass                5
		    needAttributeList.add("Resolution Date");//pass         6
		    
		    try {
			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
		    		
		    		//P3:Draw Logix-Like
		    		List<String> x1=new ArrayList<>();
		    		List<Integer> y1=new ArrayList<>();
		    		List<Integer> y2=new ArrayList<>();
		    		
		    		for(List<String> tmpList2 : LogixLike)
		    		{
		    			x1.add(tmpList2.get(0)); //Summary
		    			y1.add(Integer.parseInt(tmpList2.get(1)));//finish
		    			y2.add(Integer.parseInt(tmpList2.get(2)));//remain
		    		}
		    		
		    		Create_P9(x1,y1,y2);
		    	}
		    	
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e);
		    }
		}
		
		//Lane Ma
		//Calculate CCW trend by team-9
		public static void Calculate_EpicTrend(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList) 
		{
		    List<String> needAttributeList = new ArrayList<>();
		    needAttributeList.add("Id");//pass                       0
		    needAttributeList.add("Story Points (numeric)");//pass   1
		    needAttributeList.add("Status");//pass                   2
		    needAttributeList.add("Summary");//pass                  3
		    needAttributeList.add("Resolution Date");//pass          4
		    
		    try {
			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
		    		
		    		//P3:Draw Logix-Like
		    		List<String> x1=new ArrayList<>();
		    		List<Integer> y1=new ArrayList<>();
		    		List<Integer> y2=new ArrayList<>();
		    		
		    		for(List<String> tmpList2 : LogixLike)
		    		{
		    			x1.add(tmpList2.get(0)); //Summary
		    			y1.add(Integer.parseInt(tmpList2.get(1)));//finish
		    			y2.add(Integer.parseInt(tmpList2.get(2)));//remain
		    		}
		    		
		    		Create_P9(x1,y1,y2);
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
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
	    		
	    		//P4:FW - related
	    		List<String> x1=new ArrayList<>();
	    		List<Integer> y1=new ArrayList<>();
	    		List<Integer> y2=new ArrayList<>();
	    		
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			x1.add(tmpList2.get(0)); //Summary
	    			y1.add(Integer.parseInt(tmpList2.get(1)));//finish
	    			y2.add(Integer.parseInt(tmpList2.get(2)));//remain
	    		}
	    		
	    		String chartID4=Create_P4(x1,y1,y2);
	    		System.out.println("FW Related:\n"+ ConstString.CHART_URL + chartID4);
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
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
	    		
	    		//P4:FW - related
	    		List<String> x1=new ArrayList<>();
	    		List<Integer> y1=new ArrayList<>();
	    		List<Integer> y2=new ArrayList<>();
	    		
	    		for(List<String> tmpList2 : LogixLike)
	    		{
	    			x1.add(tmpList2.get(0)); //Summary
	    			y1.add(Integer.parseInt(tmpList2.get(1)));//finish
	    			y2.add(Integer.parseInt(tmpList2.get(2)));//remain
	    		}
	    		
	    		String chartID5=Create_P4(x1,y1,y2);
	    		System.out.println("Others:\n"+ ConstString.CHART_URL + chartID5);
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
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(2), null, conditionsList);		    
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
	
	//Lane Ma, Modify the demo to draw specific chart
	public static void Create_P1(List<String> x1,List<Integer> y1, List<Integer> y2)
	{	
		FTVIEWSE_PM_Data_Weekly_Trend=new ProductData();
		FTVIEWSE_PM_Data_Weekly_Trend.title="CCW R11 Release BurnUp(IC-Nov 7)";
		
		FTVIEWSE_PM_Data_Weekly_Trend.description="";//description		
		FTVIEWSE_PM_Data_Weekly_Trend.xTitle="Date";
		FTVIEWSE_PM_Data_Weekly_Trend.yTitle="Number";
		FTVIEWSE_PM_Data_Weekly_Trend.yAxisFormat="#";
		FTVIEWSE_PM_Data_Weekly_Trend.tableData=new DataTable();
		FTVIEWSE_PM_Data_Weekly_Trend.colorList=Arrays.asList("red","blue");
		
		FTVIEWSE_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		FTVIEWSE_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Original Commitment"));
		FTVIEWSE_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Story Points Finish"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTVIEWSE_PM_Data_Weekly_Trend.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static String Create_P2(List<String> x1,List<Integer> y1)
	{						
		Chart S5KA_One_Chart=new ColumnChart("CCW Team Velocity by Sprint");//Title
		
		S5KA_One_Chart.description="";//description		
		S5KA_One_Chart.xTitle="Sprint";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("blue");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Story Points"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			S5KA_One_Chart.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return null;
		}
		
		return S5KA_One_Chart.ToEagleEye();
	}
	
	public static void Create_P6(List<String> x1,List<Integer> y1, List<Integer> y2)
	{	
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint=new ProductData();
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.title="Throughput - Velocity by Sprint";
		
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.description="";//description		
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.xTitle="Sprint";
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.yTitle="Number";
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.yAxisFormat="#";
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.tableData=new DataTable();
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.colorList=Arrays.asList("red","blue");
		
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "AVERAGE"));
		FTVIEWSE_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
				
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTVIEWSE_PM_Data_ThroughputVelocity_sprint.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void Create_P7(List<String> x1,List<Integer> y1, List<Integer> y2)
	{	
		FTVIEWSE_PM_Data_Plan_Actual_Sprint=new ProductData();
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.title="Plan vs Actual";
		
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.description="";//description		
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.xTitle="Sprint";
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.yTitle="Number";
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.yAxisFormat="#";
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.tableData=new DataTable();
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.colorList=Arrays.asList("blue","yellow");

		
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Planned"));
		FTVIEWSE_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Actual"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTVIEWSE_PM_Data_Plan_Actual_Sprint.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void Create_P8(List<String> x1,List<Integer> y1, List<Integer> y2)
	{	
		FTVIEWSE_PM_Data_Feature_Progress=new ProductData();
		FTVIEWSE_PM_Data_Feature_Progress.title="all epic";
		FTVIEWSE_PM_Data_Feature_Progress.description="";//description		
		FTVIEWSE_PM_Data_Feature_Progress.xTitle="Epic";
		FTVIEWSE_PM_Data_Feature_Progress.yTitle="Number";
		FTVIEWSE_PM_Data_Feature_Progress.yAxisFormat="#";
		FTVIEWSE_PM_Data_Feature_Progress.tableData=new DataTable();
		FTVIEWSE_PM_Data_Feature_Progress.colorList=Arrays.asList("green","blue");
		
		FTVIEWSE_PM_Data_Feature_Progress.isStacked="true";
		FTVIEWSE_PM_Data_Feature_Progress.chartLeft=400;
		
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTVIEWSE_PM_Data_Feature_Progress.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void Create_P9(List<String> x1,List<Integer> y1, List<Integer> y2)
	{	
		FTVIEWSE_PM_Data_Feature_Progress=new ProductData();
		FTVIEWSE_PM_Data_Feature_Progress.title="Trend by Team";
		FTVIEWSE_PM_Data_Feature_Progress.description="";//description		
		FTVIEWSE_PM_Data_Feature_Progress.xTitle="Date";
		FTVIEWSE_PM_Data_Feature_Progress.yTitle="Number";
		FTVIEWSE_PM_Data_Feature_Progress.yAxisFormat="#";
		FTVIEWSE_PM_Data_Feature_Progress.tableData=new DataTable();
		FTVIEWSE_PM_Data_Feature_Progress.colorList=Arrays.asList("gray","blue");
		
		FTVIEWSE_PM_Data_Feature_Progress.isStacked="true";
		FTVIEWSE_PM_Data_Feature_Progress.chartLeft=400;
		
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		FTVIEWSE_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			//TODO:(Jma7)
			//	FTVIEWSE_PM_Data_Trend_Team.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static String Create_P3(List<String> x1,List<Integer> y1, List<Integer> y2)
	{						
		Chart S5KA_One_Chart=new BarChart("Logix-Ladder");//Title
		
		S5KA_One_Chart.description="";//description		
		S5KA_One_Chart.xTitle="Epic";
		S5KA_One_Chart.yTitle="Story Points";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("blue","yellow");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remaining"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			S5KA_One_Chart.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return null;
		}
		
		return S5KA_One_Chart.ToEagleEye();
	}
	
	public static String Create_P4(List<String> x1,List<Integer> y1, List<Integer> y2)
	{						
		Chart S5KA_One_Chart=new BarChart("FW Related");//Title
		
		S5KA_One_Chart.description="";//description		
		S5KA_One_Chart.xTitle="Epic";
		S5KA_One_Chart.yTitle="Story Points";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("blue","yellow");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remaining"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			S5KA_One_Chart.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return null;
		}
		
		return S5KA_One_Chart.ToEagleEye();
	}
	
	public static String Create_P5(List<String> x1,List<Integer> y1, List<Integer> y2)
	{						
		Chart S5KA_One_Chart=new BarChart("Other");//Title
		
		S5KA_One_Chart.description="";//description		
		S5KA_One_Chart.xTitle="Epic";
		S5KA_One_Chart.yTitle="Story Points";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("blue","yellow");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remaining"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			S5KA_One_Chart.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return null;
		}
		
		return S5KA_One_Chart.ToEagleEye();
	}

}
