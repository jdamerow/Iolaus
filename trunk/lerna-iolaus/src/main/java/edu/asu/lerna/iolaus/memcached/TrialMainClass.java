package edu.asu.lerna.iolaus.memcached;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class TrialMainClass {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		MemcachedClient memcachedClient = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
		
		OperationFuture<Boolean> result = memcachedClient.set("1", 0, "One");
		System.out.println(result.get());
		result = memcachedClient.set("10", 0, "TEN");
		System.out.println(result.get());
		result = memcachedClient.set("2", 0, "Two");
		System.out.println(result.get());
		result = memcachedClient.set("3", 0, "Three");
		System.out.println(result.get());
		result = memcachedClient.set("4", 0, "Four");
		System.out.println(result.get());
		System.out.println("Inserted into cache");
		System.out.println(memcachedClient.get("10"));
		

	}

}
