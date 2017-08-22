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

import static groovy.lang.Closure.DELEGATE_FIRST

class Intel8085AssemblerDsl {
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
		AssemblerParameters params = new AssemblerParameters(),
		@DelegatesTo(ClosureDelegate) Closure body
	) {
		params.validate()

		byte[] machineCode = new byte[params.autoSize ? 65536 : params.size]
		ClosureDelegate delegate = new ClosureDelegate(params.target, machineCode)
		body.delegate = delegate
		body.resolveStrategy = DELEGATE_FIRST
		body()
		delegate.finish()

		if (params.autoHalt) {
			delegate.HLT()
		}

		if (params.autoSize) {
			return Arrays.copyOf(machineCode, delegate.index)
		} else {
			return machineCode
		}
	}
}
