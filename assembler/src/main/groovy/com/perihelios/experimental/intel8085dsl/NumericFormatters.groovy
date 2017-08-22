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

@PackageScope
class NumericFormatters {
	static String toCompressedHex(long value, long minBitWidthMask) {
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

	static String toCompressedOctal(long value, long minBitWidthMask) {
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

	static String toCompressedBinary(long value, long minBitWidthMask) {
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

	// Prevent instantiation of this class
	private NumericFormatters() {}
}
