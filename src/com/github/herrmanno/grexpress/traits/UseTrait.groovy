package com.github.herrmanno.grexpress.traits

import java.util.regex.Pattern

import com.github.herrmanno.grexpress.App
import com.github.herrmanno.grexpress.Util

trait UseTrait {
	
	def public void use(Object... c) {
		use('/', c)
	}

	def public void use(String path, Object... c) {
		use(Util.ContextToPattern(path), c)
	}
	
	def public void use(Pattern path, Object... c) {
		for(Object o : c) {
			if(!(o instanceof App) && !(o instanceof Closure<?>)) {
				throw new IllegalArgumentException("Only com.github.herrmanno.grexpress.App or groovy.lang.Closure are permitted")
			}
			list.add(['PATH':path, 'ANY':o] )
		}
	}
}
