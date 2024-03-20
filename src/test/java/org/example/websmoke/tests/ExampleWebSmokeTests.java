package org.example.websmoke.tests;

import org.example.websmoke.engine.GET;
import org.example.websmoke.engine.WebSmokeTest;


// tag::for_article[]

@WebSmokeTest
public class ExampleWebSmokeTests {

    @GET(expected = 200)
    public static String shouldReturn200 = "https://blogs.oracle.com/javamagazine/";

    @GET(expected = 401)
    public static String shouldReturn401 = "https://httpstat.us/401";

    @GET(expected = 201)
    public static String expect201ButGet404 = "https://httpstat.us/404";

}

// end::for_article[]
