package com.example

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.scaladsl.{Flow, Sink, Source}
import org.apache.pekko.util.ByteString


object PekkoQuickstart extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val s = Source
    .repeat(())
    .map(_ => ByteString('a' * 400000))
    .take(1000000)
    .flatMapPrefix(50000) { prefix => Flow[ByteString] }

  Source.empty
    .concatAllLazy(List.tabulate(30000)(_ => s): _*)
    .runWith(Sink.ignore).onComplete(println(_))

//  Source
//    .repeat(s)
//    .take(30000)
//    .flatMapConcat(x => x)
//    .runWith(Sink.ignore)
//    .onComplete(println(_))

//  Source.empty
//    .concatAllLazy(List.tabulate(30000)(_ => Source.lazySource(() => s)): _*)
//    .runWith(Sink.ignore).onComplete(println(_))
}
