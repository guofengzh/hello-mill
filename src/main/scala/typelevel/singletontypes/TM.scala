package typelevel.matrix

object TM:
    // https://www.anycodings.com/questions/scala-3-match-type-reduction
    //
    // Let's say I want to represent a vector using anycodings_match-types a 
    // matched type like this:

    type V[I <: Int, N] = I match
        case 2 => (N, N)
        case 3 => (N, N, N)
        case 4 => (N, N, N, N)    

    // so now I can declare my vectors using anycodings_match-types tuples:
    val v2: V[2, Int] = (2, 2)
    val v3: V[3, Int] = (3, 3, 3)
    val v4: V[4, Int] = (4, 4, 4, 4)

    // and then I can define a tuple-matrix and a anycodings_match-types tuple-square-matrix:
    type Mx[L <: Int, C <: Int, N] = V[L, V[C, N]]
    type Mxq[T <: Int, N] = Mx[T, T, N]
    type M3I = Mxq[3, Int]        

    // everything is fine until now:

    //Compiles
    val m3: M3I = (
        (1, 0, 0),
        (0, 1, 0),
        (0, 0, 1)
    )

    /*
    //Doesn't compile
    val m3Err: M3I = (
        (1, 0, 0, 1),
        (0, 1, 0),
        (0, 0, 1)
    )
    */
    
