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

import com.perihelios.experimental.intel8085dsl.exceptions.DuplicateLabelException
import com.perihelios.experimental.intel8085dsl.exceptions.UndefinedLabelException
import groovy.transform.PackageScope

@PackageScope
class BasicLabel implements Label {
	final String name

	private final Map<Integer, Integer> unreferencedEncounterCounts = new HashMap<>(8)

	BasicLabel(String name) {
		this.name = name
	}

	@Override
	void encounter(int offset) {
		unreferencedEncounterCounts.compute(offset, { _, value ->
			value == null ? 1 : value + 1
		})
	}

	@Override
	void reference(int offset) {
		adjustUnreferencedEncounters(offset)
	}

	@Override
	int getOffset() {
		switch (unreferencedEncounterCounts.values().sum()) {
			case 0:
			case null:
				throw new UndefinedLabelException("Reference made to undefined label $name")
			case 1:
				return unreferencedEncounterCounts.keySet()[0]
			default:
				throw new DuplicateLabelException("Duplicate definitions of label $name")
		}
	}

	@Override
	int getByteCount() {
		2
	}

	@Override
	Label plus(int offset) {
		return new OffsetLabel(this, offset)
	}

	@Override
	Label minus(int offset) {
		return new OffsetLabel(this, -offset)
	}

	private void adjustUnreferencedEncounters(int offset) {
		Integer unreferencedEncounterCount = unreferencedEncounterCounts[offset]
		if (unreferencedEncounterCount != null) {
			unreferencedEncounterCount -= 1

			if (unreferencedEncounterCount == 0) {
				unreferencedEncounterCounts.remove(offset)
			} else {
				unreferencedEncounterCounts[offset] = unreferencedEncounterCount
			}
		}
	}
}
