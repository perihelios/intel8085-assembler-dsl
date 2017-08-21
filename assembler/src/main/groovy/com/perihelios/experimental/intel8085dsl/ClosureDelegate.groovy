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

@PackageScope
class ClosureDelegate {
	private final LabelManager labelManager
	private final ProcessorTarget target
	private final byte[] machineCode
	private int index

	ClosureDelegate(ProcessorTarget target, byte[] machineCode) {
		this.target = target
		this.machineCode = machineCode
		this.labelManager = new LabelManager()
	}

	final Register A = Register.A
	final Register B = Register.B
	final Register C = Register.C
	final Register D = Register.D
	final Register E = Register.E
	final Register H = Register.H
	final Register L = Register.L
	final Register M = Register.M
	final Register SP = Register.SP
	final Register PSW = Register.PSW

	AssemblerMethodReturn ACI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11001110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn ADC(Register reg) {
		machineCode[index++] = 0b10001000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn ADD(Register reg) {
		machineCode[index++] = 0b10000000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn ADI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11000110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn ANA(Register reg) {
		machineCode[index++] = 0b10100000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn ANI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11100110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn CALL(long address) {
		validateA16(address)

		machineCode[index++] = 0b11001101
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11011100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CM(long address) {
		validateA16(address)

		machineCode[index++] = 0b11111100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CMA() {
		machineCode[index++] = 0b00101111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn CMC() {
		machineCode[index++] = 0b00111111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn CMP(Register reg) {
		machineCode[index++] = 0b10111000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn CNC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11010100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CNZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11110100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CPE(long address) {
		validateA16(address)

		machineCode[index++] = 0b11101100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CPI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11111110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn CPO(long address) {
		validateA16(address)

		machineCode[index++] = 0b11100100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn CZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11001100
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn DAA() {
		machineCode[index++] = 0b00100111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn DAD(Register reg) {
		machineCode[index++] = 0b00001001 | (reg.reg16 << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn DCR(Register reg) {
		machineCode[index++] = 0b00000101 | (reg.reg8 << 3)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn DCX(Register reg) {
		machineCode[index++] = 0b00001011 | (reg.reg16 << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn DI() {
		machineCode[index++] = 0b11110011
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn EI() {
		machineCode[index++] = 0b11111011
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn HLT() {
		machineCode[index++] = 0b01110110
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn IN(long port) {
		validateP8(port)

		machineCode[index++] = 0b11011011
		machineCode[index++] = port
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn INR(Register reg) {
		machineCode[index++] = 0b00000100 | (reg.reg8 << 3)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn INX(Register reg) {
		machineCode[index++] = 0b00000011 | (reg.reg16 << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn JC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11011010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JM(long address) {
		validateA16(address)

		machineCode[index++] = 0b11111010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JMP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000011
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JNC(long address) {
		validateA16(address)

		machineCode[index++] = 0b11010010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JNZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11000010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JP(long address) {
		validateA16(address)

		machineCode[index++] = 0b11110010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JPE(long address) {
		validateA16(address)

		machineCode[index++] = 0b11101010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JPO(long address) {
		validateA16(address)

		machineCode[index++] = 0b11100010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn JZ(long address) {
		validateA16(address)

		machineCode[index++] = 0b11001010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn LDA(long address) {
		validateA16(address)

		machineCode[index++] = 0b00111010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn LDAX(Register reg) {
		machineCode[index++] = 0b00001010 | (reg.ax << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn LHLD(long address) {
		validateA16(address)

		machineCode[index++] = 0b00101010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn LXI(Register reg, long value) {
		validateD16(value)

		machineCode[index++] = 0b00000001 | (reg.reg16 << 4)
		machineCode[index++] = value & 0xff
		machineCode[index++] = (value >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn MOV(Register dest, Register src) {
		if (dest == M && src == M) throw new InvalidRegisterException("Source and destination cannot both be M")

		machineCode[index++] = 0b01000000 | (dest.reg8 << 3) | src.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn MVI(Register reg, long value) {
		validateD8(value)

		machineCode[index++] = 0b00000110 | (reg.reg8 << 3)
		machineCode[index++] = value & 0xff
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn NOP() {
		machineCode[index++] = 0b00000000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn ORA(Register reg) {
		machineCode[index++] = 0b10110000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn ORI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11110110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn OUT(long port) {
		validateP8(port)

		machineCode[index++] = 0b11010011
		machineCode[index++] = port
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn PCHL() {
		machineCode[index++] = 0b11101001
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn POP(Register reg) {
		machineCode[index++] = 0b11000001 | (reg.pushPop << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn PUSH(Register reg) {
		machineCode[index++] = 0b11000101 | (reg.pushPop << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RAL() {
		machineCode[index++] = 0b00010111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RAR() {
		machineCode[index++] = 0b00011111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RC() {
		machineCode[index++] = 0b11011000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RET() {
		machineCode[index++] = 0b11001001
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RIM() {
		if (target != i8085) {
			throw new InvalidInstructionForTargetException("RIM instruction cannot be used with $target target")
		}

		machineCode[index++] = 0b00100000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RLC() {
		machineCode[index++] = 0b00000111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RM() {
		machineCode[index++] = 0b11111000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RNC() {
		machineCode[index++] = 0b11010000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RNZ() {
		machineCode[index++] = 0b11000000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RP() {
		machineCode[index++] = 0b11110000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RPE() {
		machineCode[index++] = 0b11101000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RPO() {
		machineCode[index++] = 0b11100000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RRC() {
		machineCode[index++] = 0b00001111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RST(long addressCode) {
		validateD3(addressCode)

		machineCode[index++] = 0b11000111 | (addressCode << 3)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn RZ() {
		machineCode[index++] = 0b11001000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn SBB(Register reg) {
		machineCode[index++] = 0b10011000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn SBI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11011110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn SHLD(long address) {
		validateA16(address)

		machineCode[index++] = 0b00100010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn SIM() {
		if (target != i8085) {
			throw new InvalidInstructionForTargetException("SIM instruction cannot be used with $target target")
		}

		machineCode[index++] = 0b00110000
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn SPHL() {
		machineCode[index++] = 0b11111001
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn STA(long address) {
		validateA16(address)

		machineCode[index++] = 0b00110010
		machineCode[index++] = address & 0xff
		machineCode[index++] = (address >>> 8) & 0xff
		new AssemblerMethodReturn(3)
	}

	AssemblerMethodReturn STAX(Register reg) {
		machineCode[index++] = 0b00000010 | (reg.ax << 4)
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn STC() {
		machineCode[index++] = 0b00110111
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn SUB(Register reg) {
		machineCode[index++] = 0b10010000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn SUI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11010110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn XCHG() {
		machineCode[index++] = 0b11101011
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn XRA(Register reg) {
		machineCode[index++] = 0b10101000 | reg.reg8
		new AssemblerMethodReturn(1)
	}

	AssemblerMethodReturn XRI(long value) {
		validateD8(value)

		machineCode[index++] = 0b11101110
		machineCode[index++] = value
		new AssemblerMethodReturn(2)
	}

	AssemblerMethodReturn XTHL() {
		machineCode[index++] = 0b11100011
		new AssemblerMethodReturn(1)
	}

	int get$i() {
		return index
	}

	@PackageScope
	void finish() {
		labelManager.applyReferences(machineCode)
	}

	def propertyMissing(String name) {
		Label label = labelManager[name]
		label.encounter(index)

		return label
	}

	def methodMissing(String name, def args) {
		MetaMethod method = this.metaClass.methods.find { it.name == name.toUpperCase() }
		List argList = args as List
		Label label = argList.find { it instanceof Label } as Label

		Class<?>[] retypedArgs = args.collect {
			Class<?> type = it.getClass()
			Label.isAssignableFrom(type) ? long : type
		} as Class[]

		if (method && label && method.isValidMethod(retypedArgs)) {
			labelManager.addReference(index, label)
			method.invoke(this, args.collect {
				it instanceof Label ? 0L : it
			} as Object[])
		} else if (!method && argList[0] instanceof AssemblerMethodReturn) {
			label = labelManager[name]
			AssemblerMethodReturn asmReturn = argList[0] as AssemblerMethodReturn
			label.encounter(index - asmReturn.bytesUsed)
		}
	}

	static long HIGH(long value) {
		(value >>> 8) & 0xff
	}

	static Label HIGH(Label label) {
		new HighLabel(label)
	}

	static long LOW(long value) {
		value & 0xff
	}

	static Label LOW(Label label) {
		new LowLabel(label)
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
