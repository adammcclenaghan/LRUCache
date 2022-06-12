/**
 * The RecencyQueue will always add new items to the head of the queue.
 * Also, retrieving or updating the value of an item will cause that item
 * to be moved to the head of the queue.
 *
 * Therefore, the head of the queue represents the most recently used
 * node, and the tail of the queue represents the least recently used node.
 * @param <T> The type of the object stored in this queue
 */
class RecencyQueue<T> {

    private int currentSize;

    /**
     * Construct an empty RecencyQueue.
     * The first/last values will be null.
     */
    RecencyQueue() {
        this.currentSize = 0;
    }

    /**
     * Appends the value to the start of the queue
     * @param item The item to add to the queue
     */
    public Node<T> add(final T item) {
        final Node<T> node = new Node<>(item, null, null);

        // Setup first/last if the queue is empty
        if (last == null || first == null) {
            first = node;
            last = first;
        }

        setMRU(node);
        currentSize++;
        return node;
    }

    /**
     * Set the most recently used node to the provided node
     * @param node The node to set as the MRU node
     */
    public void setMRU(final Node<T> node) {
        if (node != first) {
            // If this is the last node, ensure we update the last ref before continuing
            if (node == last) {
                last = node.previous;
            }

            node.detach();
            first.previous = node;
            node.next = first;

            first = node;
        }
    }

    /**
     * Update the value for the provided node
     * @param node  The node to update
     * @param value The new value to store in the node
     */
    public void updateNode(final Node<T> node, final T value) {
        node.nodeItem = value;
        setMRU(node);
    }

    /**
     * Remove the least recently used node from the RecencyQueue.
     * @return The evicted node
     */
    public Node<T> evictLRU() {
        final Node<T> oldLast = last;
        /*
        If the queue has a max capacity of 1 then when we evict the LRU
        we are in effect evicting the first & last node. We should null out the first & last
        references in that case so that next item which is added can set itself to first & last
         */
        if (first == last) {
            // Capacity 1 queue
            first = null;
            last = null;
        }
        else {
            last = last.previous;
        }

        currentSize--;
        return oldLast;
    }


    public int getSize() {
        return currentSize;
    }


    private Node<T> first;
    private Node<T> last;

    /**
     * A node in the queue.
     *
     * A node holds:
     * - A value
     * - A reference to the next node
     * - A reference to the previous node
     * @param <T> Type for the value held by this node
     */
    static class Node<T> {

        public Node(final T value, final Node<T> previous, final Node<T> next) {
            this.nodeItem = value;
            this.previous = previous;
            this.next = next;
        }

        public T getNodeItem() {
            return nodeItem;
        }

        /**
         * Detach this node from its neighbouring nodes.
         */
        public void detach() {
            if (previous != null)
                previous.next = next;
            if (next != null)
                next.previous = previous;

            next = null;
            previous = null;
        }

        public String toString() {
            return nodeItem.toString();
        }

        private T nodeItem;
        private Node<T> next;
        private Node<T> previous;
    }
}
