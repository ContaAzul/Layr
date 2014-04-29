package layr.routing.impl;

import static layr.api.routing.impl.Responses.json;
import static layr.api.routing.impl.Responses.redirectTo;
import static layr.api.routing.impl.Responses.statusCode;
import static layr.api.routing.impl.Responses.template;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import layr.api.http.Connection;
import layr.api.routing.DELETE;
import layr.api.routing.Data;
import layr.api.routing.GET;
import layr.api.routing.POST;
import layr.api.routing.PUT;
import layr.api.routing.PathParameter;
import layr.api.routing.QueryParameter;
import layr.api.routing.QueryParameters;
import layr.api.routing.Response;
import layr.api.routing.WebResource;

@WebResource( "hello" )
public class HelloResource {

        private static final int CREATED = 201;
		private static final double VALUE2 = 5432.1D;
		private static final long VALUE1 = 3336L;
		private static final String REQUEST_PARAM = "requestParam";
		private static final String PATH_PARAM = "pathParam";
		private static final String HELLO_XHTML = "hello.xhtml";

		@GET( "world/{pathParam}" )
        public Response renderThroughResponseBuilder(
                        @PathParameter( PATH_PARAM ) Long pathParam,
                        @QueryParameter( REQUEST_PARAM ) Double requestParam )
        {
            return template( HELLO_XHTML )
                    .set( PATH_PARAM, pathParam )
                    .set( REQUEST_PARAM, requestParam )
                    .build();
        }

        @PUT( "world/{pathParam}" )
        public Response renderThroughTemplateParametersObject(
                        @PathParameter( PATH_PARAM ) Long pathParam,
                        @QueryParameter( REQUEST_PARAM ) Double requestParam ) {
                return template( HELLO_XHTML )
                            .parameterObject( new Hello( pathParam, requestParam ) )
                            .build();
        }

        @POST( "world/{param1}/{param2}" )
        public Response sendRedirectionWithResponseBuilder(
                        @PathParameter( "param1" ) String param1,
                        @PathParameter( "param2" ) Double param2,
                        @QueryParameter( "isSomething" ) Boolean isSomething )
        {
                String url = String.format( "/response/%s/%s/%s/",
                        		param1, param2, isSomething );
                return redirectTo( url ).build();
        }

        @DELETE( "world" )
        public void doSomethingButDoNotRenderTemplate() {
        }

        @GET( "handled/error" )
        public void handledError() {
                throw new NullPointerException();
        }

        @GET( "unhandled/error" )
        public void unhandledError() throws IOException {
                throw new IOException();
        }

        @POST( "world" )
        public Response renderWithHandledData(
                        @Data Connection connection ) {
                return template( HELLO_XHTML )
                        .set( PATH_PARAM, connection.request().url() )
                        .set( REQUEST_PARAM, connection.request().readAsStream() )
                        .build();
        }

        @GET( "json/builder" )
        public Response renderJsonObjectWithBuilder() {
                return json( new Hello( VALUE1, VALUE2 ) ).build();
        }

        @GET( "json/object" )
        public Hello renderJsonObjectWithoutBuilder() {
                return new Hello( VALUE1, VALUE2 );
        }

        @GET( "world/filter/object" )
        public Response renderWithFilterObject(
                        @QueryParameters Hello hello ) {
                return template( HELLO_XHTML )
                        .set( PATH_PARAM, hello.getValue1() )
                        .set( REQUEST_PARAM, hello.getValue2() )
                        .build();
        }

        @PUT( "world/body/object" )
        public Response renderWithBodyObject( Hello helloFilterObject ) {
                return template( HELLO_XHTML )
                        .set( PATH_PARAM, helloFilterObject.getValue1() )
                        .set( REQUEST_PARAM, helloFilterObject.getValue2() )
                        .build();
        }

        @DELETE( "world/status/code" )
        public Response renderDifferentStatusCode() {
                return statusCode( CREATED )
                        .header( "Location", "/blah" )
                        .build();
        }

        @GET( "difference/between/{date}" )
        public Long calcDifferenceBetweenDates(
                        @PathParameter( "date" ) Date date1,
                        @QueryParameter( "date" ) GregorianCalendar date2 ) {
                return date2.getTime().getTime() - date1.getTime();
        }
}