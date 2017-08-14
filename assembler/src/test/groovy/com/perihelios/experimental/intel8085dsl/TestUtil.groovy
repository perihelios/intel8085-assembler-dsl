package com.perihelios.experimental.intel8085dsl

class TestUtil {
	static <L, R> List<Tuple2<L, R>> combine(Iterable<L> left, Iterable<R> right) {
		List<Tuple2<L, R>> result = []

		left.each { leftItem ->
			right.each { rightItem ->
				result << new Tuple2<L, R>(leftItem, rightItem)
			}
		}

		return result
	}
}
