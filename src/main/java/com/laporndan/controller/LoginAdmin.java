package com.laporndan.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginAdmin", urlPatterns = {"/LoginAdmin"})
public class LoginAdmin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validasi akun admin sesuai permintaan
        if ("admin".equals(username) && "adminlubang123".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("isAdmin", true); // Simpan status login
            response.sendRedirect("index.jsp?status=login_success");
        } else {
            response.sendRedirect("index.jsp?status=login_failed");
        }
    }
}