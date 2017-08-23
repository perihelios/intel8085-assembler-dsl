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

class MacroTest extends Specification {
	def "Macros generate no machine code when not invoked"() {
		when:
			byte[] machineCode = asm(new AssemblerParameters(autoHalt: false)) {
				macro("foo") {
					LXI(H, 0x1234)
				}

				NOP()
			}

		then:
			machineCode == [0x00] as byte[]
	}

	def "Macros generate machine code at location where invoked"() {
		when:
			byte[] machineCode = asm {
				macro("foo") {
					LXI(H, 0x1234)
				}

				NOP()
				foo()
				MOV(A, A)
				foo()
			}

		then:
			machineCode[0] == 0x00 as byte

			machineCode[1] == 0x21 as byte
			machineCode[2] == 0x34 as byte
			machineCode[3] == 0x12 as byte

			machineCode[4] == 0x7f as byte

			machineCode[5] == 0x21 as byte
			machineCode[6] == 0x34 as byte
			machineCode[7] == 0x12 as byte
	}

	def "Macros take parameters"() {
		when:
			byte[] machineCode = asm {
				macro("ADDI") { Register reg, long value ->
					PUSH(PSW)
					MOV(A, reg)
					ADI(value)
					MOV(reg, A)
					POP(PSW)
				}

				ADDI(E, 0x23)
			}

		then:
			machineCode[0] == 0xf5 as byte

			machineCode[1] == 0x7b as byte
			machineCode[2] == 0xc6 as byte
			machineCode[3] == 0x23 as byte

			machineCode[4] == 0x5f as byte

			machineCode[5] == 0xf1 as byte
	}
}
