package info5.sar.EventBasedChannel.Impl;

/**
 * CircularBuffer is a thread-safe circular buffer implementation.
 * It supports basic operations like push and pull to add and remove bytes from the buffer.
 * The buffer has a fixed capacity and uses volatile variables to ensure visibility of changes across threads.
 * 
 * <p>Methods:
 * <ul>
 *   <li>{@link #full()} - Checks if the buffer is full.</li>
 *   <li>{@link #empty()} - Checks if the buffer is empty.</li>
 *   <li>{@link #push(byte)} - Adds a byte to the buffer. Throws IllegalStateException if the buffer is full.</li>
 *   <li>{@link #pull()} - Retrieves and removes the next available byte from the buffer. Throws IllegalStateException if the buffer is empty.</li>
 * </ul>
 * </p>
 * 
 * <p>Fields:
 * <ul>
 *   <li>{@code m_tail} - The index of the tail of the buffer.</li>
 *   <li>{@code m_head} - The index of the head of the buffer.</li>
 *   <li>{@code m_bytes} - The array that stores the bytes in the buffer.</li>
 * </ul>
 * </p>
 * 
 */
public class CircularBuffer {

		  volatile int m_tail, m_head;
		  volatile byte m_bytes[];

		  public CircularBuffer(int capacity) {
		    m_bytes = new byte[capacity];
		    m_tail = m_head = 0;
		  }

		  /**
		   * @return true if this buffer is full, false otherwise
		   */
		  public boolean full() {
		    int next = (m_head + 1) % m_bytes.length;
		    return (next == m_tail);
		  }

		  /**
		   * @return true if this buffer is empty, false otherwise
		   */
		  public boolean empty() {
		    return (m_tail == m_head);
		  }

		  /**
		   * @param b: the byte to push in the buffer
		   * @return the next available byte
		   * @throws an IllegalStateException if full.
		   */
		  public void push(byte b) {
		    int next = (m_head + 1) % m_bytes.length;
		    if (next == m_tail)
		      throw new IllegalStateException();
		    m_bytes[m_head] = b;
		    m_head = next;
		  }

		  /**
		   * @return the next available byte
		   * @throws an IllegalStateException if empty.
		   */
		  public byte pull() {
		    if (m_tail == m_head)
		      throw new IllegalStateException();
		    int next = (m_tail + 1) % m_bytes.length;
		    byte bits = m_bytes[m_tail];
		    m_tail = next;
		    return bits;
		  }

		
}
