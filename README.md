# Intel 8080/8085 Macro Assembler
A Groovy DSL that assembles Intel 8080 or 8085 mnemonics into machine code.

## Overview
The Intel 8080 and 8085 (which are nearly identical in terms of instruction set)
are 8-bit microprocessors with 64K of memory addressability. This macro
assembler allows you to create machine code for these processors in Groovy,
using a domain-specific language (DSL).

## Example
This is a simple (and nonsensical) example showing the use of a handful of
mnemonics and registers; the use of labels; and a macro declaration and
invocation:

```
asm {
    macro("ADDI") { reg, value ->
        PUSH(PSW)
        MOV(A, reg)
        ADI(value)
        MOV(reg, A)
        POP(PSW)
    }

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

|Parameter|Allowed Values|Default|Description
|---|---|---|---|
|`target`|`i8080`, `i8085`|`i8085`|Target processor for which to assemble (use constants from class [ProcessorTarget](assembler/src/main/groovy/com/perihelios/experimental/intel8085dsl/ProcessorTarget.groovy))|
|`size`|1-65536|None (use `autoSize`)|Size, in bytes, of buffer into which machine code assembled (parameter must not be specified if `autoSize` specified)|
|`autoSize`|`true`, `false`|`true`|Automatically produce machine code buffer of exact size needed for given instructions (parameter must not be specified if `size` specified)|
|`autoHlt`|`true`, `false`|`true`|Automatically add HLT instruction at end of machine code buffer

Parameters are passed in an
[AssemblerParameters](assembler/src/main/groovy/com/perihelios/experimental/intel8085dsl/AssemblerParameters.groovy)
instance:

```
asm(new AssemblerParameters(target: i8080, size: 256, autoHlt: false)) {
    // Assembly language
}
```

### Instructions
The Intel 8085 has 80 instructions, and the 8080 has 78. Both processors have
the same seven 8-bit registers, plus a set of flags. The 8085 has an additional
three-bit interrupt mask, accessed through the RIM and SIM instructions.

## License
Apache 2.0 (see [LICENSE.txt](LICENSE.txt) and [NOTICE.txt](NOTICE.txt) for details).
