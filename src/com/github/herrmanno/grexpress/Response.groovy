package com.github.herrmanno.grexpress

import groovy.json.JsonBuilder

import java.util.zip.GZIPOutputStream

import javax.servlet.http.HttpServletResponse

import com.github.herrmanno.grexpress.ghtml.GhtmlRenderEngine

class Response {
	
	static renderEngine = new GhtmlRenderEngine() 

	private HttpServletResponse resp
	private Request req
	private compress = false
	private sent = false
	
	Map headers = [:]
	
	String contentType;
	def setContentType(String value) {
		contentType = value
		resp.setContentType(value)
	}
	
	//------- CONSTRUCTOR
	def Response(HttpServletResponse resp, Request req) {
		this.resp = resp
		this.req = req
		def ae = req.headers['accept-encoding']
		this.compress = (ae != null && ae.indexOf('gzip') != -1)
		if(this.compress)
			headers << ['Content-Encoding': 'gzip']
	}
	
	def public render(String view, Map params=[:]) {
		renderEngine(req, this, view, params)
	}
	
	def public sendJson(Object obj) {
		sendJson(new JsonBuilder(obj).toPrettyString())
	}
	
	def public sendJson(String json) {
		contentType = 'application/json'
		write(json)
	}
	
	def public send(int status = 200, String s) {
		if(sent)
			throw new Exception("Content already sent")
		resp.setStatus(status)
		writeHeaders()
		write(s)
	}
	
	def private writeHeaders() {
		headers.each {k, v -> resp.setHeader(k, v)}
	}
	
	def private write(String s) {
		if(!compress) {
			resp.writer.write(s)
		} else {
		
			def os = new ByteArrayOutputStream()
			def gs = new GZIPOutputStream(os)
			gs.write(s.bytes)
			gs.close()
			resp.writer.write(os.toString('ISO-8859-1'))
		}
		sent = true
	}
	
	def redirect(String url) {
		this.resp.sendRedirect(url)
	}
	
	
}
