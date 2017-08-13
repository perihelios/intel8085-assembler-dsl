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

import static Intel8085AssemblerDsl.ProcessorTarget.*
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.M

class Intel8085AssemblerDsl {
	static final int AUTO_SIZE = -1

	static enum ProcessorTarget {
		i8080, i8085
	}

	static enum Register {
		A(7, -1, -1, -1),
		B(0, 0, 0, 0),
		C(1, -1, -1, -1),
		D(2, 1, 1, 1),
		E(3, -1, -1, -1),
		H(4, 2, 2, -1),
		L(5, -1, -1, -1),
		M(6, -1, -1, -1),
		SP(-1, 3, -1, -1),
		PSW(-1, -1, 3, -1)

		final int reg8
		final int reg16
		final int pushPop
		final int ax

		Register(int reg8, int reg16, int pushPop, int ax) {
			this.reg8 = reg8
			this.reg16 = reg16
			this.pushPop = pushPop
			this.ax = ax
		}

		int getReg8() {
			if (reg8 < 0) throw new InvalidRegisterException("Invalid register $this for instruction")

			return reg8
		}

		int getReg16() {
			if (reg16 < 0) throw new InvalidRegisterException("Invalid register $this for instruction")

			return reg16
		}

		int getPushPop() {
			if (pushPop < 0) throw new InvalidRegisterException("Invalid register $this for instruction")

			return pushPop
		}

		int getAx() {
			if (ax < 0) throw new InvalidRegisterException("Invalid register $this for instruction")

			return ax
		}
	}

	static byte[] asm(
		ProcessorTarget target = i8085,
		int bytes = AUTO_SIZE,
		boolean autoHalt = true,
		@DelegatesTo(Delegate) Closure body
	) {
		if (target == null) throw new IllegalArgumentException("Target must be either $i8080 or $i8085; got null")
		if (bytes > 65536 || (bytes < 1 && bytes != AUTO_SIZE)) {
			throw new IllegalArgumentException(
				"Bytes must be from 1-65536, or AUTO_SIZE (magic number: $AUTO_SIZE); got " + bytes
			)
		}

		Delegate delegate = new Delegate(target, bytes == AUTO_SIZE ? 65536 : bytes)
		body.delegate = delegate
		body()

		if (autoHalt) {
			delegate.HLT()
		}

		if (bytes == AUTO_SIZE) {
			return Arrays.copyOf(delegate.machineCode, delegate.index)
		} else {
			return delegate.machineCode
		}
	}

	static class Delegate {
		private final ProcessorTarget target
		private final byte[] machineCode
		private int index

		Delegate(ProcessorTarget target, int size) {
			this.target = target
			this.machineCode = new byte[size]
		}

		void ACI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11001110
			machineCode[index++] = value
		}

		void ADC(Register reg) {
			machineCode[index++] = 0b10001000 | reg.reg8
		}

		void ADD(Register reg) {
			machineCode[index++] = 0b10000000 | reg.reg8
		}

		void ADI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11000110
			machineCode[index++] = value
		}

		void ANA(Register reg) {
			machineCode[index++] = 0b10100000 | reg.reg8
		}

		void ANI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11100110
			machineCode[index++] = value
		}

		void CALL(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11001101
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CC(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11011100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CM(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11111100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CMA() {
			machineCode[index++] = 0b00101111
		}

		void CMC() {
			machineCode[index++] = 0b00111111
		}

		void CMP(Register reg) {
			machineCode[index++] = 0b10111000 | reg.reg8
		}

		void CNC(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11010100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CNZ(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11000100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CP(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11110100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CPE(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11101100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CPI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11111110
			machineCode[index++] = value
		}

		void CPO(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11100100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void CZ(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11001100
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void DAA() {
			machineCode[index++] = 0b00100111
		}

		void DAD(Register reg) {
			machineCode[index++] = 0b00001001 | (reg.reg16 << 4)
		}

		void DCR(Register reg) {
			machineCode[index++] = 0b00000101 | (reg.reg8 << 3)
		}

		void DCX(Register reg) {
			machineCode[index++] = 0b00001011 | (reg.reg16 << 4)
		}

		void DI() {
			machineCode[index++] = 0b11110011
		}

		void EI() {
			machineCode[index++] = 0b11111011
		}

		void HLT() {
			machineCode[index++] = 0b01110110
		}

		void IN(long port) {
			validate8BitValue(port)

			machineCode[index++] = 0b11011011
			machineCode[index++] = port
		}

		void INR(Register reg) {
			machineCode[index++] = 0b00000100 | (reg.reg8 << 3)
		}

		void INX(Register reg) {
			machineCode[index++] = 0b00000011 | (reg.reg16 << 4)
		}

		void JC(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11011010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JM(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11111010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JMP(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11000011
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JNC(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11010010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JNZ(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11000010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JP(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11110010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JPE(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11101010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JPO(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11100010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void JZ(long address) {
			validateAddress(address)

			machineCode[index++] = 0b11001010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void LDA(long address) {
			validateAddress(address)

			machineCode[index++] = 0b00111010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void LDAX(Register reg) {
			machineCode[index++] = 0b00001010 | (reg.ax << 4)
		}

		void LHLD(long address) {
			validateAddress(address)

			machineCode[index++] = 0b00101010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void LXI(Register reg, long value) {
			validate16BitValue(value)

			machineCode[index++] = 0b00000001 | (reg.reg16 << 4)
			machineCode[index++] = value & 0xff
			machineCode[index++] = (value >>> 8) & 0xff
		}

		void MOV(Register dest, Register src) {
			if (dest == M && src == M) throw new InvalidRegisterException("Source and destination cannot both be M")

			machineCode[index++] = 0b01000000 | (dest.reg8 << 3) | src.reg8
		}

		void MVI(Register reg, long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b00000110 | (reg.reg8 << 3)
			machineCode[index++] = value & 0xff
		}

		void NOP() {
			machineCode[index++] = 0b00000000
		}

		void ORA(Register reg) {
			machineCode[index++] = 0b10110000 | reg.reg8
		}

		void ORI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11110110
			machineCode[index++] = value
		}

		void OUT(long port) {
			validate8BitValue(port)

			machineCode[index++] = 0b11010011
			machineCode[index++] = port
		}

		void PCHL() {
			machineCode[index++] = 0b11101001
		}

		void POP(Register reg) {
			machineCode[index++] = 0b11000001 | (reg.pushPop << 4)
		}

		void PUSH(Register reg) {
			machineCode[index++] = 0b11000101 | (reg.pushPop << 4)
		}

		void RAL() {
			machineCode[index++] = 0b00010111
		}

		void RAR() {
			machineCode[index++] = 0b00011111
		}

		void RC() {
			machineCode[index++] = 0b11011000
		}

		void RET() {
			machineCode[index++] = 0b11001001
		}

		void RIM() {
			if (target != i8085) {
				throw new InvalidInstructionForTargetException("RIM instruction cannot be used with $target target")
			}

			machineCode[index++] = 0b00100000
		}

		void RLC() {
			machineCode[index++] = 0b00000111
		}

		void RM() {
			machineCode[index++] = 0b11111000
		}

		void RNC() {
			machineCode[index++] = 0b11010000
		}

		void RNZ() {
			machineCode[index++] = 0b11000000
		}

		void RP() {
			machineCode[index++] = 0b11110000
		}

		void RPE() {
			machineCode[index++] = 0b11101000
		}

		void RPO() {
			machineCode[index++] = 0b11100000
		}

		void RRC() {
			machineCode[index++] = 0b00001111
		}

		void RST(long addressCode) {
			validateRstAddressCode(addressCode)

			machineCode[index++] = 0b11000111 | (addressCode << 3)
		}

		void RZ() {
			machineCode[index++] = 0b11001000
		}

		void SBB(Register reg) {
			machineCode[index++] = 0b10011000 | reg.reg8
		}

		void SBI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11011110
			machineCode[index++] = value
		}

		void SHLD(long address) {
			validateAddress(address)

			machineCode[index++] = 0b00100010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void SIM() {
			if (target != i8085) {
				throw new InvalidInstructionForTargetException("SIM instruction cannot be used with $target target")
			}

			machineCode[index++] = 0b00110000
		}

		void SPHL() {
			machineCode[index++] = 0b11111001
		}

		void STA(long address) {
			validateAddress(address)

			machineCode[index++] = 0b00110010
			machineCode[index++] = address & 0xff
			machineCode[index++] = (address >>> 8) & 0xff
		}

		void STAX(Register reg) {
			machineCode[index++] = 0b00000010 | (reg.ax << 4)
		}

		void STC() {
			machineCode[index++] = 0b00110111
		}

		void SUB(Register reg) {
			machineCode[index++] = 0b10010000 | reg.reg8
		}

		void SUI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11010110
			machineCode[index++] = value
		}

		void XCHG() {
			machineCode[index++] = 0b11101011
		}

		void XRA(Register reg) {
			machineCode[index++] = 0b10101000 | reg.reg8
		}

		void XRI(long value) {
			validate8BitValue(value)

			machineCode[index++] = 0b11101110
			machineCode[index++] = value
		}

		void XTHL() {
			machineCode[index++] = 0b11100011
		}

		private static void validate8BitValue(long value) {
			String hex = null
			String octal = null
			String binary = null

			if (value > 0xff) {
				hex = Long.toString(value, 16)
				octal = Long.toString(value, 8)
				binary = Long.toString(value, 2)
			}

			if (value < 0) {
				if((value >>> 8) != 0xffffffffffffffL) {
					hex = Long.toUnsignedString(value, 16).replaceFirst(/^f+/, "f")
					octal = Long.toUnsignedString(value, 8).replaceFirst(/^17+/, "7")
					binary = Long.toUnsignedString(value, 2).replaceFirst(/^1+/, "1")
				} else if((value & 0x80) == 0) {
					hex = "1" + Long.toString(value & 0xff, 16).padLeft(2, '0')
					octal =
						Long.toString(((value & 0700) >>> 6) | 04, 8) +
						Long.toString((value & 070) >>> 3, 8) +
						Long.toString(value & 07, 8)
					binary = "1" + Long.toString(value & 0xff, 2).padLeft(8, '0')
				}
			}

			if (hex) {
				throw new OverflowException("Value $value (0x$hex, 0$octal, 0b$binary) is larger than 8 bits")
			}
		}

		private static void validate16BitValue(long value) {
		}

		private static void validateAddress(long address) {
//			if (address > 0xffff) {
//				throw new InvalidAddressException("Address $address ")
//			}
		}

		private static void validateRstAddressCode(long addressCode) {
		}
	}
}
