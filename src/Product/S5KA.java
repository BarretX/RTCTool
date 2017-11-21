package Product;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import GetAttributeDispValue.GetAttributesValue;
import Login.LoginHandler;
import Main.Calendar_Compare;
import QueryReference.QueryChild;
import SearchWorkItem.MulConditionQuery;
import SearchWorkItem.SearchCondition;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
import Charts.ChartSets;
import Charts.ColumnChart;
import Charts.ComboChart;
import Charts.LineChart;
import EagleEyeAPI.ConstString;

public class S5KA {
	
	public static void ChartOfQA()
	{
		String repositoryURI ="https://ccmclm.rockwellautomation.com/ccm";
	    
	    String userId = "JMa7";
	    String password = "WelcomeLaneMa00";
	    LoginHandler handler=new LoginHandler(userId,password,repositoryURI);
	    
	    ITeamRepository repository=handler.login();
	    
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
		GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(0));
		List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
		for(String str: allDispNames)
		{
			System.out.println(str);
		}

	    
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.defect", AttributeOperation.EQUALS));
		
	    List<String> needAttributeList = new ArrayList<>();
	    needAttributeList.add("Type");//pass                       0
	    needAttributeList.add("Id");//pass                         1
	    needAttributeList.add("Summary");//pass                    2
	    needAttributeList.add("Owned By");//pass                   3
	    needAttributeList.add("Status");//pass                     4
	    needAttributeList.add("Priority");//pass                   5
	    needAttributeList.add("Customer Severity");//pass          6
	    needAttributeList.add("Last Modified Date");//pass         7
	    needAttributeList.add("Customer Impact");//pass            8
	    needAttributeList.add("Customer Occurrence");//pass        9
//	    needAttributeList.add("Phase Detected");//pass             10
	    needAttributeList.add("Must Be Fixed");//pass              11
	    needAttributeList.add("Creation Date");//pass              12
	    needAttributeList.add("Found in SW Version");//pass        13
	    needAttributeList.add("Found In Version");//pass           14
	    needAttributeList.add("Target Release");//pass             15
	    needAttributeList.add("Resolved By");//pass                16
//	    needAttributeList.add("Resolution");//pass                 17
	    needAttributeList.add("Feature");//pass                    18
	    needAttributeList.add("Closed Date");//pass                19
//	    needAttributeList.add("Phase Injected");//pass             20
	    needAttributeList.add("Filed Against");//pass              21
//	    needAttributeList.add("Customer First Escalation");//pass  22
	    needAttributeList.add("Verified Date");//pass              23
	    needAttributeList.add("Impacted Product");//pass           24
//	    needAttributeList.add("SAP Ticket");//pass                 25
	    	    
	    try {

		    MulConditionQuery query=new MulConditionQuery();
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(0), null, conditionsList);		    
	    		 
	    	if(resultOwner!=null)
	    	{
	    		resultOwner.setLimit(100);	
	    		List<List<String>> resultList = getAttributesValue.GetAllNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),resultOwner,needAttributeList);
	    		
	    		//show all data
	    		int j=0;
	    		for(List<String> tmpList2 : resultList)
	    		{
	    			int i = 0;
	    			j++;
	    			System.out.print(j+" ");
	    			for(String str : tmpList2)
	    			{
	    				System.out.print(i++ + "  "+str+"  ;; ");
	    			}
	    			System.out.println();
	    		}
	    		
	    		//Save all data
	    		try
	    		{	        		
	        		WritableWorkbook book =Workbook.createWorkbook(new File("ExcelSource/Anomaly Metrics_S5KA.xls"));
	        		WritableSheet sheet=book.createSheet("sourcedata_S5KA", 0);
	        		
	        		for(int i=0;i<needAttributeList.size();i++)
	        		{
	        			Label label=new Label(i,0,needAttributeList.get(i));
	        			sheet.addCell(label);
	        		}
	        		
	        		
	        		for(int i=0;i<resultList.size();i++)
	        		{
	        			for(int m=0;m<resultList.get(i).size();m++)
	        			{
	        				Label label=new Label(m,i+1,resultList.get(i).get(m));
	        				sheet.addCell(label);
	        			}
	        		}
	        		
	        		book.write();
	        		book.close();
	    		}
	    		catch(Exception e)
	    		{
	    			System.out.println(e);
	    		}
	    		
	    		
                //calculate the data need in next 
	    		List<ArrayList<String>> SourceDataFilled=CalculateSourceData(needAttributeList,resultList); 	    		
	    		
	    		//calculate the anomaly count created in every week
	    		List<ArrayList<String>> ResultData=S5KA_FY17_Anomlay_Backlog3_SourceData_Week(SourceDataFilled);

	    		//Fill the x and y souce date of S5KA_Incoming
	    		List<String> X_Data=new ArrayList<String>();
	    		List<Integer> Y1_Data=new ArrayList<Integer>();
	    		List<Integer> Y2_Data=new ArrayList<Integer>();
	    		List<Integer> Y3_Data=new ArrayList<Integer>();
	    		for(List<String> tmpList3:ResultData)
	    		{	    			
	    			X_Data.add(tmpList3.get(0));
	    			Y1_Data.add(Integer.parseInt(tmpList3.get(1)));
	    			Y2_Data.add(Integer.parseInt(tmpList3.get(2)));
	    			Y3_Data.add(Integer.parseInt(tmpList3.get(5)));	    			
	    		}
	    		
	    	//    System.out.println("S5KA FY17 Anomlay Backlog_3.0 Weekly:\n"+ConstString.CHART_URL+ Set_Create_For_QA_S5KA_1(X_Data,Y1_Data,Y2_Data));
	    	  //  System.out.println("Shippable State_S5KA RC anomaly Weekly:\n"+ConstString.CHART_URL+ Set_Shippable_RC_Weekly(X_Data,Y3_Data));
	    	    String Anomlay_Week=Set_Create_For_QA_S5KA_1(X_Data,Y1_Data,Y2_Data);
	    	    String RC_Week=Set_Shippable_RC_Weekly(X_Data,Y3_Data);

	    	    
	    	    ResultData.clear();
	    	    X_Data.clear();
	    	    Y1_Data.clear();
	    	    Y2_Data.clear();
	    	    Y3_Data.clear();
	    	    ResultData=S5KA_FY17_Anomlay_Backlog3_SourceData_Sprint(SourceDataFilled);
	    	    for(List<String> tmpList3:ResultData)
	    		{	    			
	    			X_Data.add(tmpList3.get(2));
	    			Y1_Data.add(Integer.parseInt(tmpList3.get(3)));
	    			Y2_Data.add(Integer.parseInt(tmpList3.get(4)));
	    			Y3_Data.add(Integer.parseInt(tmpList3.get(7)));	    			
	    		}
	    	  //  System.out.println("S5KA FY17 Anomlay Backlog_3.0 Sprint:\n"+ConstString.CHART_URL+ Set_Create_For_QA_S5KA_Sprint(X_Data,Y1_Data,Y2_Data));
	    	    //System.out.println("Shippable State_S5KA RC anomaly Sprint:\n"+ConstString.CHART_URL+ Set_Shippable_RC_Sprint(X_Data,Y3_Data));	    	    
	    	    String Anomlay_Sprint=Set_Create_For_QA_S5KA_Sprint(X_Data,Y1_Data,Y2_Data);
	    	    String RC_Sprint= Set_Shippable_RC_Sprint(X_Data,Y3_Data);	    	    

	    	    
	    	    ResultData.clear();
	    	    X_Data.clear();
	    	    Y1_Data.clear();
	    	    Y2_Data.clear();
	    	    Y3_Data.clear();
	    	    ResultData= S5KA_FY17_Anomlay_Backlog3_SourceData_Monthly(SourceDataFilled);
	    	    for(List<String> tmpList3:ResultData)
	    		{	    			
	    			X_Data.add(tmpList3.get(0));
	    			Y1_Data.add(Integer.parseInt(tmpList3.get(1)));
	    			Y2_Data.add(Integer.parseInt(tmpList3.get(2)));
	    			Y3_Data.add(Integer.parseInt(tmpList3.get(5)));	    			
	    		}
	    	   // System.out.println("S5KA FY17 Anomlay Backlog_3.0 Monthly:\n"+ConstString.CHART_URL+ Set_Create_For_QA_S5KA_Monthly(X_Data,Y1_Data,Y2_Data));
	    	   // System.out.println("Shippable State_S5KA RC anomaly Monthly:\n"+ConstString.CHART_URL+ Set_Shippable_RC_Monthly(X_Data,Y3_Data));
	    	    String Anomlay_Monthly=Set_Create_For_QA_S5KA_Monthly(X_Data,Y1_Data,Y2_Data);
	    	    String RC_Monthly=Set_Shippable_RC_Monthly(X_Data,Y3_Data);
	    	    
	    	    String AnomlaySetID=ChartSets.Create("S5KA FY17 Anomlay Backlog_3.0", "S5KA_QA", Arrays.asList(Anomlay_Week,Anomlay_Sprint,Anomlay_Monthly));
	    	    System.out.println("S5KA FY17 Anomlay Backlog_3.0:\n"+ConstString.CHART_URL+ AnomlaySetID);
	    	    
	    	    String RCSetID=ChartSets.Create("Shippable State_S5KA RC anomaly", "S5KA_QA", Arrays.asList(RC_Week,RC_Sprint,RC_Monthly));
	    	    System.out.println("Shippable State_S5KA RC anomaly:\n"+ConstString.CHART_URL+ RCSetID);
	    	    
	    	    
	    	    
	    	  //calculate the anomaly count created in every week
	    	   /*(0)Sprint;(1)S5KA Incoming;(2)S5KA Fixed; (3)S5KA Incoming total; (4)S5KA Fixed total
	    	     (5)S5KA Incoming rc (6)S5KA Fixed rc (7)S5KA Incoming rc total (8)S5KA Incoming Fixed total*/
	    		List<ArrayList<String>> ResultData1=CalculateBySprint(SourceDataFilled);

	    		//Fill the x and y souce date of S5KA_Incoming
	    		List<String> X_Data_1=new ArrayList<String>();
	    		List<Integer> Y1_Data_1=new ArrayList<Integer>();
	    		List<Integer> Y2_Data_1=new ArrayList<Integer>();
	    		List<Integer> Y1_Data_2=new ArrayList<Integer>();
	    		List<Integer> Y2_Data_2=new ArrayList<Integer>();
	    		List<Integer> Y1_Data_3=new ArrayList<Integer>();
	    		List<Integer> Y2_Data_3=new ArrayList<Integer>();
	    		List<Integer> Y1_Data_4=new ArrayList<Integer>();
	    		List<Integer> Y2_Data_4=new ArrayList<Integer>();
	    		for(List<String> tmpList3:ResultData1)
	    		{
	    			X_Data_1.add(tmpList3.get(0));	
	    			Y1_Data_1.add(Integer.parseInt(tmpList3.get(1)));
	    			Y2_Data_1.add(Integer.parseInt(tmpList3.get(2)));
	    			Y1_Data_2.add(Integer.parseInt(tmpList3.get(3)));
	    			Y2_Data_2.add(Integer.parseInt(tmpList3.get(4)));
	    			Y1_Data_3.add(Integer.parseInt(tmpList3.get(5)));
	    			Y2_Data_3.add(Integer.parseInt(tmpList3.get(6)));
	    			Y1_Data_4.add(Integer.parseInt(tmpList3.get(7)));
	    			Y2_Data_4.add(Integer.parseInt(tmpList3.get(8)));
	    		}
	    		
	    	    String ID1= Set_Create_For_QA_S5KA_3(X_Data_1,Y1_Data_1,Y2_Data_1);
	    	    String ID2=Set_Create_For_QA_S5KA_5(X_Data_1,Y1_Data_3,Y2_Data_3);
	    	    String ID3= Set_Create_For_QA_S5KA_4(X_Data_1,Y1_Data_2,Y2_Data_2);	    	    
	    	    String ID4= Set_Create_For_QA_S5KA_6(X_Data_1,Y1_Data_4,Y2_Data_4);
	    	    
	    	    String RCSet5=ChartSets.Create("S5KA Runrate", "S5KA_QA", Arrays.asList(ID1,ID2));
	    	    System.out.println("S5KA Runrate:\n"+ConstString.CHART_URL+ RCSet5);
	    	    
	    	    String RCSet6=ChartSets.Create("S5KA Stability", "S5KA_QA", Arrays.asList(ID3,ID4));
	    	    System.out.println("S5KA Stability:\n"+ConstString.CHART_URL+ RCSet6);
	    	}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	    handler.logoff();
	}


	public static void ChartOfPM()
	{
		String repositoryURI ="https://ccmclm.rockwellautomation.com/ccm";
	    
	    String userId = "JMa7";
	    String password = "WelcomeLaneMa00";
	    LoginHandler handler=new LoginHandler(userId,password,repositoryURI);
	    
	    ITeamRepository repository=handler.login();
	    
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
		GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(0));
		List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
		for(String str: allDispNames)
		{
			System.out.println(str);
		}
		 
	    List<SearchCondition> conditionsList = new ArrayList<>(); 
	    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
//	    conditionsList.add(new SearchCondition(IWorkItem.ID_PROPERTY,"12177",AttributeOperation.EQUALS));
	    
	    Calculate_BurnDown	( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//1,2,6,7,8
	    Calculate_LogixLike( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//3
	    Calculate_FW_Related( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//4
	    Calculate_Others( repository, handler,projectAreaNames,getAttributesValue,conditionsList);//5
	}
	public static String Set_Shippable_RC_Weekly(List<String> x1,List<Integer> y1)
	{
		Chart S5KA_One_Chart=new ColumnChart("Shippable State_S5KA RC anomlay Weekly");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Week";
		S5KA_One_Chart.yTitle="";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		
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
	
	public static String Set_Shippable_RC_Sprint(List<String> x1,List<Integer> y1)
	{		
		Chart S5KA_One_Chart=new ColumnChart("Shippable State_S5KA RC anomlay Sprint");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Sprint";
		S5KA_One_Chart.yTitle="";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		
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
	
	public static String Set_Shippable_RC_Monthly(List<String> x1,List<Integer> y1)
	{		
		Chart S5KA_One_Chart=new ColumnChart("Shippable State_S5KA RC anomlay Monthly");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Month";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Month"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		
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

	public static String Set_Create_For_QA_S5KA_2(List<String> x1,List<Integer> y1)
	{
		Chart S5KA_One_Chart=new ColumnChart("S5KA_incoming");//Title
		
		S5KA_One_Chart.description="S5KA auto in each week";//description		
		S5KA_One_Chart.xTitle="Time";
		S5KA_One_Chart.yTitle="S5KA(incoming)";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
		
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
	
	
	public static String Set_Create_For_QA_S5KA_1(List<String> x1,List<Integer> y1,List<Integer> y2)
	{
		
		Chart S5KA_One_Chart=new ColumnChart("S5KA FY17 Anomlay Backlog_3.0 Weekly");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Week";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","blue");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Week"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
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
	
	public static String Set_Create_For_QA_S5KA_Sprint(List<String> x1,List<Integer> y1,List<Integer> y2)
	{		
		Chart S5KA_One_Chart=new ColumnChart("S5KA FY17 Anomlay Backlog_3.0 Sprint");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","blue");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
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
	
	public static String Set_Create_For_QA_S5KA_Monthly(List<String> x1,List<Integer> y1,List<Integer> y2)
	{
		Chart S5KA_One_Chart=new ColumnChart("S5KA FY17 Anomlay Backlog_3.0 Monthly");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","blue");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Month"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
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

	
	public static String Set_Create_For_QA_S5KA_3(List<String> x1,List<Integer> y1,List<Integer> y2)
	{
		Chart S5KA_One_Chart=new LineChart("S5KA Runrate (all anomally)");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","blue");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
		
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

//Lane:Calculate the anomaly count created in every week
	public static List<ArrayList<String>> CalculateByWeek(List<ArrayList<String>> sourcedata) 
	{
		List<ArrayList<String>> ResultData=new ArrayList<ArrayList<String>>();
		
		Calendar c_begin=new GregorianCalendar();
		Calendar c_end=new GregorianCalendar();
		Calendar c_LastMonday=new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				
		c_LastMonday.set(2017,1,22);
		c_begin.set(2017,1,26); // start date, the index of the month in calendar is between 0-11
		c_end.set(2017,7,31);   // end date
		while(c_begin.before(c_end))
		{
			if(c_begin.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)
			{
				int i=0;
				ArrayList<String> InsertItem=new ArrayList<String>();
				for(ArrayList<String> tempList:sourcedata)
				{
					String temp="";
					temp=tempList.get(12); //get the create date
					try
					{
						Date date = sdf.parse(temp);
						Calendar CreateDate=new GregorianCalendar();
						CreateDate.setTime(date);
						if(CreateDate.after(c_LastMonday)&&CreateDate.before(c_begin))  //Create in this week
						{
							i++;
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}			
				}
				String dateStr=sdf1.format(c_begin.getTime());
				InsertItem.add(String.valueOf(i));
				InsertItem.add(dateStr);
				ResultData.add(InsertItem);	
				c_LastMonday.set(c_begin.get(Calendar.YEAR),c_begin.get(Calendar.MONTH),c_begin.get(Calendar.DATE));
			}
			c_begin.add(Calendar.DAY_OF_YEAR,1);
		}
		return ResultData;		
	}

//Lane:Calculate the sourcedata to get the data need in next step
	public static List<ArrayList<String>> CalculateSourceData(List<String> soucedatatitle,List<List<String>> sourcedata)
	{
		List<ArrayList<String>> SourceDataFilled=new ArrayList<ArrayList<String>>();
		for(List<String> tmpList3:sourcedata)
		{
			ArrayList<String> SourceDataItem=new ArrayList<>();
			int IndexCow;
			String TemString=new String("");
			//Found in 4.0 index:0
			IndexCow=soucedatatitle.indexOf("Found in SW Version");
			TemString=tmpList3.get(IndexCow);
			if(!TemString.equals("3.0"))
			{
				SourceDataItem.add("1");
			}
			else
			{
				SourceDataItem.add("0");
			}
			
			//Found in 3.0 inedx:1
			IndexCow=soucedatatitle.indexOf("Found in SW Version");
			TemString=tmpList3.get(IndexCow);
			if(TemString.equals("3.0"))
			{
				SourceDataItem.add("1");
			}
			else
			{
				SourceDataItem.add("0");
			}
			
			//Document  index:2
			//needAttributeList.indexOf("")
			SourceDataItem.add("0");
			
			//Not Committed  index:3
			IndexCow=soucedatatitle.indexOf("Target Release");
			TemString=tmpList3.get(IndexCow);
			if(TemString.equals("Future"))
			{
				SourceDataItem.add("1");
			}
			else
			{
				SourceDataItem.add("0");
			}
			
			//RC index:4
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			TemString=tmpList3.get(IndexCow);
			if(TemString==null||TemString.length()<=0)
			{
				SourceDataItem.add("0");
			}
			else
			{
				if(Float.parseFloat(TemString)>23)
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA=[16,24) index:5 
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			if(TemString==null||TemString.length()<=0)
			{
    			SourceDataItem.add("0");
			}
			else
			{
				if((Float.parseFloat(TemString)>=16)&&(Float.parseFloat(TemString)<24))
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA=(16,24) index:6 
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			if(TemString==null||TemString.length()<=0)
			{
    			SourceDataItem.add("0");
			}
			else
			{
				if((Float.parseFloat(TemString)>16)&&(Float.parseFloat(TemString)<24))
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA=20 index: 7
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			if(TemString==null||TemString.length()<=0)
			{
    			SourceDataItem.add("0");
			}
			else
			{
				if(Float.parseFloat(TemString)==20)
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA=16 index : 8
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			if(TemString==null||TemString.length()<=0)
			{
    			SourceDataItem.add("0");
			}
			else
			{
				if(Float.parseFloat(TemString)==16)
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA<16 index : 9
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			if(TemString==null||TemString.length()<=0)
			{
    			SourceDataItem.add("0");
			}
			else
			{
				if(Float.parseFloat(TemString)<16)
    			{
    				SourceDataItem.add("1");
    			}
    			else
    			{
    				SourceDataItem.add("0");
    			}
			}
			
			//FMEA index : 10
			IndexCow=soucedatatitle.indexOf("Customer Impact");
			SourceDataItem.add(tmpList3.get(IndexCow));
			
			//Active index :11
			IndexCow=soucedatatitle.indexOf("Status");
			TemString=tmpList3.get(IndexCow);
			if(TemString.equals("Closed"))
			{
				SourceDataItem.add("0");
			}
			else
			{
				SourceDataItem.add("1");
			}
			
			//Create Date index : 12
			IndexCow=soucedatatitle.indexOf("Creation Date");
			SourceDataItem.add(tmpList3.get(IndexCow));	    			
			
			//Verify Date index : 13
			IndexCow=soucedatatitle.indexOf("Verified Date");
			SourceDataItem.add(tmpList3.get(IndexCow));	    			
			
			SourceDataFilled.add(SourceDataItem);
		}
		return SourceDataFilled;
	}
	public static List<ArrayList<String>> CalculateBySprint(List<ArrayList<String>> sourcedata) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<ArrayList<String>> SourceDataFilled=new ArrayList<ArrayList<String>>();
		List<List<String>> Sprint_Definition=Arrays.asList(Arrays.asList("1/6/2017","1/26/2017","PI1_Sprint1"),
														   Arrays.asList("1/27/2017","2/16/2017","PI1_Sprint2"),
														   Arrays.asList("2/17/2017","3/9/2017","PI1_Sprint3"),
														   Arrays.asList("3/10/2017","3/23/2017","PI1_IP"),
														   Arrays.asList("3/24/2017","4/13/2017","PI2_Sprint1"),
														   Arrays.asList("4/14/2017","5/4/2017","PI2_Sprint2"),
														   Arrays.asList("5/5/2017","5/25/2017","PI2_Sprint3"),
														   Arrays.asList("5/26/2017","6/8/2017","PI2_IP"),
														   Arrays.asList("6/9/2017","6/29/2017","PI3_Sprint1"),
														   Arrays.asList("6/30/2017","7/20/2017","PI3_Sprint2"),
														   Arrays.asList("7/21/2017","8/10/2017","PI3_Sprint3"),
														   Arrays.asList("8/11/2017","8/24/2017","PI3_IP"),
														   Arrays.asList("8/25/2017","9/14/2017","PI4_Sprint1"),
														   Arrays.asList("9/15/2017","10/5/2017","PI4_Sprint2"),
														   Arrays.asList("10/6/2017","10/19/2017","PI4_Sprint3"));

		
		Calendar c_SprintBegin=new GregorianCalendar();
		Calendar c_SprintEnd=new GregorianCalendar();
		
		int i_sum=0;
		int j_sum=0;
		
		int i_rc_sum=0;
		int j_rc_sum=0;
		for(List<String> tempList:Sprint_Definition)
		{
			int i=0;int i_rc=0;
			int j=0;int j_rc=0;
			ArrayList<String> FilledItes=new ArrayList<String>();
			try
			{
				Date d_SprintBegin=sdf.parse(tempList.get(0));
				Date d_SprintEnd=sdf.parse(tempList.get(1));
				c_SprintBegin.setTime(d_SprintBegin);
				c_SprintEnd.setTime(d_SprintEnd);
				
				for(ArrayList<String> tempList1:sourcedata)
				{
					String temp="";
					temp=tempList1.get(12); //get the create date
					String temp1="";
					temp1=tempList1.get(13);//get the verified date

					Date date = sdf1.parse(temp);
					Date date1=new Date();
					if(!temp1.equals(""))
					{
						date1=sdf1.parse(temp1);
					}
					else
					{
						date1=sdf1.parse("1991-10-24 00:00:00");
					}
					Calendar CreateDate=new GregorianCalendar();
					Calendar VerifyDate=new GregorianCalendar();
					CreateDate.setTime(date);
					VerifyDate.setTime(date1);
					
					CreateDate.set(Calendar.HOUR, 0);
					CreateDate.set(Calendar.MINUTE, 0);
					CreateDate.set(Calendar.SECOND, 0);
					
					VerifyDate.set(Calendar.HOUR, 0);
					VerifyDate.set(Calendar.MINUTE, 0);
					VerifyDate.set(Calendar.SECOND, 0);
					
					c_SprintBegin.set(Calendar.HOUR, 0);
					c_SprintBegin.set(Calendar.MINUTE, 0);
					c_SprintBegin.set(Calendar.SECOND, 0);
					
					c_SprintEnd.set(Calendar.HOUR, 0);
					c_SprintEnd.set(Calendar.MINUTE, 0);
					c_SprintEnd.set(Calendar.SECOND, 0);
					
					//if((CreateDate.after(c_SprintBegin)&&CreateDate.before(c_SprintEnd)))  //Create in this week
					if((CreateDate.after(c_SprintBegin)&&CreateDate.before(c_SprintEnd))||(CreateDate.equals(c_SprintBegin))||CreateDate.equals(c_SprintEnd))  //Create in this week
					{
						i++;
						if(tempList1.get(4).equals("1"))
						{
							i_rc++;
						}
					}	
					//if(VerifyDate.after(c_SprintBegin)&&VerifyDate.before(c_SprintEnd))
					if((VerifyDate.after(c_SprintBegin)&&VerifyDate.before(c_SprintEnd))||(VerifyDate.equals(c_SprintBegin))||(VerifyDate.equals(c_SprintEnd)))
					{
						j++;
						if(tempList1.get(4).equals("1"))
						{
							j_rc++;
						}
					}
				}
				
				FilledItes.add(tempList.get(2));
				FilledItes.add(String.valueOf(i));
				i_sum+=i;
				FilledItes.add(String.valueOf(j));
				j_sum+=j;
				
				FilledItes.add(String.valueOf(i_sum));
				FilledItes.add(String.valueOf(j_sum));
				
				FilledItes.add(String.valueOf(i_rc));
				i_rc_sum+=i_rc;
				FilledItes.add(String.valueOf(j_rc));
				j_rc_sum+=j_rc;
				SourceDataFilled.add(FilledItes);
				
				FilledItes.add(String.valueOf(i_rc_sum));
				FilledItes.add(String.valueOf(j_rc_sum));
				
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));				
				Calendar Today=Calendar.getInstance();
				if(Today.before(c_SprintBegin))
				{
					break;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return SourceDataFilled;
	}
	
//Lane:Get the S5KA FY17 Anomlay Backlog_3.0 source data
	public static List<ArrayList<String>> S5KA_FY17_Anomlay_Backlog3_SourceData_Week(List<ArrayList<String>> sourcedata) 
	{
		List<ArrayList<String>> ResultData=new ArrayList<ArrayList<String>>();
		
		Calendar Today=Calendar.getInstance();
		Today.set(Calendar.HOUR, 0);
		Today.set(Calendar.MINUTE, 0);
		Today.set(Calendar.SECOND, 0);
		Calendar LastUpdateDay=new GregorianCalendar();
		SimpleDateFormat sdf2=new SimpleDateFormat("MM/dd/yyyy");
		
		int RC_ACTIVE=0;
		int IMPACT_24=0;
		int IMPACT_16=0;
				
		//Get the history data from the Excel
		jxl.Workbook readwb=null;
		try
		{
			InputStream instream = new FileInputStream("ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			Sheet readsheet=readwb.getSheet(0);
			   
	           int rsColumns = readsheet.getColumns();   
	  
	       int rsRows = readsheet.getRows(); 
	                
	       for (int i = 1; i < rsRows; i++)   	 
	        {   
	    	   ArrayList<String> InsertItem=new ArrayList<String>();
	            for (int j = 0; j < rsColumns; j++)   
	               {   
	                   Cell cell = readsheet.getCell(j, i);  
	                   if(!cell.getContents().isEmpty())
	                   {
	                	   InsertItem.add(cell.getContents());   
	                   }
	                } 
	            ResultData.add(InsertItem);
	        }  
		}catch (Exception e) {   
			  
	           e.printStackTrace();   
	  
	        } finally {   
	  
	            readwb.close();    
	       } 
		
		try
		{
			Date tempDate=sdf2.parse(ResultData.get(ResultData.size()-1).get(0));//æœ€å�Žä¸€è¡Œæœ€å�Žä¸€åˆ—
			
			LastUpdateDay.setTime(tempDate);
			LastUpdateDay.set(Calendar.HOUR, 0);
			LastUpdateDay.set(Calendar.MINUTE, 0);
			LastUpdateDay.set(Calendar.SECOND, 0);
			
			if((Today.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)&&(Today.after(LastUpdateDay)))//å½“å‰�æ—¶é—´ä¸ºå‘¨ä¸€å¹¶ä¸”å¤§äºŽä¸Šä¸€æ¬¡æ›´æ–°æ—¶é—´
			{
				ArrayList<String> InsertItem=new ArrayList<String>();
				for(ArrayList<String> tempList:sourcedata)
				{			
					if(tempList.get(1).equals("1")&&   //Found in 3.0=1
					   tempList.get(2).equals("0")&&   //Document=0
					   tempList.get(3).equals("0")&&   //Not committed=0
					   tempList.get(4).equals("1")&&   //RC=1
					   tempList.get(11).equals("1"))   //ACtive=1
					{
						RC_ACTIVE++;
					}
					
					if(tempList.get(1).equals("1")&&   //Found in 3.0=1
							   tempList.get(2).equals("0")&&   //Document=0
							   tempList.get(3).equals("0")&&   //Not committed=0
							   tempList.get(5).equals("1")&&   //FMEA=[16,24)
							   tempList.get(11).equals("1"))   //ACtive=1
					{
						IMPACT_24++;
					}
					
					if(tempList.get(1).equals("1")&&   //Found in 3.0=1
							   tempList.get(2).equals("0")&&   //Document=0
							   tempList.get(3).equals("0")&&   //Not committed=0
							   tempList.get(9).equals("1")&&   //FMEA<16
							   tempList.get(11).equals("1"))   //ACtive=1
					{
						IMPACT_16++;
					}
					
				}
				
				InsertItem.add(sdf2.format(Today.getTime()));
				InsertItem.add(String.valueOf(RC_ACTIVE));
				InsertItem.add(String.valueOf(IMPACT_24));
				InsertItem.add(String.valueOf(IMPACT_16));
				InsertItem.add(String.valueOf(15));
				InsertItem.add(String.valueOf(RC_ACTIVE+1));				
				ResultData.add(InsertItem);	
				
				//Record the data in the excel
				jxl.Workbook wb=Workbook.getWorkbook(new File("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls"));
				WritableWorkbook book=Workbook.createWorkbook(new File("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls"),wb);
				
				WritableSheet sheet=book.getSheet(0);
				int rsRows = sheet.getRows(); 
				sheet.addCell(new Label(0,rsRows,sdf2.format(Today.getTime())));
				sheet.addCell(new Label(1,rsRows,String.valueOf(RC_ACTIVE)));
				sheet.addCell(new Label(2,rsRows,String.valueOf(IMPACT_24)));
				sheet.addCell(new Label(3,rsRows,String.valueOf(IMPACT_16)));
				sheet.addCell(new Label(4,rsRows,String.valueOf(15)));
				sheet.addCell(new Label(5,rsRows,String.valueOf(RC_ACTIVE+1)));
				
				book.write();
				book.close();				
			 }				
		}
		catch (Exception e) {   
			  
	           e.printStackTrace();   
		}	
		return ResultData;		
	}
	
	public static List<ArrayList<String>> S5KA_FY17_Anomlay_Backlog3_SourceData_Sprint(List<ArrayList<String>> sourcedata) 
	{
		List<ArrayList<String>> ResultData=new ArrayList<ArrayList<String>>();
		
		Calendar Today=Calendar.getInstance();
		Calendar C_Sprint_Begin=new GregorianCalendar();
		Calendar C_Sprint_End=new GregorianCalendar();
		SimpleDateFormat sdf2=new SimpleDateFormat("MM/dd/yyyy");
		
		int RC_ACTIVE=0;
		int IMPACT_24=0;
		int IMPACT_16=0;
		
		int Dead_Row=0;
				
		//Get the history data from the Excel
		jxl.Workbook readwb=null;
		try
		{

			InputStream instream = new FileInputStream("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			//Anomaly Metrics_S5KA Sprint
			Sheet readsheet=readwb.getSheet(1);
			 
	  
	           int rsColumns = readsheet.getColumns();   
	  
  
	       int rsRows = readsheet.getRows(); 
	       	                  
	       for (int i = 1; i < rsRows; i++)   	 
	        {   
	    	   ArrayList<String> InsertItem=new ArrayList<String>();
	    	   
	    	   Cell cell_begin = readsheet.getCell(0, i);//start
	    	   Cell cell_end = readsheet.getCell(1, i);  //end
	    	   
	    	   Date d_Sprint_Begin=sdf2.parse(cell_begin.getContents());
	    	   Date d_Sprint_End=sdf2.parse(cell_end.getContents());
	    	   
	    	   C_Sprint_Begin.setTime(d_Sprint_Begin);
	    	   C_Sprint_End.setTime(d_Sprint_End);
	    	   
           		if((Calendar_Compare.Equal(Today,C_Sprint_Begin)|Calendar_Compare.After(Today, C_Sprint_Begin))
           		&&(Calendar_Compare.Equal(Today,C_Sprint_End)|Calendar_Compare.Before(Today, C_Sprint_End)))
        		{
	           		Dead_Row=i;	
	           		break;
           		}
	    	   
	            for (int j = 0; j < rsColumns; j++)   
               {   
                   Cell cell = readsheet.getCell(j, i);  
                   if(!cell.getContents().isEmpty())
                   {
                	   InsertItem.add(cell.getContents());   
                   }
                } 
	            ResultData.add(InsertItem);
	        }  
	       
	       ArrayList<String> InsertItem=new ArrayList<String>();
			for(ArrayList<String> tempList:sourcedata)
			{			
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
				   tempList.get(2).equals("0")&&   //Document=0
				   tempList.get(3).equals("0")&&   //Not committed=0
				   tempList.get(4).equals("1")&&   //RC=1
				   tempList.get(11).equals("1"))   //ACtive=1
				{
					RC_ACTIVE++;
				}
				
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
						   tempList.get(2).equals("0")&&   //Document=0
						   tempList.get(3).equals("0")&&   //Not committed=0
						   tempList.get(5).equals("1")&&   //FMEA=[16,24)
						   tempList.get(11).equals("1"))   //ACtive=1
				{
					IMPACT_24++;
				}
				
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
						   tempList.get(2).equals("0")&&   //Document=0
						   tempList.get(3).equals("0")&&   //Not committed=0
						   tempList.get(9).equals("1")&&   //FMEA<16
						   tempList.get(11).equals("1"))   //ACtive=1
				{
					IMPACT_16++;
				}
				
			}
			
			InsertItem.add(readsheet.getCell(0, Dead_Row).getContents());
			InsertItem.add(readsheet.getCell(1, Dead_Row).getContents());
			InsertItem.add(readsheet.getCell(2, Dead_Row).getContents());
			InsertItem.add(String.valueOf(RC_ACTIVE));
			InsertItem.add(String.valueOf(IMPACT_24));
			InsertItem.add(String.valueOf(IMPACT_16));
			InsertItem.add(String.valueOf(15));
			InsertItem.add(String.valueOf(RC_ACTIVE+1));
			
			ResultData.add(InsertItem);	
		}catch (Exception e) {   
			  
	           e.printStackTrace();   
	  
	        } finally {   
	  
	            readwb.close();    
	       } 
		return ResultData;		
	}
	public static List<ArrayList<String>> S5KA_FY17_Anomlay_Backlog3_SourceData_Monthly(List<ArrayList<String>> sourcedata) 
	{
		List<ArrayList<String>> ResultData=new ArrayList<ArrayList<String>>();
		
		Calendar Today=Calendar.getInstance();
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy/MM");
		Calendar Month_Selected=Calendar.getInstance();
		
		int RC_ACTIVE=0;
		int IMPACT_24=0;
		int IMPACT_16=0;
				
		int Dead_Row=0;
		//Get the history data from the Excel
		jxl.Workbook readwb=null;
		try
		{
			//æž„å»ºWorkbookå¯¹è±¡ï¼Œå�ªè¯»Workbookå¯¹è±¡
			//ç›´æŽ¥ä»Žæœ¬åœ°æ–‡ä»¶åˆ›å»ºWorkbook
			InputStream instream = new FileInputStream("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			//Anomaly Metrics_S5KA Monthly  Sheet2
			Sheet readsheet=readwb.getSheet(2);
			 
	        //èŽ·å�–Sheetè¡¨ä¸­æ‰€åŒ…å�«çš„æ€»åˆ—æ•°   	  
	           int rsColumns = readsheet.getColumns();   
	  
	           //èŽ·å�–Sheetè¡¨ä¸­æ‰€åŒ…å�«çš„æ€»è¡Œæ•°   
	       int rsRows = readsheet.getRows(); 
	       
	     //èŽ·å�–æŒ‡å®šå�•å…ƒæ ¼çš„å¯¹è±¡å¼•ç”¨   å¹¶æ›´æ–°å…ƒæ•°æ�®åˆ—è¡¨ï¼ˆæ·»åŠ åŽ†å�²æ•°æ�®ï¼‰	           
	       for (int i = 1; i < rsRows; i++)   	 
	        {   
	    	   ArrayList<String> InsertItem=new ArrayList<String>();
	    	   
	    	   Cell C_Month = readsheet.getCell(0, i);//start	    	   
	    	   Date D_Month=sdf2.parse(C_Month.getContents());	    	   
	    	   Month_Selected.setTime(D_Month);
	    	   
	    	   if(Today.get(Calendar.MONTH)==Month_Selected.get(Calendar.MONTH))
	    	   {
	    		   Dead_Row=i;
	    		   break;
	    	   }
	    	   
	            for (int j = 0; j < rsColumns; j++)   
	               {   
	                   Cell cell = readsheet.getCell(j, i);  
	                   if(!cell.getContents().isEmpty())
	                   {
	                	   InsertItem.add(cell.getContents());   
	                   }
	                } 
	            ResultData.add(InsertItem);
	        }  
	       ArrayList<String> InsertItem=new ArrayList<String>();
			for(ArrayList<String> tempList:sourcedata)
			{			
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
				   tempList.get(2).equals("0")&&   //Document=0
				   tempList.get(3).equals("0")&&   //Not committed=0
				   tempList.get(4).equals("1")&&   //RC=1
				   tempList.get(11).equals("1"))   //ACtive=1
				{
					RC_ACTIVE++;
				}
				
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
						   tempList.get(2).equals("0")&&   //Document=0
						   tempList.get(3).equals("0")&&   //Not committed=0
						   tempList.get(5).equals("1")&&   //FMEA=[16,24)
						   tempList.get(11).equals("1"))   //ACtive=1
				{
					IMPACT_24++;
				}
				
				if(tempList.get(1).equals("1")&&   //Found in 3.0=1
						   tempList.get(2).equals("0")&&   //Document=0
						   tempList.get(3).equals("0")&&   //Not committed=0
						   tempList.get(9).equals("1")&&   //FMEA<16
						   tempList.get(11).equals("1"))   //ACtive=1
				{
					IMPACT_16++;
				}
				
			}
			
			Cell cell = readsheet.getCell(0, Dead_Row);
			InsertItem.add(cell.getContents());
			InsertItem.add(String.valueOf(RC_ACTIVE));
			InsertItem.add(String.valueOf(IMPACT_24));
			InsertItem.add(String.valueOf(IMPACT_16));
			InsertItem.add(String.valueOf(15));
			InsertItem.add(String.valueOf(RC_ACTIVE+1));
			
			ResultData.add(InsertItem);	
	       
		}catch (Exception e) {   
			  
	           e.printStackTrace();   
	  
	        } finally {   
	  
	            readwb.close();    
	       } 
		return ResultData;		
	}

	public static String Set_Create_For_QA_S5KA_4(List<String> x1,List<Integer> y1,List<Integer> y2)
	{		
		Chart S5KA_One_Chart=new LineChart("S5KA Stability (all anomally)");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
		
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
	public static String Set_Create_For_QA_S5KA_5(List<String> x1,List<Integer> y1,List<Integer> y2)
	{		
		Chart S5KA_One_Chart=new LineChart("S5KA Stability (RC)");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
		
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
	public static String Set_Create_For_QA_S5KA_6(List<String> x1,List<Integer> y1,List<Integer> y2)
	{
		
		Chart S5KA_One_Chart=new LineChart("S5KA Stability (RC)");//Title
		
		S5KA_One_Chart.description="";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
		
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
			
			//the definition of the array:0-Epic Summary; 1-Finish Point; 2-Remain Point;
			List<List<String>> All_Epic=new ArrayList<>();
		    
		    try {
			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(0), null, conditionsList);		    
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
						 
						 ////Calculate all Epic Point [begin]
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
		    		for(int i=0;i<Point_of_Sprint.size();i++)
		    		{
		    			List<String> tempList5=Point_of_Sprint.get(i);
		    			Point_Plan_Remain=Point_Plan_Remain-Integer.parseInt(tempList5.get(2));
		    			Point_finish_Remain=Point_finish_Remain-Integer.parseInt(tempList5.get(3));
		    			
		    			Remain_of_Sprint.get(i).set(2, Integer.toString(Point_Plan_Remain));
		    			Remain_of_Sprint.get(i).set(3, Integer.toString(Point_finish_Remain));
		    		}  	
		    		
		    		//Print the sprint plan and finish
		    	/*	int ii=0;
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
		    		}*/
		    		
		    		
		    		List<String> x1=new ArrayList<>();
		    		List<Integer> y1=new ArrayList<>();
		    		List<Integer> y2=new ArrayList<>();
		    	//	List<Double> y3=new ArrayList<>();
		    		
		    		//P1:Draw the burndown
		    		for(List<String> item:Remain_of_Sprint)
		    		{
		    			x1.add(item.get(0));
		    			y1.add(Integer.parseInt(item.get(2)));
		    			y2.add(Integer.parseInt(item.get(3)));
		    		}
		    		
		    		String chartID1=Create_P1(x1,y1,y2);
		    		System.out.println("CCW R11 Release Burndown(IC-Nov 7):\n"+ ConstString.CHART_URL + chartID1);
		
		    		
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

		    		String chartID6=Create_P6(x1,y2,y1);
		    		System.out.println("Throughput-Velocity by Sprint:\n"+ ConstString.CHART_URL + chartID6);
		    		
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
		    		
		    		String chartID7=Create_P7(x1,y1,y2);
		    		System.out.println("Plan vs Actual:\n"+ ConstString.CHART_URL + chartID7);
		    		
		    		
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
		    		
		    		String chartID8=Create_P8(x1,y1,y2);
		    		System.out.println("All Epic:\n"+ ConstString.CHART_URL + chartID8);    		
		    		
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
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(0), null, conditionsList);		    
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
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(0), null, conditionsList);		    
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
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(0), null, conditionsList);		    
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
		//Lane Ma, Modify the demo to draw specific chart
		public static String Create_P1(List<String> x1,List<Integer> y1, List<Integer> y2)
		{						
			Chart S5KA_One_Chart=new LineChart("CCW R11 Release Burndown(IC-Nov 7)");//Title
			
			S5KA_One_Chart.description="";//description		
			S5KA_One_Chart.xTitle="Date";
			S5KA_One_Chart.yTitle="Number";
			S5KA_One_Chart.yAxisFormat="#";
			S5KA_One_Chart.tableData=new DataTable();
			S5KA_One_Chart.colorList=Arrays.asList("red","blue");
			
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Original Commitment"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Story Points Remaining"));
			
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
		
		public static String Create_P6(List<String> x1,List<Integer> y1, List<Integer> y2)
		{						
			Chart S5KA_One_Chart=new ComboChart("Throughput - Velocity by Sprint");//Title
			
			S5KA_One_Chart.description="";//description		
			S5KA_One_Chart.xTitle="Sprint";
			S5KA_One_Chart.yTitle="Number";
			S5KA_One_Chart.yAxisFormat="#";
			S5KA_One_Chart.tableData=new DataTable();
			S5KA_One_Chart.colorList=Arrays.asList("red","blue");
			
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "AVERAGE"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
			
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
		
		public static String Create_P7(List<String> x1,List<Integer> y1, List<Integer> y2)
		{						
			Chart S5KA_One_Chart=new ColumnChart("Plan vs Actual");//Title
			
			S5KA_One_Chart.description="";//description		
			S5KA_One_Chart.xTitle="Sprint";
			S5KA_One_Chart.yTitle="Number";
			S5KA_One_Chart.yAxisFormat="#";
			S5KA_One_Chart.tableData=new DataTable();
			S5KA_One_Chart.colorList=Arrays.asList("blue","yellow");

			
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Planned"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Actual"));
			
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
		
		public static String Create_P8(List<String> x1,List<Integer> y1, List<Integer> y2)
		{						
			Chart S5KA_One_Chart=new BarChart("All Epic");//Title
			
			S5KA_One_Chart.description="";//description		
			S5KA_One_Chart.xTitle="Epic";
			S5KA_One_Chart.yTitle="Number";
			S5KA_One_Chart.yAxisFormat="#";
			S5KA_One_Chart.tableData=new DataTable();
			S5KA_One_Chart.colorList=Arrays.asList("green","blue");
			
			((BarChart)S5KA_One_Chart).isStacked="true";
			((BarChart)S5KA_One_Chart).chartLeft=400;
			
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
			S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
			
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
