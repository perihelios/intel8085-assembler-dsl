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

class LabelManager {
	private final Map<String, Label> labels = new LinkedHashMap<>(256)
	private final List<Reference> references = new ArrayList<>(1024)

	Label getAt(String name) {
		labels.computeIfAbsent(name, { new BasicLabel(name) })
	}

	List<Reference> addReference(int offset, Label label) {
		label.reference(offset)
		references << new Reference(offset, label)
	}

	void applyReferences(byte[] machineCode) {
		references*.apply(machineCode)
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
