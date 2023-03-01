package macros

@main
def postIntMain: Unit =
    val good1 = PosInt(1)
    val good2 = PosInt(2)
    val good3 = good1 + good2
    val good4 = good3 * good2
    println(good4)
    //val bad = PosInt(-1)
