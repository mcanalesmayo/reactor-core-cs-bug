package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MonoTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(5L);

    private static final List<Mono<String>> MONOS_TO_TEST = List.of(
            Mono.fromFuture(() -> CompletableFuture.completedFuture("test")),
            Mono.fromFuture(CompletableFuture.completedFuture("test")),
            Mono.fromSupplier(() -> "test"),
            Mono.fromCompletionStage(() -> CompletableFuture.completedStage("test")),
            Mono.fromCompletionStage(CompletableFuture.completedStage("test")),
            Mono.fromFuture(CompletableFuture.completedStage("test").toCompletableFuture())
    );

    static Stream<Arguments> monos() {
        return MONOS_TO_TEST.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("monos")
    void should_replace_error_signal_from_map_into_complete_signal(Mono<String> mono) {
        Mono<String> res = mono
                .<String> map(s -> {
                    throw new RuntimeException();
                })
                // should replace above error and signal completion
                .onErrorComplete();

        assertDoesNotThrow(() -> res.block(TIMEOUT));
    }

    @ParameterizedTest
    @MethodSource("monos")
    void should_replace_error_signal_from_flatMap_into_complete_signal(Mono<String> mono) {
        Mono<String> res = mono
                .<String> flatMap(s -> Mono.error(new RuntimeException()))
                // should replace above error and signal completion
                .onErrorComplete();

        assertDoesNotThrow(() -> res.block(TIMEOUT));
    }

    @ParameterizedTest
    @MethodSource("monos")
    void should_replace_error_signal_from_handle_into_complete_signal(Mono<String> mono) {
        Mono<String> res = mono
                .<String> handle((r, s) -> s.error(new RuntimeException()))
                // should replace above error and signal completion
                .onErrorComplete();

        assertDoesNotThrow(() -> res.block(TIMEOUT));
    }
}
