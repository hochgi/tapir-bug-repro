# Repruding a bug in [Tapir](https://github.com/softwaremill/tapir/)

Tapir version: 0.19.0
Scala version: 2.13.7

## Description
The code in this repo compiles just fine with no errors.
But when trying to evaluate tapir endpoints, an exception is thrown at runtime.

## How to reproduce
- Option 1: `sbt swagger/generateYaml` - this custom task generates the proper swagger YAML as part of the build
- Option 2: `sbt server/run` - simply try to run the server. Exception is thrown at initialization.

### Option 1 stack trace:
```text
sbt:root> last swagger / generateYaml
[info] running com.hochgi.repro.swagger.GenerateSwagger -s plain /home/hochgi/dev/tapir-bug-repro/repro-swagger/target/swagger.yaml
[debug] Waiting for threads to exit or System.exit to be called.
[debug]   Classpath:
[debug] 	/home/hochgi/dev/tapir-bug-repro/repro-swagger/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/repro-endpoints/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/repro-datatypes/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/json-ast/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/json-circe/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/json-encode/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/compat/target/scala-2.13/classes
[debug] 	/home/hochgi/dev/tapir-bug-repro/json-ops/target/scala-2.13/classes
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.7/scala-library-2.13.7.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-openapi-circe-yaml_2.13/0.19.0/tapir-openapi-circe-yaml_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-openapi-docs_2.13/0.19.0/tapir-openapi-docs_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/github/scopt/scopt_2.13/4.0.1/scopt_2.13-4.0.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-core_2.13/0.19.0/tapir-core_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/akka/akka-stream_2.13/2.6.17/akka-stream_2.13-2.6.17.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-json-circe_2.13/0.19.0/tapir-json-circe_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-core_2.13/0.14.1/circe-core_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/scala-logging/scala-logging_2.13/3.9.3/scala-logging_2.13-3.9.3.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-openapi-circe_2.13/0.19.0/tapir-openapi-circe_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-yaml_2.13/0.14.1/circe-yaml_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-openapi-model_2.13/0.19.0/tapir-openapi-model_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-apispec-docs_2.13/0.19.0/tapir-apispec-docs_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-enumeratum_2.13/0.19.0/tapir-enumeratum_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/model/core_2.13/1.4.18/core_2.13-1.4.18.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/shared/core_2.13/1.2.7/core_2.13-1.2.7.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/shared/ws_2.13/1.2.7/ws_2.13-1.2.7.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/propensive/magnolia_2.13/0.17.0/magnolia_2.13-0.17.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/akka/akka-actor_2.13/2.6.17/akka-actor_2.13-2.6.17.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/akka/akka-protobuf-v3_2.13/2.6.17/akka-protobuf-v3_2.13-2.6.17.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/ssl-config-core_2.13/0.4.2/ssl-config-core_2.13-0.4.2.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-parser_2.13/0.14.1/circe-parser_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-generic_2.13/0.14.1/circe-generic_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-numbers_2.13/0.14.1/circe-numbers_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-core_2.13/2.6.1/cats-core_2.13-2.6.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.13.7/scala-reflect-2.13.7.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/yaml/snakeyaml/1.28/snakeyaml-1.28.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/softwaremill/sttp/tapir/tapir-apispec-model_2.13/0.19.0/tapir-apispec-model_2.13-0.19.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/beachape/enumeratum_2.13/1.7.0/enumeratum_2.13-1.7.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/propensive/mercator_2.13/0.2.1/mercator_2.13-0.2.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/typesafe/config/1.4.0/config-1.4.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-java8-compat_2.13/1.0.0/scala-java8-compat_2.13-1.0.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.13/1.1.2/scala-parser-combinators_2.13-1.1.2.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/io/circe/circe-jawn_2.13/0.14.1/circe-jawn_2.13-0.14.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/chuusai/shapeless_2.13/2.3.7/shapeless_2.13-2.3.7.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/cats-kernel_2.13/2.6.1/cats-kernel_2.13-2.6.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/simulacrum-scalafix-annotations_2.13/0.5.4/simulacrum-scalafix-annotations_2.13-0.5.4.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/com/beachape/enumeratum-macros_2.13/1.6.1/enumeratum-macros_2.13-1.6.1.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/commons-io/commons-io/2.11.0/commons-io-2.11.0.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/commons/commons-text/1.9/commons-text-1.9.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/typelevel/jawn-parser_2.13/1.1.2/jawn-parser_2.13-1.1.2.jar
[debug] 	/home/hochgi/.cache/coursier/v1/https/repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.11/commons-lang3-3.11.jar
[debug] Waiting for thread run-main-0 to terminate.
[error] (run-main-0) java.lang.ExceptionInInitializerError
[error] java.lang.ExceptionInInitializerError
[error] 	at com.hochgi.repro.swagger.EvalEndpoints$.evalAll(EvalEndpoints.scala:31)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$.delayedEndpoint$com$hochgi$repro$swagger$GenerateSwagger$1(GenerateSwagger.scala:58)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$delayedInit$body.apply(GenerateSwagger.scala:15)
[error] 	at scala.Function0.apply$mcV$sp(Function0.scala:39)
[error] 	at scala.Function0.apply$mcV$sp$(Function0.scala:39)
[error] 	at scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:17)
[error] 	at scala.App.$anonfun$main$1(App.scala:76)
[error] 	at scala.App.$anonfun$main$1$adapted(App.scala:76)
[error] 	at scala.collection.IterableOnceOps.foreach(IterableOnce.scala:563)
[error] 	at scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:561)
[error] 	at scala.collection.AbstractIterable.foreach(Iterable.scala:926)
[error] 	at scala.App.main(App.scala:76)
[error] 	at scala.App.main$(App.scala:74)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$.main(GenerateSwagger.scala:15)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger.main(GenerateSwagger.scala)
[error] 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[error] 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
[error] 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
[error] 	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
[error] 	at sbt.Run.invokeMain(Run.scala:133)
[error] 	at sbt.Run.execute$1(Run.scala:82)
[error] 	at sbt.Run.$anonfun$runWithLoader$5(Run.scala:110)
[error] 	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
[error] 	at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:17)
[error] 	at sbt.TrapExit$App.run(TrapExit.scala:258)
[error] 	at java.base/java.lang.Thread.run(Thread.java:829)
[error] Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
[error] 	at scala.collection.immutable.ArraySeq$ofRef.apply(ArraySeq.scala:331)
[error] 	at scala.collection.IndexedSeqOps.head(IndexedSeq.scala:84)
[error] 	at scala.collection.IndexedSeqOps.head$(IndexedSeq.scala:84)
[error] 	at scala.collection.immutable.ArraySeq.head(ArraySeq.scala:35)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:20)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
[error] 	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$23(SemiStructured.scala:88)
[error] 	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
[error] 	at magnolia.CallByNeed.value(magnolia.scala:817)
[error] 	at magnolia.Subtype$$anon$1.typeclass(interface.scala:73)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$2(SchemaMagnoliaDerivation.scala:67)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$1(SchemaMagnoliaDerivation.scala:67)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch(SchemaMagnoliaDerivation.scala:65)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch$(SchemaMagnoliaDerivation.scala:64)
[error] 	at sttp.tapir.generic.auto.package$.dispatch(package.scala:6)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$lzycompute$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$lzycompute$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$39(SemiStructured.scala:88)
[error] 	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
[error] 	at magnolia.CallByNeed.value(magnolia.scala:817)
[error] 	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
[error] 	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$lzycompute$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$lzycompute$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$43(SemiStructured.scala:88)
[error] 	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
[error] 	at magnolia.CallByNeed.value(magnolia.scala:817)
[error] 	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
[error] 	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
[error] 	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
[error] 	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$lzycompute$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$1(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.endpoints.SemiStructured$.<clinit>(SemiStructured.scala:88)
[error] 	at com.hochgi.repro.swagger.EvalEndpoints$.evalAll(EvalEndpoints.scala:31)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$.delayedEndpoint$com$hochgi$repro$swagger$GenerateSwagger$1(GenerateSwagger.scala:58)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$delayedInit$body.apply(GenerateSwagger.scala:15)
[error] 	at scala.Function0.apply$mcV$sp(Function0.scala:39)
[error] 	at scala.Function0.apply$mcV$sp$(Function0.scala:39)
[error] 	at scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:17)
[error] 	at scala.App.$anonfun$main$1(App.scala:76)
[error] 	at scala.App.$anonfun$main$1$adapted(App.scala:76)
[error] 	at scala.collection.IterableOnceOps.foreach(IterableOnce.scala:563)
[error] 	at scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:561)
[error] 	at scala.collection.AbstractIterable.foreach(Iterable.scala:926)
[error] 	at scala.App.main(App.scala:76)
[error] 	at scala.App.main$(App.scala:74)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger$.main(GenerateSwagger.scala:15)
[error] 	at com.hochgi.repro.swagger.GenerateSwagger.main(GenerateSwagger.scala)
[error] 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[error] 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
[error] 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
[error] 	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
[error] 	at sbt.Run.invokeMain(Run.scala:133)
[error] 	at sbt.Run.execute$1(Run.scala:82)
[error] 	at sbt.Run.$anonfun$runWithLoader$5(Run.scala:110)
[error] 	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
[error] 	at sbt.util.InterfaceUtil$$anon$1.get(InterfaceUtil.scala:17)
[error] 	at sbt.TrapExit$App.run(TrapExit.scala:258)
[error] 	at java.base/java.lang.Thread.run(Thread.java:829)
[debug] 	Thread run-main-0 exited.
[debug] Interrupting remaining threads (should be all daemons).
[debug] Sandboxed run complete..
[error] Nonzero exit code: 1
[error] (swagger / generateYaml) Nonzero exit code: 1
```

### Option 2 stack trace:
```text
13:08:08.872 [run-main-0] DEBUG com.hochgi.repro.server.Server$ - Starting server
13:08:09.874 [evidence-manager-akka.actor.default-dispatcher-3] INFO akka.event.slf4j.Slf4jLogger - Slf4jLogger started
Uncaught error from thread [evidence-manager-akka.actor.default-dispatcher-3]: null, shutting down JVM since 'akka.jvm-exit-on-fatal-error' is enabled for ActorSystem[evidence-manager]
java.lang.ExceptionInInitializerError
	at com.hochgi.repro.swagger.EvalEndpoints$.evalAll(EvalEndpoints.scala:31)
	at com.hochgi.repro.server.Server$.endpointsAndRoutes(Server.scala:35)
	at com.hochgi.repro.server.Server$.$anonfun$apply$1(Server.scala:69)
	at akka.actor.typed.internal.BehaviorImpl$DeferredBehavior$$anon$1.apply(BehaviorImpl.scala:120)
	at akka.actor.typed.Behavior$.start(Behavior.scala:168)
	at akka.actor.typed.internal.InterceptorImpl$$anon$1.start(InterceptorImpl.scala:50)
	at akka.actor.typed.BehaviorInterceptor.aroundStart(BehaviorInterceptor.scala:55)
	at akka.actor.typed.internal.InterceptorImpl.preStart(InterceptorImpl.scala:73)
	at akka.actor.typed.internal.InterceptorImpl$.$anonfun$apply$1(InterceptorImpl.scala:30)
	at akka.actor.typed.internal.BehaviorImpl$DeferredBehavior$$anon$1.apply(BehaviorImpl.scala:120)
	at akka.actor.typed.Behavior$.start(Behavior.scala:168)
	at akka.actor.typed.Behavior$.interpret(Behavior.scala:275)
	at akka.actor.typed.Behavior$.interpretMessage(Behavior.scala:230)
	at akka.actor.typed.internal.adapter.ActorAdapter.handleMessage(ActorAdapter.scala:131)
	at akka.actor.typed.internal.adapter.ActorAdapter.aroundReceive(ActorAdapter.scala:107)
	at akka.actor.ActorCell.receiveMessage(ActorCell.scala:580)
	at akka.actor.ActorCell.invoke(ActorCell.scala:548)
	at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:270)
	at akka.dispatch.Mailbox.run(Mailbox.scala:231)
	at akka.dispatch.Mailbox.exec(Mailbox.scala:243)
	at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1020)
	at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1656)
	at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1594)
	at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:183)
Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
	at scala.collection.immutable.ArraySeq$ofRef.apply(ArraySeq.scala:331)
	at scala.collection.IndexedSeqOps.head(IndexedSeq.scala:84)
	at scala.collection.IndexedSeqOps.head$(IndexedSeq.scala:84)
	at scala.collection.immutable.ArraySeq.head(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:20)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$23(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.Subtype$$anon$1.typeclass(interface.scala:73)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$2(SchemaMagnoliaDerivation.scala:67)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$1(SchemaMagnoliaDerivation.scala:67)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch(SchemaMagnoliaDerivation.scala:65)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch$(SchemaMagnoliaDerivation.scala:64)
	at sttp.tapir.generic.auto.package$.dispatch(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$39(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$43(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.<clinit>(SemiStructured.scala:88)
	... 25 more
13:08:10.527 [evidence-manager-akka.actor.default-dispatcher-6] ERROR akka.actor.ActorSystemImpl - Uncaught error from thread [evidence-manager-akka.actor.default-dispatcher-3]: null, shutting down JVM since 'akka.jvm-exit-on-fatal-error' is enabled for ActorSystem[evidence-manager]
java.lang.ExceptionInInitializerError: null
	at com.hochgi.repro.swagger.EvalEndpoints$.evalAll(EvalEndpoints.scala:31)
	at com.hochgi.repro.server.Server$.endpointsAndRoutes(Server.scala:35)
	at com.hochgi.repro.server.Server$.$anonfun$apply$1(Server.scala:69)
	at akka.actor.typed.internal.BehaviorImpl$DeferredBehavior$$anon$1.apply(BehaviorImpl.scala:120)
	at akka.actor.typed.Behavior$.start(Behavior.scala:168)
	at akka.actor.typed.internal.InterceptorImpl$$anon$1.start(InterceptorImpl.scala:50)
	at akka.actor.typed.BehaviorInterceptor.aroundStart(BehaviorInterceptor.scala:55)
	at akka.actor.typed.internal.InterceptorImpl.preStart(InterceptorImpl.scala:73)
	at akka.actor.typed.internal.InterceptorImpl$.$anonfun$apply$1(InterceptorImpl.scala:30)
	at akka.actor.typed.internal.BehaviorImpl$DeferredBehavior$$anon$1.apply(BehaviorImpl.scala:120)
	at akka.actor.typed.Behavior$.start(Behavior.scala:168)
	at akka.actor.typed.Behavior$.interpret(Behavior.scala:275)
	at akka.actor.typed.Behavior$.interpretMessage(Behavior.scala:230)
	at akka.actor.typed.internal.adapter.ActorAdapter.handleMessage(ActorAdapter.scala:131)
	at akka.actor.typed.internal.adapter.ActorAdapter.aroundReceive(ActorAdapter.scala:107)
	at akka.actor.ActorCell.receiveMessage(ActorCell.scala:580)
	at akka.actor.ActorCell.invoke(ActorCell.scala:548)
	at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:270)
	at akka.dispatch.Mailbox.run(Mailbox.scala:231)
	at akka.dispatch.Mailbox.exec(Mailbox.scala:243)
	at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1020)
	at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1656)
	at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1594)
	at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:183)
Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
	at scala.collection.immutable.ArraySeq$ofRef.apply(ArraySeq.scala:331)
	at scala.collection.IndexedSeqOps.head(IndexedSeq.scala:84)
	at scala.collection.IndexedSeqOps.head$(IndexedSeq.scala:84)
	at scala.collection.immutable.ArraySeq.head(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:20)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$23(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.Subtype$$anon$1.typeclass(interface.scala:73)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$2(SchemaMagnoliaDerivation.scala:67)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$dispatch$1(SchemaMagnoliaDerivation.scala:67)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch(SchemaMagnoliaDerivation.scala:65)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.dispatch$(SchemaMagnoliaDerivation.scala:64)
	at sttp.tapir.generic.auto.package$.dispatch(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.jsonTypeclass$macro$67$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$58$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$39(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.objTypeclass$macro$56$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.paramTypeclass$macro$6$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.$anonfun$SemiStructuredCodec$43(SemiStructured.scala:88)
	at magnolia.CallByNeed.value$lzycompute(magnolia.scala:818)
	at magnolia.CallByNeed.value(magnolia.scala:817)
	at magnolia.ReadOnlyParam$$anon$2.typeclass(interface.scala:173)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$productSchemaType$1(SchemaMagnoliaDerivation.scala:31)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:75)
	at scala.collection.immutable.ArraySeq.map(ArraySeq.scala:35)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.productSchemaType(SchemaMagnoliaDerivation.scala:30)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.$anonfun$combine$1(SchemaMagnoliaDerivation.scala:22)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.withCache(SchemaMagnoliaDerivation.scala:95)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine(SchemaMagnoliaDerivation.scala:17)
	at sttp.tapir.generic.internal.SchemaMagnoliaDerivation.combine$(SchemaMagnoliaDerivation.scala:16)
	at sttp.tapir.generic.auto.package$.combine(package.scala:6)
	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$lzycompute$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.semistructuredTypeclass$macro$1$1(SemiStructured.scala:88)
	at com.hochgi.repro.endpoints.SemiStructured$.<clinit>(SemiStructured.scala:88)
	... 25 common frames omitted
```