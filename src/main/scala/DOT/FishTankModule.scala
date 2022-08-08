package DOT

object FishTankModule {
  
}

trait FishTank:
  type Fish
  val fish : List[Fish]

def addFish(a: FishTank, f: a.Fish) =
  new FishTank:
    type Fish = a.Fish
    val fish = a.fish :+ f

class Piranha
class Goldfish

val piranhasTank = new FishTank:
  type Fish = Piranha
  val fish = List.empty[Piranha]

val goldfishTank = new FishTank:
  type Fish = Goldfish
  val fish = List.empty[Goldfish ]

@main def testFishTank: Unit = 
    val gf :Goldfish = Goldfish()
    addFish(goldfishTank, gf)

    addFish(piranhasTank , gf)
