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

import com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget
import com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register
import com.perihelios.experimental.intel8085dsl.exceptions.InvalidInstructionForTargetException
import com.perihelios.experimental.intel8085dsl.exceptions.InvalidRegisterException
import com.perihelios.experimental.intel8085dsl.exceptions.OverflowException
import groovy.transform.PackageScope

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8085
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.Register.M

@PackageScope
class ClosureDelegate {
	private final ProcessorTarget target
	private final byte[] machineCode
	private int index

	ClosureDelegate(ProcessorTarget target, byte[] machineCode) {
		this.target = target
		this.machineCode = machineCode
	}

	void ACI(long value) {
		validateD8(value)

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
		validateD8(value)

		machineCode[index++] = 0b11000110
		machineCode[index++] = value
	}

	void ANA(Register reg) {
		machineCode[index++] = 0b10100000 | reg.reg8
	}

	void ANI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11100110
		machineCode[index++] = value
	}

	void CALL(long address) {
		validateA16(address)

		machineCode[index++] = 0b11001101
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11011100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CM(long address) {
		validateA16(address)

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
		validateA16(address)

		machineCode[index++] = 0b11010100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CNZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11110100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CPE(long address) {
		validateA16(address)

		machineCode[index++] = 0b11101100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CPI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11111110
		machineCode[index++] = value
	}

	void CPO(long address) {
		validateA16(address)

		machineCode[index++] = 0b11100100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void CZ(long address) {
		validateA16(address)

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
		validateP8(port)

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
		validateA16(address)

		machineCode[index++] = 0b11011010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JM(long address) {
		validateA16(address)

		machineCode[index++] = 0b11111010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JMP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000011
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JNC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11010010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JNZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11110010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JPE(long address) {
		validateA16(address)

		machineCode[index++] = 0b11101010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JPO(long address) {
		validateA16(address)

		machineCode[index++] = 0b11100010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void JZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11001010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void LDA(long address) {
		validateA16(address)

		machineCode[index++] = 0b00111010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void LDAX(Register reg) {
		machineCode[index++] = 0b00001010 | (reg.ax << 4)
	}

	void LHLD(long address) {
		validateA16(address)

		machineCode[index++] = 0b00101010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
	}

	void LXI(Register reg, long value) {
		validateD16(value)

		machineCode[index++] = 0b00000001 | (reg.reg16 << 4)
		machineCode[index++] = value & 0xff
		machineCode[index++] = (value >>> 8) & 0xff
	}

	void MOV(Register dest, Register src) {
		if (dest == M && src == M) throw new InvalidRegisterException("Source and destination cannot both be M")

		machineCode[index++] = 0b01000000 | (dest.reg8 << 3) | src.reg8
	}

	void MVI(Register reg, long value) {
		validateD8(value)

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
		validateD8(value)

		machineCode[index++] = 0b11110110
		machineCode[index++] = value
	}

	void OUT(long port) {
		validateP8(port)

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
		validateD3(addressCode)

		machineCode[index++] = 0b11000111 | (addressCode << 3)
	}

	void RZ() {
		machineCode[index++] = 0b11001000
	}

	void SBB(Register reg) {
		machineCode[index++] = 0b10011000 | reg.reg8
	}

	void SBI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11011110
		machineCode[index++] = value
	}

	void SHLD(long address) {
		validateA16(address)

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
		validateA16(address)

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
		validateD8(value)

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
		validateD8(value)

		machineCode[index++] = 0b11101110
		machineCode[index++] = value
	}

	void XTHL() {
		machineCode[index++] = 0b11100011
	}

	int get$i() {
		return index
	}

	private static void validateD3(long value) {
		if (value < 0 || value > 7) {
			String hex = toCompressedHex(value, 0xf)
			String octal = toCompressedOctal(value, 077)
			String binary = toCompressedBinary(value, 0b1111)

			throw new OverflowException(
				"Operand value must be from 0 to 7; got $value ($hex, $octal, $binary)"
			)
		}
	}

	private static void validateD8(long value) {
		if (value < -128 || value > 255) {
			String hex = toCompressedHex(value, 0xfff)
			String octal = toCompressedOctal(value, 0777)
			String binary = toCompressedBinary(value, 0b111111111)

			throw new OverflowException(
				"Operand value must be from -128 to 255; got $value ($hex, $octal, $binary)"
			)
		}
	}

	private static void validateP8(long value) {
		if (value < 0 || value > 255) {
			String hex = toCompressedHex(value, 0xfff)
			String octal = toCompressedOctal(value, 0777)
			String binary = toCompressedBinary(value, 0b111111111)

			throw new OverflowException(
				"Operand value must be from 0 to 255; got $value ($hex, $octal, $binary)"
			)
		}
	}

	private static void validateA16(long value) {
		if (value < 0 || value > 65535) {
			String hex = toCompressedHex(value, 0xfffff)
			String octal = toCompressedOctal(value, 0777777)
			String binary = toCompressedBinary(value, 0b11111111111111111)

			throw new OverflowException(
				"Operand value must be from 0 to 65535; got $value ($hex, $octal, $binary)"
			)
		}
	}

	private static void validateD16(long value) {
		if (value < -32768 || value > 65535) {
			String hex = toCompressedHex(value, 0xfffff)
			String octal = toCompressedOctal(value, 0777777)
			String binary = toCompressedBinary(value, 0b11111111111111111)

			throw new OverflowException(
				"Operand value must be from -32768 to 65535; got $value ($hex, $octal, $binary)"
			)
		}
	}

	private static String toCompressedHex(long value, long minBitWidthMask) {
		if (value < 0) {
			long fMask = 0xf000000000000000L
			long negativeMask = 0x800000000000000L
			while ((fMask < 0 || fMask > minBitWidthMask) && (value & fMask) == fMask && (negativeMask & value) != 0) {
				value &= ~fMask
				fMask >>>= 4
				negativeMask >>>= 4
			}
		}

		return "0x" + Long.toUnsignedString(value, 16)
	}

	private static String toCompressedOctal(long value, long minBitWidthMask) {
		long negativeMask = 0400000000000000000000L

		if (value < 0 && (value & negativeMask) != 0) {
			value &= Long.MAX_VALUE
			negativeMask >>>= 3

			long fMask = 0700000000000000000000L
			while (fMask > minBitWidthMask && (value & fMask) == fMask && (negativeMask & value) != 0) {
				value &= ~fMask
				fMask >>>= 3
				negativeMask >>>= 3
			}
		}

		return "0" + Long.toUnsignedString(value, 8)
	}

	private static String toCompressedBinary(long value, long minBitWidthMask) {
		if (value < 0) {
			long fMask = 0b1000000000000000000000000000000000000000000000000000000000000000L
			long negativeMask = 0b100000000000000000000000000000000000000000000000000000000000000L
			while ((fMask < 0 || fMask > minBitWidthMask) && (value & fMask) == fMask && (negativeMask & value) != 0) {
				value &= ~fMask
				fMask >>>= 1
				negativeMask >>>= 1
			}
		}

		return "0b" + Long.toUnsignedString(value, 2)
	}
}
