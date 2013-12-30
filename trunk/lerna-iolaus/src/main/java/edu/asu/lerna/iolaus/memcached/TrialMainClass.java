package edu.asu.lerna.iolaus.memcached;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;

public class TrialMainClass {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		MemcachedClient memcachedClient = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11212 127.0.0.1:11211"));
		
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
		
		
		
		MemcachedClient memcachedClient1 = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11212 127.0.0.1:11211"));
		System.out.println(memcachedClient1.get("10"));
		GetFuture<Object> returnedFutureObject = memcachedClient1.asyncGet("2");
		Object returnedObject = null;
		try {
			returnedObject = returnedFutureObject.get(5, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(returnedObject);
		

	}

}
