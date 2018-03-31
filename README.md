# Intel 8080/8085 Macro Assembler
A Groovy DSL that assembles Intel 8080 or 8085 mnemonics into machine code.

## Overview
The Intel 8080 and 8085 (which are nearly identical in terms of instruction set)
are 8-bit microprocessors with 64K of memory addressability. This macro
assembler allows you to create machine code for these processors in Groovy,
using a domain-specific language (DSL).

## Example
This is a simple (and nonsensical) example showing the use of a handful of
mnemonics and registers; data directives; declaration and referencing of labels;
and a macro declaration and invocation:

```
asm {
    macro("ADDI") { reg, value ->
        PUSH(PSW)
        MOV(A, reg)
        ADI(value)
        MOV(reg, A)
        POP(PSW)
    }

    DB(10)
    DW(0x1234)
    DS(200)

    start
        MVI(B, 17)
        LDA(0x1726)
        ADD(B)
        CPI(25)
        JZ(end)
        PUSH(PSW)
        PUSH(H)
        RST(6)
        ADDI(E, 0x23)
        CMP(B)
        JPO($i + 6)
        CALL(0x908)
    end RST(0)
}
```

## Technical Details
Several distinct features comprise the assembler.

### Invocation
The assembler is invoked by passing a closure to the `asm` method of the class
[Intel8085AssemblerDsl](assembler/src/main/groovy/com/perihelios/experimental/intel8085dsl/Intel8085AssemblerDsl.groovy).
If you're not familiar with Groovy or closures, don't worry; all you need to
know is the basic the syntax to invoke the assembler:

```
import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm

asm {
    // Assembly language
}
```

Inside the `asm` block, you will add assembly instructions, labels, and macros.

#### Assembly Parameters
The `asm` method optionally takes some parameters that control the assembly
process.

|Parameter|Allowed Values|Default|Description|
|---|---|---|---|
|`target`|`i8080`, `i8085`|`i8085`|Target processor for which to assemble (use constants from class [ProcessorTarget](assembler/src/main/groovy/com/perihelios/experimental/intel8085dsl/ProcessorTarget.groovy))|
|`size`|1-65536|Use `autoSize`|Size, in bytes, of buffer into which machine code assembled (parameter must not be specified if `autoSize` specified)|
|`autoSize`|`true`, `false`|`true`|Automatically produce machine code buffer of exact size needed for given instructions (parameter must not be specified if `size` specified)|
|`autoHalt`|`true`, `false`|`true`|Automatically add HLT instruction at end of machine code buffer|

Parameters are passed in an
[AssemblerParameters](assembler/src/main/groovy/com/perihelios/experimental/intel8085dsl/AssemblerParameters.groovy)
instance:

```
asm(new AssemblerParameters(target: i8080, size: 256, autoHalt: false)) {
    // Assembly language
}
```

### Instructions
The Intel 8085 has 80 instructions, and the 8080 has 78. Both processors have
the same seven 8-bit registers, plus a set of flags. The 8085 has an additional
three-bit interrupt mask, accessed through the RIM and SIM instructions.

#### Registers

There are seven registers, each 8 bits in size:

* A
* B
* C
* D
* E
* H
* L

The A register is the *accumulator*, and is special: It is used for nearly all
arithmetic-logic instructions.

#### Instruction Summary

For more information, see the book *Intel 8080/8085 Assembly Language
Programming*, available online as a scanned
[PDF](https://ia801904.us.archive.org/26/items/bitsavers_intel80859mblyLanguageProgrammingManualNov78_5034151/9800301C_8080_8085_Assembly_Language_Programming_Manual_Nov78.pdf),
or
[text](https://archive.org/stream/bitsavers_intel80859mblyLanguageProgrammingManualNov78_5034151/9800301C_8080_8085_Assembly_Language_Programming_Manual_Nov78_djvu.txt)
that has been processed with OCR (contains errors).

|Mnemonic|Operands\*|Description|Notes|
|---|---|---|---|
|`ACI`|`D8`|Add immediate with carry to accumulator||
|`ADC`|`REGM8`|Add register with carry to accumulator||
|`ADD`|`REGM8`|Add register to accumulator||
|`ADI`|`D8`|Add immediate to accumulator||
|`ANA`|`REGM8`|Boolean AND register with accumulator||
|`ANI`|`D8`|Boolean AND immediate with accumulator||
|`CALL`|`A16`|Call subroutine||
|`CC`|`A16`|Call subroutine if carry||
|`CM`|`A16`|Call subroutine if minus||
|`CMA`||Complement (Boolean NOT) accumulator||
|`CMC`||Complement (Boolean NOT) carry flag||
|`CMP`|`REGM8`|Compare register with accumulator||
|`CNC`|`A16`|Call subroutine if no carry||
|`CNZ`|`A16`|Call subroutine if not zero||
|`CP`|`A16`|Call subroutine if positive||
|`CPE`|`A16`|Call subroutine if parity even||
|`CPI`|`D8`|Compare immediate with accumulator||
|`CPO`|`A16`|Call subroutine if parity odd||
|`CZ`|`A16`|Call subroutine if zero||
|`DAA`||Decimal adjust accumulator (for BCD)||
|`DAD`|`REG16`|Add register pair to HL||
|`DCR`|`REGM8`|Decrement register||
|`DCX`|`REG16`|Decrement register pair||
|`DI`||Disable interrupts||
|`EI`||Enable interrupts||
|`HLT`||Halt||
|`IN`|`P8`|Input from I/O port||
|`INR`|`REGM8`|Increment register||
|`INX`|`REG16`|Increment register pair||
|`JC`|`A16`|Jump if carry||
|`JM`|`A16`|Jump if minus||
|`JMP`|`A16`|Jump||
|`JNC`|`A16`|Jump if no carry||
|`JNZ`|`A16`|Jump if not zero||
|`JP`|`A16`|Jump if positive||
|`JPE`|`A16`|Jump if parity even||
|`JPO`|`A16`|Jump if parity odd||
|`JZ`|`A16`|Jump if zero||
|`LDA`|`A16`|Load accumulator from address||
|`LDAX`|`REGAX`|Load accumulator from address in register pair||
|`LHLD`|`A16`|Load HL from address||
|`LXI`|`REG16`, `D16`|Load immediate to register pair||
|`MOV`|`REGM8`, `REGM8`|Move (copy) from register to register|Both registers cannot be M|
|`MVI`|`REGM8`, `D8`|Move (copy) immediate to register||
|`NOP`||No operation|Used for padding, or timing loops|
|`ORA`|`REGM8`|Boolean OR register with accumulator||
|`ORI`|`D8`|Boolean OR immediate with accumulator||
|`OUT`|`P8`|Output to I/O port||
|`PCHL`||Copy HL to program counter|Effectively, jump to address in HL|
|`POP`|`REGP`|Pop stack into register pair||
|`PUSH`|`REGP`|Push register pair onto stack||
|`RAL`||Rotate accumulator left through carry||
|`RAR`||Rotate accumulator right through carry||
|`RC`||Return from subroutine if carry||
|`RET`||Return from subroutine||
|`RIM`||Read interrupt mask into accumulator|Only 8085|
|`RLC`||Rotate accumulator left||
|`RM`||Return from subroutine if minus||
|`RNC`||Return from subroutine if no carry||
|`RNZ`||Return from subroutine if not zero||
|`RP`||Return from subroutine if positive||
|`RPE`||Return from subroutine if parity even||
|`RPO`||Return from subroutine if parity odd||
|`RRC`||Rotate accumulator right||
|`RST`|`D3`|Restart||
|`RZ`||Return from subroutine if zero||
|`SBB`|`REGM8`|Subtract register with borrow from accumulator||
|`SBI`|`D8`|Subtract immediate with borrow from accumulator||
|`SHLD`|`A16`|Store HL to address||
|`SIM`||Set interrupt mask from accumulator|Only 8085|
|`SPHL`||Copy SP to HL||
|`STA`|`A16`|Store accumulator to address||
|`STAX`|`REGAX`|Store accumulator to address in register pair||
|`STC`||Set carry flag||
|`SUB`|`REGM8`|Subtract register from accumulator||
|`SUI`|`D8`|Subtract immediate from accumulator||
|`XCHG`||Exchange HL with DE||
|`XRA`|`REGM8`|Boolean XOR register with accumulator||
|`XRI`|`D8`|Boolean XOR immediate with accumulator||
|`XTHL`||Exchange value on top of stack with HL||

**\*Operand Types:**

|Symbol|Description|
|---|---|
|`A16`|Address (16-bit)|
|`D3`|3-bit immediate value (0-7)|
|`D8`|8-bit immediate value|
|`D16`|16-bit immediate value|
|`REG16`|One of the 16-bit register pairs identified by `B`, `D`, or `H`, or the register `SP`|
|`REGAX`|One of the 16-bit register pairs identified by `B` or `D`|
|`REGM8`|One of the 8-bit registers `A`, `B`, `C`, `D`, `E`, `H`, or `L`, or the special pseudoregister `M`, which accesses the 8-bit memory address in the `HL` register pair|
|`REGP`|One of the 16-bit register pairs identified by `B`, `D`, `H`, or `PSW`|
|`P8`|8-bit unsigned port number (0-255)|

### Directives
Directives are not directly assembled to instructions, but are used by the
assembler for other purposes, such as embedding data in data areas of the
program.

#### List of Directives

|Symbol|Operands|Description|
|---|---|---|
|`DB`|8-bit value|Stores the given value at the current location|
|`DW`|16-bit value|Stores the given value, with reversed byte order (little-endian), at the current location|
|`DS`|Size|Reserves a block of memory of the specified size at the current location|

### Current Location Pointer
The current location (offset, in bytes, from the beginning of the assembly
language program) is available as the special symbol `$i`. (This differs from
the `$` symbol traditionally used in Intel assemblers as a limitation of
Groovy—symbols in Groovy cannot be a lone dollar sign.) You may add or subtract
a literal number from the value.

```
LXI(H, $i)
JMP($i + 35)
CNC($i - 0x209)
```

The value represented by `$i` is 16 bits in size. To access the high- or
low-order bytes of this value, use `HIGH` or `LOW`, respectively:

```
MVI(A, HIGH($i))
ORI(LOW($i))
ANI(HIGH($i - 10) + 7)
```

### Labels
Labels represent the location (offset, in bytes, from the beginning of the
assembly language program) at which they are declared. They are declared by
placing them on a line by themselves, or in front of an instruction:

```
start
    MVI(A, 20)
end RST(7)
```

Labels must be all one word, composed of letters and numbers, and must begin
with a letter. Using indentation to make labels more visible is traditional, but
not required.

Instructions use labels to refer to a location by name, rather than using a
literal number. For example, the two JNZ instructions in this program are
identical:

```
loop
    ...
    DCR(C)
    JNZ(loop)
    JNZ(0) // identical to above
```

Here, `loop` is declared at location 0. However, if more instructions or data
were added above the `loop` declaration, its value would no longer be 0; it
would automatically adjust to represent the correct value for its new location:

```
    MVI(C, 20)
    AND(B)
loop
    ...
    DCR(C)
    JNZ(loop) // loop now represents the number 3
```

The value represented by a label is 16 bits in size. To access the high- or
low-order bytes of this value, use `HIGH` or `LOW`, respectively:

```
    XOR(A)
lbl
    MVI(A, HIGH(lbl))
    MVI(B, LOW(lbl))
```

You may add or subtract a literal number from a label:

```
start
    LXI(H, start + 10)
    MVI(B, HIGH(start - 17) + 5)
```

### Macros
Macros are subroutines called as part of the assembly process. They allow you to
write a sequence of instructions once, then insert those instructions at other
places in the program.

Macros are named, and are invoked when needed by using that name, just as if
they were instructions.

Let's start with a very simple example. We define a macro named `DBL` that will
double the value in register `A`, and then we invoke the macro twice in a simple
program:

```
macro("DBL") {
    ADD(A)
}

MVI(A, 7)
DBL
SUI(1)
DBL
```

This is assembled identically to:

```
MVI(A, 7)
ADD(A)
SUI(1)
ADD(A)
```

To make macros more flexible, you can define parameters that are used in the
instructions inside the macro. Here is an example macro, `ADDTOI`, that adds an
immediate value to any given register, not just `A`; flags will be set
appropriately from the addition:

```
macro("ADDTOI") { reg, value ->
    if (reg == A) {
        ADI(value)
    } else {
        def tempReg = (reg == B || reg == C) ? D : B

        PUSH(tempReg)    // Save values currently in registers needed for temp work
        MOV(tempReg, A)  // Save A
        MOV(A, reg)      // Put target register value in accumulator
        ADI(value)       // Add value
        MOV(reg, A)      // Store the value in the original register
        MOV(A, tempReg)  // Restore A
        POP(tempReg)     // Restore values to registers that were needed for temp work
    }
}

MVI(B, 17)
ADDTOI(B, 72)
```

If called with `A` as the register, `ADDTOI` just assembles a single `ADI`
instruction. If another register is passed, the macro outputs instructions that
wrangle registers and the stack to make it possible to do the addition in the
`A` register, as required by the CPU architecture, while still producing the end
result of adding to the specified register. No other registers are overwritten,
and the flags are preserved from the addition operation.

#### Labels in Macros
Labels in macros are always local. You cannot refer to a label outside the macro
body (for instance, a label in another macro or in the main program). Because of
this, there's no conflict in reusing names:

```
macro("DO_IT") {
    loop
        ...
        JNC(loop)
}

loop
    DO_IT
    JZ(loop)
```

There is no confusion of the two `loop` labels; the assembler uses the correct
address for the label in both the `JNC` and `JZ` instructions.

#### Macro Tips
Macros are extremely powerful. Here are some tips for using them.

1. At the top of the macro, carefully document what it does, including any
   registers or memory it modifies.
1. It's very easy to create a tangled, spaghetti-like mess in assembly language.
   Macros can help with that... or make it worse. Use them wisely to make your
   code easier to follow.
1. Unlike assembly-language subroutines, macros take up space everywhere they're
   invoked. They're like a powerful version of copy-and-paste of a bunch of
   instructions. It may be more appropriate to use a subroutine for a large
   series of instructions, instead of plopping a macro invocation in dozens of
   places throughout your code, especially if memory is tight.

## License
Apache 2.0 (see [LICENSE.txt](LICENSE.txt) and [NOTICE.txt](NOTICE.txt) for
details).
