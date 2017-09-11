package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormatSymbols;
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
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import Charts.Chart;
import Charts.ColumnChart;
import Charts.LineChart;

public class Main {
	private static String front="http://apcndaekbhost:8080";
	//private static String front="http://10.108.178.43:9000";
	
	public static void main(String[] args) 
	{
		String repositoryURI ="https://ccmclm.rockwellautomation.com/ccm";
		//String userId = "JMa7";
	    //String password = "WelcomeLaneMa00";
	    
	    String userId = "yzhai";
	    String password = "ZYT1017rockwell";
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
	    //conditionsList.add(new SearchCondition(IWorkItem.ID_PROPERTY,"33821",AttributeOperation.EQUALS));
		
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
	    	IQueryResult<IResolvedResult<IWorkItem>> resultOwner = query.queryByCondition(repository, handler.getMonitor(), projectAreaNames.get(1), null, conditionsList);		    
	    		 
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
	        		//打开一个文件的副本，并且指定数据写回到源文件
	        		WritableWorkbook book =Workbook.createWorkbook(new File("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Anomaly Metrics_S5KA.xls"));
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
	    		
	    	    System.out.println("S5KA FY17 Anomlay Backlog_3.0 Weekly：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_1(X_Data,Y1_Data,Y2_Data));
	    	    System.out.println("Shippable State_S5KA RC anomaly Weekly：\n"+front+"/#!/charts/"+ Set_Shippable_RC_Weekly(X_Data,Y3_Data));
	    	    
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
	    	    System.out.println("S5KA FY17 Anomlay Backlog_3.0 Sprint：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_Sprint(X_Data,Y1_Data,Y2_Data));
	    	    System.out.println("Shippable State_S5KA RC anomaly Sprint：\n"+front+"/#!/charts/"+ Set_Shippable_RC_Sprint(X_Data,Y3_Data));
	    	    
	    	   
	    	    
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
	    	    System.out.println("S5KA FY17 Anomlay Backlog_3.0 Monthly：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_Monthly(X_Data,Y1_Data,Y2_Data));
	    	    System.out.println("Shippable State_S5KA RC anomaly Monthly：\n"+front+"/#!/charts/"+ Set_Shippable_RC_Monthly(X_Data,Y3_Data));
	    	    
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
	    		
	    	    System.out.println("S5KA Runrate(all anomaly)：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_3(X_Data_1,Y1_Data_1,Y2_Data_1));
	    	    System.out.println("S5KA Stability(all anomaly)：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_4(X_Data_1,Y1_Data_2,Y2_Data_2));
	    	    System.out.println("S5KA Runrate(RC)：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_5(X_Data_1,Y1_Data_3,Y2_Data_3));
	    	    System.out.println("S5KA Stability(RC)：\n"+front+"/#!/charts/"+ Set_Create_For_QA_S5KA_6(X_Data_1,Y1_Data_4,Y2_Data_4));
	    	}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	    handler.logoff();
	}

	public static String Set_Shippable_RC_Weekly(List<String> x_data,List<Integer> y1_data)
	{
		String title="Shippable State_S5KA RC anomlay Weekly";
		String description="";
		String x_name="Week";
		String y1_name="";
		
		return Create_Shippable_RC_Weekly( title, description, x_name, y1_name, x_data, y1_data);		
	}
	
	public static String Set_Shippable_RC_Sprint(List<String> x_data,List<Integer> y1_data)
	{
		String title="Shippable State_S5KA RC anomlay Sprint";
		String description="";
		String x_name="print";
		String y1_name="";
		
		return Create_Shippable_RC_Weekly( title, description, x_name, y1_name, x_data, y1_data);		
	}
	
	public static String Set_Shippable_RC_Monthly(List<String> x_data,List<Integer> y1_data)
	{
		String title="Shippable State_S5KA RC anomlay Monthly";
		String description="";
		String x_name="Month";
		String y1_name="";
		
		return Create_Shippable_RC_Weekly( title, description, x_name, y1_name, x_data, y1_data);		
	}
	public static String Create_Shippable_RC_Weekly(
			String title,
			String description,
			String x_name,
			String y1_name,
			List<String> x_data,
			List<Integer> y1_data)
	{
		Chart S5KA_One_Chart=new ColumnChart(title);
		S5KA_One_Chart.description=description;
		S5KA_One_Chart.tableData=new DataTable();
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, x_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.NUMBER, y1_name));
		
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

	public static String Set_Create_For_QA_S5KA_2(List<String> x_data,List<Integer> y1_data)
	{
		String title="S5KA_incoming";
		String description="S5KA auto in each week";
		String x_name="Time";
		String y1_name="S5KA(incoming)";
		
		return Create_Chart_For_QA_S5KA_2( title, description, x_name, y1_name, x_data, y1_data);		
	}
	public static String Create_Chart_For_QA_S5KA_2(
			String title,
			String description,
			String x_name,
			String y1_name,
			List<String> x_data,
			List<Integer> y1_data)
	{
		Chart S5KA_One_Chart=new ColumnChart(title);
		S5KA_One_Chart.description=description;
		S5KA_One_Chart.tableData=new DataTable();
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, x_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.NUMBER, y1_name));
		
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
	
	public static String Set_Create_For_QA_S5KA_1(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA FY17 Anomlay Backlog_3.0 Weekly";
		String description="";
		String x_name="Week";
		String y1_name="RC";
		String y2_name="Customer impact[16,24)";		
		
		return Create_Chart_For_QA_S5KA_1( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	
	public static String Set_Create_For_QA_S5KA_Sprint(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA FY17 Anomlay Backlog_3.0 Sprint";
		String description="";
		String x_name="Sprint";
		String y1_name="RC";
		String y2_name="Customer impact[16,24)";		
		
		return Create_Chart_For_QA_S5KA_1( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	
	public static String Set_Create_For_QA_S5KA_Monthly(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA FY17 Anomlay Backlog_3.0 Monthly";
		String description="";
		String x_name="Month";
		String y1_name="RC";
		String y2_name="Customer impact[16,24)";		
		
		return Create_Chart_For_QA_S5KA_1( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	
	public static String Create_Chart_For_QA_S5KA_1(
			String title,
			String description,
			String x_name,
			String y1_name,
			String y2_name,
			List<String> x_data,
			List<Integer> y1_data,
			List<Integer> y2_data)
	{
		Chart S5KA_One_Chart=new ColumnChart(title);
		S5KA_One_Chart.description=description;
		S5KA_One_Chart.tableData=new DataTable();
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, x_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.NUMBER, y1_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.NUMBER, y2_name));
		
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
	
	public static String Set_Create_For_QA_S5KA_3(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA Runrate (all anomally)";
		String description="";
		String x_name="Sprint";
		String y1_name="S5KA(incoming)";
		String y2_name="S5KA(fixed)";		
		
		return Create_Chart_For_QA_S5KA_3( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	public static String Create_Chart_For_QA_S5KA_3(
			String title,
			String description,
			String x_name,
			String y1_name,
			String y2_name,
			List<String> x_data,
			List<Integer> y1_data,
			List<Integer> y2_data)
	{
		Chart S5KA_One_Chart=new LineChart(title);
		S5KA_One_Chart.description=description;
		S5KA_One_Chart.tableData=new DataTable();
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, x_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.NUMBER, y1_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.NUMBER, y2_name));
		
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
			//构建Workbook对象，只读Workbook对象
			//直接从本地文件创建Workbook
			InputStream instream = new FileInputStream("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			//Sheet的下标是从0开始
			//获取第一张Sheet表
			Sheet readsheet=readwb.getSheet(0);
			 
	        //获取Sheet表中所包含的总列数   	  
	           int rsColumns = readsheet.getColumns();   
	  
	           //获取Sheet表中所包含的总行数   
	       int rsRows = readsheet.getRows(); 
	       
	     //获取指定单元格的对象引用   并更新元数据列表（添加历史数据）	           
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
		
		//获取最后的更新时间
		try
		{
			Date tempDate=sdf2.parse(ResultData.get(ResultData.size()-1).get(0));//最后一行最后一列
			
			LastUpdateDay.setTime(tempDate);
			LastUpdateDay.set(Calendar.HOUR, 0);
			LastUpdateDay.set(Calendar.MINUTE, 0);
			LastUpdateDay.set(Calendar.SECOND, 0);
			
			if((Today.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)&&(Today.after(LastUpdateDay)))//当前时间为周一并且大于上一次更新时间
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
			//构建Workbook对象，只读Workbook对象
			//直接从本地文件创建Workbook
			InputStream instream = new FileInputStream("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			//Sheet的下标是从0开始
			//Anomaly Metrics_S5KA Sprint
			Sheet readsheet=readwb.getSheet(1);
			 
	        //获取Sheet表中所包含的总列数   	  
	           int rsColumns = readsheet.getColumns();   
	  
	           //获取Sheet表中所包含的总行数   
	       int rsRows = readsheet.getRows(); 
	       	       
	     //获取指定单元格的对象引用   并更新元数据列表（添加历史数据）	           
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
			//构建Workbook对象，只读Workbook对象
			//直接从本地文件创建Workbook
			InputStream instream = new FileInputStream("C://Users/jma7/Desktop/Susie 1 day/ExcelSource/Source Data.xls");
			readwb=Workbook.getWorkbook(instream);
			
			//Anomaly Metrics_S5KA Monthly  Sheet2
			Sheet readsheet=readwb.getSheet(2);
			 
	        //获取Sheet表中所包含的总列数   	  
	           int rsColumns = readsheet.getColumns();   
	  
	           //获取Sheet表中所包含的总行数   
	       int rsRows = readsheet.getRows(); 
	       
	     //获取指定单元格的对象引用   并更新元数据列表（添加历史数据）	           
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

	public static String Set_Create_For_QA_S5KA_4(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA Stability (all anomally)";
		String description="";
		String x_name="Sprint";
		String y1_name="S5KA(incoming)";
		String y2_name="S5KA(fixed)";		
		
		return Create_Chart_For_QA_S5KA_4( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	public static String Set_Create_For_QA_S5KA_5(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA Stability (RC)";
		String description="";
		String x_name="Sprint";
		String y1_name="S5KA(incoming)";
		String y2_name="S5KA(fixed)";		
		
		return Create_Chart_For_QA_S5KA_4( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	public static String Set_Create_For_QA_S5KA_6(List<String> x_data,List<Integer> y1_data,List<Integer> y2_data)
	{
		String title="S5KA Stability (RC)";
		String description="";
		String x_name="Sprint";
		String y1_name="S5KA(incoming)";
		String y2_name="S5KA(fixed)";		
		
		return Create_Chart_For_QA_S5KA_4( title, description, x_name, y1_name, y2_name, x_data, y1_data, y2_data);		
	}
	public static String Create_Chart_For_QA_S5KA_4(
			String title,
			String description,
			String x_name,
			String y1_name,
			String y2_name,
			List<String> x_data,
			List<Integer> y1_data,
			List<Integer> y2_data)
	{
		Chart S5KA_One_Chart=new LineChart(title);
		S5KA_One_Chart.description=description;
		S5KA_One_Chart.tableData=new DataTable();
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, x_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.NUMBER, y1_name));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.NUMBER, y2_name));
		
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
