package com.laporndan.controller;

import com.laporndan.dao.LaporanDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AksiAdmin", urlPatterns = {"/AksiAdmin"})
public class AksiAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        
        if (action != null && idStr != null) {
            int id = Integer.parseInt(idStr);
            
            if (action.equals("selesai")) {
                LaporanDao.updateStatus(id);
            } else if (action.equals("hapus")) {
                LaporanDao.hapusLaporan(id);
            }
        }
        
        // Kembalikan ke halaman utama setelah aksi selesai
        response.sendRedirect("index.jsp");
    }
}