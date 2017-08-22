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

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm

class NamingTest extends Specification {
	def "Lowercase mnemonics work"() {
		when:
			byte[] machineCode = asm {
				mvi B, 0x10
				lxi H, 0x1234
			}

		then:
			machineCode[0] == 0x06 as byte
			machineCode[1] == 0x10 as byte

			machineCode[2] == 0x21 as byte
			machineCode[3] == 0x34 as byte
			machineCode[4] == 0x12 as byte
	}

	def "Mixed-case mnemonics don't work"() {
		when:
			asm { mVI B, 10 }

		then:
			thrown(MissingMethodException)

		when:
			asm { Mvi(B, 10) }

		then:
			thrown(MissingMethodException)
	}

	def "Totally missing methods don't work"() {
		when:
			asm { whoa() }

		then:
			thrown(MissingMethodException)

		when:
			asm { whoa(17) }

		then:
			thrown(MissingMethodException)

		when:
			asm { whoa(Mock(Label)) }

		then:
			thrown(MissingMethodException)
	}
}
