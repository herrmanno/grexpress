package com.github.herrmanno.grexpress

import javax.servlet.http.HttpSession;

class Session extends Expando {

	def Session(HttpSession s) {
		this.id = s.getId()
		s.attributeNames.each {this."$it" = s.getAttribute(it)}
	}
}
