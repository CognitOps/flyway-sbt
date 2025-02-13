lazy val repoSlug = "sbt/flyway-sbt"
lazy val flywayVersion = "10.21.0"
lazy val scala212 = "2.12.20"
lazy val scala3 = "3.3.4"

ThisBuild / organization := "com.github.sbt"

ThisBuild / version := "10.21.0.2"

// Clint: I hacked the build.sbt and project/plugin.sbt so that it uses the
// CognitOps GCS artifact directory for releasing. To build and release our
// internal code, just do:
//
// 1. sbt compile
// 2. sbt publish-local
// 3. sbt publish
//
// Hopefully at some point, the open source maintaners fix the bugs and we
// can get rid of this fork.

/*
ThisBuild / version := {
  val orig = (ThisBuild / version).value
  if (orig.startsWith("9.") && orig.endsWith("-SNAPSHOT")) {
    "9.0.0-SNAPSHOT"
  } else orig
}*/

publishTo := Some({
  val bucketName = "cognitops-libraries"
  val folder = if (isSnapshot.value) "snapshots" else "releases"
  s"cognitops $folder" at s"gs://$bucketName/$folder"
})


lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "flyway-sbt",
    crossScalaVersions := Seq(scala212, scala3),
    libraryDependencies ++= Seq(
      "org.flywaydb" % "flyway-core" % flywayVersion
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-Xfuture"
    ),
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.5.8"
        case _      => "2.0.0-M2"
      }
    },
    Compile / doc / scalacOptions ++= {
      scalaBinaryVersion.value match {
        case "2.12" =>
          Seq(
            "-sourcepath",
            (LocalRootProject / baseDirectory).value.getAbsolutePath,
            "-doc-source-url",
            s"""https://github.com/sbt/flyway-sbt/tree/${sys.process
                .Process("git rev-parse HEAD")
                .lineStream_!
                .head}€{FILE_PATH}.scala"""
          )
        case _ => Nil
      }
    },
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    publishMavenStyle := true,
  )

ThisBuild / description := "An sbt plugin for Flyway database migration"
ThisBuild / developers := List(
  Developer(
    id = "davidmweber",
    name = "David Weber",
    email = "dave@veryflatcat.com",
    url = url("https://davidmweber.github.io/flyway-sbt-docs/")
  )
)
ThisBuild / licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/$repoSlug"),
    s"scm:git@github.com:$repoSlug.git"
  )
)
ThisBuild / homepage := Some(url(s"https://github.com/$repoSlug"))
/*
ThisBuild / publishTo := sonatypePublishTo.value
ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("+test", "+scripted")))
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")))
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)
ThisBuild / githubWorkflowOSes := Seq("ubuntu-latest", "macos-latest")
ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("17")
)
 */
