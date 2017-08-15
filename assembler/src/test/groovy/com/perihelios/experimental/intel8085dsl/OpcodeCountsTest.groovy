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

import static Intel8085AssemblerDsl.asm
import static Intel8085AssemblerDsl.getAUTO_SIZE
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8085

class OpcodeCountsTest extends Specification {
	@Unroll
	"Opcode counts match (using #undefined8 for values)"() {
		when:
			byte[] machineCode = asm(i8085, AUTO_SIZE, false) {
				ACI(undefined8)
				ADC(A)
				ADC(B)
				ADC(C)
				ADC(D)
				ADC(E)
				ADC(H)
				ADC(L)
				ADC(M)
				ADD(A)
				ADD(B)
				ADD(C)
				ADD(D)
				ADD(E)
				ADD(H)
				ADD(L)
				ADD(M)
				ADI(undefined8)
				ANA(A)
				ANA(B)
				ANA(C)
				ANA(D)
				ANA(E)
				ANA(H)
				ANA(L)
				ANA(M)
				ANI(undefined8)
				CALL(undefined16)
				CC(undefined16)
				CM(undefined16)
				CMA()
				CMC()
				CMP(A)
				CMP(B)
				CMP(C)
				CMP(D)
				CMP(E)
				CMP(H)
				CMP(L)
				CMP(M)
				CNC(undefined16)
				CNZ(undefined16)
				CP(undefined16)
				CPE(undefined16)
				CPI(undefined8)
				CPO(undefined16)
				CZ(undefined16)
				DAA()
				DAD(B)
				DAD(D)
				DAD(H)
				DAD(SP)
				DCR(A)
				DCR(B)
				DCR(C)
				DCR(D)
				DCR(E)
				DCR(H)
				DCR(L)
				DCR(M)
				DCX(B)
				DCX(D)
				DCX(H)
				DCX(SP)
				DI()
				EI()
				HLT()
				IN(undefined8)
				INR(A)
				INR(B)
				INR(C)
				INR(D)
				INR(E)
				INR(H)
				INR(L)
				INR(M)
				INX(B)
				INX(D)
				INX(H)
				INX(SP)
				JC(undefined16)
				JM(undefined16)
				JMP(undefined16)
				JNC(undefined16)
				JNZ(undefined16)
				JP(undefined16)
				JPE(undefined16)
				JPO(undefined16)
				JZ(undefined16)
				LDA(undefined16)
				LDAX(B)
				LDAX(D)
				LHLD(undefined16)
				LXI(B, undefined16)
				LXI(D, undefined16)
				LXI(H, undefined16)
				LXI(SP, undefined16)
				MOV(A, A)
				MOV(A, B)
				MOV(A, C)
				MOV(A, D)
				MOV(A, E)
				MOV(A, H)
				MOV(A, L)
				MOV(A, M)
				MOV(B, A)
				MOV(B, B)
				MOV(B, C)
				MOV(B, D)
				MOV(B, E)
				MOV(B, H)
				MOV(B, L)
				MOV(B, M)
				MOV(C, A)
				MOV(C, B)
				MOV(C, C)
				MOV(C, D)
				MOV(C, E)
				MOV(C, H)
				MOV(C, L)
				MOV(C, M)
				MOV(D, A)
				MOV(D, B)
				MOV(D, C)
				MOV(D, D)
				MOV(D, E)
				MOV(D, H)
				MOV(D, L)
				MOV(D, M)
				MOV(E, A)
				MOV(E, B)
				MOV(E, C)
				MOV(E, D)
				MOV(E, E)
				MOV(E, H)
				MOV(E, L)
				MOV(E, M)
				MOV(H, A)
				MOV(H, B)
				MOV(H, C)
				MOV(H, D)
				MOV(H, E)
				MOV(H, H)
				MOV(H, L)
				MOV(H, M)
				MOV(L, A)
				MOV(L, B)
				MOV(L, C)
				MOV(L, D)
				MOV(L, E)
				MOV(L, H)
				MOV(L, L)
				MOV(L, M)
				MOV(M, A)
				MOV(M, B)
				MOV(M, C)
				MOV(M, D)
				MOV(M, E)
				MOV(M, H)
				MOV(M, L)
				MVI(A, undefined8)
				MVI(B, undefined8)
				MVI(C, undefined8)
				MVI(D, undefined8)
				MVI(E, undefined8)
				MVI(H, undefined8)
				MVI(L, undefined8)
				MVI(M, undefined8)
				NOP()
				ORA(A)
				ORA(B)
				ORA(C)
				ORA(D)
				ORA(E)
				ORA(H)
				ORA(L)
				ORA(M)
				ORI(undefined8)
				OUT(undefined8)
				PCHL()
				POP(B)
				POP(D)
				POP(H)
				POP(PSW)
				PUSH(B)
				PUSH(D)
				PUSH(H)
				PUSH(PSW)
				RAL()
				RAR()
				RC()
				RET()
				RIM()
				RLC()
				RM()
				RNC()
				RNZ()
				RP()
				RPE()
				RPO()
				RRC()
				RST(0)
				RST(1)
				RST(2)
				RST(3)
				RST(4)
				RST(5)
				RST(6)
				RST(7)
				RZ()
				SBB(A)
				SBB(B)
				SBB(C)
				SBB(D)
				SBB(E)
				SBB(H)
				SBB(L)
				SBB(M)
				SBI(undefined8)
				SHLD(undefined16)
				SIM()
				SPHL()
				STA(undefined16)
				STAX(B)
				STAX(D)
				STC()
				SUB(A)
				SUB(B)
				SUB(C)
				SUB(D)
				SUB(E)
				SUB(H)
				SUB(L)
				SUB(M)
				SUI(undefined8)
				XCHG()
				XRA(A)
				XRA(B)
				XRA(C)
				XRA(D)
				XRA(E)
				XRA(H)
				XRA(L)
				XRA(M)
				XRI(undefined8)
				XTHL()
			}

			Map<Byte, Integer> counts = (machineCode as List<Byte>).countBy { it }

		then:
			counts.size() == 247
			counts.findAll { key, value -> value != 1 } == [(undefined8 as byte): 70]

		where:
			undefined8 | undefined16
			0xFD       | 0xFDFD
			0x08       | 0x0808
	}
}
