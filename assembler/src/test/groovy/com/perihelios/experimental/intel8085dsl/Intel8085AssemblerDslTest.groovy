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

import com.perihelios.experimental.intel8085dsl.exceptions.InvalidInstructionForTargetException
import com.perihelios.experimental.intel8085dsl.exceptions.OverflowException
import spock.lang.Specification
import spock.lang.Unroll

import static Intel8085AssemblerDsl.AUTO_SIZE
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8080
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8085
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.A
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.B
import static Intel8085AssemblerDsl.asm

class Intel8085AssemblerDslTest extends Specification {
	def "target validated"() {
		when:
			asm(null) {}

		then:
			IllegalArgumentException e = thrown()
			e.message == "Target must be either i8080 or i8085; got null"
	}

	def "bytes validated"() {
		setup:
			IllegalArgumentException expected

		when:
			asm(i8085, 65537) {}

		then:
			expected = thrown()
			expected.message == "Bytes must be from 1-65536, or AUTO_SIZE (magic number: -1); got 65537"

		when:
			asm(i8085, 0) {}

		then:
			expected = thrown()
			expected.message == "Bytes must be from 1-65536, or AUTO_SIZE (magic number: -1); got 0"

		when:
			asm(i8085, -2) {}

		then:
			expected = thrown()
			expected.message == "Bytes must be from 1-65536, or AUTO_SIZE (magic number: -1); got -2"
	}

	def "AUTO_SIZE automatically sizes array"() {
		setup:
			byte[] machineCode

		when:
			machineCode = asm(i8085, AUTO_SIZE, false) {
				MOV(A, B)
				LXI(B, 0xabcd)
			}

		then:
			machineCode.length == 4
	}

	def "autoHalt automatically adds HLT at end"() {
		setup:
			byte[] machineCode

		when:
			machineCode = asm(i8085, 4, true) {}

		then:
			machineCode[0] == 0x76 as byte

		when:
			machineCode = asm(i8085, 4, false) {}

		then:
			machineCode[0] == 0x00 as byte
	}

	@Unroll
	"8080 target rejects unimplemented instruction #inst"() {
		when:
			asm(i8080) {
				"$inst"()
			}

		then:
			InvalidInstructionForTargetException expected = thrown()
			expected.message == "$inst instruction cannot be used with i8080 target"

		where:
			inst << ["RIM", "SIM"]
	}

	@Unroll
	"argument overflow: #inst #value"() {
		when:
			asm {
				"$inst"(value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == message

		where:
			inst  | value || message
			"ACI" | 256   || "Value 256 (0x100, 0400, 0b100000000) is larger than 8 bits"
			"ACI" | -129  || "Value -129 (0x17f, 0577, 0b101111111) is larger than 8 bits"
			"ACI" | -1000 || "Value -1000 (0xfc18, 076030, 0b10000011000) is larger than 8 bits"
	}
}
