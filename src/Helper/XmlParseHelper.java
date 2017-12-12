package Helper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlParseHelper {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		Document document=load("output.xml");
		System.out.println(document.asXML());
		
		Element element=(Element)document.selectSingleNode("/root/author[@name ='xiong']");
		if(element == null)
		{
			System.out.println("获取节点失败");
			return;
		}
		System.out.println(element.asXML());
		
		element.addAttribute("name", "xiongang");
		System.out.println(element.asXML());
		
		persist(document,"output.xml");
		*/
		Document document=load("config.xml");
		List<Node> timeNodes = document.selectNodes("//@time");
        System.out.println(timeNodes.size());	

        timeNodes=document.selectSingleNode("/Products/Bucket[@name='FTView']/Product[@name ='FTView SE']").selectNodes(".//@time");
        System.out.println(timeNodes.size());
	}	
	
	public static Document load(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            SAXReader saxReader = new SAXReader();
            try {
                return saxReader.read(file);
            } catch (DocumentException e) {    
                System.out.println("文件加载异常：" + filePath);              
            }
        } else{
            System.out.println("文件不存在 : " + filePath);
        } 
        return null;
    }
	
	public static void persist(Document document, String filePath){
		try{
			FileWriter out = new FileWriter(filePath);
			document.write(out);
			out.close();
		} catch(IOException e){
			System.out.println("文件加载异常：" + filePath); 
		}
			
    }
}
