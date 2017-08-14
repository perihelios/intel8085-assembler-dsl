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

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.*
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm

class AddressingTest extends Specification {
	def 'Current address pointer, $i, correct'() {
		when:
			byte[] machineCode = asm {
				LXI(H, $i)
				LXI(H, $i)
				LXI(H, $i + 10)

				(1..1000).each {
					LXI(H, $i)
				}
			}

		then:
			machineCode[1] == 0x00 as byte
			machineCode[2] == 0x00 as byte

			machineCode[4] == 0x03 as byte
			machineCode[5] == 0x00 as byte

			machineCode[7] == 0x10 as byte
			machineCode[8] == 0x00 as byte

			machineCode[3007] == 0xbe as byte
			machineCode[3008] == 0x0b as byte
	}
}
