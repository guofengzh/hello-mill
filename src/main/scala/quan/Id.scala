package quan

type Id[A] = A

// @TODO[WTF] It's bullshit that we need these Id[A] <> A conversions

given [A]: Conversion[A, Id[A]] = a => a

given [A]: Conversion[Id[A], A] = a => a
