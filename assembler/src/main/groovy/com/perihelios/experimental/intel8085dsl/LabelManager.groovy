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
class LabelManager {
	private final Deque<Map<String, Label>> labels = new ArrayDeque<>()
	private final Deque<List<Reference>> references = new ArrayDeque<>()
	private final List<Reference> allReferences = new ArrayList<>(1024)

	LabelManager() {
		push()
	}

	Label getAt(String name) {
		labels.peek().computeIfAbsent(name, { new BasicLabel(name) })
	}

	List<Reference> addReference(int offset, Label label) {
		label.reference(offset)
		Reference ref = new Reference(offset, label)
		references.peek() << ref
		allReferences << ref
	}

	void applyReferences(byte[] machineCode) {
		references.peek()*.apply(machineCode)
	}

	void push() {
		labels.push(new LinkedHashMap<>(256))
		references.push(new ArrayList<>(1024))
	}

	void pop() {
		labels.pop()
		references.pop()
	}

	private static class Reference {
		private static final int OPCODE_SKIP_BYTES = 1

		protected final int offset
		protected final Label label

		Reference(int offset, Label label) {
			this.offset = offset
			this.label = label
		}

		void apply(byte[] machineCode) {
			int labelOffset = label.offset

			for (int i = 0; i < label.byteCount; i++) {
				machineCode[offset + OPCODE_SKIP_BYTES + i] = labelOffset & 0xff
				labelOffset >>>= 8
			}
		}
	}
}
