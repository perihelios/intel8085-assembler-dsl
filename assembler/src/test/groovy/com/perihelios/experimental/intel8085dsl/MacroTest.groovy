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

import com.perihelios.experimental.intel8085dsl.exceptions.UndefinedLabelException
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

	def "Labels outside macro can't be referenced inside"() {
		when:
			asm {
				start
				macro("foo") {
					LXI(B, start)
				}
				foo()
			}

		then:
			thrown(UndefinedLabelException)
	}

	def "Labels inside macro can't be referenced outside"() {
		when:
			asm {
				macro("foo") {
					loop NOP()
				}
				foo()
				LXI(B, loop)
			}

		then:
			thrown(UndefinedLabelException)
	}

	def "Labels resolved appropriately when using macros"() {
		setup:
			int startingAddress = 0xab00

		when:
			byte[] machineCode = asm {
				macro("foo") {
					start
						LXI(B, start)
						LXI(B, end)
					end
				}

				(1..startingAddress).each { NOP() }

				start
					LXI(B, start)
					LXI(B, end)
					foo()
					foo()
					LXI(B, start)
					LXI(B, end)
				end
			}

		then:
			machineCode[startingAddress + 1] == 0x00 as byte
			machineCode[startingAddress + 2] == 0xab as byte

			machineCode[startingAddress + 4] == 0x18 as byte
			machineCode[startingAddress + 5] == 0xab as byte

			machineCode[startingAddress + 7] == 0x06 as byte
			machineCode[startingAddress + 8] == 0xab as byte
			machineCode[startingAddress + 10] == 0x0c as byte
			machineCode[startingAddress + 11] == 0xab as byte

			machineCode[startingAddress + 13] == 0x0c as byte
			machineCode[startingAddress + 14] == 0xab as byte
			machineCode[startingAddress + 16] == 0x12 as byte
			machineCode[startingAddress + 17] == 0xab as byte

			machineCode[startingAddress + 19] == 0x00 as byte
			machineCode[startingAddress + 20] == 0xab as byte

			machineCode[startingAddress + 22] == 0x18 as byte
			machineCode[startingAddress + 23] == 0xab as byte
	}
}
