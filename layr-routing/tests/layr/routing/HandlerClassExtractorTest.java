package layr.routing;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import layr.engine.RequestContext;
import layr.routing.api.ApplicationContext;
import layr.routing.api.DataProvider;
import layr.routing.api.ExceptionHandler;
import layr.routing.api.Handler;
import layr.routing.api.Response;
import layr.routing.impl.RequestContextDataProvider;
import layr.routing.lifecycle.HandlerClassExtractor;

public class HandlerClassExtractorTest {

	@SuppressWarnings("rawtypes")
	HandlerClassExtractor<ExceptionHandler> exceptionHandlerExtractor;

	@SuppressWarnings("rawtypes")
	HandlerClassExtractor<DataProvider> handlerClassExtractor;

	public HandlerClassExtractorTest() {
		exceptionHandlerExtractor = HandlerClassExtractor
				.newInstance(ExceptionHandler.class);
		handlerClassExtractor = HandlerClassExtractor
				.newInstance(DataProvider.class);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void grantThatExtractDataAsExpected() {
		exceptionHandlerExtractor.extract(ValidHandler.class);
		exceptionHandlerExtractor.extract(InvalidHandler.class);
		Map<String, Class<ExceptionHandler>> handlers = exceptionHandlerExtractor
				.getRegisteredHandlers();
		Assert.assertNotNull(handlers);
		Assert.assertEquals(1, handlers.size());
		Assert.assertEquals(ValidHandler.class,
				handlers.get(Throwable.class.getCanonicalName()));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void grantThatExtractDataProviderAsExpected() {
		handlerClassExtractor.extract(RequestContextDataProvider.class);
		Map<String, Class<DataProvider>> handlers = handlerClassExtractor
				.getRegisteredHandlers();
		Assert.assertNotNull(handlers);
		Assert.assertEquals(1, handlers.size());
		Assert.assertEquals(RequestContextDataProvider.class,
				handlers.get(RequestContext.class.getCanonicalName()));
	}

	interface FakeHandler {}

	class InvalidHandler implements FakeHandler {}

	@Handler
	class ValidHandler implements ExceptionHandler<Throwable>, FakeHandler {
		@Override
		public Response render(Throwable exception,
				ApplicationContext appContext, RequestContext reqContext) {
			return null;
		}
	}
}
