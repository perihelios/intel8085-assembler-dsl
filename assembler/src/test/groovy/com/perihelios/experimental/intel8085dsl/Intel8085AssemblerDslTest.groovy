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
import spock.lang.Specification
import spock.lang.Unroll

import static Intel8085AssemblerDsl.asm
import static com.perihelios.experimental.intel8085dsl.ProcessorTarget.i8080

class Intel8085AssemblerDslTest extends Specification {
	def "Parameters validated"() {
		setup:
			def params = Mock(AssemblerParameters)

		when:
			asm(params) {}

		then:
			1 * params.validate()
	}

	def "autoSize automatically sizes array"() {
		setup:
			byte[] machineCode

		when:
			machineCode = asm(new AssemblerParameters(autoSize: true, autoHalt: false)) {
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
			machineCode = asm(new AssemblerParameters(size: 4, autoHalt: true)) {}

		then:
			machineCode[0] == 0x76 as byte

		when:
			machineCode = asm(new AssemblerParameters(size: 4, autoHalt: false)) {}

		then:
			machineCode[0] == 0x00 as byte
	}

	@Unroll
	"8080 target rejects unimplemented instruction #inst"() {
		when:
			asm(new AssemblerParameters(target: i8080)) {
				"$inst"()
			}

		then:
			InvalidInstructionForTargetException expected = thrown()
			expected.message == "$inst instruction cannot be used with i8080 target"

		where:
			inst << ["RIM", "SIM"]
	}
}
