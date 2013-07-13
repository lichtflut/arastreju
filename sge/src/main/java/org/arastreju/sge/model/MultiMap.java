/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Map containing multiple values for a single key.
 * 
 * Created: Jan 2, 2008
 *
 * @author Oliver Tigges
 * 
 */
public class MultiMap<K, V> {
	
	private Map<K, Set<V>> map = new HashMap<K, Set<V>>();
	
	//-----------------------------------------------------
	
	public MultiMap() {
	}
	
	public MultiMap(Map<K, Set<V>> other) {
		this.map.putAll(other);
	}
	
	public MultiMap(MultiMap<K, V> other) {
		this.map.putAll(other.map);
	}
	
	//-----------------------------------------------------

	/**
	 * Adds the value to the set specified by key.
	 */
	public void add(K key, V value){
		getOrCreate(key).add(value);
	}
	
	/**
	 * Adds all values to the set specified by key.
	 */
	public void addAll(K key, Collection<V> values){
		getOrCreate(key).addAll(values);
	}
	
	/**
	 * clears the set specified by key and puts value in it.
	 * @param key
	 * @param value
	 */
	public void put(K key, V value){
		Set<V> set = new HashSet<V>();
		set.add(value);
		map.put(key, set);
	}
	
	/**
	 * clears the set specified by key and puts all given values in it.
	 * @param key
	 * @param values
	 */
	public void putAll(K key, Collection<V> values){
		Set<V> set = new HashSet<V>();
		set.addAll(values);
		map.put(key, set);
	}
	
	// ------------------------------------------------------
	
	public boolean containsKey(K key){
		return map.containsKey(key);
	}
	
	public Set<V> getValues(K key){
		return getOrCreate(key);
	}
	
	public Set<V> allValues(){
		Set<V> all = new HashSet<V>();
		for (Set<V> set : map.values()) {
			all.addAll(set);
		}
		return all;
	}
	
	public Set<K> keySet(){
		return map.keySet();
	}
	
	public Set<V> remove(K key){
		return map.remove(key);
	} 
	
	public boolean remove(K key, V value){
		if (containsKey(key)){
			return map.get(key).remove(value);
		}
		return false;
	} 
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public void clear() {
		map.clear();
	}
	
	// ------------------------------------------------------
	
	/**
	 * returns the size of the keys.
	 */
	public int keySize(){
		return map.size();
	}
	
	/**
	 * returns the sum of all values contained in all sets.
	 * @return
	 */
	public int valueSize(){
		int size = 0;
		for (Set<V> set : map.values()) {
			size += set.size();
		}
		return size;
	}
	
	//-----------------------------------------------------
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	//-----------------------------------------------------
	
	protected Set<V> getOrCreate(K key){
		Set<V> set = map.get(key);
		if (set == null){
			set = new HashSet<V>();
			map.put(key, set);
		}
		return set;
	}
	
}
