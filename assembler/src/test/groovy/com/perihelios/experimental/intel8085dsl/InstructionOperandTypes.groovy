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

class InstructionOperandTypes {
	static final SortedSet<String> NONE = [
		"CMA",
		"CMC",
		"DAA",
		"DI",
		"EI",
		"HLT",
		"NOP",
		"PCHL",
		"RAL",
		"RAR",
		"RC",
		"RET",
		"RIM",
		"RLC",
		"RM",
		"RNC",
		"RNZ",
		"RP",
		"RPE",
		"RPO",
		"RRC",
		"RZ",
		"SIM",
		"SPHL",
		"STC",
		"XCHG",
		"XTHL"
	] as TreeSet

	static final SortedSet<String> D3 = [
		"RST",
	] as TreeSet

	static final SortedSet<String> D8 = [
		"ACI",
		"ADI",
		"ANI",
		"CPI",
		"ORI",
		"SBI",
		"SUI",
		"XRI"
	] as TreeSet

	static final SortedSet<String> P8 = [
		"IN",
		"OUT",
	] as TreeSet

	static final SortedSet<String> A16 = [
		"CALL",
		"CC",
		"CM",
		"CNC",
		"CNZ",
		"CP",
		"CPE",
		"CPO",
		"CZ",
		"JC",
		"JM",
		"JMP",
		"JNC",
		"JNZ",
		"JP",
		"JPE",
		"JPO",
		"JZ",
		"LDA",
		"LHLD",
		"SHLD",
		"STA",
	] as TreeSet

	static final SortedSet<String> REGM8 = [
		"ADC",
		"ADD",
		"ANA",
		"CMP",
		"DCR",
		"INR",
		"ORA",
		"SBB",
		"SUB",
		"XRA",
	] as TreeSet

	static final SortedSet<String> REG16 = [
		"DAD",
		"DCX",
		"INX",
		"LDAX",
		"POP",
		"PUSH",
		"STAX",
	] as TreeSet

	static final SortedSet<String> REGM8_D8 = [
		"MVI",
	] as TreeSet

	static final SortedSet<String> REG16_D16 = [
		"LXI",
	] as TreeSet

	static final SortedSet<String> REGM8_REGM8 = [
		"MOV",
	] as TreeSet

	// Prevent instantiation
	private InstructionOperandTypes() {}
}
