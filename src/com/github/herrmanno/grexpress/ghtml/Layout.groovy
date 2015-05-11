package com.github.herrmanno.grexpress.ghtml

import java.util.Map

abstract class Layout extends View {

	@Override
	protected Object content(Map m) {
		def Set j = (m.js ?: []) + js ?: []
		def Set c = (m.js ?: []) + css ?: []
		m.js = j
		m.css = c
		
		html5 {
			head {
				m.css.each {css(it)}
				m.js.each {js(it)}
				_head_(m)
			}
			body {
				_body_(m)
			}
		}
	}
	
	def abstract _head_(m)
	def abstract _body_(m)
	
	/*
	def js () {[]}
	def css () {[]}
	*/
}
