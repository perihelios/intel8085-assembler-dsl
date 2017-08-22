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
import groovy.transform.PackageScope

import static NumericFormatters.toCompressedBinary
import static NumericFormatters.toCompressedHex
import static NumericFormatters.toCompressedOctal

@PackageScope
class NumericOperandValidators {
	static void validateD3(long value) {
		if (value < 0 || value > 7) {
			String hex = toCompressedHex(value, 0xf)
			String octal = toCompressedOctal(value, 077)
			String binary = toCompressedBinary(value, 0b1111)

			throw new OverflowException(
				"Operand value must be from 0 to 7; got $value ($hex, $octal, $binary)"
			)
		}
	}

	static void validateD8(long value) {
		if (value < -128 || value > 255) {
			String hex = toCompressedHex(value, 0xfff)
			String octal = toCompressedOctal(value, 0777)
			String binary = toCompressedBinary(value, 0b111111111)

			throw new OverflowException(
				"Operand value must be from -128 to 255; got $value ($hex, $octal, $binary)"
			)
		}
	}

	static void validateP8(long value) {
		if (value < 0 || value > 255) {
			String hex = toCompressedHex(value, 0xfff)
			String octal = toCompressedOctal(value, 0777)
			String binary = toCompressedBinary(value, 0b111111111)

			throw new OverflowException(
				"Operand value must be from 0 to 255; got $value ($hex, $octal, $binary)"
			)
		}
	}

	static void validateA16(long value) {
		if (value < 0 || value > 65535) {
			String hex = toCompressedHex(value, 0xfffff)
			String octal = toCompressedOctal(value, 0777777)
			String binary = toCompressedBinary(value, 0b11111111111111111)

			throw new OverflowException(
				"Operand value must be from 0 to 65535; got $value ($hex, $octal, $binary)"
			)
		}
	}

	static void validateD16(long value) {
		if (value < -32768 || value > 65535) {
			String hex = toCompressedHex(value, 0xfffff)
			String octal = toCompressedOctal(value, 0777777)
			String binary = toCompressedBinary(value, 0b11111111111111111)

			throw new OverflowException(
				"Operand value must be from -32768 to 65535; got $value ($hex, $octal, $binary)"
			)
		}
	}
}
