## reactor-core fromCompletionStage bug

Test to reproduce an issue where a Mono created with `Mono.fromCompletionStage` fails to propagate an error signal downstream.
The issue can be reproduced in the following cases:
- `mono.map` throws an unchecked exception
- `mono.flatMap` returns a mono containing an error signal
- `mono.handle` signals an error on the provided sink

## Expectation

When using `Mono.fromCompletionStage`, error signals produced on mapping methods should propagate downstream

## Workaround

Use `Mono.fromFuture(cs.toCompletableFuture())` instead.

## How to run
```sh
./gradlew test
```
