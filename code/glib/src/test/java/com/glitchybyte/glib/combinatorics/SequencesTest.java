// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Combinatorics - Sequences")
public class SequencesTest {

    private static final class SequenceTestCase {

        public final String[] sequence;
        public final boolean expectedResult;
        public boolean result = false;

        public SequenceTestCase(final String[] sequence, final boolean expectedResult) {
            this.sequence = sequence;
            this.expectedResult = expectedResult;
        }

        public boolean didPass() {
            return result == expectedResult;
        }
    }

    private boolean isSame(final String[] sequence1, final String[] sequence2, final boolean ordered) {
        if (ordered) {
            return Arrays.equals(sequence1, sequence2);
        }
        if (sequence1.length != sequence2.length) {
            return false;
        }
        final Map<String, Integer> items = new HashMap<>(sequence1.length);
        for (int i = 0; i < sequence1.length; ++i) {
            final String item1 = sequence1[i];
            items.put(item1, items.getOrDefault(item1, 0) + 1);
            final String item2 = sequence2[i];
            items.put(item2, items.getOrDefault(item2, 0) - 1);
        }
        return items.values().stream().allMatch(item -> item == 0);
    }

    @Nested
    @DisplayName("Sequences - Combinations")
    public class Combinations {

        private void runTestCases(final GCombinations<String> generator, final SequenceTestCase[] testCases, final boolean ordered) {
            for (final List<String> sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence.toArray(new String[0]), ordered)) {
                        testCase.result = true;
                    }
                }
            }
            for (final SequenceTestCase testCase: testCases) {
                assertTrue(testCase.didPass(),
                        Arrays.toString(testCase.sequence) + (testCase.result ? " was found" : " was not found"));
            }
        }

        @Test
        @DisplayName("Creation")
        public void testCreation() {
            // We only test to make sure constructors are implemented.
            final String[] items = { "one", "two", "three" };
            assertDoesNotThrow(() -> new GCombinations<>(items, false, 2, 3));
            assertDoesNotThrow(() -> new GCombinations<>(items, true));
        }

        @Test
        @DisplayName("Generation without duplicates")
        public void testGenerationWithoutDuplicates() {
            final String[] items = { "one", "two", "three" };
            final GCombinations<String> generator = new GCombinations<>(items, false, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new String[] { items[0], items[0], items[0] }, false),
                    new SequenceTestCase(new String[] { items[0], items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[0], items[1], items[1] }, false)
            };
            runTestCases(generator, testCases, false);
        }

        @Test
        @DisplayName("Generation with duplicates")
        public void testGenerationWithDuplicates() {
            final String[] items = { "one", "two", "three" };
            final GCombinations<String> generator = new GCombinations<>(items, true, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new String[] { items[0], items[0], items[0] }, true),
                    new SequenceTestCase(new String[] { items[0], items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[1], items[2] }, true),
                    new SequenceTestCase(new String[] { items[0], items[1], items[0] }, true)
            };
            runTestCases(generator, testCases, false);
        }
    }

    @Nested
    @DisplayName("Sequences - Permutations")
    public class Permutations {

        private void runTestCases(final GPermutations<String> generator, final SequenceTestCase[] testCases, final boolean ordered) {
            for (final List<String> sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence.toArray(new String[0]), ordered)) {
                        testCase.result = true;
                    }
                }
            }
            for (final SequenceTestCase testCase: testCases) {
                assertTrue(testCase.didPass(),
                        Arrays.toString(testCase.sequence) + (testCase.result ? " was found" : " was not found"));
            }
        }

        @Test
        @DisplayName("Creation")
        public void testCreation() {
            // We only test to make sure constructors are implemented.
            final String[] items = { "one", "two", "three" };
            assertDoesNotThrow(() -> new GPermutations<>(items, false, 2, 3));
            assertDoesNotThrow(() -> new GPermutations<>(items, true));
        }

        @Test
        @DisplayName("Generation without duplicates")
        public void testGenerationWithoutDuplicates() {
            final String[] items = { "one", "two", "three" };
            final GPermutations<String> generator = new GPermutations<>(items, false, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new String[] { items[0], items[0], items[0] }, false),
                    new SequenceTestCase(new String[] { items[0], items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[0], items[1], items[0] }, false)
            };
            runTestCases(generator, testCases, true);
        }

        @Test
        @DisplayName("Generation with duplicates")
        public void testGenerationWithDuplicates() {
            final String[] items = { "one", "two", "three" };
            final GPermutations<String> generator = new GPermutations<>(items, true, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new String[] { items[0], items[0], items[0] }, true),
                    new SequenceTestCase(new String[] { items[0], items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[2], items[1] }, true),
                    new SequenceTestCase(new String[] { items[0], items[1], items[0] }, true)
            };
            runTestCases(generator, testCases, true);
        }
    }
}
