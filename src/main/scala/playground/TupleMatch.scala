package com.mycompany.playground

object TupleMatch:
  val pair = (123,"xyz")

  val result = pair match
    case (fst, snd) => s"Hello, fst is $fst, snd is $snd"

  val pf: PartialFunction[(Int, String), String] = { case (fst, snd) => s"Hello, fst is $fst, snd is $snd" }