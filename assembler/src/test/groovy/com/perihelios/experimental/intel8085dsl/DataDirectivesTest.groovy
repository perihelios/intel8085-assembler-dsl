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

import com.perihelios.experimental.intel8085dsl.exceptions.OverflowException
import com.perihelios.experimental.intel8085dsl.util.InvalidValue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm
import static com.perihelios.experimental.intel8085dsl.util.TestUtil.combine

class DataDirectivesTest extends Specification {
	@Shared
	List<Tuple2<String, InvalidValue>> dataDirectivePairs = combine(["DB"], [
		new InvalidValue(-129, "Operand value must be from -128 to 255; got -129 (0xf7f, 0577, 0b101111111)"),
		new InvalidValue(256, "Operand value must be from -128 to 255; got 256 (0x100, 0400, 0b100000000)")
	]) + combine(["DW"], [
		new InvalidValue(-32769, "Operand value must be from -32768 to 65535; got -32769 " +
			"(0xf7fff, 0677777, 0b10111111111111111)"),
		new InvalidValue(65536, "Operand value must be from -32768 to 65535; got 65536 " +
			"(0x10000, 0200000, 0b10000000000000000)")
	])

	@Unroll
	"DB #value"() {
		when:
			byte[] machineCode = asm {
				DB(value)
			}

		then:
			machineCode[0] == value as byte

		where:
			value << [-128, -1, 0, 1, 255]
	}

	@Unroll
	"DW #value"() {
		when:
			byte[] machineCode = asm {
				DW(value)			}

		then:
			machineCode[0] == (value & 0xff) as byte
			machineCode[1] == ((value >>> 8) & 0xff) as byte

		where:
			value << [-32768, -1, 0, 1, 65535]
	}

	@Unroll
	"#dataDirective overflow: #invalid.value"() {
		when:
			asm {
				"$dataDirective"(invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			dataDirective << dataDirectivePairs*.first
			invalid << dataDirectivePairs*.second
	}

	def "DB/DW series"() {
		when:
			byte[] machineCode = asm {
				start
					DB 0x20
					DW 0xabcd
					DB 0x01
					DW(start)
					DW(end)
					DB(LOW(start))
				end	DB(LOW(end))
			}

		then:
			machineCode[0] == 0x20 as byte
			machineCode[1] == 0xcd as byte
			machineCode[2] == 0xab as byte
			machineCode[3] == 0x01 as byte
			machineCode[4] == 0x00 as byte
			machineCode[5] == 0x00 as byte
			machineCode[6] == 0x09 as byte
			machineCode[7] == 0x00 as byte
			machineCode[8] == 0x00 as byte
			machineCode[9] == 0x09 as byte
	}
}
