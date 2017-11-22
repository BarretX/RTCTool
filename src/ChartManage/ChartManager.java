package ChartManage;

import java.util.List;
import com.google.visualization.datasource.datatable.DataTable;

import Charts.*;
import DataForChart.ProductData;

public class ChartManager {
	public ProductData data;
	public Chart chart;
	
	public ChartManager(ProductData data, Chart chart)
	{
		this.data=data;
		this.chart=chart;
		initChartData();
	}
	
	public void initChartData()
	{
		chart.description=data.description;
		chart.title=data.title;
		chart.xTitle=data.xTitle;
		chart.yTitle=data.yTitle;
		chart.yAxisFormat=data.yAxisFormat;
		chart.colorList=data.colorList;
		chart.tableData=data.tableData;
		chart.isStacked= data.isStacked;
		chart.chartLeft=data.chartLeft;
		chart.baseLineIndex=data.baseLineIndex;
	}
	
	public String createChartInEagleEye()
	{
		return chart.ToEagleEye();
	}
	
	public static String CreateChartSet(String title,String description, List<String> chartSetIDList )
	{
		String chartSetID=ChartSets.Create(title, description, chartSetIDList);
		return chartSetID;
	}
}
