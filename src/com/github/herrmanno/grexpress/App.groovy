package com.github.herrmanno.grexpress;

import java.util.regex.Pattern

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.github.herrmanno.grexpress.traits.GetTrait
import com.github.herrmanno.grexpress.traits.PostTrait
import com.github.herrmanno.grexpress.traits.RouteTrait
import com.github.herrmanno.grexpress.traits.UseTrait


//TODO Change list structure to '[method:'GET|ANY', route:'route', action:[closures]]'
public class App implements UseTrait, GetTrait, PostTrait, RouteTrait {

	List<Map> list = new LinkedList();
	String currentPath;
	
	
	def public void call(HttpServletRequest request, HttpServletResponse response) {
		def req = new Request(request)
		def resp = new Response(response, req)
		currentPath = req.path
		
		try {
			call(req, resp, {})
			if(!resp.sent)
				throw new Exception("No Content was sent - maybe no Route was invoked")
		} catch(Exception e) {
			e.printStackTrace()
			resp.send(500, e.message)
		}
	}

	public void call(Request req, Response resp, Closure<?> next) {
		
		def boolean brk = true;
		def unBreak = {
			brk = false
		}
		def Map m;
		for(int i = 0; i < list.size(); i++) {
			m = list[i]
			
			/*-----------------------------
			A Middleware (PATH is present)
			------------------------------*/
			
			if(m.PATH != null && m.PATH.matcher(currentPath).matches()) {
				if(m['ANY'].hasProperty('currentPath')) {
					def matcher = currentPath =~ m.PATH
					matcher.matches()
					m['ANY'].currentPath = currentPath.substring(matcher.group(1).length())
				}
				//Some Middleware - Next sets break to false
				if(i+1 < list.size()) {
					brk = true
					invoke(m['ANY'], req, resp, unBreak)
				}
				//Last List item - Next is parents next
				else {
					brk = true
					invoke(m['ANY'], req, resp, next)
				}

				if(brk) break;
			} 
			
			
			/*-----------------------
			A Route (ROUTE is present)
			------------------------*/
			
			//------- Continue if requested Method is not supported by this route
			else if(m[req.method] != null && m.ROUTE && m.ROUTE.matcher(currentPath).matches()) {
				req.params = Util.getParams(currentPath, m.ROUTE)
				
				//-------Some Route - next() sets break to false
				if(i+1 < list.size()) {
					brk = true
					req.params = 
					invoke(m[req.method], req, resp, unBreak)
				}
				
				//-------Last List item - Next is parents next
				else {
					brk = true
					invoke(m[req.method], req, resp, next)
				}
				
				if(brk) break;
			}
			
			if(brk) break;
		}
	}
	
	//TODO make list of closures invokable
	def private invoke(Closure c, Request req, Response resp, Closure<?> next) {
		def params = [req, resp, next] + req?.params.values()
		params = params[0..(c.maximumNumberOfParameters-1)]
		c(*params)
	}
	
	def private invoke(App a, Request req, Response resp, Closure<?> next) {
		def params = [req, resp, next]
		a(*params)
	}
	
	def String toString() {
		def sb = new StringBuilder()
		list.each {
			if(it.ANY)
				sb << "MIDDLEWARE\t\t"
			if(it.GET)
				sb << "GET\t\t"
			if(it.POST)
				sb << "POST\t\t"
				
			sb << ' :: '
			
			if(it.ANY)
				sb << "$m.PATH\t\t"
			else
				sb << "$m.ROUTE\t\t"
				
			sb << ' :: '
			
			if(it.ANY)
				sb << "$m.ANY\t\t"
			if(it.GET)
				sb << "$m.GET\t\t"
			if(it.POST)
				sb << "$m.POST\t\t"
		}
	}

}
