// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.gbcc;

import java.util.*;

/**
 * Generates an ASCII graphic.
 * <p>
 * The graphic is diagram-like consisting of circles and lines.
 */
public final class Graphic {

    /**
     * Coordniates of a cell within the grid.
     */
    private static final class Coord {

        /**
         * The row within the grid.
         */
        public final int row;

        /**
         * The column within the grid.
         */
        public final int col;

        /**
         * Constructs a Coord with the given row and column.
         *
         * @param row The row within the grid.
         * @param col The column within the grid.
         */
        public Coord(final int row, final int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Character representing an empty cell in the grid.
     */
    private static final char EMPTY = ' ';

    /**
     * The width of the grid.
     */
    public final int width;

    /**
     * The height of the grid.
     */
    public final int height;

    /**
     * Number of nodes in the graphic.
     */
    public final int nodeCount;

    /**
     * Grid that acts as a canvas for the graphic.
     */
    private final char[][] grid;

    /**
     * Creates a graphic with the given width, height, and number of nodes.
     *
     * @param width The width of the grid.
     * @param height The height of the grid.
     * @param nodeCount Number of nodes in the graphic.
     */
    public Graphic(final int width, final int height, final int nodeCount) {
        this.width = width;
        this.height = height;
        this.nodeCount = nodeCount;
        grid = new char[height][width];
        draw();
    }

    /**
     * Draws the graphic.
     * <p>
     * Example:
     * o
     *  \   o-----o            o------o
     *   \   \                /
     *    \   \              /
     *     o----o---o------o-----o
     */
    private void draw() {
        // Clear canvas.
        for (int row = 0; row < height; ++row) {
            Arrays.fill(grid[row], EMPTY);
        }
        // Draw.
        final Random random = new Random();
        final List<Coord> nodes = new ArrayList<>();
        final Coord origin = new Coord(random.nextInt(height), random.nextInt(width));
        nodes.add(origin);
        grid[origin.row][origin.col] = 'o';
        while (nodes.size() < nodeCount) {
            final Coord coord = nodes.get(random.nextInt(nodes.size()));
            switch (random.nextInt(8)) {
                case 0 -> {
                    // Up.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, -1, 0, length)) {
                        drawPath(coord, -1, 0, length, '|');
                        nodes.add(new Coord(coord.row - length, coord.col));
                    }
                }
                case 1 -> {
                    // Down.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, +1, 0, length)) {
                        drawPath(coord, +1, 0, length, '|');
                        nodes.add(new Coord(coord.row + length, coord.col));
                    }
                }
                case 2 -> {
                    // Left.
                    final int length = random.nextInt(4) + 4;
                    if (isPathOpen(coord, 0, -1, length)) {
                        drawPath(coord, 0, -1, length, '-');
                        nodes.add(new Coord(coord.row, coord.col - length));
                    }
                }
                case 3 -> {
                    // Right.
                    final int length = random.nextInt(4) + 4;
                    if (isPathOpen(coord, 0, +1, length)) {
                        drawPath(coord, 0, +1, length, '-');
                        nodes.add(new Coord(coord.row, coord.col + length));
                    }
                }
                case 4 -> {
                    // Up-Left.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, -1, -1, length)) {
                        drawPath(coord, -1, -1, length, '\\');
                        nodes.add(new Coord(coord.row - length, coord.col - length));
                    }
                }
                case 5 -> {
                    // Down-Right.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, +1, +1, length)) {
                        drawPath(coord, +1, +1, length, '\\');
                        nodes.add(new Coord(coord.row + length, coord.col + length));
                    }
                }
                case 6 -> {
                    // Up-Right.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, -1, +1, length)) {
                        drawPath(coord, -1, +1, length, '/');
                        nodes.add(new Coord(coord.row - length, coord.col + length));
                    }
                }
                case 7 -> {
                    // Down-Left.
                    final int length = random.nextInt(3) + 2;
                    if (isPathOpen(coord, +1, -1, length)) {
                        drawPath(coord, +1, -1, length, '/');
                        nodes.add(new Coord(coord.row + length, coord.col - length));
                    }
                }
            }
        }
    }

    /**
     * Tests is a path can be traced from coord in the given direction for the specified length.
     *
     * @param coord Starting coordinates.
     * @param rowInc Vertical direction (should be -1, 0, or +1).
     * @param colInc Horizontal direction (should be -1, 0, or +1).
     * @param length Length of path.
     * @return True if a path can be traced.
     */
    private boolean isPathOpen(final Coord coord, final int rowInc, final int colInc, final int length) {
        int row = coord.row;
        int col = coord.col;
        for (int i = 0; i < length; ++i) {
            row += rowInc;
            col += colInc;
            if (!isInBounds(row, col)) {
                return false;
            }
            if (i == (length - 1)) {
                for (int y = -1; y < 2; ++y) {
                    for (int x = -2; x < 3; ++x) {
                        final int areaRow = row + y;
                        final int areaCol = col + x;
                        if (isInBounds(areaRow, areaCol) && (grid[areaRow][areaCol] != EMPTY)) {
                            return false;
                        }
                    }
                }
            } else {
                if (grid[row][col] != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the given row and column are within the grid.
     *
     * @param row Row in grid.
     * @param col Column in grid.
     * @return True if the coordinates are within the grid.
     */
    private boolean isInBounds(final int row, final int col) {
        return (row >= 0) && (row < height) && (col >= 0) && (col < width);
    }

    /**
     * Draws a path on the grid. The path is composed of lines and ends in a circle.
     *
     * @param coord Starting coordinates.
     * @param rowInc Vertical direction (should be -1, 0, or +1).
     * @param colInc Horizontal direction (should be -1, 0, or +1).
     * @param length Length of path.
     * @param ch Character to draw as line.
     */
    private void drawPath(final Coord coord, final int rowInc, final int colInc, final int length, final char ch) {
        int row = coord.row;
        int col = coord.col;
        for (int i = 0; i < length; ++i) {
            row += rowInc;
            col += colInc;
            grid[row][col] = i == (length - 1) ? 'o' : ch;
        }
    }

    /**
     * Converts the grid canvas into an array of strings.
     *
     * @return An array of strings.
     */
    public String[] toStringArray() {
        final String[] graphic = new String[height];
        for (int row = 0; row < height; ++row) {
            graphic[row] = String.copyValueOf(grid[row]);
        }
        return graphic;
    }
}
