// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Combinatorics - Coded sequences")
public class CodedSequencesTest {

    private static final class MockSequencesGenerator extends GCodedSequencesGenerator {

        public MockSequencesGenerator(final int itemCount, final boolean allowDuplicateItems, final int minSize, final int maxSize) {
            super(itemCount, allowDuplicateItems, minSize, maxSize);
        }

        public MockSequencesGenerator(final int itemCount, final boolean allowDuplicateItems) {
            super(itemCount, allowDuplicateItems);
        }

        @Override
        protected boolean incCode(final int[] code) {
            return false;
        }
    }

    @Test
    @DisplayName("Creation of sequence generator")
    public void testCreation() {
        assertDoesNotThrow(
                () -> new MockSequencesGenerator(3, false, 2, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new MockSequencesGenerator(0, false, 2, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new MockSequencesGenerator(3, false, 0, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new MockSequencesGenerator(3, false, 4, 3));
        assertDoesNotThrow(
                () -> new MockSequencesGenerator(3, true, 2, 4));
        assertThrows(IllegalArgumentException.class,
                () -> new MockSequencesGenerator(3, false, 2, 4));
        assertDoesNotThrow(
                () -> new MockSequencesGenerator(3, false));
        assertThrows(IllegalArgumentException.class,
                () -> new MockSequencesGenerator(0, false));
    }

    private static final class SequenceTestCase {

        public final int[] sequence;
        public final boolean expectedResult;
        public boolean result = false;

        public SequenceTestCase(final int[] sequence, final boolean expectedResult) {
            this.sequence = sequence;
            this.expectedResult = expectedResult;
        }

        public boolean didPass() {
            return result == expectedResult;
        }
    }

    private boolean isSame(final int[] sequence1, final int[] sequence2, final boolean ordered) {
        if (ordered) {
            return Arrays.equals(sequence1, sequence2);
        }
        if (sequence1.length != sequence2.length) {
            return false;
        }
        final Map<Integer, Integer> items = new HashMap<>(sequence1.length);
        for (int i = 0; i < sequence1.length; ++i) {
            final int item1 = sequence1[i];
            items.put(item1, items.getOrDefault(item1, 0) + 1);
            final int item2 = sequence2[i];
            items.put(item2, items.getOrDefault(item2, 0) - 1);
        }
        return items.values().stream().allMatch(item -> item == 0);
    }

    @Nested
    @DisplayName("Coded sequences - Combinations")
    public class Combinations {

        @Test
        @DisplayName("Creation")
        public void testCreation() {
            // We only test to make sure constructors are implemented.
            assertDoesNotThrow(
                    () -> new GCodedCombinations(3, false, 2, 3));
            assertDoesNotThrow(
                    () -> new GCodedCombinations(3, true));
        }

        @Test
        @DisplayName("Generation without duplicates")
        public void testGenerationWithoutDuplicates() {
            final GCodedCombinations generator = new GCodedCombinations(5, false, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new int[] { 0, 0, 0 }, false),
                    new SequenceTestCase(new int[] { 0, 4, 2 }, true),
                    new SequenceTestCase(new int[] { 4, 3 }, true),
                    new SequenceTestCase(new int[] { 0, 1, 1 }, false)
            };
            for (final int[] sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence, false)) {
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
        @DisplayName("Generation with duplicates")
        public void testGenerationWithDuplicates() {
            final GCodedCombinations generator = new GCodedCombinations(5, true, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new int[] { 0, 0, 0 }, true),
                    new SequenceTestCase(new int[] { 0, 4, 2 }, true),
                    new SequenceTestCase(new int[] { 4, 3 }, true),
                    new SequenceTestCase(new int[] { 0, 1, 0 }, true)
            };
            for (final int[] sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence, false)) {
                        testCase.result = true;
                    }
                }
            }
            for (final SequenceTestCase testCase: testCases) {
                assertTrue(testCase.didPass(),
                        Arrays.toString(testCase.sequence) + (testCase.result ? " was found" : " was not found"));
            }
        }
    }

    @Nested
    @DisplayName("Coded sequences - Permutations")
    public class Permutations {

        @Test
        @DisplayName("Creation")
        public void testCreation() {
            // We only test to make sure constructors are implemented.
            assertDoesNotThrow(
                    () -> new GCodedPermutations(3, false, 2, 3));
            assertDoesNotThrow(
                    () -> new GCodedPermutations(3, true));
        }

        @Test
        @DisplayName("Generation without duplicates")
        public void testGenerationWithoutDuplicates() {
            final GCodedPermutations generator = new GCodedPermutations(5, false, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new int[] { 0, 0, 0 }, false),
                    new SequenceTestCase(new int[] { 0, 4, 2 }, true),
                    new SequenceTestCase(new int[] { 4, 3 }, true),
                    new SequenceTestCase(new int[] { 0, 1, 0 }, false)
            };
            for (final int[] sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence, true)) {
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
        @DisplayName("Generation with duplicates")
        public void testGenerationWithDuplicates() {
            final GCodedPermutations generator = new GCodedPermutations(5, true, 2, 3);
            final SequenceTestCase[] testCases = {
                    new SequenceTestCase(new int[] { 0, 0, 0 }, true),
                    new SequenceTestCase(new int[] { 0, 4, 2 }, true),
                    new SequenceTestCase(new int[] { 4, 3 }, true),
                    new SequenceTestCase(new int[] { 0, 1, 0 }, true)
            };
            for (final int[] sequence: generator) {
                for (final SequenceTestCase testCase: testCases) {
                    if (isSame(testCase.sequence, sequence, true)) {
                        testCase.result = true;
                    }
                }
            }
            for (final SequenceTestCase testCase: testCases) {
                assertTrue(testCase.didPass(),
                        Arrays.toString(testCase.sequence) + (testCase.result ? " was found" : " was not found"));
            }
        }
    }
}
