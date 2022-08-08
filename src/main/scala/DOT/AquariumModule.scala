package DOT

object AquariumModule:
    class Piranha
    class Goldfish

    trait Aquarium:
        type Fish
        val fish : List [ Fish ]

    // the type Fish is path dependent, i.e. specific to the run-time Aquarium object that
    // the fish belongs to. This allows the addFish method to guarantee at
    // compile time that an aquarium a accepts only fish of type a.Fish.
    // 
    // DOT, so we should consider the "O", that is, "a" in a.Fish
    // NOTE: path-dependent type, where type depends on value, 
    //       dependent-object type, where type depdens on object, we encode type in the value, 
    //       that is, "object"
    def addFish(a: Aquarium, f: a.Fish) =
        new Aquarium:
            type Fish = a.Fish
            val fish = a.fish :+ f

    val piranhas = new Aquarium:
        type Fish = Piranha
        val fish = List.empty[Piranha]

    val goldfish = new Aquarium
        type Fish = Goldfish
        val fish = List.empty[Goldfish ]

    def main(args: Array[String]): Unit =
        // This program lets us add a fish gf to the goldfish aquarium:
        val gf : Goldfish = Goldfish()
        addFish(goldfish, gf)

        // but it will result in a type error when trying to add gf to the piranha aquarium:
        // addFish(piranhas , gf) // expected: piranhas.Fish , actual : goldfish.Fish

        /*
         * The reason the goldfish is protected from the piranhas is that the type
         * Fish is path dependent, i.e. specific to the run-time Aquarium object that
         * the fish belongs to. This allows the addFish method to guarantee at
         * compile time that an aquarium a accepts only fish of type a.Fish.
         */
