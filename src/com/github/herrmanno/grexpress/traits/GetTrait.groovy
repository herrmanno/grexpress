package com.github.herrmanno.grexpress.traits

import java.util.regex.Pattern

import com.github.herrmanno.grexpress.Util

trait GetTrait {

	def public void get(String route, Closure<?>... c) {
		get(Util.RouteToPattern(route), c)
	}
	
	def public void get(Pattern route, Closure<?>... c) {
		for(Closure o : c)
			list.add(['ROUTE':route, 'GET': o] )
	}
}
