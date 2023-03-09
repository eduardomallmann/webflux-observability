# Webflux Observation

Primary code copied and adapted from: https://github.com/marios-code-path/path-to-springboot-3.git

## Goal

The goal with this code is to show the traceId sharing among the services involved in a call.

## Deployment

In order to achieve the goal, two instance of this code should be deployed.

1. The first one with the standard code and configuration
2. The second one with some properties modification as the image below

![Second Deployment Configuration](Captura%20de%20tela%202023-03-09%20103232.png)

Follow the properties for the second deployment:

- server.port=8686
- spring.application.name=client

## Test

To test the operation call the endpoint "/hello/client/{name}" and than you can check the logs.

### Call example
```sh
curl --location 'localhost:8787/hello/client/edu'
```

### Logs example

#### webflux-observation instance
```text
2023-03-09T10:29:57.341-03:00 [webflux-observation,6409df55c080fbf9be326ba884d946ef,be326ba884d946ef] 19249 --- [or-http-epoll-4] i.m.o.ObservationTextPublisher           : START - name='greeting.client.call', contextualName='greeting.client.call', error='null', lowCardinalityKeyValues=[latency='high', reactor.type='Mono'], highCardinalityKeyValues=[], map=[], parentObservation={name=http.server.requests(null), error=null, context=name='http.server.requests', contextualName='null', error='null', lowCardinalityKeyValues=[exception='none', method='GET', outcome='SUCCESS', status='200', uri='UNKNOWN'], highCardinalityKeyValues=[http.url='/hello/client/edu'], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/be326ba884d946ef), scope=io.micrometer.tracing.brave.bridge.BraveScope@164f9452}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.099780328, duration(nanos)=9.9780328E7, startTimeNanos=790103368246}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@24cc6dfd'], parentObservation=null}
2023-03-09T10:29:57.826-03:00 [webflux-observation,6409df55c080fbf9be326ba884d946ef,be326ba884d946ef] 19249 --- [or-http-epoll-4] i.m.o.ObservationTextPublisher           :  STOP - name='greeting.client.call', contextualName='greeting.client.call', error='null', lowCardinalityKeyValues=[latency='high', reactor.status='completed', reactor.type='Mono'], highCardinalityKeyValues=[], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/db38a3750f26c2a2), scope=null}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=-1.0E-9, duration(nanos)=-1.0, startTimeNanos=790205048563}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@165b6ba9'], parentObservation={name=http.server.requests(null), error=null, context=name='http.server.requests', contextualName='null', error='null', lowCardinalityKeyValues=[exception='none', method='GET', outcome='SUCCESS', status='200', uri='UNKNOWN'], highCardinalityKeyValues=[http.url='/hello/client/edu'], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/be326ba884d946ef), scope=io.micrometer.tracing.brave.bridge.BraveScope@21cdba44}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.585109777, duration(nanos)=5.85109777E8, startTimeNanos=790103368246}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@24cc6dfd'], parentObservation=null}
```
#### client instance
```text
2023-03-09T10:29:57.650-03:00 [client,6409df55c080fbf9be326ba884d946ef,930fbf3bd2792b43] 18728 --- [or-http-epoll-3] i.m.o.ObservationTextPublisher           : START - name='greeting.call', contextualName='greeting.call', error='null', lowCardinalityKeyValues=[latency='low', reactor.type='Mono'], highCardinalityKeyValues=[], map=[], parentObservation={name=http.server.requests(null), error=null, context=name='http.server.requests', contextualName='null', error='null', lowCardinalityKeyValues=[exception='none', method='GET', outcome='SUCCESS', status='200', uri='UNKNOWN'], highCardinalityKeyValues=[http.url='/hello/edu'], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/930fbf3bd2792b43), scope=io.micrometer.tracing.brave.bridge.BraveScope@7b0a5e78}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.076988306, duration(nanos)=7.6988306E7, startTimeNanos=790435168622}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@6a2b94a5'], parentObservation=null}
2023-03-09T10:29:57.750-03:00 [client,6409df55c080fbf9be326ba884d946ef,930fbf3bd2792b43] 18728 --- [     parallel-1] i.m.o.ObservationTextPublisher           :  STOP - name='greeting.call', contextualName='greeting.call', error='null', lowCardinalityKeyValues=[latency='low', reactor.status='completed', reactor.type='Mono'], highCardinalityKeyValues=[], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/f1a9ef649c9e4238), scope=null}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=-1.0E-9, duration(nanos)=-1.0, startTimeNanos=790513941928}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@12f088a3'], parentObservation={name=http.server.requests(null), error=null, context=name='http.server.requests', contextualName='null', error='null', lowCardinalityKeyValues=[exception='none', method='GET', outcome='SUCCESS', status='200', uri='UNKNOWN'], highCardinalityKeyValues=[http.url='/hello/edu'], map=[class io.micrometer.tracing.handler.TracingObservationHandler$TracingContext='TracingContext{span=NoopSpan(6409df55c080fbf9be326ba884d946ef/930fbf3bd2792b43), scope=io.micrometer.tracing.brave.bridge.BraveScope@580b8dc9}', class io.micrometer.core.instrument.LongTaskTimer$Sample='SampleImpl{duration(seconds)=0.176811032, duration(nanos)=1.76811032E8, startTimeNanos=790435168622}', class io.micrometer.core.instrument.Timer$Sample='io.micrometer.core.instrument.Timer$Sample@6a2b94a5'], parentObservation=null}
```