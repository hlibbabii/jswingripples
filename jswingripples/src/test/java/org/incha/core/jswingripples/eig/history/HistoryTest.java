package org.incha.core.jswingripples.eig.history;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HistoryTest {
    /**
     * list of actions.
     */
    private final LinkedList<Integer> actions = new LinkedList<Integer>();
    private final History history = new History();

    class ValueAdded implements UndoAction {
        final int value;
        public ValueAdded(final int value) {
            super();
            this.value = value;
        }
        /* (non-Javadoc)
         * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
         */
        @Override
        public UndoAction undo() {
            actions.removeLast();
            return new ValueRemoved(value);
        }
    }
    class ValueRemoved implements UndoAction {
        final int value;
        public ValueRemoved(final int value) {
            super();
            this.value = value;
        }
        /* (non-Javadoc)
         * @see org.incha.core.jswingripples.eig.history.UndoAction#undo()
         */
        @Override
        public UndoAction undo() {
            actions.add(value);
            return new ValueAdded(value);
        }
    }

    @Test
    public void testHistory() {
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);

        history.undo();
        history.undo();
        history.undo();

        assertTrue(history.canUndo());
        assertTrue(history.canRedo());

        history.redo();
        history.redo();

        //check sequence
        assertEquals(5, actions.size());
        assertEquals(new Integer(1), actions.get(0));
        assertEquals(new Integer(2), actions.get(1));
        assertEquals(new Integer(3), actions.get(2));
        assertEquals(new Integer(4), actions.get(3));
        assertEquals(new Integer(5), actions.get(4));

        history.redo();

        assertTrue(history.canUndo());
        assertFalse(history.canRedo());

        history.undo();
        history.undo();
        history.undo();
        history.undo();
        history.undo();

        assertTrue(history.canUndo());
        assertTrue(history.canRedo());

        history.undo();

        assertFalse(history.canUndo());
        assertTrue(history.canRedo());

        history.redo();
        history.redo();

        //check sequence
        assertEquals(actions.size(), 2);
        assertEquals(new Integer(1), actions.get(0));
        assertEquals(new Integer(2), actions.get(1));
    }

    @Test
    public void testIgnoresEmptyQueue() {
        //check not throws any exceptions
        history.undo();
        history.redo();
    }
    /**
     * @param value
     */
    private void add(final int value) {
        actions.add(value);
        history.add(new ValueAdded(value));
    }
}
