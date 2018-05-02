package Test;


import java.util.Arrays;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import Charts.Chart;
import Charts.AreaChart;
import Charts.BarChart;
import Charts.ChartSets;
import Charts.ColumnChart;
import Charts.ComboChart;
import Charts.LineChart;
import ConstVar.ConstString;


public class Test {
  
/*	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		String chartID1=Create_BarChart();
		System.out.println("chart url:\n"+ ConstString.CHART_URL + chartID1);
		String chartID2=Create_LineChart();
		System.out.println("chart url:\n"+ ConstString.CHART_URL + chartID2);
		String chartID3=Create_ColumnChart();
		System.out.println("chart url:\n"+ ConstString.CHART_URL + chartID3);
		String chartID4=Create_AreaChart();
		System.out.println("chart url:\n"+ ConstString.CHART_URL + chartID4);
		String chartID5=Create_ComboChart();
		System.out.println("chart url:\n"+ ConstString.CHART_URL + chartID5);
		
		String chartSetID=CreateChartSet(Arrays.asList(chartID1,chartID2,chartID3,chartID4,chartID5));
		System.out.println("chartSet url:\n"+ ConstString.CHART_SET_URL + chartSetID);
		//Example:  http://apcndaec3ycs12:8080/#!/chart-sets/59ed906da8e8eb07883a2ed8
		
	}*/

	public static String Create_AreaChart()
	{						
		Chart S5KA_One_Chart=new AreaChart("S5KA FY17 Anomlay Backlog_3.0");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		((AreaChart)S5KA_One_Chart).isStacked="true";
		//((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=Arrays.asList(
						"2/27/2017",
						"3/6/2017",
						"3/13/2017",
						"3/20/2017",
						"3/27/2017",
						"4/3/2017",
						"4/10/2017",
						"4/17/2017",
						"4/24/2017",
						"5/1/2017",
						"5/8/2017",
						"5/15/2017",
						"5/22/2017",
						"5/29/2017",
						"6/5/2017",
						"6/12/2017",
						"6/19/2017",
						"6/26/2017",
						"7/3/2017",
						"7/10/2017");
				List<Integer> y1_data=Arrays.asList(
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						5,
						5,
						4,
						2,
						1,
						2);
				List<Integer> y2_data=Arrays.asList(
						8,
						7,
						2,
						3,
						3,
						3,
						3,
						3,
						3,
						2,
						2,
						2,
						2,
						2,
						2,
						2,
						3,
						3,
						3,
						1);
		
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
	
	public static String Create_ColumnChart()
	{						
		Chart S5KA_One_Chart=new ColumnChart("S5KA FY17 Anomlay Backlog_3.0");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		//((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=Arrays.asList(
						"2/27/2017",
						"3/6/2017",
						"3/13/2017",
						"3/20/2017",
						"3/27/2017",
						"4/3/2017",
						"4/10/2017",
						"4/17/2017",
						"4/24/2017",
						"5/1/2017",
						"5/8/2017",
						"5/15/2017",
						"5/22/2017",
						"5/29/2017",
						"6/5/2017",
						"6/12/2017",
						"6/19/2017",
						"6/26/2017",
						"7/3/2017",
						"7/10/2017");
				List<Integer> y1_data=Arrays.asList(
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						5,
						5,
						4,
						2,
						1,
						2);
				List<Integer> y2_data=Arrays.asList(
						8,
						7,
						2,
						3,
						3,
						3,
						3,
						3,
						3,
						2,
						2,
						2,
						2,
						2,
						2,
						2,
						3,
						3,
						3,
						1);
		
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
	
	public static String Create_ComboChart()
	{						
		Chart S5KA_One_Chart=new ComboChart("S5KA FY17 Anomlay Backlog_3.0");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		//((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=Arrays.asList(
						"2/27/2017",
						"3/6/2017",
						"3/13/2017",
						"3/20/2017",
						"3/27/2017",
						"4/3/2017",
						"4/10/2017",
						"4/17/2017",
						"4/24/2017",
						"5/1/2017",
						"5/8/2017",
						"5/15/2017",
						"5/22/2017",
						"5/29/2017",
						"6/5/2017",
						"6/12/2017",
						"6/19/2017",
						"6/26/2017",
						"7/3/2017",
						"7/10/2017");
				List<Integer> y1_data=Arrays.asList(
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						5,
						5,
						4,
						2,
						1,
						2);
				List<Integer> y2_data=Arrays.asList(
						8,
						7,
						2,
						3,
						3,
						3,
						3,
						3,
						3,
						2,
						2,
						2,
						2,
						2,
						2,
						2,
						3,
						3,
						3,
						1);
		
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
	
	public static String Create_BarChart()
	{						
		Chart S5KA_One_Chart=new BarChart("S5KA FY17 Anomlay Backlog_3.0");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=Arrays.asList(
						"2/27/2017",
						"3/6/2017",
						"3/13/2017",
						"3/20/2017",
						"3/27/2017",
						"4/3/2017",
						"4/10/2017",
						"4/17/2017",
						"4/24/2017",
						"5/1/2017",
						"5/8/2017",
						"5/15/2017",
						"5/22/2017",
						"5/29/2017",
						"6/5/2017",
						"6/12/2017",
						"6/19/2017",
						"6/26/2017",
						"7/3/2017",
						"7/10/2017");
				List<Integer> y1_data=Arrays.asList(
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						5,
						5,
						4,
						2,
						1,
						2);
				List<Integer> y2_data=Arrays.asList(
						8,
						7,
						2,
						3,
						3,
						3,
						3,
						3,
						3,
						2,
						2,
						2,
						2,
						2,
						2,
						2,
						3,
						3,
						3,
						1);
		
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
	
	public static String Create_LineChart()
	{						
		Chart S5KA_One_Chart=new LineChart("S5KA FY17 Anomlay Backlog_3.0");//Title
		
		S5KA_One_Chart.description="Goal (Customer impact16):15";//description		
		S5KA_One_Chart.xTitle="Date";
		S5KA_One_Chart.yTitle="Number";
		S5KA_One_Chart.yAxisFormat="#";
		S5KA_One_Chart.tableData=new DataTable();
		S5KA_One_Chart.colorList=Arrays.asList("red","green");
		//((ColumnChart)S5KA_One_Chart).isStacked="false";
		//((AreaChart)S5KA_One_Chart).isStacked="true";
		//((BarChart)S5KA_One_Chart).isStacked="true";
		
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("x", ValueType.TEXT, "Time"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y1", ValueType.INT, "RC"));
		S5KA_One_Chart.tableData.addColumn(new ColumnDescription("y2", ValueType.INT, "Customer impact(24,16]"));
		
		//Chart data
				//////////////////////////////////////////////
				List<String> x_data=Arrays.asList(
						"2/27/2017",
						"3/6/2017",
						"3/13/2017",
						"3/20/2017",
						"3/27/2017",
						"4/3/2017",
						"4/10/2017",
						"4/17/2017",
						"4/24/2017",
						"5/1/2017",
						"5/8/2017",
						"5/15/2017",
						"5/22/2017",
						"5/29/2017",
						"6/5/2017",
						"6/12/2017",
						"6/19/2017",
						"6/26/2017",
						"7/3/2017",
						"7/10/2017");
				List<Integer> y1_data=Arrays.asList(
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						0,
						5,
						5,
						4,
						2,
						1,
						2);
				List<Integer> y2_data=Arrays.asList(
						8,
						-2,
						2,
						3,
						-3,
						3,
						3,
						3,
						3,
						-2,
						2,
						2,
						-2,
						2,
						2,
						-2,
						3,
						3,
						3,
						1);
		
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
	
	public static String CreateChartSet(List<String> chartSetIDList)
	{
		String chartSetID=ChartSets.Create("S5KA_QA_ONE", "S5KA_QA", chartSetIDList);
		return chartSetID;
	}
}
