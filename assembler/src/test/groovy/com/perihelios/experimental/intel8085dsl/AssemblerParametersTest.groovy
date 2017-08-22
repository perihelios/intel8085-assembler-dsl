/*
 Copyright 2017, Perihelios LLC

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.perihelios.experimental.intel8085dsl

import spock.lang.Specification

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8085

class AssemblerParametersTest extends Specification {
	def "Correct defaults applied"() {
		setup:
			def params = new AssemblerParameters()

		expect:
			params.target == i8085
			params.size == 0
			params.autoSize
			params.autoHalt

		when:
			params.validate()

		then:
			noExceptionThrown()
	}

	def "Validates target"() {
		when:
			new AssemblerParameters(target: null).validate()

		then:
			IllegalArgumentException e = thrown()
			e.message == "Target must be either i8080 or i8085; got null"
	}

	def "Validates size"() {
		setup:
			IllegalArgumentException expected

		when:
			new AssemblerParameters(size: 65537).validate()

		then:
			expected = thrown()
			expected.message == "Size must be from 1-65536; got 65537"

		when:
			new AssemblerParameters(size: 0).validate()

		then:
			expected = thrown()
			expected.message == "Size must be from 1-65536; got 0"

		when:
			new AssemblerParameters(size: -2).validate()

		then:
			expected = thrown()
			expected.message == "Size must be from 1-65536; got -2"
	}

	def "Validates autoSize"() {
		setup:
			IllegalArgumentException expected

		when:
			new AssemblerParameters(autoSize: true, size: 1).validate()

		then:
			expected = thrown()
			expected.message == "Set either size or autoSize, not both"

		when:
			new AssemblerParameters(autoSize: false).validate()

		then:
			expected = thrown()
			expected.message == "Must set size when not using autoSize"
	}

	def "Setting size clears autoSize"() {
		when:
			def params = new AssemblerParameters()
			params.size = 10

		then:
			!params.autoSize
	}
}
