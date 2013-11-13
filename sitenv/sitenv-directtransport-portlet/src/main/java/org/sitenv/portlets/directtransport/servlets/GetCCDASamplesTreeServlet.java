package org.sitenv.portlets.directtransport.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.sitenv.portlets.directtransport.models.jsTreeNode;

import com.google.gson.Gson;


public class GetCCDASamplesTreeServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	public static final String SAMPLECCDAROOT = "SampleCCDA";
	
	private static Logger _log = Logger.getLogger(GetCCDASamplesTreeServlet.class);
	
	public String getRelativePath(File file, File folder) {
	    String filePath = file.getAbsolutePath();
	    String folderPath = folder.getAbsolutePath();
	    if (filePath.startsWith(folderPath)) {
	        return filePath.substring(folderPath.length() + 1);
	    } else {
	        return null;
	    }
	}
	
	private void tranverseDir(String path, jsTreeNode root, int deep) throws IOException
	{
		File[] files = (new File(path)).listFiles();
		
		if(files==null) return;
		
		int count = 1;
		deep++;
		for(File file : files)
		{
			count++;
			if(!file.getName().equalsIgnoreCase(".git") && !file.getName().equalsIgnoreCase("README.md"))
			{
				
			
			if(file.isDirectory())
			{
				String dirPath = file.getCanonicalPath();
				jsTreeNode folder = new jsTreeNode(file.getName(), "folder", "open", String.format("%d_%d", deep, count) , "helloword");
				folder.getMetadata().setDescription("This is CCDA file 1.");
				root.addChild(folder);
				tranverseDir(dirPath,folder,deep);
			}
			else
			{
				//String dirPath = getRelativePath(file,new File(CCDAServerRootPath));
				String dirPath = file.getCanonicalPath();
				jsTreeNode folder = new jsTreeNode(file.getName(), "file", "leaf", String.format("%d_%d", deep, count) , "helloword");
				folder.getMetadata().setDescription("This is CCDA file 1.");
				folder.getMetadata().setServerPath(dirPath.replace("/Users/chris/Development/tomcat/tomcat-SITE/temp/sample_ccdas/", ""));
				root.addChild(folder);
			}
			}
		}
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			_log.trace("Start get sample CCDAs.");
			jsTreeNode root = new jsTreeNode("Localhost", "root", "open", "1", "helloword");
			String CCDASampleDir = "/Users/chris/Development/tomcat/tomcat-SITE/temp/sample_ccdas";
			this.tranverseDir(CCDASampleDir, root, 1);
			
			PrintWriter out = response.getWriter();
		    
		    //jsTreeNode github = new jsTreeNode("GitHub", "root", "leaf", "1", "helloword");
		    
		    ArrayList<jsTreeNode> roots = new ArrayList<jsTreeNode>();	   
		    roots.add(root);
		    //roots.add(github);
		    Gson gson = new Gson();
		    String json = gson.toJson(roots);
		    out.println(json);
		    _log.trace("End get sample CCDAs.");
		}
		catch(Exception exp)
		{
			_log.error("Failed to construct the tree structure, ", exp);
		}
	}
	
}
