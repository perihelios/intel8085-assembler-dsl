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
import spock.lang.Unroll

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.A
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.B
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.C
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.D
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.E
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.H
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.L
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.M
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.PSW
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.SP
import static Intel8085AssemblerDsl.asm

class InstructionTest extends Specification {
	@Unroll
	"ACI #value"() {
		when:
			byte[] machineCode = asm {
				ACI(value)
			}

		then:
			machineCode[0] == 0xCE as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"ADC #reg"() {
		when:
			byte[] machineCode = asm {
				ADC(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x8F
			B   | 0x88
			C   | 0x89
			D   | 0x8A
			E   | 0x8B
			H   | 0x8C
			L   | 0x8D
			M   | 0x8E
	}

	@Unroll
	"ADD #reg"() {
		when:
			byte[] machineCode = asm {
				ADD(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x87
			B   | 0x80
			C   | 0x81
			D   | 0x82
			E   | 0x83
			H   | 0x84
			L   | 0x85
			M   | 0x86
	}

	@Unroll
	"ADI #value"() {
		when:
			byte[] machineCode = asm {
				ADI(value)
			}

		then:
			machineCode[0] == 0xC6 as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"ANA #reg"() {
		when:
			byte[] machineCode = asm {
				ANA(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0xA7
			B   | 0xA0
			C   | 0xA1
			D   | 0xA2
			E   | 0xA3
			H   | 0xA4
			L   | 0xA5
			M   | 0xA6
	}

	@Unroll
	"ANI #value"() {
		when:
			byte[] machineCode = asm {
				ANI(value)
			}

		then:
			machineCode[0] == 0xE6 as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"CALL #address"() {
		when:
			byte[] machineCode = asm {
				CALL(address)
			}

		then:
			machineCode[0] == 0xCD as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CC #address"() {
		when:
			byte[] machineCode = asm {
				CC(address)
			}

		then:
			machineCode[0] == 0xDC as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CM #address"() {
		when:
			byte[] machineCode = asm {
				CM(address)
			}

		then:
			machineCode[0] == 0xFC as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CMA "() {
		when:
			byte[] machineCode = asm {
				CMA()
			}

		then:
			machineCode[0] == 0x2F as byte
	}

	@Unroll
	"CMC "() {
		when:
			byte[] machineCode = asm {
				CMC()
			}

		then:
			machineCode[0] == 0x3F as byte
	}

	@Unroll
	"CMP #reg"() {
		when:
			byte[] machineCode = asm {
				CMP(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0xBF
			B   | 0xB8
			C   | 0xB9
			D   | 0xBA
			E   | 0xBB
			H   | 0xBC
			L   | 0xBD
			M   | 0xBE
	}

	@Unroll
	"CNC #address"() {
		when:
			byte[] machineCode = asm {
				CNC(address)
			}

		then:
			machineCode[0] == 0xD4 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CNZ #address"() {
		when:
			byte[] machineCode = asm {
				CNZ(address)
			}

		then:
			machineCode[0] == 0xC4 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CP #address"() {
		when:
			byte[] machineCode = asm {
				CP(address)
			}

		then:
			machineCode[0] == 0xF4 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CPE #address"() {
		when:
			byte[] machineCode = asm {
				CPE(address)
			}

		then:
			machineCode[0] == 0xEC as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CPI #value"() {
		when:
			byte[] machineCode = asm {
				CPI(value)
			}

		then:
			machineCode[0] == 0xFE as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"CPO #address"() {
		when:
			byte[] machineCode = asm {
				CPO(address)
			}

		then:
			machineCode[0] == 0xE4 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"CZ #address"() {
		when:
			byte[] machineCode = asm {
				CZ(address)
			}

		then:
			machineCode[0] == 0xCC as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"DAA "() {
		when:
			byte[] machineCode = asm {
				DAA()
			}

		then:
			machineCode[0] == 0x27 as byte
	}

	@Unroll
	"DAD #reg"() {
		when:
			byte[] machineCode = asm {
				DAD(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0x09
			D   | 0x19
			H   | 0x29
			SP  | 0x39
	}

	@Unroll
	"DCR #reg"() {
		when:
			byte[] machineCode = asm {
				DCR(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x3D
			B   | 0x05
			C   | 0x0D
			D   | 0x15
			E   | 0x1D
			H   | 0x25
			L   | 0x2D
			M   | 0x35
	}

	@Unroll
	"DCX #reg"() {
		when:
			byte[] machineCode = asm {
				DCX(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0x0B
			D   | 0x1B
			H   | 0x2B
			SP  | 0x3B
	}

	@Unroll
	"DI "() {
		when:
			byte[] machineCode = asm {
				DI()
			}

		then:
			machineCode[0] == 0xF3 as byte
	}

	@Unroll
	"EI "() {
		when:
			byte[] machineCode = asm {
				EI()
			}

		then:
			machineCode[0] == 0xFB as byte
	}

	@Unroll
	"HLT "() {
		when:
			byte[] machineCode = asm {
				HLT()
			}

		then:
			machineCode[0] == 0x76 as byte
	}

	@Unroll
	"IN #port"() {
		when:
			byte[] machineCode = asm {
				IN(port)
			}

		then:
			machineCode[0] == 0xDB as byte
			machineCode[1] == port as byte

		where:
			port << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"INR #reg"() {
		when:
			byte[] machineCode = asm {
				INR(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x3C
			B   | 0x04
			C   | 0x0C
			D   | 0x14
			E   | 0x1C
			H   | 0x24
			L   | 0x2C
			M   | 0x34
	}

	@Unroll
	"INX #reg"() {
		when:
			byte[] machineCode = asm {
				INX(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0x03
			D   | 0x13
			H   | 0x23
			SP  | 0x33
	}

	@Unroll
	"JC #address"() {
		when:
			byte[] machineCode = asm {
				JC(address)
			}

		then:
			machineCode[0] == 0xDA as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JM #address"() {
		when:
			byte[] machineCode = asm {
				JM(address)
			}

		then:
			machineCode[0] == 0xFA as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JMP #address"() {
		when:
			byte[] machineCode = asm {
				JMP(address)
			}

		then:
			machineCode[0] == 0xC3 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JNC #address"() {
		when:
			byte[] machineCode = asm {
				JNC(address)
			}

		then:
			machineCode[0] == 0xD2 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JNZ #address"() {
		when:
			byte[] machineCode = asm {
				JNZ(address)
			}

		then:
			machineCode[0] == 0xC2 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JP #address"() {
		when:
			byte[] machineCode = asm {
				JP(address)
			}

		then:
			machineCode[0] == 0xF2 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JPE #address"() {
		when:
			byte[] machineCode = asm {
				JPE(address)
			}

		then:
			machineCode[0] == 0xEA as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JPO #address"() {
		when:
			byte[] machineCode = asm {
				JPO(address)
			}

		then:
			machineCode[0] == 0xE2 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"JZ #address"() {
		when:
			byte[] machineCode = asm {
				JZ(address)
			}

		then:
			machineCode[0] == 0xCA as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"LDA #address"() {
		when:
			byte[] machineCode = asm {
				LDA(address)
			}

		then:
			machineCode[0] == 0x3A as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"LDAX #reg"() {
		when:
			byte[] machineCode = asm {
				LDAX(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0x0A
			D   | 0x1A
	}

	@Unroll
	"LHLD #address"() {
		when:
			byte[] machineCode = asm {
				LHLD(address)
			}

		then:
			machineCode[0] == 0x2A as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"LXI #reg, #value"() {
		when:
			byte[] machineCode = asm {
				LXI(reg, value)
			}

		then:
			machineCode[0] == opcode as byte
			machineCode[1] == (value & 0xff) as byte
			machineCode[2] == ((value >>> 8) & 0xff) as byte

		where:
			reg | value  | opcode
			B   | 0      | 0x01
			B   | 1      | 0x01
			B   | 0xfffe | 0x01
			B   | 0xffff | 0x01
			D   | 0      | 0x11
			D   | 1      | 0x11
			D   | 0xfffe | 0x11
			D   | 0xffff | 0x11
			H   | 0      | 0x21
			H   | 1      | 0x21
			H   | 0xfffe | 0x21
			H   | 0xffff | 0x21
			SP  | 0      | 0x31
			SP  | 1      | 0x31
			SP  | 0xfffe | 0x31
			SP  | 0xffff | 0x31
	}

	@Unroll
	"MOV #dest, #src"() {
		when:
			byte[] machineCode = asm {
				MOV(dest, src)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			dest | src | opcode
			A    | A   | 0x7F
			A    | B   | 0x78
			A    | C   | 0x79
			A    | D   | 0x7A
			A    | E   | 0x7B
			A    | H   | 0x7C
			A    | L   | 0x7D
			A    | M   | 0x7E

			B    | A   | 0x47
			B    | B   | 0x40
			B    | C   | 0x41
			B    | D   | 0x42
			B    | E   | 0x43
			B    | H   | 0x44
			B    | L   | 0x45
			B    | M   | 0x46

			C    | A   | 0x4F
			C    | B   | 0x48
			C    | C   | 0x49
			C    | D   | 0x4A
			C    | E   | 0x4B
			C    | H   | 0x4C
			C    | L   | 0x4D
			C    | M   | 0x4E

			D    | A   | 0x57
			D    | B   | 0x50
			D    | C   | 0x51
			D    | D   | 0x52
			D    | E   | 0x53
			D    | H   | 0x54
			D    | L   | 0x55
			D    | M   | 0x56

			E    | A   | 0x5F
			E    | B   | 0x58
			E    | C   | 0x59
			E    | D   | 0x5A
			E    | E   | 0x5B
			E    | H   | 0x5C
			E    | L   | 0x5D
			E    | M   | 0x5E

			H    | A   | 0x67
			H    | B   | 0x60
			H    | C   | 0x61
			H    | D   | 0x62
			H    | E   | 0x63
			H    | H   | 0x64
			H    | L   | 0x65
			H    | M   | 0x66

			L    | A   | 0x6F
			L    | B   | 0x68
			L    | C   | 0x69
			L    | D   | 0x6A
			L    | E   | 0x6B
			L    | H   | 0x6C
			L    | L   | 0x6D
			L    | M   | 0x6E

			M    | A   | 0x77
			M    | B   | 0x70
			M    | C   | 0x71
			M    | D   | 0x72
			M    | E   | 0x73
			M    | H   | 0x74
			M    | L   | 0x75
	}

	@Unroll
	"MVI #reg, #value"() {
		when:
			byte[] machineCode = asm {
				MVI(reg, value)
			}

		then:
			machineCode[0] == opcode as byte
			machineCode[1] == value as byte

		where:
			reg | value | opcode
			A   | 0     | 0x3E
			A   | 1     | 0x3E
			A   | 0xff  | 0x3E
			A   | -1    | 0x3E
			A   | -128  | 0x3E

			B   | 0     | 0x06
			B   | 1     | 0x06
			B   | 0xff  | 0x06
			B   | -1    | 0x06
			B   | -128  | 0x06

			C   | 0     | 0x0E
			C   | 1     | 0x0E
			C   | 0xff  | 0x0E
			C   | -1    | 0x0E
			C   | -128  | 0x0E

			D   | 0     | 0x16
			D   | 1     | 0x16
			D   | 0xff  | 0x16
			D   | -1    | 0x16
			D   | -128  | 0x16

			E   | 0     | 0x1E
			E   | 1     | 0x1E
			E   | 0xff  | 0x1E
			E   | -1    | 0x1E
			E   | -128  | 0x1E

			H   | 0     | 0x26
			H   | 1     | 0x26
			H   | 0xff  | 0x26
			H   | -1    | 0x26
			H   | -128  | 0x26

			L   | 0     | 0x2E
			L   | 1     | 0x2E
			L   | 0xff  | 0x2E
			L   | -1    | 0x2E
			L   | -128  | 0x2E

			M   | 0     | 0x36
			M   | 1     | 0x36
			M   | 0xff  | 0x36
			M   | -1    | 0x36
			M   | -128  | 0x36
	}

	@Unroll
	"NOP "() {
		when:
			byte[] machineCode = asm {
				NOP()
			}

		then:
			machineCode[0] == 0x00 as byte
	}

	@Unroll
	"ORA #reg"() {
		when:
			byte[] machineCode = asm {
				ORA(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0xB7
			B   | 0xB0
			C   | 0xB1
			D   | 0xB2
			E   | 0xB3
			H   | 0xB4
			L   | 0xB5
			M   | 0xB6
	}

	@Unroll
	"ORI #value"() {
		when:
			byte[] machineCode = asm {
				ORI(value)
			}

		then:
			machineCode[0] == 0xF6 as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"OUT #port"() {
		when:
			byte[] machineCode = asm {
				OUT(port)
			}

		then:
			machineCode[0] == 0xD3 as byte
			machineCode[1] == port as byte

		where:
			port << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"PCHL "() {
		when:
			byte[] machineCode = asm {
				PCHL()
			}

		then:
			machineCode[0] == 0xE9 as byte
	}

	@Unroll
	"POP #reg"() {
		when:
			byte[] machineCode = asm {
				POP(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0xC1
			D   | 0xD1
			H   | 0xE1
			PSW | 0xF1
	}

	@Unroll
	"PUSH #reg"() {
		when:
			byte[] machineCode = asm {
				PUSH(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0xC5
			D   | 0xD5
			H   | 0xE5
			PSW | 0xF5
	}

	@Unroll
	"RAL "() {
		when:
			byte[] machineCode = asm {
				RAL()
			}

		then:
			machineCode[0] == 0x17 as byte
	}

	@Unroll
	"RAR "() {
		when:
			byte[] machineCode = asm {
				RAR()
			}

		then:
			machineCode[0] == 0x1F as byte
	}

	@Unroll
	"RC "() {
		when:
			byte[] machineCode = asm {
				RC()
			}

		then:
			machineCode[0] == 0xD8 as byte
	}

	@Unroll
	"RET "() {
		when:
			byte[] machineCode = asm {
				RET()
			}

		then:
			machineCode[0] == 0xC9 as byte
	}

	@Unroll
	"RIM "() {
		when:
			byte[] machineCode = asm {
				RIM()
			}

		then:
			machineCode[0] == 0x20 as byte
	}

	@Unroll
	"RLC "() {
		when:
			byte[] machineCode = asm {
				RLC()
			}

		then:
			machineCode[0] == 0x07 as byte
	}

	@Unroll
	"RM "() {
		when:
			byte[] machineCode = asm {
				RM()
			}

		then:
			machineCode[0] == 0xF8 as byte
	}

	@Unroll
	"RNC "() {
		when:
			byte[] machineCode = asm {
				RNC()
			}

		then:
			machineCode[0] == 0xD0 as byte
	}

	@Unroll
	"RNZ "() {
		when:
			byte[] machineCode = asm {
				RNZ()
			}

		then:
			machineCode[0] == 0xC0 as byte
	}

	@Unroll
	"RP "() {
		when:
			byte[] machineCode = asm {
				RP()
			}

		then:
			machineCode[0] == 0xF0 as byte
	}

	@Unroll
	"RPE "() {
		when:
			byte[] machineCode = asm {
				RPE()
			}

		then:
			machineCode[0] == 0xE8 as byte
	}

	@Unroll
	"RPO "() {
		when:
			byte[] machineCode = asm {
				RPO()
			}

		then:
			machineCode[0] == 0xE0 as byte
	}

	@Unroll
	"RRC "() {
		when:
			byte[] machineCode = asm {
				RRC()
			}

		then:
			machineCode[0] == 0x0F as byte
	}

	@Unroll
	"RST "() {
		when:
			byte[] machineCode = asm {
				RST(addressCode)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			addressCode | opcode
			0           | 0xC7
			1           | 0xCF
			2           | 0xD7
			3           | 0xDF
			4           | 0xE7
			5           | 0xEF
			6           | 0xF7
			7           | 0xFF
	}

	@Unroll
	"RZ "() {
		when:
			byte[] machineCode = asm {
				RZ()
			}

		then:
			machineCode[0] == 0xC8 as byte
	}

	@Unroll
	"SBB #reg"() {
		when:
			byte[] machineCode = asm {
				SBB(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x9F
			B   | 0x98
			C   | 0x99
			D   | 0x9A
			E   | 0x9B
			H   | 0x9C
			L   | 0x9D
			M   | 0x9E
	}

	@Unroll
	"SBI #value"() {
		when:
			byte[] machineCode = asm {
				SBI(value)
			}

		then:
			machineCode[0] == 0xDE as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"SHLD #address"() {
		when:
			byte[] machineCode = asm {
				SHLD(address)
			}

		then:
			machineCode[0] == 0x22 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"SIM "() {
		when:
			byte[] machineCode = asm {
				SIM()
			}

		then:
			machineCode[0] == 0x30 as byte
	}

	@Unroll
	"SPHL "() {
		when:
			byte[] machineCode = asm {
				SPHL()
			}

		then:
			machineCode[0] == 0xF9 as byte
	}

	@Unroll
	"STA #address"() {
		when:
			byte[] machineCode = asm {
				STA(address)
			}

		then:
			machineCode[0] == 0x32 as byte
			machineCode[1] == (address & 0xff) as byte
			machineCode[2] == ((address >>> 8) & 0xff) as byte

		where:
			address << [0, 1, 0xfffe, 0xffff]
	}

	@Unroll
	"STAX #reg"() {
		when:
			byte[] machineCode = asm {
				STAX(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			B   | 0x02
			D   | 0x12
	}

	@Unroll
	"STC "() {
		when:
			byte[] machineCode = asm {
				STC()
			}

		then:
			machineCode[0] == 0x37 as byte
	}

	@Unroll
	"SUB #reg"() {
		when:
			byte[] machineCode = asm {
				SUB(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0x97
			B   | 0x90
			C   | 0x91
			D   | 0x92
			E   | 0x93
			H   | 0x94
			L   | 0x95
			M   | 0x96
	}

	@Unroll
	"SUI #value"() {
		when:
			byte[] machineCode = asm {
				SUI(value)
			}

		then:
			machineCode[0] == 0xD6 as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"XCHG "() {
		when:
			byte[] machineCode = asm {
				XCHG()
			}

		then:
			machineCode[0] == 0xEB as byte
	}

	@Unroll
	"XRA #reg"() {
		when:
			byte[] machineCode = asm {
				XRA(reg)
			}

		then:
			machineCode[0] == opcode as byte

		where:
			reg | opcode
			A   | 0xAF
			B   | 0xA8
			C   | 0xA9
			D   | 0xAA
			E   | 0xAB
			H   | 0xAC
			L   | 0xAD
			M   | 0xAE
	}

	@Unroll
	"XRI #value"() {
		when:
			byte[] machineCode = asm {
				XRI(value)
			}

		then:
			machineCode[0] == 0xEE as byte
			machineCode[1] == value as byte

		where:
			value << [0, 1, 0xff, -1, -128]
	}

	@Unroll
	"XTHL "() {
		when:
			byte[] machineCode = asm {
				XTHL()
			}

		then:
			machineCode[0] == 0xE3 as byte
	}
}
