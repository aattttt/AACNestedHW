package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author A.J. Trimble
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> clone = new AssociativeArray<>();
    clone.size = this.size;
    int temp = this.size;
    while (temp > DEFAULT_CAPACITY) {
      temp -= DEFAULT_CAPACITY;
      clone.expand();
    } // whil
    for (int i = 0; i < this.size; i++) {
      clone.pairs[i] = this.pairs[i].clone();
    } // for
    return clone;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String output = "";
    for (int i = 0; i < this.size; i++) {
      output = output + this.pairs[i].key + ":" + this.pairs[i].val + ", ";
    } // for
    return "{" + output.substring(0, (output.length() - 2)) + "}";
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *   The key whose value we are seeting.
   * @param value
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException("Null key is invalid.");
    } // if
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        this.pairs[i].val = value;
        return;
      } // if
    } // for
    if (this.size == this.pairs.length) {
      expand();
    } // if
    this.pairs[this.size] = new KVPair<K, V>(key, value);
    this.size++;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @return
   *   The corresponding value
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException("Null key is invalid.");
    } // if
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return this.pairs[i].val;
      } // if
    } // for
    throw new KeyNotFoundException("The key was not found");
  } // get(K)

  public String[] keyList() {
    String[] keyList = new String[this.size];
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(null)) {
        keyList[i] = (String) this.pairs[i].key;
      } // if
    } // for
    return keyList;
  }

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key, since it cannot appear.
   *
   * @param key
   *   The key we're looking for.
   *
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return true;
      } // if
    } // for
    return false;
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   *   The key to remove.
   */
  public void remove(K key) {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        for (; i < this.size - 1; i++) {
          this.pairs[i] = this.pairs[i + 1];
        } // for
        this.pairs[this.size - 1] = null;
        this.size = this.size - 1;
        return;
      } // if
    } // for
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @return
   *   The index of the key, if found.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException(); 
  } // find(K)

} // class AssociativeArray


