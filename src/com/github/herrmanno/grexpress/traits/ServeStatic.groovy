package com.github.herrmanno.grexpress.traits

import java.nio.file.Files

trait ServeStatic {

	Closure<?> serveStatic(String path="") {
		def root = servletContext.getRealPath("/WEB-INF/$path");
		return {req, resp, next ->
			def p = root + File.separator + req.path
			p = p.replaceAll('\\/\\/', '/')
			def File f = new File(p)
			if(!f.exists() || f.isDirectory())
				return next()
			
			def ct = servletContext.getMimeType(p)
			resp.contentType = ct
			resp.send f.text
		}
	}
}
