package project;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class HelloServelt
 */
public class HelloServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;  
   
    public HelloServelt() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//example /hi?query=nba&type=text  
		Search index = new Search();
		//index.buildIndex(); //build index 
		String query=request.getParameter("query");
		String type=request.getParameter("type");  //text or hashtags
		JSONArray results = new JSONArray();
		try {
			results = index.testQuery(query, type);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.getWriter().write(results);
		PrintWriter out=response.getWriter();
		out.print(results);
//		PrintWriter out=response.getWriter();
//		out.print("Done");
	}

	
}
