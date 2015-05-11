package com.github.herrmanno.grexpress

import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest

class Request extends Expando {

	private HttpServletRequest req;

	final String path;
	final String method;
	final Map params = [:]
	final Map headers = [:]
	final Map data = [:]
	final Object json
	final Session session
	
	
	def Request(HttpServletRequest r) {
		this.req = r
		this.path = req.getRequestURI().substring(req.getContextPath().length())
		this.method = req.method
		
		r.getHeaderNames().each {this.headers << [(it):r.getHeader(it)]}
		//data = r.getParameterMap()
		r.getParameterMap().each {String k, String[] v ->
			data[k] = v.size() == 1 ? v[0] : v
		}
		
		try {
			this.json = new JsonSlurper().parse(r.inputStream)
		} catch(Exception e) {
			this.json = null
		}
		
		session = new Session(r.session)
	}
	
}
