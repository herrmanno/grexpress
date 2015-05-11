package com.github.herrmanno.grexpress.traits

import java.util.regex.Pattern

import com.github.herrmanno.grexpress.Util

trait PostTrait {

	def public void post(String route, Closure<?>... c) {
		post(Util.RouteToPattern(route), c)
	}
	
	def public void post(Pattern route, Closure<?>... c) {
		for(Closure o : c)
			list.add(['ROUTE':route, 'POST': o] )
	}
}
