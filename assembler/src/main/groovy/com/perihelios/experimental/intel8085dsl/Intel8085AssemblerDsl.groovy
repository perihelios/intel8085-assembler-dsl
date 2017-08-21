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

import com.perihelios.experimental.intel8085dsl.exceptions.InvalidRegisterException

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8080
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.ProcessorTarget.i8085

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
		@DelegatesTo(ClosureDelegate) Closure body
	) {
		if (target == null) throw new IllegalArgumentException("Target must be either $i8080 or $i8085; got null")
		if (bytes > 65536 || (bytes < 1 && bytes != AUTO_SIZE)) {
			throw new IllegalArgumentException(
				"Bytes must be from 1-65536, or AUTO_SIZE (magic number: $AUTO_SIZE); got " + bytes
			)
		}

		byte[] machineCode = new byte[bytes == AUTO_SIZE ? 65536 : bytes]
		ClosureDelegate delegate = new ClosureDelegate(target, machineCode)
		body.delegate = delegate
		body()
		delegate.finish()

		if (autoHalt) {
			delegate.HLT()
		}

		if (bytes == AUTO_SIZE) {
			return Arrays.copyOf(machineCode, delegate.$i)
		} else {
			return machineCode
		}
	}
}
