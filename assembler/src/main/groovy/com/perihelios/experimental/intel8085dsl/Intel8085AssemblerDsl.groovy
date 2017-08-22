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

import static groovy.lang.Closure.DELEGATE_FIRST

class Intel8085AssemblerDsl {
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
		}

		return machineCode
	}
}
