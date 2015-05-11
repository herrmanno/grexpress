package com.github.herrmanno.grexpress.ghtml
import java.util.Map


public abstract class View extends AbstractView {

	def html5(Map m=[:], Closure c) {
		doIndent()
		x << '<!DOCTYPE html>\n'
		x << '<html '
		map(m)
		x << '>'
		nest(c)
		x << '</html>'
		doNewLine()
	}
	
	def css(String href) {
		//href += href.toLowerCase().endsWith('.css') ? '' : '.css'
		if(href.endsWith('.less'))
			return less(href)
			
		doIndent()
		x << "<link rel='stylesheet' type='text/css' href='$href' >"
		doNewLine()
	}
	
	def less(String href) {
		//href += href.toLowerCase().endsWith('.css') ? '' : '.css'
		
		doIndent()
		x << "<link rel='stylesheet' type='text/less' href='$href' >"
		doNewLine()
	}
	
	def js(String src) {
		doIndent()
		x << "<script type='text/javascript' src='$src'></script>"
		doNewLine()
	}
	
	def script(String str) {
		doIndent()
		x << "<script type='text/javascript'>$str</script>"
		doNewLine()
	}
	
	def script(Closure c) {
		doIndent()
		x << "<script type='text/javascript'>"
		nest(c)
		x << "</script>"
		doNewLine()
	}
	
	def a(String href, String text) {
		doIndent()
		x << "<a href='$href'>$text</a>"
		doNewLine()
	}
	
	def br() {
		doIndent()
		x << '<br>'
		doNewLine()
	}

	def text(String str) {
		doIndent()
		x << str
		doNewLine()
	}

}
