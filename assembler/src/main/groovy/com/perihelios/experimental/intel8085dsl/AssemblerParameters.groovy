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

import groovy.transform.PackageScope

import static com.perihelios.experimental.intel8085dsl.ProcessorTarget.i8080
import static com.perihelios.experimental.intel8085dsl.ProcessorTarget.i8085

class AssemblerParameters {
	ProcessorTarget target = i8085
	int size
	boolean autoSize = true
	boolean autoHalt = true

	private boolean bytesSpecified = false
	private boolean autoSizeSpecified = false

	void setSize(int bytes) {
		this.size = bytes
		this.autoSize = false
		this.bytesSpecified = true
	}

	void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize
		this.autoSizeSpecified = true
	}

	@PackageScope
	void validate() {
		if (target == null) {
			throw new IllegalArgumentException("Target must be one of: $i8080, $i8085; got null")
		}

		if (autoSizeSpecified && bytesSpecified) {
			throw new IllegalArgumentException("Set either size or autoSize, not both")
		}

		if (!autoSize && !bytesSpecified) {
			throw new IllegalArgumentException("Must set size when not using autoSize")
		}

		if (!autoSize) {
			if (size > 65536 || size < 1) {
				throw new IllegalArgumentException(
					"Size must be from 1-65536; got " + size
				)
			}
		}
	}
}
