package com.github.herrmanno.grexpress

import java.util.regex.Pattern

class Util {

	def static Pattern RouteToPattern(String s) {
		s = s.replaceAll(':([^\\s\\/]+)', '(?<$1>[^\\/]+)')
		s = s.replaceAll('\\*\\*\\/\\*', 'CATCHALL')
		s = s.replaceAll('\\*', "[^\\/]*")
		s = s.replaceAll('CATCHALL', '.*')
		if(s.startsWith("/"))
			s = '\\/?' + s.substring(1)
		else
			s = '\\/?' + s
		
		return Pattern.compile(s)
	}
	
	
	def static Pattern ContextToPattern(String s) {
		s = s.replaceAll('\\*\\*\\/\\*', 'CATCHALL')
		s = s.replaceAll('\\*', "[^\\\\/]*")
		s = s.replaceAll('CATCHALL', '.*')
		if(s.startsWith("/"))
			s = '\\/?' + s.substring(1)
		else
			s = '\\/?' + s
		s = '(' + s + ').*'
		
		return Pattern.compile(s)
	}
	
	
	def static getParams(String path, Pattern route) {
		def Map params = [:]
		def List paramNames = []
		
		def groupM = route =~ '\\?<([^>]+)>'
		while(groupM.find())
			paramNames << groupM.group(1)
		
		def m = path =~ route.pattern()
		m.matches()
		for(String name : paramNames) {
			def p = m.group(name)
			params << [(name): p]
		}
			
		return params
	}
	
	/*
	def static getParams(String path, String route) {
		def Map params = [:]
		def List paramNames = []
		
		def groupM = route =~ '\\?<([^>]+)>'
		while(groupM.find())
			paramNames << groupM.group(1)
		
		def m = path =~ route
		m.matches()
		for(String name : paramNames) {
			def p = m.group(name)
			params << [(name): p]
		}	
			
		return params
	}
	*/

}
