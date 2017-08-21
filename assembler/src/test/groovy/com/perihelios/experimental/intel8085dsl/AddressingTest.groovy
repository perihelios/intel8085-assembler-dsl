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

	def 'Labels applied correctly'() {
		when:
			byte[] machineCode = asm {
				    MOV(A, B)
				start
					LXI(B, end)
					LXI(D, start)
					LXI(H, end)
				end
					MOV(A, B)
				inline LXI(B, inline)
					LXI(D, inline)
				more_inline MVI(A, 0)
					LDA(more_inline)
			}

		then:
			machineCode[2] == 0x0a as byte
			machineCode[3] == 0x00 as byte
			machineCode[5] == 0x01 as byte
			machineCode[6] == 0x00 as byte
			machineCode[8] == 0x0a as byte
			machineCode[9] == 0x00 as byte
			machineCode[12] == 0x0b as byte
			machineCode[13] == 0x00 as byte
			machineCode[15] == 0x0b as byte
			machineCode[16] == 0x00 as byte
			machineCode[20] == 0x11 as byte
			machineCode[21] == 0x00 as byte
	}

	def 'Label arithmetic works'() {
		when:
			byte[] machineCode = asm {
				    MOV(A, B)
				start
					LXI(H, start + 0xaaaa)
					LXI(H, start - 0xbbbb)
					MVI(H, LOW(start + 5))
					MVI(H, HIGH(start + 0x5500))
					MVI(H, LOW(LOW(start + 5)))
			}

		then:
			machineCode[2] == 0xab as byte
			machineCode[3] == 0xaa as byte
			machineCode[5] == 0x46 as byte
			machineCode[6] == 0x44 as byte
			machineCode[8] == 0x06 as byte
			machineCode[10] == 0x55 as byte
			machineCode[12] == 0x06 as byte
	}

	def 'HIGH works with literal value'() {
		when:
			byte[] machineCode = asm {
				MVI(A, HIGH(0xabcd))
			}

		then:
			machineCode[1] == 0xab as byte
	}

	def 'HIGH works with label'() {
		when:
			byte[] machineCode = asm {
				MVI(A, HIGH(lbl))
				NOP()

				(1..0x1231).each {
					MOV(A, A)
				}

				lbl
			}

		then:
			machineCode[1] == 0x12 as byte
			machineCode[2] == 0x00 as byte
	}

	def 'LOW works with literal value'() {
		when:
			byte[] machineCode = asm {
				MVI(A, LOW(0xabcd))
			}

		then:
			machineCode[1] == 0xcd as byte
	}

	def 'LOW works with label'() {
		when:
			byte[] machineCode = asm {
				MVI(A, LOW(lbl))
				NOP()

				(1..0x1231).each {
					MOV(A, A)
				}

				lbl
			}

		then:
			machineCode[1] == 0x34 as byte
			machineCode[2] == 0x00 as byte
	}
}
