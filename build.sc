//import mill.scalalib.{SbtModule, TestModule, Dep, DepSyntax}

import mill._, scalalib._
//import mill.scalalib.bsp.ScalaMetalsSupport

val scala3Version = "3.1.0"

trait BetterFilesModule extends SbtModule {
  def semanticDbVersion = "4.4.29"
  def scalaVersion = scala3Version
  // https://docs.scala-lang.org/scala3/guides/migration/options-lookup.html
  def scalacOptions = Seq(
    "-unchecked",  // Enable additional warnings where generated code depends on assumptions.
    "-deprecation",      // Emit warning and location for usages of deprecated APIs.
    "-explain",          // Explain errors in more detail.
    "-encoding", "utf8", // Specify character encoding used by source files.
    "-feature",          // Emit warning and location for usages of features that should be imported explicitly.
    "-source:future",    // force future deprecation warnings.
    "-new-syntax",       // Require then and do in control expressions.
    "-indent",           // Together with -rewrite, remove {…} syntax when possible due to significant indentation.
    // "-Xsemanticdb",       // the Scala 3 compiler has built-in SemanticDB support. 
    "-Xfatal-warnings" // Fail on warnings, not just errors
  )

  object test extends Tests with TestModule.Junit4 {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.10",
      ivy"com.novocode:junit-interface:0.11"
    )
  }
}

object helloworld extends BetterFilesModule {
   def millSourcePath = build.millSourcePath
}
