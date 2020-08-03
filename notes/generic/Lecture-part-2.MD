## Configurable warnings

[Scala 2.13.2](https://github.com/scala/scala/releases/tag/v2.13.2) introduced configurable warnings via -Wconf compiler 
flag that allows filtering and configuring compiler warnings (silence them, or turn them into errors). Details can be 
found in the [pull request](https://github.com/scala/scala/pull/8373) to Scala.

For example, if you want to turn the warning "fruitless type test" showed in the video, you need to add the following option to build.sbt

scalacOptions += "-Wconf:cat=unchecked:e"