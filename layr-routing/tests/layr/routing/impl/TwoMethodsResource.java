package layr.routing.impl;

import static layr.api.routing.impl.Responses.render;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import layr.api.routing.CookieParameter;
import layr.api.routing.Data;
import layr.api.routing.GET;
import layr.api.routing.HeaderParameter;
import layr.api.routing.POST;
import layr.api.routing.PathParameter;
import layr.api.routing.Produces;
import layr.api.routing.QueryParameter;
import layr.api.routing.Response;
import layr.api.routing.WebResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors( fluent = true )
@WebResource( "hello" )
@Produces( "plain/text" )
public class TwoMethodsResource
		implements CountdownResource<TwoMethodsResource> {

	CountDownLatch countDownLatch;

	@GET( "world/{pathParam}" )
	// @Checkstyle:OFF I'm testing multiple parameters injection.
	public Response renderThroughResponseBuilder(
			@PathParameter( "pathParam" ) Long pathParam,
			@QueryParameter( "requestParam" ) Double requestParam,
			@HeaderParameter( "Last-Modified-Since" ) Date lastModifiedSince,
			@CookieParameter( "token" ) BigDecimal token,
			Hello hello,
			@Data Hello hello2 ) {
		countDown();
		return render( hello2, "text/plain" )
				.set( "pathParam", pathParam )
				.build();
	}

	private void countDown() {
		if ( this.countDownLatch != null )
			this.countDownLatch.countDown();
	}
	
	@POST( "world" )
	@Produces( "application/json" )
	public void failure() throws FailureException{
		throw new FailureException(countDownLatch);
	}

	@Getter
	@Accessors( fluent=true )
	@RequiredArgsConstructor
	public class FailureException extends Exception {

		private static final long serialVersionUID = -520710984076057382L;
		final CountDownLatch countDownLatch;
		
	}
}