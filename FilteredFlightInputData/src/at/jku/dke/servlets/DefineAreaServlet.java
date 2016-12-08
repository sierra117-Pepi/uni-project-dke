package at.jku.dke.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/defineAreaServlet")
public class DefineAreaServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Data: " + request.getParameter("mapp"));
		response.sendRedirect("http://localhost:2000/FilteredFlightInputData/JSP-Files/flight_area.jsp");
	}
}
