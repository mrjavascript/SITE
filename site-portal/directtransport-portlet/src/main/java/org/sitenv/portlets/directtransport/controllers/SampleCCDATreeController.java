package org.sitenv.portlets.directtransport.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.sitenv.common.utilities.controller.BaseController;
import org.sitenv.portlets.directtransport.models.SampleCCDATreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.google.gson.Gson;

@Controller
@RequestMapping("VIEW")
public class SampleCCDATreeController extends BaseController {
	
	private static Logger _log = Logger.getLogger(SampleCCDATreeController.class);
	
	private ArrayList<SampleCCDATreeNode> roots = null;

	private void tranverseDir(String path, SampleCCDATreeNode root, int deep)
			throws IOException {
		if (this.props == null) {
			this.loadProperties();
		}

		File[] files = (new File(path)).listFiles();

		if (files == null)
			return;

		Arrays.sort(files);

		int count = 1;
		deep++;
		for (File file : files) {
			count++;
			if (!file.getName().equalsIgnoreCase(".git")
					&& !file.getName().equalsIgnoreCase("README.md")) {

				if (file.isDirectory()) {
					String dirPath = file.getCanonicalPath();
					SampleCCDATreeNode folder = new SampleCCDATreeNode(
							file.getName(), "folder", "open", String.format(
									"%d_%d", deep, count), "helloword");
					folder.getMetadata().setDescription("This is CCDA file 1.");
					root.addChild(folder);
					tranverseDir(dirPath, folder, deep);
				} else {
					// String dirPath = getRelativePath(file,new
					// File(CCDAServerRootPath));
					String dirPath = file.getCanonicalPath();
					SampleCCDATreeNode folder = new SampleCCDATreeNode(
							file.getName(), "file", "leaf", String.format(
									"%d_%d", deep, count), "helloword");
					folder.getMetadata().setDescription("This is CCDA file 1.");
					folder.getMetadata().setServerPath(
							dirPath.replace(props.getProperty("sampleCcdaDir")
									+ "/", ""));
					root.addChild(folder);
				}
			}
		}

	}

	@ActionMapping(params = "javax.portlet.action=sampleCCDATree")
	public void response(ActionRequest request, ActionResponse response)
			throws IOException {
		if (this.props == null) {
			this.loadProperties();
		}

		_log.trace("Start get sample CCDAs.");
		SampleCCDATreeNode root = new SampleCCDATreeNode("Localhost", "root",
				"open", "1", "helloword");
		String CCDASampleDir = props.getProperty("sampleCcdaDir");
		this.tranverseDir(CCDASampleDir, root, 1);

		roots = new ArrayList<SampleCCDATreeNode>();
		roots.add(root);

		

		_log.trace("End get sample CCDAs.");

		response.setRenderParameter("javax.portlet.action", "sampleCCDATree");

	}

	@RequestMapping(params = "javax.portlet.action=sampleCCDATree")
	public ModelAndView process(RenderRequest request, Model model)
			throws IOException {
		Map map = new HashMap();
		
		Gson gson = new Gson();
		String json = gson.toJson(roots);

		map.put("jsonRoot", json);

		return new ModelAndView("sampleCCDATreeJsonView", map);
	}

}
