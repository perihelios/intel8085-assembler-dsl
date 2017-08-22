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
class CurrentLocationLabel implements Label {
	final int offset

	CurrentLocationLabel(int offset) {
		this.offset = offset
	}

	@Override
	String getName() {
		'$'
	}

	@Override
	void encounter(int offset) {
		// Nothing to do
	}

	@Override
	void reference(int offset) {
		// Nothing to do
	}

	@Override
	int getByteCount() {
		2
	}

	@Override
	Label plus(int offset) {
		new OffsetLabel(this, offset)
	}

	@Override
	Label minus(int offset) {
		new OffsetLabel(this, -offset)
	}
}
