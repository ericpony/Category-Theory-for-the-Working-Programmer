name := "example"

scalaVersion in ThisBuild := "2.10.4"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-unchecked", "-feature", "-language:_")

initialCommands in console := "import example._"
