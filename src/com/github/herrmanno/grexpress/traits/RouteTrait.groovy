package com.github.herrmanno.grexpress.traits

import java.util.regex.Pattern

import com.github.herrmanno.grexpress.App
import com.github.herrmanno.grexpress.Util

trait RouteTrait {

	def public R route(String r) {
		return route(Util.RouteToPattern(r))
	}
	
	def public R route(Pattern r) {
		return new R(this, r)
	}
	
	
}

class R {
	private final App a;
	private final Pattern p;
	
	def R(App a, Pattern p) {
		this.a = a;
		this.p = p;
	}
	
	def R get(Closure<?>... c) {
		a.get(p, c)
		return this
	}
	
	def R post(Closure<?>... c) {
		a.post(p, c)
		return this
	}
}
