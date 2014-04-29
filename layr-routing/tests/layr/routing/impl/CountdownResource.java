package layr.routing.impl;

import java.util.concurrent.CountDownLatch;

public interface CountdownResource<T> {

	T countDownLatch( CountDownLatch countDownLatch );
}
