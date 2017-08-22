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
abstract class DelegatingLabel implements Label {
	protected final Label delegate

	DelegatingLabel(Label delegate) {
		this.delegate = delegate
	}

	@Override
	String getName() {
		return delegate.name
	}

	@Override
	void encounter(int offset) {
		delegate.encounter(offset)
	}

	@Override
	void reference(int offset) {
		delegate.reference(offset)
	}

	@Override
	int getByteCount() {
		delegate.byteCount
	}

	@Override
	Label plus(int offset) {
		return new OffsetLabel(this, offset)
	}

	@Override
	Label minus(int offset) {
		return new OffsetLabel(this, -offset)
	}
}
