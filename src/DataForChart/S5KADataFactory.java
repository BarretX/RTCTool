package DataForChart;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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
import ConstVar.ConstString;
import GetAttributeDispValue.GetAttributesValue;
import Helper.Calendar_Compare;
import Helper.ColorFormater;
import Login.LoginHandler;
import Main.Program;
import QueryReference.QueryChild;
import SearchWorkItem.MulConditionQuery;
import SearchWorkItem.SearchCondition;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


	public class S5KADataFactory
	{
		private static LoginHandler handler=Program.handler;
		private static ITeamRepository repository=Program.repository;		
		
		//QA:1 
		public static ProductData Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly()
		{
			if(S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly==null)
				ChartOfQA();			
			return S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly;
		}
		//QA:2
		public static ProductData Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly()
		{
			if(S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly==null)
				ChartOfQA();			
			return S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly;
		}
		//QA:3
		public static ProductData Get_S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint()
		{
			if(S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint==null)
				ChartOfQA();			
			return S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint;
		}
		//QA:4
		public static ProductData Get_S5KA_QA_Data_Shippable_RC_anomaly_Weekly()
		{
			if(S5KA_QA_Data_Shippable_RC_anomaly_Weekly==null)
				ChartOfQA();			
			return S5KA_QA_Data_Shippable_RC_anomaly_Weekly;
		}
		//QA:5
		public static ProductData Get_S5KA_QA_Data_Shippable_RC_anomaly_monthly()
		{
			if(S5KA_QA_Data_Shippable_RC_anomaly_monthly==null)
				ChartOfQA();			
			return S5KA_QA_Data_Shippable_RC_anomaly_monthly;
		}
		//QA:6
		public static ProductData Get_S5KA_QA_Data_Shippable_RC_anomaly_Sprint()
		{
			if(S5KA_QA_Data_Shippable_RC_anomaly_Sprint==null)
				ChartOfQA();			
			return S5KA_QA_Data_Shippable_RC_anomaly_Sprint;
		}
		//QA:7
		public static ProductData Get_S5KA_QA_Data_Runrate_all_anomaly()
		{
			if(S5KA_QA_Data_Runrate_all_anomaly==null)
				ChartOfQA();			
			return S5KA_QA_Data_Runrate_all_anomaly;
		}
		//QA:8
		public static ProductData Get_S5KA_QA_Data_Runrate_RC()
		{
			if(S5KA_QA_Data_Runrate_RC==null)
				ChartOfQA();			
			return S5KA_QA_Data_Runrate_RC;
		}
		//QA:9
		public static ProductData Get_S5KA_QA_Data_Stability_all_anomaly()
		{
			if(S5KA_QA_Data_Stability_all_anomaly==null)
				ChartOfQA();			
			return S5KA_QA_Data_Stability_all_anomaly;
		}
		//QA:10
		public static ProductData Get_S5KA_QA_Data_Stability_RC()
		{
			if(S5KA_QA_Data_Stability_RC==null)
				ChartOfQA();			
			return S5KA_QA_Data_Stability_RC;
		}		
		
		private static ProductData S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly= null ;
		private static ProductData S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly= null ;
		private static ProductData S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint= null ;
		private static ProductData S5KA_QA_Data_Shippable_RC_anomaly_Weekly= null ;
		private static ProductData S5KA_QA_Data_Shippable_RC_anomaly_monthly= null;
		private static ProductData S5KA_QA_Data_Shippable_RC_anomaly_Sprint= null;
		private static ProductData S5KA_QA_Data_Runrate_all_anomaly = null;
		private static ProductData S5KA_QA_Data_Runrate_RC = null;
		private static ProductData S5KA_QA_Data_Stability_all_anomaly = null;
		private static ProductData S5KA_QA_Data_Stability_RC = null;
		
		public static void ChartOfQA()
		{	
			
			//[start]
		    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
		    List<String> projectAreaNames = new ArrayList<>();
		    int nProjectNumber=0;
		    for(int i = 0;i<iProcessAreas.size();i++)
			{
		    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
		    	projectAreaNames.add(iProcessArea.getName());
				System.out.println(iProcessArea.getName());
				if(iProcessArea.getName().equals("CVB Studio 5000 Architect - RTC SAFe"))
				{
					nProjectNumber=i;
				}
			}
		    //[end]
		    
		    // there suppose you take the first value
		    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
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
//		    needAttributeList.add("Phase Detected");//pass             10
		    needAttributeList.add("Must Be Fixed");//pass              11
		    needAttributeList.add("Creation Date");//pass              12
		    needAttributeList.add("Found in SW Version");//pass        13
		    needAttributeList.add("Found In Version");//pass           14
		    needAttributeList.add("Target Release");//pass             15
		    needAttributeList.add("Resolved By");//pass                16
//		    needAttributeList.add("Resolution");//pass                 17
		    needAttributeList.add("Feature");//pass                    18
		    needAttributeList.add("Closed Date");//pass                19
//		    needAttributeList.add("Phase Injected");//pass             20
		    needAttributeList.add("Filed Against");//pass              21
//		    needAttributeList.add("Customer First Escalation");//pass  22
		    needAttributeList.add("Verified Date");//pass              23
		    needAttributeList.add("Impacted Product");//pass           24
//		    needAttributeList.add("SAP Ticket");//pass                 25
		    	    
		    try {

			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);		    
		    		 
		    	if(resultOwner!=null)
		    	{
		    		resultOwner.setLimit(100);	
		    		List<List<String>> resultList = getAttributesValue.GetAllNeedAttribute(repository,handler.getMonitor(), query.getProjectArea(),resultOwner,needAttributeList);
		    		
		    		//show all anomaly data
		    	/*	int j=0;
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
		    		}*/
		    		
		    		//Save all data
		    		try
		    		{	        		
		        		WritableWorkbook book =Workbook.createWorkbook(new File("ExcelSource/Anomaly Metrics_S5KA.xls"));
		        		WritableSheet sheet=book.createSheet("ExcelSource/sourcedata_S5KA", 0);
		        		
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
		    		
		    		
	                //calculate the data need in next step
		    		List<ArrayList<String>> SourceDataFilled=CalculateSourceData(needAttributeList,resultList); 	
		    		
		    		//print the SourceData Filled
		    		
		    		/*for (List<String> item:SourceDataFilled)
		    		{
		    			int i = 0;
		    			for(String str : item)
		    			{
		    				System.out.print(i++ + "\t"+str+"\t");
		    			}
		    			System.out.println();
		    		}*/
		    		
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
		    		
		    		// ("S5KA FY17 Anomlay Backlog_3.0 Weekly)
		    	    Set_Create_For_QA_S5KA_1(X_Data,Y1_Data,Y2_Data);
		    	    //("S5KA Shippable State RC Anomaly Weekly")
		    	    Set_Shippable_RC_Weekly(X_Data,Y3_Data);

		    	    
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
		    	    //("S5KA FY17 Anomlay Backlog_3.0 Sprint")
		    	    Set_Create_For_QA_S5KA_Sprint(X_Data,Y1_Data,Y2_Data);
		    	    //("S5KA Shippable State RC Anomaly Sprint")
		    	    Set_Shippable_RC_Sprint(X_Data,Y3_Data);	    	    

		    	    
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
		    	   //("S5KA FY17 Anomlay Backlog_3.0 Monthly");
		    	    Set_Create_For_QA_S5KA_Monthly(X_Data,Y1_Data,Y2_Data);
		    	    //("S5KA Shippable State RC Anomaly Monthly")
		    	    Set_Shippable_RC_Monthly(X_Data,Y3_Data);
		    	    		    	    
		    	    
		    	    
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
		    		
		    	    Set_Create_For_QA_S5KA_3(X_Data_1,Y1_Data_1,Y2_Data_1); //Runrate (All Anomaly)
		    	    Set_Create_For_QA_S5KA_4(X_Data_1,Y1_Data_2,Y2_Data_2);	//Runrate (RC)
		    	    Set_Create_For_QA_S5KA_5(X_Data_1,Y1_Data_3,Y2_Data_3); //Stability (All Anomaly)   	    
		    	    Set_Create_For_QA_S5KA_6(X_Data_1,Y1_Data_4,Y2_Data_4); //Stability (RC)
		    	}
		    	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		public static void Set_Shippable_RC_Weekly(List<String> x1,List<Integer> y1)
		{
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly=new ProductData();
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.title=ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Weekly;
			
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.xTitle="Week";
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.yTitle="";
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.yAxisFormat="#";
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.tableData=new DataTable();
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77));
			
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_QA_Data_Shippable_RC_anomaly_Weekly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			
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
				S5KA_QA_Data_Shippable_RC_anomaly_Weekly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}	
		}
		
		public static void Set_Shippable_RC_Sprint(List<String> x1,List<Integer> y1)
		{	
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint=new ProductData();
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.title=ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_Sprint;
			
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.xTitle="Sprint";
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.yTitle="";
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.yAxisFormat="#";
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.tableData=new DataTable();
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77));
			
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_QA_Data_Shippable_RC_anomaly_Sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			
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
				S5KA_QA_Data_Shippable_RC_anomaly_Sprint.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}	
		}
		
		public static void Set_Shippable_RC_Monthly(List<String> x1,List<Integer> y1)
		{	
			S5KA_QA_Data_Shippable_RC_anomaly_monthly=new ProductData();
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.title=ConstString.S5KA_QA_CHART_Shippable_RC_anomaly_monthly;
			
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.xTitle="Month";
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.yTitle="Number";
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.yAxisFormat="#";
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.tableData=new DataTable();
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77),ColorFormater.RGB2String(85, 142, 213));
			
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Month"));
			S5KA_QA_Data_Shippable_RC_anomaly_monthly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			
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
				S5KA_QA_Data_Shippable_RC_anomaly_monthly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}		
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
				
		public static void Set_Create_For_QA_S5KA_1(List<String> x1,List<Integer> y1,List<Integer> y2)
		{
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly=new ProductData();
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.title=ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Weekly;
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.xTitle="Week";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.yTitle="Number";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.yAxisFormat="#";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.tableData=new DataTable();
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77),ColorFormater.RGB2String(85, 142, 213));
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Week"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
			
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
				S5KA_QA_Data_FY17_Anomlay_Backlog_Weekly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}	
		}
		
		public static void Set_Create_For_QA_S5KA_Sprint(List<String> x1,List<Integer> y1,List<Integer> y2)
		{	
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint=new ProductData();
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.title=ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Sprint;
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.xTitle="date";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.yTitle="Number";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.yAxisFormat="#";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.tableData=new DataTable();
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77),ColorFormater.RGB2String(85, 142, 213));
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
			
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
				S5KA_QA_Data_FY17_Anomlay_Backlog_Sprint.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
					
		}
		
		public static void Set_Create_For_QA_S5KA_Monthly(List<String> x1,List<Integer> y1,List<Integer> y2)
		{
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly=new ProductData();
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.title=ConstString.S5KA_QA_CHART_FY17_Anomlay_Backlog_Monthly;
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.xTitle="Month";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.yTitle="Number";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.yAxisFormat="#";
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.tableData=new DataTable();
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.colorList=Arrays.asList(ColorFormater.RGB2String(192, 80, 77),ColorFormater.RGB2String(85, 142, 213));
			
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Month"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
			S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
			
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
				S5KA_QA_Data_FY17_Anomlay_Backlog_Monthly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}	
		}

		
		public static void Set_Create_For_QA_S5KA_3(List<String> x1,List<Integer> y1,List<Integer> y2)
		{
			S5KA_QA_Data_Runrate_all_anomaly=new ProductData();
			S5KA_QA_Data_Runrate_all_anomaly.title=ConstString.S5KA_QA_CHART_Runrate_all_anomaly;
			
			S5KA_QA_Data_Runrate_all_anomaly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Runrate_all_anomaly.xTitle="Sprint";
			S5KA_QA_Data_Runrate_all_anomaly.yTitle="Number";
			S5KA_QA_Data_Runrate_all_anomaly.yAxisFormat="#";
			S5KA_QA_Data_Runrate_all_anomaly.tableData=new DataTable();
			S5KA_QA_Data_Runrate_all_anomaly.colorList=Arrays.asList(ColorFormater.RGB2String(250,192,143),ColorFormater.RGB2String(2,109,190));
			
			S5KA_QA_Data_Runrate_all_anomaly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_QA_Data_Runrate_all_anomaly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
			S5KA_QA_Data_Runrate_all_anomaly.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
			
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
				S5KA_QA_Data_Runrate_all_anomaly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
			
		}	

		public static void Set_Create_For_QA_S5KA_4(List<String> x1,List<Integer> y1,List<Integer> y2)
		{	
			S5KA_QA_Data_Stability_all_anomaly=new ProductData();
			S5KA_QA_Data_Stability_all_anomaly.title=ConstString.S5KA_QA_CHART_Stability_all_anomaly;
			
			S5KA_QA_Data_Stability_all_anomaly.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Stability_all_anomaly.xTitle="Sprint";
			S5KA_QA_Data_Stability_all_anomaly.yTitle="Number";
			S5KA_QA_Data_Stability_all_anomaly.yAxisFormat="#";
			S5KA_QA_Data_Stability_all_anomaly.tableData=new DataTable();
			S5KA_QA_Data_Stability_all_anomaly.colorList=Arrays.asList(ColorFormater.RGB2String(250,192,143),ColorFormater.RGB2String(2,109,190));
			
			S5KA_QA_Data_Stability_all_anomaly.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_QA_Data_Stability_all_anomaly.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
			S5KA_QA_Data_Stability_all_anomaly.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
			
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
				S5KA_QA_Data_Stability_all_anomaly.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
				
		}
		
		public static void Set_Create_For_QA_S5KA_5(List<String> x1,List<Integer> y1,List<Integer> y2)
		{	
			S5KA_QA_Data_Runrate_RC=new ProductData();
			S5KA_QA_Data_Runrate_RC.title=ConstString.S5KA_QA_CHART_Runrate_RC;
			
			S5KA_QA_Data_Runrate_RC.description="Goal (Customer impact16):15";//description		
			S5KA_QA_Data_Runrate_RC.xTitle="Date";
			S5KA_QA_Data_Runrate_RC.yTitle="Number";
			S5KA_QA_Data_Runrate_RC.yAxisFormat="#";
			S5KA_QA_Data_Runrate_RC.tableData=new DataTable();
			S5KA_QA_Data_Runrate_RC.colorList=Arrays.asList(ColorFormater.RGB2String(250,192,143),ColorFormater.RGB2String(2,109,190));
			
			S5KA_QA_Data_Runrate_RC.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_QA_Data_Runrate_RC.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
			S5KA_QA_Data_Runrate_RC.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
			
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
				S5KA_QA_Data_Runrate_RC.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
				
		}
		
		public static void Set_Create_For_QA_S5KA_6(List<String> x1,List<Integer> y1,List<Integer> y2)
		{
			S5KA_QA_Data_Stability_RC=new ProductData();
			S5KA_QA_Data_Stability_RC.title=ConstString.S5KA_QA_CHART_Stability_RC;
			
			S5KA_QA_Data_Stability_RC.description="";//description		
			S5KA_QA_Data_Stability_RC.xTitle="Date";
			S5KA_QA_Data_Stability_RC.yTitle="Number";
			S5KA_QA_Data_Stability_RC.yAxisFormat="#";
			S5KA_QA_Data_Stability_RC.tableData=new DataTable();
			S5KA_QA_Data_Stability_RC.colorList=Arrays.asList(ColorFormater.RGB2String(250,192,143),ColorFormater.RGB2String(2,109,190));
			
			S5KA_QA_Data_Stability_RC.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_QA_Data_Stability_RC.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "S5KA(incoming)"));
			S5KA_QA_Data_Stability_RC.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "S5KA(fixed)"));
			
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
				S5KA_QA_Data_Stability_RC.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
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
			for(List<String> tempList:Sprint_Definition) // 1-2 layer for() loop, iterate the sprint
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
					
					for(ArrayList<String> tempList1:sourcedata)  //2-1 layer for() loop, iterate the sprint
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
						
						//if create in this sprint
						if((CreateDate.after(c_SprintBegin)&&CreateDate.before(c_SprintEnd))||(CreateDate.equals(c_SprintBegin))||CreateDate.equals(c_SprintEnd))  //Create in this week
						{
							i++;  //All Anomaly
							if(tempList1.get(4).equals("1"))  //RC=1,RC_Anomaly
							{
								i_rc++;
							}
						}	
						//if verify in this sprint
						if((VerifyDate.after(c_SprintBegin)&&VerifyDate.before(c_SprintEnd))||(VerifyDate.equals(c_SprintBegin))||(VerifyDate.equals(c_SprintEnd)))
						{
							j++;  //All Anomaly
							if(tempList1.get(4).equals("1"))  //RC=1,RC_Anomaly
							{
								j_rc++;
							}
						}
					}
					
					FilledItes.add(tempList.get(2));        //index 0:Sprint
					FilledItes.add(String.valueOf(i));      //index 1:All Anomaly incoming of this sprint
					i_sum+=i;
					FilledItes.add(String.valueOf(j));      //index 2:All Anomaly fixed of this sprint
					j_sum+=j;
					
					FilledItes.add(String.valueOf(i_sum));   //index 3:all Anomaly incoming by today
					FilledItes.add(String.valueOf(j_sum));   //index 4:all anomaly fixed by today
					
					FilledItes.add(String.valueOf(i_rc));    //index 5:RC Anomaly incoming of this sprint
					i_rc_sum+=i_rc;
					FilledItes.add(String.valueOf(j_rc));    //index 6:RC Anomaly fixed of this sprint
					j_rc_sum+=j_rc;
					SourceDataFilled.add(FilledItes);
					
					FilledItes.add(String.valueOf(i_rc_sum)); //index 7:RC Anomaly incoming by today
					FilledItes.add(String.valueOf(j_rc_sum)); //index 8:RC Anomaly fixed by today
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
				Date tempDate=sdf2.parse(ResultData.get(ResultData.size()-1).get(0));
				
				LastUpdateDay.setTime(tempDate);
				LastUpdateDay.set(Calendar.HOUR, 0);
				LastUpdateDay.set(Calendar.MINUTE, 0);
				LastUpdateDay.set(Calendar.SECOND, 0);
				
				if((Today.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)&&(Calendar_Compare.After(Today, LastUpdateDay)))
				{
					ArrayList<String> InsertItem=new ArrayList<String>();
					for(ArrayList<String> tempList:sourcedata)
					{			
						if(tempList.get(1).equals("1")&&   //Found in 3.0=1
						//   tempList.get(2).equals("0")&&   //Document=0
						//   tempList.get(3).equals("0")&&   //Not committed=0
						   tempList.get(4).equals("1")&&   //RC=1
						   tempList.get(11).equals("1"))   //ACtive=1
						{
							RC_ACTIVE++;
						}
						
						if(tempList.get(1).equals("1")&&   //Found in 3.0=1
							//	   tempList.get(2).equals("0")&&   //Document=0
							//	   tempList.get(3).equals("0")&&   //Not committed=0
								   tempList.get(5).equals("1"))//&&   //FMEA=[16,24)
							//	   tempList.get(11).equals("1"))   //ACtive=1
						{
							IMPACT_24++;
						}
						
						if(tempList.get(1).equals("1")&&   //Found in 3.0=1
							//	   tempList.get(2).equals("0")&&   //Document=0
							//	   tempList.get(3).equals("0")&&   //Not committed=0
								   tempList.get(9).equals("1"))//&&   //FMEA<16
							//	   tempList.get(11).equals("1"))   //ACtive=1
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
					jxl.Workbook wb=Workbook.getWorkbook(new File("ExcelSource/Source Data.xls"));
					WritableWorkbook book=Workbook.createWorkbook(new File("ExcelSource/Source Data.xls"),wb);
					
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

				InputStream instream = new FileInputStream("ExcelSource/Source Data.xls");
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
				InputStream instream = new FileInputStream("ExcelSource/Source Data.xls");
				readwb=Workbook.getWorkbook(instream);
				
				//Anomaly Metrics_S5KA Monthly  Sheet2
				Sheet readsheet=readwb.getSheet(2);
				 
		        //èŽ·å�–Sheetè¡¨ä¸­æ‰€åŒ…å�«çš„æ€»åˆ—æ•°   	  
		           int rsColumns = readsheet.getColumns();   
		  
		           //èŽ·å�–Sheetè¡¨ä¸­æ‰€åŒ…å�«çš„æ€»è¡Œæ•°   
		       int rsRows = readsheet.getRows(); 
		       
		     //èŽ·å�–æŒ‡å®šå�•å…ƒæ ¼çš„å¯¹è±¡å¼•ç�?¨   å¹¶æ›´æ–°å…ƒæ•°æ�®åˆ—è¡¨ï¼ˆæ·»åŠ åŽ†å�²æ•°æ�®ï¼‰	           
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

	//Lane:
		
		public static ProductData Get_S5KA_PM_Data_Weekly_Trend()
		{
			if(S5KA_PM_Data_Weekly_Trend==null)
			{
				ChartOfPM();
			}
			
			return S5KA_PM_Data_Weekly_Trend;
		}
		
		public static ProductData Get_S5KA_PM_Data_Trend_Epic()
		{
			//[start]
		    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
		    List<String> projectAreaNames = new ArrayList<>();
		    int nProjectNumber=0;
		    for(int i = 0;i<iProcessAreas.size();i++)
			{
		    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
		    	projectAreaNames.add(iProcessArea.getName());
		    	
				if(iProcessArea.getName().equals("CVB Studio 5000 Architect - RTC SAFe"))
				{
					nProjectNumber=i;
				}
				
				System.out.println(iProcessArea.getName());
			}
		    //[end]
		    
		    // there suppose you take the first value
		    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
			List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
			for(String str: allDispNames)
			{
				System.out.println(str);
			}
			
		    List<SearchCondition> conditionsList = new ArrayList<>(); 
		    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
		    
		    List<String> needAttributeList = new ArrayList<>();
		    needAttributeList.add("Id");//pass                       0
		    needAttributeList.add("Planned For");//pass              1 
		    needAttributeList.add("Story Points (numeric)");//pass   2
		    needAttributeList.add("Status");//pass                   3
		    needAttributeList.add("Resolution Date");//pass          4
		    
		    try {
			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);		    
		    	if(resultOwner!=null)
		    	{
		    		resultOwner.setLimit(1000);
		    			
		    		IWorkItem workItem = null;
		    		IResolvedResult<IWorkItem> resolved =null;
		    		
		//			while(resultOwner.hasNext(handler.getMonitor()))
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
						 
						 for(List<String> tempList4:resultList)   //the story status of the sprint
						 {
							 int i=0;
							 for(String str : tempList4)
				    			{
				    				System.out.print(i++ + "\t"+str+"\t");
				    			}
							 System.out.println("");
						 }
						 
						 ////Calculate the week section
						 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						 List<Date> Epic_Resolution_Date=new ArrayList<Date>();
						 
						 for(List<String> tempList4:resultList)   //the story status of the sprint
						 {
							if(tempList4.get(4).equals(""))
								continue;
							
							 Date temp=sdf.parse(tempList4.get(4));
							Epic_Resolution_Date.add(temp);
						 }
						 
						 Date Date_Min=new Date();
						 Date Date_Max=new Date();
						 Date_Max=Epic_Resolution_Date.get(0);
						 
						 for(Date temp:Epic_Resolution_Date)
						 {
							 if(temp.before(Date_Min))
							 {
								 Date_Min=temp;
							 }
							 if(temp.after(Date_Max))
							 {
								 Date_Max=temp;
							 }
						 }
						 
						 List<Date> Week_Trend=new ArrayList<Date>();
						 Week_Trend.add(Date_Min);
						 for(Date temp=Date_Min;temp.before(Date_Max);)
						 {
							 Calendar calendar=Calendar.getInstance();
							 calendar.setTime(temp);
							 calendar.add(Calendar.WEEK_OF_MONTH,1);
							 temp=calendar.getTime();
							 Week_Trend.add(temp);
						 }
						 
						 List<List<String>> Trend_result=new ArrayList<>();
						 for(int i=0;i<Week_Trend.size()-2;i++)
						 {
							 List<String> Haha=new ArrayList<>();
							 int StoryPoint=0;
							 for(List<String> tempList4:resultList)
							 {
								 if(tempList4.get(4).equals(""))
								 continue;
								 
								 Date DateOfStory=new Date();
								 DateOfStory=sdf.parse(tempList4.get(4));
								 
								 if(DateOfStory.after(Week_Trend.get(i))&&DateOfStory.before(Week_Trend.get(i+1)))
								 {
									 StoryPoint+=Integer.parseInt(tempList4.get(2));
								 }
							 }
							 Haha.add(sdf.format(Week_Trend.get(i)));
							 Haha.add(Integer.toString(StoryPoint));
							 Trend_result.add(Haha);
						 }				 
						 
			    		List<String> x1=new ArrayList<>();
			    		List<Integer> y1=new ArrayList<>();
			    		
			    		for(List<String> temp:Trend_result)
			    		{
			    			x1.add(temp.get(0));
			    			y1.add(Integer.parseInt(temp.get(1)));		    			
			    		}
			    		
			    		
			    		S5KA_PM_Data_Trend_Epic=new ProductData();
			    		S5KA_PM_Data_Trend_Epic.title="Trend by Epic";
			    		
			    		S5KA_PM_Data_Trend_Epic.description=workItem.getHTMLSummary().toString();//description		
			    		S5KA_PM_Data_Trend_Epic.xTitle="Date";
			    		S5KA_PM_Data_Trend_Epic.yTitle="Number";
			    		S5KA_PM_Data_Trend_Epic.yAxisFormat="#";
			    		S5KA_PM_Data_Trend_Epic.tableData=new DataTable();
			    		S5KA_PM_Data_Trend_Epic.colorList=Arrays.asList(ColorFormater.RGB2String(158,158,158));
			    		
			    		S5KA_PM_Data_Trend_Epic.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			    		S5KA_PM_Data_Trend_Epic.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Original Commitment"));
			    		
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
			    			S5KA_PM_Data_Trend_Epic.tableData.addRows(rows);
			    		}catch(Exception e)
			    		{
			    			System.out.println("Import Exception!");
			    		}
					}	
		    	}
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e);
		    }
			return S5KA_PM_Data_Trend_Epic;
		}
		public static ProductData Get_S5KA_PM_Data_Trend_Team()
		{		
			//[start]
		    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
		    List<String> projectAreaNames = new ArrayList<>();
		    int nProjectNumber=0;
		    for(int i = 0;i<iProcessAreas.size();i++)
			{
		    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
		    	projectAreaNames.add(iProcessArea.getName());
		    	
				if(iProcessArea.getName().equals("CVB Studio 5000 Architect - RTC SAFe"))
				{
					nProjectNumber=i;
				}
				
				System.out.println(iProcessArea.getName());
			}
		    //[end]
		    
		    // there suppose you take the first value
		    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
			List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
			for(String str: allDispNames)
			{
				System.out.println(str);
			}
			
			String SpeicTeam="CCW Localization";
			
		    List<SearchCondition> conditionsList = new ArrayList<>(); 
		    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
		    
		    List<String> needAttributeList = new ArrayList<>();
		    needAttributeList.add("Id");//pass                       0
		    needAttributeList.add("Planned For");//pass              1 
		    needAttributeList.add("Story Points (numeric)");//pass   2
		    needAttributeList.add("Status");//pass                   3
		    needAttributeList.add("Resolution Date");//pass          4
		    
		    ////Calculate the week section
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 
			 List<Date> Week_Trend=new ArrayList<Date>();
			 List<List<String>> Week_Result=new ArrayList<>();
			 Date Date_Max=new Date();
			 Date Date_Min;
			try {
					Date_Min = sdf.parse("2017-01-01");	 
		
					Week_Trend.add(Date_Min);
					
					 for(Date temp=Date_Min;temp.before(Date_Max);)
					 {
						 Calendar calendar=Calendar.getInstance();
						 calendar.setTime(temp);
						 calendar.add(Calendar.WEEK_OF_MONTH,1);
						 temp=calendar.getTime();
						 Week_Trend.add(temp);
					 }
				} 
			catch (ParseException e1) 
				{
					e1.printStackTrace();
				}
			
			//Create the new record list
			for(Date item:Week_Trend)
			{
				List<String> temp=new ArrayList<>();
				temp.add(sdf.format(item));
				temp.add("0");
				Week_Result.add(temp);			
			}
			
		    try {
				    MulConditionQuery query=new MulConditionQuery();
			    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(nProjectNumber), null, conditionsList);		    
			    	if(resultOwner!=null)
			    	{
			    		resultOwner.setLimit(1000);
			    			
			    		IWorkItem workItem = null;
			    		IResolvedResult<IWorkItem> resolved =null;
			    		
			    		
						while(resultOwner.hasNext(handler.getMonitor()))//iterate the father item
						{
					
							resolved = resultOwner.next(handler.getMonitor());
							 workItem = (IWorkItem)resolved.getItem();
							 
							 //Print the Father's ID
							 System.out.println("Epic: " + workItem.getId()+"   "+workItem.getWorkItemType());					 
							 
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
							 List<String> TeamResultList=getAttributesValue.GetTeamAreaList(repository, handler.getMonitor(),query.getProjectArea(), ChildList);
							 for(List<String> tempList4:resultList)   //the story status of the sprint
							 {
								 int i=0;
								 for(String str : tempList4)
					    			{
					    				System.out.print(i++ + "\t"+str+"\t");
					    			}
								 System.out.println("");
							 }
							 
							 for(int i=0;i<Week_Trend.size()-1;i++)
							 {
								 int StoryPoint=0;
								 for(int j=0;j<resultList.size();j++)
								 {
									 if(resultList.get(j).get(4).equals(""))
									 continue;
									 
									 Date DateOfStory=new Date();
									 DateOfStory=sdf.parse(resultList.get(j).get(4));
									 
									 if(DateOfStory.after(Week_Trend.get(i))&&DateOfStory.before(Week_Trend.get(i+1))&&TeamResultList.get(j).equals(SpeicTeam))
									// if(DateOfStory.after(Week_Trend.get(i))&&DateOfStory.before(Week_Trend.get(i+1)))
									 {
										 if(resultList.get(j).get(2).equals(""))
											 continue;
										 StoryPoint+=Integer.parseInt(resultList.get(j).get(2));
									 }
								 }
								 String NumberString=Integer.toString(StoryPoint+Integer.parseInt(Week_Result.get(i).get(1)));
								 Week_Result.get(i).set(1, NumberString);
							 }				 
						}
			    	}
			    	
		    		List<String> x1=new ArrayList<>();
		    		List<Integer> y1=new ArrayList<>();
		    		
		    		for(List<String> temp:Week_Result)
		    		{
		    			x1.add(temp.get(0));
		    			y1.add(Integer.parseInt(temp.get(1)));		    			
		    		}
			    		
			    		
			    		S5KA_PM_Data_Trend_Team=new ProductData();
			    		S5KA_PM_Data_Trend_Team.title="Trend by Team";
			    		
			    		S5KA_PM_Data_Trend_Team.description=SpeicTeam;//description		
			    		S5KA_PM_Data_Trend_Team.xTitle="Date";
			    		S5KA_PM_Data_Trend_Team.yTitle="Number";
			    		S5KA_PM_Data_Trend_Team.yAxisFormat="#";
			    		S5KA_PM_Data_Trend_Team.tableData=new DataTable();
			    		S5KA_PM_Data_Trend_Team.colorList=Arrays.asList(ColorFormater.RGB2String(158,158,158));
			    		
			    		S5KA_PM_Data_Trend_Team.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			    		S5KA_PM_Data_Trend_Team.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Original Commitment"));
			    		
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
			    			S5KA_PM_Data_Trend_Team.tableData.addRows(rows);
			    		}
			    		catch(Exception e)
			    		{
			    			System.out.println("Import Exception!");
			    		}
		    }
		    catch(Exception e)
		    {
		    	System.out.println(e);
		    }
			return S5KA_PM_Data_Trend_Team;
		}
		public static ProductData Get_S5KA_PM_Data_ThroughputVelocity_sprint()
		{
			if(S5KA_PM_Data_ThroughputVelocity_sprint==null)
			{
				ChartOfPM();
			}
			
			return S5KA_PM_Data_ThroughputVelocity_sprint;
		}
		public static ProductData Get_S5KA_PM_Data_Plan_Actual_Sprint()
		{
			if(S5KA_PM_Data_Plan_Actual_Sprint==null)
			{
				ChartOfPM();
			}
			
			return S5KA_PM_Data_Plan_Actual_Sprint;
		}
		public static ProductData Get_S5KA_PM_Data_Feature_Progress()
		{
			if(S5KA_PM_Data_Feature_Progress==null)
			{
				ChartOfPM();
			}
			
			return S5KA_PM_Data_Feature_Progress;
		}
		
		public static ProductData S5KA_PM_Data_Weekly_Trend=null;
		public static ProductData S5KA_PM_Data_Trend_Epic=null;
		public static ProductData S5KA_PM_Data_Trend_Team=null;
		public static ProductData S5KA_PM_Data_ThroughputVelocity_sprint=null;
		public static ProductData S5KA_PM_Data_Plan_Actual_Sprint=null;
		public static ProductData S5KA_PM_Data_Feature_Progress=null;
		
		public static void ChartOfPM()
		{	
			
			//[start]
		    List<?> iProcessAreas = handler.GetAllProcessArea(repository, handler.getMonitor());
		    List<String> projectAreaNames = new ArrayList<>();
		    int nProjectNumber=0;
		    for(int i = 0;i<iProcessAreas.size();i++)
			{
		    	IProcessArea iProcessArea = (IProcessArea)iProcessAreas.get(i);
		    	projectAreaNames.add(iProcessArea.getName());
				System.out.println(iProcessArea.getName());
				if(iProcessArea.getName().equals("CVB Studio 5000 Architect - RTC SAFe"))
				{
					nProjectNumber=i;
				}
			}
		    //[end]
		    
		    // there suppose you take the first value
		    GetAttributesValue getAttributesValue = new GetAttributesValue(repository,handler.getMonitor(), (IProjectArea)iProcessAreas.get(nProjectNumber));
			List<String> allDispNames = getAttributesValue.GetAllAttributeDispName();
			for(String str: allDispNames)
			{
				System.out.println(str);
			}
			
		    List<SearchCondition> conditionsList = new ArrayList<>(); 
		    conditionsList.add(new SearchCondition(IWorkItem.TYPE_PROPERTY, "com.ibm.team.workitem.workItemType.programEpic", AttributeOperation.EQUALS));
		    
		    Calculate_BurnDown	( repository, handler,projectAreaNames,getAttributesValue,conditionsList,nProjectNumber);//1,2,6,7,8
		}
		
		//Calculate Agile Burndown of S5KA 1,2,6,7,8
		public static void Calculate_BurnDown(ITeamRepository repository,LoginHandler handler,List<String> projectAreaNames,GetAttributesValue getAttributesValue,List<SearchCondition> conditionsList,int ProjecrtArea) 
		{
		    List<String> needAttributeList = new ArrayList<>();
		    needAttributeList.add("Id");//pass                       0
		    needAttributeList.add("Planned For");//pass              1 
		    needAttributeList.add("Story Points (numeric)");//pass   2
		    needAttributeList.add("Status");//pass                   3
		    
		    //the definition of the array:0-Sprint 1-date 2-planed 3-remained
			List<List<String>> Point_of_Sprint=Arrays.asList(Arrays.asList("PI1_Sprint1","1/6/2017","0","0"),
														   Arrays.asList("PI1_Sprint2","1/26/2017","0","0"),
														   Arrays.asList("PI1_Sprint3","2/17/2017","0","0"),
														   Arrays.asList("PI1_IP","3/10/2017","0","0"),
														   Arrays.asList("PI2_Sprint1","3/24/2017","0","0"),
														   Arrays.asList("PI2_Sprint2","4/14/2017","0","0"),
														   Arrays.asList("PI2_Sprint3","5/5/2017","0","0"),
														   Arrays.asList("PI2_IP","5/26/2017","0","0"),
														   Arrays.asList("PI3_Spring1","6/9/2017","0","0"),
														   Arrays.asList("PI3_Spring2","6/30/2017","0","0"),
														   Arrays.asList("PI3_Spring3","7/21/2017","0","0"),
														   Arrays.asList("PI3_IP","8/11/2017","0","0"),
														   Arrays.asList("PI4_Spring1","8/25/2017","0","0"),
														   Arrays.asList("PI4_Spring2","9/15/2017","0","0"),
														   Arrays.asList("PI4_Spring3","10/6/2017","0","0"));
			
			//the definition of the array:0-Epic Summary; 1-Finish Point; 2-Remain Point;
			List<List<String>> All_Epic=new ArrayList<>();
		    
		    try {
			    MulConditionQuery query=new MulConditionQuery();
		    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(ProjecrtArea), null, conditionsList);		    
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
		    		
		    		
		    		List<List<String>> Remain_of_Sprint=Arrays.asList(Arrays.asList("PI1_Sprint1","1/6/2017","0","0"),
																   Arrays.asList("PI1_Sprint2","1/26/2017","0","0"),
																   Arrays.asList("PI1_Sprint3","2/17/2017","0","0"),
																   Arrays.asList("PI1_IP","3/10/2017","0","0"),
																   Arrays.asList("PI2_Sprint1","3/24/2017","0","0"),
																   Arrays.asList("PI2_Sprint2","4/14/2017","0","0"),
																   Arrays.asList("PI2_Sprint3","5/5/2017","0","0"),
																   Arrays.asList("PI2_IP","5/26/2017","0","0"),
																   Arrays.asList("PI3_Spring1","6/9/2017","0","0"),
																   Arrays.asList("PI3_Spring2","6/30/2017","0","0"),
																   Arrays.asList("PI3_Spring3","7/21/2017","0","0"),
																   Arrays.asList("PI3_IP","8/11/2017","0","0"),
																   Arrays.asList("PI4_Spring1","8/25/2017","0","0"),
																   Arrays.asList("PI4_Spring2","9/15/2017","0","0"),
																   Arrays.asList("PI4_Spring3","10/6/2017","0","0"));
		    		
		    		List<List<String>> BurnUp_of_Sprint=Arrays.asList(Arrays.asList("PI1_Sprint1","1/6/2017","0","0"),
																   Arrays.asList("PI1_Sprint2","1/26/2017","0","0"),
																   Arrays.asList("PI1_Sprint3","2/17/2017","0","0"),
																   Arrays.asList("PI1_IP","3/10/2017","0","0"),
																   Arrays.asList("PI2_Sprint1","3/24/2017","0","0"),
																   Arrays.asList("PI2_Sprint2","4/14/2017","0","0"),
																   Arrays.asList("PI2_Sprint3","5/5/2017","0","0"),
																   Arrays.asList("PI2_IP","5/26/2017","0","0"),
																   Arrays.asList("PI3_Spring1","6/9/2017","0","0"),
																   Arrays.asList("PI3_Spring2","6/30/2017","0","0"),
																   Arrays.asList("PI3_Spring3","7/21/2017","0","0"),
																   Arrays.asList("PI3_IP","8/11/2017","0","0"),
																   Arrays.asList("PI4_Spring1","8/25/2017","0","0"),
																   Arrays.asList("PI4_Spring2","9/15/2017","0","0"),
																   Arrays.asList("PI4_Spring3","10/6/2017","0","0"));
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
		
		//Lane Ma, Modify the demo to draw specific chart
		public static void Create_P1(List<String> x1,List<Integer> y1, List<Integer> y2)
		{	
			S5KA_PM_Data_Weekly_Trend=new ProductData();
			S5KA_PM_Data_Weekly_Trend.title="S5KA Release BurnUp(IC-Nov 7)";
			
			S5KA_PM_Data_Weekly_Trend.description="";//description		
			S5KA_PM_Data_Weekly_Trend.xTitle="Date";
			S5KA_PM_Data_Weekly_Trend.yTitle="Number";
			S5KA_PM_Data_Weekly_Trend.yAxisFormat="#";
			S5KA_PM_Data_Weekly_Trend.tableData=new DataTable();
			S5KA_PM_Data_Weekly_Trend.colorList=Arrays.asList(ColorFormater.RGB2String(130,175,94),ColorFormater.RGB2String(135,199,255));
			
			S5KA_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Original Commitment"));
			S5KA_PM_Data_Weekly_Trend.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Story Points Finish"));
			
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
				S5KA_PM_Data_Weekly_Trend.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
		}
		
		public static String Create_P2(List<String> x1,List<Integer> y1)
		{						
			Chart S5KA_One_Chart=new ColumnChart("S5KA Team Velocity by Sprint");//Title
			
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
			S5KA_PM_Data_ThroughputVelocity_sprint=new ProductData();
			S5KA_PM_Data_ThroughputVelocity_sprint.title="Throughput - Velocity by Sprint";
			
			S5KA_PM_Data_ThroughputVelocity_sprint.description="";//description		
			S5KA_PM_Data_ThroughputVelocity_sprint.xTitle="Sprint";
			S5KA_PM_Data_ThroughputVelocity_sprint.yTitle="Number";
			S5KA_PM_Data_ThroughputVelocity_sprint.yAxisFormat="#";
			S5KA_PM_Data_ThroughputVelocity_sprint.tableData=new DataTable();
			S5KA_PM_Data_ThroughputVelocity_sprint.colorList=Arrays.asList(ColorFormater.RGB2String(238,118,37),ColorFormater.RGB2String(91,155,213));
			
			S5KA_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Sprint"));
			S5KA_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "AVERAGE"));
			S5KA_PM_Data_ThroughputVelocity_sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
			
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
				S5KA_PM_Data_ThroughputVelocity_sprint.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
		}
		
		public static void Create_P7(List<String> x1,List<Integer> y1, List<Integer> y2)
		{	
			S5KA_PM_Data_Plan_Actual_Sprint=new ProductData();
			S5KA_PM_Data_Plan_Actual_Sprint.title="Plan vs Actual";
			
			S5KA_PM_Data_Plan_Actual_Sprint.description="";//description		
			S5KA_PM_Data_Plan_Actual_Sprint.xTitle="Sprint";
			S5KA_PM_Data_Plan_Actual_Sprint.yTitle="Number";
			S5KA_PM_Data_Plan_Actual_Sprint.yAxisFormat="#";
			S5KA_PM_Data_Plan_Actual_Sprint.tableData=new DataTable();
			S5KA_PM_Data_Plan_Actual_Sprint.colorList=Arrays.asList(ColorFormater.RGB2String(91,155,213),ColorFormater.RGB2String(237,125,49));

			
			S5KA_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
			S5KA_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Planned"));
			S5KA_PM_Data_Plan_Actual_Sprint.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Actual"));
			
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
				S5KA_PM_Data_Plan_Actual_Sprint.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
		}
		
		public static void Create_P8(List<String> x1,List<Integer> y1, List<Integer> y2)
		{	
			S5KA_PM_Data_Feature_Progress=new ProductData();
			S5KA_PM_Data_Feature_Progress.title="all epic";
			S5KA_PM_Data_Feature_Progress.description="";//description		
			S5KA_PM_Data_Feature_Progress.xTitle="Epic";
			S5KA_PM_Data_Feature_Progress.yTitle="Number";
			S5KA_PM_Data_Feature_Progress.yAxisFormat="#";
			S5KA_PM_Data_Feature_Progress.tableData=new DataTable();
			S5KA_PM_Data_Feature_Progress.colorList=Arrays.asList(ColorFormater.RGB2String(112,173,71),ColorFormater.RGB2String(68,114,196));
			
			S5KA_PM_Data_Feature_Progress.isStacked="true";
			S5KA_PM_Data_Feature_Progress.chartLeft=400;
			
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
			
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
				S5KA_PM_Data_Feature_Progress.tableData.addRows(rows);
			}catch(Exception e)
			{
				System.out.println("Import Exception!");
			}
		}
		
		public static void Create_P9(List<String> x1,List<Integer> y1, List<Integer> y2)
		{	
			S5KA_PM_Data_Feature_Progress=new ProductData();
			S5KA_PM_Data_Feature_Progress.title="Trend by Team";
			S5KA_PM_Data_Feature_Progress.description="";//description		
			S5KA_PM_Data_Feature_Progress.xTitle="Date";
			S5KA_PM_Data_Feature_Progress.yTitle="Number";
			S5KA_PM_Data_Feature_Progress.yAxisFormat="#";
			S5KA_PM_Data_Feature_Progress.tableData=new DataTable();
			S5KA_PM_Data_Feature_Progress.colorList=Arrays.asList("gray","blue");
			
			S5KA_PM_Data_Feature_Progress.isStacked="true";
			S5KA_PM_Data_Feature_Progress.chartLeft=400;
			
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Epic"));
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "Completed"));
			S5KA_PM_Data_Feature_Progress.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Remain"));
			
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
				//	S5KA_PM_Data_Trend_Team.tableData.addRows(rows);
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
