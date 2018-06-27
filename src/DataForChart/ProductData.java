package DataForChart;

import java.util.List;

import com.google.visualization.datasource.datatable.DataTable;

public class ProductData {
	public String description="";
	public String title="";
	public String xTitle="";
	public String yTitle="";
	public String yAxisFormat="";
	public List<String> colorList=null;
	public DataTable tableData;
	public String isStacked= "false";
	public int chartLeft=200;
	public int baseLineIndex=2;
	
	public List<String> ticks=null;
}
