package layr.routing.sample;

import java.io.IOException;

import static layr.api.ResponseBuilder.*;
import layr.api.*;

@WebResource("hello")
public class HelloResource {

	@GET("world/{pathParam}")
	public Response renderThroughResponseBuilder(
		@PathParameter("pathParam") Long pathParam,
		@QueryParameter("requestParam") Double requestParam )
	{
		return renderTemplate( "hello.xhtml" )
				.set("pathParam", pathParam)
				.set("requestParam", requestParam);
	}

	@PUT("world/{pathParam}")
	public Response renderThroughTemplateParametersObject(
			@PathParameter("pathParam") Long pathParam,
			@QueryParameter("requestParam") Double requestParam)
	{
		return renderTemplate("hello.xhtml")
				.parameters( new Hello(pathParam, requestParam) );
	}

	@POST("world/{param1}/{param2}")
	public Response sendRedirectionWithResponseBuilder(
			@PathParameter("param1") String param1,
			@PathParameter("param2") Double param2,
			@QueryParameter("isSomething") Boolean isSomething)
	{
		String url = String.format("/response/%s/%s/%s/",
				param1, param2, isSomething);
		return redirectTo( url );
	}

	@DELETE("world")
	public void doSomethingButDoNotRenderTemplate(){}

	@GET("handled/error")
	public void handledError(){
		throw new NullPointerException();
	}

	@GET("unhandled/error")
	public void unhandledError() throws IOException{
		throw new IOException();
	}
	
	@POST("world")
	public Response renderWithHandledData(
			@Data RequestContext requestContext ){
		return renderTemplate("hello.xhtml")
				.set("pathParam", requestContext.getApplicationRootPath())
				.set("requestParam", requestContext.getDefaultResource());
	}
}
