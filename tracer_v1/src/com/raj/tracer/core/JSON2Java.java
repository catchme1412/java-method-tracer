package com.raj.tracer.core;

import java.util.concurrent.atomic.AtomicReference;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSON2Java {

	private static final ScriptEngine jsonParser;

	static {
		try {
			String init = "toJava = function(o) {  return o == null ? null : o.toJava();};Object.prototype.toJava = function() {  var m = new java.util.HashMap();  for (var key in this)    if (this.hasOwnProperty(key))      m.put(key, toJava(this[key]));  return m;};Array.prototype.toJava = function() {  var l = this.length;  var a = new java.lang.reflect.Array.newInstance(java.lang.Object, l);  for (var i = 0;i < l;i++)    a[i] = toJava(this[i]);  return a;};String.prototype.toJava = function() {  return new java.lang.String(this);};Boolean.prototype.toJava = function() {  return java.lang.Boolean.valueOf(this);};";
//			String init = "function(o) {  return o == null ? null : o.toJava();};Object.prototype.toJava = function() {return 2;};";
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
			engine.eval(init);
			jsonParser = engine;
		} catch (Exception e) {
			// Unexpected
			throw new AssertionError(e);
		}
	}

	public static Object parseJSON(String json) {
		try {
			String eval = "new java.util.concurrent.atomic.AtomicReference(toJava((" + json + ")))";
			AtomicReference ret = (AtomicReference) jsonParser.eval(eval);
			return ret.get();
		} catch (ScriptException e) {
			throw new RuntimeException("Invalid json", e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(parseJSON("{'Observable'{}}").getClass());
		
	}
}