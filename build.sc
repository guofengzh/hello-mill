//import mill.scalalib.{SbtModule, TestModule, Dep, DepSyntax}

import mill._, scalalib._

val scala3Version = "3.0.1"

trait BetterFilesModule extends SbtModule{

  def scalaVersion = scala3Version

  object test extends Tests with TestModule.Junit4 {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.9",
      ivy"com.novocode:junit-interface:0.11"
    )
  }
}

object helloworld extends BetterFilesModule {
   def millSourcePath = build.millSourcePath
}
