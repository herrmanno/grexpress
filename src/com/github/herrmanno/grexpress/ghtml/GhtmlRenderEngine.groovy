package com.github.herrmanno.grexpress.ghtml;

import com.github.herrmanno.grexpress.Request
import com.github.herrmanno.grexpress.Response
import com.sun.java.util.jar.pack.Package.Class;

public class GhtmlRenderEngine {

	def String rootPackage = 'view';
	
	def call(Request req, Response resp, String file, Map params) {
		def fullPath = rootPackage + '.' + file.replaceAll('\\/', '.')
		def View view = Class.forName(fullPath).newInstance()
		resp.contentType = 'text/html'
		resp.send(view([req:req, resp:resp, *:params]).toString())
	}
}
