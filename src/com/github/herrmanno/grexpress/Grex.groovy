package com.github.herrmanno.grexpress;

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.github.herrmanno.grexpress.traits.ServeStatic

public class Grex extends HttpServlet implements ServeStatic {

	private static final long serialVersionUID = -8369088200001536500L;
	
	public final App app = new App();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		app(req, resp);		
	}
	
}
