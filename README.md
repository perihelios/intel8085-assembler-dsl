# Intel 8080/8085 Assembler
A Groovy DSL that assembles Intel 8080 or 8085 mnemonics into machine code.

## Overview
The Intel 8080 and 8085 (which are nearly identical in terms of instruction set)
are 8-bit microprocessors with 64K of memory addressability. They have 78 and 80
mnemonics, respectively. This assembler allows you to create machine code for
these processors in Groovy, using a domain-specific language (DSL).

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

## License
Apache 2.0 (see [LICENSE.txt](LICENSE.txt) and [NOTICE.txt](NOTICE.txt) for details).
