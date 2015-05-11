package com.github.herrmanno.grexpress.ghtml
import javax.management.InstanceOfQueryExp;


public abstract class AbstractView {
	def static protected USAGE = "args: void | string | map | string, closure, | map, closure"
	
	def StringBuilder x
	def boolean pretty = true
	def int indent = 0
		
	
	def call(Map m=[:]) {
		
		x = new StringBuilder()
		content(m)
		return x
	}
	
	def protected abstract content(Map m)
	
	def methodMissing(String name, args) {
		doIndent()
		
		if(args.size() == 0) {
			noArgs(name)
		}
		else if(args.size() == 1) {
			if(args[0] instanceof String)
				string(name, args[0])
			else if(args[0] instanceof GString)
				string(name, args[0].toString())
			else if(args[0] instanceof Map)
				map(name, args[0])
			else if(args[0] instanceof Closure)
				closure(name, args[0])
			else
				throw new IllegalArgumentException(USAGE)
		}
		else if(args.size() == 2) {
			if(args[0] instanceof String && args[1] instanceof Map)
				stringMap(name, args[0], args[1])
			else if(args[0] instanceof GString && args[1] instanceof Map)
				stringMap(name, args[0].toString(), args[1])
			if(args[1] instanceof String && args[0] instanceof Map)
				stringMap(name, args[1], args[0])
			else if(args[1] instanceof GString && args[0] instanceof Map)
				stringMap(name, args[1].toString(), args[0])
			else if(args[0] instanceof Map && args[1] instanceof Closure)
				mapClosure(name, args[0], args[1])
			else
				throw new IllegalArgumentException(USAGE)
		}
		
		doNewLine()
	}
	
	/*-----------------------
	 *--- Helper Methods --- 
	 *----------------------*/
	
	def protected doIndent() {
		if(pretty) indent.times {x<<'\t'}
	}
	
	def protected doNewLine() {
		if(pretty) x << '\n'
	}
	
	def protected map(Map m) {
		m.each {k,v ->
			if(v instanceof Boolean && v == true) {
				x << "$k "
			} else {
				x << "$k=\"$v\" "
			}
		}
	}
	
	def protected nest(Closure c) {
		doNewLine()
		indent++
		c()
		indent--
		doIndent()
	}
	
	
	/*--------------------------
	 *---- Builder-Methods ----
	 *------------------------*/
	
	def protected noArgs(name) {
		x << "<$name></$name>"
	}
	
	def protected string(name, str) {
		x << "<$name>$str</$name>"
	}
	
	def protected map(name, m) {
		x << "<$name "
		
		map(m)
		
		x << "/>"
	}
	
	def protected closure(name, c) {
		x << "<$name>"
		nest(c)
		x << "</$name>"
	}
	
	def protected stringMap(name, str, m) {
		x << "<$name "
		
		map(m)
		
		x << ">$str</$name>"
	}
	
	def protected mapClosure(name, m, c) {
		x << "<$name "
		
		m.each {k,v ->
			if(v instanceof Boolean && v == true) {
				x << "$k "
			} else {
				x << "$k=$v "
			}
		}
		
		x << ">"
		nest(c)
		x << "</$name>"
	}

	@Override
	public String toString() {
		return x.toString()
	}
}
