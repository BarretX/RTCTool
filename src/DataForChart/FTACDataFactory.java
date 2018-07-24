package DataForChart;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Region;
import org.w3c.dom.ls.LSInput;

import com.google.common.collect.Lists;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.team.process.common.IProcessArea;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import ConstVar.ConstString;
import GetAttributeDispValue.GetAttributesValue;
import Helper.TrendLine;
import Helper.ColorFormater;
import Helper.EpicItem;
import Login.LoginHandler;
import Main.Program;
import QueryReference.QueryChild;
import SearchWorkItem.MulConditionQuery;
import SearchWorkItem.SearchCondition;

public class FTACDataFactory {
	private static LoginHandler handler=Program.handler;
	private static ITeamRepository repository=Program.repository;	
    public static Map<String, EpicItem> mapFeature=new HashMap<String,EpicItem>();
    public static Map<String, EpicItem> mapEpic=new HashMap<String,EpicItem>();
	
	public static ProductData Get_FTAC_PM_Data_Weekly_Trend()
	{
		if(FTAC_PM_Data_Weekly_Trend==null)
		{
			ChartOfPM();
		}
		
		return FTAC_PM_Data_Weekly_Trend;
	}
	
	public static ProductData Get_FTAC_PM_Data_BurnDown()
	{
		if(FTAC_PM_Data_Weekly_BurnDown==null)
		{
			ChartOfPM();
		}
		
		return FTAC_PM_Data_Weekly_BurnDown;
	}
	
	public static void ChartOfPM_About_Epic()
	{
		//[start]
		int nProjectNumber=0;
	    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
	    List<String> projectAreaNames = new ArrayList<>();
	    for(int i = 0;i<iProcessAreas.size();i++)
		{
	    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
	    	projectAreaNames.add(iProcessArea.getName());
	    	
			if(iProcessArea.getName().equals("CVB FTAC - RTC SAFe"))
			{
				nProjectNumber=i;
			}
		}
	    //[end]
	    
	    // there suppose you take the first value
	    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
		
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
	    
	    List<SearchCondition> conditionsList1 = new ArrayList<>(); 
	    
	    conditionsList1.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.feature", AttributeOperation.EQUALS));
	    
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");                    //pass        0
	    needAttributeList.add("Planned For");           //pass        1 
	    needAttributeList.add("Story Points (numeric)");//pass        2
	    needAttributeList.add("Status");                //pass        3
	    needAttributeList.add("Creation Date");         //pass        4
	    needAttributeList.add("Risk Estimate");         //pass        5
	    needAttributeList.add("Type");                  //pass        6
		
		 //the definition of the array:0-Epic Summary; 1-Finish Point; 2-Remain Point;
		List<EpicItem> All_Epic=new ArrayList<>();
		 
	    try {
		    MulConditionQuery query=new MulConditionQuery();
	    	
		    IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);	
		    IQueryResult<IResolvedResult<IWorkItem>> resultOwner1 = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList1);		    
		    
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(10000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
				 
				while(resultOwner.hasNext(handler.getMonitor()))
				{	
					resolved = resultOwner.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem(); 
					 
					 //test
					// System.out.println("Parent: "+workItem.getId()+"\t"+workItem.getHTMLSummary());
					
					 EpicItem One_Epic=new EpicItem(); 
					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
					 ChildList = queryChild.analyzeReferences(repository,references);					 
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);
					 				 					 
					//// [section 2] Calculate all Point of every epic[begin]
					 int Epic_Plan_Point=0;
					 int Epic_Finish_Point=0;
					 int Epic_Remain_Point=0;
					 int Epic_Risk_Point=0;
					 for(List<String> tempList4:resultList)   //the story status of the sprint
					 {
						 //test
						// System.out.println("Child: \t"+tempList4.get(0)+"\t"+tempList4.get(6));
						 
						 if(tempList4.get(6).contains("feature")||tempList4.get(6).contains("programEpic"))
						 {
							One_Epic.ChildID.add(tempList4.get(0));  //if this is feature or epic, only record
							continue;
						 }
						 
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
						 
						 if(!tempList4.get(5).equals(""))
						 {
							 Epic_Risk_Point+=Integer.parseInt(tempList4.get(5));
						 }
					 }
					Epic_Remain_Point=Epic_Plan_Point-Epic_Finish_Point;
					
					 //the record will be used to draw Chart : All Epic
					
					One_Epic.TotalPoint=Epic_Finish_Point+Epic_Remain_Point;
					One_Epic.EpicName=workItem.getHTMLSummary().toString();
					One_Epic.FinishPoint=Epic_Finish_Point;
					One_Epic.RemainPoint=Epic_Remain_Point;
					One_Epic.RiskEstimate=Epic_Risk_Point;
					One_Epic.EpicID=Integer.toString(workItem.getId());
					
					mapEpic.put(One_Epic.EpicID, One_Epic);
					
					////Calculate all Epic Point [end]
				}	   		
	    	} 	
	    		    	
	    	if(resultOwner1!=null)
	    	{
	    		resultOwner1.setLimit(10000);
	    			
	    		IWorkItem workItem = null;
	    		IResolvedResult<IWorkItem> resolved =null;
				 
				while(resultOwner1.hasNext(handler.getMonitor()))
				{					 
					resolved = resultOwner1.next(handler.getMonitor());
					 workItem = (IWorkItem)resolved.getItem(); 
					
					 EpicItem One_Epic=new EpicItem(); 
					 
					 QueryChild queryChild = new QueryChild();
					 IWorkItemCommon common= (IWorkItemCommon) ((ITeamRepository)workItem.getOrigin()).getClientLibrary(IWorkItemCommon.class);
					 IWorkItemReferences references = common.resolveWorkItemReferences(workItem, null);
					 List<IWorkItem> ChildList = new ArrayList<>();
					 ChildList = queryChild.analyzeReferences(repository,references);					 
					 
					 List<List<String>> resultList=getAttributesValue.GetPointNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),ChildList,needAttributeList);
					 				 					 
					//// [section 2] Calculate all Point of every epic[begin]
					 int Epic_Plan_Point=0;
					 int Epic_Finish_Point=0;
					 int Epic_Remain_Point=0;
					 int Epic_Risk_Point=0;
					 for(List<String> tempList4:resultList)   //the story status of the sprint
					 {
						if(tempList4.get(6).contains("feature"))
						{
							One_Epic.ChildID.add(tempList4.get(0));  //if this is feature or epic, only record
							continue;
						}
						 
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
						 
						 if(!tempList4.get(5).equals(""))
						 {
							 Epic_Risk_Point+=Integer.parseInt(tempList4.get(5));
						 }
					 }
					Epic_Remain_Point=Epic_Plan_Point-Epic_Finish_Point;
					
					 //the record will be used to draw Chart : All Epic
					
					One_Epic.TotalPoint=Epic_Finish_Point+Epic_Remain_Point;
					One_Epic.EpicName=workItem.getHTMLSummary().toString();
					One_Epic.FinishPoint=Epic_Finish_Point;
					One_Epic.RemainPoint=Epic_Remain_Point;
					One_Epic.RiskEstimate=Epic_Risk_Point;
					One_Epic.EpicID=Integer.toString(workItem.getId());
					
					mapFeature.put(One_Epic.EpicID, One_Epic);
				}	   		
	    	}
			
	    	for(EpicItem entry : mapEpic.values())
	    	{
	    		EpicItem temp=entry;
	    		CalculateEpicStoryPoint(temp,0);
	    		mapEpic.put(temp.EpicID, temp);
	    	}
	    	
	    	List<EpicItem> needEpic=new ArrayList<>();
	    	
	    	for(EpicItem entry: mapEpic.values())
	    	{	    		
	    		if(entry.m_nlevel==0)
	    		{
	    			needEpic.add(entry);
	    		}
	    	}
	    	
			//// [section 2] Calculate the top 10 epic
			Collections.sort(needEpic);	 
		
			List<String> x1=new ArrayList<>();
			List<Integer> y1=new ArrayList<>();
			List<Integer> y2=new ArrayList<>();
			List<Integer> y3=new ArrayList<>();
			int lengthMax=0;
			
			//// [section 3.1] Draw All Epic
			//only draw top ten epic
			for(int i=0;i<needEpic.size()&&i<10;i++)
			{
				EpicItem item=needEpic.get(i);
				
				int legth=item.EpicName.length();				
				if(legth>=lengthMax) lengthMax=legth;
				
				x1.add(item.EpicName);
				y1.add(item.FinishPoint);
				y2.add(item.RemainPoint-item.RiskEstimate);
				if(item.RiskEstimate == 0)
				{
					y3.add(-1);
				}
				else
				{
					y3.add(item.RiskEstimate);
				}
			}
			
			Create_P8(x1,y1,y2,y3,lengthMax);
	    	
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	}

	public static ProductData Get_FTAC_PM_Data_Plan_Actual_Sprint()
	{
		if(FTAC_PM_Data_Plan_Actual_Sprint==null)
		{
			ChartOfPM();
		}
		
		return FTAC_PM_Data_Plan_Actual_Sprint;
	}
	
	public static ProductData Get_FTAC_PM_Data_All_Epic()
	{
		if(FTAC_PM_Data_Feature_Progress==null)
		{
			ChartOfPM_About_Epic();
		}
		
		return FTAC_PM_Data_Feature_Progress;
	}
		
	
	public static ProductData FTAC_PM_Data_Weekly_Trend=null;
	public static ProductData FTAC_PM_Data_Weekly_BurnDown=null;
	public static ProductData FTAC_PM_Data_Trend_Epic=null;
	public static ProductData FTAC_PM_Data_Trend_Team=null;
	public static ProductData FTAC_PM_Data_ThroughputVelocity_sprint=null;
	public static ProductData FTAC_PM_Data_Plan_Actual_Sprint=null;
	public static ProductData FTAC_PM_Data_Feature_Progress=null;
	
	public static List<String> TeamGroup=Arrays.asList("FTAC #1");
	
	public static void ChartOfPM()
	{	
		//[start]
		int nProjectNumber=0;
	    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
	    List<String> projectAreaNames = new ArrayList<>();
	    for(int i = 0;i<iProcessAreas.size();i++)
		{
	    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
	    	projectAreaNames.add(iProcessArea.getName());
	    	
			if(iProcessArea.getName().equals("CVB FTAC - RTC SAFe"))
			{
				nProjectNumber=i;
			}
	    	
			System.out.println(iProcessArea.getName());
		}
	    //[end]
	    
	    // there suppose you take the first value
	    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
		
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.apt.workItemType.story", AttributeOperation.EQUALS));
	    
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Id");//pass                       0
	    needAttributeList.add("Planned For");//pass              1 
	    needAttributeList.add("Story Points (numeric)");//pass   2
	    needAttributeList.add("Status");//pass                   3
	    needAttributeList.add("Creation Date");//pass            4
	    needAttributeList.add("Resolution Date");//pass          5
	    
	    int Sum_Plan_For_This_Sprint=0;	    
	    
	    try 
	    {
		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner1 = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(10000);
	    		resultOwner1.setLimit(10000);
	    		
	    		List<List<String>> resultList = getAttributesValue.GetAllNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),resultOwner,needAttributeList);
	    		List<String> TeamResultList=getAttributesValue.GetTeamAreaList(repository, handler.getMonitor(),query.getProjectArea(), resultOwner1);	    		
	    		
	    	    //the definition of the array:0-Sprint 1-start date 2-end date 3-planed 4-remained
	    		int Sprint_Name_Index=0;
	    		int Sprint_Start_Index=1;
	    		int Sprint_End_Index=2;
	    		int Sprint_Plan_Index=3;
	    		int Sprint_Finish_Index=4;
	    		List<List<String>> Point_of_Sprint=Arrays.asList(
	    													   Arrays.asList("Sprint17","12/4/2017","12/23/2017","0","0"),
	    													   Arrays.asList("Sprint18","12/25/2017","1/13/2018","0","0"),
	    													   Arrays.asList("Sprint19","1/15/2018","2/3/2018","0","0"),
	    													   Arrays.asList("Sprint20","2/5/2018","2/24/2018","0","0"),
	    													   Arrays.asList("Sprint21","2/26/2018","3/17/2018","0","0"),
	    													   Arrays.asList("Sprint22","3/19/2018","4/7/2018","0","0"),
	    													   Arrays.asList("Sprint23","4/9/2018","4/28/2018","0","0"),
	    													   Arrays.asList("Sprint24","4/30/2018","5/19/2018","0","0"),
	    													   Arrays.asList("Sprint25","5/21/2018","6/9/2018","0","0"),
	    													   Arrays.asList("Sprint26","6/11/2018","6/30/2018","0","0"));
	    													 
	    				
	    	    ////Calculate the week section
	    		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
	    		SimpleDateFormat sdf1=new SimpleDateFormat("MM/dd/yy");
	    		SimpleDateFormat sdf2=new SimpleDateFormat("MM/dd");
	    		SimpleDateFormat sdfget=new SimpleDateFormat("yyyy-MM-dd");
	    		List<Date> Week_Trend=new ArrayList<Date>();
	    		List<Date> Week_Trend_Of_BurnDown=new ArrayList<Date>();
	    		
	    		Date Date_Max=new Date();
	    		Date Date_Min=new Date();
	    		
	    		try
	    		{
	    			 Date_Min = sdf.parse(Point_of_Sprint.get(0).get(Sprint_Start_Index));	
	    			 Date_Max = sdf.parse("1/18/2019");	
	    		}
	    		catch (ParseException e1) 
	    		{
	    			e1.printStackTrace();
	    		}
	    		
	    		Date Date_Max_BurnDown=new Date();
	    		Date Date_Min_BurnDown=new Date();
	    		
	    		try
	    		{
	    			//Sprint start and end
	    			Date_Min_BurnDown = sdf.parse("7/2/2018");	
	    			Date_Max_BurnDown = sdf.parse("7/21/2018");
	    		}
	    		catch (ParseException e1) 
	    		{
	    			e1.printStackTrace();
	    		}   		
	    	
	    		Week_Trend.add(Date_Min);
	    		Week_Trend_Of_BurnDown.add(Date_Min_BurnDown);
	    		
	    		 for(Date temp=Date_Min;!temp.after(Date_Max);)
	    		 {
	    			 Calendar calendar=Calendar.getInstance();
	    			 calendar.setTime(temp);
	    			 calendar.add(Calendar.DAY_OF_MONTH,2);
	    			 temp=calendar.getTime();
	    			 Week_Trend.add(temp);
	    		 }
	    		 
	    		 for(Date temp=Date_Min_BurnDown;!temp.after(Date_Max_BurnDown);)
	    		 {
	    			 Calendar calendar=Calendar.getInstance();
	    			 calendar.setTime(temp);
	    			 calendar.add(Calendar.DAY_OF_MONTH,2);
	    			 temp=calendar.getTime();
	    			 Week_Trend_Of_BurnDown.add(temp);
	    		 }
	    		 
	    		 ///Construct the week break record
	    		 ///Format : 0-Week Date, 1-Week Plan, 2- Week Finish, 3-Week Should
	    		 int Week_Date_Index=0;
	    		 int Week_Plan_Index=1;
	    		 int Week_Finish_Index=2;
	    		 int Week_PlanTotal_Index=3;
	    		 int Week_ShouldTotal_Index=4;
	    		 int Week_CloseTotal_Index=5;
	    		 List<List<String>> Week_Break = new ArrayList<>();
	    		 List<List<String>> Week_Break_BurnDown=new ArrayList<>();
	    		
	    		 //Initialize the week break record
	    		for(Date item:Week_Trend)
	    		{
	    			List<String> temp=new ArrayList<>();
	    			temp.add(sdf.format(item));  //0
	    			temp.add("0"); // 1 plan
	    			temp.add("0"); // 2 finish
	    			temp.add("0"); // 3 plan total
	    			temp.add("0"); // 4 should total
	    			temp.add("0"); // 5 close total
	    			Week_Break.add(temp);			
	    		}
	    		
	    		//Initialize the week break record
	    		for(Date item:Week_Trend_Of_BurnDown)
	    		{
	    			List<String> temp=new ArrayList<>();
	    			temp.add(sdf.format(item));  //0
	    			temp.add("0"); // 1 plan
	    			temp.add("0"); // 2 finish
	    			temp.add("0"); // 3 plan total
	    			temp.add("0"); // 4 should total
	    			temp.add("0"); // 5 close total
	    			Week_Break_BurnDown.add(temp);			
	    		}	    					 
	    					 
				 //Format the Week_Break
				for(int i=0;i<resultList.size();i++)
				 {
					List<String> tempList4=resultList.get(i); 	//get one data of FTAC project 
					String strTeam=TeamResultList.get(i);
					
					if(tempList4.get(4).equals(""))  	//if no [CreationDate], ignore this story
						 continue;
					if(tempList4.get(2).equals("")) 
						 continue;                    	//if no [StoryPoint], ignore this story
					 
					if(!Burn_Filter(tempList4.get(1),strTeam))
						continue;						//if [planned for] not contained by lstPlanFor, ignore this story
					
					 Date CreationDate = sdfget.parse(tempList4.get(4));
					 
					 for (List<String> tempList3:Week_Break )//the record of the sprint point
					 {
						 Date WeekDate = sdf.parse(tempList3.get(Week_Date_Index));
						 if(CreationDate.before(WeekDate))
						 {
							//get the plan point and add to the record
							 int temp_plan_point= Integer.parseInt(tempList3.get(Week_Plan_Index))+Integer.parseInt(tempList4.get(2));
							 tempList3.set(Week_Plan_Index,Integer.toString(temp_plan_point));
							 break;
						 }			 					 
					 }
					 
					for(int j=0;j<Week_Break.size();j++)
					 {
						 List<String> tempList3=Week_Break.get(j);
						 
						 if(tempList4.get(5).equals("")) 
							 continue;                    //if no [ResolutionDate], ignore this story
						 
						 Date WeekDate = sdf.parse(tempList3.get(Week_Date_Index));
						 Date ResolutionDate=sdfget.parse(tempList4.get(5));
						 
						 Calendar temp=Calendar.getInstance();
						 temp.setTime(WeekDate);
						 temp.add(Calendar.DAY_OF_MONTH,2);
						 
						 Date WeekEnd=temp.getTime();
						 						 
						 if(!ResolutionDate.before(WeekDate)&&ResolutionDate.before(WeekEnd))
						 {
							//get the plan point and add to the record
							 int temp_finish_point= Integer.parseInt(tempList3.get(Week_Finish_Index))+Integer.parseInt(tempList4.get(2));
							 tempList3.set(Week_Finish_Index,Integer.toString(temp_finish_point));
							 break;
						 }			 					 
					 }
				 }
				
				 //Format the Week_Break_Down
				for(int i=0;i<resultList.size();i++)
				 {
					List<String> tempList4=resultList.get(i); 
					String strTeam=TeamResultList.get(i);
					
					if(tempList4.get(4).equals(""))  //if no [CreationDate], ignore this story
						 continue;
					if(tempList4.get(2).equals("")) 
						 continue;                    //if no [StoryPoint], ignore this story
					 
					if(!Burn_Filter(tempList4.get(1),strTeam))
						continue;
					
					if(!tempList4.get(1).contains("Sprint26")) 
						 continue;                    //if not plan for sprint 25
					 
					 Sum_Plan_For_This_Sprint+=Integer.parseInt(tempList4.get(2));	
					
					for(int j=0;j<Week_Break_BurnDown.size();j++)
					 {
						 List<String> tempList3=Week_Break_BurnDown.get(j);							 					 
						 
						 if(tempList4.get(5).equals("")) 
							 continue;                    //if no [ResolutionDate], ignore this story						 
						 
						 Date WeekDate = sdf.parse(tempList3.get(Week_Date_Index));
						 Date ResolutionDate=sdfget.parse(tempList4.get(5));
						 
						 Calendar temp=Calendar.getInstance();
						 temp.setTime(WeekDate);
						 temp.add(Calendar.DAY_OF_MONTH,1);
						 
						 Date WeekEnd=temp.getTime();
						 						 
						 if(!ResolutionDate.before(WeekDate)&&ResolutionDate.before(WeekEnd))
						 {
							//get the plan point and add to the record
							 int temp_finish_point= Integer.parseInt(tempList3.get(Week_Finish_Index))+Integer.parseInt(tempList4.get(2));
							 tempList3.set(Week_Finish_Index,Integer.toString(temp_finish_point));
							 break;
						 }			 					 
					 }
				 }
				
				//add data to Point_of_Sprint
				for(int i=0;i<Point_of_Sprint.size();i++)
				 {
					 List<String> tempList3=Point_of_Sprint.get(i);
					 Date SprintBegin = sdf.parse(tempList3.get(Sprint_Start_Index));	
					 Date SprintEnd   = sdf.parse(tempList3.get(Sprint_End_Index));
					 
					 for(int j=0;j<resultList.size();j++)
					 {
						 List<String> tempList4=resultList.get(j);
						 //if can't get the attribute points,loop this for()
						 if(tempList4.get(2).equals("")) 
							 continue;
						 
						 String strTeam=TeamResultList.get(j);
						 if(!Burn_Filter(tempList4.get(1),strTeam))
								continue;
						 
						 //if record the create date
						 if(!tempList4.get(4).equals("")) 
						 {
							 Date CreationDate = sdfget.parse(tempList4.get(4));
							 
							 if((CreationDate.before(SprintEnd))&&tempList4.get(1).contains(tempList3.get(0)))	//To be modified
							 {
								 //get the plan point and add to the record
								 int temp_plan_point= Integer.parseInt(tempList3.get(Sprint_Plan_Index))+Integer.parseInt(tempList4.get(2));
								 tempList3.set(Sprint_Plan_Index,Integer.toString(temp_plan_point));								 
							 }
						 }
							 
						 //if record the resolution date
						 if(tempList4.get(1).contains(tempList3.get(0))&&(tempList4.get(3).equals("Closed"))) 
						 {							
							 int temp_finish_point=Integer.parseInt(tempList3.get(Sprint_Finish_Index))+Integer.parseInt(tempList4.get(2));
							 tempList3.set(Sprint_Finish_Index, Integer.toString(temp_finish_point));
						 }
					 }
				 }
				
	    	    		
	    		int sum_plan=0;
	    		int sum_close=0;
	    		for(int i=0;i<Week_Break.size();i++)
	    		{
	    			List<String> tempList6=Week_Break.get(i);
	    			sum_plan+=Integer.parseInt(tempList6.get(Week_Plan_Index));
	    			sum_close+=Integer.parseInt(tempList6.get(Week_Finish_Index));
	    			
	    			tempList6.set(Week_PlanTotal_Index, Integer.toString(sum_plan));
	    			tempList6.set(Week_CloseTotal_Index, Integer.toString(sum_close));
	    		}
	    		
	    		for(int i=0;i<Week_Break.size();i++)
	    		{
	    			List<String> tempList6=Week_Break.get(i);
	    			int should=sum_plan/(Week_Break.size()-1)*i;
	    			tempList6.set(Week_ShouldTotal_Index, Integer.toString(should));
	    		}
	    		
	    		int sum_close_burndown=0;
	    		for(int i=0;i<Week_Break_BurnDown.size();i++)
	    		{
	    			List<String> tempList6=Week_Break_BurnDown.get(i);
	    			sum_close_burndown+=Integer.parseInt(tempList6.get(Week_Finish_Index));
	    			
	    			tempList6.set(Week_CloseTotal_Index, Integer.toString(sum_close_burndown));
	    		}
	    		
	    		for(int i=0;i<Week_Break_BurnDown.size();i++)
	    		{
	    			List<String> tempList6=Week_Break_BurnDown.get(i);
	    			int should=Sum_Plan_For_This_Sprint/(Week_Break_BurnDown.size()-1)*i;
	    			tempList6.set(Week_ShouldTotal_Index, Integer.toString(should));
	    		}
	    		
	    		//add data to Point_of_Sprint
	    		/*for(int i = 0; i < Week_Break.size(); i++)
		    	{
					List<String> tempList4 = Week_Break.get(i);
					
		    	    Date week_date=sdf.parse((Week_Break.get(i)).get(Week_Date_Index));
		    	    Date Today=new Date();
		    	    if(week_date.before(Today))
		    	    {
		    	    	for(int j=0;j<Point_of_Sprint.size();j++)
						{
							List<String> tempList3 = Point_of_Sprint.get(j);
							Date SprintBegin = sdf.parse(tempList3.get(Sprint_Start_Index));
							Date SprintEnd = sdf.parse(tempList3.get(Sprint_End_Index));
							
							if(week_date.before(SprintEnd))
							{
								int temp_plan_point = Integer.parseInt(tempList3.get(Sprint_Plan_Index)) + Integer.parseInt(tempList4.get(1));
								tempList3.set(Sprint_Plan_Index,Integer.toString(temp_plan_point));
							
								int temp_finish_point = Integer.parseInt(tempList3.get(Sprint_Finish_Index)) + Integer.parseInt(tempList4.get(2));
								tempList3.set(Sprint_Finish_Index, Integer.toString(temp_finish_point));
							}
						}
		    	    }
		    	    
		    	}*/
	    	    		 
	    		List<String> x1=new ArrayList<>();
	    		List<Integer> y1=new ArrayList<>();
	    		List<Integer> y2=new ArrayList<>();
	    		List<Integer> y3=new ArrayList<>();
	    	    		
	    		//P1:Draw the Release Burn up, x1-date, y1-PlanTotal, y2-ShouldTotal, y3-CloseTotal, 
	    		//y4-forecasted trajectory 1(On Time),y5-forecasted trajectory 2(The trend in recent month)

	    		List<Double> y4=new ArrayList<>();	//forecasted trajectory 1
	    		List<Double> y5 = new ArrayList<>();	//forecasted trajectory 2
	    		
	    		int Plantotal = Integer.parseInt((Week_Break.get(Week_Break.size()-1)).get(Week_PlanTotal_Index));		//plantotal
    			int Closetotal = Integer.parseInt((Week_Break.get(Week_Break.size()-1)).get(Week_CloseTotal_Index));	//closetotal
    			int plan_remain = Plantotal - Closetotal;																//
    			
    			Date time_today=new Date();
    			int days = (int) ((Date_Max.getTime() - time_today.getTime()) / (1000*3600*24));
    			double ave_plan_daily = (double)plan_remain / (days + 2);
	    	    
	    	    //least square method in recent month
	    	    double t1=0.0, t2=0.0, t3=0.0, t4=0.0;
	    	    double a_forcast = 0.0, b_forcast = 0.0;
	    	    List<Integer> x_leastsquare = new ArrayList<>();
	    	    List<Integer> y_leastsquare = new ArrayList<>();
	    	    Date endDate = new Date();	//forecast completed date
	    	    
	    	    for(int i = 0; i < Week_Break.size(); i++)
	    	    {
	    	    	Date week_date=sdf.parse((Week_Break.get(i)).get(Week_Date_Index));
	    	    	Date Today=new Date();
	    	    	if(!week_date.after(Today))
	    	    	{
	    	    		x_leastsquare.add(i);
	    	    		y_leastsquare.add(Integer.parseInt((Week_Break.get(i)).get(Week_CloseTotal_Index)));
	    	    	}
	    	    }
	    	    if(x_leastsquare.size() > 0)
	    	    {
	    	    	for(int i = 0; i < x_leastsquare.size(); i++)
		    	    {
		    	    	t1 += x_leastsquare.get(i) * x_leastsquare.get(i);
		    	    	t2 += x_leastsquare.get(i);
		    	    	t3 += x_leastsquare.get(i) * y_leastsquare.get(i);
		    	    	t4 += y_leastsquare.get(i);
		    	    }
		    	    a_forcast = (t3*x_leastsquare.size() - t2*t4) / (t1*x_leastsquare.size() - t2*t2);
		    	    b_forcast = (t1*t4 - t2*t3) / (t1*x_leastsquare.size()- t2*t2);
		    	    
		    	    double x = 0.0;
		    	    x = (((double)Plantotal - b_forcast) /a_forcast) * 2 - 331;
		    	    if (x > 0)
		    	    {
			    	    int add_count = (int)(x/2);
			    	    for(int i = 1; i < add_count; i++)
			    	    {
			    	    	Calendar add_date = Calendar.getInstance();
				    	    add_date.setTime(Date_Max);
				    	    add_date.add(Calendar.DAY_OF_MONTH, (i + 1)*2);
				    	    Date temp_date = add_date.getTime();
				    	    List<String> temp = new ArrayList<>();
				    	    temp.add(sdf.format(temp_date));
				    	    temp.add("0");
				    	    temp.add("0");
				    	    temp.add("0");
				    	    temp.add("0");
				    	    temp.add("0");
				    	    Week_Break.add(temp);
				    	    endDate = temp_date;
			    	    }
		    		}
		    	
	    	    }
	    	    
	    	  //Forecasted trajectory 1, complete on time
	    		int start_show_count = 0;
	    		
	    	    for(int i=0;i<Week_Break.size();i++)
	    	    {
	    	    	Date WeekDate=sdf.parse((Week_Break.get(i)).get(Week_Date_Index));
	    			Date Today=new Date();
	    	    	if(!WeekDate.before(Today) && WeekDate.before(Date_Max))
	    	    	{
	    	    		start_show_count++;
	    	    		y4.add((double)Closetotal + start_show_count * ave_plan_daily * 2);
	    	    	}
	    	    	else
	    	    	{
	    	    		y4.add(-1.0);
	    	    	}
	    	    }
	    	    
	    	    //forecasted trajectory (2) based on least square method
	    	    int plan_total = 0;
	    	    for(int i = 0; i < Week_Break.size(); i++)
	    	    {
	    	    	Date week_date=sdf.parse((Week_Break.get(i)).get(Week_Date_Index));
	    	    	
	    	    	List<String> temp = Week_Break.get(i);
	    	    	plan_total+=Integer.parseInt(temp.get(Week_Plan_Index));
	    	    	temp.set(Week_PlanTotal_Index, Integer.toString(plan_total));
	    	    	
	    	    	if(week_date.before(time_today) || week_date.after(endDate))
	    	    	{
	    	    		y5.add(-1.0);
	    	    	}
	    	    	else
	    	    	{
	    	    		y5.add(a_forcast * i + b_forcast);
	    	    	}
	    	    }
	    	    
	    		for(List<String> item:Week_Break)
	    		{
	    			Date WeekDate=sdf.parse(item.get(Week_Date_Index));
	    			Date Today=new Date();
	    			
	    			Date X_Axis=sdf.parse(item.get(Week_Date_Index));
	    			x1.add(sdf1.format(X_Axis));
	    			
	    			y1.add(Integer.parseInt(item.get(Week_PlanTotal_Index)));
	    			
	    			if(WeekDate.after(Date_Max))
	    			{
	    				y2.add(-1);
	    			}
	    			else
	    			{
	    				y2.add(Integer.parseInt(item.get(Week_ShouldTotal_Index)));
	    			}
	    			
	    			if(WeekDate.after(Today))
	    			{
	    				y3.add(-1);	
	    			}
	    			else
	    			{
	    				y3.add(Integer.parseInt(item.get(Week_CloseTotal_Index)));	
	    			}
	    		}
	    	   
	    		Create_P1(x1,y1,y2,y3,y5);
	    		
	    		              
	    		//P2:Draw the Release BurnDown
	    		x1.clear();
	    		y1.clear();
	    		y2.clear();
	    		y3.clear();
	    		y4.clear();
	    		y5.clear();
	    		
    			for(int i=0;i<Week_Break_BurnDown.size();i++)
	    		{
	    			List<String> item=Week_Break_BurnDown.get(i);
	    			
	    			Date WeekDate=sdf.parse(item.get(Week_Date_Index));
	    			Date Today=new Date();
	    			
	    			x1.add(sdf2.format(WeekDate));
	    			y1.add(Sum_Plan_For_This_Sprint-Integer.parseInt(item.get(Week_ShouldTotal_Index)));
	    			
	    			if(i==0)
	    			{
	    				y2.add(Sum_Plan_For_This_Sprint);
	    			}
	    			else
	    			{
	    				if(WeekDate.after(Today))
	    				{
	    					y2.add(-1);
	    				}
	    				else
	    				{
	    					y2.add(Sum_Plan_For_This_Sprint-Integer.parseInt(Week_Break_BurnDown.get(i-1).get(Week_CloseTotal_Index)));
	    				}
	    			}
	    			y3.add(Sum_Plan_For_This_Sprint);
	    		}
	    		
	    		Create_P2(x1,y1,y2,y3);
    	    		
	    	    		
	    		//P3:Draw the Plan vs Actual
	    		x1.clear();
	    		y1.clear();
	    		y2.clear();
	    		y3.clear();
	    		y4.clear();
	    		y5.clear();
	    		
	    		List<Double> yy=new ArrayList<>();	//finished average of Sprint
	    		
	    		int sprint_plan_total = 0;
	    		int sprint_finish_total = 0;
	    		
	    		int Sprint_Average=0;
	    		int TodayCount=0;
	    		
				Date today=new Date();
				Calendar calendar=Calendar.getInstance();
    			calendar.setTime(today);
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{
	    			Date SprintBegin = sdf.parse(item.get(Sprint_Start_Index));	
					//Date SprintEnd   = sdf.parse(item.get(Sprint_End_Index));
					
					System.out.println(item.get(0)+"\t"+item.get(1)+"\t"+item.get(2));
	    			
	    			x1.add(item.get(0));

	    			if(today.after(SprintBegin))
	    			{
	    				TodayCount++;
	    				sprint_plan_total = Integer.parseInt(item.get(Sprint_Plan_Index));
		    			y1.add(sprint_plan_total);//plan
		    			
		    			sprint_finish_total = Integer.parseInt(item.get(Sprint_Finish_Index));
	    				y2.add(sprint_finish_total);//finish
	    				
	    				Sprint_Average += sprint_finish_total;
	    			}
	    			else
	    			{
	    				y1.add(0);
	    				y2.add(0);//when current sprint, set actual = 0  
	    			}
	    		}
	    		
	    		if(TodayCount == 0)
	    		{
	    			TodayCount++;
	    		}
	    		Sprint_Average = sprint_finish_total/TodayCount;
	    		
	    		for(List<String> item:Point_of_Sprint)
	    		{
	    			Date SprintBegin = sdf.parse(item.get(Sprint_Start_Index));	
	    			
	    			if(today.after(SprintBegin))
	    			{
	    				yy.add((double)Sprint_Average);
	    			}
	    		}
	    		
	    		
	    		Create_P3(x1,yy,y1,y2);
	    	}
	    }
	    catch(Exception e1)
    	{
    		System.out.println("FTAC Exception in ChartofPM :"+e1.toString());
    	}
	}
	
	//Lane Ma,judge if the story is the needed one
	public static boolean Burn_Filter(String PlanFor,String Team)
	{
		boolean isTimed=false;
		boolean isNeedTeam=true;
		List<String> lstPlanFor=Arrays.asList(
											  "FTAC_9.0",
											  "Sprint17",
											  "Sprint18",
											  "Sprint19",
											  "Sprint20",
											  "Sprint21",
											  "Sprint22",
											  "Sprint23",
											  "Sprint24",
											  "Sprint25",
											  "Sprint26",
											  "Sprint27",
											  "Sprint28"
											  );
		
		List<String> lstOtherTeam=Arrays.asList();
		
		for(String strPlanFor:lstPlanFor)
		{
			if(PlanFor.contains(strPlanFor))
			{
				isTimed=true;
				break;
			}
		}
		
		for(String strOtherTeam:lstOtherTeam)
		{
			if(strOtherTeam.equals(Team))
			{
				isNeedTeam=false;
				break;
			}
		}
		
		return isTimed&isNeedTeam;
	}
	//Lane Ma, Modify the demo to draw specific chart
	public  static void Create_P1(List<String> x1,List<Integer> y1,List<Integer> y2,List<Integer> y3,List<Double> y5)	//,List<Double> y4
	{	
		// 
		FTAC_PM_Data_Weekly_Trend=new ProductData();
		FTAC_PM_Data_Weekly_Trend.title=ConstString.FTAC_PM_CHART_Weekly_Trend;
		
		FTAC_PM_Data_Weekly_Trend.description="";//description		
		FTAC_PM_Data_Weekly_Trend.yTitle="Story Point";
		FTAC_PM_Data_Weekly_Trend.yAxisFormat="#";
		FTAC_PM_Data_Weekly_Trend.tableData=new DataTable();
		FTAC_PM_Data_Weekly_Trend.colorList=Arrays.asList(ColorFormater.RGB2String(145,38,41),ColorFormater.RGB2String(129,173,81),ColorFormater.RGB2String(58,63,113),
														ColorFormater.RGB2String(72,118,255),ColorFormater.RGB2String(255,255,255));		//ColorFormater.RGB2String(222,63,255),
		
		FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Total Estimate"));
		FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Ideal Closed Estimate"));
		FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y3", ValueType.INT, "Closed Estimate"));
		//FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y4", ValueType.INT, "Forecasted Trajectory1"));
		FTAC_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y5", ValueType.INT, "Forecasted Trajectory"));
		
		//Chart data
		//////////////////////////////////////////////
		List<String> x_data=x1;
		List<Integer> y1_data=y1;
		List<Integer> y2_data=y2;		// TO Optimize
		List<Integer> y3_data=y3;
		//List<Integer> y4_data = new ArrayList<>();
		List<Integer> y5_data = new ArrayList<>();
		
		/*for(int i = 0; i < y4.size();i++)
		{
			int temp = Integer.parseInt(new java.text.DecimalFormat("0").format(y4.get(i)));
			y4_data.add(i, temp);
		}*/
		for(int i = 0; i < y5.size();i++)
		{
			int temp = Integer.parseInt(new java.text.DecimalFormat("0").format(y5.get(i)));
			y5_data.add(i, temp);
		}
		
		
		int dataCount1=x_data.size();

		List<TableRow> rows = Lists.newArrayList();
				
		for(int i=0;i<dataCount1;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
			row.addCell(new TableCell(y2_data.get(i)));
		    row.addCell(new TableCell(y3_data.get(i)));
			//row.addCell(new TableCell(y4_data.get(i)));
		    row.addCell(new TableCell(y5_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTAC_PM_Data_Weekly_Trend.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	//Lane Ma, Modify the demo to draw specific chart
	public static void Create_P2(List<String> x1,List<Integer> y1, List<Integer> y2,List<Integer> y3)
	{	
		FTAC_PM_Data_Weekly_BurnDown=new ProductData();
		FTAC_PM_Data_Weekly_BurnDown.title=ConstString.FTAC_PM_CHART_Weekly_BurnDown;
		
		FTAC_PM_Data_Weekly_BurnDown.description="";//description		
		FTAC_PM_Data_Weekly_BurnDown.yTitle="Story Point";
		FTAC_PM_Data_Weekly_BurnDown.yAxisFormat="#";
		FTAC_PM_Data_Weekly_BurnDown.tableData=new DataTable();
		FTAC_PM_Data_Weekly_BurnDown.colorList=Arrays.asList(ColorFormater.RGB2String(20,83,114),ColorFormater.RGB2String(176,38,59),ColorFormater.RGB2String(230,230,230));
		
		FTAC_PM_Data_Weekly_BurnDown.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		FTAC_PM_Data_Weekly_BurnDown.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Ideal Line"));
		FTAC_PM_Data_Weekly_BurnDown.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "ToDo"));
		FTAC_PM_Data_Weekly_BurnDown.tableData.addColumn(new ColumnDescription("y3", ValueType.INT, "All"));
		
		//Chart data
		//////////////////////////////////////////////
		List<String> x_data=x1;
		List<Integer> y1_data=y1;
		List<Integer> y2_data=y2;
		List<Integer> y3_data=y3;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    row.addCell(new TableCell(y3_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTAC_PM_Data_Weekly_BurnDown.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void Create_P3(List<String> x1,List<Double> y3,List<Integer> y1, List<Integer> y2)
	{	
		FTAC_PM_Data_Plan_Actual_Sprint=new ProductData();
		FTAC_PM_Data_Plan_Actual_Sprint.title=ConstString.FTAC_PM_CHART_Plan_Actual_Sprint;
		
		FTAC_PM_Data_Plan_Actual_Sprint.description="";//description		
		FTAC_PM_Data_Plan_Actual_Sprint.yTitle="Story Point";
		FTAC_PM_Data_Plan_Actual_Sprint.yAxisFormat="#";
		FTAC_PM_Data_Plan_Actual_Sprint.tableData=new DataTable();
		FTAC_PM_Data_Plan_Actual_Sprint.colorList=Arrays.asList("green",ColorFormater.RGB2String(91,155,213),ColorFormater.RGB2String(237,125,49));

		
		FTAC_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		FTAC_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y3", ValueType.INT, "Average"));
		FTAC_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Planned"));
		FTAC_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Actual"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=x1;
				List<Double> y3_data=y3;
				List<Integer> y1_data=y1;
				List<Integer> y2_data=y2;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y3_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTAC_PM_Data_Plan_Actual_Sprint.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void Create_P8(List<String> x1,List<Integer> y1, List<Integer> y2,List<Integer> y3,int lengthMax)
	{	
		FTAC_PM_Data_Feature_Progress=new ProductData();
		FTAC_PM_Data_Feature_Progress.title=ConstString.FTAC_PM_CHART_Feature_Progress;
		FTAC_PM_Data_Feature_Progress.description="";//description		
		FTAC_PM_Data_Feature_Progress.yAxisFormat="#";
		FTAC_PM_Data_Feature_Progress.tableData=new DataTable();
		FTAC_PM_Data_Feature_Progress.colorList=Arrays.asList(ColorFormater.RGB2String(112,173,71),ColorFormater.RGB2String(68,114,196),ColorFormater.RGB2String(255,20,20));
		
		FTAC_PM_Data_Feature_Progress.isStacked="true";
		FTAC_PM_Data_Feature_Progress.chartLeft=7*lengthMax;
		
		FTAC_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
		FTAC_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
		FTAC_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
		FTAC_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y3", ValueType.INT, "Risk"));
		
		//Chart data
		//////////////////////////////////////////////
		List<String> x_data=x1;
		List<Integer> y1_data=y1;
		List<Integer> y2_data=y2;
		List<Integer> y3_data=y3;
		
		int dataCount=x_data.size();
		List<TableRow> rows = Lists.newArrayList();
		for(int i=0;i<dataCount;i++)
		{
			TableRow row = new TableRow();
		    row.addCell(new TableCell(x_data.get(i)));
		    row.addCell(new TableCell(y1_data.get(i)));
		    row.addCell(new TableCell(y2_data.get(i)));
		    row.addCell(new TableCell(y3_data.get(i)));
		    rows.add(row);
		}
		try 
		{
			FTAC_PM_Data_Feature_Progress.tableData.addRows(rows);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
		}
	}
	
	public static void AddEpicLevel(EpicItem item)
	{
		item.m_nlevel+=1;
		for(String childitem:item.ChildID)
		{
			if(mapEpic.containsKey(childitem))
			{
				mapEpic.get(childitem).m_nlevel+=1;
				EpicItem currentEpic=mapEpic.get(childitem);
				if(!currentEpic.ChildID.isEmpty())
				{
					AddEpicLevel(currentEpic);
				}
			}
		}
	}
	
	public static void CalculateEpicStoryPoint(EpicItem epic,int parentLevel)
	{		
		if(epic.m_isCompleted)
			return;
		
		for(String id:epic.ChildID)
		{
			if(mapFeature.containsKey(id))  //if this item is a feature, add the story directly
			{
				epic.FinishPoint += mapFeature.get(id).FinishPoint;
				epic.RemainPoint += mapFeature.get(id).RemainPoint;
				epic.RiskEstimate+= mapFeature.get(id).RiskEstimate;
				epic.TotalPoint+=mapFeature.get(id).TotalPoint;
				
				continue;
			}
			
			if(mapEpic.containsKey(id))
			{
				if(mapEpic.get(id).m_isCompleted)
				{
					epic.FinishPoint += mapEpic.get(id).FinishPoint;
					epic.RemainPoint += mapEpic.get(id).RemainPoint;
					epic.RiskEstimate+= mapEpic.get(id).RiskEstimate;
					epic.TotalPoint	 +=	mapEpic.get(id).TotalPoint;
					
					AddEpicLevel(mapEpic.get(id));
				}
				else
				{
					EpicItem temp;
					temp=mapEpic.get(id);
					CalculateEpicStoryPoint(temp,parentLevel+1);
					mapEpic.put(temp.EpicID, temp);
					
					epic.FinishPoint += mapEpic.get(id).FinishPoint;
					epic.RemainPoint += mapEpic.get(id).RemainPoint;
					epic.RiskEstimate+= mapEpic.get(id).RiskEstimate;
					epic.TotalPoint  +=mapEpic.get(id).TotalPoint;
				}				
				continue;
			}
		}
		
		epic.m_nlevel=parentLevel;
		epic.m_isCompleted=true;
	}
	
}

