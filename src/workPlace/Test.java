package workPlace;


import java.util.Arrays;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import Charts.Chart;
import Charts.ChartSets;
import Charts.ColumnChart;
import Charts.LineChart;


public class Test {
	/*  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Chart_For_One();
		//Chart_For_Two();
		
		System.out.println("url：\n"+"http://apcndaekbhost:8080/#!/charts/"+ Set_Create_For_QA_S5KA_1());
	}
	*/
	public static String Set_Create_For_QA_S5KA_1()
	{
		String title="S5KA FY17 Anomlay Backlog_3.0";
		String description="Goal (Customer impact16):15";
		String x_name="Time";
		String y1_name="RC";
		String y2_name="Customer impact(24,16]";
		
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
		/////////////////////////////////////////////
		
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
	
	public static void Chart_For_Two()
	{
		Chart lineChart=new LineChart("Test_TWO_line");
		Chart columnChart=new ColumnChart("Test_TWO_column");
		
		try 
		{
			DataTable tableData=new DataTable();
			
			ColumnDescription c0 = new ColumnDescription("x", ValueType.TEXT, "change");
		    ColumnDescription c1 = new ColumnDescription("y1", ValueType.NUMBER, "2010");
		    ColumnDescription c2 = new ColumnDescription("y2", ValueType.NUMBER, "2000");

		    tableData.addColumn(c0);
		    tableData.addColumn(c1);
		    tableData.addColumn(c2);
		    
			List<TableRow> rows = Lists.newArrayList();

		    TableRow row = new TableRow();
		    row.addCell(new TableCell("111111"));
		    row.addCell(new TableCell(77));
		    row.addCell(new TableCell(56));
		    rows.add(row);

		    row = new TableRow();
		    row.addCell(new TableCell("22222222"));
		    row.addCell(new TableCell(88));
		    row.addCell(new TableCell(23));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("3333333333"));
		    row.addCell(new TableCell(46));
		    row.addCell(new TableCell(56));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("44444444444"));
		    row.addCell(new TableCell(88));
		    row.addCell(new TableCell(33));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("55555555555"));
		    row.addCell(new TableCell(21));
		    row.addCell(new TableCell(41));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("6666666666"));
		    row.addCell(new TableCell(33));
		    row.addCell(new TableCell(42));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("77777777777"));
		    row.addCell(new TableCell(77));
		    row.addCell(new TableCell(1));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("55555555555555"));
		    row.addCell(new TableCell(55));
		    row.addCell(new TableCell(31));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("888888888888"));
		    row.addCell(new TableCell(22));
		    row.addCell(new TableCell(4));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("44444444444444444444"));
		    row.addCell(new TableCell(14));
		    row.addCell(new TableCell(74));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("555555555555555555"));
		    row.addCell(new TableCell(6));
		    row.addCell(new TableCell(9));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("00000000000000000"));
		    row.addCell(new TableCell(66));
		    row.addCell(new TableCell(77));
		    rows.add(row);
		    
		    row = new TableRow();
		    row.addCell(new TableCell("244545545454545"));
		    row.addCell(new TableCell(55));
		    row.addCell(new TableCell(3));
		    rows.add(row);

		    tableData.addRows(rows);
		    
		    lineChart.tableData = tableData;
			String id1=lineChart.ToEagleEye();
			System.out.println("Chart："+id1);
			
			columnChart.tableData = tableData;
			String id2=columnChart.ToEagleEye();
			System.out.println("Chart："+id2);
			
			String id3=ChartSets.Create("S5KA_QA_TWO", "S5KA_QA", Arrays.asList(id1,id2));
			System.out.println("ChartSets："+id3);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return;
		}
	}
	
	public static void Chart_For_One()
	{
		Chart lineChart=new LineChart("Test_ONE_line");
		Chart columnChart=new ColumnChart("Test_ONE_column");
		
		try 
		{
			DataTable tableData=new DataTable();
			
			ColumnDescription c0 = new ColumnDescription("x", ValueType.TEXT, "City");
		    ColumnDescription c1 = new ColumnDescription("y", ValueType.NUMBER, "2010 Population");

		    tableData.addColumn(c0);
		    tableData.addColumn(c1);
		    
			List<TableRow> rows = Lists.newArrayList();

		    TableRow row = new TableRow();
		    row.addCell(new TableCell("New York City, NY"));
		    row.addCell(new TableCell(8175000));
		    rows.add(row);

		    row = new TableRow();
		    row.addCell(new TableCell("Los Angeles, CA"));
		    row.addCell(new TableCell(3792000));
		    rows.add(row);

		    tableData.addRows(rows);
		    
		    lineChart.tableData = tableData;
			String id1=lineChart.ToEagleEye();
			System.out.println("Chart："+id1);
			
			columnChart.tableData = tableData;
			String id2=columnChart.ToEagleEye();
			System.out.println("Chart："+id2);
			
			String id3=ChartSets.Create("S5KA_QA_ONE", "S5KA_QA", Arrays.asList(id1,id2));
			System.out.println("ChartSets："+id3);
		}catch(Exception e)
		{
			System.out.println("Import Exception!");
			return;
		}
	}
}
