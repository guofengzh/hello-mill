//import mill.scalalib.{SbtModule, TestModule, Dep, DepSyntax}

import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill._, scalalib._
//import mill.scalalib.bsp.ScalaMetalsSupport, coming soon in v0.10.0

val scala3Version = "3.3.1-RC4"

trait BetterFilesModule extends SbtModule {
  //def semanticDbVersion = "4.4.29"
  def scalaVersion = scala3Version
  // https://docs.scala-lang.org/scala3/guides/migration/options-lookup.html
  // https://docs.scala-lang.org/scala3/guides/migration/options-new.html
  def scalacOptions = Seq(
    "-unchecked",  // Enable additional warnings where generated code depends on assumptions.
    "-deprecation",      // Emit warning and location for usages of deprecated APIs.
    "-explain",          // Explain errors in more detail.
    "-explain-types",
    "-encoding", "utf8", // Specify character encoding used by source files.
    "-feature",          // Emit warning and location for usages of features that should be imported explicitly.
    "-source:future",    // force future deprecation warnings.
    "-rewrite",          // 
    "-indent",           // Together with -rewrite, remove {…} syntax when possible due to significant indentation.
    //"-new-syntax",     // Require then and do in control expressions. 
                         // -- illegal combination of -rewrite targets: -new-syntax and -indent
    //"-Xsemanticdb",      // the Scala 3 compiler has built-in SemanticDB support. 
    //"-Xcheck-macros",  // Additional check useful during development
    "-Xfatal-warnings"   // Fail on warnings, not just errors
    ,
    "-Yexplicit-nulls"
  )

  object test extends JavaModuleTests with TestModule.Junit4 {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.13",
      ivy"com.github.sbt:junit-interface:0.13.3"
    )
  }
}

object hellomill extends BetterFilesModule {
   def millSourcePath = build.millSourcePath
   override def ivyDeps = Agg(
      ivy"org.tpolecat::typename:1.0.0",
      ivy"org.typelevel::cats-core:2.9.0"
    )
}
