val scala3Version = "3.4.2-RC1"

val dependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.10.0",
  "org.typelevel" %% "cats-effect" % "3.6-623178c",
  "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
  "ch.qos.logback" % "logback-classic" % "1.5.3" % Runtime,
  "org.scalameta" %% "munit" % "1.0.0-M11" % Test
)

lazy val compilerOptions = Seq(
  "-unchecked",  // Enable additional warnings where generated code depends on assumptions.
  "-explain",          // Explain errors in more detail.
  "-explain-types",
  "-encoding", "utf8", // Specify character encoding used by source files.
  "-feature",          // Emit warning and location for usages of features that should be imported explicitly.
  "-source:future",    // force future deprecation warnings.
  "-rewrite",          // 
  "-indent",           // Together with -rewrite, remove {…} syntax when possible due to significant indentation.
  "-deprecation",      // Emit warning and location for usages of deprecated APIs. Together with -rewrite, ....
  //"-new-syntax",     // Require then and do in control expressions. 
                       // -- illegal combination of -rewrite targets: -new-syntax and -indent
  //"-Xsemanticdb",    // the Scala 3 compiler has built-in SemanticDB support. 
  //"-Xcheck-macros",  // Additional check useful during development
  "-Xfatal-warnings"   // Fail on warnings, not just errors
  ,
  // https://hackernoon.com/null-the-billion-dollar-mistake-8t5z32d6
  // 
  "-Yexplicit-nulls",    // to allow null, using 'import scala.language.unsafeNulls'
  //"-language:implicitConversions",  // Use of implicit conversion, using import scala.language.implicitConversions
  //"-Ycc",              // Enable support for capture checking. https://docs.scala-lang.org/scala3/reference/experimental/index.html
  "-Ysafe-init"          // https://docs.scala-lang.org/scala3/reference/other-new-features/safe-initialization.html
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-tp-codes",
    organization := "com.tp.codes",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    
    scalacOptions ++= compilerOptions,

    libraryDependencies ++= dependencies,

    // resources are only copied AFTER the compilation, so that macros cannot find the files in resources.
    // this adds the resources directory to the classpath to solve this issue.
    Compile / unmanagedClasspath  += baseDirectory.value / "src/main/resources"
  )
